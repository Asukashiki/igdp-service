package com.inspur.seed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.entity.BreedingBatchInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 繁殖批次信息Mapper接口
 *
 * @author igdp
 * @date 2025-11-29
 */
@Mapper
public interface BreedingBatchInfoMapper extends BaseMapper<BreedingBatchInfo> {

    /**
     * 统计批次的跟踪记录数量
     *
     * @param batchId 批次编号
     * @return 跟踪记录数量
     */
    @Select("SELECT COUNT(*) FROM breeding_tracking_info WHERE batch_id = #{batchId} AND del_flag = '0'")
    Integer countTrackingByBatchId(@Param("batchId") String batchId);

    /**
     * 统计批次的检测记录数量
     *
     * @param batchId 批次编号
     * @return 检测记录数量
     */
    @Select("SELECT COUNT(*) FROM breeding_test_info WHERE batch_id = #{batchId} AND del_flag = '0'")
    Integer countTestByBatchId(@Param("batchId") String batchId);
}
