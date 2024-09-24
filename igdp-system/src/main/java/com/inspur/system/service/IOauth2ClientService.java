package com.inspur.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.system.domain.Oauth2Client;

import java.util.List;

/**
 * @author liyunlong
 * @date 2024/1/25
 */
public interface IOauth2ClientService extends IService<Oauth2Client> {
    /**
     * 查询列表
     * @param queryParam 查询条件
     * @return 列表集合
     * */
    List<Oauth2Client> selectList(Oauth2Client queryParam);
    /**
     * 新增保存
     * @param client 保存信息
     * @return 结果
     * */
    AjaxResult addClient(Oauth2Client client);
    /**
     * 更新
     * @param client 更新内容
     * @return 结果
     * */
    AjaxResult updateClient(Oauth2Client client);
    /**
     * 变更状态
     * @param clientId id
     * @param enabled 目标状态
     * @return 结果
     * */
    boolean changeEnabled(String clientId,boolean enabled);
}
