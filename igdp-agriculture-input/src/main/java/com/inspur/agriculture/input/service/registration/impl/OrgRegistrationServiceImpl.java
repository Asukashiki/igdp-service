package com.inspur.agriculture.input.service.registration.impl;

import cn.dev33.satoken.secure.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inspur.agriculture.input.domain.registration.AuditLog;
import com.inspur.agriculture.input.domain.registration.OrgRegistration;
import com.inspur.agriculture.input.dto.registration.AuditDTO;
import com.inspur.agriculture.input.dto.registration.OrgRegistrationDTO;
import com.inspur.agriculture.input.mapper.registration.AuditLogMapper;
import com.inspur.agriculture.input.mapper.registration.OrgRegistrationMapper;
import com.inspur.agriculture.input.service.registration.IOrgRegistrationService;
import com.inspur.agriculture.input.vo.registration.OrgRegistrationDetailVO;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.DateUtils;
import com.inspur.common.utils.SecurityUtils;
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
 * 机构注册申请服务实现类
 *
 * @author igdp
 */
@Service
public class OrgRegistrationServiceImpl implements IOrgRegistrationService {

    private static final Logger log = LoggerFactory.getLogger(OrgRegistrationServiceImpl.class);

    /**
     * 审核状态：待审核
     */
    private static final Integer AUDIT_STATUS_PENDING = 0;

    /**
     * 审核状态：已通过
     */
    private static final Integer AUDIT_STATUS_APPROVED = 1;

    /**
     * 审核状态：已驳回
     */
    private static final Integer AUDIT_STATUS_REJECTED = 2;

    /**
     * 机构类型：联合会
     */
    private static final String ORG_TYPE_UNION = "UNION";

    /**
     * 机构类型：合作社
     */
    private static final String ORG_TYPE_COOPERATIVE = "COOPERATIVE";

    /**
     * AES 加密密钥（与前端保持一致）
     */
    private static final String AES_KEY = "ab489fe897hh78ha";

    @Autowired
    private OrgRegistrationMapper orgRegistrationMapper;

    @Autowired
    private AuditLogMapper auditLogMapper;

    /**
     * 用户中心注册接口地址
     */
    @Value("${user.center.register.url:http://10.110.149.140:30012/auth/rbac/user/register}")
    private String userCenterRegisterUrl;

