package com.inspur.agriculture.input.mapper.demand;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.demand.DemandInputSummaryItem;
import com.inspur.agriculture.input.dto.demand.DemandInputSummaryItemQueryDTO;
import com.inspur.agriculture.input.dto.demand.DemandOrganDTO;
import com.inspur.agriculture.input.vo.demand.DemandInputSummaryItemVO;
import com.inspur.agriculture.input.vo.demand.InputAggregationSummaryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 农资汇聚统计 Mapper
 *
 * @author inspur
 * @date 2025-12-09
 */
public interface DemandInputSummaryItemMapper extends BaseMapper<DemandInputSummaryItem> {

    /**
     * 查询农资汇聚统计列表
     *
     * @param query 查询条件
     * @return 农资汇聚统计列表
     */
    List<DemandInputSummaryItemVO> selectDemandInputSummaryItemList(@Param("query") DemandInputSummaryItemQueryDTO query);

    /**
     * 根据ID查询农资汇聚统计详情
     *
     * @param id 主键ID
     * @return 农资汇聚统计详情
     */
    DemandInputSummaryItemVO selectDemandInputSummaryItemById(@Param("id") String id);

    /**
     * 检查农资分类和类型组合是否存在
     *
     * @param inputCategory 农资分类
     * @param inputType 农资类型
     * @param excludeId 排除的ID
     * @return 数量
     */
    int checkCategoryTypeExists(@Param("inputCategory") String inputCategory,
                                 @Param("inputType") String inputType,
                                 @Param("excludeId") String excludeId);


    List<InputAggregationSummaryVO> getInputAggregation(@Param("query") DemandOrganDTO demandOrganDTO);

    List<InputAggregationSummaryVO> getInputAggregationZone(@Param("query") DemandOrganDTO demandOrganDTO);

}
