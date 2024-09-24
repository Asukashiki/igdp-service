package com.inspur.system.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.system.domain.SysLoginInfo;
import com.inspur.system.mapper.SysLoginInfoMapper;
import com.inspur.system.service.ISysLoginInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 系统访问日志情况信息 服务层处理
 *
 * @author liyunlong
 */
@Service
public class SysLoginInfoServiceImpl extends ServiceImpl<SysLoginInfoMapper, SysLoginInfo> implements ISysLoginInfoService {

    /**
     * 新增系统登录日志
     *
     * @param loginInfo 访问日志对象
     */
    @Override
    public void insertLoginInfo(SysLoginInfo loginInfo) {
        loginInfo.setLoginTime(LocalDateTime.now());
        save(loginInfo);
    }

    /**
     * 查询系统登录日志集合
     *
     * @param loginInfo 访问日志对象
     * @return 登录记录集合
     */
    @Override
    public List<SysLoginInfo> selectLogininforList(SysLoginInfo loginInfo) {
        return list(new LambdaQueryWrapper<SysLoginInfo>()
                .eq(null != loginInfo.getInfoId(), SysLoginInfo::getInfoId, loginInfo.getInfoId())
                .like(StrUtil.isNotEmpty(loginInfo.getIpaddr()), SysLoginInfo::getIpaddr, loginInfo.getIpaddr())
                .eq(StrUtil.isNotEmpty(loginInfo.getStatus()), SysLoginInfo::getStatus, loginInfo.getStatus())
                .eq(StrUtil.isNotEmpty(loginInfo.getUserName()), SysLoginInfo::getUserName, loginInfo.getUserName())
                .ge(null != loginInfo.getParams().get("beginTime"), SysLoginInfo::getLoginTime, loginInfo.getParams().get("beginTime"))
                .le(null != loginInfo.getParams().get("endTime"), SysLoginInfo::getLoginTime, loginInfo.getParams().get("endTime"))
                .orderByDesc(SysLoginInfo::getInfoId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteLoginInfoByIds(Long[] infoIds) {
        if (null != infoIds && infoIds.length > 0) {
            return removeByIds(Arrays.asList(infoIds));
        }
        return false;
    }

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLoginInfo() {
        this.baseMapper.cleanLoginInfo();
    }
}
