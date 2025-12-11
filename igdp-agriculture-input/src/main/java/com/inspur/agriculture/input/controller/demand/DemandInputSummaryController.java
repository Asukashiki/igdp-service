package com.inspur.agriculture.input.controller.demand;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inspur.agriculture.input.dto.demand.DemandInputSummaryDTO;
import com.inspur.agriculture.input.dto.demand.DemandInputSummaryQueryDTO;
import com.inspur.agriculture.input.service.demand.IDemandInputSummaryService;
import com.inspur.agriculture.input.vo.demand.DemandInputSummaryVO;
import com.inspur.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 农资需求汇总管理 Controller
 *
 * @author inspur
 * @date 2025-12-10
 */
@RestController
@RequestMapping("/demand/input/summary/main")
public class DemandInputSummaryController {

    @Autowired
    private IDemandInputSummaryService demandInputSummaryService;

    /**
     * 分页查询农资需求汇总列表
     */
    @GetMapping("/list")
    public AjaxResult list(
            DemandInputSummaryQueryDTO queryDTO,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        try {
            PageHelper.startPage(page, pageSize);
            List<DemandInputSummaryVO> list = demandInputSummaryService.getDemandInputSummaryList(queryDTO);
            PageInfo<DemandInputSummaryVO> pageInfo = new PageInfo<>(list);

            Map<String, Object> result = new HashMap<>();
            result.put("list", pageInfo.getList());
            result.put("total", pageInfo.getTotal());
            result.put("page", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());

            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询农资需求汇总详情
     */
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable String id) {
        try {
            DemandInputSummaryVO summary = demandInputSummaryService.getDemandInputSummaryById(id);
            if (summary == null) {
                return AjaxResult.error("农资需求汇总记录不存在");
            }
            return AjaxResult.success(summary);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 添加农资需求汇总
     */
    @PostMapping()
    public AjaxResult add(@Validated @RequestBody DemandInputSummaryDTO dto) {
        try {
            int rows = demandInputSummaryService.addDemandInputSummary(dto);
            if (rows > 0) {
                return AjaxResult.success("添加成功");
            }
            return AjaxResult.error("添加失败");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 更新农资需求汇总
     */
    @PostMapping("/update")
    public AjaxResult update(@Validated @RequestBody DemandInputSummaryDTO dto) {
        try {
            int rows = demandInputSummaryService.updateDemandInputSummary(dto);
            if (rows > 0) {
                return AjaxResult.success("更新成功");
            }
            return AjaxResult.error("更新失败");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

}
