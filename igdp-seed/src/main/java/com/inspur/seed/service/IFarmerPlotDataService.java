package com.inspur.seed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.dto.FarmerPlotDataDTO;
import com.inspur.seed.domain.entity.FarmerPlotData;
import com.inspur.seed.domain.vo.FarmerPlotDataVO;

import java.util.List;

/**
 * 农民与地块属性数据采集Service接口
 *
 * @author igdp
 * @date 2025-11-26
 */
public interface IFarmerPlotDataService extends IService<FarmerPlotData> {

    /**
     * 查询农民与地块属性数据列表
     *
     * @param dto 查询条件
     * @return 列表
     */
    List<FarmerPlotDataVO> selectFarmerPlotDataList(FarmerPlotDataDTO dto);

    /**
     * 查询农民与地块属性数据详情
     *
     * @param dataId 数据ID
     * @return 详情
     */
    FarmerPlotDataVO selectFarmerPlotDataById(String dataId);

    /**
     * 新增农民与地块属性数据
     *
     * @param dto 数据
     * @return 结果
     */
    int insertFarmerPlotData(FarmerPlotDataDTO dto);

    /**
     * 修改农民与地块属性数据
     *
     * @param dto 数据
     * @return 结果
     */
    int updateFarmerPlotData(FarmerPlotDataDTO dto);

    /**
     * 批量删除农民与地块属性数据
     *
     * @param dataIds 需要删除的数据ID
     * @return 结果
     */
    int deleteFarmerPlotDataByIds(String[] dataIds);
}
