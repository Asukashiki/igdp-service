package com.inspur.seed.multiplication.oseReceive.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.multiplication.oseReceive.domain.entity.OseBreedSeedReceiveConfirm;
import com.inspur.seed.multiplication.oseReceive.domain.dto.OseReceiveConfirmDTO;
import com.inspur.seed.multiplication.oseReceive.domain.dto.OseReceiveConfirmQueryDTO;
import com.inspur.seed.multiplication.oseReceive.domain.vo.OseReceiveConfirmVO;

import java.util.List;

/**
 * OSE接收确认Service接口
 *
 * @author igdp
 */
public interface IOseReceiveConfirmService extends IService<OseBreedSeedReceiveConfirm> {

    /**
     * 查询接收确认列表
     *
     * @param queryDTO 查询条件
     * @return 接收确认列表
     */
    List<OseReceiveConfirmVO> getReceiveConfirmList(OseReceiveConfirmQueryDTO queryDTO);

    /**
     * 根据ID查询接收确认详情
     *
     * @param receiveConfirmId 接收确认ID
     * @return 接收确认详情
     */
    OseReceiveConfirmVO getReceiveConfirmDetail(String receiveConfirmId);

    /**
     * 确认接收
     *
     * @param receiveConfirmId 接收确认ID
     * @param dto 确认信息
     * @return 更新后的接收确认信息
     */
    OseReceiveConfirmVO confirmReceive(String receiveConfirmId, OseReceiveConfirmDTO dto);
}
