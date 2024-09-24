package com.inspur.workorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.workorder.domain.WorkOrderKnowledgeBase;

import java.util.List;

/**
 * 知识库Service
 * @author 王海龙
 * @date 2024/7/2
 */
public interface IWorkOrderKnowledgeBaseService extends IService<WorkOrderKnowledgeBase> {
    /**
     * 新增知识库
     * @param workOrderKnowledgeBase 知识库内容
     * @return 结果
     * */
    AjaxResult addKnowledge(WorkOrderKnowledgeBase workOrderKnowledgeBase);

    /**
     * 知识库检索
     * @param keyword 关键字
     * @return 集合
     * */
    List<WorkOrderKnowledgeBase> getListByKeyword(String keyword);

    /**
     * 判断该目录下是否有知识库数据
     * @param id
     * @return
     */
    Boolean getBaseByid(String id);



    /**
     * 查询知识库
     * @param workOrderKnowledgeBase
     * @return
     */
    List<WorkOrderKnowledgeBase> getKlBaseList(WorkOrderKnowledgeBase workOrderKnowledgeBase);
    /**
     * 删除知识库数据
     * @param id
     * @return
     */
    AjaxResult deleteKnowledge(String[] id);

    /**
     * 修改知识库数据
     * @param workOrderKnowledgeBase
     * @return
     */
    AjaxResult updateKnowledge(WorkOrderKnowledgeBase workOrderKnowledgeBase);
    /**
     * 导入知识库数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importUser(List<WorkOrderKnowledgeBase> userList, Boolean isUpdateSupport, String operName);

}
