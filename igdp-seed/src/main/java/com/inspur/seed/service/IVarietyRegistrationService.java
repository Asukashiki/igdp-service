package com.inspur.seed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.VarietyRegistration;

import java.util.List;

/**
 * 品种登记服务接口
 *
 * @author system
 */
public interface IVarietyRegistrationService extends IService<VarietyRegistration> {

    /**
     * 提交品种登记申请
     *
     * @param varietyRegistration 品种登记信息
     * @return 登记申请ID
     */
    String submitRegistration(VarietyRegistration varietyRegistration);

    /**
     * 查询品种登记列表
     *
     * @param varietyName 品种名称
     * @param enterpriseName 企业名称
     * @param enterpriseType 企业类型
     * @param recordType 备案类型
     * @return 品种登记列表
     */
    List<VarietyRegistration> queryRegistrationList(String varietyName, String enterpriseName, String enterpriseType, String recordType);

    /**
     * 根据登记ID查询详情
     *
     * @param registrationId 登记ID
     * @return 品种登记信息
     */
    VarietyRegistration queryByRegistrationId(String registrationId);

    /**
     * 更新备案状态
     *
     * @param registrationId 登记ID
     * @param recordStatus 备案状态
     */
    void updateRecordStatus(String registrationId, Integer recordStatus);

    /**
     * 查询待发布的品种列表
     *
     * @param varietyName 品种名称
     * @param cropType 作物类型
     * @return 待发布品种列表
     */
    List<VarietyRegistration> queryPendingPublishList(String varietyName, String cropType);
}
