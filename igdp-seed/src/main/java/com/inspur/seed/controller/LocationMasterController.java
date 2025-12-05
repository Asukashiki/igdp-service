package com.inspur.seed.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.domain.dto.LocationMasterDTO;
import com.inspur.seed.domain.dto.LocationMasterQueryDTO;
import com.inspur.seed.service.LocationMasterService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 研究中心Controller
 */
@RestController
@RequestMapping("/seed/locationMaster")
public class LocationMasterController {
    
    @Resource
    private LocationMasterService locationMasterService;
    
    /**
     * 分页查询研究中心
     * 
     * @param page 分页参数
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public AjaxResult page(@RequestParam(defaultValue = "1") Integer current,
                          @RequestParam(defaultValue = "10") Integer size,
                          @RequestBody LocationMasterQueryDTO queryDTO) {
        IPage<LocationMasterDTO> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(current, size);
        return AjaxResult.success(locationMasterService.page(page, queryDTO));
    }
    
    /**
     * 新增研究中心
     * 
     * @param locationMasterDTO 研究中心信息
     * @return 操作结果
     */
    @PostMapping("/add")
    public AjaxResult add(@Valid @RequestBody LocationMasterDTO locationMasterDTO) {
        return locationMasterService.add(locationMasterDTO);
    }
    
    /**
     * 修改研究中心
     * 
     * @param locationMasterDTO 研究中心信息
     * @return 操作结果
     */
    @PostMapping("/update")
    public AjaxResult update(@Valid @RequestBody LocationMasterDTO locationMasterDTO) {
        return locationMasterService.update(locationMasterDTO);
    }
    
    /**
     * 删除研究中心
     * 
     * @param locationId 位置ID
     * @return 操作结果
     */
    @GetMapping("/delete")
    public AjaxResult delete(@RequestParam String locationId) {
        return locationMasterService.delete(locationId);
    }
    
    /**
     * 获取研究中心详情
     * 
     * @param locationId 位置ID
     * @return 研究中心信息
     */
    @GetMapping("/detail")
    public AjaxResult detail(@RequestParam String locationId) {
        return locationMasterService.detail(locationId);
    }
    
    /**
     * 查询研究中心列表
     * 
     * @param queryDTO 查询条件
     * @return 研究中心列表
     */
    @PostMapping("/list")
    public AjaxResult list(@RequestBody LocationMasterQueryDTO queryDTO) {
        return locationMasterService.list(queryDTO);
    }
}