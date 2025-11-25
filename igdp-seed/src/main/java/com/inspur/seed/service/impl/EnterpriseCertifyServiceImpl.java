package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.LoginHelper;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.uuid.IdUtils;
import com.inspur.seed.domain.EnterpriseInfo;
import com.inspur.seed.mapper.EnterpriseInfoMapper;
import com.inspur.seed.service.IEnterpriseCertifyService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 企业认证服务实现类
 *
 * @author system
 */
@Service
public class EnterpriseCertifyServiceImpl extends ServiceImpl<EnterpriseInfoMapper, EnterpriseInfo> implements IEnterpriseCertifyService {

    @Override
    public String submitCertifyApplication(EnterpriseInfo enterpriseInfo) {
        // 校验统一社会信用代码是否已存在
        LambdaQueryWrapper<EnterpriseInfo> creditCodeWrapper = new LambdaQueryWrapper<>();
        creditCodeWrapper.eq(EnterpriseInfo::getUnifiedSocialCreditCode, enterpriseInfo.getUnifiedSocialCreditCode());
        if (count(creditCodeWrapper) > 0) {
            throw new ServiceException("统一社会信用代码已存在，请核对");
        }

        // 校验种子许可证编号是否已存在
        LambdaQueryWrapper<EnterpriseInfo> licenseWrapper = new LambdaQueryWrapper<>();
        licenseWrapper.eq(EnterpriseInfo::getSeedLicenseNo, enterpriseInfo.getSeedLicenseNo());
        if (count(licenseWrapper) > 0) {
            throw new ServiceException("种子许可证编号已存在，请核对");
        }

        // 生成企业ID
        String enterpriseId = "ENT" + IdUtils.fastSimpleUUID().substring(0, 16).toUpperCase();
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
