package com.inspur.farmland.management.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.farmland.management.bean.entity.FarmerCertification;
import com.inspur.farmland.management.mapper.FarmerCertificationMapper;
import com.inspur.farmland.management.service.IFarmerCertificationService;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 农民认证Service实现类
 * 
 * @author inspur
 */
@Service
public class FarmerCertificationServiceImpl extends ServiceImpl<FarmerCertificationMapper, FarmerCertification> implements IFarmerCertificationService {

    @Override
    public FarmerCertification getCertificationByUserId(Long userId) {
        return this.lambdaQuery()
                .eq(FarmerCertification::getUserId, userId)
                .one();
    }

    @Override
    public List<FarmerCertification> getPendingCertifications() {
        return this.lambdaQuery()
                .eq(FarmerCertification::getStatus, 0) // 0表示待审批
                .list();
    }

    @Override
    public boolean approveCertification(Long certId, Long approverId) {
        FarmerCertification certification = this.getById(certId);
        if (certification != null) {
            certification.setStatus(1); // 1表示审批通过
            certification.setApproverId(approverId);
            return this.updateById(certification);
        }
        return false;
    }

    @Override
    public boolean rejectCertification(Long certId, Long approverId, String rejectReason) {
        FarmerCertification certification = this.getById(certId);
        if (certification != null) {
            certification.setStatus(2); // 2表示审批驳回
            certification.setApproverId(approverId);
            certification.setRejectReason(rejectReason);
            return this.updateById(certification);
        }
        return false;
    }
}