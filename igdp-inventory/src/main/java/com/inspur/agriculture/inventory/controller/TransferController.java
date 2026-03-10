package com.inspur.agriculture.inventory.controller;

import com.inspur.agriculture.inventory.dto.TransferDTO;
import com.inspur.agriculture.inventory.service.ITransferService;
import com.inspur.agriculture.inventory.vo.TransferVO;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 调拨管理Controller
 */
@RestController
@RequestMapping("/inventory/transfer")
public class TransferController extends BaseController {

    @Autowired
    private ITransferService transferService;

    @GetMapping("/list")
    public TableDataInfo list(TransferDTO dto) {
        startPage();
        List<TransferVO> list = transferService.selectTransferList(dto);
        return getDataTable(list);
    }

    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(transferService.selectTransferById(id));
    }

    @PostMapping("/create")
    public AjaxResult add(@RequestBody TransferDTO dto) {
        Long id = transferService.insertTransfer(dto);
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        return AjaxResult.success(result);
    }

    @PutMapping
    public AjaxResult edit(@RequestBody TransferDTO dto) {
        return toAjax(transferService.updateTransfer(dto));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(transferService.deleteTransferByIds(ids));
    }

    @PostMapping("/submit")
    public AjaxResult submit(@RequestBody Map<String, Long> params) {
        Long id = params.get("id");
        return toAjax(transferService.submitTransfer(id));
    }

    @PostMapping("/audit")
    public AjaxResult audit(@RequestBody Map<String, Object> params) {
        Long id = Long.parseLong(params.get("id").toString());
        Boolean approved = Boolean.parseBoolean(params.get("approved").toString());
        String auditComment = params.get("auditComment") != null ? params.get("auditComment").toString() : "";
        return toAjax(transferService.auditTransfer(id, approved, auditComment));
    }
}
