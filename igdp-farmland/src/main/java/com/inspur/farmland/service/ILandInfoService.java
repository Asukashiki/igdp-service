package com.inspur.farmland.service;

import com.inspur.farmland.domain.LandInfo;

import java.util.List;
import java.util.Map;

/**
 * 土地信息Service接口
 *
 * @author inspur
 */
public interface ILandInfoService {

    /**
     * 分页查询土地列表
     *
     * @param landInfo 查询条件
     * @return 土地列表
     */
    List<LandInfo> selectLandInfoList(LandInfo landInfo);

    /**
     * 根据土地编码查询土地详情
     *
     * @param landId 土地编码
     * @return 土地信息
     */
    LandInfo selectLandInfoByLandId(String landId);

    /**
     * 新增土地
     *
     * @param landInfo 土地信息
     * @return 土地编码
     */
    String insertLandInfo(LandInfo landInfo);

    /**
     * 修改土地
     *
     * @param landInfo 土地信息
     * @return 影响行数
     */
    int updateLandInfo(LandInfo landInfo);

    /**
     * 删除土地
     *
     * @param landId 土地编码
     * @return 影响行数
     */
    int deleteLandInfoByLandId(String landId);

    /**
     * 批量删除土地
     *
     * @param landIds 土地编码数组
     * @return 删除结果
     */
    Map<String, Integer> deleteLandInfoByIds(String[] landIds);

    /**
     * 关联农民
     *
     * @param landId 土地编码
     * @param farmerId 农民编码
     * @return 影响行数
     */
    int bindFarmer(String landId, String farmerId);

    /**
     * 解除农民关联
     *
     * @param landId 土地编码
     * @return 影响行数
     */
    int unbindFarmer(String landId);

    /**
     * 获取农民的土地列表
     *
     * @param farmerId 农民编码
     * @return 土地列表
     */
    List<LandInfo> selectLandListByFarmerId(String farmerId);

    /**
     * 获取土地统计数据
     *
     * @param kebeleCode 村代码
     * @param woredaCode 镇代码
     * @param zoneCode 区代码
     * @return 统计数据
     */
    Map<String, Object> getLandStatistics(String kebeleCode, String woredaCode, String zoneCode);
}
