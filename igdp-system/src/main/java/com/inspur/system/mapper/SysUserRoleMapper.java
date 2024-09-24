package com.inspur.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.inspur.system.domain.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.Arrays;

/**
 * 用户与角色关联表 数据层
 *
 * @author liyunlong
 */
@Mapper
public interface SysUserRoleMapper extends MPJBaseMapper<SysUserRole> {
    /**
     * 通过用户ID删除用户和角色关联
     *
     * @param userId 用户ID
     * @return 结果
     */
    default int deleteUserRoleByUserId(String userId) {
        return delete(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId));
    }

    /**
     * 批量删除用户和角色关联
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    default int deleteUserRoleByUserIds(String[] userIds) {
        if (null != userIds && userIds.length > 0) {
            return delete(new LambdaQueryWrapper<SysUserRole>()
                    .in(SysUserRole::getUserId, Arrays.asList(userIds)));
        }
        return 0;
    }

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    default Long countUserRoleByRoleId(String roleId){
        return selectCount(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getRoleId,roleId));
    }

    /**
     * 删除用户和角色关联信息
     *
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    default int deleteUserRoleInfo(SysUserRole userRole) {
        return delete(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userRole.getUserId())
                .eq(SysUserRole::getRoleId, userRole.getRoleId()));
    }

    /**
     * 批量取消授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    default int deleteUserRoleInfos(String roleId, String[] userIds) {
        return delete(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getRoleId, roleId)
                .in(SysUserRole::getUserId, Arrays.asList(userIds)));
    }
}
