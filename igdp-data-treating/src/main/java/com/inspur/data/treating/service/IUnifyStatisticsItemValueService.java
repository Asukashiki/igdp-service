package com.inspur.data.treating.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.common.core.domain.entity.UnifyStatisticsItemValue;

import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName IUnifyStatisticsItemValueService
 * @date 2024/7/17 14:11
 */
public interface IUnifyStatisticsItemValueService extends IService<UnifyStatisticsItemValue> {
    /**
     * 根据key获取统计数据
     * @param key key
     * @return 统计信息
     * */
    UnifyStatisticsItemValue getByKey(String key);
    /**
     * 根据查询条件获取统计数据列表
     * @param queryParam 查询条件
     * @return 集合
     * */
    List<UnifyStatisticsItemValue> getList(UnifyStatisticsItemValue queryParam);
    /**
     * 保存数据
     * @param unifyStatisticsItemValue 统计数据内容
     * */
    void saveValue(UnifyStatisticsItemValue unifyStatisticsItemValue);
    /**
     * 批量保存
     * @param itemValueList 统计内容集合
     * */
    void saveValueList(List<UnifyStatisticsItemValue> itemValueList);
}
