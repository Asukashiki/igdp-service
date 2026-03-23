package com.inspur.seed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.entity.DemandFarmerInputItem;
import com.inspur.seed.domain.vo.FarmerInputAggregationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Demand Farmer Input Item Mapper
 *
 * @author igdp
 * @date 2025-12-04
 */
@Mapper
public interface DemandFarmerInputItemMapper extends BaseMapper<DemandFarmerInputItem> {

    /**
     * Get aggregated statistics of farmer input items
     *
     * @return List of aggregated input items grouped by category and type
     */
    @Select("SELECT " +
            "    i.input_category AS inputCategory, " +  // 别名对应DTO属性名（驼峰命名）
            "    i.input_type AS inputType, " +
            "    i.variety AS variety, " +
            "    COUNT(*) AS totalCount, " +
            "    SUM(i.quantity) AS totalQuantity, " +
            "    d.farmer_name "+
            "FROM demand_farmer_input_item i, demand_farmer_detail d " +
            "WHERE " +
            "    i.demand_id = d.id " +
            "    AND d.kebele = #{kebele} " +
            "    AND d.year = #{year}" +
            "    AND d.status = '2' " +       // 字符串常量用单引号
            "    AND i.is_deleted = 0 " +
            "GROUP BY i.input_category, i.input_type")
    List<FarmerInputAggregationVO> getInputAggregation(@Param("kebele") String kebele,@Param("year") String year);
}
