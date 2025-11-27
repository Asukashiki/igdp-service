package com.inspur.seed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.dto.AgronomicTraitDataDTO;
import com.inspur.seed.domain.entity.AgronomicTraitData;
import com.inspur.seed.domain.vo.AgronomicTraitDataVO;

import java.util.List;

/**
 * 农艺性状数据采集Service接口
 *
 * @author igdp
 * @date 2025-11-26
 */
public interface IAgronomicTraitDataService extends IService<AgronomicTraitData> {

    /**
     * 查询农艺性状数据列表
     *
     * @param dto 查询条件
     * @return 列表
     */
    List<AgronomicTraitDataVO> selectAgronomicTraitDataList(AgronomicTraitDataDTO dto);

    /**
     * 查询农艺性状数据详情
     *
     * @param dataId 数据ID
     * @return 详情
     */
    AgronomicTraitDataVO selectAgronomicTraitDataById(String dataId);

    /**
     * 新增农艺性状数据
     *
     * @param dto 数据
     * @return 结果
     */
    int insertAgronomicTraitData(AgronomicTraitDataDTO dto);

    /**
     * 修改农艺性状数据
     *
     * @param dto 数据
     * @return 结果
     */
    int updateAgronomicTraitData(AgronomicTraitDataDTO dto);

    /**
     * 批量删除农艺性状数据
     *
     * @param dataIds 需要删除的数据ID
     * @return 结果
     */
    int deleteAgronomicTraitDataByIds(String[] dataIds);
}
