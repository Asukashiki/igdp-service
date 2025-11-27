package com.inspur.agriculture.input.mapper.inventory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.inventory.StockIn;
import com.inspur.agriculture.input.dto.inventory.StockInQueryDTO;
import com.inspur.agriculture.input.vo.inventory.StockInVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 入库单 Mapper
 *
 * @author inspur
 * @date 2025-11-26
 */
public interface StockInMapper extends BaseMapper<StockIn> {

    /**
     * 查询入库单列表
     *
     * @param query 查询条件
     * @return 入库单列表
     */
    List<StockInVO> selectStockInList(@Param("query") StockInQueryDTO query);

    /**
     * 根据入库单号查询详情
     *
     * @param stockInId 入库单号
     * @return 入库单详情
     */
    StockInVO selectStockInById(@Param("stockInId") String stockInId);

    /**
     * 生成入库单号
     *
     * @return 入库单号
     */
    String generateStockInId();

    /**
     * 生成批次号
     *
     * @return 批次号
     */
    String generateBatchNo();
}
