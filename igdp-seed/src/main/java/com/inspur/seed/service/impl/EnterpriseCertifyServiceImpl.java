package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.LoginHelper;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.uuid.IdUtils;
import com.inspur.seed.domain.EnterpriseAudit;
import com.inspur.seed.domain.EnterpriseInfo;
import com.inspur.seed.mapper.EnterpriseAuditMapper;
import com.inspur.seed.mapper.EnterpriseInfoMapper;
import com.inspur.seed.service.IEnterpriseCertifyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 企业认证服务实现类
 *
 * @author system
 */
@Service
public class EnterpriseCertifyServiceImpl extends ServiceImpl<EnterpriseInfoMapper, EnterpriseInfo> implements IEnterpriseCertifyService {

    @Resource
    EnterpriseAuditMapper enterpriseAuditMapper;


    @Override
    public String submitCertifyApplication(EnterpriseInfo enterpriseInfo) {
        //根据enterpriseInfo.getenterpriseId()是否存在 存在更新不存在插入
        String enterpriseId;

        if(StringUtils.isNotEmpty(enterpriseInfo.getEnterpriseId())){
            enterpriseId = enterpriseInfo.getEnterpriseId();
            //更新
            // 设置认证状态为待审核
            enterpriseInfo.setCertificationStatus(0);
            // 设置操作时间
            enterpriseInfo.setOperationTime(LocalDateTime.now());

            enterpriseInfo.setUpdateBy(LoginHelper.getUsername());
            enterpriseInfo.setUpdateTime(LocalDateTime.now());
             updateById(enterpriseInfo);
        }else{
            // 生成企业ID
             enterpriseId = "ENT" + IdUtils.fastSimpleUUID().substring(0, 16).toUpperCase();
            enterpriseInfo.setEnterpriseId(enterpriseId);

            // 设置认证状态为待审核
            enterpriseInfo.setCertificationStatus(0);

            // 设置操作时间
            enterpriseInfo.setOperationTime(LocalDateTime.now());

            // 设置创建信息
            enterpriseInfo.setCreateBy(LoginHelper.getUsername());
            enterpriseInfo.setCreateTime(LocalDateTime.now());

            // 保存企业认证信息
            save(enterpriseInfo);

            //将当前提交的认证信息保存到审核记录表
            EnterpriseAudit enterpriseAudit = new EnterpriseAudit();
            enterpriseAudit.setAuditId("AUDIT" + IdUtils.fastSimpleUUID().substring(0, 16).toUpperCase());
            enterpriseAudit.setEnterpriseId(enterpriseId);
            enterpriseAudit.setAuditResult(0);
            enterpriseAudit.setAuditOpinion("待审核");
            enterpriseAudit.setAuditor(LoginHelper.getUsername());
            enterpriseAudit.setAuditTime(LocalDateTime.now());
            enterpriseAuditMapper.insert(enterpriseAudit);
        }
        return enterpriseId;
    }

    @Override
    public List<EnterpriseInfo> queryCertifyList(String keyword, String enterpriseType, Integer certificationStatus) {
        LambdaQueryWrapper<EnterpriseInfo> wrapper = new LambdaQueryWrapper<>();

        // 关键词查询：企业名称、信用代码、许可证编号
        if (StringUtils.isNotEmpty(keyword)) {
            wrapper.and(w -> w.like(EnterpriseInfo::getEnterpriseName, keyword)
                    .or().like(EnterpriseInfo::getUnifiedSocialCreditCode, keyword)
                    .or().like(EnterpriseInfo::getSeedLicenseNo, keyword));
        }

        // 企业类型筛选
        if (StringUtils.isNotEmpty(enterpriseType)) {
            wrapper.eq(EnterpriseInfo::getEnterpriseType, enterpriseType);
        }

        // 认证状态筛选
        if (certificationStatus != null) {
            wrapper.eq(EnterpriseInfo::getCertificationStatus, certificationStatus);
        }

        // 按创建时间倒序排列
        wrapper.orderByDesc(EnterpriseInfo::getCreateTime);

        return list(wrapper);
    }

    @Override
    public EnterpriseInfo queryByEnterpriseId(String enterpriseId) {
        return getById(enterpriseId);
    }

    @Override
    public EnterpriseInfo queryByUserId(String userId) {
        LambdaQueryWrapper<EnterpriseInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EnterpriseInfo::getCreateBy, userId);
        return getOne(wrapper);
    }

    @Override
    public void updateCertificationStatus(String enterpriseId, Integer status) {
        EnterpriseInfo enterpriseInfo = new EnterpriseInfo();
        enterpriseInfo.setEnterpriseId(enterpriseId);
        enterpriseInfo.setCertificationStatus(status);
        enterpriseInfo.setUpdateBy(LoginHelper.getUsername());
        enterpriseInfo.setUpdateTime(LocalDateTime.now());
        updateById(enterpriseInfo);
    }

    @Override
    public String saveDraft(EnterpriseInfo enterpriseInfo) {
        // 生成企业ID
        String enterpriseId = "ENT" + IdUtils.fastSimpleUUID().substring(0, 16).toUpperCase();
        enterpriseInfo.setEnterpriseId(enterpriseId);

        // 设置认证状态为草稿（-1）
        enterpriseInfo.setCertificationStatus(-1);

        // 设置操作时间
        enterpriseInfo.setOperationTime(LocalDateTime.now());

        // 设置创建信息
        enterpriseInfo.setCreateBy(LoginHelper.getUsername());
        enterpriseInfo.setCreateTime(LocalDateTime.now());

        // 保存企业草稿信息
        save(enterpriseInfo);

        return enterpriseId;
    }

    @Override
    public boolean updateDraft(EnterpriseInfo enterpriseInfo) {
        // 查询现有企业信息
        EnterpriseInfo existingInfo = queryByEnterpriseId(enterpriseInfo.getEnterpriseId());
        if (existingInfo == null) {
            throw new ServiceException("企业信息不存在");
        }

        // 只有草稿状态（-1）的记录才允许更新为草稿
        if (existingInfo.getCertificationStatus() != -1) {
            throw new ServiceException("只有草稿状态的企业信息才能更新草稿");
        }

        // 保持草稿状态
        enterpriseInfo.setCertificationStatus(-1);

        // 设置更新信息
        enterpriseInfo.setUpdateBy(LoginHelper.getUsername());
        enterpriseInfo.setUpdateTime(LocalDateTime.now());

        return updateById(enterpriseInfo);
    }
}
