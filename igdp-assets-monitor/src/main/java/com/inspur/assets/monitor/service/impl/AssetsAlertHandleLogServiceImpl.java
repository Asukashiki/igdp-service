package com.inspur.assets.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.assets.monitor.domain.AssetsAlertCur;
import com.inspur.assets.monitor.domain.AssetsAlertHandleLog;
import com.inspur.assets.monitor.mapper.AssetsAlertHandleLogMapper;
import com.inspur.assets.monitor.service.IAssetsAlertHandleLogService;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.inspur.common.utils.LoginHelper.getUserId;
import static com.inspur.common.utils.LoginHelper.getUsername;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName AssetsAlertHandleLogServiceImpl
 * @date 2024/7/8 11:27
 */
@Service
public class AssetsAlertHandleLogServiceImpl extends ServiceImpl<AssetsAlertHandleLogMapper, AssetsAlertHandleLog> implements IAssetsAlertHandleLogService {
    @Override
    public AjaxResult add(AssetsAlertHandleLog assetsAlertHandleLog) {
        assetsAlertHandleLog.setUserId(getUserId());
        save(assetsAlertHandleLog);
        return AjaxResult.success();
    }

    @Override
    public List<AssetsAlertHandleLog> getAlertHandleLog(String alertId) {
        LambdaQueryWrapper<AssetsAlertHandleLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(alertId),AssetsAlertHandleLog::getAlertId,alertId);
        queryWrapper.orderByDesc(AssetsAlertHandleLog::getHandleTime);
        return list(queryWrapper);
    }
}
