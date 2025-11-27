package com.inspur.seed.controller;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.domain.dto.AgronomicTraitDataDTO;
import com.inspur.seed.domain.vo.AgronomicTraitDataVO;
import com.inspur.seed.service.IAgronomicTraitDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 农艺性状数据采集Controller
 *
 * @author igdp
 * @date 2025-11-26
 */
@RestController
@RequestMapping("/seed/agronomic/trait")
public class AgronomicTraitDataController {

    @Autowired
    private IAgronomicTraitDataService agronomicTraitDataService;

    /**
     * 查询农艺性状数据列表
     */
    @PostMapping("/list")
    public AjaxResult list(@RequestBody AgronomicTraitDataDTO dto) {
        List<AgronomicTraitDataVO> list = agronomicTraitDataService.selectAgronomicTraitDataList(dto);
        return AjaxResult.success(list);
    }

    /**
     * 获取农艺性状数据详细信息
     */
    @GetMapping("/{dataId}")
    public AjaxResult getInfo(@PathVariable String dataId) {
        AgronomicTraitDataVO vo = agronomicTraitDataService.selectAgronomicTraitDataById(dataId);
        return AjaxResult.success(vo);
    }

    /**
     * 新增农艺性状数据
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody AgronomicTraitDataDTO dto) {
        return AjaxResult.success(agronomicTraitDataService.insertAgronomicTraitData(dto));
    }

    /**
     * 修改农艺性状数据
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody AgronomicTraitDataDTO dto) {
        return AjaxResult.success(agronomicTraitDataService.updateAgronomicTraitData(dto));
    }

    /**
     * 删除农艺性状数据
     */
    @PostMapping("/delete")
    public AjaxResult remove(@RequestBody String[] dataIds) {
        return AjaxResult.success(agronomicTraitDataService.deleteAgronomicTraitDataByIds(dataIds));
    }
}
