package com.inspur.seed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.entity.AgronomicTraitDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 农艺性状明细Mapper
 * 
 * @author inspur
 */
@Mapper
public interface AgronomicTraitDetailMapper extends BaseMapper<AgronomicTraitDetail> {

    /**
     * 根据主记录ID查询明细列表
     * 
     * @param recordId 主记录ID
     * @return 明细列表
     */
    List<AgronomicTraitDetail> selectDetailsByRecordId(@Param("recordId") String recordId);

    /**
     * 批量插入明细
     * 
     * @param detailList 明细列表
     * @return 插入数量
     */
    int batchInsertDetails(@Param("list") List<AgronomicTraitDetail> detailList);

    /**
     * 根据主记录ID删除明细
     * 
     * @param recordId 主记录ID
     * @return 删除数量
     */
    int deleteDetailsByRecordId(@Param("recordId") String recordId);

    /**
     * 生成明细序号
     * 
     * @param recordId 主记录ID
     * @return 序号
     */
    Integer generateDetailSequence(@Param("recordId") String recordId);
}
