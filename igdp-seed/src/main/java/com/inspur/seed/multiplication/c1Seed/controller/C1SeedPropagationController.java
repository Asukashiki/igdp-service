package com.inspur.seed.multiplication.c1Seed.controller;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.multiplication.c1Seed.domain.dto.AvailableBasicSeedQueryDTO;
import com.inspur.seed.multiplication.c1Seed.domain.dto.C1SeedPropagationDTO;
import com.inspur.seed.multiplication.c1Seed.domain.dto.C1SeedPropagationQueryDTO;
import com.inspur.seed.multiplication.c1Seed.service.IC1SeedPropagationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * C1种子繁殖申请Controller
 * 供Union/Cooperative使用
 *
 * @author system
 * @since 2025-12-08
 */
@Slf4j
@RestController
@RequestMapping("/seed/c1-propagation")
public class C1SeedPropagationController {

    @Autowired
    private IC1SeedPropagationService propagationService;

    /**
     * 获取申请列表（分页）
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @PostMapping("/list")
    public AjaxResult getList(@RequestBody C1SeedPropagationQueryDTO queryDTO) {
        log.info("获取C1繁殖申请列表,查询条件: {}", queryDTO);
        return propagationService.getList(queryDTO);
    }

    /**
     * 根据ID获取申请详情
     *
     * @param id 申请ID
     * @return 申请详情
     */
    @GetMapping("/getById/{id}")
    public AjaxResult getById(@PathVariable("id") String id) {
        log.info("获取C1繁殖申请详情,ID: {}", id);
        return propagationService.getById(id);
    }

    /**
     * 新增申请
     *
     * @param dto 申请信息
     * @return 操作结果
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody C1SeedPropagationDTO dto) {
        log.info("新增C1繁殖申请,机构: {}", dto.getApplicantOrgName());
        return propagationService.add(dto);
    }

    /**
     * 修改申请
     *
     * @param dto 申请信息
     * @return 操作结果
     */
    @PostMapping("/update")
    public AjaxResult update(@Validated @RequestBody C1SeedPropagationDTO dto) {
        log.info("修改C1繁殖申请,ID: {}", dto.getId());
        return propagationService.update(dto);
    }

    /**
     * 删除申请
     *
     * @param ids 申请ID数组
     * @return 操作结果
     */
    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody String[] ids) {
        log.info("删除C1繁殖申请,数量: {}", ids.length);
        return propagationService.delete(ids);
    }

    /**
     * 获取可用的Basic种子列表
     * 聚合OSE接收确认和批次采集两个数据源
     *
     * @param queryDTO 查询条件
     * @return 可用种子列表
     */
    @PostMapping("/available-seeds")
    public AjaxResult getAvailableBasicSeeds(@RequestBody(required = false) AvailableBasicSeedQueryDTO queryDTO) {
        log.info("获取可用Basic种子列表,查询条件: {}", queryDTO);
        return propagationService.getAvailableBasicSeeds(queryDTO);
    }

    /**
     * 获取指定批次的可用数量
     *
     * @param batchId 批次ID
     * @param sourceType 数据来源类型 (OSE_RECEIVE/OSE_BATCH_COLLECTION)
     * @return 可用数量信息
     */
    @GetMapping("/available-quantity/{batchId}")
    public AjaxResult getAvailableQuantity(
            @PathVariable("batchId") String batchId,
            @RequestParam(value = "sourceType", required = false) String sourceType) {
        log.info("获取批次可用数量,batchId: {}, sourceType: {}", batchId, sourceType);
        return propagationService.getAvailableQuantity(batchId, sourceType);
    }
}
