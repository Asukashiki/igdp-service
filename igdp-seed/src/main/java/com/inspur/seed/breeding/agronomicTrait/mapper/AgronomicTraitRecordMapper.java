package com.inspur.seed.breeding.agronomicTrait.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.breeding.agronomicTrait.domain.entity.AgronomicTraitRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 农艺性状采集主记录Mapper
 *
 * @author inspur
 */
@Mapper
public interface AgronomicTraitRecordMapper extends BaseMapper<AgronomicTraitRecord> {

    /**
     * 查询主记录列表（含性状数量统计）
     *
     * @param record 查询条件
     * @return 主记录列表
     */
    List<AgronomicTraitRecord> selectRecordList(AgronomicTraitRecord record);

    /**
     * 根据ID查询主记录详情（级联查询明细）
     *
     * @param recordId 记录ID
     * @return 主记录详情
     */
    AgronomicTraitRecord selectRecordByIdWithDetails(@Param("recordId") String recordId);

    /**
     * 生成新的记录序号
     *
     * @param plotId 地块ID
     * @return 序号
     */
    Integer generateRecordSequence(@Param("plotId") String plotId);
}
