package com.inspur.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.domain.SelectEntity;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.enums.BusinessType;
import com.inspur.common.utils.LoginHelper;
import com.inspur.common.utils.poi.ExcelUtil;
import com.inspur.system.domain.SysUnifyTodo;
import com.inspur.system.service.ISysUnifyTodoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 待办事项接口
 *
 * @author liyunlong
 * @date 2024/1/25
 */
@RestController
@RequestMapping("/system/todo")
public class SysTodoController extends BaseController {
    @Resource
    private ISysUnifyTodoService sysTodoService;

    /**
     * 查询待办事项列表
     */
    @GetMapping("/list")
    public TableDataInfo<?> list(SysUnifyTodo sysUnifyTodo) {
        startPage();
        sysUnifyTodo.setUserId(LoginHelper.getUserId());
        List<SysUnifyTodo> list = sysTodoService.selectSysTodoList(sysUnifyTodo);
        return getDataTable(list);
    }

    /**
     * 导出待办事项列表
     */
    @Log(title = "待办事项", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUnifyTodo sysUnifyTodo) {
        sysUnifyTodo.setUserId(LoginHelper.getUserId());
        List<SysUnifyTodo> list = sysTodoService.selectSysTodoList(sysUnifyTodo);
        ExcelUtil<SysUnifyTodo> util = new ExcelUtil<SysUnifyTodo>(SysUnifyTodo.class);
        util.exportExcel(response, list, "待办事项数据");
    }

    /**
     * 获取待办事项详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return success(sysTodoService.getById(id));
    }

    /**
     * 新增待办事项
     */
    @SaCheckPermission("system:todo:add")
    @Log(title = "待办事项", businessType = BusinessType.INSERT)
    @PostMapping
    public void add(@RequestBody SysUnifyTodo sysUnifyTodo) {
        sysTodoService.addTodo(sysUnifyTodo);
    }

    /**
     * 修改待办事项
     */
    @SaCheckPermission("system:todo:edit")
    @Log(title = "待办事项", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysUnifyTodo sysUnifyTodo) {
        return toAjax(sysTodoService.updateTodo(sysUnifyTodo));
    }

    /**
     * 删除待办事项
     */
    @SaCheckPermission("system:todo:remove")
    @Log(title = "待办事项", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(sysTodoService.removeBatchByIds(Arrays.asList(ids)));
    }

    /**
     * 获取统一待办类型
     */
    @GetMapping("/type")
    public AjaxResult getType() {
        List<SelectEntity> typeList = sysTodoService.selectType();
        return success(typeList);
    }
    /**
     * 获取统一待办来源
     */
    @GetMapping("/source")
    public AjaxResult getSource() {
        List<SelectEntity> sourceList = sysTodoService.selectSource();
        return success(sourceList);
    }


}
