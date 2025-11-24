package com.inspur.farmland.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.farmland.management.bean.entity.User;

/**
 * 用户Service接口
 * 
 * @author inspur
 */
public interface IUserService extends IService<User> {

    /**
     * 根据用户ID获取用户信息
     * 
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserById(Long userId);

    /**
     * 更新用户信息
     * 
     * @param user 用户信息
     * @return 是否成功
     */
    boolean updateUser(User user);

    /**
     * 修改密码
     * 
     * @param userId 用户ID
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean updatePassword(Long userId, String newPassword);
}