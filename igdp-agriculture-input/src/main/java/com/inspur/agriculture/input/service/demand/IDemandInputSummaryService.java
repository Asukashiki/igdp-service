package com.inspur.agriculture.input.service.demand;

import com.inspur.agriculture.input.dto.demand.DemandInputSummaryDTO;
import com.inspur.agriculture.input.dto.demand.DemandInputSummaryQueryDTO;
import com.inspur.agriculture.input.service.demand.impl.DemandInputSummaryServiceImpl;
import com.inspur.agriculture.input.vo.demand.DemandInputSummaryVO;

import java.util.List;

/**
 * 农资需求汇总 Service接口
 *
 * @author inspur
 * @date 2025-12-10
 */
public interface IDemandInputSummaryService {

    /**
     * 查询农资需求汇总列表
     *
     * @param queryDTO 查询条件
     * @return 农资需求汇总列表
     */
    List<DemandInputSummaryVO> getDemandInputSummaryList(DemandInputSummaryQueryDTO queryDTO);


    List<DemandInputSummaryVO> getDemandInputSummaryList1(DemandInputSummaryQueryDTO queryDTO);
    List<DemandInputSummaryVO> getDemandInputSummaryList2(DemandInputSummaryQueryDTO queryDTO);

    /**
     * 根据ID查询农资需求汇总详情
     *
     * @param id 主键ID
     * @return 农资需求汇总详情
     */
    DemandInputSummaryVO getDemandInputSummaryById(String id);

    /**
     * 添加农资需求汇总
     *
     * @param dto 农资需求汇总信息
     * @return 结果
     */
    int addDemandInputSummary(DemandInputSummaryDTO dto);

    /**
     * 更新农资需求汇总
     *
     * @param dto 农资需求汇总信息
     * @return 结果
     */
    int updateDemandInputSummary(DemandInputSummaryDTO dto);

    /**
     * 删除农资需求汇总
     *
     * @param id 主键ID
     * @return 结果
     */
    int deleteDemandInputSummary(String id);

    /**
     * 批量删除农资需求汇总
     *
     * @param ids 主键ID数组
     * @return 结果
     */
    int batchDeleteDemandInputSummary(List<String> ids);

    java.util.Map<String, DemandInputSummaryServiceImpl.CreateTaskResult> createAllMainTask(String year);
}
