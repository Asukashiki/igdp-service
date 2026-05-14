package com.inspur.seed.controller.invested;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.domain.invested.InputReleaseFarmerMain;
import com.inspur.seed.dto.invested.InputReleaseFarmerDTO;
import com.inspur.seed.service.invested.IInputReleaseFarmerService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 农民分发Controller
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@RestController
@RequestMapping("/invested/release/farmer")
public class InputReleaseFarmerController extends BaseController {

    @Resource
    private IInputReleaseFarmerService releaseService;

    /**
     * 查询农民分发列表
     */
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(required = false) String woredaName,
                               @RequestParam(required = false) String farmerName,
                               @RequestParam(required = false) String farmerId,
                               @RequestParam(required = false) Integer year,
                               @RequestParam(required = false) String receiveStatus,
                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startTime,
                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endTime,
                               @RequestParam(required = false, defaultValue = "0") String flag) {
        validateFlag(flag);
        startPage();
        List<InputReleaseFarmerMain> list = releaseService.queryReleaseList(woredaName, farmerName, farmerId,
                year, receiveStatus, startTime, endTime, flag);
        return getDataTable(list);
    }

    /**
     * 新增农民分发单
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody InputReleaseFarmerDTO dto) {
        Map<String, String> result = releaseService.addRelease(dto);
        return AjaxResult.success("农民分发单新增成功", result);
    }

    /**
     * 编辑农民分发单
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody InputReleaseFarmerDTO dto) {
        boolean success = releaseService.editRelease(dto);
        return success ? AjaxResult.success("农民分发单编辑成功") : AjaxResult.error("农民分发单编辑失败");
    }

    /**
     * 查询农民分发单详情
     */
    @GetMapping("/detail/{id}")
    public AjaxResult detail(@PathVariable String id) {
        Map<String, Object> result = releaseService.queryReleaseDetail(id);
        return AjaxResult.success(result);
    }

    /**
     * 删除农民分发单
     */
    @GetMapping("/delete/{ids}")
    public AjaxResult delete(@PathVariable String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        boolean success = releaseService.removeRelease(idList);
        return success ? AjaxResult.success("农民分发单删除成功") : AjaxResult.error("农民分发单删除失败");
    }

    /**
     * 查询农民领用列表（从农民角度）
     */
    @GetMapping("/receive/list")
    public TableDataInfo receiveList(@RequestParam(required = false) String farmerId,
                                      @RequestParam(required = false) String farmerName,
                                      @RequestParam(required = false) Integer year,
                                      @RequestParam(required = false) String receiveStatus,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startTime,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endTime,
                                      @RequestParam(required = false, defaultValue = "0") String flag) {
        validateFlag(flag);
        startPage();
        List<InputReleaseFarmerMain> list = releaseService.queryReleaseList(null, farmerName, farmerId,
                year, receiveStatus, startTime, endTime, flag);
        return getDataTable(list);
    }

    /**
     * 查询农民领用详情
     */
    @GetMapping("/receive/detail/{id}")
    public AjaxResult receiveDetail(@PathVariable String id) {
        Map<String, Object> result = releaseService.queryReleaseDetail(id);
        return AjaxResult.success(result);
    }

    /**
     * 确认领用
     */
    @PostMapping("/receive/confirm/{id}")
    public AjaxResult confirmReceive(@PathVariable String id) {
        boolean success = releaseService.confirmReceive(id);
        return success ? AjaxResult.success("领用确认成功") : AjaxResult.error("领用确认失败");
    }

    private void validateFlag(String flag) {
        if (!"0".equals(flag) && !"1".equals(flag) && !"2".equals(flag)) {
            throw new IllegalArgumentException("flag参数只能为0、1或2");
        }
    }
}
