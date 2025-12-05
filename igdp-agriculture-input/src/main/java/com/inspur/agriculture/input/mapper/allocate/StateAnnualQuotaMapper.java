package com.inspur.agriculture.input.mapper.allocate;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.agriculture.input.domain.allocate.entity.StateAnnualQuota;
import com.inspur.agriculture.input.domain.allocate.vo.StateAnnualQuotaVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * State Annual Quota Mapper
 * 州级年度配额Mapper接口
 */
@Mapper
public interface StateAnnualQuotaMapper extends BaseMapper<StateAnnualQuota> {

    /**
     * Page query state annual quota with associated data
     *
     * @param page Page object
     * @param year Year filter
     * @param categoryId Category ID filter
     * @param operatorDivisionId Operator division ID filter
     * @return Page result
     */
    IPage<StateAnnualQuotaVO> selectPageWithDetail(Page<StateAnnualQuotaVO> page,
                                                    @Param("year") Integer year,
                                                    @Param("categoryId") String categoryId,
                                                    @Param("operatorDivisionId") String operatorDivisionId);

    /**
     * Get state annual quota detail by quota ID
     *
     * @param quotaId Quota ID
     * @param operatorDivisionId Operator division ID
     * @return StateAnnualQuotaVO
     */
    StateAnnualQuotaVO selectDetailById(@Param("quotaId") String quotaId,
                                        @Param("operatorDivisionId") String operatorDivisionId);
}
