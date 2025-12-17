package com.inspur.agriculture.input.mapper.demand;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.demand.DemandInputSummary;
import com.inspur.agriculture.input.dto.demand.DemandInputSummaryQueryDTO;
import com.inspur.agriculture.input.vo.demand.DemandInputSummaryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DemandInputSummaryMapper extends BaseMapper<DemandInputSummary> {

    // 原有方法：完全保留
    List<DemandInputSummaryVO> selectDemandInputSummaryList(@Param("query") DemandInputSummaryQueryDTO query);
    List<DemandInputSummaryVO> selectDemandInputSummaryList1(@Param("query") DemandInputSummaryQueryDTO query);
    DemandInputSummaryVO selectDemandInputSummaryById(@Param("id") String id);
    
    List<DemandInputSummaryVO> selectDemandInputSummaryList2(@Param("query") DemandInputSummaryQueryDTO query);
}