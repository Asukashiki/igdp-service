package com.inspur.seed.service.ose;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.ose.OseBreedSeedReceiveConfirm;
import com.inspur.seed.dto.ose.OseReceiveConfirmDTO;
import com.inspur.seed.dto.ose.OseReceiveConfirmQueryDTO;
import com.inspur.seed.vo.ose.OseReceiveConfirmVO;

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
     * 确认接收
     *
     * @param receiveConfirmId 接收确认ID
     * @param dto 确认信息
     * @return 更新后的接收确认信息
     */
    OseReceiveConfirmVO confirmReceive(String receiveConfirmId, OseReceiveConfirmDTO dto);
}
