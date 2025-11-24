package com.inspur.farmland.management.controller;

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
    public User getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/update")
    public boolean updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    /**
     * 修改密码
     */
    @PostMapping("/{userId}/password")
    public boolean updatePassword(@PathVariable Long userId, @RequestParam String newPassword) {
        return userService.updatePassword(userId, newPassword);
    }
}