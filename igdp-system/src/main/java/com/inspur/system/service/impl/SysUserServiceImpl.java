package com.inspur.system.service.impl;

import cn.hutool.core.text.PasswdStrength;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.inspur.common.annotation.DataScope;
import com.inspur.common.constant.Constants;
import com.inspur.common.constant.UserConstants;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.domain.entity.SysDept;
import com.inspur.common.core.domain.entity.SysRole;
import com.inspur.common.core.domain.entity.SysUser;
import com.inspur.common.core.domain.model.LoginUser;
import com.inspur.common.exception.ServiceException;

import com.inspur.common.utils.LoginHelper;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.bean.BeanValidators;
import com.inspur.common.utils.spring.SpringUtils;
import com.inspur.system.domain.SysPost;
import com.inspur.system.domain.SysUserPost;
import com.inspur.system.domain.SysUserRole;
import com.inspur.system.mapper.*;
import com.inspur.system.service.ISysConfigService;
import com.inspur.system.service.ISysUserRoleService;
import com.inspur.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.inspur.common.utils.StringUtils.isNotEmpty;

/**
 * 用户 业务层处理
 *
 * @author liyunlong
 */
@Service("sysUserService")
public class SysUserServiceImpl extends MPJBaseServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private ISysUserRoleService userRoleService;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    protected Validator validator;

    /**
     * 密码中必须包含字母、数字、特称字符，至少8个字符，最多16个字符
     */
    private static final String REG_EX2 = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,20}";

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "t")
    public List<SysUser> selectUserList(SysUser user) {
        MPJLambdaWrapper<SysUser> wrapper = new MPJLambdaWrapper<>();
        wrapper.distinct().selectAll(SysUser.class)
                .selectAs(SysDept::getDeptName, SysUser::getDeptName)
                .leftJoin(SysDept.class, "d", SysDept::getDeptId, SysUser::getDeptId);
        wrapper.eq(SysUser::getDelFlag, Constants.DELETE_FLAG_VALID);
        if (isNotEmpty(user.getUserId())) {
            wrapper.eq(SysUser::getUserId, user.getUserId());
        }
        if (isNotEmpty(user.getUserName())) {
            wrapper.like(SysUser::getUserName, user.getUserName());
        }
        if (StringUtils.isNotEmpty(user.getNickName())) {
            wrapper.like(SysUser::getNickName, user.getNickName());
        }
        if (isNotEmpty(user.getStatus())) {
            wrapper.eq(SysUser::getStatus, user.getStatus());
        }
        if (isNotEmpty(user.getAllowedShow())) {
            wrapper.eq(SysUser::getAllowedShow, user.getAllowedShow());
        }
        if (isNotEmpty(user.getPhoneNumber())) {
            wrapper.like(SysUser::getPhoneNumber, user.getPhoneNumber());
        }
        if (isNotEmpty(user.getDeptId())) {
            wrapper.eq(SysUser::getDeptId, user.getDeptId());
            wrapper.or().apply(StringUtils.format(" t.dept_id in (SELECT sd.DEPT_ID FROM sys_dept sd LEFT JOIN sys_dept sd2 ON sd.ancestors LIKE CONCAT(sd2.ANCESTORS, '%') WHERE sd2.DEPT_ID = '{}' )", user.getDeptId()));

        }
        if (StringUtils.isNotEmpty(user.getRoleId()) || StringUtils.isNotEmpty(user.getRoleKey())) {
            wrapper.leftJoin(SysUserRole.class, "ur", SysUserRole::getUserId, SysUser::getUserId)
                    .leftJoin(SysRole.class, "r", SysRole::getRoleId, SysUserRole::getRoleId);
            if (StringUtils.isNotEmpty(user.getRoleId())) {
                wrapper.eq(SysUserRole::getRoleId, user.getRoleId());
            }
            if (StringUtils.isNotEmpty(user.getRoleKey())) {
                wrapper.eq(SysRole::getRoleKey, user.getRoleKey());
            }
        }


        Map<String, Object> params = user.getParams();
        if (null != params) {
            LocalDate beginTime = null != params.get("beginTime") ? LocalDate.parse(params.get("beginTime").toString()) : null;
            LocalDate endTime = null != params.get("endTime") ? LocalDate.parse(params.get("endTime").toString()) : null;
            String keyword = null != params.get("keyword") ? params.get("keyword").toString() : "";
            if (StringUtils.isNotEmpty(keyword)) {
                wrapper.like(SysUser::getUserId, keyword).or().like(SysUser::getUserName, keyword)
                        .or().like(SysUser::getNickName, keyword).or().like(SysUser::getPhoneNumber, keyword).or().like(SysUser::getEmail, keyword);
            }
            String dataScope = null != params.get("dataScope") ? params.get("dataScope").toString().toLowerCase() : "";
            if (null != beginTime) {
                wrapper.ge(SysUser::getCreateTime, beginTime);
            }
            if (null != endTime) {
                wrapper.le(SysUser::getCreateTime, endTime);
            }
            if (isNotEmpty(dataScope)) {
                if (dataScope.startsWith(" and")) {
                    dataScope = dataScope.replace(" and", "");
                }
                wrapper.apply(dataScope);
            }
        }
//        wrapper.last(" group by t.user_id");
        return selectJoinList(SysUser.class, wrapper);
    }

    @Override
    public IPage<SysUser> selectUserPage(SysUser sysUser, Integer pageNum, Integer limit) {
        IPage<SysUser> page = new Page<>(pageNum, limit);
        MPJLambdaWrapper<SysUser> wrapper = JoinWrappers.lambda(SysUser.class);
        wrapper.select(SysUser::getUserId, SysUser::getUserName, SysUser::getPhoneNumber, SysUser::getDeptId, SysUser::getNickName, SysUser::getEmail);
        wrapper.selectAs(SysDept::getDeptName, SysUser::getDeptName);
        wrapper.leftJoin(SysDept.class, "d", SysDept::getDeptId, SysUser::getDeptId);

        wrapper.eq(SysUser::getDelFlag, Constants.DELETE_FLAG_VALID);
        if (StringUtils.isNotEmpty(sysUser.getStatus())) {
            wrapper.eq(SysUser::getStatus, sysUser.getStatus());
        }
        if (StringUtils.isNotEmpty(sysUser.getUserId())) {
            wrapper.eq(SysUser::getUserId, sysUser.getUserId());
        }
        if (StringUtils.isNotEmpty(sysUser.getUserName())) {
            wrapper.like(SysUser::getUserName, sysUser.getUserName());
        }
        if (StringUtils.isNotEmpty(sysUser.getAllowedShow())) {
            wrapper.eq(SysUser::getAllowedShow, sysUser.getAllowedShow());
        }
        if (StringUtils.isNotEmpty(sysUser.getPhoneNumber())) {
            wrapper.like(SysUser::getPhoneNumber, sysUser.getPhoneNumber());
        }
        if (isNotEmpty(sysUser.getDeptId())) {
            wrapper.eq(SysUser::getDeptId, sysUser.getDeptId());
            wrapper.or().apply(StringUtils.format(" t.dept_id in (SELECT sd.DEPT_ID FROM sys_dept sd LEFT JOIN sys_dept sd2 ON sd.ancestors LIKE CONCAT(sd2.ANCESTORS, '%') WHERE sd2.DEPT_ID = '{}' )", sysUser.getDeptId()));
        }
        Map<String, Object> params = sysUser.getParams();
        if (null != params) {
            if (null != params.get("keyword")) {
                String keyword = params.get("keyword").toString();
                wrapper.like(SysUser::getUserId, keyword).or().like(SysUser::getNickName, keyword)
                        .or().like(SysUser::getUserName, keyword)
                        .or().like(SysUser::getPhoneNumber, keyword)
                        .or().like(SysUser::getEmail, keyword);
            }
        }
        if (StringUtils.isNotEmpty(sysUser.getRoleId()) || StringUtils.isNotEmpty(sysUser.getRoleKey())) {
            wrapper.leftJoin(SysUserRole.class, "ur", SysUserRole::getUserId, SysUser::getUserId);
            wrapper.leftJoin(SysRole.class, "r", SysRole::getRoleId, SysUserRole::getRoleId);
            if (StringUtils.isNotEmpty(sysUser.getRoleId())) {
                wrapper.eq(SysUserRole::getRoleId, sysUser.getRoleId());
            }
            if (StringUtils.isNotEmpty(sysUser.getRoleKey())) {
                wrapper.eq(SysRole::getRoleKey, sysUser.getRoleKey());
            }
        }
        return this.baseMapper.selectJoinPage(page, SysUser.class, wrapper);
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectAllocatedList(SysUser user) {
        return this.baseMapper.selectAllocatedList(user);
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUnallocatedList(SysUser user) {
        return this.baseMapper.selectUnallocatedList(user);
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName) {
        SysUser user = getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserName, userName)
                .eq(SysUser::getDelFlag, Constants.DELETE_FLAG_VALID));
        if (null != user) {
            List<SysRole> roles = roleMapper.getRoleListByUserId(user.getUserId());
            if (null == roles || roles.isEmpty()) {
                //默认角色
                user.setRoleIds(new String[]{"public"});
                insertUserRole(user);
                roles = roleMapper.getRoleListByUserId(user.getUserId());
            }
            user.setRoles(roles);
            if (StrUtil.isNotEmpty(user.getDeptId())) {
                SysDept sysDept = sysDeptMapper.selectDeptById(user.getDeptId());
                user.setDept(sysDept);
            }

        }
        return user;
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(String userId) {
        SysUser user = getOne(new LambdaQueryWrapper<SysUser>()
                .select(SysUser::getUserId, SysUser::getUserName, SysUser::getNickName, SysUser::getEmail, SysUser::getPhoneNumber, SysUser::getAvatar, SysUser::getDeptId, SysUser::getSex, SysUser::getStatus)
                .eq(SysUser::getUserId, userId));
        if (null != user) {
            List<SysRole> roles = roleMapper.getRoleListByUserId(userId);
            if (null == roles || roles.isEmpty()) {
                //默认角色
                user.setRoleIds(new String[]{"public"});
                insertUserRole(user);
                roles = roleMapper.getRoleListByUserId(user.getUserId());
            }
            user.setRoles(roles);
            if (StrUtil.isNotEmpty(user.getDeptId())) {
                SysDept sysDept = sysDeptMapper.selectDeptById(user.getDeptId());
                user.setDept(sysDept);
            }

        }
        return user;
    }

    /**
     * 查询用户所属角色组
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userId) {
        List<SysRole> list = roleMapper.getRoleListByUserId(userId);
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
    }

    /**
     * 查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String userName) {
        List<SysPost> list = postMapper.selectPostsByUserName(userName);
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysPost::getPostName).collect(Collectors.joining(","));
    }


    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean checkUserNameUnique(SysUser user) {
        String userId = StringUtils.isNull(user.getUserId()) ? "-1" : user.getUserId();
        SysUser info = getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserName, user.getUserName()).eq(SysUser::getDelFlag, Constants.DELETE_FLAG_VALID));
        if (StringUtils.isNotNull(info) && !info.getUserId().equals(userId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     */
    @Override
    public boolean checkPhoneUnique(SysUser user) {
        String userId = StringUtils.isNull(user.getUserId()) ? "-1" : user.getUserId();
        SysUser info = getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getPhoneNumber, user.getPhoneNumber()).eq(SysUser::getDelFlag, Constants.DELETE_FLAG_VALID));
        if (StringUtils.isNotNull(info) && !info.getUserId().equals(userId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     */
    @Override
    public boolean checkEmailUnique(SysUser user) {
        String userId = StringUtils.isNull(user.getUserId()) ? "-1" : user.getUserId();
        SysUser info = getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getEmail, user.getEmail()).eq(SysUser::getDelFlag, Constants.DELETE_FLAG_VALID));
        if (StringUtils.isNotNull(info) && !info.getUserId().equals(userId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    /**
     * 校验用户是否有数据权限
     *
     * @param userId 用户id
     */
    @Override
    public void checkUserDataScope(String userId) {
        if (!SysUser.isAdmin(LoginHelper.getUserId())) {
            SysUser user = new SysUser();
            user.setUserId(userId);
            List<SysUser> users = SpringUtils.getAopProxy(this).selectUserList(user);
            if (StringUtils.isEmpty(users)) {
                throw new ServiceException("没有权限访问用户数据！");
            }
        }
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertUser(SysUser user) {
        // 新增用户信息
        user.setCreateTime(LocalDateTime.now());
        boolean result = save(user);
        // 新增用户岗位关联
        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return result;
    }

    /**
     * 注册用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean registerUser(SysUser user) {
        if (StrUtil.isEmpty(user.getUserId())) {
            user.setUserId(IdUtil.fastUUID());
        }
        user.setCreateTime(LocalDateTime.now());
        return save(user);
    }

    @Override
    public SysUser registerByLoginUser(LoginUser loginUser) {
        SysUser user = loginUser.getUser();
        if (null == user) {
            user = new SysUser();
            user.setPassword(SmUtil.sm3(MD5.create().digestHex(Constants.INIT_PASSWORD)));
            user.setUserName(loginUser.getUsername());
            user.setUserId(loginUser.getUserId());
            user.setNickName(loginUser.getNickname());
            user.setDeptId(loginUser.getDeptId());
            user.setStatus(Constants.STATUS_VALID);
            user.setCreateTime(LocalDateTime.now());
            user.setDelFlag(Constants.DELETE_FLAG_VALID);
            user.setCreateBy("0");
        }
        //默认角色
        user.setRoleIds(new String[]{"public"});
        //角色信息保存
        save(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return user;
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(SysUser user) {
        String userId = user.getUserId();
        // 删除用户与角色关联
        userRoleService.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(user);
        user.setUpdateTime(LocalDateTime.now());
        user.setPassword(null);
        return updateById(user);
    }

    /**
     * 用户授权角色
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUserAuth(String userId, String[] roleIds) {
        userRoleService.deleteUserRoleByUserId(userId);
        insertUserRole(userId, roleIds);
    }

    /**
     * 修改用户状态
     *
     * @param userId 用户信息
     * @param status 目标状态
     * @return 结果
     */
    @Override
    public boolean updateUserStatus(String userId, String status) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setStatus(status);
        sysUser.setUpdateTime(LocalDateTime.now());
        sysUser.setUpdateBy(LoginHelper.getUserId());
        return updateById(sysUser);
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean updateUserProfile(SysUser user) {

        return updateById(user);
    }

    /**
     * 修改用户头像
     *
     * @param userId 用户ID
     * @param avatar 头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(String userId, String avatar) {
        return this.baseMapper.updateUserAvatar(userId, avatar) > 0;
    }

    /**
     * 重置用户密码
     *
     * @param userId   用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    public AjaxResult changeUserPwd(String userId, String password) {
        SysUser user = getById(userId);
        checkUserAllowed(user);
        checkUserDataScope(userId);
        //校验密码是否符合规则
        PasswdStrength.PASSWD_LEVEL level = PasswdStrength.getLevel(password);
        if (level.ordinal() < PasswdStrength.PASSWD_LEVEL.STRONG.ordinal()) {
            return AjaxResult.error("密码强度太弱，请使用8位以上的大小写字母数字以及特殊符号组合");
        }
        int result = this.baseMapper.resetUserPwd(userId, SmUtil.sm3(MD5.create().digestHex(password)));
        if (result > 0) {
            return AjaxResult.success("密码重置成功");
        } else {
            return AjaxResult.error("密码重置失败");
        }
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user) {
        String[] roleIds = user.getRoleIds();
        if (StringUtils.isNotEmpty(roleIds)) {
            // 新增用户与岗位管理
            List<SysUserRole> list = new ArrayList<>(roleIds.length);
            for (String roleId : roleIds) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getUserId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            userRoleService.saveBatch(list);
        }
    }

    /**
     * 新增用户岗位信息
     *
     * @param user 用户对象
     */
    public void insertUserPost(SysUser user) {
        String[] posts = user.getPostIds();
        if (StringUtils.isNotEmpty(posts)) {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<>(posts.length);
            for (String postId : posts) {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            userPostMapper.batchUserPost(list);
        }
    }

    /**
     * 新增用户角色信息
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    public void insertUserRole(String userId, String[] roleIds) {
        if (StringUtils.isNotEmpty(roleIds)) {
            userRoleService.insertAuthRoles(userId, roleIds);
        }
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserById(String userId) {
        // 删除用户与角色关联
        userRoleService.deleteUserRoleByUserId(userId);
        // 删除用户与岗位表
        userPostMapper.deleteUserPostByUserId(userId);
        return this.baseMapper.deleteUserById(userId);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserByIds(String[] userIds) {
        for (String userId : userIds) {
            checkUserAllowed(new SysUser(userId));
            checkUserDataScope(userId);
        }
        // 删除用户与角色关联
        userRoleService.deleteUserRoleByUserIds(userIds);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPost(userIds);
        return this.baseMapper.deleteUserByIds(userIds);
    }

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(userList) || userList.isEmpty()) {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (SysUser user : userList) {
            try {
                // 验证是否存在这个用户
                SysUser u = this.baseMapper.selectUserByUserName(user.getUserName());
                if (StringUtils.isNull(u)) {
                    BeanValidators.validateWithException(validator, user);
                    user.setPassword(SmUtil.sm3(MD5.create().digestHex(password)));
                    user.setCreateBy(operName);
                    user.setCreateTime(LocalDateTime.now());
                    save(user);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账号 ").append(user.getUserName()).append(" 导入成功");
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, user);
                    checkUserAllowed(u);
                    checkUserDataScope(u.getUserId());
                    user.setUserId(u.getUserId());
                    user.setUpdateBy(operName);
                    user.setUpdateTime(LocalDateTime.now());
                    updateById(user);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账号 ").append(user.getUserName()).append(" 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、账号 ").append(user.getUserName()).append(" 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
