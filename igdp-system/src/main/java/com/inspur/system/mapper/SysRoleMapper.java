package com.inspur.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.inspur.common.constant.Constants;
import com.inspur.common.core.domain.entity.SysDept;
import com.inspur.common.core.domain.entity.SysRole;
import com.inspur.common.core.domain.entity.SysUser;
import com.inspur.common.utils.StringUtils;
import com.inspur.system.domain.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.QueryMethod;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.inspur.common.utils.StringUtils.isNotEmpty;

/**
 * 角色表 数据层
 *
 * @author liyunlong
 */
@Mapper
public interface SysRoleMapper extends MPJBaseMapper<SysRole> {
    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    default List<SysRole> selectRoleList(SysRole role) {
        MPJLambdaWrapper<SysRole> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(SysRole.class, "t");
        wrapper.eq(SysRole::getDelFlag, Constants.DELETE_FLAG_VALID);
        if (StringUtils.isNotEmpty(role.getRoleId())) {
            wrapper.eq(SysRole::getRoleId, role.getRoleId());
        }
        if (StringUtils.isNotEmpty(role.getRoleCode())) {
            wrapper.eq(SysRole::getRoleCode, role.getRoleCode());
        }
        if (StringUtils.isNotEmpty(role.getRoleName())) {
            wrapper.like(SysRole::getRoleName, role.getRoleName());
        }
        if (StringUtils.isNotEmpty(role.getStatus())) {
            wrapper.eq(SysRole::getStatus, role.getStatus());
        }
        if (StringUtils.isNotEmpty(role.getRoleKey())) {
            wrapper.like(SysRole::getRoleKey, role.getRoleKey());
        }
        Map<String, Object> params = role.getParams();
        if (null != params) {
            LocalDateTime beginTime = role.getBeginTime();
            LocalDateTime endTime = role.getEndTime();
            String dataScope = null != params.get("dataScope") ? params.get("dataScope").toString().toLowerCase() : "";
            if (null != beginTime) {
                wrapper.ge(SysRole::getCreateTime, beginTime);
            }
            if (null != endTime) {
                wrapper.le(SysRole::getCreateTime, endTime);
            }
            if (isNotEmpty(dataScope)) {
                if (dataScope.startsWith(" and")) {
                    dataScope = dataScope.replace(" and", "");
                }
                wrapper.leftJoin(SysUserRole.class, "ur", SysUserRole::getRoleId, SysRole::getRoleId);
                wrapper.leftJoin(SysUser.class, "u", SysUser::getUserId, SysUserRole::getUserId);
                wrapper.leftJoin(SysDept.class, "d", SysDept::getDeptId, SysUser::getDeptId);
                wrapper.apply(dataScope);
            }
        }
        wrapper.orderByAsc(SysRole::getRoleSort);
        return selectJoinList(SysRole.class, wrapper);
    }

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    default List<SysRole> getRoleListByUserId(String userId) {
        MPJLambdaWrapper<SysRole> queryWrapper = new MPJLambdaWrapper<SysRole>()
                .selectAll(SysRole.class)
                .leftJoin(SysUserRole.class, SysUserRole::getRoleId, SysRole::getRoleId)
                .eq(SysUserRole::getUserId, userId);
        return selectJoinList(SysRole.class, queryWrapper);
    }

    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    default List<String> selectRoleListByUserId(String userId) {
        MPJLambdaWrapper<SysRole> queryWrapper = new MPJLambdaWrapper<>();
        queryWrapper.select(SysRole::getRoleId);
        queryWrapper.leftJoin(SysUserRole.class, SysUserRole::getRoleId, SysRole::getRoleId);
        queryWrapper.eq(SysUserRole::getUserId, userId);
        return selectJoinList(String.class, queryWrapper);
    }

}
