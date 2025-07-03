package com.inspur.ucif.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.domain.TreeSelect;
import com.inspur.common.core.domain.entity.SysDept;
import com.inspur.common.core.domain.entity.SysMenu;
import com.inspur.common.core.domain.entity.SysRole;
import com.inspur.common.core.domain.entity.SysUser;
import com.inspur.common.core.domain.model.LoginUser;
import com.inspur.common.utils.LoginHelper;
import com.inspur.system.service.ISysDeptService;
import com.inspur.system.service.ISysRoleService;
import com.inspur.system.service.ISysUserService;
import com.inspur.ucif.service.IAccountStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 系统内置的获取用户相关信息实现
 *
 * @author liyunlong
 * @date 2024/1/10
 */
@Service("igdpAccountStrategy")
@Slf4j
@ConditionalOnProperty(name = "sys.account-select-type", havingValue = "igdp")
public class IgdpAccountStrategy implements IAccountStrategy {
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysDeptService sysDeptService;
    @Resource
    private ISysRoleService sysRoleService;

    @Override
    public LoginUser getCurrentUser() {
        return LoginHelper.getLoginUser();
    }

    @Override
    public List<SysUser> searchUserList(SysUser queryParams) {
        queryParams.setStatus("0");
        queryParams.setAllowedShow("0");
        return sysUserService.selectUserList(queryParams);
    }

    @Override
    public AjaxResult searchUserPage(SysUser queryParams, Integer pageNum, Integer pageSize) {
        queryParams.setStatus("0");
        queryParams.setAllowedShow("0");
        IPage<SysUser> pageInfo = sysUserService.selectUserPage(queryParams, pageNum, pageSize);
        return AjaxResult.success(pageInfo);
    }

    @Override
    public SysUser getUserById(String userId) {
        return sysUserService.selectUserById(userId);
    }

    @Override
    public SysDept getDeptById(String deptId) {
        return sysDeptService.selectDeptById(deptId);
    }

    @Override
    public List<SysRole> searchRoleList(SysRole queryParams) {
        return sysRoleService.selectRoleList(queryParams);
    }

    @Override
    public AjaxResult searchRolePage(SysRole queryParams, Integer pageNum, Integer pageSize) {
        IPage<SysRole> rolePage = sysRoleService.getRolePage(queryParams, pageNum, pageSize);
        if (null != rolePage) {
            if (null != rolePage.getRecords() && !rolePage.getRecords().isEmpty()) {
                for (SysRole role : rolePage.getRecords()) {
                    role.setRoleCode(role.getRoleKey());
                }
            }
        }
        return AjaxResult.success(rolePage);
    }

    @Override
    public List<TreeSelect> getDeptTree(SysDept sysDept) {
        return sysDeptService.selectDeptTreeList(sysDept);
    }

    @Override
    public List<SysDept> getDeptList(SysDept dept) {
        return sysDeptService.selectListWithOutDataScope(dept);
    }

    @Override
    public List<TreeSelect> getDeptUserTreeList() {
        return sysDeptService.selectDeptUserTreeList();
    }

    @Override
    public List<SysMenu> getMenuTree(String token) {
        return Collections.emptyList();
    }
}
