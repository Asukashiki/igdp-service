package com.inspur.agriculture.input.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inspur.agriculture.input.domain.AgriInput;
import com.inspur.agriculture.input.service.IAgriInputService;
import com.inspur.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 农业投入品Controller
 *
 * @author igdp
 */
@RestController
@RequestMapping("/agriculture/input")
public class AgriInputController {

    @Autowired
    private IAgriInputService agriInputService;

    /**
     * 查询投入品列表（分页）
     *
     * @param inputName    投入品名称
     * @param type         投入品类型
     * @param registerCode 登记批号
     * @param inputSku     SKU编码
     * @param status       状态
     * @param keyword      关键词搜索
     * @param page         页码
     * @param pageSize     每页数量
     * @return 投入品列表
     */
    @GetMapping("/list")
    public AjaxResult list(
            @RequestParam(required = false) String inputName,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String registerCode,
            @RequestParam(required = false) String inputSku,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        AgriInput agriInput = new AgriInput();
        agriInput.setInputName(inputName);
        agriInput.setType(type);
        agriInput.setRegisterCode(registerCode);
        agriInput.setInputSku(inputSku);
        agriInput.setStatus(status);

        // 如果有keyword，覆盖inputName和registerCode
        if (keyword != null && !keyword.trim().isEmpty()) {
            agriInput.setInputName(keyword);
            agriInput.setRegisterCode(keyword);
            agriInput.setInputSku(keyword);
        }

        // 启动分页
        PageHelper.startPage(page, pageSize);
        List<AgriInput> list = agriInputService.selectInputList(agriInput);
        PageInfo<AgriInput> pageInfo = new PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("list", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("page", pageInfo.getPageNum());
        result.put("pageSize", pageInfo.getPageSize());

        return AjaxResult.success(result);
    }

    @GetMapping("/getAllInputList")
    public AjaxResult getAllInputList() {
        AgriInput agriInput = new AgriInput();
        agriInput.setDelFlag("0");
        agriInput.setStatus("active");
        List<AgriInput> list = agriInputService.selectInputList(agriInput);
        return AjaxResult.success(list);
    }

    /**
     * 获取投入品详情
     *
     * @param id 投入品ID
     * @return 投入品详情
     */
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        AgriInput agriInput = agriInputService.selectInputById(id);
        if (agriInput == null) {
            return AjaxResult.error("投入品不存在");
        }
        return AjaxResult.success(agriInput);
    }

    /**
     * 新增投入品
     *
     * @param agriInput 投入品信息
     * @return 操作结果
     */
    @PostMapping
    public AjaxResult add(@RequestBody AgriInput agriInput) {
        // 参数校验
        if (agriInput.getInputName() == null || agriInput.getInputName().trim().isEmpty()) {
            return AjaxResult.error("投入品名称不能为空");
        }
        if (agriInput.getType() == null || agriInput.getType().trim().isEmpty()) {
            return AjaxResult.error("投入品类型不能为空");
        }
        if (agriInput.getInputSku() == null || agriInput.getInputSku().trim().isEmpty()) {
            return AjaxResult.error("产品标识码不能为空");
        }

        // 设置默认状态
        if (agriInput.getStatus() == null || agriInput.getStatus().trim().isEmpty()) {
            agriInput.setStatus("active");
        }

        int rows = agriInputService.insertInput(agriInput);
        if (rows > 0) {
            return AjaxResult.success("新增成功", agriInput);
        }
        return AjaxResult.error("新增失败");
    }

    /**
     * 修改投入品
     *
     * @param id        投入品ID
     * @param agriInput 投入品信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public AjaxResult edit(@PathVariable("id") Long id, @RequestBody AgriInput agriInput) {
        // 检查投入品是否存在
        AgriInput existInput = agriInputService.selectInputById(id);
        if (existInput == null) {
            return AjaxResult.error("投入品不存在");
        }

        agriInput.setInputId(id);
        int rows = agriInputService.updateInput(agriInput);
        if (rows > 0) {
            return AjaxResult.success("修改成功", agriInput);
        }
        return AjaxResult.error("修改失败");
    }

    /**
     * 删除投入品
     *
     * @param id 投入品ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable("id") Long id) {
        int rows = agriInputService.deleteInputById(id);
        if (rows > 0) {
            return AjaxResult.success("删除成功");
        }
        return AjaxResult.error("删除失败");
    }

    /**
     * 批量删除投入品
     *
     * @param ids 投入品ID数组
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    public AjaxResult removeBatch(@RequestBody Long[] ids) {
        if (ids == null || ids.length == 0) {
            return AjaxResult.error("请选择要删除的投入品");
        }

        int rows = agriInputService.deleteInputByIds(ids);
        if (rows > 0) {
            return AjaxResult.success("批量删除成功");
        }
        return AjaxResult.error("批量删除失败");
    }

    /**
     * 获取投入品统计信息
     *
     * @return 统计信息
     */
    @GetMapping("/statistics")
    public AjaxResult getStatistics() {
        Map<String, Object> statistics = agriInputService.getInputStatistics();
        return AjaxResult.success(statistics);
    }

    /**
     * 导出投入品数据
     *
     * @param inputName 投入品名称
     * @param type      投入品类型
     * @param keyword   关键词搜索
     * @return 投入品数据
     */
    @GetMapping("/export")
    public AjaxResult export(
            @RequestParam(required = false) String inputName,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword
    ) {
        AgriInput agriInput = new AgriInput();
        agriInput.setInputName(inputName);
        agriInput.setType(type);

        // 如果有keyword，覆盖inputName
        if (keyword != null && !keyword.trim().isEmpty()) {
            agriInput.setInputName(keyword);
            agriInput.setRegisterCode(keyword);
        }

        List<AgriInput> list = agriInputService.selectInputList(agriInput);
        return AjaxResult.success("导出成功", list);
    }
}
