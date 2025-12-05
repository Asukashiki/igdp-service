package com.inspur.seed.controller.invested;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.seed.domain.invested.InputQuota;
import com.inspur.seed.service.invested.IInputQuotaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 投入品配额管理Controller
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@RestController
@RequestMapping("/invested/quota")
public class InputQuotaController extends BaseController {

    @Resource
    private IInputQuotaService quotaService;

    /**
     * 查询投入品配额列表
     */
    @GetMapping("/list")
    public TableDataInfo list(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String adminOrg,
            @RequestParam(required = false) String zone,
            @RequestParam(required = false) String inputType,
            @RequestParam(required = false) String farmerId,
            @RequestParam(required = false) String receiveStatus) {
        startPage();
        List<InputQuota> list = quotaService.queryQuotaList(year, adminOrg, zone, inputType, farmerId, receiveStatus);
        return getDataTable(list);
    }

    /**
     * 查询配额详情
     */
    @GetMapping("/detail/{id}")
    public AjaxResult getDetail(@PathVariable String id) {
        InputQuota quota = quotaService.queryById(id);
        if (quota == null) {
            return AjaxResult.error("配额记录不存在");
        }
        return AjaxResult.success("查询成功", quota);
    }
}
