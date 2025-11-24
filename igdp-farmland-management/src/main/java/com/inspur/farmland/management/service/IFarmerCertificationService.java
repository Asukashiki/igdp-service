package com.inspur.farmland.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.farmland.management.bean.entity.FarmerCertification;
import java.util.List;

/**
 * 农民认证Service接口
 * 
 * @author inspur
 */
public interface IFarmerCertificationService extends IService<FarmerCertification> {

    /**
     * 根据用户ID查询认证状态
     * 
     * @param userId 用户ID
     * @return 认证信息
     */
    FarmerCertification getCertificationByUserId(Long userId);

    /**
     * 获取待审批列表
     * 
     * @return 待审批列表
     */
    List<FarmerCertification> getPendingCertifications();

    /**
     * 审批通过
     * 
     * @param certId 认证ID
     * @param approverId 审批人ID
     * @return 是否成功
     */
    boolean approveCertification(Long certId, Long approverId);

    /**
     * 审批驳回
     * 
     * @param certId 认证ID
     * @param approverId 审批人ID
     * @param rejectReason 驳回原因
     * @return 是否成功
     */
    boolean rejectCertification(Long certId, Long approverId, String rejectReason);
}