    private RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submitRegistration(OrgRegistrationDTO dto) {
        // 参数校验
        validateRegistrationDTO(dto);

        // 检查用户名唯一性
        if (!checkUsernameUnique(dto.getApplyUsername(), dto.getId())) {
            throw new ServiceException("登录账号已存在，请更换账号");
        }

        OrgRegistration entity = new OrgRegistration();
        BeanUtils.copyProperties(dto, entity);

        // 密码加密存储（使用AES可逆加密，以便审核通过时可以解密）
        if (StringUtils.hasText(dto.getApplyPassword())) {
            entity.setApplyPassword(aesEncrypt(dto.getApplyPassword()));
        }

        Date now = DateUtils.getNowDate();

        if (StringUtils.hasText(dto.getId())) {
            // 修改（驳回后重填）
            OrgRegistration existing = orgRegistrationMapper.selectById(dto.getId());
            if (existing == null || "2".equals(existing.getDelFlag())) {
                throw new ServiceException("注册申请不存在");
            }
            // 只有驳回状态才能重新提交
            if (!Objects.equals(AUDIT_STATUS_REJECTED, existing.getAuditStatus())) {
                throw new ServiceException("只有驳回状态的申请才能重新提交");
            }
            entity.setAuditStatus(AUDIT_STATUS_PENDING);
            entity.setUpdateTime(now);
            orgRegistrationMapper.updateById(entity);
            return entity.getId();
        } else {
            // 新增
            entity.setAuditStatus(AUDIT_STATUS_PENDING);
            entity.setCreateTime(now);
            entity.setUpdateTime(now);
            entity.setDelFlag("0");
            try {
                entity.setApplicantId(SecurityUtils.getUserId().toString());
            } catch (Exception e) {
                entity.setApplicantId("anonymous");
            }
            orgRegistrationMapper.insert(entity);
            return entity.getId();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void audit(AuditDTO dto) {
        // 参数校验
        if (!StringUtils.hasText(dto.getRegistrationId())) {
            throw new ServiceException("注册申请ID不能为空");
        }
        if (dto.getAuditResult() == null || (dto.getAuditResult() != 1 && dto.getAuditResult() != 2)) {
            throw new ServiceException("审核结果无效");
        }

        // 查询注册申请
        OrgRegistration registration = orgRegistrationMapper.selectById(dto.getRegistrationId());
        if (registration == null || "2".equals(registration.getDelFlag())) {
            throw new ServiceException("注册申请不存在");
        }

        // 校验当前用户权限
        validateAuditPermission(registration);

        // 检查是否为待审核状态
        if (!Objects.equals(AUDIT_STATUS_PENDING, registration.getAuditStatus())) {
            throw new ServiceException("该申请不是待审核状态");
        }

        // 更新审核状态
        registration.setAuditStatus(dto.getAuditResult());
        registration.setUpdateTime(DateUtils.getNowDate());
        orgRegistrationMapper.updateById(registration);

        // 插入审核日志
        AuditLog auditLog = new AuditLog();
        auditLog.setRegistrationId(dto.getRegistrationId());
        auditLog.setAuditResult(dto.getAuditResult());
        auditLog.setAuditComment(dto.getAuditComment());
        auditLog.setAuditTime(DateUtils.getNowDate());
        try {
            auditLog.setAuditorId(SecurityUtils.getUserId().toString());
            auditLog.setAuditorName(SecurityUtils.getUsername());
        } catch (Exception e) {
            auditLog.setAuditorId("system");
            auditLog.setAuditorName("System");
        }
        auditLogMapper.insert(auditLog);

        // 如果审核通过，同步账号到用户中心
        if (Objects.equals(AUDIT_STATUS_APPROVED, dto.getAuditResult())) {
            syncUserToUserCenter(registration);
        }
    }

    @Override
    public Map<String, Object> listRegistrations(OrgRegistration query, Integer page, Integer pageSize) {
        LambdaQueryWrapper<OrgRegistration> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrgRegistration::getDelFlag, "0");

        // 数据权限过滤
        applyDataScope(wrapper, query);

        // 动态查询条件
        if (StringUtils.hasText(query.getOrgName())) {
            wrapper.like(OrgRegistration::getOrgName, query.getOrgName());
        }
        if (query.getAuditStatus() != null) {
            wrapper.eq(OrgRegistration::getAuditStatus, query.getAuditStatus());
        }
        if (StringUtils.hasText(query.getOrgType())) {
            wrapper.eq(OrgRegistration::getOrgType, query.getOrgType());
        }

        wrapper.orderByDesc(OrgRegistration::getCreateTime);

        PageHelper.startPage(page, pageSize);
        List<OrgRegistration> list = orgRegistrationMapper.selectList(wrapper);
        PageInfo<OrgRegistration> pageInfo = new PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("list", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("page", pageInfo.getPageNum());
        result.put("pageSize", pageInfo.getPageSize());
        return result;
    }

    @Override
    public OrgRegistrationDetailVO getDetail(String id) {
        if (!StringUtils.hasText(id)) {
            throw new ServiceException("申请ID不能为空");
        }

        // 查询主表信息
        OrgRegistration registration = orgRegistrationMapper.selectById(id);
        if (registration == null || "2".equals(registration.getDelFlag())) {
            throw new ServiceException("注册申请不存在");
        }

        // 隐藏密码
        registration.setApplyPassword(null);

        // 查询审核历史
        LambdaQueryWrapper<AuditLog> logWrapper = new LambdaQueryWrapper<>();
        logWrapper.eq(AuditLog::getRegistrationId, id);
        logWrapper.orderByDesc(AuditLog::getAuditTime);
        List<AuditLog> auditLogs = auditLogMapper.selectList(logWrapper);

        // 组装返回结果
        OrgRegistrationDetailVO vo = new OrgRegistrationDetailVO();
        vo.setBaseInfo(registration);
        vo.setAuditLogs(auditLogs);
        return vo;
    }

    @Override
    public boolean checkUsernameUnique(String username, String excludeId) {
        if (!StringUtils.hasText(username)) {
            return false;
        }
        LambdaQueryWrapper<OrgRegistration> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrgRegistration::getApplyUsername, username);
        wrapper.eq(OrgRegistration::getDelFlag, "0");
        if (StringUtils.hasText(excludeId)) {
            wrapper.ne(OrgRegistration::getId, excludeId);
        }
        return orgRegistrationMapper.selectCount(wrapper) == 0;
    }

    /**
     * 验证注册申请DTO
     */
    private void validateRegistrationDTO(OrgRegistrationDTO dto) {
        if (!StringUtils.hasText(dto.getOrgType())) {
            throw new ServiceException("机构类型不能为空");
        }
        if (!ORG_TYPE_UNION.equals(dto.getOrgType()) && !ORG_TYPE_COOPERATIVE.equals(dto.getOrgType())) {
            throw new ServiceException("机构类型无效，必须为 UNION 或 COOPERATIVE");
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
        // 新增时密码必填
        if (!StringUtils.hasText(dto.getId()) && !StringUtils.hasText(dto.getApplyPassword())) {
            throw new ServiceException("登录密码不能为空");
        }
        if (!StringUtils.hasText(dto.getRegionCode())) {
            throw new ServiceException("所属区域不能为空");
        }
    }

    /**
     * 验证审核权限
     * 根据用户所属区域验证权限
     */
    private void validateAuditPermission(OrgRegistration registration) {
        // TODO: 根据实际权限系统实现
        // 验证当前用户是否有权限审核该区域的申请
    }

    /**
     * 应用数据权限过滤
     * 根据用户所属区域过滤数据
     */
    private void applyDataScope(LambdaQueryWrapper<OrgRegistration> wrapper, OrgRegistration query) {
        // 如果前端传了 regionCode，直接使用
        if (StringUtils.hasText(query.getRegionCode())) {
            wrapper.eq(OrgRegistration::getRegionCode, query.getRegionCode());
        }
        // TODO: 根据当前登录用户的权限级别自动过滤
    }

    /**
     * 同步用户到用户中心
     * 审核通过后调用用户中心注册接口
     */
    private void syncUserToUserCenter(OrgRegistration registration) {
        try {
            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("account", registration.getApplyUsername());
            requestBody.put("name", registration.getOrgName());
            // 密码需要使用 AES 加密（与前端加密方式一致）
            requestBody.put("password", encryptPasswordForUserCenter(registration.getApplyPassword()));
            requestBody.put("gender", "0"); // 默认性别
            requestBody.put("identityNum", registration.getLicenseNumber()); // 使用许可证号作为身份标识
            requestBody.put("mobile", registration.getContactMobile() != null ? registration.getContactMobile() : "");
            requestBody.put("email", registration.getContactEmail() != null ? registration.getContactEmail() : "");
            requestBody.put("regionCode", registration.getRegionCode());
            requestBody.put("regionName", registration.getRegionName() != null ? registration.getRegionName() : "");
            // 添加 orgCode 和 orgName，值与 regionCode 和 regionName 相同
            requestBody.put("orgCode", registration.getRegionCode());
            requestBody.put("orgName", registration.getRegionName() != null ? registration.getRegionName() : "");

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // 调用用户中心注册接口（API 可能返回 true/false 或 Map）
            ResponseEntity<Object> response = restTemplate.exchange(
                    userCenterRegisterUrl,
                    HttpMethod.POST,
                    entity,
                    Object.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                Object body = response.getBody();
                // 处理不同的返回类型：可能是 Boolean 或 Map
                if (body instanceof Boolean && Boolean.TRUE.equals(body)) {
                    log.info("用户同步到用户中心成功: account={}", registration.getApplyUsername());
                } else if (body instanceof Map) {
                    log.info("用户同步到用户中心成功: account={}, response={}", registration.getApplyUsername(), body);
                } else {
                    log.warn("用户同步到用户中心返回未知格式: account={}, response={}", registration.getApplyUsername(), body);
                }
            } else {
                log.error("用户同步到用户中心失败: account={}, status={}", registration.getApplyUsername(), response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("用户同步到用户中心异常: account={}", registration.getApplyUsername(), e);
            // 不抛出异常，避免影响审核流程。可根据业务需求调整
        }
    }

    /**
     * 使用 AES 加密
     * AES/ECB/PKCS5Padding，密钥: ab489fe897hh78ha
     *
     * @param plainText 明文
     * @return AES加密后的Base64字符串
     */
    private String aesEncrypt(String plainText) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(AES_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            log.error("AES加密失败", e);
            throw new ServiceException("密码加密失败");
        }
    }

    /**
     * 使用 AES 解密
     * AES/ECB/PKCS5Padding，密钥: ab489fe897hh78ha
     *
     * @param encryptedText AES加密后的Base64字符串
     * @return 解密后的明文
     */
    private String aesDecrypt(String encryptedText) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(AES_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("AES解密失败", e);
            throw new ServiceException("密码解密失败");
        }
    }

    /**
     * 为用户中心加密密码
     * 先从数据库中的AES加密密码解密，再用AES加密发送给用户中心
     *
     * @param storedEncryptedPassword 数据库中存储的AES加密密码
     * @return 用于用户中心的AES加密密码
     */
    private String encryptPasswordForUserCenter(String storedEncryptedPassword) {
        // 先解密获取原始密码
        String rawPassword = aesDecrypt(storedEncryptedPassword);
        // 再用AES加密发送给用户中心（使用相同的密钥）
        return aesEncrypt(rawPassword);
    }
}
