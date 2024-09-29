package com.inspur.transformation.controller;


import com.inspur.transformation.service.IDifyUserReleationService;
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
    final IDifyUserReleationService transfomationService;

    public ProxyController(IDifyUserReleationService transfomationService) {
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

        return transfomationService.commonCommit(request, response);

    }


}
