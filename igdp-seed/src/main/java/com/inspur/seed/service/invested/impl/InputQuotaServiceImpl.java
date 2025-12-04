package com.inspur.seed.service.invested.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.utils.StringUtils;
import com.inspur.seed.domain.invested.InputQuota;
import com.inspur.seed.mapper.invested.InputQuotaMapper;
import com.inspur.seed.service.invested.IInputQuotaService;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

/**
 * 投入品配额Service实现
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@Service
public class InputQuotaServiceImpl extends ServiceImpl<InputQuotaMapper, InputQuota>
        implements IInputQuotaService {

    @Override
    public List<InputQuota> queryQuotaList(Integer year, String adminOrg, String zone,
                                            String inputType, String farmerId, String receiveStatus) {
        LambdaQueryWrapper<InputQuota> wrapper = new LambdaQueryWrapper<>();

        // 默认查询当前年度
        if (year == null) {
            year = Calendar.getInstance().get(Calendar.YEAR);
        }
        wrapper.eq(InputQuota::getYear, year);

        if (StringUtils.isNotEmpty(adminOrg)) {
            wrapper.like(InputQuota::getAdminOrg, adminOrg);
        }
        if (StringUtils.isNotEmpty(zone)) {
            wrapper.eq(InputQuota::getZone, zone);
        }
        if (StringUtils.isNotEmpty(inputType)) {
            wrapper.eq(InputQuota::getInputType, inputType);
        }
        if (StringUtils.isNotEmpty(farmerId)) {
            wrapper.eq(InputQuota::getFarmerId, farmerId);
        }
        if (StringUtils.isNotEmpty(receiveStatus)) {
            wrapper.eq(InputQuota::getReceiveStatus, receiveStatus);
        }

        wrapper.orderByDesc(InputQuota::getCreateTime);

        return list(wrapper);
    }

    @Override
    public InputQuota queryById(String id) {
        return getById(id);
    }
}
