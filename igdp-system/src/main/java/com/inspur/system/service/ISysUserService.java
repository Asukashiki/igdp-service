package com.inspur.system.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.github.yulichang.base.MPJBaseService;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.domain.entity.SysRole;
import com.inspur.common.core.domain.entity.SysUser;
import com.inspur.common.core.domain.model.LoginUser;


/**
 * 用户 业务层
 *
 * @author liyunlong
 */
public interface ISysUserService extends MPJBaseService<SysUser> {

    /**
     * 根据查询条件查询列表
     *
     * @param user 查询条件
     * @return 集合
     */
    List<SysUser> selectUserList(SysUser user);

    /**
     * 分页查询用户列表
     * @param sysUser 查询条件
     * @param pageNum 当前分页
     * @param limit limit
     * @return 分页列表
     * */
    IPage<SysUser> selectUserPage(SysUser sysUser, Integer pageNum, Integer limit);


    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    List<SysUser> selectAllocatedList(SysUser user);

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    List<SysUser> selectUnallocatedList(SysUser user);

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    SysUser selectUserByUserName(String userName);

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    SysUser selectUserById(String userId);

    /**
     * 根据用户ID查询用户所属角色组
     *
     * @param userId 用户id
     * @return 结果
     */
    String selectUserRoleGroup(String userId);

    /**
     * 根据用户ID查询用户所属岗位组
     *
     * @param userId 用户ID
     * @return 结果
     */
    String selectUserPostGroup(String userId);

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    boolean checkUserNameUnique(SysUser user);

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    boolean checkPhoneUnique(SysUser user);

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    boolean checkEmailUnique(SysUser user);

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    void checkUserAllowed(SysUser user);

    /**
     * 校验用户是否有数据权限
     *
     * @param userId 用户id
     */
    void checkUserDataScope(String userId);

    /**
     * 新增用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    boolean insertUser(SysUser user);

    /**
     * 注册用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    boolean registerUser(SysUser user);

    /**
     * 根据登录信息注册用户
     * 用于三方授权，本地没有相关用户信息的情况
     *
     * @param loginUser 登录用户信息
     * @return 结果
     */
    SysUser registerByLoginUser(LoginUser loginUser);

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    boolean updateUser(SysUser user);

    /**
     * 用户授权角色
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    void insertUserAuth(String userId, String[] roleIds);

    /**
     * 修改用户状态
     *
     * @param userId 用户ID
     * @param status 目标状态
     * @return 结果
     */
    boolean updateUserStatus(String userId, String status);

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    boolean updateUserProfile(SysUser user);

    /**
     * 修改用户头像
     *
     * @param userId 用户ID
     * @param avatar 头像地址
     * @return 结果
     */
    boolean updateUserAvatar(String userId, String avatar);

    /**
     * 重置用户密码
     *
     * @param userId   用户id
     * @param password 密码
     * @return 结果
     */
    AjaxResult changeUserPwd(String userId, String password);

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    int deleteUserById(String userId);

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    int deleteUserByIds(String[] userIds);

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName);
}
