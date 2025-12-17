package com.inspur.seed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.entity.DemandFarmerDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Demand Farmer Detail Mapper
 *
 * @author igdp
 * @date 2025-12-04
 */
@Mapper
public interface DemandFarmerDetailMapper extends BaseMapper<DemandFarmerDetail> {

    @Select("select count(*) from demand_farmer_detail where is_deleted = '0' and status = 2 or 3 and year = #{year} and kebele = #{kebele}")
    int countAuditQuantity(String year, String kebele);

    @Select("select count(*) from demand_farmer_detail where is_deleted = '0' and status = 1 and year = #{year} and kebele = #{kebele}")
    int countSubmitQuantity(String year, String kebele);

}
