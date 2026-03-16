package com.inspur.agriculture.inventory.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.inventory.domain.InventoryStock;
import com.inspur.agriculture.inventory.domain.StockCheck;
import com.inspur.agriculture.inventory.domain.req.StockCheckCreateReq;
import com.inspur.agriculture.inventory.domain.req.StockCheckListQuery;
import com.inspur.agriculture.inventory.domain.req.StockCheckReviewReq;
import com.inspur.agriculture.inventory.domain.req.StockCheckUpdateReq;
import com.inspur.agriculture.inventory.domain.vo.StockCheckDetailVO;
import com.inspur.agriculture.inventory.domain.vo.StockCheckListVO;
import com.inspur.agriculture.inventory.domain.vo.WarehouseInventoryItemVO;
import com.inspur.agriculture.inventory.mapper.InventoryStockMapper;
import com.inspur.agriculture.inventory.mapper.StockCheckMapper;
import com.inspur.agriculture.inventory.service.IInventoryStockService;
import com.inspur.agriculture.inventory.service.IStockCheckService;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 盘点业务 Service 实现类
 */
@Service
public class StockCheckServiceImpl extends ServiceImpl<StockCheckMapper, StockCheck> implements IStockCheckService {

    @Autowired
    private IInventoryStockService inventoryStockService;

    @Autowired
    private InventoryStockMapper inventoryStockMapper;

    @Override
    public List<StockCheckListVO> getList(StockCheckListQuery query) {
        return baseMapper.selectAggregatedList(query);
    }

