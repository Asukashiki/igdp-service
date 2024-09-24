package com.inspur.workorder.controller;

import cn.hutool.core.util.StrUtil;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.workorder.domain.vo.ProcessStatisticsVo;
import com.inspur.workorder.service.ITodoItemStatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName WorkOrderStatisticsController
 * @date 2024/6/28 10:32
 */
@RestController
@RequestMapping("/work-order/statistics")
public class WorkOrderStatisticsController extends BaseController {

    @Resource
    private ITodoItemStatisticsService todoItemStatisticsService;

    /**
     * 统计所有的工单
     * 工单总量、处理中、已办结、未解决
     */
    @GetMapping("/index")
    public AjaxResult getAllStatistics(Integer year, Integer month, String type,String startTimeStr,String endTimeStr) {
        LocalDateTime[] dates = handleDate(year, month, startTimeStr, endTimeStr);
        List<ProcessStatisticsVo> voList = todoItemStatisticsService.countAllWithState(dates[0], dates[1]);
        return success(voList);
    }

    private LocalDateTime[] handleDate(Integer year, Integer month, String startTimeStr, String endTimeStr) {
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (null != year || null != month) {
            if (null != month) {
                if (null == year) {
                    year = LocalDate.now().getYear();
                }
                LocalDate firstDay = LocalDate.of(year, month, 1);
                startTime = LocalDateTime.of(firstDay, LocalTime.MIN);
                LocalDate lastDay = firstDay.with(TemporalAdjusters.lastDayOfMonth());
                //截止日期是下月1号
                endTime = LocalDateTime.of(lastDay, LocalTime.MAX);
            } else {
                startTime = LocalDateTime.of(LocalDate.of(year, 1, 1), LocalTime.MIN);
                endTime = LocalDateTime.of(LocalDate.of(year, 12, 31), LocalTime.MAX);
            }
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if (StrUtil.isNotEmpty(startTimeStr)) {
                startTime = LocalDateTime.parse(startTimeStr, formatter);
            }
            if (StrUtil.isNotEmpty(endTimeStr)) {
                endTime = LocalDateTime.parse(endTimeStr, formatter);
            }
        }
        return new LocalDateTime[]{startTime, endTime};
    }
}
