package com.inspur.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.inspur.common.constant.Constants;
import com.inspur.common.core.domain.entity.SysDept;
import com.inspur.common.core.domain.entity.SysMenu;
import com.inspur.common.core.domain.entity.SysRole;
import com.inspur.common.core.domain.entity.SysUser;
import com.inspur.common.utils.StringUtils;
import com.inspur.system.domain.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.inspur.common.utils.StringUtils.isNotEmpty;

/**
 * 用户表 数据层
 *
 * @author liyunlong
 */
@Mapper
public interface SysUserMapper extends MPJBaseMapper<SysUser> {

    /**
     * 查询所有用户信息
     * 未删除的、正常使用的，有所属部门的
     *
     * @return 用户集合
     */
    default List<SysUser> selectAllValidUser() {
        return selectList(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getDelFlag, "0")
                .eq(SysUser::getStatus, "0")
                .isNotNull(SysUser::getDeptId)
                .orderByAsc(SysUser::getSortNumber));
    }

    /**
     * 根据条件分页查询已配用户角色列表
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
     * 查询分页信息
     *
     * @param page        分页
     * @param queryParams 查询条件
     * @return 分页结果
     */
    IPage<SysUser> selectUserPageByParams(IPage<SysUser> page, @Param("queryParams") SysUser queryParams);


    /**
     * 修改用户头像
     *
     * @param userId 用户ID
     * @param avatar 头像地址
     * @return 结果
     */
    default int updateUserAvatar(String userId, String avatar) {
        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SysUser::getAvatar, avatar);
        updateWrapper.eq(SysUser::getUserId, userId);
        return update(updateWrapper);
    }

    /**
     * 重置用户密码
     *
     * @param userId   用户ID
     * @param password 密码
     * @return 结果
     */
    default int resetUserPwd(@Param("userId") String userId, @Param("password") String password) {
        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SysUser::getPassword, password);
        updateWrapper.set(SysUser::getUpdateTime, LocalDateTime.now());
        updateWrapper.eq(SysUser::getUserId, userId);
        return update(updateWrapper);
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    default int deleteUserById(String userId) {
        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SysUser::getDelFlag, Constants.DELETE_FLAG_INVALID);
        updateWrapper.eq(SysUser::getUserId, userId);
        return update(updateWrapper);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    default int deleteUserByIds(String[] userIds) {
        if (null != userIds && userIds.length > 0) {
            LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(SysUser::getDelFlag, Constants.DELETE_FLAG_INVALID);
            updateWrapper.in(SysUser::getUserId, Arrays.asList(userIds));
            return update(updateWrapper);
        }
        return 0;
    }
}
