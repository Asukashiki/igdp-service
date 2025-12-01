package com.inspur.seed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.TrialPlotRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 试验-地块关联Mapper接口
 *
 * @author inspur
 */
@Mapper
public interface TrialPlotRelationMapper extends BaseMapper<TrialPlotRelation> {

    /**
     * 根据试验ID删除关联关系（逻辑删除）
     *
     * @param trialId 试验ID
     * @return 影响行数
     */
    int deleteByTrialId(@Param("trialId") String trialId);

    /**
     * 根据试验ID查询关联地块ID列表
     *
     * @param trialId 试验ID
     * @return 地块ID列表
     */
    List<String> selectPlotIdsByTrialId(@Param("trialId") String trialId);

    /**
     * 批量插入关联关系
     *
     * @param relationList 关联关系列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<TrialPlotRelation> relationList);
}
