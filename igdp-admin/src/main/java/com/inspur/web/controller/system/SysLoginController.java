package com.inspur.web.controller.system;

import java.util.List;
import java.util.Set;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.inspur.common.config.SystemConfig;
import com.inspur.common.core.domain.model.LoginUser;
import com.inspur.framework.manager.AsyncManager;
import com.inspur.framework.manager.factory.AsyncFactory;
import com.inspur.system.service.ISysRoleWorkbenchItemService;
import com.inspur.system.service.ISysWorkbenchItemService;
import com.inspur.ucif.service.IAccountStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.inspur.common.constant.Constants;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.domain.entity.SysMenu;
import com.inspur.common.core.domain.entity.SysUser;
import com.inspur.common.core.domain.model.LoginBody;
import com.inspur.common.utils.LoginHelper;
import com.inspur.framework.web.service.SysLoginService;
import com.inspur.framework.web.service.SysPermissionService;
import com.inspur.system.service.ISysMenuService;

import javax.annotation.Resource;

/**
 * 登录验证
 *
 * @author liyunlong
 */
@RestController
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Resource
    private ISysWorkbenchItemService workbenchItemService;
    @Resource
    private IAccountStrategy accountStrategy;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

//    /**
//     * 获取用户信息
//     *
//     * @return 用户信息
//     */
//    @GetMapping("getInfo")
//    public AjaxResult getInfo() {
//        SysUser user = LoginHelper.getLoginUser().getUser();
//        // 角色集合
//        Set<String> roles = permissionService.getRolePermission(user);
//        // 权限集合
//        Set<String> permissions = permissionService.getMenuPermission(user);
//        AjaxResult ajax = AjaxResult.success();
//        ajax.put("user", user);
//        ajax.put("roles", roles);
//        ajax.put("permissions", permissions);
//        return ajax;
//    }
//
//    /**
//     * 获取路由信息
//     *
//     * @return 路由信息
//     */
//    @GetMapping("getRouters")
//    public AjaxResult getRouters() {
//        String userId = LoginHelper.getUserId();
//        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
//        return AjaxResult.success(menuService.buildMenus(menus));
//    }
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
        roles.add("superAdmin");
        roles.add("admin");
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


    /**
     * 获取工作台内容
     */
    @GetMapping("/getWorkbenchItem")
    public AjaxResult getWorkbenchItem() {
        String userId = LoginHelper.getUserId();
        return AjaxResult.success(workbenchItemService.getItemListByUserId(userId));
    }

    @PostMapping("logout")
    public AjaxResult logout() {
        Object loginId = StpUtil.getLoginIdDefaultNull();
        if (ObjectUtil.isNull(loginId)) {
            return AjaxResult.success();
        }
        String username = LoginHelper.getUsername();
        String userId = LoginHelper.getUserId();
        StpUtil.logout();
        // 记录用户退出日志
        AsyncManager.me().execute(AsyncFactory.recordLoginInfo(userId, username, Constants.LOGOUT, "退出成功"));
        return AjaxResult.success();
    }
}
