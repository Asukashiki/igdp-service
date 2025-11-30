package com.inspur.seed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.SowingInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 播种信息Mapper接口
 *
 * @author inspur
 */
@Mapper
public interface SowingInfoMapper extends BaseMapper<SowingInfo> {

    /**
     * 根据地块ID查询播种信息列表
     *
     * @param groundId 地块ID
     * @return 播种信息列表
     */
    List<SowingInfo> selectSowingListByGroundId(@Param("groundId") String groundId);

    /**
     * 根据地块ID删除播种信息（逻辑删除）
     *
     * @param groundId 地块ID
     * @return 影响行数
     */
    int deleteByGroundId(@Param("groundId") String groundId);

    /**
     * 批量插入播种信息
     *
     * @param sowingList 播种信息列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<SowingInfo> sowingList);
}
