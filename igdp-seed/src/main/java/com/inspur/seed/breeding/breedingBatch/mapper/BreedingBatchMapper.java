package com.inspur.seed.breeding.breedingBatch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.breeding.breedingBatch.domain.entity.BreedingBatch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 育种批次信息Mapper接口
 *
 * @author inspur
 */
@Mapper
public interface BreedingBatchMapper extends BaseMapper<BreedingBatch> {

    /**
     * 查询育种批次列表
     *
     * @param breedingBatch 查询条件
     * @return 育种批次列表
     */
    List<BreedingBatch> selectBreedingBatchList(BreedingBatch breedingBatch);



    /**
     * 获取育种批次Voided列表
     *
     * @param breedingBatch 筛选条件
     * @return 育种批次列表
     */
    List<BreedingBatch> selectBreedingBatchVoidedList(BreedingBatch breedingBatch);

    /**
     * 根据批次ID查询育种批次
     *
     * @param batchId 批次ID
     * @return 育种批次
     */
    BreedingBatch selectByBatchId(@Param("batchId") String batchId);

    /**
     * 生成新的批次ID
     *
     * @param year 年份
     * @return 批次ID
     */
    String generateBatchId(@Param("year") int year);

    /**
     * 根据作物类型和年份生成新的批次ID
     * 格式: B_{cropType}_{year}_serial(6位)
     *
     * @param cropType 作物类型
     * @param year 年份
     * @return 批次ID
     */
    String generateBatchIdByCropTypeAndYear(@Param("cropType") String cropType, @Param("year") Integer year);

    /**
     * 获取育种批次下拉列表
     *
     * @return 育种批次列表
     */
    List<BreedingBatch> selectBatchOptions();
}
