package com.inspur.seed.controller.invested;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.domain.invested.InputReleaseMain;
import com.inspur.seed.dto.invested.InputReleaseDTO;
import com.inspur.seed.service.invested.IInputReleaseService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Union分发投入品到Woreda Controller
 * 复用OSE分发表，通过release_org字段区分
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@RestController
@RequestMapping("/invested/release/union")
public class InputReleaseUnionController extends BaseController {

    @Resource
    private IInputReleaseService releaseService;

    /**
     * 查询Union分发到Woreda列表
     */
    @GetMapping("/list")
    public TableDataInfo list(
            @RequestParam(required = true) String releaseType,
            @RequestParam(required = false) String woredaName,
                               @RequestParam(required = false) String inputType,
                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startTime,
                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endTime) {
        startPage();
        List<InputReleaseMain> list = releaseService.queryReleaseList(releaseType, woredaName, inputType, startTime, endTime);
        return getDataTable(list);
    }

    /**
     * 新增Union分发单
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody InputReleaseDTO dto) {
        Map<String, String> result = releaseService.addRelease(dto);
        return AjaxResult.success("Union分发单新增成功", result);
    }

    /**
     * 编辑Union分发单
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody InputReleaseDTO dto) {
        boolean success = releaseService.editRelease(dto);
        return success ? AjaxResult.success("Union分发单编辑成功") : AjaxResult.error("Union分发单编辑失败");
    }

    /**
     * 查询Union分发单详情
     */
    @GetMapping("/detail/{id}")
    public AjaxResult detail(@PathVariable String id) {
        Map<String, Object> result = releaseService.queryReleaseDetail(id);
        return AjaxResult.success(result);
    }

    /**
     * 删除Union分发单
     */
    @GetMapping("/delete/{ids}")
    public AjaxResult delete(@PathVariable String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        boolean success = releaseService.removeRelease(idList);
        return success ? AjaxResult.success("Union分发单删除成功") : AjaxResult.error("Union分发单删除失败");
    }
}
