package com.inspur.seed.Institution.multiplication.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inspur.seed.Institution.multiplication.domain.entity.BreedingAuditLog;
import com.inspur.seed.Institution.multiplication.domain.entity.BreedingOrgRegistration;
import com.inspur.seed.Institution.multiplication.domain.dto.BreedingOrgRegistrationDTO;
import com.inspur.seed.Institution.multiplication.mapper.BreedingAuditLogMapper;
import com.inspur.seed.Institution.multiplication.mapper.BreedingOrgRegistrationMapper;
import com.inspur.seed.Institution.multiplication.service.IBreedingOrgRegistrationService;
import com.inspur.seed.Institution.multiplication.domain.vo.BreedingOrgRegistrationDetailVO;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.DateUtils;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.common.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 繁殖机构注册申请服务实现类
 *
 * @author igdp
 */
@Service
public class BreedingOrgRegistrationServiceImpl extends ServiceImpl<BreedingOrgRegistrationMapper, BreedingOrgRegistration> implements IBreedingOrgRegistrationService {

    private static final Logger log = LoggerFactory.getLogger(BreedingOrgRegistrationServiceImpl.class);

    private static final Integer AUDIT_STATUS_PENDING = 0;
    private static final Integer AUDIT_STATUS_APPROVED = 1;
    private static final Integer AUDIT_STATUS_REJECTED = 2;

    private static final String ORG_TYPE_UNION = "UNION";
    private static final String ORG_TYPE_COOPERATIVE = "COOPERATIVE";

    private static final String AES_KEY = "ab489fe897hh78ha";

    @Autowired
    private BreedingOrgRegistrationMapper breedingOrgRegistrationMapper;

    @Autowired
    private BreedingAuditLogMapper breedingAuditLogMapper;

    @Value("${bsp.center.register.url:http://172.26.100.103:9403/rbac/user/register}")
    private String userCenterRegisterUrl;

