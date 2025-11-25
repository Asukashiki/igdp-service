package com.inspur.farmland.management.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.farmland.management.bean.entity.User;
import com.inspur.farmland.management.mapper.UserMapper;
import com.inspur.farmland.management.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * 用户Service实现类
 * 
 * @author inspur
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public User getUserById(String userId) {
        return this.getById(userId);
    }

    @Override
    public boolean updateUser(User user) {
        return this.updateById(user);
    }

    @Override
    public boolean updatePassword(String userId, String newPassword) {
        User user = this.getById(userId);
        if (user != null) {
            user.setPassword(newPassword);
            return this.updateById(user);
        }
        return false;
    }
}