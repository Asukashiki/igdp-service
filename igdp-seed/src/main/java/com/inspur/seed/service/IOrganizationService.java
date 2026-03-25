package com.inspur.seed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.Organization;

import java.util.List;

public interface IOrganizationService extends IService<Organization> {

    List<Organization> selectOrganizationList(Organization organization);

    Organization selectOrganizationById(Long id);

    boolean createOrganization(Organization organization);

    boolean updateOrganization(Organization organization);

    boolean deleteOrganization(Long id);
}
