package com.inspur.seed.multiplication.oseReceive.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.multiplication.oseReceive.domain.entity.OseBreedSeedReceiveConfirm;
import com.inspur.seed.multiplication.oseReceive.domain.vo.OseReceiveConfirmVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * OSE接收确认Mapper接口
 *
 * @author igdp
 */
@Mapper
public interface OseReceiveConfirmMapper extends BaseMapper<OseBreedSeedReceiveConfirm> {



    /**
     * 根据ID查询接收确认详情
     *
     * @param receiveConfirmId 接收确认ID
     * @return 接收确认详情
     */
    OseReceiveConfirmVO selectReceiveConfirmById(@Param("receiveConfirmId") String receiveConfirmId);
}
