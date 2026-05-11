package com.inspur.agriculture.input.mapper.demand;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.demand.DemandInputSummaryAdjustmentHistory;
import com.inspur.agriculture.input.vo.demand.DemandSummaryAdjustmentHistoryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DemandInputSummaryAdjustmentHistoryMapper extends BaseMapper<DemandInputSummaryAdjustmentHistory> {

    List<DemandSummaryAdjustmentHistoryVO> selectHistoryByDetailId(@Param("detailId") String detailId);
}
