package com.inspur.system.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.inspur.common.annotation.DataScope;
import com.inspur.common.constant.Constants;
import com.inspur.common.constant.UserConstants;
import com.inspur.common.core.domain.TreeSelect;
import com.inspur.common.core.domain.entity.SysDept;
import com.inspur.common.core.domain.entity.SysRole;
import com.inspur.common.core.domain.entity.SysUser;
import com.inspur.common.core.domain.model.DeptUserTreeBody;
import com.inspur.common.core.text.Convert;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.LoginHelper;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.spring.SpringUtils;
import com.inspur.system.mapper.SysDeptMapper;
import com.inspur.system.mapper.SysRoleMapper;
import com.inspur.system.mapper.SysUserMapper;
import com.inspur.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.inspur.common.utils.StringUtils.isNotEmpty;

/**
 * 部门管理 服务实现
 *
 * @author liyunlong
 */
@Service("sysDeptService")
public class SysDeptServiceImpl extends MPJBaseServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {
    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysUserMapper userMapper;

    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    @DataScope(deptAlias = "t")
    public List<SysDept> selectDeptList(SysDept dept) {

        MPJLambdaWrapper<SysDept> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(SysDept.class,"t");
        wrapper.eq(SysDept::getDelFlag, Constants.DELETE_FLAG_VALID);
        if (StringUtils.isNotEmpty(dept.getDeptId())) {
            wrapper.eq(SysDept::getDeptId, dept.getDeptId());
        }
        if (StringUtils.isNotEmpty(dept.getParentId())) {
            wrapper.eq(SysDept::getParentId, dept.getParentId());
        }
        if (StringUtils.isNotEmpty(dept.getDeptName())) {
            wrapper.like(SysDept::getDeptName, dept.getDeptName());
        }
        if (StringUtils.isNotEmpty(dept.getStatus())) {
            wrapper.eq(SysDept::getStatus, dept.getStatus());
        }
        Map<String, Object> params = dept.getParams();
        if (null != params) {
            String dataScope = null != params.get("dataScope") ? params.get("dataScope").toString().toLowerCase() : "";
            if (isNotEmpty(dataScope)) {
                if (dataScope.startsWith(" and")) {
                    dataScope = dataScope.replace(" and", "");
                }
                wrapper.apply(dataScope);
            }
        }
        wrapper.orderByAsc(SysDept::getParentId, SysDept::getOrderNum);

        return list(wrapper);
    }

    @Override
    public List<SysDept> selectListWithOutDataScope(SysDept dept) {
        MPJLambdaWrapper<SysDept> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(SysDept.class);
        wrapper.eq(SysDept::getDelFlag, Constants.DELETE_FLAG_VALID);
        if (StringUtils.isNotEmpty(dept.getDeptId())) {
            wrapper.eq(SysDept::getDeptId, dept.getDeptId());
        }
        if (StringUtils.isNotEmpty(dept.getParentId())) {
            wrapper.eq(SysDept::getParentId, dept.getParentId());
        }
        if (StringUtils.isNotEmpty(dept.getDeptName())) {
            wrapper.like(SysDept::getDeptName, dept.getDeptName());
        }
        if (StringUtils.isNotEmpty(dept.getStatus())) {
            wrapper.eq(SysDept::getStatus, dept.getStatus());
        }
        wrapper.orderByAsc(SysDept::getParentId, SysDept::getOrderNum);

        return list(wrapper);
    }

    /**
     * 查询部门树结构信息
     *
     * @param dept 部门信息
     * @return 部门树信息集合
     */
    @Override
    public List<TreeSelect> selectDeptTreeList(SysDept dept) {
        List<SysDept> deptList = SpringUtils.getAopProxy(this).selectDeptList(dept);
        return buildDeptTreeSelect(deptList);
    }

