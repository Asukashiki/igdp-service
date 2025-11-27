package com.inspur.seed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.dto.FarmingRecordDataDTO;
import com.inspur.seed.domain.entity.FarmingRecordData;
import com.inspur.seed.domain.vo.FarmingRecordDataVO;

import java.util.List;

/**
 * 农事记录数据采集Service接口
 *
 * @author igdp
 * @date 2025-11-26
 */
public interface IFarmingRecordDataService extends IService<FarmingRecordData> {

    /**
     * 查询农事记录数据列表
     *
     * @param dto 查询条件
     * @return 列表
     */
    List<FarmingRecordDataVO> selectFarmingRecordDataList(FarmingRecordDataDTO dto);

    /**
     * 查询农事记录数据详情
     *
     * @param dataId 数据ID
     * @return 详情
     */
    FarmingRecordDataVO selectFarmingRecordDataById(String dataId);

    /**
     * 新增农事记录数据
     *
     * @param dto 数据
     * @return 结果
     */
    int insertFarmingRecordData(FarmingRecordDataDTO dto);

    /**
     * 修改农事记录数据
     *
     * @param dto 数据
     * @return 结果
     */
    int updateFarmingRecordData(FarmingRecordDataDTO dto);

    /**
     * 批量删除农事记录数据
     *
     * @param dataIds 需要删除的数据ID
     * @return 结果
     */
    int deleteFarmingRecordDataByIds(String[] dataIds);
}
