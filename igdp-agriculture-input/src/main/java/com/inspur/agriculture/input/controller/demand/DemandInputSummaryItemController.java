package com.inspur.agriculture.input.controller.demand;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inspur.agriculture.input.dto.demand.DemandInputSummaryItemDTO;
import com.inspur.agriculture.input.dto.demand.DemandInputSummaryItemQueryDTO;
import com.inspur.agriculture.input.dto.demand.DemandOrganDTO;
import com.inspur.agriculture.input.service.demand.IDemandInputSummaryItemService;
import com.inspur.agriculture.input.vo.demand.DemandInputSummaryItemVO;
import com.inspur.agriculture.input.vo.demand.InputAggregationSummaryVO;
import com.inspur.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 农资汇聚统计管理 Controller
 *
 * @author inspur
 * @date 2025-12-09
 */
@RestController
@RequestMapping("/demand/input/summary")
public class DemandInputSummaryItemController {

    @Autowired
    private IDemandInputSummaryItemService demandInputSummaryItemService;

    /**
     * 查询农资汇聚统计列表
     */
    @GetMapping("/list")
    public AjaxResult list(
            DemandInputSummaryItemQueryDTO queryDTO,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        try {
            PageHelper.startPage(page, pageSize);
            List<DemandInputSummaryItemVO> list = demandInputSummaryItemService.getDemandInputSummaryItemList(queryDTO);
            PageInfo<DemandInputSummaryItemVO> pageInfo = new PageInfo<>(list);

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
     * 查询农资汇聚统计详情
     */
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable String id) {
        try {
            DemandInputSummaryItemVO item = demandInputSummaryItemService.getDemandInputSummaryItemById(id);
            if (item == null) {
                return AjaxResult.error("农资汇聚统计记录不存在");
            }
            return AjaxResult.success(item);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @PostMapping("/detail")
    public AjaxResult detail(@Validated @RequestBody DemandInputSummaryItemQueryDTO dto) {
        try {
            List<DemandInputSummaryItemVO> result = demandInputSummaryItemService.getDemandInputSummaryItemList(dto);
            return AjaxResult.error("fail");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 添加农资汇聚统计(镇、市)
     */
    @PostMapping()
    public AjaxResult add(@Validated @RequestBody DemandInputSummaryItemDTO dto) {
        try {
            int rows = demandInputSummaryItemService.addDemandInputSummaryItem(dto);
            if (rows > 0) {
                return AjaxResult.success("添加成功");
            }
            return AjaxResult.error("添加失败");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 更新农资汇聚统计
     */
    @PostMapping("/update")
    public AjaxResult update(@Validated @RequestBody DemandInputSummaryItemDTO dto) {
        try {
            int rows = demandInputSummaryItemService.updateDemandInputSummaryItem(dto);
            if (rows > 0) {
                return AjaxResult.success("更新成功");
            }
            return AjaxResult.error("更新失败");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }


    /**
     * 二次汇聚查看
     * 从汇聚统计表中按来源编码查询并再次汇总(镇、市)
     */


    @PostMapping("/aggregate")
    public AjaxResult aggregate(@RequestBody DemandOrganDTO demandOrganDTO) {
        try {
            int count = demandInputSummaryItemService.submitInputAggregation(demandOrganDTO);
            return AjaxResult.success("汇聚成功，共汇聚" + count + "条记录", count);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @PostMapping("/getAggregate")
    public AjaxResult getAggregate(@RequestBody DemandOrganDTO demandOrganDTO) {
        try {
            List<InputAggregationSummaryVO> result = demandInputSummaryItemService.getInputAggregation(demandOrganDTO);
            return AjaxResult.success("获取成功", result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 删除农资汇聚统计
     */
    @PostMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable String id) {
        try {
            int rows = demandInputSummaryItemService.deleteDemandInputSummaryItem(id);
            if (rows > 0) {
                return AjaxResult.success("删除成功");
            }
            return AjaxResult.error("删除失败");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 批量删除农资汇聚统计
     */
    @PostMapping("/batchDelete")
    public AjaxResult batchDelete(@RequestBody List<String> ids) {
        try {
            int rows = demandInputSummaryItemService.batchDeleteDemandInputSummaryItem(ids);
            if (rows > 0) {
                return AjaxResult.success("删除成功，共删除" + rows + "条记录");
            }
            return AjaxResult.error("删除失败");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
