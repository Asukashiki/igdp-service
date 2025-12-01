package com.inspur.seed.controller;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.domain.dto.BreedingLicenseDTO;
import com.inspur.seed.domain.dto.BreedingLicenseQueryDTO;
import com.inspur.seed.service.IBreedingLicenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 育种许可Controller
 *
 * @author system
 * @since 2025-01-30
 */
@Slf4j
@RestController
@RequestMapping("/seed/license")
public class BreedingLicenseController {

    @Autowired
    private IBreedingLicenseService licenseService;

    /**
     * 获取许可列表(分页)
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @PostMapping("/list")
    public AjaxResult getLicenseList(@RequestBody BreedingLicenseQueryDTO queryDTO) {
        log.info("获取许可列表,查询条件: {}", queryDTO);
        return licenseService.getLicenseList(queryDTO);
    }

    /**
     * 根据ID获取许可详情(包含物种特性)
     *
     * @param id 许可ID
     * @return 许可详情
     */
    @GetMapping("/getById/{id}")
    public AjaxResult getLicenseById(@PathVariable("id") String id) {
        log.info("获取许可详情,ID: {}", id);
        return licenseService.getLicenseById(id);
    }

    /**
     * 根据批次ID获取许可详情
     *
     * @param batchId 批次ID
     * @return 许可详情
     */
    @GetMapping("/getByBatchId/{batchId}")
    public AjaxResult getLicenseByBatchId(@PathVariable("batchId") String batchId) {
        log.info("根据批次ID获取许可,批次ID: {}", batchId);
        return licenseService.getLicenseByBatchId(batchId);
    }

    /**
     * 新增许可(包含物种特性)
     *
     * @param dto 许可信息
     * @return 操作结果
     */
    @PostMapping("/add")
    public AjaxResult addLicense(@Validated @RequestBody BreedingLicenseDTO dto) {
        log.info("新增许可,批次ID: {}", dto.getBatchId());
        return licenseService.addLicense(dto);
    }

    /**
     * 修改许可(包含物种特性)
     *
     * @param dto 许可信息
     * @return 操作结果
     */
    @PostMapping("/update")
    public AjaxResult updateLicense(@Validated @RequestBody BreedingLicenseDTO dto) {
        log.info("修改许可,ID: {}", dto.getId());
        return licenseService.updateLicense(dto);
    }

    /**
     * 删除许可
     *
     * @param ids 许可ID数组
     * @return 操作结果
     */
    @PostMapping("/delete")
    public AjaxResult deleteLicense(@RequestBody String[] ids) {
        log.info("删除许可,数量: {}", ids.length);
        return licenseService.deleteLicense(ids);
    }
}
