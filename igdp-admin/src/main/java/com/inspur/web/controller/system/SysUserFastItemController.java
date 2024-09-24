package com.inspur.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.domain.model.LoginUser;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.utils.LoginHelper;
import com.inspur.system.domain.SysFastItem;
import com.inspur.system.domain.SysWorkbenchItem;
import com.inspur.system.service.ISysFastItemService;
import com.inspur.system.service.ISysUserFastItemService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName SysUserFastItemController
 * @date 2024/6/15 10:44
 */
@RestController
@RequestMapping("/system/user-fast-item")
public class SysUserFastItemController extends BaseController {
    @Resource
    private ISysUserFastItemService userFastItemService;
    @Resource
    private ISysFastItemService fastItemService;

    /**
     * 获取个人的快速发起列表
     */
    @GetMapping("/list")
    public AjaxResult getPersonalList() {
        List<SysFastItem> list = fastItemService.getListByUserId(LoginHelper.getUserId());
        return AjaxResult.success(list);
    }

    /**
     * 新增保存
     */
    @PutMapping("/save")
    public AjaxResult add(@RequestBody String[] itemIds) {

        userFastItemService.addItem(LoginHelper.getUserId(), itemIds);
        return AjaxResult.success();
    }

    /**
     * 获取个人配置发起的列表
     */
        @GetMapping("/selectList")
    public AjaxResult getOptionsList() {
        AjaxResult ajaxResult = AjaxResult.success();
        List<SysFastItem> itemList = fastItemService.getListByUserId(LoginHelper.getUserId());

        ajaxResult.put("checkedKeys", itemList);
        ajaxResult.put("itemList", fastItemService.getOtherList(itemList));
        return ajaxResult;
    }

    /**
     * 新增快速开始
     */
    @SaCheckPermission("system:user-fast-item:saveItem")
    @PutMapping("/saveItem")
    public AjaxResult addItem(@RequestBody SysFastItem sysFastItem) {

        fastItemService.addFastItem(sysFastItem);
        return AjaxResult.success();
    }
    /**
     * 删除快速开始数据
     */
    @SaCheckPermission("system:user-fast-item:deleteItem")
    @PostMapping("/deleteItem")
    public AjaxResult removeItem( String[] id) {
        return fastItemService.removeItem(id);
    }
    /**
     * 修改快速开始数据
     */
    @SaCheckPermission("system:user-fast-item:editItem")
    @PostMapping("/editItem")
    public AjaxResult updateItem(@RequestBody SysFastItem sysFastItem) {

        return fastItemService.updateFastItem(sysFastItem);
    }

    /**
     * 查询快速开始数据
     */
    @SaCheckPermission("system:user-fast-item:queryItem")
    @GetMapping("/queryItem")
    public TableDataInfo<?> getFastItemList(String keyword) {
        startPage();
        List<SysFastItem> list = fastItemService.getFastItemList(keyword);
        return getDataTable(list);
    }

    /**
     * 根据id查快速开始数据
     * @param id
     * @return
     */
    @GetMapping("/queryItemById")
    public TableDataInfo<?> getFastItemById(String id) {
        startPage();
        List<SysFastItem> list = fastItemService.getFastItemById(id);
        return getDataTable(list);
    }

}
