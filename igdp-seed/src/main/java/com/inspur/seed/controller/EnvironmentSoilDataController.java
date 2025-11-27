package com.inspur.seed.controller;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.domain.dto.EnvironmentSoilDataDTO;
import com.inspur.seed.domain.vo.EnvironmentSoilDataVO;
import com.inspur.seed.service.IEnvironmentSoilDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 环境与土壤属性数据采集Controller
 *
 * @author igdp
 * @date 2025-11-26
 */
@RestController
@RequestMapping("/seed/environment/soil")
public class EnvironmentSoilDataController {

    @Autowired
    private IEnvironmentSoilDataService environmentSoilDataService;

    /**
     * 查询环境与土壤属性数据列表
     */
    @PostMapping("/list")
    public AjaxResult list(@RequestBody EnvironmentSoilDataDTO dto) {
        List<EnvironmentSoilDataVO> list = environmentSoilDataService.selectEnvironmentSoilDataList(dto);
        return AjaxResult.success(list);
    }

    /**
     * 获取环境与土壤属性数据详细信息
     */
    @GetMapping("/{dataId}")
    public AjaxResult getInfo(@PathVariable String dataId) {
        EnvironmentSoilDataVO vo = environmentSoilDataService.selectEnvironmentSoilDataById(dataId);
        return AjaxResult.success(vo);
    }

    /**
     * 新增环境与土壤属性数据
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody EnvironmentSoilDataDTO dto) {
        return AjaxResult.success(environmentSoilDataService.insertEnvironmentSoilData(dto));
    }

    /**
     * 修改环境与土壤属性数据
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody EnvironmentSoilDataDTO dto) {
        return AjaxResult.success(environmentSoilDataService.updateEnvironmentSoilData(dto));
    }

    /**
     * 删除环境与土壤属性数据
     */
    @PostMapping("/delete")
    public AjaxResult remove(@RequestBody String[] dataIds) {
        return AjaxResult.success(environmentSoilDataService.deleteEnvironmentSoilDataByIds(dataIds));
    }
}