    @Override
    public StockCheckDetailVO getDetail(String checkId) {
        List<StockCheck> list = this.list(Wrappers.<StockCheck>lambdaQuery().eq(StockCheck::getCheckId, checkId));
        if (CollUtil.isEmpty(list)) {
            throw new ServiceException("Stock check order not found.");
        }
        StockCheck head = list.get(0);
        StockCheckDetailVO vo = new StockCheckDetailVO();
        BeanUtils.copyProperties(head, vo);
        vo.setDetails(list);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createStockCheck(StockCheckCreateReq req) {
        // R005: 校验当天同仓库是否存在草稿/待审核
        Date today = new Date();
        long count = this.count(Wrappers.<StockCheck>lambdaQuery()
                .eq(StockCheck::getWarehouseId, req.getWarehouseId())
                .eq(StockCheck::getCheckDate, DateUtil.beginOfDay(req.getCheckDate()))
                .in(StockCheck::getCheckStatus, "DRAFT", "PENDING"));
        if (count > 0) {
            throw new ServiceException("A full stock check already exists for this warehouse today.");
        }

        // 生成盘点编号
        String checkId = generateCheckId();

        // 查验库存底表记录
        List<WarehouseInventoryItemVO> stockList = getWarehouseInventory(req.getWarehouseId());
        Map<String, WarehouseInventoryItemVO> stockMap = stockList.stream()
                .collect(Collectors.toMap(item -> item.getProductId() + "_" + item.getBatchNo(), item -> item));

        String userId = SecurityUtils.getUserId();
        String nickname = SecurityUtils.getNickname();

        List<StockCheck> batchList = new ArrayList<>();
        // 获取所有提交的明细，进行校验防并发
        for (StockCheckCreateReq.DetailReq dReq : req.getDetails()) {
            if (dReq.getActualQty() != null && dReq.getActualQty().compareTo(BigDecimal.ZERO) < 0) {
                throw new ServiceException("Actual quantity cannot be negative. (R001)");
            }
            // 从底表找出对应的商品信息，这避免了前台传假数据，且补全了冗余字段
            String key = dReq.getProductId() + "_" + dReq.getBatchNo();
            WarehouseInventoryItemVO currentStock = stockMap.get(key);
            if (currentStock == null) {
                throw new ServiceException("Batch not found for product " + dReq.getProductId() + ", batch " + dReq.getBatchNo() + " in current warehouse.");
            }

            StockCheck sc = new StockCheck();
            sc.setCheckId(checkId);
            sc.setCheckDate(req.getCheckDate());
            sc.setWarehouseId(req.getWarehouseId());
            sc.setWarehouseName(currentStock.getProductName()); // 如果有具体的名称需补全，此处简化

            sc.setCheckRemark(req.getCheckRemark());
            sc.setCheckerId(userId);
            sc.setCheckerName(nickname);
            sc.setCheckStatus("PENDING");
            sc.setUnit(currentStock.getUnit());
            sc.setProductId(currentStock.getProductId());
            sc.setProductName(currentStock.getProductName());
            sc.setCategoryMajor(currentStock.getCategoryMajor());
            sc.setCategoryMinor(currentStock.getCategoryMinor());
            sc.setBatchNo(currentStock.getBatchNo());
            sc.setUnit(currentStock.getUnit());
            sc.setExpiryDate(currentStock.getExpiryDate());
            sc.setQualityStatus(currentStock.getQualityStatus());

            sc.setSystemQty(currentStock.getCurrentQty());
            sc.setActualQty(dReq.getActualQty());
            sc.setItemRemark(dReq.getItemRemark());

            calculateDiff(sc);
            batchList.add(sc);
        }

        this.saveBatch(batchList);
        return checkId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStockCheck(String checkId, StockCheckUpdateReq req) {
        List<StockCheck> list = this.list(Wrappers.<StockCheck>lambdaQuery().eq(StockCheck::getCheckId, checkId));
        if (CollUtil.isEmpty(list)) {
            throw new ServiceException("Stock check order not found.");
        }
        String status = list.get(0).getCheckStatus();
        if (!"DRAFT".equals(status) && !"REJECTED".equals(status)) {
            throw new ServiceException("Current status does not allow editing. (R009)");
        }

        Map<Long, StockCheck> map = list.stream().collect(Collectors.toMap(StockCheck::getId, s -> s));

        for (StockCheckUpdateReq.DetailUpdateReq dReq : req.getDetails()) {
            StockCheck sc = map.get(dReq.getId());
            if (sc == null)
                continue;

            if (dReq.getActualQty() != null && dReq.getActualQty().compareTo(BigDecimal.ZERO) < 0) {
                throw new ServiceException("Actual quantity cannot be negative. (R001)");
            }
            sc.setActualQty(dReq.getActualQty());
            sc.setItemRemark(dReq.getItemRemark());
            if (req.getCheckDate() != null) {
                sc.setCheckDate(req.getCheckDate());
            }
            if (req.getCheckRemark() != null) {
                sc.setCheckRemark(req.getCheckRemark());
            }

            calculateDiff(sc);
            this.updateById(sc);

            // 为了保持同行其他记录一致同步整体头信息：(优化可以批量更新头部信息)
            LambdaQueryWrapper<StockCheck> updateHead = Wrappers.lambdaQuery();
            updateHead.eq(StockCheck::getCheckId, checkId);
            StockCheck updater = new StockCheck();
            if (req.getCheckDate() != null)
                updater.setCheckDate(req.getCheckDate());
            if (req.getCheckRemark() != null)
                updater.setCheckRemark(req.getCheckRemark());
            this.update(updater, updateHead);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitStockCheck(String checkId) {
        List<StockCheck> list = this.list(Wrappers.<StockCheck>lambdaQuery().eq(StockCheck::getCheckId, checkId));
        if (CollUtil.isEmpty(list))
            throw new ServiceException("Stock check order not found.");

        String status = list.get(0).getCheckStatus();
        if (!"DRAFT".equals(status) && !"REJECTED".equals(status)) {
            throw new ServiceException("Current status does not allow submission.");
        }

        for (StockCheck sc : list) {
            if (sc.getActualQty() == null) {
                throw new ServiceException("Please fill in actual quantities for all items. (R003)");
            }
            if (!"NONE".equals(sc.getDiffType())
                    && (sc.getItemRemark() == null || sc.getItemRemark().trim().isEmpty())) {
                throw new ServiceException("Please provide remarks for items with differences. (R004)");
            }
        }

        StockCheck up = new StockCheck();
        up.setCheckStatus("PENDING");
        up.setReviewerId(null);
        up.setReviewerName(null);
        up.setReviewOpinion(null);
        up.setReviewDate(null);
        this.update(up, Wrappers.<StockCheck>lambdaQuery().eq(StockCheck::getCheckId, checkId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStockCheck(String checkId) {
        List<StockCheck> list = this.list(Wrappers.<StockCheck>lambdaQuery().eq(StockCheck::getCheckId, checkId));
        if (CollUtil.isEmpty(list))
            return;
        if (!"DRAFT".equals(list.get(0).getCheckStatus())) {
            throw new ServiceException("Only draft status allows deletion. (R009)");
        }
        this.remove(Wrappers.<StockCheck>lambdaQuery().eq(StockCheck::getCheckId, checkId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelStockCheck(String checkId) {
        StockCheck up = new StockCheck();
        up.setCheckStatus("CANCELLED");
        int res = baseMapper.update(up, Wrappers.<StockCheck>lambdaQuery().eq(StockCheck::getCheckId, checkId)
                .eq(StockCheck::getCheckStatus, "DRAFT"));
        if (res == 0) {
            throw new ServiceException("Cancellation failed. Only draft checks can be cancelled.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveStockCheck(String checkId, StockCheckReviewReq req) {
        List<StockCheck> list = this.list(Wrappers.<StockCheck>lambdaQuery().eq(StockCheck::getCheckId, checkId));
        if (CollUtil.isEmpty(list))
            throw new ServiceException("Stock check order not found.");
        if (!"PENDING".equals(list.get(0).getCheckStatus())) {
            throw new ServiceException("Order is not pending review.");
        }

        String userId = SecurityUtils.getUserId();
        String nickname = SecurityUtils.getNickname();

        StockCheck headerUp = new StockCheck();
        headerUp.setCheckStatus("APPROVED");
        headerUp.setReviewerId(userId);
        headerUp.setReviewerName(nickname);
        headerUp.setReviewOpinion(req.getReviewOpinion());
        headerUp.setReviewDate(new Date());
        this.update(headerUp, Wrappers.<StockCheck>lambdaQuery().eq(StockCheck::getCheckId, checkId));

        for (StockCheck sc : list) {
            if ("NONE".equals(sc.getDiffType())) {
                continue;
            }
            Long warehouseId = parseRequiredLong("warehouseId", sc.getWarehouseId());
            Long productId = parseRequiredLong("productId", sc.getProductId());
            BigDecimal qty = sc.getDiffQty() == null ? null : sc.getDiffQty().abs();
            if (qty == null || qty.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ServiceException("Invalid difference quantity. Unable to adjust inventory.");
            }
            InventoryStock stock = resolveStock(warehouseId, productId);
            if ("SURPLUS".equals(sc.getDiffType())) {
                stock.setAvailableQty(stock.getAvailableQty().add(qty));
            } else if ("LOSS".equals(sc.getDiffType())) {
                if (stock.getAvailableQty().compareTo(qty) < 0) {
                    throw new ServiceException("Insufficient inventory to deduct.");
                }
                stock.setAvailableQty(stock.getAvailableQty().subtract(qty));
            }
            if (sc.getQualityStatus() != null && !sc.getQualityStatus().trim().isEmpty()) {
                stock.setStockStatus(sc.getQualityStatus().trim());
            }
            inventoryStockMapper.updateById(stock);
        }

        StockCheck finalUp = new StockCheck();
        finalUp.setCheckStatus("ADJUSTED");
        this.update(finalUp, Wrappers.<StockCheck>lambdaQuery().eq(StockCheck::getCheckId, checkId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectStockCheck(String checkId, StockCheckReviewReq req) {
        if (req.getReviewOpinion() == null || req.getReviewOpinion().trim().isEmpty()) {
            throw new ServiceException("Review comments are required for rejection. (R007)");
        }
        StockCheck up = new StockCheck();
        up.setCheckStatus("REJECTED");
        up.setReviewerId(SecurityUtils.getUserId());
        up.setReviewerName(SecurityUtils.getNickname());
        up.setReviewOpinion(req.getReviewOpinion());
        up.setReviewDate(new Date());

        int res = baseMapper.update(up, Wrappers.<StockCheck>lambdaQuery()
                .eq(StockCheck::getCheckId, checkId).eq(StockCheck::getCheckStatus, "PENDING"));
        if (res == 0) {
            throw new ServiceException("Rejection failed. Order is not pending review.");
        }
    }

    @Override
    public List<WarehouseInventoryItemVO> getWarehouseInventory(String warehouseId) {
        // 调用底表查询
        InventoryStock q = new InventoryStock();
        q.setWarehouseId(Long.valueOf(warehouseId));
        List<InventoryStock> baseList = inventoryStockService.selectStockList(q);

        List<WarehouseInventoryItemVO> res = new ArrayList<>();
        for (InventoryStock is : baseList) {
            WarehouseInventoryItemVO vo = new WarehouseInventoryItemVO();
            vo.setProductId(String.valueOf(is.getProductId()));
            vo.setProductName(is.getProductName() != null ? is.getProductName() : "Unknown");
            vo.setCategoryMajor(is.getMainCategory());
            vo.setCategoryMinor(is.getSubCategory());
            vo.setUnit(is.getUnit());
            vo.setBatchNo(is.getBatchNo());
            vo.setBatchId(String.valueOf(is.getBatchId()));
            vo.setQualityStatus(is.getStockStatus());
            vo.setCurrentQty(is.getAvailableQty());
            // unit 省略
            res.add(vo);
        }
        return res;
    }

    @Override
    public boolean isWarehouseChecking(String warehouseId) {
        long count = this.count(Wrappers.<StockCheck>lambdaQuery()
                .eq(StockCheck::getWarehouseId, warehouseId)
                .in(StockCheck::getCheckStatus, "DRAFT", "PENDING"));
        return count > 0;
    }

    /** 计算差异并写入对象 */
    private void calculateDiff(StockCheck sc) {
        if (sc.getActualQty() == null) {
            sc.setDiffQty(null);
            sc.setDiffType(null);
            return;
        }
        BigDecimal diff = sc.getActualQty().subtract(sc.getSystemQty());
        sc.setDiffQty(diff);
        if (diff.compareTo(BigDecimal.ZERO) > 0) {
            sc.setDiffType("SURPLUS");
        } else if (diff.compareTo(BigDecimal.ZERO) < 0) {
            sc.setDiffType("LOSS");
        } else {
            sc.setDiffType("NONE");
        }
    }

    /** 线程安全的号段生成 */
    private synchronized String generateCheckId() {
        String prefix = "PD" + DateUtil.format(new Date(), "yyyyMMdd");
        String maxId = baseMapper.selectMaxCheckIdByPrefix(prefix);
        int seq = 1;
        if (maxId != null && maxId.length() == 14) {
            String seqStr = maxId.substring(10);
            try {
                seq = Integer.parseInt(seqStr) + 1;
            } catch (Exception ignored) {
            }
        }
        return prefix + String.format("%04d", seq);
    }

    private InventoryStock resolveStock(Long warehouseId, Long productId) {
        LambdaQueryWrapper<InventoryStock> stockQuery = new LambdaQueryWrapper<>();
        stockQuery.eq(InventoryStock::getProductId, productId)
                .eq(InventoryStock::getWarehouseId, warehouseId);
        List<InventoryStock> stocks = inventoryStockMapper.selectList(stockQuery);
        if (stocks == null || stocks.isEmpty()) {
            throw new ServiceException("Inventory record not found, productId=" + productId + ", warehouseId=" + warehouseId);
        }
        if (stocks.size() > 1) {
            throw new ServiceException("Inventory record is not unique, productId=" + productId + ", warehouseId=" + warehouseId);
        }
        return stocks.get(0);
    }

    private Long parseRequiredLong(String field, String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new ServiceException(field + " cannot be empty.");
        }
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException ex) {
            throw new ServiceException(field + " is invalid: " + value);
        }
    }

}
