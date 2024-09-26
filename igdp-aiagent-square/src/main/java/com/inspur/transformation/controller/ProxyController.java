package com.inspur.transformation.controller;


import com.inspur.common.core.domain.AjaxResult;
import com.inspur.transformation.service.IDifyUserReleationService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 区域
 */
@RestController
public class ProxyController {
    final IDifyUserReleationService transfomationService;

    public ProxyController(IDifyUserReleationService transfomationService) {
        this.transfomationService = transfomationService;
    }


    @RequestMapping(value = "/dify/**")
    public AjaxResult commit(HttpServletRequest request, HttpServletResponse response) {

        return transfomationService.commonCommit(request,response);

    }








}
