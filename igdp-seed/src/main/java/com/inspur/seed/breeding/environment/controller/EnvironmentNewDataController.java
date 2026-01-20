package com.inspur.seed.breeding.environment.controller;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.breeding.environment.domain.entity.EnvironmentNewData;
import com.inspur.seed.breeding.environment.service.IEnvironmentNewDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 环境监测新数据Controller
 * Environment New Data Controller
 *
 * @author inspur
 */
@RestController
@RequestMapping("/breeding/environmentNew")
public class EnvironmentNewDataController extends BaseController {

    @Autowired
    private IEnvironmentNewDataService environmentNewDataService;

    /**
     * 分页查询环境监测数据列表
     * Query environment new data list with pagination
     */
    @GetMapping("/list")
    public TableDataInfo list(EnvironmentNewData environmentNewData) {
        startPage();
        List<EnvironmentNewData> list = environmentNewDataService.selectEnvironmentNewDataList(environmentNewData);
        return getDataTable(list);
    }

    /**
     * 获取环境监测数据详情
     * Get environment new data detail
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo(@RequestParam("envRecordId") String envRecordId) {
        return AjaxResult.success(environmentNewDataService.selectEnvironmentNewDataById(envRecordId));
    }

    /**
     * 新增环境监测数据
     * Add new environment data
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody EnvironmentNewData environmentNewData) {
        String envRecordId = environmentNewDataService.insertEnvironmentNewData(environmentNewData);
        return AjaxResult.success("新增成功 / Added successfully", envRecordId);
    }

    /**
     * 修改环境监测数据
     * Update environment data
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody EnvironmentNewData environmentNewData) {
        return toAjax(environmentNewDataService.updateEnvironmentNewData(environmentNewData));
    }

    /**
     * 删除环境监测数据
     * Delete environment data
     */
    @GetMapping("/remove")
    public AjaxResult remove(@RequestParam("envRecordIds") String envRecordIds) {
        String[] ids = envRecordIds.split(",");
        return toAjax(environmentNewDataService.deleteEnvironmentNewDataByIds(ids));
    }

    /**
     * 提交环境监测数据审核
     * Submit environment new data for audit
     */
    @PostMapping("/submitForAudit")
    public AjaxResult submitForAudit(@RequestBody Map<String, String> params) {
        String envRecordId = params.get("envRecordId");
        return AjaxResult.success(environmentNewDataService.submitForAudit(envRecordId));
    }

    /**
     * 审核通过环境监测数据
     * Approve environment new data
     */
    @PostMapping("/approve")
    public AjaxResult approve(@RequestBody Map<String, String> params) {
        String envRecordId = params.get("envRecordId");
        String auditComment = params.get("auditComment");
        return AjaxResult.success(environmentNewDataService.approve(envRecordId, auditComment));
    }

    /**
     * 驳回环境监测数据
     * Reject environment new data
     */
    @PostMapping("/reject")
    public AjaxResult reject(@RequestBody Map<String, String> params) {
        String envRecordId = params.get("envRecordId");
        String auditComment = params.get("auditComment");
        return AjaxResult.success(environmentNewDataService.reject(envRecordId, auditComment));
    }

    /**
     * 批量提交环境监测数据审核
     * Batch submit environment new data for audit
     */
    @PostMapping("/batchSubmitForAudit")
    public AjaxResult batchSubmitForAudit(@RequestBody Map<String, Object> params) {
        try {
            List<String> envRecordIds = (List<String>) params.get("envRecordIds");
            int count = 0;
            for (String envRecordId : envRecordIds) {
                int result = environmentNewDataService.submitForAudit(envRecordId);
                if (result > 0) {
                    count++;
                }
            }
            return AjaxResult.success("成功提交 " + count + " 条记录审核");
        } catch (Exception e) {
            return AjaxResult.error("批量提交审核失败: " + e.getMessage());
        }
    }

    /**
     * 批量审核通过环境监测数据
     * Batch approve environment new data
     */
    @PostMapping("/batchApprove")
    public AjaxResult batchApprove(@RequestBody Map<String, Object> params) {
        try {
            List<String> envRecordIds = (List<String>) params.get("envRecordIds");
            String auditComment = (String) params.get("auditComment");
            int count = 0;
            for (String envRecordId : envRecordIds) {
                int result = environmentNewDataService.approve(envRecordId, auditComment);
                if (result > 0) {
                    count++;
                }
            }
            return AjaxResult.success("成功审核通过 " + count + " 条记录");
        } catch (Exception e) {
            return AjaxResult.error("批量审核通过失败: " + e.getMessage());
        }
    }

    /**
     * 批量驳回环境监测数据
     * Batch reject environment new data
     */
    @PostMapping("/batchReject")
    public AjaxResult batchReject(@RequestBody Map<String, Object> params) {
        try {
            List<String> envRecordIds = (List<String>) params.get("envRecordIds");
            String auditComment = (String) params.get("auditComment");
            int count = 0;
            for (String envRecordId : envRecordIds) {
                int result = environmentNewDataService.reject(envRecordId, auditComment);
                if (result > 0) {
                    count++;
                }
            }
            return AjaxResult.success("成功驳回 " + count + " 条记录");
        } catch (Exception e) {
            return AjaxResult.error("批量驳回失败: " + e.getMessage());
        }
    }
}
