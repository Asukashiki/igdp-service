package com.inspur.agriculture.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.inventory.domain.StockCheck;
import com.inspur.agriculture.inventory.domain.req.StockCheckListQuery;
import com.inspur.agriculture.inventory.domain.vo.StockCheckListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存盘点 Mapper 接口
 */
public interface StockCheckMapper extends BaseMapper<StockCheck> {

    /**
     * 生成今日新的流水号需要查询当日最大流水号
     * 
     * @param datePrefix 日期前缀，例如 "PD20260309"
     * @return 今日最大流水号的单号，不存在则返回 null
     */
    String selectMaxCheckIdByPrefix(@Param("datePrefix") String datePrefix);

    /**
     * 根据查询条件进行聚合查询，获取盘点单列表
     * 
     * @param query 查询条件
     * @return 聚合后的盘点单列表
     */
    List<StockCheckListVO> selectAggregatedList(@Param("query") StockCheckListQuery query);
}
