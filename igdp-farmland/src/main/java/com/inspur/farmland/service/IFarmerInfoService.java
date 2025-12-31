package com.inspur.farmland.service;

import com.inspur.farmland.domain.FarmerInfo;

import java.util.List;
import java.util.Map;

/**
 * 农民信息Service接口
 *
 * @author inspur
 */
public interface IFarmerInfoService {

    /**
     * 分页查询农民列表
     *
     * @param farmerInfo 查询条件
     * @return 农民列表
     */
    List<FarmerInfo> selectFarmerInfoList(FarmerInfo farmerInfo);

    /**
     * 根据农民编码查询农民详情
     *
     * @param farmerId 农民编码
     * @return 农民信息
     */
    FarmerInfo selectFarmerInfoByFarmerId(String farmerId);

    /**
     * 新增农民
     *
     * @param farmerInfo 农民信息
     * @return 农民编码
     */
    String insertFarmerInfo(FarmerInfo farmerInfo);

    /**
     * 修改农民
     *
     * @param farmerInfo 农民信息
     * @return 影响行数
     */
    int updateFarmerInfo(FarmerInfo farmerInfo);

    /**
     * 删除农民
     *
     * @param farmerId 农民编码
     * @return 影响行数
     */
    int deleteFarmerInfoByFarmerId(String farmerId);

    /**
     * 批量删除农民
     *
     * @param farmerIds 农民编码数组
     * @return 删除结果
     */
    Map<String, Integer> deleteFarmerInfoByIds(String[] farmerIds);

    /**
     * 获取农民下拉选项
     *
     * @param kebeleCode 村代码
     * @param keyword 关键词（姓名/身份证号）
     * @return 农民选项列表
     */
    List<Map<String, Object>> selectFarmerOptions(String kebeleCode, String keyword);

    /**
     * 检查身份证号是否唯一
     *
     * @param idCard 身份证号
     * @param farmerId 农民编码（修改时排除自己）
     * @return true-唯一 false-重复
     */
    boolean checkIdCardUnique(String idCard, String farmerId);

    /**
     * 更新农民的土地统计信息
     *
     * @param farmerId 农民编码
     */
    void updateLandStatistics(String farmerId);

    /**
     * 根据身份证号查询农民
     *
     * @param idCard 身份证号
     * @return 农民信息
     */
    FarmerInfo selectFarmerByIdCard(String idCard);

    /**
     * 批量导入农民数据
     *
     * @param farmerList 农民数据列表
     * @param updateSupport 是否支持更新已存在的数据
     * @return 导入结果
     */
    Map<String, Object> importFarmerData(List<FarmerInfo> farmerList, boolean updateSupport);
}
