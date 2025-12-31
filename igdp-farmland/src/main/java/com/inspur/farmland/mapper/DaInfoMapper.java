package com.inspur.farmland.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.farmland.domain.DaInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.math.BigDecimal;

/**
 * DA信息Mapper接口
 *
 * @author inspur
 */
@Mapper
public interface DaInfoMapper extends BaseMapper<DaInfo> {

    @Select("SELECT COUNT(*) FROM t_farmer_info WHERE da_id = #{daId} AND status = '1'")
    Long selectFarmerCountByDaId(@Param("daId") String daId);

    @Select("SELECT COUNT(l.id) FROM t_land_info l LEFT JOIN t_farmer_info f ON l.farmer_id = f.farmer_id WHERE f.da_id = #{daId} AND l.status = '1' AND f.status = '1'")
    Long selectLandCountByDaId(@Param("daId") String daId);

    @Select("SELECT IFNULL(SUM(l.area_size), 0) FROM t_land_info l LEFT JOIN t_farmer_info f ON l.farmer_id = f.farmer_id WHERE f.da_id = #{daId} AND l.status = '1' AND f.status = '1'")
    BigDecimal selectLandAreaByDaId(@Param("daId") String daId);

}
