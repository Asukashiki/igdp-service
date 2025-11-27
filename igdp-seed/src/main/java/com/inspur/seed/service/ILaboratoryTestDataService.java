package com.inspur.seed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.dto.LaboratoryTestDataDTO;
import com.inspur.seed.domain.entity.LaboratoryTestData;
import com.inspur.seed.domain.vo.LaboratoryTestDataVO;

import java.util.List;

/**
 * 实验室测试数据采集Service接口
 *
 * @author igdp
 * @date 2025-11-26
 */
public interface ILaboratoryTestDataService extends IService<LaboratoryTestData> {

    /**
     * 查询实验室测试数据列表
     *
     * @param dto 查询条件
     * @return 列表
     */
    List<LaboratoryTestDataVO> selectLaboratoryTestDataList(LaboratoryTestDataDTO dto);

    /**
     * 查询实验室测试数据详情
     *
     * @param dataId 数据ID
     * @return 详情
     */
    LaboratoryTestDataVO selectLaboratoryTestDataById(String dataId);

    /**
     * 新增实验室测试数据
     *
     * @param dto 数据
     * @return 结果
     */
    int insertLaboratoryTestData(LaboratoryTestDataDTO dto);

    /**
     * 修改实验室测试数据
     *
     * @param dto 数据
     * @return 结果
     */
    int updateLaboratoryTestData(LaboratoryTestDataDTO dto);

    /**
     * 批量删除实验室测试数据
     *
     * @param dataIds 需要删除的数据ID
     * @return 结果
     */
    int deleteLaboratoryTestDataByIds(String[] dataIds);
}
