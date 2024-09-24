package com.inspur.workorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.workorder.domain.ProcessFrequentlyUsed;

import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName IWorkOrderFrequentlyUsedService
 * @date 2024/5/20 11:28
 */
public interface IProcessFrequentlyUsedService extends IService<ProcessFrequentlyUsed> {
    /**
     * 获取常用工单列表
     * @param userId 用户id
     * @return 列表集合
     * */
    List<ProcessFrequentlyUsed> getList(String userId);
}
