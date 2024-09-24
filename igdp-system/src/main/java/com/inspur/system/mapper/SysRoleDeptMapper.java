package com.inspur.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.system.domain.SysRoleDept;

import java.util.Arrays;
import java.util.List;

/**
 * 角色与部门关联表 数据层
 *
 * @author liyunlong
 */
public interface SysRoleDeptMapper extends BaseMapper<SysRoleDept> {
    /**
     * 通过角色ID删除角色和部门关联
     *
     * @param roleId 角色ID
     */
    default void deleteRoleDeptByRoleId(String roleId) {
        delete(new LambdaQueryWrapper<SysRoleDept>().eq(SysRoleDept::getRoleId, roleId));
    }

    /**
     * 批量删除角色部门关联信息
     *
     * @param roleIds 需要删除的数据ID
     */
    default void deleteRoleDeptByRoleIds(String[] roleIds) {
        if (null != roleIds && roleIds.length > 0) {
            delete(new LambdaQueryWrapper<SysRoleDept>().in(SysRoleDept::getRoleId, Arrays.asList(roleIds)));
        }
    }

    /**
     * 查询部门使用数量
     *
     * @param deptId 部门ID
     * @return 结果
     */
    default long selectCountRoleDeptByDeptId(String deptId) {
        return selectCount(new LambdaQueryWrapper<SysRoleDept>().eq(SysRoleDept::getDeptId, deptId));
    }

    /**
     * 批量新增角色部门信息
     *
     * @param roleDeptList 角色部门列表
     * @return 结果
     */
    public int batchRoleDept(List<SysRoleDept> roleDeptList);
}
