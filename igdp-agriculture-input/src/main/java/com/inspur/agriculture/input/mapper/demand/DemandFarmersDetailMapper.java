package com.inspur.agriculture.input.mapper.demand;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.controller.demand.DemandFarmerDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DemandFarmersDetailMapper extends BaseMapper<DemandFarmerDetail> {

    @Select("select count(*) from demand_farmer_detail where is_deleted = '0' and status in (2, 3)  and year = #{year} and kebele = #{kebele}")
    int countAuditQuantity(@Param("year")String year, @Param("kebele") String kebele);

    @Select("select count(*) from demand_farmer_detail where is_deleted = '0' and status = 1 and year = #{year} and kebele = #{kebele}")
    int countSubmitQuantity(@Param("year")String year, @Param("kebele") String kebele);

    @Select("select count(*) from demand_farmer_detail where is_deleted = '0' and year = #{year} and kebele = #{kebele}")
    int countAllQuantity(@Param("year")String year, @Param("kebele") String kebele);

    @Select("select count(*) from demand_farmer_detail where is_deleted = '0' and status = '0' and year = #{year} and kebele = #{kebele}")
    int countUnsubmitQuantity(@Param("year")String year, @Param("kebele") String kebele);

}
