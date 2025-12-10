package com.inspur.agriculture.input.mapper.demand;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.demand.DemandInputSummary;
import com.inspur.agriculture.input.dto.demand.DemandInputSummaryQueryDTO;
import com.inspur.agriculture.input.vo.demand.DemandInputSummaryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 农资需求汇总 Mapper
 *
 * @author inspur
 * @date 2025-12-10
 */
public interface DemandInputSummaryMapper extends BaseMapper<DemandInputSummary> {

    /**
     * 查询农资需求汇总列表
     *
     * @param query 查询条件
     * @return 农资需求汇总列表
     */
    List<DemandInputSummaryVO> selectDemandInputSummaryList(@Param("query") DemandInputSummaryQueryDTO query);

    /**
     * 根据ID查询农资需求汇总详情
     *
     * @param id 主键ID
     * @return 农资需求汇总详情
     */
    DemandInputSummaryVO selectDemandInputSummaryById(@Param("id") String id);
}
