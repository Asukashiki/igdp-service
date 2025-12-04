package com.inspur.seed.service.invested.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.StringUtils;
import com.inspur.seed.domain.invested.InputReceiveUnion;
import com.inspur.seed.mapper.invested.InputReceiveUnionMapper;
import com.inspur.seed.service.invested.IInputReceiveUnionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Union接收确认Service实现
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@Service
public class InputReceiveUnionServiceImpl extends ServiceImpl<InputReceiveUnionMapper, InputReceiveUnion>
        implements IInputReceiveUnionService {

    @Override
    public List<InputReceiveUnion> queryReceiveList(String releaseBy, String batchId, String cropType,
                                                     String varietyName, LocalDate startTime, LocalDate endTime,
                                                     String receiveStatus) {
        LambdaQueryWrapper<InputReceiveUnion> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotEmpty(releaseBy)) {
            wrapper.like(InputReceiveUnion::getReleaseBy, releaseBy);
        }
        if (startTime != null) {
            wrapper.ge(InputReceiveUnion::getReleaseDate, LocalDateTime.of(startTime, LocalTime.MIN));
        }
        if (endTime != null) {
            wrapper.le(InputReceiveUnion::getReleaseDate, LocalDateTime.of(endTime, LocalTime.MAX));
        }
        if (StringUtils.isNotEmpty(receiveStatus)) {
            wrapper.eq(InputReceiveUnion::getReceiveStatus, receiveStatus);
        }

        wrapper.orderByDesc(InputReceiveUnion::getReleaseDate);

        return list(wrapper);
    }

    @Override
    public boolean confirmReceive(String id, String confirmBy, String confirmOrg) {
        InputReceiveUnion receive = getById(id);
        if (receive == null) {
            throw new ServiceException("接收记录不存在");
        }

        if ("已确认".equals(receive.getReceiveStatus())) {
            throw new ServiceException("该记录已确认，无需重复操作");
        }

        receive.setConfirmBy(confirmBy);
        receive.setConfirmOrg(confirmOrg);
        receive.setConfirmTime(LocalDateTime.now());
        receive.setReceiveStatus("Confirmed");
        receive.setOperateBy("admin"); // TODO: 从登录用户获取
        receive.setOperateTime(LocalDateTime.now());
        receive.setUpdateTime(LocalDateTime.now());

        return updateById(receive);
    }

    @Override
    public InputReceiveUnion queryById(String id) {
        return getById(id);
    }
}
