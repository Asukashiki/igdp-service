package com.inspur.framework.web.service;

import cn.dev33.satoken.stp.StpInterface;
import com.inspur.common.core.domain.model.LoginUser;
import com.inspur.common.utils.LoginHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author liyunlong
 * @date 2023/12/19
 */
@Service("strInterface")
public class StpInterfaceImpl implements StpInterface {
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        List<String> list = null;
        if (null != loginUser) {
            Set<String> permissions = loginUser.getPermissions();
            if (null != permissions && !permissions.isEmpty()) {
                list = new ArrayList<>(permissions);
            }
        }
        return list;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        List<String> list = null;
        if (null != loginUser) {
            Set<String> roles = loginUser.getRoles();
            if (null != roles && !roles.isEmpty()) {
                list = new ArrayList<>(roles);
            }
        }
        return list;
    }
}
