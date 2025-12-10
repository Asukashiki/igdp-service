package com.inspur.agriculture.input.service.institution.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.input.constant.*;
import com.inspur.agriculture.input.domain.institution.dto.*;
import com.inspur.agriculture.input.domain.institution.entity.InputEnterpriseInfo;
import com.inspur.agriculture.input.domain.institution.entity.InputEnterpriseLicense;
import com.inspur.agriculture.input.domain.institution.entity.InputEnterpriseLocation;
import com.inspur.agriculture.input.domain.institution.entity.InputRegistrationAudit;
import com.inspur.agriculture.input.domain.institution.vo.*;
import com.inspur.agriculture.input.mapper.institution.InputEnterpriseInfoMapper;
import com.inspur.agriculture.input.mapper.institution.InputEnterpriseLicenseMapper;
import com.inspur.agriculture.input.mapper.institution.InputEnterpriseLocationMapper;
import com.inspur.agriculture.input.mapper.institution.InputRegistrationAuditMapper;
import com.inspur.agriculture.input.service.institution.IInputRegistrationService;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.LoginHelper;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 机构注册服务实现类
 *
 * @author system
 */
@Service
public class InputRegistrationServiceImpl extends ServiceImpl<InputEnterpriseInfoMapper, InputEnterpriseInfo> implements IInputRegistrationService {

    @Resource(name = "inputEnterpriseInfoMapper")
    private InputEnterpriseInfoMapper enterpriseInfoMapper;

    @Resource(name = "inputEnterpriseLocationMapper")
    private InputEnterpriseLocationMapper locationMapper;

    @Resource(name = "inputEnterpriseLicenseMapper")
    private InputEnterpriseLicenseMapper licenseMapper;

