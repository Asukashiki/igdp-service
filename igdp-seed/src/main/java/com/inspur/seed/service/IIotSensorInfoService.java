package com.inspur.seed.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.domain.dto.IotSensorQueryDto;
import com.inspur.seed.domain.dto.IotSensorSaveDto;
import com.inspur.seed.domain.entity.IotSensorInfo;
import com.inspur.seed.domain.vo.IotSensorVo;

import java.util.List;

/**
 * 物联网传感器信息Service接口
 *
 * @author igdp
 * @date 2025-11-30
 */
public interface IIotSensorInfoService extends IService<IotSensorInfo> {

    /**
     * 分页查询物联网传感器信息
     *
     * @param queryDto 查询条件
     * @return 分页结果
     */
    IPage<IotSensorVo> page(IotSensorQueryDto queryDto);

    /**
     * 查询物联网传感器信息列表（不分页）
     *
     * @param queryDto 查询条件
     * @return 列表数据
     */
    List<IotSensorVo> list(IotSensorQueryDto queryDto);

    /**
     * 新增物联网传感器信息
     *
     * @param saveDto 新增数据
     * @return 结果
     */
    AjaxResult add(IotSensorSaveDto saveDto);

    /**
     * 修改物联网传感器信息
     *
     * @param saveDto 修改数据
     * @return 结果
     */
    AjaxResult update(IotSensorSaveDto saveDto);

    /**
     * 查询物联网传感器详情
     *
     * @param dataId 主键ID
     * @return 传感器详情
     */
    IotSensorVo detail(String dataId);

    /**
     * 删除物联网传感器信息（逻辑删除）
     *
     * @param dataIds 主键ID列表
     * @return 结果
     */
    AjaxResult delete(List<String> dataIds);
}
