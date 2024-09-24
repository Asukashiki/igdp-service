package com.inspur.assets.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.assets.domain.dto.AssetsStatisticsDto;
import com.inspur.assets.monitor.domain.AssetsAlertCur;
import com.inspur.common.core.domain.entity.UnifyStatisticsItemValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName AssetsAlertCurMapper
 * @date 2024/7/8 11:20
 */
@Mapper
public interface AssetsAlertCurMapper extends BaseMapper<AssetsAlertCur> {

    /**
     * 根据sql语句查询统计信息
     * @param sqlStr sql语句
     * @return 查询统计列表
     * */
    @Select("${sqlStr}")
    List<UnifyStatisticsItemValue> selectStatisticsItemValueListBySql(@Param("sqlStr")String sqlStr);

    /**
     * 查询近7日不同告警级别事件分布
     * @return
     */
    List<AssetsStatisticsDto> getStatisticsDtoListGroupByLocalization();
    /**
     * 应用系统查询告警信息
     * @param code
     * @return 集合
     */
    List<AssetsAlertCur> getWarnList(@Param("code")String code);
}
