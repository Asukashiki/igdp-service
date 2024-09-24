package com.inspur.ucif.controller;

import com.inspur.common.config.SystemConfig;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.domain.entity.SysDept;
import com.inspur.common.core.domain.entity.SysRole;
import com.inspur.common.core.domain.entity.SysUser;
import com.inspur.ucif.service.IAccountStrategy;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author liyunlong
 * @date 2024/1/10
 */
@RestController
@RequestMapping("/ucif/account")
@RefreshScope
public class UcifAccountController extends BaseController {

    @Resource
    private IAccountStrategy accountStrategy;

    /**
     * 获取组织树
     */
    @GetMapping("/deptTree")
    public AjaxResult getDeptTree(SysDept dept) {
        return AjaxResult.success(accountStrategy.getInstance(SystemConfig.getAccountSelectType()).getDeptTree(dept));
    }

    /**
     * 获取组织列表
     */
    @GetMapping("/deptList")
    public AjaxResult getDeptList(SysDept dept) {
        return AjaxResult.success(accountStrategy.getInstance(SystemConfig.getAccountSelectType()).getDeptList(dept));
    }

    /**
     * 根据id获取部门信息
     */
    @GetMapping("/getDeptInfo")
    public AjaxResult getDeptInfoByDeptId(@RequestParam("deptId") String deptId) {
        return AjaxResult.success(accountStrategy.getInstance(SystemConfig.getAccountSelectType()).getDeptById(deptId));
    }

    @GetMapping("/getUserInfo")
    public AjaxResult getUserInfoByUserId(@RequestParam("userId") String userId) {
        return AjaxResult.success(accountStrategy.getInstance(SystemConfig.getAccountSelectType()).getUserById(userId));
    }

    /**
     * 查询用户列表
     */
    @GetMapping("/searchUserList")
    public AjaxResult searchUserList(SysUser user) {
        return AjaxResult.success(accountStrategy.getInstance(SystemConfig.getAccountSelectType()).searchUserList(user));
    }

    /**
     * 查询用户列表，带分页
     */
    @GetMapping("/searchUserWithPage")
    public AjaxResult searchUserWithPage(String keyword, Integer pageNum, Integer pageSize,String deptId,String regionId,String roleId,String roleKey) {
        SysUser user = new SysUser();
        user.setDeptId(deptId);
        user.getParams().put("keyword", keyword);
        user.setRoleId(roleId);
        user.setRoleKey(roleKey);
        return accountStrategy.getInstance(SystemConfig.getAccountSelectType()).searchUserPage(user, pageNum, pageSize);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/currentUser")
    public AjaxResult getCurrentUser() {
        return AjaxResult.success(accountStrategy.getInstance(SystemConfig.getAccountSelectType()).getCurrentUser());
    }

    /**
     * 获取角色分页列表
     */
    @GetMapping("/rolePage")
    public AjaxResult getRolePage(SysRole role, Integer pageNum, Integer pageSize) {

        if (null == pageNum) {
            pageNum = 1;
        }
        if (null == pageSize) {
            pageSize = 20;
        }
        return accountStrategy.getInstance(SystemConfig.getAccountSelectType()).searchRolePage(role, pageNum, pageSize);
    }

    /**
     * 获取部门用户树列表
     * 包括部门和用户组成的组合树
     */
    @GetMapping("/deptUserTree")
    public AjaxResult deptUserTree() {
        return success(accountStrategy.getDeptUserTreeList());
    }


}
