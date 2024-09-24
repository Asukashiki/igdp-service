package com.inspur.system.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.utils.LoginHelper;
import com.inspur.system.domain.Oauth2Client;
import com.inspur.system.mapper.Oauth2ClientMapper;
import com.inspur.system.service.IOauth2ClientService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author liyunlong
 * @date 2024/1/25
 */
@Service("oauth2ClientService")
public class Oauth2ClientServiceImpl extends ServiceImpl<Oauth2ClientMapper, Oauth2Client> implements IOauth2ClientService {

    /**
     * 查询列表
     *
     * @param queryParam 查询条件
     * @return 列表集合
     */
    @Override
    public List<Oauth2Client> selectList(Oauth2Client queryParam) {
        LambdaQueryWrapper<Oauth2Client> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotEmpty(queryParam.getClientId()), Oauth2Client::getClientId, queryParam.getClientId());
        queryWrapper.like(StrUtil.isNotEmpty(queryParam.getClientName()), Oauth2Client::getClientName, queryParam.getClientName());
        queryWrapper.eq(StrUtil.isNotEmpty(queryParam.getStatus()), Oauth2Client::getStatus, queryParam.getStatus());
        return list(queryWrapper);
    }

    @Override
    public AjaxResult addClient(Oauth2Client client) {
        //判断clientId是否重复
        if (StrUtil.isNotEmpty(client.getClientId())) {
            Oauth2Client currentClient = getById(client.getClientId());
            if (null != currentClient) {
                return AjaxResult.error("client_id已经存在");
            }
        } else {
            client.setClientId(IdUtil.fastSimpleUUID());
        }
        //随机生成10位秘钥
        if (StrUtil.isEmpty(client.getClientSecret())) {
            client.setClientSecret(RandomUtil.randomString(10));
        }
        client.setCreateBy(LoginHelper.getUserId());
        client.setCreateTime(LocalDateTime.now());
        boolean result = save(client);
        return AjaxResult.success(result);
    }

    @Override
    public AjaxResult updateClient(Oauth2Client client) {
        //判断旧数据是否存在
        if (StrUtil.isEmpty(client.getClientId())) {
            return AjaxResult.error("client_id不能为空");
        }
        client.setUpdateBy(LoginHelper.getUserId());
        client.setUpdateTime(LocalDateTime.now());
        boolean result = updateById(client);
        if (result) {
            return AjaxResult.success("保存成功");
        } else {
            return AjaxResult.error("信息不存在");
        }
    }

    @Override
    public boolean changeEnabled(String clientId, boolean enabled) {
        LambdaUpdateWrapper<Oauth2Client> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Oauth2Client::getStatus, enabled);
        updateWrapper.set(Oauth2Client::getUpdateTime, LocalDateTime.now());
        updateWrapper.set(Oauth2Client::getUpdateBy, LoginHelper.getUserId());
        updateWrapper.eq(Oauth2Client::getClientId, clientId);
        return update(updateWrapper);
    }
}
