package com.inspur.system.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.constant.CacheConstants;
import com.inspur.common.constant.Constants;
import com.inspur.common.constant.HttpStatus;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.redis.RedisCache;
import com.inspur.common.utils.LoginHelper;
import com.inspur.common.utils.StringUtils;
import com.inspur.system.domain.SysOpenApp;
import com.inspur.system.mapper.SysOpenAppMapper;
import com.inspur.system.service.ISysOpenAppService;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName SysOpenAppSerViceImpl
 * @date 2024/6/14 15:13
 */
@Service
public class SysOpenAppSerViceImpl extends ServiceImpl<SysOpenAppMapper, SysOpenApp> implements ISysOpenAppService {
    @Resource
    private RedisCache redisCache;

    @Override
    public SysOpenApp getSysOpenApp(String appId) {
        String key = CacheConstants.OPEN_APP_KEY + appId;
        SysOpenApp openApp;
        Boolean hasKey = redisCache.hasKey(key);
        if (null != hasKey && hasKey) {
            openApp = redisCache.getCacheObject(key);
        } else {
            openApp = getById(appId);
            if (null != openApp) {
                redisCache.setCacheObject(key, openApp);
                redisCache.expire(key, 10, TimeUnit.MINUTES);
            }
        }

        return openApp;
    }

    @Override
    public AjaxResult checkByAppid(String appid) {
        SysOpenApp app = getSysOpenApp(appid);
        if (null == app) {
            return AjaxResult.error(HttpStatus.BAD_REQUEST, "appid不存在");
        }
        if (!app.getStatus().equals(Constants.STATUS_VALID)) {
            return AjaxResult.error(HttpStatus.BAD_REQUEST, "状态不可用");
        }
        if (!app.getInvokeStatus().equals(SysOpenApp.UN_INVOKE)) {
            return AjaxResult.error(HttpStatus.BAD_REQUEST, "已被锁定，请稍后重试");
        }
        return AjaxResult.success();
    }

    @Override
    public List<SysOpenApp> getList(SysOpenApp queryParam) {
        LambdaQueryWrapper<SysOpenApp> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(queryParam.getAppid())) {
            wrapper.eq(SysOpenApp::getAppid, queryParam.getAppid());
        }
        if (StringUtils.isNotEmpty(queryParam.getStatus())) {
            wrapper.eq(SysOpenApp::getStatus, queryParam.getStatus());
        }
        if (StringUtils.isNotEmpty(queryParam.getInvokeStatus())) {
            wrapper.eq(SysOpenApp::getInvokeStatus, queryParam.getInvokeStatus());
        }
        if (StringUtils.isNotEmpty(queryParam.getAppName())) {
            wrapper.like(SysOpenApp::getAppName, queryParam.getAppName());
        }
        wrapper.orderByDesc(SysOpenApp::getCreateTime);
        return list(wrapper);
    }

    @Override
    public AjaxResult addApp(SysOpenApp sysOpenApp) {

        if (StringUtils.isNotEmpty(sysOpenApp.getAppid())) {
            SysOpenApp app = getSysOpenApp(sysOpenApp.getAppid());
            if (null != app) {
                return AjaxResult.error("应用ID已存在");
            }
        } else {
            sysOpenApp.setAppid(IdUtil.fastSimpleUUID());
        }
        if (StringUtils.isEmpty(sysOpenApp.getAppSecret())) {
            sysOpenApp.setAppSecret(IdUtil.fastSimpleUUID());
        }
        sysOpenApp.setCreateBy(LoginHelper.getUsername());
        sysOpenApp.setCreateTime(LocalDateTime.now());
        save(sysOpenApp);
        return AjaxResult.success(sysOpenApp);
    }

    @Override
    public AjaxResult editApp(SysOpenApp sysOpenApp) {

        sysOpenApp.setUpdateTime(LocalDateTime.now());
        sysOpenApp.setUpdateBy(LoginHelper.getUsername());
        boolean result = updateById(sysOpenApp);
        if (result) {
            return AjaxResult.success(sysOpenApp);
        } else {
            return AjaxResult.error(HttpStatus.BAD_REQUEST, "信息不存在，更新失败");
        }
    }

    @Override
    public boolean changeStatus(String appid, String status) {
        LambdaUpdateWrapper<SysOpenApp> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysOpenApp::getAppid, appid);
        wrapper.set(SysOpenApp::getStatus, status);
        wrapper.set(SysOpenApp::getUpdateTime, LocalDateTime.now());
        wrapper.set(SysOpenApp::getUpdateBy, LoginHelper.getUsername());
        return update(wrapper);
    }

    @Override
    public boolean changeInvokeStatus(String appid, String invokeStatus) {
        LambdaUpdateWrapper<SysOpenApp> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysOpenApp::getAppid, appid);
        wrapper.set(SysOpenApp::getInvokeStatus, invokeStatus);
        wrapper.set(SysOpenApp::getUpdateTime, LocalDateTime.now());
        wrapper.set(SysOpenApp::getUpdateBy, LoginHelper.getUsername());
        return update(wrapper);
    }

    @Override
    public boolean removeApp(String appId) {
        return removeById(appId);
    }

}
