package com.inspur.seed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.entity.BreedingTrackingInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 繁殖跟踪信息Mapper接口
 *
 * @author igdp
 * @date 2025-11-29
 */
@Mapper
public interface BreedingTrackingInfoMapper extends BaseMapper<BreedingTrackingInfo> {

    /**
     * 统计跟踪记录的检测数量
     *
     * @param trackingId 跟踪编号
     * @return 检测记录数量
     */
    @Select("SELECT COUNT(*) FROM breeding_test_info WHERE tracking_id = #{trackingId} AND del_flag = '0'")
    Integer countTestByTrackingId(@Param("trackingId") String trackingId);
}