    @Override
    public List<TreeSelect> selectDeptUserTreeList() {
        List<SysDept> deptList = list(new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getDelFlag, "0")
                .orderByAsc(SysDept::getOrderNum));
        List<SysUser> userList = userMapper.selectAllValidUser();
        List<DeptUserTreeBody> treeBodyList = new ArrayList<>();
        if (null != deptList && !deptList.isEmpty()) {
            for (SysDept dept : deptList) {
                DeptUserTreeBody treeBody = new DeptUserTreeBody();
                treeBody.setId(dept.getDeptId());
                treeBody.setParentId(dept.getParentId());
                treeBody.setName(dept.getDeptName());
                treeBody.setPhoneNumber(dept.getPhone());
                treeBody.setType("dept");
                treeBodyList.add(treeBody);
            }
        }

        if (null != userList && !userList.isEmpty()) {
            for (SysUser user : userList) {
                DeptUserTreeBody treeBody = new DeptUserTreeBody();
                treeBody.setId(user.getUserId());
                treeBody.setParentId(user.getDeptId());
                treeBody.setName(user.getNickName());
                treeBody.setPhoneNumber(user.getPhoneNumber());
                treeBody.setType("user");
                treeBodyList.add(treeBody);
            }
        }
        return buildDeptUserTreeSelect(treeBodyList);
    }

    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    private List<SysDept> buildDeptTree(List<SysDept> depts) {
        List<SysDept> returnList = new ArrayList<>();
        List<String> tempList = depts.stream().map(SysDept::getDeptId).collect(Collectors.toList());
        for (SysDept dept : depts) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts) {
        List<SysDept> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param treeBodyList 列表
     * @return 下拉树结构列表
     */
    private List<TreeSelect> buildDeptUserTreeSelect(List<DeptUserTreeBody> treeBodyList) {
        List<DeptUserTreeBody> deptTrees = buildDeptUserTree(treeBodyList);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    private List<DeptUserTreeBody> buildDeptUserTree(List<DeptUserTreeBody> treeBodyList) {
        List<DeptUserTreeBody> returnList = new ArrayList<>();
        List<String> tempList = treeBodyList.stream().map(DeptUserTreeBody::getId).collect(Collectors.toList());
        for (DeptUserTreeBody treeBody : treeBodyList) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(treeBody.getParentId())) {
                recursionFn(treeBodyList, treeBody);
                returnList.add(treeBody);
            }
        }
        if (returnList.isEmpty()) {
            returnList = treeBodyList;
        }
        return returnList;
    }

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    @Override
    public List<String> selectDeptListByRoleId(String roleId) {
        SysRole role = roleMapper.selectById(roleId);
        return deptMapper.selectDeptListByRoleId(roleId, role.isDeptCheckStrictly());
    }

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Override
    public SysDept selectDeptById(String deptId) {
        return deptMapper.selectDeptById(deptId);
    }

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Override
    public int selectNormalChildrenDeptById(String deptId) {
        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(SysDept::getDelFlag,"0");
        queryWrapper.eq(SysDept::getParentId,deptId);
        queryWrapper.last("limit 1");
        long count = count(queryWrapper);
        return (int)count;
    }

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public boolean hasChildByDeptId(String deptId) {
        int result = deptMapper.hasChildByDeptId(deptId);
        return result > 0;
    }

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(String deptId) {
        int result = deptMapper.checkDeptExistUser(deptId);
        return result > 0;
    }

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public boolean checkDeptNameUnique(SysDept dept) {
        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        String deptId = StringUtils.isNull(dept.getDeptId()) ? "-1" : dept.getDeptId();
        queryWrapper.eq(SysDept::getDeptName,dept.getDeptName());
        queryWrapper.eq(SysDept::getParentId,dept.getParentId());
        queryWrapper.eq(SysDept::getDelFlag,"0");
        queryWrapper.last("limit 1");
        //SysDept info = deptMapper.checkDeptNameUnique(dept.getDeptName(), dept.getParentId());
        List<SysDept> info = list(queryWrapper);
        if (StringUtils.isNotNull(info) && info.size()>0) {
            if (!info.get(0).getDeptId().equals(deptId)){
                return UserConstants.NOT_UNIQUE;
            }
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验部门是否有数据权限
     *
     * @param deptId 部门id
     */
    @Override
    public void checkDeptDataScope(String deptId) {
        if (!SysUser.isAdmin(LoginHelper.getUserId())) {
            SysDept dept = new SysDept();
            dept.setDeptId(deptId);
            List<SysDept> depts = SpringUtils.getAopProxy(this).selectDeptList(dept);
            if (StringUtils.isEmpty(depts)) {
                throw new ServiceException("没有权限访问部门数据！");
            }
        }
    }

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int insertDept(SysDept dept) {
        SysDept parentInfo = deptMapper.selectDeptById(dept.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(parentInfo.getStatus())) {
            throw new ServiceException("部门停用，不允许新增");
        }
        dept.setDeptId(IdUtil.fastSimpleUUID());
        dept.setAncestors(parentInfo.getAncestors() + "/" + dept.getParentId() + "/" + dept.getDeptId());
        dept.setCreateTime(LocalDateTime.now());
        save(dept);
        //return deptMapper.insertDept(dept);
        return 1;
    }

    /**
     * 批量新增保存部门信息
     */
    @Override
    public boolean saveDept(SysDept dept) {

        // 校验是否存在，如果存在则进行更新
        saveOrUpdate(dept);
        return true; 
    }


    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int updateDept(SysDept dept) {
        SysDept newParentDept = deptMapper.selectDeptById(dept.getParentId());
        SysDept oldDept = deptMapper.selectDeptById(dept.getDeptId());
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept)) {
            String newAncestors = newParentDept.getAncestors() + "/" + newParentDept.getDeptId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);
        }
        int result;
        Boolean b = updateById(dept);
        if (b=true) {
            result = 1;
        }else {
            result = 0;
        }

        //int result = deptMapper.updateDept(dept);
        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus()) && StringUtils.isNotEmpty(dept.getAncestors())
                && !StringUtils.equals("0", dept.getAncestors())) {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatusNormal(dept);
        }
        return result;
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private void updateParentDeptStatusNormal(SysDept dept) {
        String ancestors = dept.getAncestors();
        String[] deptIds = Convert.toStrArray(ancestors);
        deptMapper.updateDeptStatusNormal(deptIds);
    }

    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(String deptId, String newAncestors, String oldAncestors) {
        LambdaQueryWrapper<SysDept>queryWrapper =new LambdaQueryWrapper();
        List<SysDept> children = deptMapper.selectChildrenDeptById(deptId);
        for (SysDept child : children) {
            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
        }
        if (!children.isEmpty()) {
            deptMapper.updateDeptChildren(children);
        }
    }

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public int deleteDeptById(String deptId) {
        return deptMapper.deleteDeptById(deptId);
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysDept> list, SysDept t) {
        // 得到子节点列表
        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDept tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<DeptUserTreeBody> list, DeptUserTreeBody t) {
        // 得到子节点列表
        List<DeptUserTreeBody> childList = getChildListForDeptUser(list, t);
        t.setChildren(childList);
        for (DeptUserTreeBody tChild : childList) {
            if (hasChildForDeptUser(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysDept> getChildList(List<SysDept> list, SysDept t) {
        List<SysDept> tlist = new ArrayList<>();
        for (SysDept n : list) {
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().equals(t.getDeptId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 得到子节点列表
     */
    private List<DeptUserTreeBody> getChildListForDeptUser(List<DeptUserTreeBody> list, DeptUserTreeBody t) {
        List<DeptUserTreeBody> tlist = new ArrayList<>();
        for (DeptUserTreeBody n : list) {
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().equals(t.getId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDept> list, SysDept t) {
        return !getChildList(list, t).isEmpty();
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChildForDeptUser(List<DeptUserTreeBody> list, DeptUserTreeBody t) {
        return !getChildListForDeptUser(list, t).isEmpty();
    }
}
