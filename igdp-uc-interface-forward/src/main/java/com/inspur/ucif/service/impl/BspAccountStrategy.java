package com.inspur.ucif.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.inspur.common.config.SsoConfig;
import com.inspur.common.constant.UserConstants;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.domain.TreeSelect;
import com.inspur.common.core.domain.entity.SysDept;
import com.inspur.common.core.domain.entity.SysMenu;
import com.inspur.common.core.domain.entity.SysRole;
import com.inspur.common.core.domain.entity.SysUser;
import com.inspur.common.core.domain.model.LoginUser;
import com.inspur.common.core.domain.model.SsoInfo;
import com.inspur.common.utils.LoginHelper;
import com.inspur.common.utils.StringUtils;
import com.inspur.ucif.constant.GrantTypeConstants;
import com.inspur.ucif.domain.BspMenu;
import com.inspur.ucif.domain.BspMenuExtend;
import com.inspur.ucif.service.IAccountStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * BSP认证相关信息获取
 */
@Service("bspAccountStrategy")
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "sys.account-select-type", havingValue = "bsp")
public class BspAccountStrategy implements IAccountStrategy {
    
    private final SsoConfig ssoConfig;

    
    @Override
    public LoginUser getCurrentUser() {
        return LoginHelper.getLoginUser();
    }

    @Override
    public List<SysMenu> getMenuTree(String token) {
        SsoInfo ssoInfo = ssoConfig.getSsoInfo(GrantTypeConstants.BSP_GRANT_TYPE);
        String getMenuUrl = ssoInfo.getServer() + ssoInfo.getGetMenuByUserApi();

        if (StringUtils.isBlank(token)) {
            token = StpUtil.getTokenValue();
        }
        if (StringUtils.isBlank(token)) {
            return Collections.emptyList();
        }
        try {
            // 设置请求头
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", token);
            headers.put("Content-Type", "application/json;charset=UTF-8");
            // 设置请求参数
            Map<String, Object> params = new HashMap<>();
            params.put("appCode", ssoInfo.getAppId());
            // 发送POST请求
            String result = HttpRequest.post(getMenuUrl)
                    .addHeaders(headers)
                    .body(JSON.toJSONString(params))
                    .execute()
                    .body();

            log.info("调用用户中心获取菜单树响应内容：{}", result);
            // 解析响应结果
            JSONObject retJo = JSON.parseObject(result);
            if (Objects.isNull(retJo)) {
                log.error("调用用户中心返回菜单数据为空");
                return Collections.emptyList();
            }
            // 检查响应状态码
            Integer code = retJo.getInteger("code");
            if (!Objects.equals(code, 200)) {
                log.error("调用用户中心获取菜单失败，错误信息：{}", retJo.getString("msg"));
                return Collections.emptyList();
            }
            // 获取菜单数据并转换为BspMenu对象列表
            JSONArray menuArray = retJo.getJSONArray("data");
            if (Objects.isNull(menuArray) || menuArray.isEmpty()) {
                log.info("用户无菜单权限");
                return Collections.emptyList();
            }
            // 使用BspMenu实体类解析数据
            List<BspMenu> bspMenuList = JSON.parseArray(menuArray.toJSONString(), BspMenu.class);
            // 转换为SysMenu对象
            List<SysMenu> menuList = new ArrayList<>();
            for (BspMenu bspMenu : bspMenuList) {
                SysMenu menu = convertBspMenuToSysMenu(bspMenu);
                if (Objects.nonNull(menu)) {
                    // 设置 parentId为 0
                    menu.setParentId("0");
                    menuList.add(menu);
                }
            }
            return menuList;
        } catch (Exception e) {
            log.error("调用用户中心获取菜单树异常", e);
            return Collections.emptyList();
        }
    }

    /**
     * 将BspMenu对象转换为SysMenu对象
     */
    private SysMenu convertBspMenuToSysMenu(BspMenu bspMenu) {
        if (Objects.isNull(bspMenu)) {
            return null;
        }

        SysMenu menu = new SysMenu();


        // 使用扩展信息中属性覆盖菜单属性
        BspMenuExtend bspMenuExtend = new BspMenuExtend();;
        if (!StringUtils.isBlank(bspMenu.getRemark())) {
            bspMenuExtend = JSONUtil.toBean(bspMenu.getRemark(), BspMenuExtend.class);
        }

        BeanUtil.copyProperties(bspMenuExtend, menu);
        
        // 设置菜单ID
        menu.setMenuId(bspMenu.getMenuId());
        
        // 设置菜单名称
        menu.setMenuName(bspMenu.getTitle());
        
        // 设置菜单路径
        String path = bspMenu.getPath();
        menu.setPath(path);
        // 设置菜单图标
        menu.setIcon(bspMenu.getIcon());
        // 设置是否内链
        menu.setIsFrame(bspMenu.getOpenWay() == 1 ? UserConstants.NO_FRAME: UserConstants.YES_FRAME);
        menu.setVisible(bspMenu.getHidden() == 0 ? "0" : "1");
        
        // 设置菜单类型（1目录 2菜单）,如果是目录，则设置路径为 data 对应的值
        String type = bspMenu.getType();
        if ("1".equals(type)) {
            menu.setMenuType(UserConstants.TYPE_DIR);
            menu.setPath(bspMenu.getData());
        } else if ("2".equals(type)) {
            menu.setMenuType(UserConstants.TYPE_MENU);
        } else {
            menu.setMenuType(UserConstants.TYPE_MENU); // 默认为菜单
        }

        // 设置菜单排序
        menu.setOrderNum(1);

        // 递归处理子菜单
        List<BspMenu> children = bspMenu.getChildren();
        if (Objects.nonNull( children) && !children.isEmpty()) {
            List<SysMenu> childMenus = new ArrayList<>();
            for (BspMenu childBspMenu : children) {
                SysMenu childMenu = convertBspMenuToSysMenu(childBspMenu);
                if (Objects.nonNull(childMenu)) {
                    childMenus.add(childMenu);
                }
            }
            menu.setChildren(childMenus);
        }
        
        return menu;
    }

    @Override
    public List<SysUser> searchUserList(SysUser queryParams) {
        return Collections.emptyList();
    }

    @Override
    public AjaxResult searchUserPage(SysUser queryParams, Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public SysUser getUserById(String userId) {
        return null;
    }

    @Override
    public SysDept getDeptById(String deptId) {
        return null;
    }

    @Override
    public List<SysRole> searchRoleList(SysRole queryParams) {
        return Collections.emptyList();
    }

    @Override
    public AjaxResult searchRolePage(SysRole queryParams, Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public List<TreeSelect> getDeptTree(SysDept sysDept) {
        return Collections.emptyList();
    }

    @Override
    public List<SysDept> getDeptList(SysDept dept) {
        return Collections.emptyList();
    }

    @Override
    public List<TreeSelect> getDeptUserTreeList() {
        return Collections.emptyList();
    }


}
