package com.inspur.agriculture.input.service.inventory;

import com.inspur.agriculture.input.dto.inventory.StockOutDTO;
import com.inspur.agriculture.input.dto.inventory.StockOutQueryDTO;
import com.inspur.agriculture.input.vo.inventory.StockOutVO;

import java.util.List;

/**
 * 出库单 Service接口
 *
 * @author inspur
 * @date 2025-11-26
 */
public interface IStockOutService {

    /**
     * 查询出库单列表
     *
     * @param queryDTO 查询条件
     * @return 出库单列表
     */
    List<StockOutVO> getStockOutList(StockOutQueryDTO queryDTO);

    /**
     * 根据出库单号查询详情
     *
     * @param stockOutId 出库单号
     * @return 出库单详情
     */
    StockOutVO getStockOutById(String stockOutId);

    /**
     * 创建出库单
     *
     * @param dto 出库单信息
     * @return 出库单号
     */
    String createStockOut(StockOutDTO dto);

    /**
     * 确认出库
     *
     * @param stockOutId 出库单号
     * @return 结果
     */
    int confirmStockOut(String stockOutId);
}
