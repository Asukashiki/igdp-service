package com.inspur.agriculture.input.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inspur.agriculture.input.domain.AgriDemandConfirmation;
import com.inspur.agriculture.input.service.IAgriDemandConfirmationService;
import com.inspur.agriculture.input.vo.demand_confirmation.AgriDemandConfirmationVO;
import com.inspur.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 需求确认日志控制器
 *
 * @author igdp
 */
@RestController
@RequestMapping("/agriculture/demand/confirmation")
public class AgriDemandConfirmationController {

    @Autowired
    private IAgriDemandConfirmationService agriDemandConfirmationService;

    /**
     * 查询需求确认日志列表（分页）
     */
    @GetMapping("/list")
    public AjaxResult list(
            @RequestParam(required = false) String fromActor,
            @RequestParam(required = false) String toActor,
            @RequestParam(required = false) String referenceId,
            @RequestParam(required = false) String confirmationType,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        AgriDemandConfirmation confirmation = new AgriDemandConfirmation();
        confirmation.setFromActor(fromActor);
        confirmation.setToActor(toActor);
        confirmation.setReferenceId(referenceId);
        confirmation.setConfirmationType(confirmationType);

        PageHelper.startPage(page, pageSize);
        List<AgriDemandConfirmationVO> list = agriDemandConfirmationService.selectConfirmationVOList(confirmation);
        PageInfo<AgriDemandConfirmationVO> pageInfo = new PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("list", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("page", pageInfo.getPageNum());
        result.put("pageSize", pageInfo.getPageSize());

        return AjaxResult.success(result);
    }

    /**
     * 根据ID获取需求确认日志详情
     */
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        AgriDemandConfirmationVO confirmation = agriDemandConfirmationService.selectConfirmationVOById(id);
        if (confirmation == null) {
            return AjaxResult.error("Demand confirmation does not exist");
        }
        return AjaxResult.success(confirmation);
    }

    /**
     * 根据发送方和接收方查询需求确认日志
     */
    @GetMapping("/byActors")
    public AjaxResult getConfirmationsByActors(
            @RequestParam String fromActor,
            @RequestParam(required = false) String toActor
    ) {
        List<AgriDemandConfirmationVO> list = agriDemandConfirmationService.selectConfirmationVOsByActors(fromActor, toActor);
        return AjaxResult.success(list);
    }

    /**
     * 新增需求确认日志
     */
    @PostMapping
    public AjaxResult add(@RequestBody AgriDemandConfirmation confirmation) {
        if (confirmation.getFromActor() == null || confirmation.getFromActor().trim().isEmpty()) {
            return AjaxResult.error("From actor cannot be empty");
        }
        if (confirmation.getToActor() == null || confirmation.getToActor().trim().isEmpty()) {
            return AjaxResult.error("To actor cannot be empty");
        }
        if (confirmation.getReferenceId() == null || confirmation.getReferenceId().trim().isEmpty()) {
            return AjaxResult.error("Reference ID cannot be empty");
        }
        if (confirmation.getConfirmationType() == null || confirmation.getConfirmationType().trim().isEmpty()) {
            return AjaxResult.error("Confirmation type cannot be empty");
        }

        int rows = agriDemandConfirmationService.insertConfirmation(confirmation);
        if (rows > 0) {
            return AjaxResult.success("Add successfully", confirmation);
        }
        return AjaxResult.error("Add failed");
    }

    /**
     * 删除需求确认日志（单条）
     */
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable("id") String id) {
        int rows = agriDemandConfirmationService.deleteConfirmationById(id);
        if (rows > 0) {
            return AjaxResult.success("Delete successfully");
        }
        return AjaxResult.error("Delete failed");
    }

    /**
     * 批量删除需求确认日志
     */
    @DeleteMapping("/batch")
    public AjaxResult removeBatch(@RequestBody String[] ids) {
        if (ids == null || ids.length == 0) {
            return AjaxResult.error("Please select demand confirmations to delete");
        }

        int rows = agriDemandConfirmationService.deleteConfirmationByIds(ids);
        if (rows > 0) {
            return AjaxResult.success("Batch delete successfully");
        }
        return AjaxResult.error("Batch delete failed");
    }

}