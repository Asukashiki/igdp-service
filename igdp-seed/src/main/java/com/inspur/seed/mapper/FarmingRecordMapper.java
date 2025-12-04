package com.inspur.seed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.FarmingRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 农事记录Mapper接口
 *
 * @author inspur
 */
@Mapper
public interface FarmingRecordMapper extends BaseMapper<FarmingRecord> {

    /**
     * 查询农事记录列表
     *
     * @param farmingRecord 查询条件
     * @return 农事记录列表
     */
    List<FarmingRecord> selectFarmingRecordList(FarmingRecord farmingRecord);

    /**
     * 根据ID查询农事记录详情
     *
     * @param farmingId 农事记录ID
     * @return 农事记录
     */
    FarmingRecord selectFarmingRecordById(@Param("farmingId") String farmingId);

    /**
     * 查询指定地块下的最大记录编号
     *
     * @param plotId 地块ID
     * @return 最大记录编号
     */
    int getMaxRecordNoByPlotId(@Param("plotId") String plotId);
}
