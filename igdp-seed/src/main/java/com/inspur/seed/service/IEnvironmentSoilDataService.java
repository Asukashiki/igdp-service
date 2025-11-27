package com.inspur.seed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.dto.EnvironmentSoilDataDTO;
import com.inspur.seed.domain.entity.EnvironmentSoilData;
import com.inspur.seed.domain.vo.EnvironmentSoilDataVO;

import java.util.List;

/**
 * 环境与土壤属性数据采集Service接口
 *
 * @author igdp
 * @date 2025-11-26
 */
public interface IEnvironmentSoilDataService extends IService<EnvironmentSoilData> {

    /**
     * 查询环境与土壤属性数据列表
     *
     * @param dto 查询条件
     * @return 列表
     */
    List<EnvironmentSoilDataVO> selectEnvironmentSoilDataList(EnvironmentSoilDataDTO dto);

    /**
     * 查询环境与土壤属性数据详情
     *
     * @param dataId 数据ID
     * @return 详情
     */
    EnvironmentSoilDataVO selectEnvironmentSoilDataById(String dataId);

    /**
     * 新增环境与土壤属性数据
     *
     * @param dto 数据
     * @return 结果
     */
    int insertEnvironmentSoilData(EnvironmentSoilDataDTO dto);

    /**
     * 修改环境与土壤属性数据
     *
     * @param dto 数据
     * @return 结果
     */
    int updateEnvironmentSoilData(EnvironmentSoilDataDTO dto);

    /**
     * 批量删除环境与土壤属性数据
     *
     * @param dataIds 需要删除的数据ID
     * @return 结果
     */
    int deleteEnvironmentSoilDataByIds(String[] dataIds);
}
