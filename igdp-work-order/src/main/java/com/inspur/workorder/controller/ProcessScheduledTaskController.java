package com.inspur.workorder.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.enums.BusinessType;
import com.inspur.common.utils.poi.ExcelUtil;
import com.inspur.workorder.domain.ProcessScheduledTask;
import com.inspur.workorder.service.IProcessScheduledTaskService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName ProcessScheduledTaskController
 * @date 2024/6/4 16:45
 */
@RestController
@RequestMapping("/work-order/process-scheduled-task")
public class ProcessScheduledTaskController extends BaseController {
    @Resource
    private IProcessScheduledTaskService processScheduledTaskService;

    /**
     * 查询待办事项列表
     */
    @SaCheckPermission("work-order:process-scheduled-task:list")
    @GetMapping("/list")
    public TableDataInfo<?> list(ProcessScheduledTask queryParam) {
        startPage();
        List<ProcessScheduledTask> list = processScheduledTaskService.selectList(queryParam);
        return getDataTable(list);
    }

    /**
     * 导出待办事项列表
     */
    @SaCheckPermission("work-order:process-scheduled-task:export")
    @Log(title = "流程定时任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ProcessScheduledTask queryParam) {
        List<ProcessScheduledTask> list = processScheduledTaskService.selectList(queryParam);
        ExcelUtil<ProcessScheduledTask> util = new ExcelUtil<>(ProcessScheduledTask.class);
        util.exportExcel(response, list, "流程定时任务数据");
    }

    /**
     * 获取待办事项详细信息
     */
    @SaCheckPermission("work-order:process-scheduled-task:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        ProcessScheduledTask task = processScheduledTaskService.getTaskById(id);
        return success(task);
    }

    /**
     * 新增待办事项
     */
    @SaCheckPermission("work-order:process-scheduled-task:add")
    @Log(title = "流程定时任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProcessScheduledTask task) {
        return toAjax(processScheduledTaskService.addTask(task));
    }

    /**
     * 修改待办事项
     */
    @SaCheckPermission("work-order:process-scheduled-task:edit")
    @Log(title = "流程定时任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProcessScheduledTask task) {
        return toAjax(processScheduledTaskService.updateTask(task));
    }

    /**
     * 删除待办事项
     */
    @SaCheckPermission("work-order:process-scheduled-task:remove")
    @Log(title = "流程定时任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(processScheduledTaskService.removeBatchByIds(Arrays.asList(ids)));
    }
}
