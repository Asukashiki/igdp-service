package com.inspur.seed.mapper.ose;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.ose.OseInfo;
import com.inspur.seed.dto.ose.OseInfoQueryDTO;
import com.inspur.seed.vo.ose.OseInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * OSE基础信息Mapper接口
 *
 * @author igdp
 */
public interface OseInfoMapper extends BaseMapper<OseInfo> {

    /**
     * 查询OSE列表
     *
     * @param queryDTO 查询条件
     * @return OSE列表
     */
    List<OseInfoVO> selectOseList(@Param("query") OseInfoQueryDTO queryDTO);

    /**
     * 根据ID查询OSE详情
     *
     * @param oseId OSE ID
     * @return OSE详情
     */
    OseInfoVO selectOseById(@Param("oseId") String oseId);
}
