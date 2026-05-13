package com.inspur.seed.controller.invested;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.domain.invested.InputReceiveUnion;
import com.inspur.seed.service.invested.IInputReceiveUnionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Union确认接收OSE分发Controller
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@RestController
@RequestMapping("/invested/receive/union")
public class InputReceiveUnionController extends BaseController {

    @Resource
    private IInputReceiveUnionService receiveService;

    /**
     * 查询接收确认列表
     */
    @GetMapping("/list")
    public TableDataInfo list(
            @RequestParam(required = false) String releaseBy,
            @RequestParam(required = false) String batchId,
            @RequestParam(required = false) String cropType,
            @RequestParam(required = false) String varietyName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endTime,
            @RequestParam(required = false) String receiveStatus,
            @RequestParam(required = false, defaultValue = "0") String flag) {
        validateFlag(flag);
        startPage();
        List<InputReceiveUnion> list = receiveService.queryReceiveList(releaseBy, batchId, cropType,
                varietyName, startTime, endTime, receiveStatus, flag);
        return getDataTable(list);
    }

    /**
     * 确认接收操作
     */
    @PutMapping("/confirm/{id}")
    public AjaxResult confirm(@PathVariable String id,
                              @RequestBody Map<String, String> params,
                              @RequestParam(required = false, defaultValue = "0") String flag) {
        try {
            validateFlag(flag);
            String confirmBy = params.get("confirmBy");
            String confirmOrg = params.get("confirmOrg");
            receiveService.confirmReceive(id, confirmBy, confirmOrg, flag);
            return AjaxResult.success("接收确认成功");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询接收确认详情
     */
    @GetMapping("/detail/{id}")
    public AjaxResult getDetail(@PathVariable String id) {
        Map<String, Object> receive = receiveService.queryById(id);
        if (receive == null) {
            return AjaxResult.error("接收记录不存在");
        }
        return AjaxResult.success("查询成功", receive);
    }

    private void validateFlag(String flag) {
        if (!"0".equals(flag) && !"1".equals(flag)) {
            throw new IllegalArgumentException("flag参数只能为0或1");
        }
    }
}
