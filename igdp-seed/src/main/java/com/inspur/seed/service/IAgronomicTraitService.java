package com.inspur.seed.service;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.domain.AgronomicTrait;

import java.util.List;

/**
 * 农艺性状数据Service接口
 *
 * @author inspur
 */
public interface IAgronomicTraitService {

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
    AgronomicTrait selectAgronomicTraitById(String traitId);

    /**
     * 新增农艺性状数据
     *
     * @param agronomicTrait 农艺性状数据
     * @return 性状记录ID
     */
    String insertAgronomicTrait(AgronomicTrait agronomicTrait);

    /**
     * 修改农艺性状数据
     *
     * @param agronomicTrait 农艺性状数据
     * @return 影响行数
     */
    int updateAgronomicTrait(AgronomicTrait agronomicTrait);

    /**
     * 批量删除农艺性状数据
     *
     * @param traitIds 性状记录ID数组
     * @return 影响行数
     */
    int deleteAgronomicTraitByIds(String[] traitIds);


}
