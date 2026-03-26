package com.inspur.seed.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.controller.BaseController;
import com.inspur.seed.domain.dto.RulesInfoDTO;
import com.inspur.seed.domain.entity.RulesInfo;
import com.inspur.seed.domain.vo.RulesInfoVO;
import com.inspur.seed.service.IRulesInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * 规则信息Controller
 *
 * @author igdp
 * @date 2025-12-18
 */
@RestController
@RequestMapping("/seed/rulesInfo")
public class RulesInfoController extends BaseController {

    @Autowired
    private IRulesInfoService rulesInfoService;

    /**
     * 分页查询规则信息列表
     *
     * @param current 当前页码
     * @param size 每页条数
     * @param dto 查询条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public AjaxResult page(@RequestParam(defaultValue = "1") Integer current,
                          @RequestParam(defaultValue = "10") Integer size,
                          @RequestBody RulesInfoDTO dto) {
        IPage<com.inspur.seed.domain.entity.RulesInfo> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(current, size);
        IPage<RulesInfoVO> result = rulesInfoService.selectRulesInfoPage(page, dto);
        return AjaxResult.success(result);
    }

    /**
     * 查询规则信息列表
     *
     * @param dto 查询条件
     * @return 规则信息列表
     */
    @PostMapping("/list")
    public AjaxResult list(@RequestBody RulesInfoDTO dto) {
        return AjaxResult.success(rulesInfoService.selectRulesInfoList(dto));
    }

    /**
     * 获取规则信息详情
     *
     * @param id 规则信息ID
     * @return 规则信息详情
     */
    @GetMapping("/detail")
    public AjaxResult detail(@RequestParam Integer id) {
        return AjaxResult.success(rulesInfoService.selectRulesInfoById(id));
    }

    /**
     * 新增规则信息
     *
     * @param dto 规则信息
     * @return 操作结果
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody RulesInfoDTO dto) {
        return toAjax(rulesInfoService.insertRulesInfo(dto));
    }

    /**
     * 修改规则信息
     *
     * @param dto 规则信息
     * @return 操作结果
     */
    @PostMapping("/update")
    public AjaxResult update(@RequestBody RulesInfoDTO dto) {
        return toAjax(rulesInfoService.updateRulesInfo(dto));
    }

    /**
     * 删除规则信息
     *
     * @param ids 规则信息ID数组
     * @return 操作结果
     */
    @GetMapping("/delete")
    public AjaxResult delete(@RequestParam Integer[] ids) {
        return toAjax(rulesInfoService.deleteRulesInfoByIds(ids));
    }

    /**
     * 获取所有检测类型（去重）
     */
    @GetMapping("/inspectionTypes")
    public AjaxResult getInspectionTypes() {
        QueryWrapper<RulesInfo> wrapper = new QueryWrapper<>();
        wrapper.select("DISTINCT inspection_type");
        wrapper.orderByAsc("MIN(id)");
        wrapper.groupBy("inspection_type");
        List<RulesInfo> list = rulesInfoService.list(wrapper);
        List<String> types = list.stream().map(RulesInfo::getInspectionType).collect(java.util.stream.Collectors.toList());
        return AjaxResult.success(types);
    }

    /**
     * 根据检测类型获取规则列表
     */
    @GetMapping("/listByType")
    public AjaxResult listByInspectionType(@RequestParam String inspectionType) {
        QueryWrapper<RulesInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("inspection_type", inspectionType);
        wrapper.orderByAsc("id");
        return AjaxResult.success(rulesInfoService.list(wrapper));
    }

    /**
     * 根据字典编码和值判断是否满足条件
     *
     * @param dictCode 字典编码
     * @param value 值
     * @return 是否满足条件
     */
    @GetMapping("/checkRule")
    public AjaxResult checkRule(@RequestParam String dictCode, @RequestParam BigDecimal value) {
        return AjaxResult.success(rulesInfoService.checkRule(dictCode, value));
    }
}
