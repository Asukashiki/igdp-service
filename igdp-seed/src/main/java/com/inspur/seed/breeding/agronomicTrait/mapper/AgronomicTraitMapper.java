package com.inspur.seed.breeding.agronomicTrait.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.breeding.agronomicTrait.domain.entity.AgronomicTrait;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 农艺性状数据Mapper接口
 *
 * @author inspur
 */
@Mapper
public interface AgronomicTraitMapper extends BaseMapper<AgronomicTrait> {

    /**
     * 查询农艺性状数据列表
     *
     * @param agronomicTrait 查询条件
     * @return 农艺性状数据列表
     */
    List<AgronomicTrait> selectAgronomicTraitList(AgronomicTrait agronomicTrait);

    /**
     * 根据ID查询农艺性状数据详情
     *
     * @param traitId 性状记录ID
     * @return 农艺性状数据
     */
    AgronomicTrait selectAgronomicTraitById(@Param("traitId") String traitId);
}
