package com.inspur.seed.service.invested.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.input.domain.inventory.Stock;
import com.inspur.agriculture.input.domain.inventory.Warehouse;
import com.inspur.agriculture.input.mapper.AgriInputMapper;
import com.inspur.agriculture.input.mapper.inventory.StockMapper;
import com.inspur.agriculture.input.mapper.inventory.WarehouseMapper;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.uuid.IdUtils;
import com.inspur.seed.domain.invested.*;
import com.inspur.seed.dto.invested.BoaZoneReleaseDTO;
import com.inspur.seed.dto.invested.BoaZoneReleaseDetailDTO;
import com.inspur.seed.mapper.invested.*;
import com.inspur.seed.service.invested.IBoaZoneReleaseService;
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
 * 鎶曞叆鍝佸垎鍙慡ervice瀹炵幇
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@Service
public class BoaZoneReleaseServiceImpl extends ServiceImpl<BoaZoneReleaseMainMapper, BoaZoneReleaseMain>
        implements IBoaZoneReleaseService {

    @Resource
    private BoaZoneReleaseDetailMapper detailMapper;

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
    private BoaZoneReleaseMainMapper boaZoneReleaseMainMapper;

    @Override
    public List<BoaZoneReleaseMain> queryReleaseList(String releaseType, String releaseName, String inputType,
                                                   LocalDate startTime, LocalDate endTime) {
        LambdaQueryWrapper<BoaZoneReleaseMain> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BoaZoneReleaseMain::getReleaseType, releaseType);

        if (StringUtils.isNotEmpty(releaseName)) {
            wrapper.like(BoaZoneReleaseMain::getReleaseName, releaseName);
        }
        if (startTime != null) {
            wrapper.ge(BoaZoneReleaseMain::getReleaseDate, LocalDateTime.of(startTime, LocalTime.MIN));
        }
        if (endTime != null) {
            wrapper.le(BoaZoneReleaseMain::getReleaseDate, LocalDateTime.of(endTime, LocalTime.MAX));
        }

        wrapper.orderByDesc(BoaZoneReleaseMain::getReleaseDate);

        return list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> addRelease(BoaZoneReleaseDTO dto) {
        // 鐢熸垚鍒嗗彂鍗曠紪鍙?
        String releaseId = "REL" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + IdUtils.fastSimpleUUID().substring(0, 6).toUpperCase();
        String releaseType = dto.getReleaseType();
        // 淇濆瓨涓昏〃
        BoaZoneReleaseMain main = new BoaZoneReleaseMain();
        BeanUtils.copyProperties(dto, main);
        main.setId(IdUtils.fastSimpleUUID());
        main.setReleaseId(releaseId);
        main.setOperateBy("admin"); // TODO: 浠庣櫥褰曠敤鎴疯幏鍙?
        main.setOperateTime(LocalDateTime.now());
        main.setCreateTime(LocalDateTime.now());
        main.setReleaseDate(dto.getReleaseDate());
        main.setAuditDate(LocalDateTime.now());
        main.setStatus("distributed");
        main.setReleaseType(releaseType);
        main.setAuditBy(SecurityUtils.getUsername());
        main.setReleaseYear(dto.getReleaseYear() == null ? null : String.valueOf(dto.getReleaseYear()));
        if (StringUtils.isEmpty(main.getSourceOrganization())) {
            main.setSourceOrganization("Oromia Bureau of Agriculture");
        }
        save(main);

        // 淇濆瓨鏄庣粏
        if (dto.getDetails() != null && !dto.getDetails().isEmpty()) {
            saveDetails(main, dto.getDetails());
        }

        // 鑷姩鍒涘缓鎺ユ敹纭璁板綍锛堝緟纭鐘舵€侊級
        // 浼樺厛鏍规嵁releaseType鍒ゆ柇锛屽鏋滄病鏈夊垯鏍规嵁releaseOrg鍒ゆ柇
        if (StringUtils.isNotEmpty(dto.getReleaseType())) {
            if ("UNION_TO_WOREDA".equals(dto.getReleaseType())) {
                // Union鍒嗗彂鍒癢oreda锛屽垱寤篧oreda鎺ユ敹璁板綍
                createReceiveWoredaRecord(main);
            } else {
                // OSE_TO_UNION 鎴栧叾浠栵紝鍒涘缓Union鎺ユ敹璁板綍
                createReceiveUnionRecord(main);
            }
        } else if (StringUtils.isNotEmpty(main.getReleaseOrg())) {
            // 鍏煎鏃ч€昏緫锛氭牴鎹垎鍙戞満鏋勭被鍨嬪垽鏂?
            if (main.getReleaseOrg().contains("Union")) {
                // Union鍒嗗彂鍒癢oreda锛屽垱寤篧oreda鎺ユ敹璁板綍
                createReceiveWoredaRecord(main);
            } else {
                // OSE鍒嗗彂鍒癠nion锛屽垱寤篣nion鎺ユ敹璁板綍
                createReceiveUnionRecord(main);
            }
        } else {
            // 榛樿鍒涘缓Union鎺ユ敹璁板綍锛堝吋瀹规棫鏁版嵁锛?
            createReceiveUnionRecord(main);
        }

        // 杩斿洖缁撴灉
        Map<String, String> result = new HashMap<>();
        result.put("id", main.getId());
        result.put("releaseId", releaseId);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editRelease(BoaZoneReleaseDTO dto) {
        if (StringUtils.isEmpty(dto.getId())) {
            throw new ServiceException("鍒嗗彂鍗旾D涓嶈兘涓虹┖");
        }

        // 鏇存柊涓昏〃
        BoaZoneReleaseMain main = getById(dto.getId());
        if (main == null) {
            throw new ServiceException("鍒嗗彂鍗曚笉瀛樺湪");
        }

        BeanUtils.copyProperties(dto, main, "id", "releaseId", "createTime", "createBy");
        main.setReleaseYear(dto.getReleaseYear() == null ? null : String.valueOf(dto.getReleaseYear()));
        if (StringUtils.isEmpty(main.getSourceOrganization())) {
            main.setSourceOrganization("Oromia Bureau of Agriculture");
        }
        main.setOperateBy("admin"); // TODO: 浠庣櫥褰曠敤鎴疯幏鍙?
        main.setOperateTime(LocalDateTime.now());
        main.setUpdateTime(LocalDateTime.now());

        updateById(main);

        // 鍒犻櫎鏃ф槑缁?
        LambdaQueryWrapper<BoaZoneReleaseDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BoaZoneReleaseDetail::getReleaseId, main.getReleaseId());
        detailMapper.delete(wrapper);

        // 淇濆瓨鏂版槑缁?
        if (dto.getDetails() != null && !dto.getDetails().isEmpty()) {
            saveDetails(main, dto.getDetails());
        }

        return true;
    }

    @Override
    public Map<String, Object> queryReleaseDetail(String id) {
        BoaZoneReleaseMain main = getById(id);
        Map<String, Object> result = new HashMap<>();
        if (main == null) {
            throw new ServiceException("distribute order is not existed");
        }

        LambdaQueryWrapper<BoaZoneReleaseDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BoaZoneReleaseDetail::getReleaseId, main.getReleaseId());
        wrapper.orderByAsc(BoaZoneReleaseDetail::getCreateTime);
        List<BoaZoneReleaseDetail> details = detailMapper.selectList(wrapper);
        result.put("main", main);
        result.put("details", details);
        return result;

    }

    @Override
    public Map<String, Object> queryReleaseDetailByReleaseId(String releaseId) {
        // 鏍规嵁releaseId鏌ヨ涓昏〃
        LambdaQueryWrapper<BoaZoneReleaseMain> mainWrapper = new LambdaQueryWrapper<>();
        mainWrapper.eq(BoaZoneReleaseMain::getReleaseId, releaseId);
        BoaZoneReleaseMain main = getOne(mainWrapper);
        if (main == null) {
            throw new ServiceException("鍒嗗彂鍗曚笉瀛樺湪");
        }

        // 鏌ヨ鏄庣粏
        LambdaQueryWrapper<BoaZoneReleaseDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BoaZoneReleaseDetail::getReleaseId, releaseId);
        wrapper.orderByAsc(BoaZoneReleaseDetail::getCreateTime);
        List<BoaZoneReleaseDetail> details = detailMapper.selectList(wrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("main", main);
        result.put("details", details);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeRelease(List<String> ids) {
        for (String id : ids) {
            BoaZoneReleaseMain main = getById(id);
            if (main != null) {
                // 鍒犻櫎涓昏〃
                removeById(id);

                // 鍒犻櫎鏄庣粏
                LambdaQueryWrapper<BoaZoneReleaseDetail> detailWrapper = new LambdaQueryWrapper<>();
                detailWrapper.eq(BoaZoneReleaseDetail::getReleaseId, main.getReleaseId());
                detailMapper.delete(detailWrapper);

                // 绾ц仈鍒犻櫎鎺ユ敹纭璁板綍
                // 浼樺厛鏍规嵁releaseType鍒ゆ柇锛屽鏋滄病鏈夊垯鏍规嵁releaseOrg鍒ゆ柇
                if (StringUtils.isNotEmpty(main.getReleaseType())) {
                    if ("UNION_TO_WOREDA".equals(main.getReleaseType())) {
                        // Union鍒嗗彂鍒癢oreda锛屽垹闄oreda鎺ユ敹璁板綍
                        LambdaQueryWrapper<InputReceiveWoreda> woredaWrapper = new LambdaQueryWrapper<>();
                        woredaWrapper.eq(InputReceiveWoreda::getReleaseId, main.getReleaseId());
                        receiveWoredaMapper.delete(woredaWrapper);
                    } else {
                        // OSE_TO_UNION 鎴栧叾浠栵紝鍒犻櫎Union鎺ユ敹璁板綍
                        LambdaQueryWrapper<InputReceiveUnion> unionWrapper = new LambdaQueryWrapper<>();
                        unionWrapper.eq(InputReceiveUnion::getReleaseId, main.getReleaseId());
                        receiveUnionMapper.delete(unionWrapper);
                    }
                } else if (StringUtils.isNotEmpty(main.getReleaseOrg())) {
                    // 鍏煎鏃ч€昏緫锛氭牴鎹垎鍙戞満鏋勭被鍨嬪垽鏂?
                    if (main.getReleaseOrg().contains("Union")) {
                        // Union鍒嗗彂鍒癢oreda锛屽垹闄oreda鎺ユ敹璁板綍
                        LambdaQueryWrapper<InputReceiveWoreda> woredaWrapper = new LambdaQueryWrapper<>();
                        woredaWrapper.eq(InputReceiveWoreda::getReleaseId, main.getReleaseId());
                        receiveWoredaMapper.delete(woredaWrapper);
                    } else {
                        // OSE鍒嗗彂鍒癠nion锛屽垹闄nion鎺ユ敹璁板綍
                        LambdaQueryWrapper<InputReceiveUnion> unionWrapper = new LambdaQueryWrapper<>();
                        unionWrapper.eq(InputReceiveUnion::getReleaseId, main.getReleaseId());
                        receiveUnionMapper.delete(unionWrapper);
                    }
                } else {
                    // 榛樿鍒犻櫎Union鎺ユ敹璁板綍锛堝吋瀹规棫鏁版嵁锛?
                    LambdaQueryWrapper<InputReceiveUnion> unionWrapper = new LambdaQueryWrapper<>();
                    unionWrapper.eq(InputReceiveUnion::getReleaseId, main.getReleaseId());
                    receiveUnionMapper.delete(unionWrapper);
                }
            }
        }
        return true;
    }

    /**
     * 淇濆瓨鍒嗗彂鏄庣粏
     */
    private void saveDetails(BoaZoneReleaseMain main, List<BoaZoneReleaseDetailDTO> detailDTOs) {
        for (BoaZoneReleaseDetailDTO detailDTO : detailDTOs) {
            String inputId = detailDTO.getInputId();
            
            // 鍙湁褰?inputId 瀛樺湪鏃舵墠杩涜搴撳瓨鏍￠獙
            if (StringUtils.isNotEmpty(inputId)) {
                // 1銆佹牎楠岄渶姹傛暟閲忔槸鍚﹁秴杩囧簱瀛?
                // 1.1 鏍规嵁鎶曞叆鍝乮d鑾峰彇搴撳瓨鎬婚噺
                QueryWrapper<Stock> stockWrapper = new QueryWrapper<>();
                stockWrapper.select("SUM(quantity) as quantity").lambda()
                        .eq(Stock::getMaterialId, inputId);
                List<Stock> stocks = stockMapper.selectList(stockWrapper);
                BigDecimal totalQuantity = BigDecimal.ZERO;
                if (!stocks.isEmpty() && stocks.get(0) != null) {
                    totalQuantity = stocks.get(0).getQuantity();
                }
                // 1.2 鑾峰彇鐩稿悓鎶曞叆鍝併€佹湭鍑哄簱鐨勫垎鍙戝崟瀵瑰簲鐨勯渶姹傞噺
                BigDecimal requiredQuantity = boaZoneReleaseMainMapper.getRequiredFromNotDeliveryInputRelease(inputId, main.getReleaseType());

                // 1.3 浠ヤ笂浜岃€呯浉鍑忥紝灏忎簬褰撳墠搴撳瓨锛屽垯鎶涘嚭寮傚父锛屾彁绀哄簱瀛樹笉瓒筹紝閲嶆柊杈撳叆闇€姹傛暟閲?
                BigDecimal requiredAmount = detailDTO.getRequired() != null ? detailDTO.getRequired() : detailDTO.getQuantity();
                if (requiredAmount != null && totalQuantity.subtract(requiredQuantity).compareTo(requiredAmount) < 0) {
                    String inputName = agriInputMapper.selectById(inputId).getInputName();
                    throw new ServiceException("Insufficient inventory for input[" + inputName + "], required: " + requiredAmount + ", available: " + totalQuantity + ", please reduce the required quantity");
                }
            }

            // 2銆佷繚瀛樿〃
            String detailId = "DET" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                    + IdUtils.fastSimpleUUID().substring(0, 6).toUpperCase();

            BoaZoneReleaseDetail detail = new BoaZoneReleaseDetail();
            BeanUtils.copyProperties(detailDTO, detail);
            detail.setId(IdUtils.fastSimpleUUID());
            detail.setReleaseDetailId(detailId);
            detail.setReleaseId(main.getReleaseId());
            detail.setCreateTime(LocalDateTime.now());
            detail.setReleaseTime(LocalDateTime.now());
            
            // 璁剧疆 inputId锛堝鏋滄湁锛?
            if (StringUtils.isNotEmpty(inputId)) {
                detail.setInputId(Long.parseLong(inputId));
            }
            
            // 璁剧疆 inputType 鍜?inputCategory
            detail.setInputType(detailDTO.getInputType());
            detail.setInputCategory(detailDTO.getInputCategory());

            detailMapper.insert(detail);
        }
    }

    /**
     * 鍒涘缓Union鎺ユ敹纭璁板綍
     * 鍦∣SE鍒嗗彂鍗曞垱寤烘椂锛岃嚜鍔ㄧ敓鎴愬緟纭鐨勬帴鏀惰褰?
     */
    private void createReceiveUnionRecord(BoaZoneReleaseMain main) {
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
     * 鍒涘缓Woreda鎺ユ敹纭璁板綍
     * 鍦║nion鍒嗗彂鍗曞垱寤烘椂锛岃嚜鍔ㄧ敓鎴愬緟纭鐨勬帴鏀惰褰?
     */
    private void createReceiveWoredaRecord(BoaZoneReleaseMain main) {
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
            // 鏌ヨ鍒嗗彂鍗曚俊鎭?
            LambdaQueryWrapper<BoaZoneReleaseMain> mainWrapper = new LambdaQueryWrapper<>();
            mainWrapper.eq(BoaZoneReleaseMain::getReleaseId, releaseId)
                       .eq(BoaZoneReleaseMain::getIsDeleted, 0);
            BoaZoneReleaseMain main = this.getOne(mainWrapper);
            
            if (main == null) {
                statusMap.put(releaseId, "notFound");
                continue;
            }
            
            // 鏍规嵁鍒嗗彂鍗曠姸鎬佸垽鏂嚭鍏ュ簱鐘舵€?
            // status: Pending(寰呭鐞? -> notProcessed(鏈嚭搴?
            // status: Approved(宸插鏍? -> outPending(鍑哄簱寰呭鐞?
            // status: Completed(宸插畬鎴?
            // status: outCompleted(宸插嚭搴?
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
        
        // 鏍规嵁缁勭粐缂栫爜鏌ヨ瀵瑰簲浠撳簱
        List<String> warehouseIds = new java.util.ArrayList<>();
        if (organCode != null && !organCode.isEmpty()) {
            LambdaQueryWrapper<Warehouse> warehouseWrapper = new LambdaQueryWrapper<>();
            warehouseWrapper.eq(Warehouse::getOrganCode, organCode)
                           .eq(Warehouse::getStatus, "1") // 鍙煡璇㈠惎鐢ㄧ殑浠撳簱
                           .eq(Warehouse::getDelFlag, "0"); // 鏈垹闄?
            List<Warehouse> warehouses = warehouseMapper.selectList(warehouseWrapper);
            warehouseIds = warehouses.stream()
                    .map(w -> String.valueOf(w.getWarehouseId()))
                    .collect(java.util.stream.Collectors.toList());
        }
        
        // 鏌ヨ搴撳瓨鏁伴噺鍜屽閲?
        BigDecimal quantity = BigDecimal.ZERO;
        BigDecimal totalCapacity = BigDecimal.ZERO;
        
        if (inputCategory != null && !inputCategory.isEmpty() && !warehouseIds.isEmpty()) {
            // 鏍规嵁鎶曞叆鍝佺被鍒拰浠撳簱鏌ヨ搴撳瓨
            LambdaQueryWrapper<Stock> stockWrapper = new LambdaQueryWrapper<>();
            stockWrapper.eq(Stock::getAgriculturalInputType, inputCategory)
                       .in(Stock::getWarehouseId, warehouseIds)
                       .eq(Stock::getStatus, "0"); // 姝ｅ父鐘舵€?
            List<Stock> stockList = stockMapper.selectList(stockWrapper);
            
            for (Stock stock : stockList) {
                // 璁＄畻搴撳瓨鏁伴噺
//                if (stock.getQuantity() != null) {
//                    quantity = quantity.add(stock.getQuantity());
//                }
                // 璁＄畻搴撳瓨瀹归噺(KG)
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
