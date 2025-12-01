package com.inspur.seed.service.ose.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.inspur.common.exception.ServiceException;
import com.inspur.seed.domain.ose.OseBreedSeedReceiveConfirm;
import com.inspur.seed.dto.ose.OseReceiveConfirmDTO;
import com.inspur.seed.dto.ose.OseReceiveConfirmQueryDTO;
import com.inspur.seed.mapper.ose.OseReceiveConfirmMapper;
import com.inspur.seed.service.ose.IOseReceiveConfirmService;
import com.inspur.seed.vo.ose.OseReceiveConfirmVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * OSE接收确认Service实现
 *
 * @author igdp
 */
@Service
public class OseReceiveConfirmServiceImpl implements IOseReceiveConfirmService {

    @Autowired
    private OseReceiveConfirmMapper receiveConfirmMapper;

    @Override
    public List<OseReceiveConfirmVO> getReceiveConfirmList(OseReceiveConfirmQueryDTO queryDTO) {
        return receiveConfirmMapper.selectReceiveConfirmList(queryDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OseReceiveConfirmVO confirmReceive(String receiveConfirmId, OseReceiveConfirmDTO dto) {
        // 查询接收确认记录
        OseBreedSeedReceiveConfirm confirm = receiveConfirmMapper.selectById(receiveConfirmId);
        if (confirm == null) {
            throw new ServiceException("接收确认记录不存在");
        }

        // 验证状态
        if ("CONFIRMED".equals(confirm.getReceiveStatus())) {
            throw new ServiceException("该记录已确认，不能重复确认");
        }

        // 更新确认信息
        UpdateWrapper<OseBreedSeedReceiveConfirm> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("receive_confirm_id", receiveConfirmId);
        updateWrapper.set("confirm_time", new Date());
        updateWrapper.set("confirm_people", dto.getConfirmPeople());
        updateWrapper.set("receive_status", "CONFIRMED");
        updateWrapper.set("remark", dto.getRemark());
        updateWrapper.set("update_time", new Date());

        receiveConfirmMapper.update(null, updateWrapper);

        return receiveConfirmMapper.selectReceiveConfirmById(receiveConfirmId);
    }
}
