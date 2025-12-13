package com.inspur.seed.controller.invested;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.domain.invested.InputReleaseMain;
import com.inspur.seed.dto.invested.InputReleaseDTO;
import com.inspur.seed.service.invested.IInputReleaseService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * OSE分发种子到Union管理Controller
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@RestController
@RequestMapping("/invested/release/ose")
public class InputReleaseController extends BaseController {

    @Resource
    private IInputReleaseService releaseService;

    /**
     * 查询分发单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(
            @RequestParam(required = true) String releaseType,
            @RequestParam(required = false) String releaseName,
            @RequestParam(required = false) String inputType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endTime) {
        startPage();
        List<InputReleaseMain> list = releaseService.queryReleaseList(releaseType, releaseName, inputType, startTime, endTime);
        return getDataTable(list);
    }

    /**
     * 新增分发单
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody InputReleaseDTO dto) {
        try {
            Map<String, String> result = releaseService.addRelease(dto);
            return AjaxResult.success("分发单新增成功", result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 编辑分发单
     */
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody InputReleaseDTO dto) {
        try {
            releaseService.editRelease(dto);
            return AjaxResult.success("分发单编辑成功");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询分发单详情
     */
    @GetMapping("/detail/{id}")
    public AjaxResult getDetail(@PathVariable String id) {
        try {
            Map<String, Object> detail = releaseService.queryReleaseDetail(id);
            return AjaxResult.success("查询成功", detail);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 根据releaseId查询分发单详情
     */
    @GetMapping("/detailByReleaseId/{releaseId}")
    public AjaxResult getDetailByReleaseId(@PathVariable String releaseId) {
        try {
            Map<String, Object> detail = releaseService.queryReleaseDetailByReleaseId(releaseId);
            return AjaxResult.success("查询成功", detail);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 删除分发单
     */
    @DeleteMapping("/delete/{ids}")
    public AjaxResult delete(@PathVariable String ids) {
        try {
            List<String> idList = Arrays.asList(ids.split(","));
            releaseService.removeRelease(idList);
            return AjaxResult.success("分发单删除成功");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
