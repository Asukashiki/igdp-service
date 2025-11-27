package com.inspur.seed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.SeedVarietyQueryRecord;

import java.util.List;
import java.util.Map;

/**
 * 种子品种查询记录服务接口
 *
 * @author system
 */
public interface ISeedVarietyQueryRecordService extends IService<SeedVarietyQueryRecord> {

    /**
     * 查询品种公示列表
     *
     * @param varietyName 品种名称
     * @param year 发布年度
     * @param cropType 作物类型
     * @return 品种公示列表
     */
    List<Map<String, Object>> queryVarietyPublicList(String varietyName, String year, String cropType);

    /**
     * 查询品种详情
     *
     * @param publishId 品种发布ID
     * @return 品种详情
     */
    Map<String, Object> queryVarietyDetail(String publishId);

    /**
     * 记录查询行为
     *
     * @param queryKeyword 查询关键词
     * @param ipAddress IP地址
     * @param queryResultCount 查询结果数量
     * @param viewedPublishId 查看的发布ID
     * @return 查询记录ID
     */
    String recordQueryBehavior(String queryKeyword, String ipAddress, Integer queryResultCount, String viewedPublishId);
}
