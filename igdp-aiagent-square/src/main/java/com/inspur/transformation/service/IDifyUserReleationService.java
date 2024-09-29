package com.inspur.transformation.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.transformation.domain.DifyUserRelationEntity;
import com.inspur.transformation.domain.CommonCommitParams;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface IDifyUserReleationService extends IService<DifyUserRelationEntity> {






    Object commonCommit(HttpServletRequest request, HttpServletResponse response);

    void draftRun(HttpServletRequest request, HttpServletResponse response, SseEmitter emitter);
}
