package com.inspur.seed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.EnterpriseInfo;

import java.util.List;

/**
 * 企业认证服务接口
 *
 * @author system
 */
public interface IEnterpriseCertifyService extends IService<EnterpriseInfo> {

    /**
     * 提交企业认证申请
     *
     * @param enterpriseInfo 企业认证信息
     * @return 企业ID
     */
    String submitCertifyApplication(EnterpriseInfo enterpriseInfo);

    /**
     * 查询认证申请列表
     *
     * @param keyword 关键词（企业名称/信用代码/许可证编号）
     * @param enterpriseType 企业类型
     * @param certificationStatus 认证状态
     * @return 认证申请列表
     */
    List<EnterpriseInfo> queryCertifyList(String keyword, String enterpriseType, Integer certificationStatus);

    /**
     * 根据企业ID查询认证信息
     *
     * @param enterpriseId 企业ID
     * @return 企业认证信息
     */
    EnterpriseInfo queryByEnterpriseId(String enterpriseId);

    /**
     * 更新认证状态
     *
     * @param enterpriseId 企业ID
     * @param status 认证状态
     */
    void updateCertificationStatus(String enterpriseId, Integer status);

    /**
     * 保存企业认证草稿
     *
     * @param enterpriseInfo 企业认证信息
     * @return 企业ID
     */
    String saveDraft(EnterpriseInfo enterpriseInfo);

    /**
     * 更新企业认证草稿
     *
     * @param enterpriseInfo 企业认证信息
     * @return 更新结果
     */
    boolean updateDraft(EnterpriseInfo enterpriseInfo);
}
