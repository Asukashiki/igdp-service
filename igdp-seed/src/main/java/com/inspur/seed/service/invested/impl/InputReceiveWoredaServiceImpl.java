package com.inspur.seed.service.invested.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.StringUtils;
import com.inspur.seed.domain.invested.InputReceiveWoreda;
import com.inspur.seed.domain.invested.InputReleaseDetail;
import com.inspur.seed.mapper.invested.InputReceiveWoredaMapper;
import com.inspur.seed.mapper.invested.InputReleaseDetailMapper;
import com.inspur.seed.service.invested.IInputReceiveWoredaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Woreda接收确认Service实现
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@Service
public class InputReceiveWoredaServiceImpl extends ServiceImpl<InputReceiveWoredaMapper, InputReceiveWoreda>
        implements IInputReceiveWoredaService {

    @Resource
    private InputReleaseDetailMapper detailMapper;

    @Override
    public List<InputReceiveWoreda> queryReceiveList(String woredaName, String receiveStatus,
                                                      LocalDate startTime, LocalDate endTime) {
        LambdaQueryWrapper<InputReceiveWoreda> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotEmpty(woredaName)) {
            wrapper.like(InputReceiveWoreda::getTargetId, woredaName);
        }
        if (StringUtils.isNotEmpty(receiveStatus)) {
            wrapper.eq(InputReceiveWoreda::getReceiveStatus, receiveStatus);
        }
        if (startTime != null) {
            wrapper.ge(InputReceiveWoreda::getReleaseDate, LocalDateTime.of(startTime, LocalTime.MIN));
        }
        if (endTime != null) {
            wrapper.le(InputReceiveWoreda::getReleaseDate, LocalDateTime.of(endTime, LocalTime.MAX));
        }

        wrapper.orderByDesc(InputReceiveWoreda::getReleaseDate);

        return list(wrapper);
    }

    @Override
    public Map<String, Object> queryReceiveDetail(String id) {
        InputReceiveWoreda receive = getById(id);
        if (receive == null) {
            throw new ServiceException("接收确认记录不存在");
        }

        // 查询关联的分发明细
        LambdaQueryWrapper<InputReleaseDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InputReleaseDetail::getReleaseId, receive.getReleaseId());
        wrapper.orderByAsc(InputReleaseDetail::getCreateTime);
        List<InputReleaseDetail> details = detailMapper.selectList(wrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("main", receive);
        result.put("details", details);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmReceive(String id, String confirmBy, String confirmOrg) {
        InputReceiveWoreda receive = getById(id);
        if (receive == null) {
            throw new ServiceException("接收确认记录不存在");
        }

        if ("已确认".equals(receive.getReceiveStatus())) {
            throw new ServiceException("该记录已确认，无需重复操作");
        }

        receive.setReceiveStatus("Confirmed");
        receive.setConfirmBy(confirmBy);
        receive.setConfirmOrg(confirmOrg);
        receive.setConfirmTime(LocalDateTime.now());
        receive.setUpdateTime(LocalDateTime.now());

        return updateById(receive);
    }
}
