package com.inspur.seed.Institution.ose.service;

import com.inspur.seed.Institution.ose.domain.dto.OseInfoDTO;
import com.inspur.seed.Institution.ose.domain.dto.OseInfoQueryDTO;
import com.inspur.seed.Institution.ose.domain.vo.OseInfoVO;

import java.util.List;

/**
 * OSE基础信息Service接口
 *
 * @author igdp
 */
public interface IOseInfoService {

    /**
     * 查询OSE列表
     *
     * @param queryDTO 查询条件
     * @return OSE列表
     */
    List<OseInfoVO> getOseList(OseInfoQueryDTO queryDTO);

    /**
     * 查询OSE详情
     *
     * @param oseId OSE ID
     * @return OSE详情
     */
    OseInfoVO getOseById(String oseId);

    /**
     * 新增OSE信息
     *
     * @param dto OSE信息DTO
     * @return 新增的OSE信息
     */
    OseInfoVO addOse(OseInfoDTO dto);

    /**
     * 更新OSE信息
     *
     * @param oseId OSE ID
     * @param dto OSE信息DTO
     * @return 更新后的OSE信息
     */
    OseInfoVO updateOse(String oseId, OseInfoDTO dto);

    /**
     * 删除OSE信息
     *
     * @param oseId OSE ID
     * @return 删除结果
     */
    int deleteOse(String oseId);
}
