package com.inspur.seed.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.LocationMaster;
import com.inspur.seed.domain.dto.LocationMasterDTO;
import com.inspur.seed.domain.dto.LocationMasterQueryDTO;
import com.inspur.seed.domain.vo.LocationMasterVO;
import com.inspur.seed.mapper.LocationMasterMapper;
import com.inspur.seed.service.LocationMasterService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 研究中心Service实现类
 */
@Service
public class LocationMasterServiceImpl extends ServiceImpl<LocationMasterMapper, LocationMaster> implements LocationMasterService {
    
    @Resource
    private LocationMasterMapper locationMasterMapper;
    
    @Override
    public IPage<LocationMasterDTO> page(IPage<LocationMasterDTO> page, LocationMasterQueryDTO queryDTO) {
        QueryWrapper<LocationMaster> queryWrapper = new QueryWrapper<>();
        
        if (StrUtil.isNotBlank(queryDTO.getLocationId())) {
            queryWrapper.eq("location_id", queryDTO.getLocationId());
        }
        if (StrUtil.isNotBlank(queryDTO.getLocationName())) {
            queryWrapper.like("location_name", queryDTO.getLocationName());
        }
        if (StrUtil.isNotBlank(queryDTO.getRegion())) {
            queryWrapper.like("region", queryDTO.getRegion());
        }
        if (StrUtil.isNotBlank(queryDTO.getZone())) {
            queryWrapper.like("zone", queryDTO.getZone());
        }
        if (StrUtil.isNotBlank(queryDTO.getWoneda())) {
            queryWrapper.like("woneda", queryDTO.getWoneda());
        }
        
        queryWrapper.orderByDesc("create_time");
        
        IPage<LocationMaster> locationMasterPage = locationMasterMapper.selectPage(new Page<>(page.getCurrent(), page.getSize()), queryWrapper);
        
        IPage<LocationMasterDTO> resultPage = new Page<>(locationMasterPage.getCurrent(), locationMasterPage.getSize(), locationMasterPage.getTotal());
        List<LocationMasterDTO> dtoList = locationMasterPage.getRecords().stream().map(this::convertToDTO).collect(Collectors.toList());
        resultPage.setRecords(dtoList);
        
        return resultPage;
    }
    
    @Override
    public AjaxResult add(LocationMasterDTO locationMasterDTO) {
        LocationMaster locationMaster = convertToEntity(locationMasterDTO);
        locationMaster.setLocationId(UUID.randomUUID().toString());
        
        // 设置创建信息
        locationMaster.setCreateBy(SecurityUtils.getUsername());
        locationMaster.setCreateTime(new Date());
        locationMaster.setUpdateBy(SecurityUtils.getUsername());
        locationMaster.setUpdateTime(new Date());
        
        int result = locationMasterMapper.insert(locationMaster);
        if (result > 0) {
            return AjaxResult.success("研究中心新增成功");
        }
        return AjaxResult.error("研究中心新增失败");
    }
    
    @Override
    public AjaxResult update(LocationMasterDTO locationMasterDTO) {
        if (StrUtil.isBlank(locationMasterDTO.getLocationId())) {
            return AjaxResult.error("位置ID不能为空");
        }
        
        LocationMaster locationMaster = convertToEntity(locationMasterDTO);
        
        // 设置更新信息
        locationMaster.setUpdateBy(SecurityUtils.getUsername());
        locationMaster.setUpdateTime(new Date());
        
        int result = locationMasterMapper.updateById(locationMaster);
        if (result > 0) {
            return AjaxResult.success("研究中心修改成功");
        }
        return AjaxResult.error("研究中心修改失败");
    }
    
    @Override
    public AjaxResult delete(String locationId) {
        if (StrUtil.isBlank(locationId)) {
            return AjaxResult.error("位置ID不能为空");
        }
        
        int result = locationMasterMapper.deleteById(locationId);
        if (result > 0) {
            return AjaxResult.success("研究中心删除成功");
        }
        return AjaxResult.error("研究中心删除失败");
    }
    
    @Override
    public AjaxResult detail(String locationId) {
        if (StrUtil.isBlank(locationId)) {
            return AjaxResult.error("位置ID不能为空");
        }
        
        LocationMaster locationMaster = locationMasterMapper.selectById(locationId);
        if (locationMaster == null) {
            return AjaxResult.error("研究中心不存在");
        }
        
        LocationMasterVO vo = convertToVO(locationMaster);
        return AjaxResult.success(vo);
    }
    
    @Override
    public AjaxResult list(LocationMasterQueryDTO queryDTO) {
        QueryWrapper<LocationMaster> queryWrapper = new QueryWrapper<>();
        
        if (StrUtil.isNotBlank(queryDTO.getLocationId())) {
            queryWrapper.eq("location_id", queryDTO.getLocationId());
        }
        if (StrUtil.isNotBlank(queryDTO.getLocationName())) {
            queryWrapper.like("location_name", queryDTO.getLocationName());
        }
        if (StrUtil.isNotBlank(queryDTO.getRegion())) {
            queryWrapper.like("region", queryDTO.getRegion());
        }
        if (StrUtil.isNotBlank(queryDTO.getZone())) {
            queryWrapper.like("zone", queryDTO.getZone());
        }
        if (StrUtil.isNotBlank(queryDTO.getWoneda())) {
            queryWrapper.like("woneda", queryDTO.getWoneda());
        }
        
        queryWrapper.orderByDesc("create_time");
        
        List<LocationMaster> locationMasterList = locationMasterMapper.selectList(queryWrapper);
        List<LocationMasterVO> voList = locationMasterList.stream().map(this::convertToVO).collect(Collectors.toList());
        
        return AjaxResult.success(voList);
    }
    
    private LocationMasterDTO convertToDTO(LocationMaster locationMaster) {
        if (locationMaster == null) {
            return null;
        }
        LocationMasterDTO dto = new LocationMasterDTO();
        BeanUtils.copyProperties(locationMaster, dto);
        return dto;
    }
    
    private LocationMaster convertToEntity(LocationMasterDTO dto) {
        if (dto == null) {
            return null;
        }
        LocationMaster entity = new LocationMaster();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
    
    private LocationMasterVO convertToVO(LocationMaster locationMaster) {
        if (locationMaster == null) {
            return null;
        }
        LocationMasterVO vo = new LocationMasterVO();
        BeanUtils.copyProperties(locationMaster, vo);
        return vo;
    }
}