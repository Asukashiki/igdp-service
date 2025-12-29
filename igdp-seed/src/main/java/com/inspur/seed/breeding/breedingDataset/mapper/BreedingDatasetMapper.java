package com.inspur.seed.breeding.breedingDataset.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.breeding.breedingDataset.domain.entity.BreedingDataset;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 育种数据集Mapper接口
 *
 * @author system
 * @date 2025-01-30
 */
@Mapper
public interface BreedingDatasetMapper extends BaseMapper<BreedingDataset> {

    /**
     * 根据试验ID获取最大版本号
     *
     * @param trialId 试验ID
     * @return 最大版本号，如果没有记录则返回null
     */
    @Select("SELECT MAX(version_no) FROM breeding_dataset WHERE trial_id = #{trialId} AND deleted = '0'")
    Integer selectMaxVersionNoByTrialId(@Param("trialId") String trialId);
}
