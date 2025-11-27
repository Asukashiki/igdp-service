package com.inspur.agriculture.input.mapper.inventory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.inventory.StockInItem;
import com.inspur.agriculture.input.vo.inventory.StockInVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 入库商品明细 Mapper
 *
 * @author inspur
 * @date 2025-11-26
 */
public interface StockInItemMapper extends BaseMapper<StockInItem> {

    /**
     * 根据入库单号查询明细列表
     *
     * @param stockInId 入库单号
     * @return 明细列表
     */
    List<StockInVO.StockInItemVO> selectItemsByStockInId(@Param("stockInId") String stockInId);

    /**
     * 生成入库商品明细ID
     *
     * @param stockInId 入库单号
     * @param index     索引
     * @return 明细ID
     */
    String generateStockInItemId(@Param("stockInId") String stockInId, @Param("index") int index);

    /**
     * 批量插入入库商品明细
     *
     * @param items 明细列表
     * @return 影响行数
     */
    int batchInsert(@Param("items") List<StockInItem> items);
}
