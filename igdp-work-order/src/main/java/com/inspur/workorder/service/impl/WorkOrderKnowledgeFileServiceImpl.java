package com.inspur.workorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.workorder.domain.WorkOrderKnowledgeBase;
import com.inspur.workorder.domain.WorkOrderKnowledgeFile;
import com.inspur.workorder.mapper.WorkOrderKnowledgeFileMapper;
import com.inspur.workorder.service.IWorkOrderKnowledgeFileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author liyunlong
 */
@Service
public class WorkOrderKnowledgeFileServiceImpl extends ServiceImpl<WorkOrderKnowledgeFileMapper, WorkOrderKnowledgeFile> implements IWorkOrderKnowledgeFileService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveList(WorkOrderKnowledgeBase workOrderKnowledgeBase) {
        //删除旧数据
        remove(new LambdaQueryWrapper<WorkOrderKnowledgeFile>().eq(WorkOrderKnowledgeFile::getBaseId, workOrderKnowledgeBase.getId()));
        //保存新数据
        if (null != workOrderKnowledgeBase.getFileList()) {
            for (WorkOrderKnowledgeFile file : workOrderKnowledgeBase.getFileList()) {
                if(null == file.getCreateTime()){
                    file.setCreateTime(LocalDateTime.now());
                }
                file.setBaseId(workOrderKnowledgeBase.getId());
            }
            saveBatch(workOrderKnowledgeBase.getFileList());
        }

    }

    @Override
    public List<WorkOrderKnowledgeFile> getFile(String id) {
        LambdaQueryWrapper<WorkOrderKnowledgeFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WorkOrderKnowledgeFile::getBaseId,id);
        return list(queryWrapper);
    }


}
