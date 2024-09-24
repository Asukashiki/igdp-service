package com.inspur.workorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.common.core.domain.entity.UnifyStatisticsItemValue;
import com.inspur.workorder.domain.WorkOrderKnowledgeBase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


/**
 * @author liyunlong
 */
@Mapper
public interface WorkOrderKnowledgeBaseMapper extends BaseMapper<WorkOrderKnowledgeBase>{


    /**
     * 根据sql语句查询统计信息
     * @param sqlStr sql语句
     * @return 查询统计列表
     * */
    @Select("${sqlStr}")
    List<UnifyStatisticsItemValue> selectStatisticsItemValueListBySql(@Param("sqlStr")String sqlStr);


}
