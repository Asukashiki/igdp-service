package com.inspur.seed.service;

import com.inspur.seed.domain.FarmingRecord;

import java.util.List;

/**
 * 农事记录Service接口
 *
 * @author inspur
 */
public interface IFarmingRecordService {

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
    FarmingRecord selectFarmingRecordById(String farmingId);

    /**
     * 新增农事记录
     *
     * @param farmingRecord 农事记录
     * @return 农事记录ID
     */
    String insertFarmingRecord(FarmingRecord farmingRecord);

    /**
     * 修改农事记录
     *
     * @param farmingRecord 农事记录
     * @return 影响行数
     */
    int updateFarmingRecord(FarmingRecord farmingRecord);

    /**
     * 批量删除农事记录
     *
     * @param farmingIds 农事记录ID数组
     * @return 影响行数
     */
    int deleteFarmingRecordByIds(String[] farmingIds);
}
