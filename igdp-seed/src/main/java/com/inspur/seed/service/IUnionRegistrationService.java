package com.inspur.seed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.UnionInfo;
import com.inspur.seed.domain.dto.UnionRegistrationDTO;

import java.util.List;

/**
 * Union注册服务接口
 *
 * @author system
 */
public interface IUnionRegistrationService extends IService<UnionInfo> {

    /**
     * 提交Union注册申请
     *
     * @param dto Union注册申请数据
     * @return 企业ID
     */
    String submitRegistration(UnionRegistrationDTO dto);

    /**
     * 保存Union注册草稿
     *
     * @param dto Union注册申请数据
     * @return 企业ID
     */
    String saveDraft(UnionRegistrationDTO dto);

    /**
     * 查询Union列表
     *
     * @param enterpriseName 企业名称
     * @param enterpriseRegistrationId 企业注册ID
     * @param unifiedSocialCreditCode 统一社会信用代码
     * @param seedEnterpriseLicenseNumber 种子企业许可证编号
     * @param enterpriseType 企业类型
     * @param certificationStatus 认证状态
     * @return Union列表
     */
    List<UnionInfo> queryList(String enterpriseName, String enterpriseRegistrationId,
                              String unifiedSocialCreditCode, String seedEnterpriseLicenseNumber,
                              String enterpriseType, Integer certificationStatus);

    /**
     * 根据企业ID查询Union详情
     *
     * @param enterpriseId 企业ID
     * @return Union详情
     */
    UnionRegistrationDTO queryByEnterpriseId(String enterpriseId);

    /**
     * 根据用户ID查询Union详情
     *
     * @param userId 用户ID
     * @return Union详情
     */
    UnionRegistrationDTO queryByUserId(String userId);

    /**
     * 删除Union注册信息
     *
     * @param enterpriseIds 企业ID数组
     * @return 删除结果
     */
    int deleteByEnterpriseIds(String[] enterpriseIds);

    /**
     * 更新认证状态
     *
     * @param enterpriseId 企业ID
     * @param status 认证状态
     * @param rejectReason 驳回原因（可选）
     */
    void updateCertificationStatus(String enterpriseId, Integer status, String rejectReason);
}
