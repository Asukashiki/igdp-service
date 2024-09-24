package com.inspur.workorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.workorder.domain.WorkOrderKnowledgeBase;
import com.inspur.workorder.domain.WorkOrderSearch;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkOrderSearchMapper extends BaseMapper<WorkOrderSearch> {
}
