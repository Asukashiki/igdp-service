package com.inspur.farmland.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.farmland.domain.DaInfo;
import com.inspur.farmland.mapper.DaInfoMapper;
import com.inspur.farmland.service.IDaInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * DA信息Service实现类
 *
 * @author inspur
 */
@Service
public class DaInfoServiceImpl implements IDaInfoService {

    @Autowired
    private DaInfoMapper daInfoMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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

        // 加密密码
        if (StrUtil.isNotBlank(daInfo.getPassword())) {
            daInfo.setPassword(passwordEncoder.encode(daInfo.getPassword()));
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
        // 加密新密码
        String encodedPassword = passwordEncoder.encode(newPassword);

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
}
