package com.inspur.transformation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.transformation.domain.DifyUserRelationEntity;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lijieming
 * @date 2024/9/25
 */
public interface IProxyService extends IService<DifyUserRelationEntity> {






    Object difyProxy(HttpServletRequest request, HttpServletResponse response);

    void draftRun(HttpServletRequest request, HttpServletResponse response, SseEmitter emitter);

    Object labelStudioProxy(HttpServletRequest request, HttpServletResponse response);

    Object labelStudioProxyLogin(HttpServletRequest request, HttpServletResponse response);
}
