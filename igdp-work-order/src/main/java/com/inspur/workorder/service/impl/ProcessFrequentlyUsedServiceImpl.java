package com.inspur.workorder.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.workorder.domain.ProcessFrequentlyUsed;
import com.inspur.workorder.mapper.ProcessFrequentlyUsedMapper;
import com.inspur.workorder.service.IProcessFrequentlyUsedService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName WorkOrderFrequentlyUsedServiceImpl
 * @date 2024/5/20 11:28
 */
@Service
public class ProcessFrequentlyUsedServiceImpl extends ServiceImpl<ProcessFrequentlyUsedMapper, ProcessFrequentlyUsed> implements IProcessFrequentlyUsedService {
    @Override
    public List<ProcessFrequentlyUsed> getList(String userId) {
        LambdaQueryWrapper<ProcessFrequentlyUsed> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotEmpty(userId), ProcessFrequentlyUsed::getUserId, userId);
        queryWrapper.orderByDesc(ProcessFrequentlyUsed::getUsedNumber);
        return list(queryWrapper);
    }
}
