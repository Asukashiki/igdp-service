package com.inspur.agriculture.input.service.demand;

import com.inspur.agriculture.input.dto.demand.DemandInputSummaryItemDTO;
import com.inspur.agriculture.input.dto.demand.DemandInputSummaryItemQueryDTO;
import com.inspur.agriculture.input.dto.demand.DemandOrganDTO;
import com.inspur.agriculture.input.vo.demand.DemandInputSummaryItemVO;
import com.inspur.agriculture.input.vo.demand.InputAggregationSummaryVO;

import java.util.List;

/**
 * 农资汇聚统计 Service接口
 *
 * @author inspur
 * @date 2025-12-09
 */
public interface IDemandInputSummaryItemService {

    /**
     * 查询农资汇聚统计列表
     *
     * @param queryDTO 查询条件
     * @return 农资汇聚统计列表
     */
    List<DemandInputSummaryItemVO> getDemandInputSummaryItemList(DemandInputSummaryItemQueryDTO queryDTO);

    /**
     * 根据ID查询农资汇聚统计详情
     *
     * @param id 主键ID
     * @return 农资汇聚统计详情
     */
    DemandInputSummaryItemVO getDemandInputSummaryItemById(String id);

    /**
     * 添加农资汇聚统计
     *
     * @param dto 农资汇聚统计信息
     * @return 结果
     */
    int addDemandInputSummaryItem(DemandInputSummaryItemDTO dto);

    /**
     * 更新农资汇聚统计
     *
     * @param dto 农资汇聚统计信息
     * @return 结果
     */
    int updateDemandInputSummaryItem(DemandInputSummaryItemDTO dto);

    /**
     * 删除农资汇聚统计
     *
     * @param id 主键ID
     * @return 结果
     */
    int deleteDemandInputSummaryItem(String id);

    int deleteDeandInputItemBySummaryId(String summaryId);

    /**
     * 批量删除农资汇聚统计
     *
     * @param ids 主键ID数组
     * @return 结果
     */
    int batchDeleteDemandInputSummaryItem(List<String> ids);

    int submitInputAggregation(DemandOrganDTO demanOrganDTO);

    List<InputAggregationSummaryVO> getInputAggregation(DemandOrganDTO demandOrganDTO);
}
