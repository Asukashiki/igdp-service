package com.inspur.api;

import cn.dev33.satoken.annotation.SaIgnore;
import com.inspur.common.annotation.Log;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.enums.BusinessType;
import com.inspur.system.domain.SysUnifyTodo;
import com.inspur.system.service.ISysUnifyTodoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 统一待办对外api
 *
 * @author liyunlong
 * @version 1.0
 * @ClassName SysTodoApiController
 * @date 2024/6/14 14:13
 */
@SaIgnore
@RestController
@RequestMapping("/open-api/v1/unify-todo")
public class SysTodoApiController {

    @Resource
    private ISysUnifyTodoService unifyTodoService;

    /**
     * 推送统一待办事项
     * 用于三方系统推送
     */
    @Log(title = "待办事项", businessType = BusinessType.INSERT)
    @PostMapping("/push")
    public void push(@RequestBody SysUnifyTodo sysUnifyTodo) {
         unifyTodoService.addTodo(sysUnifyTodo);
    }


    /**
     * 更新待办事项状态为
     */
    @Log(title = "待办事项", businessType = BusinessType.UPDATE)
    @PostMapping("/updateStatus")
    public void update(@RequestBody SysUnifyTodo sysUnifyTodo) {
        unifyTodoService.updateStatus(sysUnifyTodo);
    }

}
