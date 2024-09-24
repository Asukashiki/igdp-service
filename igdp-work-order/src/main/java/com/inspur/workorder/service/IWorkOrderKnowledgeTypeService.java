package com.inspur.workorder.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.workorder.domain.WorkOrderKnowledgeBase;
import com.inspur.workorder.domain.WorkOrderKnowledgeType;

import java.util.List;

/**
 * 知识库菜单Service
 *  * @author 王海龙
 *  * @date 2024/7/2
 */
public interface IWorkOrderKnowledgeTypeService extends IService<WorkOrderKnowledgeType> {
    /**
     * 新增知识库菜单
     * @param workOrderKnowledgeType
     * @return 结果
     */
    AjaxResult addType(WorkOrderKnowledgeType workOrderKnowledgeType);
    /**
     * 删除知识库目录
     * @param id
     * @return
     */
    AjaxResult deleteKnowledgeType(String id);
    /**
     * 修改知识库目录
     * @param workOrderKnowledgeType
     * @return
     */
    AjaxResult updateKnowledgeType(WorkOrderKnowledgeType workOrderKnowledgeType);

    /**
     * 查询知识库目录
     * @return 集合
     */
    List<WorkOrderKnowledgeType> getTypeList();

    /**
     * 查询知识库菜单id
     * @param userList
     * @return
     */
    List<WorkOrderKnowledgeType> getKnowledgeType(List<WorkOrderKnowledgeBase> userList);

}
