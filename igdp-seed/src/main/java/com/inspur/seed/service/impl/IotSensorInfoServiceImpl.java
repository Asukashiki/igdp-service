package com.inspur.seed.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.constant.IotSensorConstant;
import com.inspur.seed.domain.dto.IotSensorQueryDto;
import com.inspur.seed.domain.dto.IotSensorSaveDto;
import com.inspur.seed.domain.entity.IotSensorInfo;
import com.inspur.seed.domain.vo.IotSensorVo;
import com.inspur.seed.mapper.IotSensorInfoMapper;
import com.inspur.seed.service.IIotSensorInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 物联网传感器信息Service实现类
 *
 * @author igdp
 * @date 2025-11-30
 */
@Service
public class IotSensorInfoServiceImpl extends ServiceImpl<IotSensorInfoMapper, IotSensorInfo> implements IIotSensorInfoService {

    @Override
    public IPage<IotSensorVo> page(IotSensorQueryDto queryDto) {
        // 构建分页条件
        Page<IotSensorInfo> page = new Page<>(queryDto.getPageNum(), queryDto.getPageSize());

        // 构建查询条件
        QueryWrapper<IotSensorInfo> queryWrapper = buildQueryWrapper(queryDto);

        // 执行分页查询
        IPage<IotSensorInfo> resultPage = this.page(page, queryWrapper);

        // 转换为VO
        return resultPage.convert(this::convertToVo);
    }

    @Override
    public List<IotSensorVo> list(IotSensorQueryDto queryDto) {
        // 构建查询条件
        QueryWrapper<IotSensorInfo> queryWrapper = buildQueryWrapper(queryDto);

        // 执行查询
        List<IotSensorInfo> list = this.list(queryWrapper);

        // 转换为VO
        return list.stream().map(this::convertToVo).collect(Collectors.toList());
    }

    @Override
    public AjaxResult add(IotSensorSaveDto saveDto) {
        try {
            // 创建实体对象
            IotSensorInfo sensorInfo = new IotSensorInfo();
            BeanUtils.copyProperties(saveDto, sensorInfo);

            // 设置创建时间和删除标志
            sensorInfo.setCreateTime(new Date());
            sensorInfo.setDelFlag(IotSensorConstant.DEL_FLAG_NORMAL);

            // TODO: 从当前登录用户上下文获取orgId和orgName
            // sensorInfo.setOrgId(SecurityUtils.getOrgId());
            // sensorInfo.setOrgName(SecurityUtils.getOrgName());
            // sensorInfo.setCreateBy(SecurityUtils.getUsername());

            // 保存数据
            boolean result = this.save(sensorInfo);

            if (result) {
                return AjaxResult.success("新增成功", sensorInfo.getDataId());
            } else {
                return AjaxResult.error("新增失败");
            }
        } catch (Exception e) {
            return AjaxResult.error("新增失败: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult update(IotSensorSaveDto saveDto) {
        try {
            // 校验dataId是否存在
            if (StrUtil.isBlank(saveDto.getDataId())) {
                return AjaxResult.error("主键ID不能为空");
            }

            IotSensorInfo existInfo = this.getById(saveDto.getDataId());
            if (existInfo == null) {
                return AjaxResult.error("传感器信息不存在");
            }

            // 创建实体对象
            IotSensorInfo sensorInfo = new IotSensorInfo();
            BeanUtils.copyProperties(saveDto, sensorInfo);

            // 设置更新时间
            sensorInfo.setUpdateTime(new Date());

            // TODO: 从当前登录用户上下文获取updateBy
            // sensorInfo.setUpdateBy(SecurityUtils.getUsername());

            // 执行更新
            boolean result = this.updateById(sensorInfo);

            if (result) {
                return AjaxResult.success("修改成功");
            } else {
                return AjaxResult.error("修改失败");
            }
        } catch (Exception e) {
            return AjaxResult.error("修改失败: " + e.getMessage());
        }
    }

    @Override
    public IotSensorVo detail(String dataId) {
        IotSensorInfo sensorInfo = this.getById(dataId);
        if (sensorInfo == null) {
            return null;
        }
        return convertToVo(sensorInfo);
    }

    @Override
    public AjaxResult delete(List<String> dataIds) {
        try {
            if (dataIds == null || dataIds.isEmpty()) {
                return AjaxResult.error("请选择要删除的数据");
            }

            // 逻辑删除，更新del_flag为"2"
            List<IotSensorInfo> list = dataIds.stream()
                    .map(id -> {
                        IotSensorInfo entity = new IotSensorInfo();
                        entity.setDataId(id);
                        entity.setDelFlag(IotSensorConstant.DEL_FLAG_DELETED);
                        entity.setUpdateTime(new Date());
                        return entity;
                    })
                    .collect(Collectors.toList());

            boolean result = this.updateBatchById(list);

            if (result) {
                return AjaxResult.success("删除成功");
            } else {
                return AjaxResult.error("删除失败");
            }
        } catch (Exception e) {
            return AjaxResult.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 构建查询条件
     *
     * @param queryDto 查询条件DTO
     * @return QueryWrapper
     */
    private QueryWrapper<IotSensorInfo> buildQueryWrapper(IotSensorQueryDto queryDto) {
        QueryWrapper<IotSensorInfo> queryWrapper = new QueryWrapper<>();

        // 传感器名称（模糊查询）
        if (StrUtil.isNotBlank(queryDto.getIotName())) {
            queryWrapper.like("iot_name", queryDto.getIotName());
        }

        // 传感器类型（精确查询）
        if (StrUtil.isNotBlank(queryDto.getIotType())) {
            queryWrapper.eq("iot_type", queryDto.getIotType());
        }

        // 时间范围查询（基于create_time字段）
        if (queryDto.getStartTime() != null) {
            queryWrapper.ge("create_time", queryDto.getStartTime());
        }
        if (queryDto.getEndTime() != null) {
            queryWrapper.le("create_time", queryDto.getEndTime());
        }

        // 过滤已删除数据
        queryWrapper.eq("del_flag", IotSensorConstant.DEL_FLAG_NORMAL);

        // 按创建时间倒序
        queryWrapper.orderByDesc("create_time");

        return queryWrapper;
    }

    /**
     * 转换为VO对象
     *
     * @param sensorInfo 实体对象
     * @return VO对象
     */
    private IotSensorVo convertToVo(IotSensorInfo sensorInfo) {
        IotSensorVo vo = new IotSensorVo();
        BeanUtils.copyProperties(sensorInfo, vo);

        // 补充类型名称
        vo.setIotTypeName(getIotTypeName(sensorInfo.getIotType()));

        return vo;
    }

    /**
     * 获取传感器类型名称
     *
     * @param iotType 传感器类型编码
     * @return 类型名称
     */
    private String getIotTypeName(String iotType) {
        if (iotType == null) {
            return "";
        }
        switch (iotType) {
            case IotSensorConstant.IOT_TYPE_TEMPERATURE:
                return "温度传感器";
            case IotSensorConstant.IOT_TYPE_HUMIDITY:
                return "湿度传感器";
            case IotSensorConstant.IOT_TYPE_LIGHT:
                return "光照传感器";
            case IotSensorConstant.IOT_TYPE_SOIL:
                return "土壤传感器";
            case IotSensorConstant.IOT_TYPE_GAS:
                return "气体传感器";
            case IotSensorConstant.IOT_TYPE_OTHER:
                return "其他";
            default:
                return "";
        }
    }
}
