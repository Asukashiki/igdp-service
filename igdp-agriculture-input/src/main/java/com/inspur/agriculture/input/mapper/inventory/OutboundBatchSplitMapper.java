package com.inspur.agriculture.input.mapper.inventory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.inventory.OutboundBatchSplit;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 出库批次拆分 Mapper
 *
 * @author igdp
 */
public interface OutboundBatchSplitMapper extends BaseMapper<OutboundBatchSplit> {

    /**
     * 根据出库明细ID查询批次拆分记录
     *
     * @param outboundDetailId 出库明细ID
     * @return 批次拆分列表
     */
    List<OutboundBatchSplit> selectByDetailId(@Param("outboundDetailId") String outboundDetailId);

    /**
     * 根据入库批次ID查询拆分记录
     *
     * @param inboundBatchId 入库批次ID
     * @return 批次拆分列表
     */
    List<OutboundBatchSplit> selectByInboundBatchId(@Param("inboundBatchId") String inboundBatchId);

    /**
     * 批量插入批次拆分记录
     *
     * @param splits 拆分记录列表
     * @return 影响行数
     */
    int batchInsert(@Param("splits") List<OutboundBatchSplit> splits);

    /**
     * 统计批次出库数量
     *
     * @param inboundBatchId 入库批次ID
     * @return 总出库数量
     */
    Map<String, Object> sumSplitQuantity(@Param("inboundBatchId") String inboundBatchId);

    /**
     * 查询批次拆分详情（关联出库单和明细）
     *
     * @param inboundBatchId 入库批次ID
     * @return 拆分详情列表
     */
    List<Map<String, Object>> selectSplitDetailsWithOrder(@Param("inboundBatchId") String inboundBatchId);
}
