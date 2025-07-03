package com.inspur.ucif.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import com.inspur.common.config.SystemConfig;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.domain.entity.SysDept;
import com.inspur.common.core.domain.entity.SysMenu;
import com.inspur.common.core.domain.entity.SysRole;
import com.inspur.common.core.domain.entity.SysUser;
import com.inspur.common.core.domain.model.LoginUser;
import com.inspur.common.utils.LoginHelper;
import com.inspur.system.service.ISysMenuService;
import com.inspur.ucif.service.IAccountStrategy;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

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
    @Resource
    private ISysMenuService menuService;

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

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        LoginUser loginUser = LoginHelper.getLoginUser();
        Assert.notNull(loginUser, "未获取到登录用户");
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = loginUser.getRoles();
        // 权限集合
        Set<String> permissions = loginUser.getPermissions();
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("/getRouters")
    public AjaxResult getRouters() {
        String token = StpUtil.getTokenValue();
        List<SysMenu> menus = accountStrategy.getInstance(SystemConfig.getAccountSelectType()).getMenuTree(token);
        return AjaxResult.success(menuService.buildMenus(menus));
    }

    @GetMapping("/getMenu")
    public AjaxResult getMenu() {
        String token = StpUtil.getTokenValue();
        List<SysMenu> menuTree = accountStrategy.getMenuTree(token);
        return AjaxResult.success(menuTree);
    }
}
