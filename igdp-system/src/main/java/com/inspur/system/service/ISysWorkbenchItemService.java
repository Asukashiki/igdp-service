package com.inspur.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.system.domain.SysWorkbenchItem;

import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName ISysWorkbenchItemService
 * @date 2024/6/10 16:53
 */
public interface ISysWorkbenchItemService extends IService<SysWorkbenchItem> {
    /**
     * 查询列表
     * @param item 查询条件
     * @return 集合
     * */
    List<SysWorkbenchItem> selectItemList(SysWorkbenchItem item);
    /**
     * 新增保存
     * @param item 信息
     * @return 结果
     * */
    AjaxResult addItem(SysWorkbenchItem item);
    /**
     * 修改
     * @param item 信息
     * @return 结果
     * */
    AjaxResult updateItem(SysWorkbenchItem item);
    /**
     * 删除
     * @param id id
     * @return 结果
     * */
    AjaxResult deleteItemById(String id);
    /**
     * 根据userId加载工作台内容
     * @param userId 用户id
     * @return 工作台内容
     * */
    List<SysWorkbenchItem> getItemListByUserId(String userId);
}
