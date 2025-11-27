package com.inspur.seed.controller;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.seed.domain.dto.TrialBaseDataDTO;
import com.inspur.seed.domain.vo.TrialBaseDataVO;
import com.inspur.seed.service.ITrialBaseDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 试验基础数据采集Controller
 *
 * @author igdp
 * @date 2025-11-26
 */
@RestController
@RequestMapping("/seed/trial/base")
public class TrialBaseDataController {

    @Autowired
    private ITrialBaseDataService trialBaseDataService;

    /**
     * 查询试验基础数据采集列表
     */
    @PostMapping("/list")
    public AjaxResult list(@RequestBody TrialBaseDataDTO dto) {
        List<TrialBaseDataVO> list = trialBaseDataService.selectTrialBaseDataList(dto);
        return AjaxResult.success(list);
    }

    /**
     * 获取试验基础数据采集详细信息
     */
    @GetMapping("/{trialId}")
    public AjaxResult getInfo(@PathVariable String trialId) {
        TrialBaseDataVO vo = trialBaseDataService.selectTrialBaseDataById(trialId);
        return AjaxResult.success(vo);
    }

    /**
     * 新增试验基础数据采集
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody TrialBaseDataDTO dto) {
        return AjaxResult.success(trialBaseDataService.insertTrialBaseData(dto));
    }

    /**
     * 修改试验基础数据采集
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody TrialBaseDataDTO dto) {
        return AjaxResult.success(trialBaseDataService.updateTrialBaseData(dto));
    }

    /**
     * 删除试验基础数据采集
     */
    @PostMapping("/delete")
    public AjaxResult remove(@RequestBody String[] trialIds) {
        return AjaxResult.success(trialBaseDataService.deleteTrialBaseDataByIds(trialIds));
    }
}
