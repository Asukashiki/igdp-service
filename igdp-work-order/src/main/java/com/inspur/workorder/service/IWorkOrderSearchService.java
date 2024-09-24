package com.inspur.workorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.workorder.domain.WorkOrderSearch;

import java.util.List;

/**
 * 知识库关键字查询Service
 * @author 王海龙
 * @date 2024/7/22
 */
public interface IWorkOrderSearchService extends IService<WorkOrderSearch> {

    /**
     * 关键字查询信息添加
     * @param keyword
     */
    void addSearch(String keyword);

    /**
     * 查询热门搜索
     * @return
     */
    List<WorkOrderSearch> queryFrequency();
}
