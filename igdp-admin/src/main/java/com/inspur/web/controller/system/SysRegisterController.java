package com.inspur.web.controller.system;

import java.util.List;

import cn.dev33.satoken.annotation.SaIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.domain.entity.SysDept;
import com.inspur.common.core.domain.entity.SysDictData;
import com.inspur.common.core.domain.model.RegisterBody;
import com.inspur.common.utils.StringUtils;
import com.inspur.framework.web.service.SysRegisterService;
import com.inspur.system.service.ISysConfigService;
import com.inspur.system.service.ISysDeptService;
import com.inspur.system.service.ISysDictTypeService;

/**
 * 注册验证
 * 
 * @author liyunlong
 */
@RestController
public class SysRegisterController extends BaseController
{
    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysDictTypeService dictTypeService;

    @SaIgnore
    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterBody user)
    {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser"))))
        {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }

    /**
     * 获取部门树列表（公开接口，用于注册页面）
     * 注意：使用 selectListWithOutDataScope 避免 DataScope 注解的登录检查
     */
    @SaIgnore
    @GetMapping("/register/deptTree")
    public AjaxResult deptTree()
    {
        List<SysDept> deptList = deptService.selectListWithOutDataScope(new SysDept());
        return success(deptService.buildDeptTreeSelect(deptList));
    }

    /**
     * 根据字典类型查询字典数据（公开接口，用于注册页面）
     */
    @SaIgnore
    @GetMapping("/register/dict/{dictType}")
    public AjaxResult dictType(@PathVariable String dictType)
    {
        List<SysDictData> data = dictTypeService.selectDictDataByType(dictType);
        return success(data);
    }
}

