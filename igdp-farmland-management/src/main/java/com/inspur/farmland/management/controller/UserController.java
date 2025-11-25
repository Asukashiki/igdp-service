package com.inspur.farmland.management.controller;

import com.inspur.common.core.domain.AjaxResult;
import com.inspur.farmland.management.bean.entity.User;
import com.inspur.farmland.management.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户Controller
 * 
 * @author inspur
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 根据用户ID获取用户信息
     */
    @GetMapping("/{userId}")
    public AjaxResult getUserById(@PathVariable String userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return AjaxResult.error("用户不存在");
        }
        return AjaxResult.success(user);
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/update")
    public AjaxResult updateUser(@RequestBody User user) {
        boolean result = userService.updateUser(user);
        if (result) {
            return AjaxResult.success("更新用户信息成功", user);
        }
        return AjaxResult.error("更新用户信息失败");
    }

    /**
     * 修改密码
     */
    @PostMapping("/{userId}/password")
    public AjaxResult updatePassword(@PathVariable String userId, @RequestParam String newPassword) {
        boolean result = userService.updatePassword(userId, newPassword);
        if (result) {
            return AjaxResult.success("修改密码成功");
        }
        return AjaxResult.error("修改密码失败");
    }
}