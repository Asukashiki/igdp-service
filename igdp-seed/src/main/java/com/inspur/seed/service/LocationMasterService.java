package com.inspur.seed.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.domain.dto.LocationMasterDTO;
import com.inspur.seed.domain.dto.LocationMasterQueryDTO;

/**
 * 研究中心Service接口
 */
public interface LocationMasterService {
    
    /**
     * 分页查询研究中心
     * 
     * @param page 分页参数
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    IPage<LocationMasterDTO> page(IPage<LocationMasterDTO> page, LocationMasterQueryDTO queryDTO);
    
    /**
     * 新增研究中心
     * 
     * @param locationMasterDTO 研究中心信息
     * @return 操作结果
     */
    AjaxResult add(LocationMasterDTO locationMasterDTO);
    
    /**
     * 修改研究中心
     * 
     * @param locationMasterDTO 研究中心信息
     * @return 操作结果
     */
    AjaxResult update(LocationMasterDTO locationMasterDTO);
    
    /**
     * 删除研究中心
     * 
     * @param locationId 位置ID
     * @return 操作结果
     */
    AjaxResult delete(String locationId);
    
    /**
     * 获取研究中心详情
     * 
     * @param locationId 位置ID
     * @return 研究中心信息
     */
    AjaxResult detail(String locationId);
    
    /**
     * 查询研究中心列表
     * 
     * @param queryDTO 查询条件
     * @return 研究中心列表
     */
    AjaxResult list(LocationMasterQueryDTO queryDTO);
}