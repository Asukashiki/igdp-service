package com.inspur.data.treating.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.data.treating.domain.UnifyStatisticsItem;

import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName IUnifyStatisticsItemService
 * @date 2024/7/17 14:01
 */
public interface IUnifyStatisticsItemService extends IService<UnifyStatisticsItem> {
    /**
     * 查询列表
     * @param query 查询条件
     * @return 列表
     * */
    List<UnifyStatisticsItem> getList(UnifyStatisticsItem query);
    /**
     * 新增
     * @param item 指标内容
     * @return 结果
     * */
    AjaxResult addItem(UnifyStatisticsItem item);
    /**
     * 修改
     * @param item 指标内容
     * @return 结果
     * */
    AjaxResult editItem(UnifyStatisticsItem item);

    /**
     * 修改状态
     * @param itemId 更新状态
     * @param status 目标状态
     * @return 结果
     * */
    AjaxResult updateStatus(String itemId,String status);
}
