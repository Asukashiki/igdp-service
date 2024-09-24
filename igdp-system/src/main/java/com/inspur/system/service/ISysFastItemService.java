package com.inspur.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.system.domain.SysFastItem;

import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName ISysFastItemService
 * @date 2024/6/14 17:25
 */
public interface ISysFastItemService extends IService<SysFastItem> {

    /**
     * 查询列表
     * @param queryParam 参数
     * @return 集合
     * */
    List<SysFastItem> getList(SysFastItem queryParam);
    /**
     * 新增保存
     * @param item 信息
     * @return 结果
     * */
    AjaxResult addItem(SysFastItem item);
    /**
     * 更新保存
     * @param item 更新内容
     * @return 结果
     * */
    AjaxResult updateItem(SysFastItem item);
    /**
     * 删除
     * @param id id
     * @return 操作结果
     * */
    AjaxResult deleteItem(String id);
    /**
     * 根据userId获取个人的项目列表
     * @param userId 用户id
     * @return 列表
     * */
    List<SysFastItem> getListByUserId(String userId);

    /**
     * 查询非本用户的列表
     * @param itemList 已有列表
     * @return 列表
     */
    List<SysFastItem> getOtherList(List<SysFastItem> itemList);
    /**
     * 新增快速发起数据
     * @param sysFastItem
     */
    AjaxResult addFastItem(SysFastItem sysFastItem);

    /**
     * 删除快速发起数据
     * @param id
     * @return
     */
    AjaxResult removeItem(String[] id);

    /**
     * 修改快速发起数据
     * @param sysFastItem
     * @return
     */
    AjaxResult updateFastItem(SysFastItem sysFastItem);

    /**
     * 查询快速发起数据
     * @param keyword
     * @return
     */
    List<SysFastItem> getFastItemList(String keyword);

    /**
     * 根据id查快速开始数据
     * @param id
     * @return
     */
    List<SysFastItem> getFastItemById(String id);
}
