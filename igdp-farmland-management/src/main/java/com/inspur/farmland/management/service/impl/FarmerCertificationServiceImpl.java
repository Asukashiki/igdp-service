package com.inspur.farmland.management.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.farmland.management.bean.entity.FarmerCertification;
import com.inspur.farmland.management.mapper.FarmerCertificationMapper;
import com.inspur.farmland.management.service.IFarmerCertificationService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 农民认证Service实现类
 * 
 * @author inspur
 */
@Service
public class FarmerCertificationServiceImpl extends ServiceImpl<FarmerCertificationMapper, FarmerCertification> implements IFarmerCertificationService {

    @Override
    public boolean applyCertification(FarmerCertification certification) {
        // 设置申请时间
        certification.setApplyTime(new Date());
        // 设置初始状态为审核中(1)
        certification.setStatus(1);
        // 保存认证申请
        return this.save(certification);
    }

    @Override
    public FarmerCertification getCertificationByUserId(String userId) {
        return this.lambdaQuery()
                .eq(FarmerCertification::getUserId, userId)
                .one();
    }

    @Override
    public List<FarmerCertification> getPendingCertifications() {
        return this.lambdaQuery()
                .eq(FarmerCertification::getStatus, 1) // 1表示审核中(待审批)
                .orderByAsc(FarmerCertification::getApplyTime)
                .list();
    }

    @Override
    public boolean approveCertification(Long certId, String approverId) {
        FarmerCertification certification = this.getById(certId);
        if (certification != null) {
            certification.setStatus(2); // 2表示已通过
            certification.setApproverId(approverId);
            certification.setApproveTime(new Date());
            return this.updateById(certification);
        }
        return false;
    }

    @Override
    public boolean rejectCertification(Long certId, String approverId, String rejectReason) {
        FarmerCertification certification = this.getById(certId);
        if (certification != null) {
            certification.setStatus(0); // 0表示未通过
            certification.setApproverId(approverId);
            certification.setApproveTime(new Date());
            certification.setRejectReason(rejectReason);
            return this.updateById(certification);
        }
        return false;
    }
}