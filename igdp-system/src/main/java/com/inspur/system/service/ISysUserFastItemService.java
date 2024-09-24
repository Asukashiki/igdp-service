package com.inspur.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.system.domain.SysFastItem;
import com.inspur.system.domain.SysUserFastItem;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName ISysUserFastItemService
 * @date 2024/6/14 17:24
 */
public interface ISysUserFastItemService extends IService<SysUserFastItem> {
    /**
     * 添加一条
     * @param userId 用户id
     * @param itemId 项目id
     * */
    void addItem(String userId,String[] itemIds);
    /**
     * 删除一条
     * @param userId 用户id
     * @param itemId 项目id
     * */
    void deleteItemByItemId(String userId,String itemId);




}
