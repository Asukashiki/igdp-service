package com.inspur.seed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.VarietyPublish;

import java.util.List;

/**
 * 品种发布服务接口
 *
 * @author system
 */
public interface IVarietyPublishService extends IService<VarietyPublish> {

    /**
     * 发布品种
     *
     * @param varietyPublish 发布信息
     * @return 发布ID
     */
    String publishVariety(VarietyPublish varietyPublish);

    /**
     * 查询已发布品种列表
     *
     * @param varietyName 品种名称
     * @param cropType 作物类型
     * @param publishStatus 公示状态
     * @return 发布列表
     */
    List<VarietyPublish> queryPublishList(String varietyName, String cropType, Integer publishStatus);

    /**
     * 根据发布ID查询详情
     *
     * @param publishId 发布ID
     * @return 发布信息
     */
    VarietyPublish queryByPublishId(String publishId);

    /**
     * 下架品种
     *
     * @param publishId 发布ID
     */
    void unpublishVariety(String publishId);

    /**
     * 根据登记ID查询发布记录
     *
     * @param registrationId 登记ID
     * @return 发布记录
     */
    VarietyPublish queryByRegistrationId(String registrationId);
}
