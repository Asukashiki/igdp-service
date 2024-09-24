package com.inspur.assets.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.assets.monitor.domain.WebAutoTaskSummary;
import com.inspur.assets.monitor.domain.WebAutoTest;
import com.inspur.assets.monitor.mapper.WebAutoTaskSummaryMapper;
import com.inspur.assets.monitor.service.IWebAutoTaskSummaryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class WebAutoTaskSummaryServiceImpl extends ServiceImpl<WebAutoTaskSummaryMapper, WebAutoTaskSummary> implements IWebAutoTaskSummaryService {
    @Resource
    WebAutoTaskSummaryMapper webAutoTaskSummaryMapper;

    @Override
    public List<WebAutoTaskSummary> SelectAllContent(){
        QueryWrapper<WebAutoTaskSummary> queryWrapper=new QueryWrapper<>();
        queryWrapper.select("task_id","task_start_time","task_finish_time","task_test_total_number","task_test_success_count","task_test_failed_count","id");
        return webAutoTaskSummaryMapper.selectList(queryWrapper);
    }

    @Override
    public List<WebAutoTaskSummary> QueryTargetContent(WebAutoTaskSummary param){
        LambdaQueryWrapper<WebAutoTaskSummary> queryWrapper=new LambdaQueryWrapper<>();
        if(param.getTaskId()!=null && param.getTaskId()!=0) {
            queryWrapper.eq(WebAutoTaskSummary::getTaskId, param.getTaskId());
        }
        if (param.getTaskStartTime()!=null) {
            queryWrapper.like(WebAutoTaskSummary::getTaskStartTime, param.getTaskStartTime());
        }

        if (param.getTaskTestFailedCount() == 0) {
            queryWrapper.eq(WebAutoTaskSummary::getTaskTestFailedCount, 0);
        } else {
            queryWrapper.ge(WebAutoTaskSummary::getTaskTestFailedCount, param.getTaskTestFailedCount());
        }

        queryWrapper.select(WebAutoTaskSummary::getTaskId,
                WebAutoTaskSummary::getTaskStartTime,
                WebAutoTaskSummary::getTaskFinishTime,
                WebAutoTaskSummary::getTaskTestTotalNumber,
                WebAutoTaskSummary::getTaskTestSuccessCount,
                WebAutoTaskSummary::getTaskTestFailedCount,
                WebAutoTaskSummary::getId,
                WebAutoTaskSummary::getTaskId
                );
        return list(queryWrapper);
    }

    @Override
    public List<WebAutoTaskSummary>QueryTargetById(Integer id){
        LambdaQueryWrapper<WebAutoTaskSummary> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(WebAutoTaskSummary::getId,id);
        queryWrapper.select(WebAutoTaskSummary::getTaskId,
                WebAutoTaskSummary::getTaskStartTime,
                WebAutoTaskSummary::getTaskFinishTime,
                WebAutoTaskSummary::getTaskTestTotalNumber,
                WebAutoTaskSummary::getTaskTestSuccessCount,
                WebAutoTaskSummary::getTaskTestFailedCount,
                WebAutoTaskSummary::getId,
                WebAutoTaskSummary::getTaskId
        );
        return webAutoTaskSummaryMapper.selectList(queryWrapper);
    }

}
