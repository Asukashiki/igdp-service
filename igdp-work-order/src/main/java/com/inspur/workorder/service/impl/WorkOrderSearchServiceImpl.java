package com.inspur.workorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.utils.StringUtils;
import com.inspur.workorder.domain.WorkOrderKnowledgeFile;
import com.inspur.workorder.domain.WorkOrderSearch;
import com.inspur.workorder.mapper.WorkOrderSearchMapper;
import com.inspur.workorder.service.IWorkOrderSearchService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static com.inspur.common.utils.LoginHelper.getUsername;

/**
 * @author wanghailong
 */
@Service
public class WorkOrderSearchServiceImpl extends ServiceImpl<WorkOrderSearchMapper, WorkOrderSearch> implements IWorkOrderSearchService{
    @Override
    public void addSearch(String keyword) {
        WorkOrderSearch workOrderSearch = new WorkOrderSearch();
        if (StringUtils.isNotNull(keyword)){
            LambdaQueryWrapper<WorkOrderSearch> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(WorkOrderSearch::getSearch,keyword);
            List<WorkOrderSearch> search = list(queryWrapper);
            if(search!= null&&search.size()>0){
                for(WorkOrderSearch list : search){
                    list.setFrequency(list.getFrequency()+1);
                    updateById(list);
                }
            }else {
                workOrderSearch.setSearch(keyword);
                workOrderSearch.setFrequency(1);
                save(workOrderSearch);
            }
        }

    }

    @Override
    public List<WorkOrderSearch> queryFrequency() {
        LambdaQueryWrapper<WorkOrderSearch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(WorkOrderSearch::getFrequency);
        List<WorkOrderSearch> list = list(queryWrapper);
        return list.subList(0, Math.min(5, list.size()));
    }
}
