package com.inspur.data.treating.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.core.domain.entity.UnifyStatisticsItemValue;
import com.inspur.data.treating.service.IUnifyStatisticsItemValueService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 统计数据接口
 *
 * @author liyunlong
 * @version 1.0
 * @ClassName UnifyStatisticsItemValueController
 * @date 2024/7/19 14:59
 */
@RestController
@RequestMapping("/data-treating/item-value")
public class UnifyStatisticsItemValueController extends BaseController {
    @Resource
    private IUnifyStatisticsItemValueService unifyStatisticsItemValueService;

    /**
     * 获取列表数据
     * 带分页
     */
    @SaCheckPermission("data-treating:item-value:list")
    @GetMapping("/listPage")
    public TableDataInfo<?> getListPage(UnifyStatisticsItemValue query) {
        startPage();
        List<UnifyStatisticsItemValue> valueList = unifyStatisticsItemValueService.getList(query);
        return getDataTable(valueList);
    }

    /**
     * 单条数据
     */
    @SaCheckPermission("data-treating:item-value:query")
    @GetMapping("/queryById")
    public AjaxResult get(@RequestParam("id") String id) {
        UnifyStatisticsItemValue itemValue = unifyStatisticsItemValueService.getById(id);
        return AjaxResult.success(itemValue);
    }


    /**
     * 用于展示数据的列表查询
     */
    @GetMapping("/list")
    public AjaxResult list(UnifyStatisticsItemValue query) {
        List<UnifyStatisticsItemValue> valueList = unifyStatisticsItemValueService.getList(query);
        return AjaxResult.success(valueList);
    }

}
