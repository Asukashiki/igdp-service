package com.inspur.transformation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.transformation.domain.DifyUserRelationEntity;
import com.inspur.transformation.domain.CommonCommitParams;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface IDifyUserReleationService extends IService<DifyUserRelationEntity> {






    AjaxResult commonCommit(HttpServletRequest request, HttpServletResponse response);
}
