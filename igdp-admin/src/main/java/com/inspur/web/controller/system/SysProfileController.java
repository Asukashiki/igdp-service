package com.inspur.web.controller.system;

import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.inspur.common.utils.RsaEncrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.inspur.common.annotation.Log;
import com.inspur.common.config.SystemConfig;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.domain.entity.SysUser;
import com.inspur.common.core.domain.model.LoginUser;
import com.inspur.common.enums.BusinessType;
import com.inspur.common.utils.LoginHelper;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.file.FileUploadUtils;
import com.inspur.common.utils.file.MimeTypeUtils;
import com.inspur.system.service.ISysUserService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 个人信息 业务处理
 *
 * @author liyunlong
 */
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Value("${rsa.private-key:''}")
    private String privateKey;
    /**
     * 密码中必须包含字母、数字、特称字符，至少8个字符，最多16个字符
     */
    private static final String REG_EX2 = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,20}";


    /**
     * 个人信息
     */
    @GetMapping
    public AjaxResult profile() {
        LoginUser loginUser = getLoginUser();
        SysUser user = loginUser.getUser();
        AjaxResult ajax = AjaxResult.success(user);
        ajax.put("roleGroup", userService.selectUserRoleGroup(loginUser.getUserId()));
        ajax.put("postGroup", userService.selectUserPostGroup(loginUser.getUserId()));
        return ajax;
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateProfile(@RequestBody SysUser user) {
        LoginUser loginUser = getLoginUser();
        SysUser currentUser = loginUser.getUser();
        currentUser.setNickName(user.getNickName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhoneNumber(user.getPhoneNumber());
        currentUser.setSex(user.getSex());
        if (StringUtils.isNotEmpty(user.getPhoneNumber()) && !userService.checkPhoneUnique(currentUser)) {
            return error("修改用户'" + loginUser.getUsername() + "'失败，手机号码已存在");
        }
        if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(currentUser)) {
            return error("修改用户'" + loginUser.getUsername() + "'失败，邮箱账号已存在");
        }
        if (userService.updateUserProfile(currentUser)) {
            loginUser.setUser(currentUser);
            // 更新缓存用户信息
            LoginHelper.updateLoginUser(loginUser);
            return success();
        }
        return error("修改个人信息异常，请联系管理员");
    }

    /**
     * 修改密码
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public AjaxResult updatePwd(String oldPassword, String newPassword) throws Exception {
        oldPassword = RsaEncrypt.decrypt(oldPassword, privateKey);
        newPassword = RsaEncrypt.decrypt(newPassword, privateKey);
        String userId = getLoginUser().getUserId();
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(SysUser::getPassword);
        wrapper.eq(SysUser::getUserId, userId);
        SysUser sysUser = userService.getOne(wrapper);
        if (!SmUtil.sm3(MD5.create().digestHex(oldPassword)).equals(sysUser.getPassword())) {
            return error("修改密码失败，旧密码错误");
        }
        if (oldPassword.equals(newPassword)) {
            return error("新密码不能与旧密码相同");
        }
        //校验密码是否符合规则
//        Pattern passwordPattern = Pattern.compile(REG_EX2);
//        Matcher matcher = passwordPattern.matcher(newPassword);
//        if (!matcher.matches()) {
//            return error("密码中必须包含字母、数字、特殊字符($@$!%*?&)，至少8个字符，最多20个字符");
//        }
        return userService.changeUserPwd(userId, newPassword);

    }

    /**
     * 头像上传
     */
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            LoginUser loginUser = getLoginUser();
            String avatar = FileUploadUtils.upload(SystemConfig.getAvatarPath(), file, MimeTypeUtils.IMAGE_EXTENSION);
            if (userService.updateUserAvatar(loginUser.getUserId(), avatar)) {
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", avatar);
                // 更新缓存用户头像
                loginUser.getUser().setAvatar(avatar);
                LoginHelper.updateLoginUser(loginUser);
                return ajax;
            }
        }
        return error("上传图片异常，请联系管理员");
    }
}
