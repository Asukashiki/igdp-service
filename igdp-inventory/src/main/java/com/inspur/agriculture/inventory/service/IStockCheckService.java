package com.inspur.agriculture.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.agriculture.inventory.domain.StockCheck;
import com.inspur.agriculture.inventory.domain.req.StockCheckCreateReq;
import com.inspur.agriculture.inventory.domain.req.StockCheckListQuery;
import com.inspur.agriculture.inventory.domain.req.StockCheckReviewReq;
import com.inspur.agriculture.inventory.domain.req.StockCheckUpdateReq;
import com.inspur.agriculture.inventory.domain.vo.StockCheckDetailVO;
import com.inspur.agriculture.inventory.domain.vo.StockCheckListVO;
import com.inspur.agriculture.inventory.domain.vo.WarehouseInventoryItemVO;

import java.util.List;

/**
 * 盘点业务 Service 接口
 */
public interface IStockCheckService extends IService<StockCheck> {

    /**
     * 获取盘点单列表
     */
    List<StockCheckListVO> getList(StockCheckListQuery query);

    /**
     * 获取盘点单详情
     */
    StockCheckDetailVO getDetail(String checkId);

    /**
     * 新建盘点单
     * 
     * @return checkId 盘点单编号
     */
    String createStockCheck(StockCheckCreateReq req);

    /**
     * 编辑盘点单
     */
    void updateStockCheck(String checkId, StockCheckUpdateReq req);

    /**
     * 删除盘点单（仅草稿）
     */
    void deleteStockCheck(String checkId);

    /**
     * 提交盘点单
     */
    void submitStockCheck(String checkId);

    /**
     * 取消盘点单（仅草稿）
     */
    void cancelStockCheck(String checkId);

    /**
     * 审核通过（自动触发库存调整，需事务）
     */
    void approveStockCheck(String checkId, StockCheckReviewReq req);

    /**
     * 审核驳回
     */
    void rejectStockCheck(String checkId, StockCheckReviewReq req);

    /**
     * 获取指定仓库当前的库存明细，用作建单时的系统库存底表
     */
    List<WarehouseInventoryItemVO> getWarehouseInventory(String warehouseId);

    /**
     * 检查某仓库是否正在盘点中
     * 供出入库等模块调用
     */
    boolean isWarehouseChecking(String warehouseId);
}
