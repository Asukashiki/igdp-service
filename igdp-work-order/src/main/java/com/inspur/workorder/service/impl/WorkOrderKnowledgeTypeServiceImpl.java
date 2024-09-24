package com.inspur.workorder.service.impl;

import cn.dev33.satoken.util.SaFoxUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.annotation.Excel;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.utils.StringUtils;
import com.inspur.workorder.domain.WorkOrderKnowledgeBase;
import com.inspur.workorder.domain.WorkOrderKnowledgeType;
import com.inspur.workorder.mapper.WorkOrderKnowledgeTypeMapper;
import com.inspur.workorder.service.IWorkOrderKnowledgeBaseService;
import com.inspur.workorder.service.IWorkOrderKnowledgeFileService;
import com.inspur.workorder.service.IWorkOrderKnowledgeTypeService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.inspur.common.utils.LoginHelper.getUsername;

@Service
public class WorkOrderKnowledgeTypeServiceImpl extends ServiceImpl<WorkOrderKnowledgeTypeMapper, WorkOrderKnowledgeType> implements IWorkOrderKnowledgeTypeService {
    @Resource
    private IWorkOrderKnowledgeBaseService workOrderKnowledgeBaseService;
    @Override
    public AjaxResult addType(WorkOrderKnowledgeType workOrderKnowledgeType) {
        LambdaQueryWrapper<WorkOrderKnowledgeType> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(WorkOrderKnowledgeType::getName,workOrderKnowledgeType.getName());
        List<WorkOrderKnowledgeType> checkNameType = list(queryWrapper);
        if (checkNameType !=null&&checkNameType.size()>0){
            return AjaxResult.success("新增菜单'" + workOrderKnowledgeType.getName() + "'失败，菜单名称已存在");
        }
        Random random = new Random();
        int randomNumber = random.nextInt(900)+100;

        if("0".equals(workOrderKnowledgeType.getParentId())){
            workOrderKnowledgeType.setId(String.valueOf(randomNumber));
        }else {
            workOrderKnowledgeType.setId(workOrderKnowledgeType.getParentId() + String.valueOf(randomNumber));
        }

        workOrderKnowledgeType.setCreateTime(LocalDateTime.now());
        workOrderKnowledgeType.setCreateBy(getUsername());
        workOrderKnowledgeType.setParentId(workOrderKnowledgeType.getParentId());
        save(workOrderKnowledgeType);
        return AjaxResult.success();
    }
    @Override
    public AjaxResult deleteKnowledgeType(String id) {
        Boolean aBoolean = workOrderKnowledgeBaseService.getBaseByid(id);
        if (aBoolean == false){
            return AjaxResult.success("删除菜单失败，已存在该菜单的知识库数据！");
        }
        return AjaxResult.success(removeById(id));
    }

    @Override
    public AjaxResult updateKnowledgeType(WorkOrderKnowledgeType workOrderKnowledgeType) {
        workOrderKnowledgeType.setUpdateBy(getUsername());
        workOrderKnowledgeType.setUpdateTime(LocalDateTime.now());
        return AjaxResult.success(updateById(workOrderKnowledgeType));
    }

    @Override
    public List<WorkOrderKnowledgeType> getTypeList() {
        LambdaQueryWrapper<WorkOrderKnowledgeType> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(WorkOrderKnowledgeType::getOrderNum);
        List<WorkOrderKnowledgeType> workOrderKnowledgeTypeList = list(queryWrapper);
        List<WorkOrderKnowledgeType> returnList = new ArrayList<>();
        List<String> tempList = workOrderKnowledgeTypeList.stream().map(WorkOrderKnowledgeType::getId).collect(Collectors.toList());
        for (WorkOrderKnowledgeType TypeList : workOrderKnowledgeTypeList) {
            Map<String,Object> map = new HashMap<>();
            WorkOrderKnowledgeBase workOrderKnowledgeBase = new WorkOrderKnowledgeBase();
            workOrderKnowledgeBase.setClassification(TypeList.getId());
            List<WorkOrderKnowledgeBase> workOrderKnowledgeBaseList = workOrderKnowledgeBaseService.getKlBaseList(workOrderKnowledgeBase);
            TypeList.setType(String.valueOf(workOrderKnowledgeBaseList.size()));
            if (!tempList.contains(TypeList.getParentId())) {
                recursionFn(workOrderKnowledgeTypeList, TypeList);
                returnList.add(TypeList);
            }

        }
        if (returnList.isEmpty()) {
            returnList = workOrderKnowledgeTypeList;
        }
        return returnList;
    }

    @Override
    public List<WorkOrderKnowledgeType> getKnowledgeType(List<WorkOrderKnowledgeBase> userList) {
        LambdaQueryWrapper<WorkOrderKnowledgeType> queryWrapper =new LambdaQueryWrapper<>();
        List<WorkOrderKnowledgeType> workOrderKnowledgeBaseList = new ArrayList<>();
        int num = 0;
        userList.forEach( knowledgeType->{
            queryWrapper.clear();
            queryWrapper.eq(WorkOrderKnowledgeType::getName,knowledgeType.getClassificationName());
            WorkOrderKnowledgeType workOrderKnowledgeType = getOne(queryWrapper);
            workOrderKnowledgeBaseList.add(workOrderKnowledgeType);
        });
        return workOrderKnowledgeBaseList;
    }


    /**
     * 递归列表
     */
    private void recursionFn(List<WorkOrderKnowledgeType> workOrderKnowledgeTypeList, WorkOrderKnowledgeType t) {
        // 得到子节点列表
        List<WorkOrderKnowledgeType> childList = getChildList(workOrderKnowledgeTypeList, t);
        t.setChildren(childList);
        for (WorkOrderKnowledgeType tChild : childList) {
            if (hasChild(workOrderKnowledgeTypeList, tChild)) {
                recursionFn(workOrderKnowledgeTypeList, tChild);
            }
        }
    }
    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<WorkOrderKnowledgeType> workOrderKnowledgeTypeList, WorkOrderKnowledgeType t) {
        return !getChildList(workOrderKnowledgeTypeList, t).isEmpty();
    }
    /**
     * 得到子节点列表
     */
    private List<WorkOrderKnowledgeType> getChildList(List<WorkOrderKnowledgeType> list, WorkOrderKnowledgeType t) {
        List<WorkOrderKnowledgeType> tlist = new ArrayList<>();
        for (WorkOrderKnowledgeType n : list) {
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().equals(t.getId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }
}
