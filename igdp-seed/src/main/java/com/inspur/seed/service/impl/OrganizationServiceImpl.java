package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.common.utils.StringUtils;
import com.inspur.seed.domain.Organization;
import com.inspur.seed.mapper.OrganizationMapper;
import com.inspur.seed.service.IOrganizationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization>
        implements IOrganizationService {

    @Override
    public List<Organization> selectOrganizationList(Organization organization) {
        LambdaQueryWrapper<Organization> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(organization.getKeyword())) {
            wrapper.and(w -> w
                    .like(Organization::getOrgCode, organization.getKeyword())
                    .or().like(Organization::getOrgName, organization.getKeyword())
                    .or().like(Organization::getContactPerson, organization.getKeyword()));
        } else {
            wrapper.like(StringUtils.isNotBlank(organization.getOrgCode()), Organization::getOrgCode, organization.getOrgCode())
                    .like(StringUtils.isNotBlank(organization.getOrgName()), Organization::getOrgName, organization.getOrgName())
                    .like(StringUtils.isNotBlank(organization.getRegion()), Organization::getRegion, organization.getRegion());
        }
        wrapper.eq(StringUtils.isNotBlank(organization.getOrgCategory()), Organization::getOrgCategory, organization.getOrgCategory())
                .eq(StringUtils.isNotBlank(organization.getStatus()), Organization::getStatus, organization.getStatus())
                .orderByDesc(Organization::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public Organization selectOrganizationById(Long id) {
        return this.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createOrganization(Organization organization) {
        validate(organization, false);
        organization.setCreateTime(LocalDateTime.now());
        organization.setUpdateTime(LocalDateTime.now());
        organization.setCreateBy(getCurrentUsername());
        organization.setUpdateBy(getCurrentUsername());
        return this.save(organization);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrganization(Organization organization) {
        if (organization == null || organization.getId() == null) {
            throw new ServiceException("Organization ID cannot be empty.");
        }
        validate(organization, true);
        organization.setUpdateTime(LocalDateTime.now());
        organization.setUpdateBy(getCurrentUsername());
        return this.updateById(organization);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteOrganization(Long id) {
        if (id == null) {
            throw new ServiceException("Organization ID cannot be empty.");
        }
        return this.removeById(id);
    }

    private void validate(Organization org, boolean isUpdate) {
        if (org == null) {
            throw new ServiceException("Organization data cannot be empty.");
        }
        if (StringUtils.isBlank(org.getOrgCode())) {
            throw new ServiceException("Organization code cannot be empty.");
        }
        if (StringUtils.isBlank(org.getOrgName())) {
            throw new ServiceException("Organization name cannot be empty.");
        }
        LambdaQueryWrapper<Organization> wrapper = new LambdaQueryWrapper<Organization>()
                .eq(Organization::getOrgCode, org.getOrgCode());
        if (isUpdate) {
            wrapper.ne(Organization::getId, org.getId());
        }
        if (this.count(wrapper) > 0) {
            throw new ServiceException("Organization code already exists.");
        }
    }

    private String getCurrentUsername() {
        try {
            return SecurityUtils.getUsername();
        } catch (Exception ex) {
            return "system";
        }
    }
}
