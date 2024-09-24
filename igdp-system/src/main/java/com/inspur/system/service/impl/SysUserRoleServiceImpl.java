package com.inspur.system.service.impl;

import com.github.yulichang.base.MPJBaseServiceImpl;
import com.inspur.system.domain.SysUserRole;
import com.inspur.system.mapper.SysUserRoleMapper;
import com.inspur.system.service.ISysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liyunlong
 * @date 2024/1/9
 */
@Service
public class SysUserRoleServiceImpl extends MPJBaseServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {
    /**
     * 批量选择授权用户角色
     *
     * @param roleId 角色ID
     * @param userIds 需要授权的用户数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertAuthUsers(String roleId, String[] userIds)
    {
        // 新增用户与角色管理
        List<SysUserRole> list = new ArrayList<SysUserRole>();
        for (String userId : userIds)
        {
            SysUserRole ur = new SysUserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        return saveBatch(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertAuthRoles(String userId, String[] roleIds) {
        // 新增用户与角色管理
        List<SysUserRole> list = new ArrayList<SysUserRole>();
        for (String roleId : roleIds)
        {
            SysUserRole ur = new SysUserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        return saveBatch(list);
    }

    @Override
    public int deleteUserRoleByUserId(String userId) {
        return this.baseMapper.deleteUserRoleByUserId(userId);
    }

    @Override
    public int deleteUserRoleByUserIds(String[] userIds) {
        return this.baseMapper.deleteUserRoleByUserIds(userIds);
    }
}
