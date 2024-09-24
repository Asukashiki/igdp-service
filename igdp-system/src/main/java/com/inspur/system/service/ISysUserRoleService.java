package com.inspur.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.yulichang.base.MPJBaseService;
import com.inspur.system.domain.SysUserRole;

/**
 * @author liyunlong
 * @date 2024/1/9
 */
public interface ISysUserRoleService extends MPJBaseService<SysUserRole> {
    /**
     * 批量选择授权用户角色
     *
     * @param roleId 角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    boolean insertAuthUsers(String roleId, String[] userIds);

    /**
     * 批量选择授权用户角色
     *
     * @param userId 用户ID
     * @param roleIds 角色ID集合
     * @return 结果
     */
    boolean insertAuthRoles(String userId,String[] roleIds);



    /**
     * 根据userId删除
     * @param userId userId
     * @return 数量
     * */
    int deleteUserRoleByUserId(String userId);

    /**
     * 根据userIds删除
     * @param userIds userId
     * @return 数量
     * */
    int deleteUserRoleByUserIds(String[] userIds);
}
