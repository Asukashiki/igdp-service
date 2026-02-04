package com.inspur.seed.controller.invested;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.domain.invested.InputReceiveWoreda;
import com.inspur.seed.service.invested.IInputReceiveWoredaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Woreda接收确认Controller
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@RestController
@RequestMapping("/invested/receive/woreda")
public class InputReceiveWoredaController extends BaseController {

    @Resource
    private IInputReceiveWoredaService receiveService;

    /**
     * 查询Woreda接收确认列表
     */
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(required = false) String woredaName,
                               @RequestParam(required = false) String receiveStatus,
                               @RequestParam(required = false) String releaseBy,
                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startTime,
                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endTime) {
        startPage();
        List<InputReceiveWoreda> list = receiveService.queryReceiveList(woredaName, receiveStatus, startTime, endTime, releaseBy);
        return getDataTable(list);
    }

    /**
     * 查询Woreda接收确认详情
     */
    @GetMapping("/detail/{id}")
    public AjaxResult detail(@PathVariable String id) {
        Map<String, Object> result = receiveService.queryReceiveDetail(id);
        return AjaxResult.success(result);
    }

    /**
     * 确认接收
     */
    @PostMapping("/confirm/{id}")
    public AjaxResult confirm(@PathVariable String id,
                              @RequestParam String confirmBy,
                              @RequestParam String confirmOrg) {
        boolean success = receiveService.confirmReceive(id, confirmBy, confirmOrg);
        return success ? AjaxResult.success("接收确认成功") : AjaxResult.error("接收确认失败");
    }
}
