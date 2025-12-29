package com.inspur.seed.multiplication.c1Seed.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.multiplication.c1Seed.domain.dto.C1BreedingBatchDTO;
import com.inspur.seed.multiplication.c1Seed.domain.dto.C1BreedingBatchQueryDTO;
import com.inspur.seed.multiplication.c1Seed.domain.vo.C1BreedingBatchVO;
import com.inspur.seed.multiplication.c1Seed.service.IC1BreedingBatchService;
import com.inspur.seed.multiplication.c1Seed.service.IC1SeedPropagationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * C1繁殖批次Controller
 * @author system
 * @since 2025-12-08
 */
@RestController
@RequestMapping("/seed/c1-breeding-batch")
public class C1BreedingBatchController {

    @Autowired
    private IC1BreedingBatchService c1BreedingBatchService;

    @Autowired
    private IC1SeedPropagationService propagationService;

    /**
     * 获取已审核通过的繁殖申请列表（供选择批次号使用）
     */
    @GetMapping("/approved-propagations")
    public AjaxResult getApprovedPropagations() {
        return propagationService.getApprovedList();
    }

    /**
     * 分页查询C1繁殖批次列表
     */
    @PostMapping("/list")
    public AjaxResult list(@RequestBody C1BreedingBatchQueryDTO queryDTO) {
        IPage<C1BreedingBatchVO> page = c1BreedingBatchService.pageList(queryDTO);
        Map<String, Object> result = new HashMap<>();
        result.put("list", page.getRecords());
        result.put("total", page.getTotal());
        result.put("pageNum", page.getCurrent());
        result.put("pageSize", page.getSize());
        return AjaxResult.success(result);
    }

    /**
     * 根据ID获取详情
     */
    @GetMapping("/getById/{id}")
    public AjaxResult getById(@PathVariable String id) {
        C1BreedingBatchVO vo = c1BreedingBatchService.getDetailById(id);
        if (vo == null) {
            return AjaxResult.error("数据不存在");
        }
        return AjaxResult.success(vo);
    }

    /**
     * 新增C1繁殖批次
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody C1BreedingBatchDTO dto) {
        boolean result = c1BreedingBatchService.add(dto);
        return result ? AjaxResult.success("新增成功") : AjaxResult.error("新增失败");
    }

    /**
     * 更新C1繁殖批次
     */
    @PostMapping("/update")
    public AjaxResult update(@RequestBody C1BreedingBatchDTO dto) {
        if (dto.getId() == null || dto.getId().isEmpty()) {
            return AjaxResult.error("ID不能为空");
        }
        boolean result = c1BreedingBatchService.update(dto);
        return result ? AjaxResult.success("更新成功") : AjaxResult.error("更新失败");
    }

    /**
     * 删除C1繁殖批次
     */
    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return AjaxResult.error("请选择要删除的数据");
        }
        boolean result = c1BreedingBatchService.deleteByIds(ids);
        return result ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
    }

    /**
     * 审核通过
     */
    @PostMapping("/approve")
    public AjaxResult approve(@RequestBody C1BreedingBatchDTO dto) {
        if (dto.getId() == null || dto.getId().isEmpty()) {
            return AjaxResult.error("ID不能为空");
        }

        boolean result = c1BreedingBatchService.approveBatch(dto.getId(), dto.getAuditComment());
        return result ? AjaxResult.success("审核通过成功") : AjaxResult.error("审核失败");
    }

    /**
     * 审核驳回
     */
    @PostMapping("/reject")
    public AjaxResult reject(@RequestBody C1BreedingBatchDTO dto) {
        if (dto.getId() == null || dto.getId().isEmpty()) {
            return AjaxResult.error("ID不能为空");
        }

        boolean result = c1BreedingBatchService.rejectBatch(dto.getId(), dto.getAuditComment());
        return result ? AjaxResult.success("审核驳回成功") : AjaxResult.error("审核失败");
    }

    /**
     * 记录打印
     */
    @PostMapping("/record-print/{id}")
    public AjaxResult recordPrint(@PathVariable String id) {
        boolean result = c1BreedingBatchService.recordPrint(id);
        return result ? AjaxResult.success("记录成功") : AjaxResult.error("记录失败");
    }

    /**
     * 获取已审核通过的批次列表（供证书颁发使用）
     */
    @PostMapping("/approved-list")
    public AjaxResult getApprovedList(@RequestBody C1BreedingBatchQueryDTO queryDTO) {
        queryDTO.setAuditStatus("approved");
        IPage<C1BreedingBatchVO> page = c1BreedingBatchService.pageList(queryDTO);
        Map<String, Object> result = new HashMap<>();
        result.put("list", page.getRecords());
        result.put("total", page.getTotal());
        result.put("pageNum", page.getCurrent());
        result.put("pageSize", page.getSize());
        return AjaxResult.success(result);
    }
}
