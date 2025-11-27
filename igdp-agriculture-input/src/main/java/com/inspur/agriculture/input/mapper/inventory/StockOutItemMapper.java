package com.inspur.agriculture.input.mapper.inventory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.inventory.StockOutItem;
import com.inspur.agriculture.input.vo.inventory.StockOutVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 出库商品明细 Mapper
 *
 * @author inspur
 * @date 2025-11-26
 */
public interface StockOutItemMapper extends BaseMapper<StockOutItem> {

    /**
     * 根据出库单号查询明细列表
     *
     * @param stockOutId 出库单号
     * @return 明细列表
     */
    List<StockOutVO.StockOutItemVO> selectItemsByStockOutId(@Param("stockOutId") String stockOutId);

    /**
     * 生成出库商品明细ID
     *
     * @param stockOutId 出库单号
     * @param index      索引
     * @return 明细ID
     */
    String generateStockOutItemId(@Param("stockOutId") String stockOutId, @Param("index") int index);

    /**
     * 批量插入出库商品明细
     *
     * @param items 明细列表
     * @return 影响行数
     */
    int batchInsert(@Param("items") List<StockOutItem> items);
}
