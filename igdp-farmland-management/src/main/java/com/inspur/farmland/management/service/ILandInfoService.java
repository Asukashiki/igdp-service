package com.inspur.farmland.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.farmland.management.bean.entity.LandInfo;
import java.util.List;

/**
 * 土地信息服务接口
 * 
 * @author inspur
 */
public interface ILandInfoService extends IService<LandInfo> {

    /**
     * 根据用户ID获取土地列表
     *
     * @param userId 用户ID
     * @return 土地列表
     */
    List<LandInfo> getLandsByUserId(String userId);

    /**
     * 添加土地信息
     * 
     * @param landInfo 土地信息
     * @return 是否成功
     */
    boolean addLandInfo(LandInfo landInfo);

    /**
     * 更新土地信息
     * 
     * @param landInfo 土地信息
     * @return 是否成功
     */
    boolean updateLandInfo(LandInfo landInfo);

    /**
     * 删除土地信息
     * 
     * @param landId 土地ID
     * @return 是否成功
     */
    boolean deleteLandInfo(Long landId);
}