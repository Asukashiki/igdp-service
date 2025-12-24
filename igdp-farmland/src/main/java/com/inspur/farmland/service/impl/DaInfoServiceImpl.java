package com.inspur.farmland.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.farmland.domain.DaInfo;
import com.inspur.farmland.mapper.DaInfoMapper;
import com.inspur.farmland.service.IDaInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * DA信息Service实现类
 *
 * @author inspur
 */
@Service
public class DaInfoServiceImpl implements IDaInfoService {

    private static final Logger log = LoggerFactory.getLogger(DaInfoServiceImpl.class);

    /**
     * AES 加密密钥（与前端保持一致）
     */
    private static final String AES_KEY = "ab489fe897hh78ha";

    @Autowired
    private DaInfoMapper daInfoMapper;

    /**
     * 用户中心注册接口地址
     */
    @Value("${user.center.register.url:http://172.26.100.103:9403/rbac/user/register}")
    private String userCenterRegisterUrl;

    private RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        this.restTemplate = new RestTemplate();
    }


    @Override
    public List<DaInfo> selectDaInfoList(DaInfo daInfo) {
        LambdaQueryWrapper<DaInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DaInfo::getStatus, "1"); // 只查询未删除的数据

        // 条件查询
        if (StrUtil.isNotBlank(daInfo.getDaName())) {
            wrapper.like(DaInfo::getDaName, daInfo.getDaName());
        }
        if (StrUtil.isNotBlank(daInfo.getDaId())) {
            wrapper.eq(DaInfo::getDaId, daInfo.getDaId());
        }
        if (StrUtil.isNotBlank(daInfo.getPhone())) {
            wrapper.like(DaInfo::getPhone, daInfo.getPhone());
        }
        if (StrUtil.isNotBlank(daInfo.getWoredaCode())) {
            wrapper.eq(DaInfo::getWoredaCode, daInfo.getWoredaCode());
        }
        if (StrUtil.isNotBlank(daInfo.getAccountStatus())) {
            wrapper.eq(DaInfo::getAccountStatus, daInfo.getAccountStatus());
        }

        // 按创建时间倒序
        wrapper.orderByDesc(DaInfo::getCreateTime);

        return daInfoMapper.selectList(wrapper);
    }

    @Override
    public DaInfo selectDaInfoByDaId(String daId) {
        LambdaQueryWrapper<DaInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DaInfo::getDaId, daId)
               .eq(DaInfo::getStatus, "1");
        return daInfoMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertDaInfo(DaInfo daInfo) {
        // 生成DA编码
        String daId = "DA" + IdUtil.getSnowflakeNextIdStr();
        daInfo.setDaId(daId);

        // 加密密码（本地存储改为AES加密，对齐BreedingOrgRegistrationServiceImpl模式）
        if (StrUtil.isNotBlank(daInfo.getPassword())) {
            daInfo.setPassword(aesEncrypt(daInfo.getPassword()));
        }

        // 设置默认值
        daInfo.setAccountStatus("1"); // 默认启用
        daInfo.setStatus("1"); // 正常状态

        // 设置创建信息
        try {
            String username = SecurityUtils.getUsername();
            daInfo.setCreateBy(username);
        } catch (Exception e) {
            daInfo.setCreateBy("system");
        }

        // 插入数据
        daInfoMapper.insert(daInfo);

        // 同步到用户中心
        syncToUserCenter(daInfo);

        return daId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDaInfo(String daId, DaInfo daInfo) {
        // 设置更新信息
        try {
            String username = SecurityUtils.getUsername();
            daInfo.setUpdateBy(username);
        } catch (Exception e) {
            daInfo.setUpdateBy("system");
        }

        // 不允许修改密码、账号、状态等敏感字段
        daInfo.setPassword(null);
        daInfo.setAccount(null);
        daInfo.setAccountStatus(null);
        daInfo.setDaId(null);

        // 更新数据
        LambdaUpdateWrapper<DaInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(DaInfo::getDaId, daId)
               .eq(DaInfo::getStatus, "1");

        return daInfoMapper.update(daInfo, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteDaInfoByDaId(String daId) {
        // 逻辑删除
        LambdaUpdateWrapper<DaInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(DaInfo::getDaId, daId)
               .eq(DaInfo::getStatus, "1")
               .set(DaInfo::getStatus, "0")
               .set(DaInfo::getAccountStatus, "0"); // 同时禁用账号

        try {
            String username = SecurityUtils.getUsername();
            wrapper.set(DaInfo::getUpdateBy, username);
        } catch (Exception e) {
            wrapper.set(DaInfo::getUpdateBy, "system");
        }

        return daInfoMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateAccountStatus(String daId, String accountStatus) {
        LambdaUpdateWrapper<DaInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(DaInfo::getDaId, daId)
               .eq(DaInfo::getStatus, "1")
               .set(DaInfo::getAccountStatus, accountStatus);

        try {
            String username = SecurityUtils.getUsername();
            wrapper.set(DaInfo::getUpdateBy, username);
        } catch (Exception e) {
            wrapper.set(DaInfo::getUpdateBy, "system");
        }

        return daInfoMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int resetPassword(String daId, String newPassword) {
        // 加密新密码 (AES)
        String encodedPassword = aesEncrypt(newPassword);

        LambdaUpdateWrapper<DaInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(DaInfo::getDaId, daId)
               .eq(DaInfo::getStatus, "1")
               .set(DaInfo::getPassword, encodedPassword);

        try {
            String username = SecurityUtils.getUsername();
            wrapper.set(DaInfo::getUpdateBy, username);
        } catch (Exception e) {
            wrapper.set(DaInfo::getUpdateBy, "system");
        }

        return daInfoMapper.update(null, wrapper);
    }

    @Override
    public List<Map<String, Object>> selectDaOptions(String kebeleCode) {
        LambdaQueryWrapper<DaInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DaInfo::getStatus, "1")
               .eq(DaInfo::getAccountStatus, "1") // 只显示启用的账号
               .select(DaInfo::getDaId, DaInfo::getDaName, DaInfo::getPhone);

        // 如果指定了村代码，筛选负责该村的DA
        if (StrUtil.isNotBlank(kebeleCode)) {
            wrapper.like(DaInfo::getKebeleCodes, kebeleCode);
        }

        List<DaInfo> list = daInfoMapper.selectList(wrapper);

        // 转换为Map列表
        List<Map<String, Object>> options = new ArrayList<>();
        for (DaInfo da : list) {
            Map<String, Object> option = new HashMap<>();
            option.put("daId", da.getDaId());
            option.put("daName", da.getDaName());
            option.put("phone", da.getPhone());
            options.add(option);
        }

        return options;
    }

    @Override
    public boolean checkDaIdUnique(String daId) {
        LambdaQueryWrapper<DaInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DaInfo::getDaId, daId)
               .eq(DaInfo::getStatus, "1");
        return daInfoMapper.selectCount(wrapper) == 0;
    }

    @Override
    public boolean checkIdCardUnique(String idCard, String daId) {
        LambdaQueryWrapper<DaInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DaInfo::getIdCard, idCard)
               .eq(DaInfo::getStatus, "1");

        // 修改时排除自己
        if (StrUtil.isNotBlank(daId)) {
            wrapper.ne(DaInfo::getDaId, daId);
        }

        return daInfoMapper.selectCount(wrapper) == 0;
    }

    @Override
    public boolean checkAccountUnique(String account, String daId) {
        LambdaQueryWrapper<DaInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DaInfo::getAccount, account)
               .eq(DaInfo::getStatus, "1");

        // 修改时排除自己
        if (StrUtil.isNotBlank(daId)) {
            wrapper.ne(DaInfo::getDaId, daId);
        }

        return daInfoMapper.selectCount(wrapper) == 0;
    }

    /**
     * 同步DA用户到用户中心
     * 
     * @param daInfo DA信息
     */
    private void syncToUserCenter(DaInfo daInfo) {
        try {
            // 构建请求体 (参数名对齐参考类)
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("account", daInfo.getAccount());
            requestBody.put("name", daInfo.getDaName());
            // 密码在同步时需要再次AES加密 (对齐参考类 encryptPasswordForUserCenter 逻辑)
            requestBody.put("password", encryptPasswordForUserCenter(daInfo.getPassword()));
            requestBody.put("gender", "MALE".equals(daInfo.getGender()) ? "0" : "1");
            requestBody.put("identityNum", daInfo.getIdCard() != null ? daInfo.getIdCard() : "");
            requestBody.put("mobile", daInfo.getPhone() != null ? daInfo.getPhone() : "");
            requestBody.put("email", daInfo.getEmail() != null ? daInfo.getEmail() : "");
            
            // 区域/组织信息映射
            requestBody.put("regionCode", daInfo.getWoredaCode());
            requestBody.put("regionName", ""); 
            requestBody.put("orgCode", daInfo.getWoredaCode());
            requestBody.put("orgName", "");

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // 调用用户中心注册接口
            ResponseEntity<Object> response = restTemplate.exchange(
                    userCenterRegisterUrl,
                    HttpMethod.POST,
                    entity,
                    Object.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("DA用户同步到用户中心成功: account={}", daInfo.getAccount());
            } else {
                log.error("DA用户同步到用户中心失败: account={}, status={}", daInfo.getAccount(), response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("DA用户同步异常: account={}", daInfo.getAccount(), e);
        }
    }

    /**
     * 使用 AES 加密
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
            throw new com.inspur.common.exception.ServiceException("密码加密失败");
        }
    }

    /**
     * 使用 AES 解密
     *
     * @param encryptedText 加密文本
     * @return 明文
     */
    private String aesDecrypt(String encryptedText) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(AES_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new com.inspur.common.exception.ServiceException("密码解密失败");
        }
    }

    /**
     * 为用户中心注册加密密码（逻辑参考 BreedingOrgRegistrationServiceImpl）
     */
    private String encryptPasswordForUserCenter(String storedEncryptedPassword) {
        String rawPassword = aesDecrypt(storedEncryptedPassword);
        return aesEncrypt(rawPassword);
    }
}
