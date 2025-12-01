package com.inspur.seed.mapper.ose;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.ose.OseBreedSeedReceiveConfirm;
import com.inspur.seed.dto.ose.OseReceiveConfirmQueryDTO;
import com.inspur.seed.vo.ose.OseReceiveConfirmVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * OSE接收确认Mapper接口
 *
 * @author igdp
 */
public interface OseReceiveConfirmMapper extends BaseMapper<OseBreedSeedReceiveConfirm> {

    /**
     * 查询接收确认列表
     *
     * @param queryDTO 查询条件
     * @return 接收确认列表
     */
    List<OseReceiveConfirmVO> selectReceiveConfirmList(@Param("query") OseReceiveConfirmQueryDTO queryDTO);

    /**
     * 根据ID查询接收确认详情
     *
     * @param receiveConfirmId 接收确认ID
     * @return 接收确认详情
     */
    OseReceiveConfirmVO selectReceiveConfirmById(@Param("receiveConfirmId") String receiveConfirmId);
}
