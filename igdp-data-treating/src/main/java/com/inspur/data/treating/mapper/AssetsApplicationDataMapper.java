package com.inspur.data.treating.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.common.core.domain.entity.UnifyStatisticsItemValue;
import com.inspur.data.treating.domain.AssetsApplicationData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName ApplicationDataMapper
 * @date 2024/7/17 14:56
 */
@Mapper
public interface AssetsApplicationDataMapper extends BaseMapper<AssetsApplicationData> {
    /**
     * 根据sql语句查询统计信息
     * @param sqlStr sql语句
     * @return 查询统计列表
     * */
    @Select("${sqlStr}")
    List<UnifyStatisticsItemValue> selectStatisticsItemValueListBySql(@Param("sqlStr")String sqlStr);
}