    private RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<String> submitRegistration(BreedingOrgRegistrationDTO dto) {
        validateRegistrationDTO(dto);

        if (!checkUsernameUnique(dto.getApplyUsername(), dto.getId())) {
            throw new ServiceException("登录账号已存在，请更换账号");
        }

        BreedingOrgRegistration entity = new BreedingOrgRegistration();
        BeanUtils.copyProperties(dto, entity);

        if (StringUtils.hasText(dto.getApplyPassword())) {
            entity.setApplyPassword(aesEncrypt(dto.getApplyPassword()));
        }

        Date now = DateUtils.getNowDate();

        if (StringUtils.hasText(dto.getId())) {
            BreedingOrgRegistration existing = breedingOrgRegistrationMapper.selectById(dto.getId());
            if (existing == null || "2".equals(existing.getDelFlag())) {
                throw new ServiceException("注册申请不存在");
            }
            if (!Objects.equals(AUDIT_STATUS_REJECTED, existing.getAuditStatus())) {
                throw new ServiceException("只有驳回状态的申请才能重新提交");
            }
            entity.setAuditStatus(AUDIT_STATUS_PENDING);
            entity.setUpdateTime(now);
            breedingOrgRegistrationMapper.updateById(entity);
            return R.ok(entity.getId());
        } else {
            entity.setAuditStatus(AUDIT_STATUS_PENDING);
            entity.setCreateTime(now);
            entity.setUpdateTime(now);
            entity.setDelFlag("0");
            try {
                entity.setApplicantId(SecurityUtils.getUserId().toString());
            } catch (Exception e) {
                entity.setApplicantId("anonymous");
            }
            breedingOrgRegistrationMapper.insert(entity);
            return R.ok(entity.getId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<String> auditRegistration(String id, Integer auditResult, String auditComment) {
        if (!StringUtils.hasText(id)) {
            throw new ServiceException("注册申请ID不能为空");
        }
        if (auditResult == null || (auditResult != 1 && auditResult != 2)) {
            throw new ServiceException("审核结果无效");
        }

        BreedingOrgRegistration registration = breedingOrgRegistrationMapper.selectById(id);
        if (registration == null || "2".equals(registration.getDelFlag())) {
            throw new ServiceException("注册申请不存在");
        }

        if (!Objects.equals(AUDIT_STATUS_PENDING, registration.getAuditStatus())) {
            throw new ServiceException("该申请不是待审核状态");
        }

        registration.setAuditStatus(auditResult);
        registration.setUpdateTime(DateUtils.getNowDate());
        breedingOrgRegistrationMapper.updateById(registration);

        BreedingAuditLog auditLog = new BreedingAuditLog();
        auditLog.setRegistrationId(id);
        auditLog.setAuditResult(auditResult);
        auditLog.setAuditComment(auditComment);
        auditLog.setAuditTime(DateUtils.getNowDate());
        try {
            auditLog.setAuditorId(SecurityUtils.getUserId().toString());
            auditLog.setAuditorName(SecurityUtils.getUsername());
        } catch (Exception e) {
            auditLog.setAuditorId("system");
            auditLog.setAuditorName("System");
        }
        breedingAuditLogMapper.insert(auditLog);

        if (Objects.equals(AUDIT_STATUS_APPROVED, auditResult)) {
            syncUserToUserCenter(registration);
        }
        return R.ok("审核完成");
    }

    @Override
    public Map<String, Object> queryList(Map<String, Object> params) {
        LambdaQueryWrapper<BreedingOrgRegistration> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BreedingOrgRegistration::getDelFlag, "0");

        String orgName = (String) params.get("orgName");
        if (StringUtils.hasText(orgName)) {
            wrapper.like(BreedingOrgRegistration::getOrgName, orgName);
        }
        String orgType = (String) params.get("orgType");
        if (StringUtils.hasText(orgType)) {
            wrapper.eq(BreedingOrgRegistration::getOrgType, orgType);
        }
        Object auditStatus = params.get("auditStatus");
        if (auditStatus != null) {
            wrapper.eq(BreedingOrgRegistration::getAuditStatus, auditStatus);
        }

        wrapper.orderByDesc(BreedingOrgRegistration::getCreateTime);

        int pageNum = params.get("page") == null ? 1 : (int) params.get("page");
        int pageSize = params.get("pageSize") == null ? 10 : (int) params.get("pageSize");

        PageHelper.startPage(pageNum, pageSize);
        List<BreedingOrgRegistration> list = breedingOrgRegistrationMapper.selectList(wrapper);
        PageInfo<BreedingOrgRegistration> pageInfo = new PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("list", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        return result;
    }

    @Override
    public BreedingOrgRegistrationDetailVO getDetail(String id) {
        if (!StringUtils.hasText(id)) {
            throw new ServiceException("申请ID不能为空");
        }

        BreedingOrgRegistration registration = breedingOrgRegistrationMapper.selectById(id);
        if (registration == null || "2".equals(registration.getDelFlag())) {
            throw new ServiceException("注册申请不存在");
        }
        registration.setApplyPassword(null);

        LambdaQueryWrapper<BreedingAuditLog> logWrapper = new LambdaQueryWrapper<>();
        logWrapper.eq(BreedingAuditLog::getRegistrationId, id);
        logWrapper.orderByDesc(BreedingAuditLog::getAuditTime);
        List<BreedingAuditLog> auditLogs = breedingAuditLogMapper.selectList(logWrapper);

        BreedingOrgRegistrationDetailVO vo = new BreedingOrgRegistrationDetailVO();
        vo.setBaseInfo(registration);
        vo.setAuditLogs(auditLogs);
        return vo;
    }

    @Override
    public boolean checkUsernameUnique(String username, String excludeId) {
        if (!StringUtils.hasText(username)) {
            return false;
        }
        LambdaQueryWrapper<BreedingOrgRegistration> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BreedingOrgRegistration::getApplyUsername, username);
        wrapper.eq(BreedingOrgRegistration::getDelFlag, "0");
        if (StringUtils.hasText(excludeId)) {
            wrapper.ne(BreedingOrgRegistration::getId, excludeId);
        }
        return breedingOrgRegistrationMapper.selectCount(wrapper) == 0;
    }

    private void validateRegistrationDTO(BreedingOrgRegistrationDTO dto) {
        if (!StringUtils.hasText(dto.getOrgType())) {
            throw new ServiceException("机构类型不能为空");
        }
        if (!ORG_TYPE_UNION.equals(dto.getOrgType()) && !ORG_TYPE_COOPERATIVE.equals(dto.getOrgType())) {
            throw new ServiceException("机构类型无效");
        }
        if (!StringUtils.hasText(dto.getOrgName())) {
            throw new ServiceException("机构名称不能为空");
        }
        if (!StringUtils.hasText(dto.getLicenseNumber())) {
            throw new ServiceException("经营许可证号不能为空");
        }
        if (!StringUtils.hasText(dto.getApplyUsername())) {
            throw new ServiceException("登录账号不能为空");
        }
        if (!StringUtils.hasText(dto.getId()) && !StringUtils.hasText(dto.getApplyPassword())) {
            throw new ServiceException("登录密码不能为空");
        }
        if (!StringUtils.hasText(dto.getRegionCode())) {
            throw new ServiceException("所属区域不能为空");
        }
    }

    private void syncUserToUserCenter(BreedingOrgRegistration registration) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("account", registration.getApplyUsername());
            requestBody.put("name", registration.getOrgName());
            requestBody.put("password", encryptPasswordForUserCenter(registration.getApplyPassword()));
            requestBody.put("gender", "0");
            requestBody.put("identityNum", registration.getLicenseNumber());
            requestBody.put("mobile", registration.getContactMobile() != null ? registration.getContactMobile() : "");
            requestBody.put("email", registration.getContactEmail() != null ? registration.getContactEmail() : "");
            requestBody.put("regionCode", registration.getRegionCode());
            requestBody.put("regionName", registration.getRegionName() != null ? registration.getRegionName() : "");
            requestBody.put("orgCode", registration.getRegionCode());
            requestBody.put("orgName", registration.getRegionName() != null ? registration.getRegionName() : "");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Object> response = restTemplate.exchange(
                    userCenterRegisterUrl,
                    HttpMethod.POST,
                    entity,
                    Object.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("用户同步成功: account={}", registration.getApplyUsername());
            } else {
                log.error("用户同步失败: account={}, status={}", registration.getApplyUsername(), response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("用户同步异常: account={}", registration.getApplyUsername(), e);
        }
    }

    private String aesEncrypt(String plainText) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(AES_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new ServiceException("密码加密失败");
        }
    }

    private String aesDecrypt(String encryptedText) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(AES_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new ServiceException("密码解密失败");
        }
    }

    private String encryptPasswordForUserCenter(String storedEncryptedPassword) {
        String rawPassword = aesDecrypt(storedEncryptedPassword);
        return aesEncrypt(rawPassword);
    }
}
