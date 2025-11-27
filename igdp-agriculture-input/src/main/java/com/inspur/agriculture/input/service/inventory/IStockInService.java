package com.inspur.agriculture.input.service.inventory;

import com.inspur.agriculture.input.dto.inventory.StockInDTO;
import com.inspur.agriculture.input.dto.inventory.StockInQueryDTO;
import com.inspur.agriculture.input.vo.inventory.StockInVO;

import java.util.List;

/**
 * 入库单 Service接口
 *
 * @author inspur
 * @date 2025-11-26
 */
public interface IStockInService {

    /**
     * 查询入库单列表
     *
     * @param queryDTO 查询条件
     * @return 入库单列表
     */
    List<StockInVO> getStockInList(StockInQueryDTO queryDTO);

    /**
     * 根据入库单号查询详情
     *
     * @param stockInId 入库单号
     * @return 入库单详情
     */
    StockInVO getStockInById(String stockInId);

    /**
     * 创建入库单
     *
     * @param dto 入库单信息
     * @return 入库单号
     */
    String createStockIn(StockInDTO dto);

    /**
     * 确认入库
     *
     * @param stockInId 入库单号
     * @return 结果
     */
    int confirmStockIn(String stockInId);
}