    @Resource(name = "inputRegistrationAuditMapper")
    private InputRegistrationAuditMapper auditMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addRegistration(InputEnterpriseAddDTO dto) {
        // 1. 校验必填字段
        validateRequiredFields(dto);

        // 2. 校验许可证有效期
        if (dto.getLicenseValidityStart().isAfter(dto.getLicenseValidityEnd())) {
            throw new ServiceException("License validity start date cannot be later than end date");
        }
        if (dto.getLicenseValidityEnd().isBefore(java.time.LocalDate.now())) {
            throw new ServiceException("License validity end date cannot be earlier than current date");
        }

        // 3. 转换并校验投入品类型
        List<String> inputTypeList = convertToList(dto.getInputTypes());
        for (String inputType : inputTypeList) {
            if (InputCategoryEnum.getByCode(inputType) == null) {
                throw new ServiceException("Invalid input type: " + inputType);
            }
        }

        // 4. 校验机构类型
        if (InputOrgTypeEnum.getByCode(dto.getOrgType()) == null) {
            throw new ServiceException("Invalid organization type: " + dto.getOrgType());
        }

        // 5. 转换销售区域
        List<String> salesRegionList = convertToList(dto.getSalesRegions());

        // 6. 保存机构基础信息
        InputEnterpriseInfo enterprise = new InputEnterpriseInfo();
        BeanUtils.copyBeanProp(enterprise, dto);
        enterprise.setInputTypes(String.join(",", inputTypeList));
        enterprise.setSalesRegions(String.join(",", salesRegionList));
        enterprise.setApplicationStatus(InputApplicationStatusEnum.DRAFT.getCode());
        enterprise.setCreatedBy(LoginHelper.getUserId().toString());
        enterprise.setCreatedTime(LocalDateTime.now());
        enterprise.setIsDeleted(0);
        enterprise.setVersion(1);

        enterpriseInfoMapper.insert(enterprise);
        String enterpriseId = enterprise.getId();

        // 6. 保存位置信息
        InputEnterpriseLocation location = new InputEnterpriseLocation();
        BeanUtils.copyBeanProp(location, dto.getLocation());
        location.setEnterpriseId(enterpriseId);
        location.setCreatedBy(LoginHelper.getUserId().toString());
        location.setCreatedTime(LocalDateTime.now());
        location.setIsDeleted(0);
        locationMapper.insert(location);

        // 7. 保存许可证件信息
        for (InputLicenseDTO licenseDTO : dto.getLicenses()) {
            // 校验证件类型
            if (InputLicenseTypeEnum.getByCode(licenseDTO.getLicenseType()) == null) {
                throw new ServiceException("Invalid license type: " + licenseDTO.getLicenseType());
            }

            InputEnterpriseLicense license = new InputEnterpriseLicense();
            BeanUtils.copyBeanProp(license, licenseDTO);
            license.setEnterpriseId(enterpriseId);
            license.setCreatedBy(LoginHelper.getUserId().toString());
            license.setCreatedTime(LocalDateTime.now());
            license.setIsDeleted(0);
            licenseMapper.insert(license);
        }

        return enterpriseId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRegistration(InputEnterpriseUpdateDTO dto) {
        // 1. 校验机构ID存在且未删除
        InputEnterpriseInfo existingEnterprise = enterpriseInfoMapper.selectById(dto.getId());
        if (existingEnterprise == null) {
            throw new ServiceException("Enterprise not found");
        }

        // 2. 校验申请状态为draft或rejected
        if (!InputApplicationStatusEnum.DRAFT.getCode().equals(existingEnterprise.getApplicationStatus()) &&
            !InputApplicationStatusEnum.REJECTED.getCode().equals(existingEnterprise.getApplicationStatus())) {
            throw new ServiceException("Only draft or rejected applications can be updated");
        }

        // 3. 校验许可证有效期
        if (dto.getLicenseValidityStart().isAfter(dto.getLicenseValidityEnd())) {
            throw new ServiceException("License validity start date cannot be later than end date");
        }
        if (dto.getLicenseValidityEnd().isBefore(java.time.LocalDate.now())) {
            throw new ServiceException("License validity end date cannot be earlier than current date");
        }

        // 4. 转换并校验投入品类型
        List<String> inputTypeList = convertToList(dto.getInputTypes());
        for (String inputType : inputTypeList) {
            if (InputCategoryEnum.getByCode(inputType) == null) {
                throw new ServiceException("Invalid input type: " + inputType);
            }
        }

        // 5. 转换销售区域
        List<String> salesRegionList = convertToList(dto.getSalesRegions());

        // 6. 更新机构基础信息
        InputEnterpriseInfo enterprise = new InputEnterpriseInfo();
        BeanUtils.copyBeanProp(enterprise, dto);
        enterprise.setInputTypes(String.join(",", inputTypeList));
        enterprise.setSalesRegions(String.join(",", salesRegionList));
        enterprise.setApplicationStatus(InputApplicationStatusEnum.DRAFT.getCode());
        enterprise.setUpdatedBy(LoginHelper.getUserId().toString());
        enterprise.setUpdatedTime(LocalDateTime.now());

        int result = enterpriseInfoMapper.updateById(enterprise);
        if (result == 0) {
            throw new ServiceException("Update failed, data may have been modified");
        }

        // 6. 更新位置信息
        if (dto.getLocation() != null) {
            InputEnterpriseLocation location = new InputEnterpriseLocation();
            BeanUtils.copyBeanProp(location, dto.getLocation());
            if (StringUtils.isEmpty(location.getId())) {
                // 新增
                location.setEnterpriseId(dto.getId());
                location.setCreatedBy(LoginHelper.getUserId().toString());
                location.setCreatedTime(LocalDateTime.now());
                location.setIsDeleted(0);
                locationMapper.insert(location);
            } else {
                // 更新
                location.setUpdatedBy(LoginHelper.getUserId().toString());
                location.setUpdatedTime(LocalDateTime.now());
                locationMapper.updateById(location);
            }
        }

        // 7. 删除原有许可证件
        LambdaQueryWrapper<InputEnterpriseLicense> licenseWrapper = new LambdaQueryWrapper<>();
        licenseWrapper.eq(InputEnterpriseLicense::getEnterpriseId, dto.getId());
        licenseMapper.delete(licenseWrapper);

        // 8. 新增许可证件
        for (InputLicenseDTO licenseDTO : dto.getLicenses()) {
            InputEnterpriseLicense license = new InputEnterpriseLicense();
            BeanUtils.copyBeanProp(license, licenseDTO);
            // 清除ID，让MyBatis-Plus自动生成新ID，避免主键冲突
            license.setId(null);
            license.setEnterpriseId(dto.getId());
            license.setCreatedBy(LoginHelper.getUserId().toString());
            license.setCreatedTime(LocalDateTime.now());
            license.setIsDeleted(0);
            licenseMapper.insert(license);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitRegistration(InputEnterpriseSubmitDTO dto) {
        // 1. 校验机构ID存在且未删除
        InputEnterpriseInfo enterprise = enterpriseInfoMapper.selectById(dto.getId());
        if (enterprise == null) {
            throw new ServiceException("Enterprise not found");
        }

        // 2. 校验当前状态为draft
        if (!InputApplicationStatusEnum.DRAFT.getCode().equals(enterprise.getApplicationStatus())) {
            throw new ServiceException("Only draft applications can be submitted");
        }

        // 3. 校验版本号
        if (!dto.getVersion().equals(enterprise.getVersion())) {
            throw new ServiceException("Data has been modified, please refresh and try again");
        }

        // 4. 更新申请状态为pending
        // MyBatis-Plus will automatically handle version optimistic locking
        enterprise.setApplicationStatus(InputApplicationStatusEnum.PENDING.getCode());
        enterprise.setUpdatedBy(LoginHelper.getUserId().toString());
        enterprise.setUpdatedTime(LocalDateTime.now());

        int result = enterpriseInfoMapper.updateById(enterprise);
        if (result == 0) {
            throw new ServiceException("Submit failed, data may have been modified");
        }
    }

    @Override
    public InputEnterpriseDetailVO getDetail(String id) {
        // 1. 查询机构基础信息
        InputEnterpriseInfo enterprise = enterpriseInfoMapper.selectById(id);
        if (enterprise == null) {
            throw new ServiceException("Enterprise not found");
        }

        InputEnterpriseDetailVO vo = new InputEnterpriseDetailVO();
        BeanUtils.copyBeanProp(vo, enterprise);

        // 转换inputTypes和salesRegions
        if (StringUtils.isNotEmpty(enterprise.getInputTypes())) {
            vo.setInputTypes(Arrays.asList(enterprise.getInputTypes().split(",")));
        }
        if (StringUtils.isNotEmpty(enterprise.getSalesRegions())) {
            // 按逗号分隔转换为List
            vo.setSalesRegions(Arrays.asList(enterprise.getSalesRegions().split(",")));
        }

        // 2. 查询位置信息
        LambdaQueryWrapper<InputEnterpriseLocation> locationWrapper = new LambdaQueryWrapper<>();
        locationWrapper.eq(InputEnterpriseLocation::getEnterpriseId, id);
        InputEnterpriseLocation location = locationMapper.selectOne(locationWrapper);
        if (location != null) {
            InputLocationVO locationVO = new InputLocationVO();
            BeanUtils.copyBeanProp(locationVO, location);
            vo.setLocation(locationVO);
        }

        // 3. 查询许可证件列表
        LambdaQueryWrapper<InputEnterpriseLicense> licenseWrapper = new LambdaQueryWrapper<>();
        licenseWrapper.eq(InputEnterpriseLicense::getEnterpriseId, id);
        List<InputEnterpriseLicense> licenses = licenseMapper.selectList(licenseWrapper);

        List<InputLicenseVO> licenseVOList = licenses.stream().map(license -> {
            InputLicenseVO licenseVO = new InputLicenseVO();
            BeanUtils.copyBeanProp(licenseVO, license);
            InputLicenseTypeEnum licenseType = InputLicenseTypeEnum.getByCode(license.getLicenseType());
            if (licenseType != null) {
                licenseVO.setLicenseTypeName(licenseType.getDesc());
            }
            return licenseVO;
        }).collect(Collectors.toList());
        vo.setLicenses(licenseVOList);

        // 4. 查询审核记录列表
        LambdaQueryWrapper<InputRegistrationAudit> auditWrapper = new LambdaQueryWrapper<>();
        auditWrapper.eq(InputRegistrationAudit::getEnterpriseId, id);
        auditWrapper.orderByDesc(InputRegistrationAudit::getAuditTime);
        List<InputRegistrationAudit> audits = auditMapper.selectList(auditWrapper);

        List<InputAuditRecordVO> auditRecordVOList = audits.stream().map(audit -> {
            InputAuditRecordVO auditVO = new InputAuditRecordVO();
            BeanUtils.copyBeanProp(auditVO, audit);
            InputAuditResultEnum auditResult = InputAuditResultEnum.getByCode(audit.getAuditResult());
            if (auditResult != null) {
                auditVO.setAuditResultName(auditResult.getDesc());
            }
            return auditVO;
        }).collect(Collectors.toList());
        vo.setAuditRecords(auditRecordVOList);

        return vo;
    }

    @Override
    public IPage<InputEnterprisePageVO> page(InputEnterprisePageDTO dto) {
        Page<InputEnterpriseInfo> page = new Page<>(dto.getPageNum(), dto.getPageSize());

        LambdaQueryWrapper<InputEnterpriseInfo> wrapper = new LambdaQueryWrapper<>();

        // 企业名称模糊查询
        if (StringUtils.isNotEmpty(dto.getEnterpriseName())) {
            wrapper.like(InputEnterpriseInfo::getEnterpriseName, dto.getEnterpriseName());
        }

        // 机构类型
        if (StringUtils.isNotEmpty(dto.getOrgType())) {
            wrapper.eq(InputEnterpriseInfo::getOrgType, dto.getOrgType());
        }

        // 申请状态
        if (StringUtils.isNotEmpty(dto.getApplicationStatus())) {
            wrapper.eq(InputEnterpriseInfo::getApplicationStatus, dto.getApplicationStatus());
        }

        // 投入品类型
        if (StringUtils.isNotEmpty(dto.getInputTypes())) {
            wrapper.like(InputEnterpriseInfo::getInputTypes, dto.getInputTypes());
        }

        // 创建时间范围
        if (dto.getCreatedTimeStart() != null) {
            wrapper.ge(InputEnterpriseInfo::getCreatedTime, dto.getCreatedTimeStart());
        }
        if (dto.getCreatedTimeEnd() != null) {
            wrapper.le(InputEnterpriseInfo::getCreatedTime, dto.getCreatedTimeEnd());
        }

        // 按创建时间倒序
        wrapper.orderByDesc(InputEnterpriseInfo::getCreatedTime);

        IPage<InputEnterpriseInfo> entityPage = enterpriseInfoMapper.selectPage(page, wrapper);

        // 转换为VO
        IPage<InputEnterprisePageVO> voPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        List<InputEnterprisePageVO> voList = entityPage.getRecords().stream().map(entity -> {
            InputEnterprisePageVO vo = new InputEnterprisePageVO();
            BeanUtils.copyBeanProp(vo, entity);

            // 设置机构类型名称
            InputOrgTypeEnum orgType = InputOrgTypeEnum.getByCode(entity.getOrgType());
            if (orgType != null) {
                vo.setOrgTypeName(orgType.getDesc());
            }

            // 设置申请状态名称
            InputApplicationStatusEnum status = InputApplicationStatusEnum.getByCode(entity.getApplicationStatus());
            if (status != null) {
                vo.setApplicationStatusName(status.getDesc());
            }

            // 查询位置信息获取woreda和zone
            LambdaQueryWrapper<InputEnterpriseLocation> locationWrapper = new LambdaQueryWrapper<>();
            locationWrapper.eq(InputEnterpriseLocation::getEnterpriseId, entity.getId());
            InputEnterpriseLocation location = locationMapper.selectOne(locationWrapper);
            if (location != null) {
                vo.setWoreda(location.getWoreda());
                vo.setZone(location.getZone());
            }

            return vo;
        }).collect(Collectors.toList());

        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRegistration(String id) {
        // 1. 校验机构ID存在
        InputEnterpriseInfo enterprise = enterpriseInfoMapper.selectById(id);
        if (enterprise == null) {
            throw new ServiceException("Enterprise not found");
        }

        // 2. 校验当前状态为draft
        if (!InputApplicationStatusEnum.DRAFT.getCode().equals(enterprise.getApplicationStatus())) {
            throw new ServiceException("Only draft applications can be deleted");
        }

        // 3. 逻辑删除机构基础信息
        enterpriseInfoMapper.deleteById(id);

        // 4. 逻辑删除位置信息
        LambdaQueryWrapper<InputEnterpriseLocation> locationWrapper = new LambdaQueryWrapper<>();
        locationWrapper.eq(InputEnterpriseLocation::getEnterpriseId, id);
        locationMapper.delete(locationWrapper);

        // 5. 逻辑删除许可证件
        LambdaQueryWrapper<InputEnterpriseLicense> licenseWrapper = new LambdaQueryWrapper<>();
        licenseWrapper.eq(InputEnterpriseLicense::getEnterpriseId, id);
        licenseMapper.delete(licenseWrapper);
    }

    /**
     * 校验必填字段
     */
    private void validateRequiredFields(InputEnterpriseAddDTO dto) {
        if (StringUtils.isEmpty(dto.getEnterpriseName())) {
            throw new ServiceException("Enterprise name is required");
        }
        if (StringUtils.isEmpty(dto.getSeedEnterpriseLicenseNumber())) {
            throw new ServiceException("Seed enterprise license number is required");
        }
        if (dto.getLicenseValidityStart() == null) {
            throw new ServiceException("License validity start date is required");
        }
        if (dto.getLicenseValidityEnd() == null) {
            throw new ServiceException("License validity end date is required");
        }
        if (StringUtils.isEmpty(dto.getEnterpriseType())) {
            throw new ServiceException("Enterprise type is required");
        }
        if (StringUtils.isEmpty(dto.getOrgType())) {
            throw new ServiceException("Organization type is required");
        }
        if (dto.getInputTypes() == null) {
            throw new ServiceException("Input types are required");
        }
        if (dto.getSalesRegions() == null) {
            throw new ServiceException("Sales regions are required");
        }
        if (dto.getLocation() == null) {
            throw new ServiceException("Location information is required");
        }
        if (dto.getLicenses() == null || dto.getLicenses().isEmpty()) {
            throw new ServiceException("Licenses are required");
        }
    }

    /**
     * 将Object转换为List<String>
     * 支持: 1. List<String> 2. String (逗号分隔) 3. String数组
     */
    @SuppressWarnings("unchecked")
    private List<String> convertToList(Object obj) {
        if (obj == null) {
            return new ArrayList<>();
        }

        if (obj instanceof List) {
            return (List<String>) obj;
        }

        if (obj instanceof String) {
            String str = (String) obj;
            if (StringUtils.isEmpty(str)) {
                return new ArrayList<>();
            }
            // 去除空格并分割
            return Arrays.asList(str.trim().split("\\s*,\\s*"));
        }

        if (obj instanceof String[]) {
            return Arrays.asList((String[]) obj);
        }

        throw new ServiceException("Invalid data format for list field");
    }
}
