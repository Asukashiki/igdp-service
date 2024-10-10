package com.inspur.transformation.controller;


import com.inspur.transformation.service.IProxyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 区域
 */
@RestController
public class ProxyController {
    final IProxyService transfomationService;

    public ProxyController(IProxyService transfomationService) {
        this.transfomationService = transfomationService;
    }

    @PostMapping(value = "/dify/**/workflows/draft/run", produces = "text/event-stream;charset=UTF-8")
    public SseEmitter draftRun(HttpServletRequest request, HttpServletResponse response) {
        SseEmitter emitter = new SseEmitter();
        new Thread(new Runnable() {
            @Override
            public void run() {
                transfomationService.draftRun(request, response, emitter);
            }
        }).start();


        return emitter;
    }

    @RequestMapping(value = "/dify/**")
    public Object commit(HttpServletRequest request, HttpServletResponse response) {

        return transfomationService.difyProxy(request, response);

    }
    @RequestMapping(value = "/labelstudio/api/**")
    public Object labelStudioProxy(HttpServletRequest request, HttpServletResponse response) {

        return transfomationService.labelStudioProxy(request, response);

    }
    @RequestMapping(value = "/labelstudio/proxy/login",produces = "text/html;charset=utf-8")
    public Object  labelStudioProxyLogin(HttpServletRequest request, HttpServletResponse response) {

       return    transfomationService.labelStudioProxyLogin(request, response);

    }
}
