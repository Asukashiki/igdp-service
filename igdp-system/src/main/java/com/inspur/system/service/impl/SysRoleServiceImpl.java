package com.inspur.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.inspur.common.annotation.DataScope;
import com.inspur.common.constant.Constants;
import com.inspur.common.constant.UserConstants;
import com.inspur.common.core.domain.entity.SysRole;
import com.inspur.common.core.domain.entity.SysUser;
import com.inspur.common.exception.ServiceException;

import com.inspur.common.utils.LoginHelper;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.spring.SpringUtils;
import com.inspur.system.domain.SysRoleDept;
import com.inspur.system.domain.SysRoleMenu;
import com.inspur.system.domain.SysUserRole;
import com.inspur.system.mapper.SysRoleDeptMapper;
import com.inspur.system.mapper.SysRoleMapper;
import com.inspur.system.mapper.SysRoleMenuMapper;
import com.inspur.system.mapper.SysUserRoleMapper;
import com.inspur.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 角色 业务层处理
 *
 * @author liyunlong
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends MPJBaseServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysRoleDeptMapper roleDeptMapper;

    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysRole> selectRoleList(SysRole role) {
        return this.baseMapper.selectRoleList(role);
    }

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectAllRolesByUserId(String userId) {
        List<SysRole> userRoles = this.baseMapper.getRoleListByUserId(userId);
        List<SysRole> roles = selectRoleAll();
        for (SysRole role : roles) {
            for (SysRole userRole : userRoles) {
                if (role.getRoleId().equals(userRole.getRoleId())) {
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;
    }


    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(String userId) {
        List<SysRole> perms = this.baseMapper.getRoleListByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms) {
            if (StringUtils.isNotNull(perm)) {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectRoleAll() {
        return SpringUtils.getAopProxy(this).selectRoleList(new SysRole());
    }

    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    @Override
    public List<String> selectRoleListByUserId(String userId) {
        return this.baseMapper.selectRoleListByUserId(userId);
    }

    @Override
    public IPage<SysRole> getRolePage(SysRole role, Integer pageNum, Integer pageSize) {

        IPage<SysRole> page = new Page<>(pageNum, pageSize);
        return page(page, new LambdaQueryWrapper<SysRole>()
                .like(StrUtil.isNotEmpty(role.getRoleName()), SysRole::getRoleName, role.getRoleName())
                .eq(StrUtil.isNotEmpty(role.getRoleName()), SysRole::getRoleId, role.getRoleName())
                .eq(SysRole::getDelFlag, Constants.DELETE_FLAG_VALID)
                .like(StrUtil.isNotEmpty(role.getRoleKey()), SysRole::getRoleKey, role.getRoleKey()));
    }

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    @Override
    public SysRole selectRoleById(String roleId) {
        return getById(roleId);
    }

    @Override
    public void handleRoleList(List<SysRole> roleList) {
        if (null != roleList && !roleList.isEmpty()) {
            for (SysRole role : roleList) {
                if (StringUtils.isNotEmpty(role.getRoleId())) {
                    SysRole currentRole = getById(role.getRoleId());
                    if (null == currentRole) {
                        role.setCreateTime(LocalDateTime.now());
                        save(role);
                    }
                }
            }
        }
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean checkRoleNameUnique(SysRole role) {
        String roleId = StringUtils.isNull(role.getRoleId()) ? "-1" : role.getRoleId();
        SysRole info = getOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleName, role.getRoleName())
                .eq(SysRole::getDelFlag, Constants.DELETE_FLAG_VALID));
        if (StringUtils.isNotNull(info) && !info.getRoleId().equals(roleId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean checkRoleKeyUnique(SysRole role) {
        String roleId = StringUtils.isNull(role.getRoleId()) ? "-1" : role.getRoleId();
        SysRole info = getOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleKey, role.getRoleKey())
                .eq(SysRole::getDelFlag, Constants.DELETE_FLAG_VALID));
        if (StringUtils.isNotNull(info) && !info.getRoleId().equals(roleId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    @Override
    public void checkRoleAllowed(SysRole role) {
        if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员角色");
        }
    }

    /**
     * 校验角色是否有数据权限
     *
     * @param roleId 角色id
     */
    @Override
    public void checkRoleDataScope(String roleId) {
        if (!SysUser.isAdmin(LoginHelper.getUserId())) {
            SysRole role = new SysRole();
            role.setRoleId(roleId);
            List<SysRole> roles = SpringUtils.getAopProxy(this).selectRoleList(role);
            if (StringUtils.isEmpty(roles)) {
                throw new ServiceException("没有权限访问角色数据！");
            }
        }
    }

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public long countUserRoleByRoleId(String roleId) {
        return userRoleMapper.countUserRoleByRoleId(roleId);
    }

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertRole(SysRole role) {
        // 新增角色信息
        role.setCreateTime(LocalDateTime.now());
        save(role);
        return insertRoleMenu(role);
    }

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRole(SysRole role) {
        // 修改角色信息
        role.setUpdateTime(LocalDateTime.now());
        updateById(role);
        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
        return insertRoleMenu(role);
    }

    /**
     * 修改角色状态
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean updateRoleStatus(SysRole role) {
        role.setUpdateTime(LocalDateTime.now());
        return updateById(role);
    }

    /**
     * 修改数据权限信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int authDataScope(SysRole role) {
        // 修改角色信息
        role.setUpdateTime(LocalDateTime.now());
        updateById(role);
        // 删除角色与部门关联
        roleDeptMapper.deleteRoleDeptByRoleId(role.getRoleId());
        // 新增角色和部门信息（数据权限）
        return insertRoleDept(role);
    }

    /**
     * 新增角色菜单信息
     *
     * @param role 角色对象
     */
    public int insertRoleMenu(SysRole role) {
        int rows = 1;
        // 新增用户与角色管理
        List<SysRoleMenu> list = new ArrayList<SysRoleMenu>();
        for (String menuId : role.getMenuIds()) {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (!list.isEmpty()) {
            rows = roleMenuMapper.batchRoleMenu(list);
        }
        return rows;
    }

    /**
     * 新增角色部门信息(数据权限)
     *
     * @param role 角色对象
     */
    public int insertRoleDept(SysRole role) {
        int rows = 1;
        // 新增角色与部门（数据权限）管理
        List<SysRoleDept> list = new ArrayList<SysRoleDept>();
        for (String deptId : role.getDeptIds()) {
            SysRoleDept rd = new SysRoleDept();
            rd.setRoleId(role.getRoleId());
            rd.setDeptId(deptId);
            list.add(rd);
        }
        if (!list.isEmpty()) {
            rows = roleDeptMapper.batchRoleDept(list);
        }
        return rows;
    }

    /**
     * 通过角色ID删除角色
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRoleById(String roleId) {
        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenuByRoleId(roleId);
        // 删除角色与部门关联
        roleDeptMapper.deleteRoleDeptByRoleId(roleId);
        SysRole updateEntity = new SysRole();
        updateEntity.setDelFlag(Constants.DELETE_FLAG_INVALID);
        updateEntity.setRoleId(roleId);
        updateEntity.setUpdateTime(LocalDateTime.now());
        return updateById(updateEntity);
    }

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRoleByIds(String[] roleIds) {
        if (null != roleIds && roleIds.length > 0) {
            for (String roleId : roleIds) {
                checkRoleAllowed(new SysRole(roleId));
                checkRoleDataScope(roleId);
                SysRole role = selectRoleById(roleId);
                if (countUserRoleByRoleId(roleId) > 0) {
                    throw new ServiceException(String.format("%1$s已分配,不能删除", role.getRoleName()));
                }
            }
            // 删除角色与菜单关联
            roleMenuMapper.deleteByRoleIds(roleIds);
            // 删除角色与部门关联
            roleDeptMapper.deleteRoleDeptByRoleIds(roleIds);
            SysRole updateEntity = new SysRole();
            updateEntity.setDelFlag(Constants.DELETE_FLAG_INVALID);
            updateEntity.setUpdateTime(LocalDateTime.now());
            return update(updateEntity, new LambdaQueryWrapper<SysRole>()
                    .in(SysRole::getRoleId, Arrays.asList(roleIds)));
        }
        return false;
    }

    /**
     * 取消授权用户角色
     *
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    @Override
    public int deleteAuthUser(SysUserRole userRole) {
        return userRoleMapper.deleteUserRoleInfo(userRole);
    }

    /**
     * 批量取消授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要取消授权的用户数据ID
     * @return 结果
     */
    @Override
    public int deleteAuthUsers(String roleId, String[] userIds) {
        return userRoleMapper.deleteUserRoleInfos(roleId, userIds);
    }

}
