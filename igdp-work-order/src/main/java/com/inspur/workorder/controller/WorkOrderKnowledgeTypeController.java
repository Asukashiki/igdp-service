package com.inspur.workorder.controller;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.workorder.domain.WorkOrderKnowledgeType;
import com.inspur.workorder.service.IWorkOrderKnowledgeTypeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 王海龙
 */
@RestController
@RequestMapping("/work-order/knowledge-type")
public class WorkOrderKnowledgeTypeController extends BaseController {
    @Resource
    private IWorkOrderKnowledgeTypeService workOrderKnowledgeTypeService;
    /**
     * 新增知识库菜单
     */
    @PostMapping("/addtype")
    public AjaxResult addKnowledgeType(WorkOrderKnowledgeType workOrderKnowledgeType) {

        return workOrderKnowledgeTypeService.addType(workOrderKnowledgeType);
    }

    /**
     * 修改知识库菜单
     */
    @PostMapping("/editKl")
    public AjaxResult editKnowledgeType(@RequestBody WorkOrderKnowledgeType workOrderKnowledgeType) {

        return workOrderKnowledgeTypeService.updateKnowledgeType(workOrderKnowledgeType);
    }

    /**
     * 删除知识库菜单
     */
    @PostMapping("/deletekl")
    public AjaxResult deleteKnowledgeType(String id) {
        return workOrderKnowledgeTypeService.deleteKnowledgeType(id);
    }

    /**
     * 查询知识库目录
     * @return
     */
    @GetMapping("/query")
    public TableDataInfo<?> query(){
        List<WorkOrderKnowledgeType> list = workOrderKnowledgeTypeService.getTypeList();
        return getDataTable(list);
    }


}
