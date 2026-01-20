package com.inspur.ucif.task;

import com.inspur.ucif.service.IAuthStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author liuhao17
 * @createDate 2026/1/6 14:59
 */
@Component
@Slf4j
public class ScheduledTask {
    
    @Autowired
    private IAuthStrategy bspAuthStrategy;
    
    /**
     * 每天凌晨2点执行组织结构同步任务
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void syncOrganizationStructure() {
        log.info("开始执行组织结构同步任务");
        try {
            // 调用组织结构同步服务
            // 通过BspAuthStrategy中的syncOrganization方法实现组织结构同步
            bspAuthStrategy.syncOrganization();
            
            log.info("组织结构同步任务执行完成");
        } catch (Exception e) {
            log.error("组织结构同步任务执行失败", e);
        }
    }
}
