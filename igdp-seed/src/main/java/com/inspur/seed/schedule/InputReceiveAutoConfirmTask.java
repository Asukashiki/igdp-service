package com.inspur.seed.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inspur.seed.domain.invested.InputReceiveUnion;
import com.inspur.seed.domain.invested.InputReceiveWoreda;
import com.inspur.seed.mapper.invested.InputReceiveUnionMapper;
import com.inspur.seed.mapper.invested.InputReceiveWoredaMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 投入品接收自动确认定时任务
 * 每天凌晨扫描接收确认表（Union和Woreda），对入库时间超过10天且未确认的记录自动更新为"已确认"
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@Slf4j
@Component
public class InputReceiveAutoConfirmTask {

    @Resource
    private InputReceiveUnionMapper receiveUnionMapper;

    @Resource
    private InputReceiveWoredaMapper receiveWoredaMapper;

    /**
     * 自动确认接收任务
     * 每天凌晨2点执行
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void autoConfirmReceive() {
        log.info("开始执行投入品接收自动确认任务");

        try {
            // 查询待确认且入库时间超过10天的记录
            LocalDateTime tenDaysAgo = LocalDateTime.now().minusDays(10);

            // 处理Union接收确认
            int unionConfirmCount = autoConfirmUnion(tenDaysAgo);

            // 处理Woreda接收确认
            int woredaConfirmCount = autoConfirmWoreda(tenDaysAgo);

            log.info("投入品接收自动确认任务完成，Union确认{}条，Woreda确认{}条，共{}条记录",
                    unionConfirmCount, woredaConfirmCount, unionConfirmCount + woredaConfirmCount);

        } catch (Exception e) {
            log.error("投入品接收自动确认任务执行失败", e);
        }
    }

    /**
     * 自动确认Union接收记录
     */
    private int autoConfirmUnion(LocalDateTime tenDaysAgo) {
        LambdaQueryWrapper<InputReceiveUnion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InputReceiveUnion::getReceiveStatus, "Pending")
               .le(InputReceiveUnion::getCreateTime, tenDaysAgo);

        List<InputReceiveUnion> pendingList = receiveUnionMapper.selectList(wrapper);

        if (pendingList.isEmpty()) {
            log.info("没有需要自动确认的Union接收记录");
            return 0;
        }

        int confirmCount = 0;
        for (InputReceiveUnion receive : pendingList) {
            receive.setReceiveStatus("Confirmed");
            receive.setConfirmTime(LocalDateTime.now());
            receive.setConfirmBy("系统自动确认");
            receive.setConfirmOrg("系统");
            receive.setUpdateTime(LocalDateTime.now());

            int updated = receiveUnionMapper.updateById(receive);
            if (updated > 0) {
                confirmCount++;
                log.info("自动确认Union接收记录：releaseId={}, targetId={}",
                        receive.getReleaseId(), receive.getTargetId());
            }
        }

        return confirmCount;
    }

    /**
     * 自动确认Woreda接收记录
     */
    private int autoConfirmWoreda(LocalDateTime tenDaysAgo) {
        LambdaQueryWrapper<InputReceiveWoreda> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InputReceiveWoreda::getReceiveStatus, "Pending")
               .le(InputReceiveWoreda::getCreateTime, tenDaysAgo);

        List<InputReceiveWoreda> pendingList = receiveWoredaMapper.selectList(wrapper);

        if (pendingList.isEmpty()) {
            log.info("没有需要自动确认的Woreda接收记录");
            return 0;
        }

        int confirmCount = 0;
        for (InputReceiveWoreda receive : pendingList) {
            receive.setReceiveStatus("Confirmed");
            receive.setConfirmTime(LocalDateTime.now());
            receive.setConfirmBy("系统自动确认");
            receive.setConfirmOrg("系统");
            receive.setUpdateTime(LocalDateTime.now());

            int updated = receiveWoredaMapper.updateById(receive);
            if (updated > 0) {
                confirmCount++;
                log.info("自动确认Woreda接收记录：releaseId={}, targetId={}",
                        receive.getReleaseId(), receive.getTargetId());
            }
        }

        return confirmCount;
    }
}
