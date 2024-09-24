package com.inspur.data.treating.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.common.constant.Constants;
import com.inspur.common.utils.StringUtils;
import com.inspur.data.treating.domain.UnifyStatisticsItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName UnifyStatisticsItemMapper
 * @date 2024/7/17 11:51
 */
@Mapper
public interface UnifyStatisticsItemMapper extends BaseMapper<UnifyStatisticsItem> {
    /**
     * 获取列表
     *
     * @param queryParam 参数
     * @return 列表
     */
    default List<UnifyStatisticsItem> getList(UnifyStatisticsItem queryParam) {
        LambdaQueryWrapper<UnifyStatisticsItem> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(queryParam.getPeriod())) {
            wrapper.eq(UnifyStatisticsItem::getPeriod, queryParam.getPeriod());
        }
        if (StringUtils.isNotEmpty(queryParam.getDataSource())) {
            wrapper.eq(UnifyStatisticsItem::getDataSource, queryParam.getDataSource());
        }
        if (StringUtils.isNotEmpty(queryParam.getCategory())) {
            wrapper.eq(UnifyStatisticsItem::getCategory, queryParam.getCategory());
        }
        if (StringUtils.isNotEmpty(queryParam.getFrequency())) {
            wrapper.eq(UnifyStatisticsItem::getFrequency, queryParam.getFrequency());
        }
        if (StringUtils.isNotEmpty(queryParam.getStatus())) {
            wrapper.eq(UnifyStatisticsItem::getStatus, queryParam.getStatus());
        }
        if (StringUtils.isNotEmpty(queryParam.getCode())) {
            wrapper.like(UnifyStatisticsItem::getCode, queryParam.getCode());
        }
        if (StringUtils.isNotEmpty(queryParam.getName())) {
            wrapper.like(UnifyStatisticsItem::getName, queryParam.getName());
        }
        wrapper.eq(UnifyStatisticsItem::getDelFlag, Constants.DELETE_FLAG_VALID);
        return selectList(wrapper);
    }
}
