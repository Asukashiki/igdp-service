package com.inspur.workorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.workorder.domain.WorkOrderKnowledgeBase;
import com.inspur.workorder.domain.WorkOrderKnowledgeFile;

import java.util.List;

public interface IWorkOrderKnowledgeFileService extends IService<WorkOrderKnowledgeFile> {

    /**
     * 根据知识库保存附件内容
     * @param workOrderKnowledgeBase
     * */
    void saveList(WorkOrderKnowledgeBase workOrderKnowledgeBase);

    /**
     * 根据id查询附件
     * @param id
     * @return 集合
     */
    List<WorkOrderKnowledgeFile> getFile(String id);


}
