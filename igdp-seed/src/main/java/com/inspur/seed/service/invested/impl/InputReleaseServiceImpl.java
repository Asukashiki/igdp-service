package com.inspur.seed.service.invested.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.input.domain.inventory.Stock;
import com.inspur.agriculture.input.mapper.AgriInputMapper;
import com.inspur.agriculture.input.mapper.inventory.StockMapper;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.uuid.IdUtils;
import com.inspur.seed.domain.invested.InputReceiveUnion;
import com.inspur.seed.domain.invested.InputReceiveWoreda;
import com.inspur.seed.domain.invested.InputReleaseDetail;
import com.inspur.seed.domain.invested.InputReleaseMain;
import com.inspur.seed.dto.invested.InputReleaseDTO;
import com.inspur.seed.dto.invested.InputReleaseDetailDTO;
import com.inspur.seed.mapper.invested.InputReceiveUnionMapper;
import com.inspur.seed.mapper.invested.InputReceiveWoredaMapper;
import com.inspur.seed.mapper.invested.InputReleaseDetailMapper;
import com.inspur.seed.mapper.invested.InputReleaseMainMapper;
import com.inspur.seed.service.invested.IInputReleaseService;
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
    private StockMapper  stockMapper;

    @Resource
    private InputReleaseMainMapper inputReleaseMainMapper;

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
        main.setReleaseYear(String.valueOf(dto.getReleaseYear()));
        save(main);

        // 保存明细
        if (dto.getDetails() != null && !dto.getDetails().isEmpty()) {
            saveDetails(releaseId, releaseType, dto.getDetails());
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
            saveDetails(main.getReleaseId(), main.getReleaseType(), dto.getDetails());
        }

        return true;
    }

    @Override
    public Map<String, Object> queryReleaseDetail(String id) {
        InputReleaseMain main = getById(id);
        if (main == null) {
            throw new ServiceException("分发单不存在");
        }

        // 查询明细
        LambdaQueryWrapper<InputReleaseDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InputReleaseDetail::getReleaseId, main.getReleaseId());
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
    private void saveDetails(String releaseId, String releaseType, List<InputReleaseDetailDTO> detailDTOs) {
        for (InputReleaseDetailDTO detailDTO : detailDTOs) {
            // 1、校验需求数量是否超过库存
            // 1.1 根据投入品id获取库存总量
            QueryWrapper<Stock> stockWrapper = new QueryWrapper<>();
            stockWrapper.select("SUM(quantity) as quantity").lambda()
                    .eq(Stock::getMaterialId, detailDTO.getInputId());
            List<Stock> stocks = stockMapper.selectList(stockWrapper);
            BigDecimal totalQuantity = BigDecimal.ZERO;
            if (!stocks.isEmpty() && stocks.get(0) != null) {
                totalQuantity = stocks.get(0).getQuantity();
            }
            String inputId = detailDTO.getInputId();
            // 1.2 获取相同投入品、未出库的分发单对应的需求量
            BigDecimal requiredQuantity = inputReleaseMainMapper.getRequiredFromNotDeliveryInputRelease(inputId, releaseType);

            // 1.3 以上二者相减，小于当前库存，则抛出异常，提示库存不足，重新输入需求数量
            if (totalQuantity.subtract(requiredQuantity).compareTo(detailDTO.getRequired()) < 0) {
                String inputName = agriInputMapper.selectById(inputId).getInputName();
                throw new ServiceException("Insufficient inventory for input[" + inputName + "], required: " + detailDTO.getRequired() + ", available: " + totalQuantity + ", please reduce the required quantity");
            }

            // 2、保存表
            String detailId = "DET" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                    + IdUtils.fastSimpleUUID().substring(0, 6).toUpperCase();

            InputReleaseDetail detail = new InputReleaseDetail();
            BeanUtils.copyProperties(detailDTO, detail);
            detail.setId(IdUtils.fastSimpleUUID());
            detail.setReleaseDetailId(detailId);
            detail.setReleaseId(releaseId);
            detail.setCreateTime(LocalDateTime.now());
            detail.setReleaseTime(LocalDateTime.now());
            detail.setInputId(Long.parseLong(detailDTO.getInputId()));

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
}
