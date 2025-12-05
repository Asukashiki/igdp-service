package com.inspur.agriculture.input.service.allocate;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.agriculture.input.domain.allocate.dto.StateAnnualQuotaAddDTO;
import com.inspur.agriculture.input.domain.allocate.dto.StateAnnualQuotaDeleteDTO;
import com.inspur.agriculture.input.domain.allocate.dto.StateAnnualQuotaUpdateDTO;
import com.inspur.agriculture.input.domain.allocate.entity.StateAnnualQuota;
import com.inspur.agriculture.input.domain.allocate.vo.StateAnnualQuotaVO;

/**
 * State Annual Quota Service
 * 州级年度配额服务接口
 */
public interface StateAnnualQuotaService extends IService<StateAnnualQuota> {

    /**
     * Add state annual quota
     *
     * @param dto Add DTO
     * @return StateAnnualQuotaVO
     */
    StateAnnualQuotaVO add(StateAnnualQuotaAddDTO dto);

    /**
     * Page query state annual quota
     *
     * @param pageNum Page number
     * @param pageSize Page size
     * @param year Year filter
     * @param categoryId Category ID filter
     * @param operatorDivisionId Operator division ID filter
     * @return IPage result
     */
    IPage<StateAnnualQuotaVO> page(Integer pageNum, Integer pageSize, Integer year,
                                   String categoryId, String operatorDivisionId);

    /**
     * Get state annual quota detail
     *
     * @param quotaId Quota ID
     * @param operatorDivisionId Operator division ID
     * @return StateAnnualQuotaVO
     */
    StateAnnualQuotaVO detail(String quotaId, String operatorDivisionId);

    /**
     * Update state annual quota
     *
     * @param dto Update DTO
     * @return StateAnnualQuotaVO
     */
    StateAnnualQuotaVO update(StateAnnualQuotaUpdateDTO dto);

    /**
     * Delete state annual quota
     *
     * @param dto Delete DTO
     * @return Delete result
     */
    Boolean delete(StateAnnualQuotaDeleteDTO dto);
}
