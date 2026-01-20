package com.inspur.farmland.controller;

import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.exception.ServiceException;
import com.inspur.farmland.domain.DaInfo;
import com.inspur.farmland.service.IDaInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DA管理Controller
 *
 * @author inspur
 */
@RestController
@RequestMapping("/farmland/da")
public class DaInfoController extends BaseController {

    @Autowired
    private IDaInfoService daInfoService;

    /**
     * 获取DA分页列表
     */
    @GetMapping("/page")
    public TableDataInfo page(DaInfo daInfo) {
        startPage();
        List<DaInfo> list = daInfoService.selectDaInfoList(daInfo);
        return getDataTable(list);
    }

    /**
     * 获取DA详情
     */
    @GetMapping("/{daId}")
    public AjaxResult getInfo(
            @PathVariable String daId) {
        DaInfo daInfo = daInfoService.selectDaInfoByDaId(daId);
        if (daInfo == null) {
            return AjaxResult.error("DA信息不存在");
        }
        return AjaxResult.success(daInfo);
    }

    /**
     * 新增DA
     */
    @PostMapping
    public AjaxResult add(@RequestBody DaInfo daInfo) {
        // 校验身份证号唯一性
        if (!daInfoService.checkIdCardUnique(daInfo.getIdCard(), null)) {
            return AjaxResult.error("身份证号已存在");
        }

        // 校验账号唯一性
        if (!daInfoService.checkAccountUnique(daInfo.getAccount(), null)) {
            return AjaxResult.error("登录账号已存在");
        }

        String daId = daInfoService.insertDaInfo(daInfo);
        Map<String, Object> result = new HashMap<>();
        result.put("daId", daId);
        return AjaxResult.success("新增成功", result);
    }

    /**
     * 修改DA
     */
    @PostMapping("/{daId}")
    public AjaxResult edit(
            @PathVariable String daId,
            @RequestBody DaInfo daInfo) {
        // 校验DA是否存在
        DaInfo existDa = daInfoService.selectDaInfoByDaId(daId);
        if (existDa == null) {
            return AjaxResult.error("DA信息不存在");
        }

        int rows = daInfoService.updateDaInfo(daId, daInfo);
        return toAjax(rows);
    }

    /**
     * 删除DA
     */
    @PostMapping("/{daId}/delete")
    public AjaxResult remove(
            @PathVariable String daId) {
        int rows = daInfoService.deleteDaInfoByDaId(daId);
        return toAjax(rows);
    }

    /**
     * 启用/禁用DA账号
     */
    @PostMapping("/{daId}/status")
    public AjaxResult updateStatus(
            @PathVariable String daId,
            @RequestBody Map<String, String> params) {
        String accountStatus = params.get("accountStatus");
        if (!"0".equals(accountStatus) && !"1".equals(accountStatus)) {
            return AjaxResult.error("账号状态参数错误");
        }

        int rows = daInfoService.updateAccountStatus(daId, accountStatus);
        return toAjax(rows);
    }

    /**
     * 重置DA密码
     */
    @PostMapping("/{daId}/password/reset")
    public AjaxResult resetPassword(
            @PathVariable String daId,
            @RequestBody Map<String, String> params) {
        String newPassword = params.get("newPassword");
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return AjaxResult.error("新密码不能为空");
        }

        // 密码复杂度校验（至少8位，包含大小写字母和数字）
        if (!newPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")) {
            return AjaxResult.error("密码必须至少8位，包含大小写字母和数字");
        }

        int rows = daInfoService.resetPassword(daId, newPassword);
        return toAjax(rows);
    }

    /**
     * 获取DA下拉选项
     */
    @GetMapping("/options")
    public AjaxResult options(
            @RequestParam(required = false) String kebeleCode) {
        List<Map<String, Object>> options = daInfoService.selectDaOptions(kebeleCode);
        return AjaxResult.success(options);
    }

    /**
     * 检查账号是否可用
     * @param account 账号
     * @param excludeDaId 排除的DA编码（编辑时使用）
     * @return true-可用，false-已存在
     */
    @GetMapping("/checkAccount")
    public AjaxResult checkAccount(
            @RequestParam String account,
            @RequestParam(required = false) String excludeDaId) {
        boolean unique = daInfoService.checkAccountUnique(account, excludeDaId);
        return AjaxResult.success(unique);
    }
}
