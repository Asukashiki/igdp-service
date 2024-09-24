package com.inspur.workorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.constant.Constants;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.domain.entity.SysUser;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.StringUtils;
import com.inspur.system.service.ISysUserService;
import com.inspur.workorder.domain.WorkOrderKnowledgeBase;
import com.inspur.workorder.mapper.WorkOrderKnowledgeBaseMapper;
import com.inspur.workorder.service.IWorkOrderKnowledgeBaseService;
import com.inspur.workorder.service.IWorkOrderKnowledgeFileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.inspur.common.utils.LoginHelper.getUsername;

/**
 * @author liyunlong
 */
@Service
public class WorkOrderKnowledgeBaseServiceImpl extends ServiceImpl<WorkOrderKnowledgeBaseMapper, WorkOrderKnowledgeBase> implements IWorkOrderKnowledgeBaseService {
    @Resource
    private IWorkOrderKnowledgeFileService workOrderKnowledgeFileService;
    @Resource
    private ISysUserService userService;

    @Override
    public AjaxResult addKnowledge(WorkOrderKnowledgeBase workOrderKnowledgeBase) {
        //todo 判断名称是否已经存在
         workOrderKnowledgeBase.setCreateTime(LocalDateTime.now());
        workOrderKnowledgeBase.setCreateBy(getUsername());
        workOrderKnowledgeBase.setStatus("0");
        save(workOrderKnowledgeBase);
        if (null != workOrderKnowledgeBase.getFileList()) {
            //保存附件内容
            workOrderKnowledgeFileService.saveList(workOrderKnowledgeBase);
        }
        return AjaxResult.success();
    }

    @Override
    public List<WorkOrderKnowledgeBase> getListByKeyword(String keyword) {
        LambdaQueryWrapper<WorkOrderKnowledgeBase> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(keyword)) {
            queryWrapper.like(WorkOrderKnowledgeBase::getSubject, keyword).or().like(WorkOrderKnowledgeBase::getSolution, keyword)
            .or().like(WorkOrderKnowledgeBase::getClassification,keyword).or().like(WorkOrderKnowledgeBase::getCreateBy,keyword);
        }
        queryWrapper.eq(WorkOrderKnowledgeBase::getStatus, Constants.STATUS_VALID);
        queryWrapper.orderByDesc(WorkOrderKnowledgeBase::getCreateTime);
        List<WorkOrderKnowledgeBase> list = list(queryWrapper);
        for (int i =0;i<list.size();i++){
            SysUser user = new SysUser();
            user.setUserName(list.get(i).getCreateBy());
            List<SysUser> sysUser = userService.selectUserList(user);
            StringBuilder nickName = new StringBuilder();
            sysUser.forEach( users ->{
                nickName.append(users.getNickName());
            });
            list.get(i).setCreateBy(nickName.toString());
        }
        return list;
    }

    @Override
    public Boolean getBaseByid(String id) {
        LambdaQueryWrapper<WorkOrderKnowledgeBase> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.like(WorkOrderKnowledgeBase::getClassification,id);
        List<WorkOrderKnowledgeBase> getType =list(queryWrapper);
        if (getType!=null&&getType.size()>0){
            return false;
        }
        return true;
    }


    @Override
    public List<WorkOrderKnowledgeBase> getKlBaseList(WorkOrderKnowledgeBase workOrderKnowledgeBase) {
        LambdaQueryWrapper<WorkOrderKnowledgeBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(workOrderKnowledgeBase.getSolution()),WorkOrderKnowledgeBase::getSolution, workOrderKnowledgeBase.getSolution());
        queryWrapper.like(StringUtils.isNotEmpty(workOrderKnowledgeBase.getSubject()),WorkOrderKnowledgeBase::getSubject, workOrderKnowledgeBase.getSubject());
        String classification = workOrderKnowledgeBase.getClassification();
        if (StringUtils.isNotEmpty(workOrderKnowledgeBase.getClassification()) &&!classification.equals("0")){
            queryWrapper.like(WorkOrderKnowledgeBase::getClassification, workOrderKnowledgeBase.getClassification());
        }
        queryWrapper.like(StringUtils.isNotEmpty(workOrderKnowledgeBase.getCreateBy()),WorkOrderKnowledgeBase::getCreateBy,workOrderKnowledgeBase.getCreateBy());
        queryWrapper.eq(StringUtils.isNotEmpty(workOrderKnowledgeBase.getSystem()),WorkOrderKnowledgeBase::getSystem,workOrderKnowledgeBase.getSystem());
        Map<String, Object> paramsMap = workOrderKnowledgeBase.getParams();
        String beginTime = null != paramsMap.get("beginTime") ? paramsMap.get("beginTime").toString() : null;
        String endTime = null != paramsMap.get("endTime") ? paramsMap.get("endTime").toString() : null;
        queryWrapper.ge(StringUtils.isNotEmpty(beginTime), WorkOrderKnowledgeBase::getCreateTime, beginTime);
        queryWrapper.le(StringUtils.isNotEmpty(endTime), WorkOrderKnowledgeBase::getCreateTime, endTime);
        queryWrapper.eq(WorkOrderKnowledgeBase::getStatus, Constants.STATUS_VALID);
        queryWrapper.orderByDesc(WorkOrderKnowledgeBase::getCreateTime);
        List<WorkOrderKnowledgeBase> list = list(queryWrapper);
        for (int i =0;i<list.size();i++){
            SysUser user = new SysUser();
            user.setUserName(list.get(i).getCreateBy());
            List<SysUser> sysUser = userService.selectUserList(user);
            StringBuilder nickName = new StringBuilder();
            sysUser.forEach( users ->{
                nickName.append(users.getNickName());
            });
            list.get(i).setCreateBy(nickName.toString());
        }
        return list;
    }
    @Override
    public AjaxResult deleteKnowledge(String[] id) {
        for(String item:id){
            removeById(item);
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult updateKnowledge(WorkOrderKnowledgeBase workOrderKnowledgeBase) {
        workOrderKnowledgeBase.setUpdateBy(getUsername());
        workOrderKnowledgeBase.setUpdateTime(LocalDateTime.now());
        if (null != workOrderKnowledgeBase.getFileList()) {
            //保存附件内容
            workOrderKnowledgeFileService.saveList(workOrderKnowledgeBase);
        }
        return AjaxResult.success(updateById(workOrderKnowledgeBase));
    }

    /**
     * 导入知识库数据
     *
     * @param userList        数据列表
     * @param isUpdateSupport
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<WorkOrderKnowledgeBase> userList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(userList) || userList.isEmpty()) {
            throw new ServiceException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (WorkOrderKnowledgeBase workOrderKnowledgeBase : userList) {
            try {
                workOrderKnowledgeBase.setCreateBy(operName);
                workOrderKnowledgeBase.setCreateTime(LocalDateTime.now());
                workOrderKnowledgeBase.setStatus("0");
                save(workOrderKnowledgeBase);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、知识库 ").append(workOrderKnowledgeBase.getSubject()).append(" 导入成功");
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "知识库 " + workOrderKnowledgeBase.getSubject() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }


}
