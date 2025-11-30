package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.LoginHelper;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.uuid.IdUtils;
import com.inspur.seed.domain.UnionInfo;
import com.inspur.seed.domain.UnionLicenseInfo;
import com.inspur.seed.domain.dto.UnionRegistrationDTO;
import com.inspur.seed.mapper.UnionInfoMapper;
import com.inspur.seed.mapper.UnionLicenseInfoMapper;
import com.inspur.seed.service.IUnionRegistrationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Union注册服务实现类
 *
 * @author system
 */
@Service
public class UnionRegistrationServiceImpl extends ServiceImpl<UnionInfoMapper, UnionInfo> implements IUnionRegistrationService {

    @Resource
    private UnionLicenseInfoMapper unionLicenseInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submitRegistration(UnionRegistrationDTO dto) {
        UnionInfo unionInfo = dto.getUnionInfo();
        UnionLicenseInfo licenseInfo = dto.getUnionLicenseInfo();

        String enterpriseId;
        String dataId;

        // 判断是新增还是更新
        if (StringUtils.isNotEmpty(unionInfo.getEnterpriseId())) {
            // 更新现有记录
            enterpriseId = unionInfo.getEnterpriseId();
            dataId = unionInfo.getDataId();

            // 设置认证状态为已注册（直接通过）
            unionInfo.setCertificationStatus(1);
            unionInfo.setOperationTime(LocalDateTime.now());
            unionInfo.setUpdateBy(LoginHelper.getUsername());
            unionInfo.setUpdateTime(LocalDateTime.now());

            // 更新Union基本信息
            updateById(unionInfo);

            // 更新许可信息
            licenseInfo.setEnterpriseId(enterpriseId);
            licenseInfo.setUpdateBy(LoginHelper.getUsername());
            licenseInfo.setUpdateTime(LocalDateTime.now());
            unionLicenseInfoMapper.updateById(licenseInfo);

        } else {
            // 新增记录
            // 生成企业ID和数据ID
            enterpriseId = "UNION" + IdUtils.fastSimpleUUID().substring(0, 14).toUpperCase();
            dataId = "DATA" + IdUtils.fastSimpleUUID().substring(0, 16).toUpperCase();

            unionInfo.setEnterpriseId(enterpriseId);
            unionInfo.setDataId(dataId);
            unionInfo.setCertificationStatus(1); // 直接设置为已注册（1=已通过）
            unionInfo.setOperationTime(LocalDateTime.now()); // 登记时间
            unionInfo.setUserId(LoginHelper.getUsername()); // 登记人代码
            unionInfo.setRegisterOrgCode(LoginHelper.getDeptId() != null ? LoginHelper.getDeptId().toString() : null); // 登记机构代码
            unionInfo.setCreateBy(LoginHelper.getUsername());
            unionInfo.setCreateTime(LocalDateTime.now());

            // 保存Union基本信息
            save(unionInfo);

            // 保存许可信息
            String licenseDataId = "LIC" + IdUtils.fastSimpleUUID().substring(0, 17).toUpperCase();
            licenseInfo.setDataId(licenseDataId);
            licenseInfo.setEnterpriseId(enterpriseId);
            licenseInfo.setCreateBy(LoginHelper.getUsername());
            licenseInfo.setCreateTime(LocalDateTime.now());
            unionLicenseInfoMapper.insert(licenseInfo);
        }

        return enterpriseId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveDraft(UnionRegistrationDTO dto) {
        UnionInfo unionInfo = dto.getUnionInfo();
        UnionLicenseInfo licenseInfo = dto.getUnionLicenseInfo();

        String enterpriseId;
        String dataId;

        // 判断是新增还是更新草稿
        if (StringUtils.isNotEmpty(unionInfo.getEnterpriseId())) {
            // 更新草稿
            enterpriseId = unionInfo.getEnterpriseId();

            // 验证只有草稿状态才能更新
            UnionInfo existingInfo = getById(unionInfo.getDataId());
            if (existingInfo != null && existingInfo.getCertificationStatus() != -1) {
                throw new ServiceException("只有草稿状态的记录才能更新草稿");
            }

            unionInfo.setCertificationStatus(-1); // 草稿状态
            unionInfo.setOperationTime(LocalDateTime.now());
            unionInfo.setUpdateBy(LoginHelper.getUsername());
            unionInfo.setUpdateTime(LocalDateTime.now());

            // 更新Union基本信息
            updateById(unionInfo);

            // 更新许可信息
            licenseInfo.setEnterpriseId(enterpriseId);
            licenseInfo.setUpdateBy(LoginHelper.getUsername());
            licenseInfo.setUpdateTime(LocalDateTime.now());
            unionLicenseInfoMapper.updateById(licenseInfo);

        } else {
            // 新增草稿
            enterpriseId = "UNION" + IdUtils.fastSimpleUUID().substring(0, 14).toUpperCase();
            dataId = "DATA" + IdUtils.fastSimpleUUID().substring(0, 16).toUpperCase();

            unionInfo.setEnterpriseId(enterpriseId);
            unionInfo.setDataId(dataId);
            unionInfo.setCertificationStatus(-1); // 草稿状态
            unionInfo.setOperationTime(LocalDateTime.now()); // 登记时间
            unionInfo.setUserId(LoginHelper.getUsername()); // 登记人代码
            unionInfo.setRegisterOrgCode(LoginHelper.getDeptId() != null ? LoginHelper.getDeptId().toString() : null); // 登记机构代码
            unionInfo.setCreateBy(LoginHelper.getUsername());
            unionInfo.setCreateTime(LocalDateTime.now());

            // 保存Union基本信息
            save(unionInfo);

            // 保存许可信息
            String licenseDataId = "LIC" + IdUtils.fastSimpleUUID().substring(0, 17).toUpperCase();
            licenseInfo.setDataId(licenseDataId);
            licenseInfo.setEnterpriseId(enterpriseId);
            licenseInfo.setCreateBy(LoginHelper.getUsername());
            licenseInfo.setCreateTime(LocalDateTime.now());
            unionLicenseInfoMapper.insert(licenseInfo);
        }

        return enterpriseId;
    }

    @Override
    public List<UnionInfo> queryList(String enterpriseName, String enterpriseRegistrationId,
                                      String unifiedSocialCreditCode, String seedEnterpriseLicenseNumber,
                                      String enterpriseType, Integer certificationStatus) {
        LambdaQueryWrapper<UnionInfo> wrapper = new LambdaQueryWrapper<>();

        // 企业名称筛选
        if (StringUtils.isNotEmpty(enterpriseName)) {
            wrapper.like(UnionInfo::getEnterpriseName, enterpriseName);
        }

        // 企业注册ID筛选
        if (StringUtils.isNotEmpty(enterpriseRegistrationId)) {
            wrapper.like(UnionInfo::getEnterpriseRegistrationId, enterpriseRegistrationId);
        }

        // 统一社会信用代码筛选
        if (StringUtils.isNotEmpty(unifiedSocialCreditCode)) {
            wrapper.like(UnionInfo::getUnifiedSocialCreditCode, unifiedSocialCreditCode);
        }

        // 种子企业许可证编号筛选
        if (StringUtils.isNotEmpty(seedEnterpriseLicenseNumber)) {
            wrapper.like(UnionInfo::getSeedEnterpriseLicenseNumber, seedEnterpriseLicenseNumber);
        }

        // 企业类型筛选
        if (StringUtils.isNotEmpty(enterpriseType)) {
            wrapper.eq(UnionInfo::getEnterpriseType, enterpriseType);
        }

        // 认证状态筛选
        if (certificationStatus != null) {
            wrapper.eq(UnionInfo::getCertificationStatus, certificationStatus);
        }

        // 按创建时间倒序排列
        wrapper.orderByDesc(UnionInfo::getCreateTime);

        return list(wrapper);
    }

    @Override
    public UnionRegistrationDTO queryByEnterpriseId(String enterpriseId) {
        // 查询Union基本信息
        LambdaQueryWrapper<UnionInfo> infoWrapper = new LambdaQueryWrapper<>();
        infoWrapper.eq(UnionInfo::getEnterpriseId, enterpriseId);
        UnionInfo unionInfo = getOne(infoWrapper);

        if (unionInfo == null) {
            return null;
        }

        // 查询许可信息
        LambdaQueryWrapper<UnionLicenseInfo> licenseWrapper = new LambdaQueryWrapper<>();
        licenseWrapper.eq(UnionLicenseInfo::getEnterpriseId, enterpriseId);
        UnionLicenseInfo licenseInfo = unionLicenseInfoMapper.selectOne(licenseWrapper);

        // 组装DTO
        UnionRegistrationDTO dto = new UnionRegistrationDTO();
        dto.setUnionInfo(unionInfo);
        dto.setUnionLicenseInfo(licenseInfo);

        return dto;
    }

    @Override
    public UnionRegistrationDTO queryByUserId(String userId) {
        // 查询Union基本信息
        LambdaQueryWrapper<UnionInfo> infoWrapper = new LambdaQueryWrapper<>();
        infoWrapper.eq(UnionInfo::getUserId, userId)
                .or()
                .eq(UnionInfo::getCreateBy, userId);
        infoWrapper.orderByDesc(UnionInfo::getCreateTime);
        infoWrapper.last("LIMIT 1");
        UnionInfo unionInfo = getOne(infoWrapper);

        if (unionInfo == null) {
            return null;
        }

        // 查询许可信息
        LambdaQueryWrapper<UnionLicenseInfo> licenseWrapper = new LambdaQueryWrapper<>();
        licenseWrapper.eq(UnionLicenseInfo::getEnterpriseId, unionInfo.getEnterpriseId());
        UnionLicenseInfo licenseInfo = unionLicenseInfoMapper.selectOne(licenseWrapper);

        // 组装DTO
        UnionRegistrationDTO dto = new UnionRegistrationDTO();
        dto.setUnionInfo(unionInfo);
        dto.setUnionLicenseInfo(licenseInfo);

        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByEnterpriseIds(String[] enterpriseIds) {
        if (enterpriseIds == null || enterpriseIds.length == 0) {
            return 0;
        }

        // 删除Union基本信息
        LambdaQueryWrapper<UnionInfo> infoWrapper = new LambdaQueryWrapper<>();
        infoWrapper.in(UnionInfo::getEnterpriseId, Arrays.asList(enterpriseIds));
        int count = baseMapper.delete(infoWrapper);

        // 删除许可信息（级联删除）
        LambdaQueryWrapper<UnionLicenseInfo> licenseWrapper = new LambdaQueryWrapper<>();
        licenseWrapper.in(UnionLicenseInfo::getEnterpriseId, Arrays.asList(enterpriseIds));
        unionLicenseInfoMapper.delete(licenseWrapper);

        return count;
    }

    @Override
    public void updateCertificationStatus(String enterpriseId, Integer status, String rejectReason) {
        LambdaQueryWrapper<UnionInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnionInfo::getEnterpriseId, enterpriseId);
        UnionInfo unionInfo = getOne(wrapper);

        if (unionInfo == null) {
            throw new ServiceException("Union信息不存在");
        }

        unionInfo.setCertificationStatus(status);
        if (StringUtils.isNotEmpty(rejectReason)) {
            unionInfo.setRejectReason(rejectReason);
        }
        unionInfo.setUpdateBy(LoginHelper.getUsername());
        unionInfo.setUpdateTime(LocalDateTime.now());

        updateById(unionInfo);
    }
}
