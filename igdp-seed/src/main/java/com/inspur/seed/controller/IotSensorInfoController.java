package com.inspur.seed.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.enums.BusinessType;
import com.inspur.seed.domain.dto.IotSensorQueryDto;
import com.inspur.seed.domain.dto.IotSensorSaveDto;
import com.inspur.seed.domain.vo.IotSensorVo;
import com.inspur.seed.service.IIotSensorInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 物联网传感器信息控制器
 *
 * @author igdp
 * @date 2025-11-30
 */
@RestController
@RequestMapping("/seed/iotSensor")
public class IotSensorInfoController extends BaseController {

    @Resource
    private IIotSensorInfoService iotSensorInfoService;

    /**
     * 分页查询物联网传感器信息
     *
     * @param queryDto 查询条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public AjaxResult page(@Validated @RequestBody IotSensorQueryDto queryDto) {
        try {
            IPage<IotSensorVo> result = iotSensorInfoService.page(queryDto);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询物联网传感器信息列表（不分页）
     *
     * @param queryDto 查询条件
     * @return 列表结果
     */
    @PostMapping("/list")
    public AjaxResult list(@Validated @RequestBody IotSensorQueryDto queryDto) {
        try {
            List<IotSensorVo> result = iotSensorInfoService.list(queryDto);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增物联网传感器
     *
     * @param saveDto 新增数据
     * @return 新增结果
     */
    @Log(title = "物联网传感器管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody IotSensorSaveDto saveDto) {
        return iotSensorInfoService.add(saveDto);
    }

    /**
     * 修改物联网传感器
     *
     * @param saveDto 修改数据
     * @return 修改结果
     */
    @Log(title = "物联网传感器管理", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public AjaxResult update(@Validated @RequestBody IotSensorSaveDto saveDto) {
        return iotSensorInfoService.update(saveDto);
    }

    /**
     * 查询物联网传感器详情
     *
     * @param dataId 主键ID
     * @return 传感器详情
     */
    @GetMapping("/detail")
    public AjaxResult detail(@RequestParam("dataId") String dataId) {
        try {
            IotSensorVo result = iotSensorInfoService.detail(dataId);
            if (result == null) {
                return AjaxResult.error("传感器信息不存在");
            }
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 删除物联网传感器
     *
     * @param dataIds 主键ID列表
     * @return 删除结果
     */
    @Log(title = "物联网传感器管理", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody List<String> dataIds) {
        return iotSensorInfoService.delete(dataIds);
    }
}
