package com.inspur.ucif.service;

import com.github.pagehelper.Page;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.domain.TreeSelect;
import com.inspur.common.core.domain.entity.SysDept;
import com.inspur.common.core.domain.entity.SysMenu;
import com.inspur.common.core.domain.entity.SysRole;
import com.inspur.common.core.domain.entity.SysUser;
import com.inspur.common.core.domain.model.LoginUser;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.spring.SpringUtils;

import java.util.List;

/**
 * @author liyunlong
 * @date 2024/1/10
 */
public interface IAccountStrategy {
    String BASE_NAME = "AccountStrategy";

    /**
     * 根据类型获取授权实现类
     * @param grantType 类型
     * @return IAuthStrategy
     * */
    default IAccountStrategy getInstance(String grantType){
        String beanName = grantType + BASE_NAME;
        if (!SpringUtils.containsBean(beanName)) {
            throw new ServiceException("授权类型不正确!");
        }
        return SpringUtils.getBean(beanName);
    }

    /**
     * 获取当前登录用户信息
     * @return LoginUser登录用户信息
     * */
    LoginUser getCurrentUser();

    /**
     * 获取用户信息列表
     * @param queryParams 查询条件
     * @return 用户列表集合
     * */
    List<SysUser> searchUserList(SysUser queryParams);

    /**
     * 查询用户分页列表
     * @param queryParams 查询条件
     * @param pageNum 分页页码
     * @param pageSize 分页大小
     * @return 结果：分页数据
     * */
    AjaxResult searchUserPage(SysUser queryParams,Integer pageNum,Integer pageSize);

    /**
     * 根据userId获取用户信息
     * @param userId 用户id
     * @return 用户信息
     * */
    SysUser getUserById(String userId);

    /**
     * 根据部门id获取部门信息
     * @param deptId 部门id
     * @return SysDept部门信息
     * */
    SysDept getDeptById(String deptId);

    /**
     * 获取角色列表
     * @param queryParams 查询条件
     * @return 角色列表
     * */
    List<SysRole> searchRoleList(SysRole queryParams);

    /**
     * 获取角色分页列表
     * @param queryParams 查询条件
     * @param pageNum 页码
     * @param pageSize 分页大小
     * @return 结果
     * */
    AjaxResult searchRolePage(SysRole queryParams,Integer pageNum,Integer pageSize);

    /**
     * 获取组织树
     * @param sysDept 部门查询条件
     * @return 树结构的组织信息集合
     * */
    List<TreeSelect> getDeptTree(SysDept sysDept);

    /**
     * 获取组织列表
     * @param dept 查询条件
     * @return 组织部门集合
     * */
    List<SysDept> getDeptList(SysDept dept);

    /**
     * 查询部门以及用户树结构信息
     * @return 部门树信息集合，包含用户在内
     * */
    List<TreeSelect> getDeptUserTreeList();

    /**
     * 获取菜单树结构信息
     * @return
     */
    List<SysMenu> getMenuTree(String token);

}
