package com.inspur.seed.service.invested.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.input.domain.inventory.Stock;
import com.inspur.agriculture.input.domain.inventory.Warehouse;
import com.inspur.agriculture.input.mapper.AgriInputMapper;
import com.inspur.agriculture.input.mapper.inventory.StockMapper;
import com.inspur.agriculture.input.mapper.inventory.WarehouseMapper;
import com.inspur.common.core.domain.model.LoginUser;
import com.inspur.common.utils.LoginHelper;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.uuid.IdUtils;
import com.inspur.seed.domain.invested.*;
import com.inspur.seed.dto.invested.InputReleaseDTO;
import com.inspur.seed.dto.invested.InputReleaseDetailDTO;
import com.inspur.seed.mapper.invested.*;
import com.inspur.seed.service.invested.IInputReleaseService;
import org.apache.ibatis.executor.ExecutorException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投入品分发Service实现
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@Service
public class InputReleaseServiceImpl extends ServiceImpl<InputReleaseMainMapper, InputReleaseMain>
        implements IInputReleaseService {

    @Resource
    private InputReleaseDetailMapper detailMapper;

    @Resource
    private InputReceiveUnionMapper receiveUnionMapper;

    @Resource
    private InputReceiveWoredaMapper receiveWoredaMapper;

    @Resource
    private AgriInputMapper agriInputMapper;

    @Resource
    private StockMapper stockMapper;

    @Resource
    private WarehouseMapper warehouseMapper;

    @Resource
    private InputReleaseMainMapper inputReleaseMainMapper;

    @Resource
    private InputReleaseFarmerMainMapper inputReleaseFarmerMainMapper;

    @Resource InputReleaseFarmerDetailMapper inputReleaseFarmerDetailMapper;

    @Override
    public List<InputReleaseMain> queryReleaseList(String releaseType, String unionName, String inputType,
                                                   LocalDate startTime, LocalDate endTime) {
        LambdaQueryWrapper<InputReleaseMain> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InputReleaseMain::getReleaseType, releaseType);

        if (StringUtils.isNotEmpty(unionName)) {
            wrapper.like(InputReleaseMain::getTargetId, unionName);
        }
        if (startTime != null) {
            wrapper.ge(InputReleaseMain::getReleaseDate, LocalDateTime.of(startTime, LocalTime.MIN));
        }
        if (endTime != null) {
            wrapper.le(InputReleaseMain::getReleaseDate, LocalDateTime.of(endTime, LocalTime.MAX));
        }

        wrapper.orderByDesc(InputReleaseMain::getReleaseDate);

        return list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> addRelease(InputReleaseDTO dto) {
        // 生成分发单编号
        String releaseId = "REL" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + IdUtils.fastSimpleUUID().substring(0, 6).toUpperCase();
        String releaseType = dto.getReleaseType();
        // 保存主表
        InputReleaseMain main = new InputReleaseMain();
        BeanUtils.copyProperties(dto, main);
        main.setId(IdUtils.fastSimpleUUID());
        main.setReleaseId(releaseId);
        main.setOperateBy("admin"); // TODO: 从登录用户获取
        main.setOperateTime(LocalDateTime.now());
        main.setCreateTime(LocalDateTime.now());
        main.setReleaseDate(dto.getReleaseDate());
        main.setAuditDate(LocalDateTime.now());
        main.setStatus("distributed");
        main.setReleaseType(releaseType);
        main.setAuditBy(SecurityUtils.getUsername());
        main.setReleaseYear(String.valueOf(dto.getReleaseYear()));
        save(main);

        // 保存明细
        if (dto.getDetails() != null && !dto.getDetails().isEmpty()) {
            saveDetails(main, dto.getDetails());
        }

        // 自动创建接收确认记录（待确认状态）
        // 优先根据releaseType判断，如果没有则根据releaseOrg判断
        if (StringUtils.isNotEmpty(dto.getReleaseType())) {
            if ("UNION_TO_WOREDA".equals(dto.getReleaseType())) {
                // Union分发到Woreda，创建Woreda接收记录
                createReceiveWoredaRecord(main);
            } else {
                // OSE_TO_UNION 或其他，创建Union接收记录
                createReceiveUnionRecord(main);
            }
        } else if (StringUtils.isNotEmpty(main.getReleaseOrg())) {
            // 兼容旧逻辑：根据分发机构类型判断
            if (main.getReleaseOrg().contains("Union")) {
                // Union分发到Woreda，创建Woreda接收记录
                createReceiveWoredaRecord(main);
            } else {
                // OSE分发到Union，创建Union接收记录
                createReceiveUnionRecord(main);
            }
        } else {
            // 默认创建Union接收记录（兼容旧数据）
            createReceiveUnionRecord(main);
        }

        // 返回结果
        Map<String, String> result = new HashMap<>();
        result.put("id", main.getId());
        result.put("releaseId", releaseId);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editRelease(InputReleaseDTO dto) {
        if (StringUtils.isEmpty(dto.getId())) {
            throw new ServiceException("分发单ID不能为空");
        }

        // 更新主表
        InputReleaseMain main = getById(dto.getId());
        if (main == null) {
            throw new ServiceException("分发单不存在");
        }

        BeanUtils.copyProperties(dto, main, "id", "releaseId", "createTime", "createBy");
        main.setOperateBy("admin"); // TODO: 从登录用户获取
        main.setOperateTime(LocalDateTime.now());
        main.setUpdateTime(LocalDateTime.now());

        updateById(main);

        // 删除旧明细
        LambdaQueryWrapper<InputReleaseDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InputReleaseDetail::getReleaseId, main.getReleaseId());
        detailMapper.delete(wrapper);

        // 保存新明细
        if (dto.getDetails() != null && !dto.getDetails().isEmpty()) {
            saveDetails(main, dto.getDetails());
        }

        return true;
    }

    @Override
    public Map<String, Object> queryReleaseDetail(String id) {
        InputReleaseMain main = getById(id);
        Map<String, Object> result = new HashMap<>();
        if (main == null) {
            InputReleaseFarmerMain farmerMain = inputReleaseFarmerMainMapper.selectById(id);
            if(farmerMain == null){
                throw new ServiceException("distribute order is not existed");
            }
            LambdaQueryWrapper<InputReleaseFarmerDetail> wrapperFarmer = new LambdaQueryWrapper<>();
            wrapperFarmer.eq(InputReleaseFarmerDetail::getReleaseId, farmerMain.getReleaseId());
            wrapperFarmer.orderByAsc(InputReleaseFarmerDetail::getCreateTime);
            List<InputReleaseFarmerDetail> farmerDetails = inputReleaseFarmerDetailMapper.selectList(wrapperFarmer);
            result.put("main", farmerMain);
            result.put("details", farmerDetails);
            return result;
        }else{
            LambdaQueryWrapper<InputReleaseDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(InputReleaseDetail::getReleaseId, main.getReleaseId());
            wrapper.orderByAsc(InputReleaseDetail::getCreateTime);
            List<InputReleaseDetail> details = detailMapper.selectList(wrapper);
            result.put("main", main);
            result.put("details", details);
            return result;
        }


    }

    @Override
    public Map<String, Object> queryReleaseDetailByReleaseId(String releaseId) {
        // 根据releaseId查询主表
        LambdaQueryWrapper<InputReleaseMain> mainWrapper = new LambdaQueryWrapper<>();
        mainWrapper.eq(InputReleaseMain::getReleaseId, releaseId);
        InputReleaseMain main = getOne(mainWrapper);
        if (main == null) {
            throw new ServiceException("分发单不存在");
        }

        // 查询明细
        LambdaQueryWrapper<InputReleaseDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InputReleaseDetail::getReleaseId, releaseId);
        wrapper.orderByAsc(InputReleaseDetail::getCreateTime);
        List<InputReleaseDetail> details = detailMapper.selectList(wrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("main", main);
        result.put("details", details);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeRelease(List<String> ids) {
        for (String id : ids) {
            InputReleaseMain main = getById(id);
            if (main != null) {
                // 删除主表
                removeById(id);

                // 删除明细
                LambdaQueryWrapper<InputReleaseDetail> detailWrapper = new LambdaQueryWrapper<>();
                detailWrapper.eq(InputReleaseDetail::getReleaseId, main.getReleaseId());
                detailMapper.delete(detailWrapper);

                // 级联删除接收确认记录
                // 优先根据releaseType判断，如果没有则根据releaseOrg判断
                if (StringUtils.isNotEmpty(main.getReleaseType())) {
                    if ("UNION_TO_WOREDA".equals(main.getReleaseType())) {
                        // Union分发到Woreda，删除Woreda接收记录
                        LambdaQueryWrapper<InputReceiveWoreda> woredaWrapper = new LambdaQueryWrapper<>();
                        woredaWrapper.eq(InputReceiveWoreda::getReleaseId, main.getReleaseId());
                        receiveWoredaMapper.delete(woredaWrapper);
                    } else {
                        // OSE_TO_UNION 或其他，删除Union接收记录
                        LambdaQueryWrapper<InputReceiveUnion> unionWrapper = new LambdaQueryWrapper<>();
                        unionWrapper.eq(InputReceiveUnion::getReleaseId, main.getReleaseId());
                        receiveUnionMapper.delete(unionWrapper);
                    }
                } else if (StringUtils.isNotEmpty(main.getReleaseOrg())) {
                    // 兼容旧逻辑：根据分发机构类型判断
                    if (main.getReleaseOrg().contains("Union")) {
                        // Union分发到Woreda，删除Woreda接收记录
                        LambdaQueryWrapper<InputReceiveWoreda> woredaWrapper = new LambdaQueryWrapper<>();
                        woredaWrapper.eq(InputReceiveWoreda::getReleaseId, main.getReleaseId());
                        receiveWoredaMapper.delete(woredaWrapper);
                    } else {
                        // OSE分发到Union，删除Union接收记录
                        LambdaQueryWrapper<InputReceiveUnion> unionWrapper = new LambdaQueryWrapper<>();
                        unionWrapper.eq(InputReceiveUnion::getReleaseId, main.getReleaseId());
                        receiveUnionMapper.delete(unionWrapper);
                    }
                } else {
                    // 默认删除Union接收记录（兼容旧数据）
                    LambdaQueryWrapper<InputReceiveUnion> unionWrapper = new LambdaQueryWrapper<>();
                    unionWrapper.eq(InputReceiveUnion::getReleaseId, main.getReleaseId());
                    receiveUnionMapper.delete(unionWrapper);
                }
            }
        }
        return true;
    }

    /**
     * 保存分发明细
     */
    private void saveDetails(InputReleaseMain main, List<InputReleaseDetailDTO> detailDTOs) {
        for (InputReleaseDetailDTO detailDTO : detailDTOs) {
            String inputId = detailDTO.getInputId();
            
            // 只有当 inputId 存在时才进行库存校验
            if (StringUtils.isNotEmpty(inputId)) {
                // 1、校验需求数量是否超过库存
                // 1.1 根据投入品id获取库存总量
                QueryWrapper<Stock> stockWrapper = new QueryWrapper<>();
                stockWrapper.select("SUM(quantity) as quantity").lambda()
                        .eq(Stock::getMaterialId, inputId);
                List<Stock> stocks = stockMapper.selectList(stockWrapper);
                BigDecimal totalQuantity = BigDecimal.ZERO;
                if (!stocks.isEmpty() && stocks.get(0) != null) {
                    totalQuantity = stocks.get(0).getQuantity();
                }
                // 1.2 获取相同投入品、未出库的分发单对应的需求量
                BigDecimal requiredQuantity = inputReleaseMainMapper.getRequiredFromNotDeliveryInputRelease(inputId, main.getReleaseType());

                // 1.3 以上二者相减，小于当前库存，则抛出异常，提示库存不足，重新输入需求数量
                BigDecimal requiredAmount = detailDTO.getRequired() != null ? detailDTO.getRequired() : detailDTO.getQuantity();
                if (requiredAmount != null && totalQuantity.subtract(requiredQuantity).compareTo(requiredAmount) < 0) {
                    String inputName = agriInputMapper.selectById(inputId).getInputName();
                    throw new ServiceException("Insufficient inventory for input[" + inputName + "], required: " + requiredAmount + ", available: " + totalQuantity + ", please reduce the required quantity");
                }
            }

            // 2、保存表
            String detailId = "DET" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                    + IdUtils.fastSimpleUUID().substring(0, 6).toUpperCase();

            InputReleaseDetail detail = new InputReleaseDetail();
            BeanUtils.copyProperties(detailDTO, detail);
            detail.setId(IdUtils.fastSimpleUUID());
            detail.setReleaseDetailId(detailId);
            detail.setReleaseId(main.getReleaseId());
            detail.setCreateTime(LocalDateTime.now());
            detail.setReleaseTime(LocalDateTime.now());
            
            // 设置 inputId（如果有）
            if (StringUtils.isNotEmpty(inputId)) {
                detail.setInputId(Long.parseLong(inputId));
            }
            
            // 设置 inputType 和 inputCategory
            detail.setInputType(detailDTO.getInputType());
            detail.setInputCategory(detailDTO.getInputCategory());

            detailMapper.insert(detail);
        }
    }

    /**
     * 创建Union接收确认记录
     * 在OSE分发单创建时，自动生成待确认的接收记录
     */
    private void createReceiveUnionRecord(InputReleaseMain main) {
        InputReceiveUnion receive = new InputReceiveUnion();
        receive.setId(IdUtils.fastSimpleUUID());
        receive.setReleaseId(main.getReleaseId());
        receive.setReleaseName(main.getReleaseName());
        receive.setTargetId(main.getTargetId());
        receive.setTargetAddress(main.getTargetAddress());
        receive.setTargetPhone(main.getTargetPhone());
        receive.setReceiveStatus("Pending");
        receive.setReleaseYear(main.getReleaseYear());
        receive.setReleaseDate(main.getReleaseDate());
        receive.setReleaseBy(main.getReleaseBy());
        receive.setReleaseOrg(main.getReleaseOrg());
        receive.setOperateBy(main.getOperateBy());
        receive.setOperateTime(LocalDateTime.now());
        receive.setCreateTime(LocalDateTime.now());

        receiveUnionMapper.insert(receive);
    }

    /**
     * 创建Woreda接收确认记录
     * 在Union分发单创建时，自动生成待确认的接收记录
     */
    private void createReceiveWoredaRecord(InputReleaseMain main) {
        InputReceiveWoreda receive = new InputReceiveWoreda();
        receive.setId(IdUtils.fastSimpleUUID());
        receive.setReleaseId(main.getReleaseId());
        receive.setReleaseName(main.getReleaseName());
        receive.setTargetId(main.getTargetId());
        receive.setTargetAddress(main.getTargetAddress());
        receive.setTargetPhone(main.getTargetPhone());
        receive.setReceiveStatus("Pending");
        receive.setReleaseYear(main.getReleaseYear());
        receive.setReleaseDate(main.getReleaseDate());
        receive.setReleaseBy(main.getReleaseBy());
        receive.setReleaseOrg(main.getReleaseOrg());
        receive.setOperateBy(main.getOperateBy());
        receive.setOperateTime(LocalDateTime.now());
        receive.setCreateTime(LocalDateTime.now());

        receiveWoredaMapper.insert(receive);
    }

    @Override
    public Map<String, String> queryStockStatus(List<String> releaseIds) {
        Map<String, String> statusMap = new HashMap<>();
        
        for (String releaseId : releaseIds) {
            // 查询分发单信息
            LambdaQueryWrapper<InputReleaseMain> mainWrapper = new LambdaQueryWrapper<>();
            mainWrapper.eq(InputReleaseMain::getReleaseId, releaseId)
                       .eq(InputReleaseMain::getIsDeleted, 0);
            InputReleaseMain main = this.getOne(mainWrapper);
            
            if (main == null) {
                statusMap.put(releaseId, "notFound");
                continue;
            }
            
            // 根据分发单状态判断出入库状态
            // status: Pending(待处理) -> notProcessed(未出库)
            // status: Approved(已审核) -> outPending(出库待处理)
            // status: Completed(已完成)
            // status: outCompleted(已出库)
            String releaseStatus = main.getStatus();
            if ("Pending".equals(releaseStatus) || "pending".equals(releaseStatus)) {
                statusMap.put(releaseId, "notProcessed");
            } else if ("Approved".equals(releaseStatus) || "approved".equals(releaseStatus)) {
                statusMap.put(releaseId, "outPending");
            } else if ("Completed".equals(releaseStatus) || "completed".equals(releaseStatus)) {
                statusMap.put(releaseId, "completed");
            } else if ("outCompleted".equals(releaseStatus) || "OutCompleted".equals(releaseStatus)) {
                statusMap.put(releaseId, "outCompleted");
            }  else {
                statusMap.put(releaseId, "notProcessed");
            }
        }
        return statusMap;
    }

    @Override
    public Map<String, Object> queryAvailableStock(String inputCategory, String organCode) {
        Map<String, Object> result = new HashMap<>();
        
        // 根据组织编码查询对应仓库
        List<String> warehouseIds = new java.util.ArrayList<>();
        if (organCode != null && !organCode.isEmpty()) {
            LambdaQueryWrapper<Warehouse> warehouseWrapper = new LambdaQueryWrapper<>();
            warehouseWrapper.eq(Warehouse::getOrganCode, organCode)
                           .eq(Warehouse::getStatus, "1") // 只查询启用的仓库
                           .eq(Warehouse::getDelFlag, "0"); // 未删除
            List<Warehouse> warehouses = warehouseMapper.selectList(warehouseWrapper);
            warehouseIds = warehouses.stream()
                    .map(w -> String.valueOf(w.getWarehouseId()))
                    .collect(java.util.stream.Collectors.toList());
        }
        
        // 查询库存数量和容量
        BigDecimal quantity = BigDecimal.ZERO;
        BigDecimal totalCapacity = BigDecimal.ZERO;
        
        if (inputCategory != null && !inputCategory.isEmpty() && !warehouseIds.isEmpty()) {
            // 根据投入品类别和仓库查询库存
            LambdaQueryWrapper<Stock> stockWrapper = new LambdaQueryWrapper<>();
            stockWrapper.eq(Stock::getAgriculturalInputType, inputCategory)
                       .in(Stock::getWarehouseId, warehouseIds)
                       .eq(Stock::getStatus, "0"); // 正常状态
            List<Stock> stockList = stockMapper.selectList(stockWrapper);
            
            for (Stock stock : stockList) {
                // 计算库存数量
//                if (stock.getQuantity() != null) {
//                    quantity = quantity.add(stock.getQuantity());
//                }
                // 计算库存容量(KG)
                if (stock.getCapacity() != null && stock.getQuantity().compareTo(BigDecimal.ZERO) > 0   ) {
                    quantity = quantity.add(stock.getCapacity());
                }
            }
        }
        
        result.put("availableStock", quantity);
//        result.put("totalCapacity", totalCapacity);
        result.put("inputCategory", inputCategory);
        result.put("organCode", organCode);
        
        return result;
    }
}