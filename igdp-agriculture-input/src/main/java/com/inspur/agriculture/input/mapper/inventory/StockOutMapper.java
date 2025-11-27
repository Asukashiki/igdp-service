package com.inspur.agriculture.input.mapper.inventory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.inventory.StockOut;
import com.inspur.agriculture.input.dto.inventory.StockOutQueryDTO;
import com.inspur.agriculture.input.vo.inventory.StockOutVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 出库单 Mapper
 *
 * @author inspur
 * @date 2025-11-26
 */
public interface StockOutMapper extends BaseMapper<StockOut> {

    /**
     * 查询出库单列表
     *
     * @param query 查询条件
     * @return 出库单列表
     */
    List<StockOutVO> selectStockOutList(@Param("query") StockOutQueryDTO query);

    /**
     * 根据出库单号查询详情
     *
     * @param stockOutId 出库单号
     * @return 出库单详情
     */
    StockOutVO selectStockOutById(@Param("stockOutId") String stockOutId);

    /**
     * 生成出库单号
     *
     * @return 出库单号
     */
    String generateStockOutId();
}
