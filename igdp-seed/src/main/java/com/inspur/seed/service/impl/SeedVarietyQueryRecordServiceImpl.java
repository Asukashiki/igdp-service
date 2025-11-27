package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.uuid.IdUtils;
import com.inspur.seed.domain.SeedVarietyQueryRecord;
import com.inspur.seed.domain.VarietyPublish;
import com.inspur.seed.domain.VarietyRegistration;
import com.inspur.seed.mapper.SeedVarietyQueryRecordMapper;
import com.inspur.seed.mapper.VarietyPublishMapper;
import com.inspur.seed.mapper.VarietyRegistrationMapper;
import com.inspur.seed.service.ISeedVarietyQueryRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 种子品种查询记录服务实现类
 *
 * @author system
 */
@Service
public class SeedVarietyQueryRecordServiceImpl extends ServiceImpl<SeedVarietyQueryRecordMapper, SeedVarietyQueryRecord> implements ISeedVarietyQueryRecordService {

    @Resource
    private VarietyPublishMapper varietyPublishMapper;

    @Resource
    private VarietyRegistrationMapper varietyRegistrationMapper;

    @Override
    public List<Map<String, Object>> queryVarietyPublicList(String varietyName, String year, String cropType) {
        // 查询品种发布信息
        LambdaQueryWrapper<VarietyPublish> wrapper = new LambdaQueryWrapper<>();

        // 公示状态为公示中
        wrapper.eq(VarietyPublish::getPublishStatus, 1);

        // 品种名称模糊查询
        if (StringUtils.isNotEmpty(varietyName)) {
            wrapper.like(VarietyPublish::getVarietyName, varietyName);
        }

        // 作物类型筛选
        if (StringUtils.isNotEmpty(cropType)) {
            wrapper.eq(VarietyPublish::getCropType, cropType);
        }

        // 按发布时间倒序排列
        wrapper.orderByDesc(VarietyPublish::getPublishTime);

        List<VarietyPublish> publishList = varietyPublishMapper.selectList(wrapper);

        // 组装返回数据
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (VarietyPublish publish : publishList) {
            // 根据年度筛选（如果提供）
            if (StringUtils.isNotEmpty(year)) {
                if (publish.getPublishDate() == null || !publish.getPublishDate().toString().startsWith(year)) {
                    continue;
                }
            }

            // 查询关联的登记信息
            VarietyRegistration registration = varietyRegistrationMapper.selectById(publish.getRegistrationId());
            if (registration != null) {
                Map<String, Object> item = new HashMap<>();
                item.put("publishId", publish.getPublishId());
                item.put("varietyName", publish.getVarietyName());
                item.put("varietyType", publish.getCropType());
                item.put("enterpriseName", registration.getEnterpriseName());
                item.put("registerNo", registration.getRegistrationNo());
                resultList.add(item);
            }
        }

        return resultList;
    }

    @Override
    public Map<String, Object> queryVarietyDetail(String publishId) {
        // 查询品种发布信息
        VarietyPublish publish = varietyPublishMapper.selectById(publishId);
        if (publish == null) {
            throw new ServiceException("品种发布信息不存在");
        }

        // 查询关联的登记信息
        VarietyRegistration registration = varietyRegistrationMapper.selectById(publish.getRegistrationId());
        if (registration == null) {
            throw new ServiceException("品种登记信息不存在");
        }

        // 组装返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("publishId", publish.getPublishId());
        result.put("varietyName", publish.getVarietyName());
        result.put("varietyType", publish.getCropType());
        result.put("enterpriseName", registration.getEnterpriseName());
        result.put("registerNo", registration.getRegistrationNo());
        result.put("baseInfo", publish.getPublicDescription());
        result.put("photoUrl", registration.getPhotoUrl());

        return result;
    }

    @Override
    public String recordQueryBehavior(String queryKeyword, String ipAddress, Integer queryResultCount, String viewedPublishId) {
        // 生成查询记录ID
        String queryId = "QUERY" + IdUtils.fastSimpleUUID().substring(0, 16).toUpperCase();

        // 创建查询记录对象
        SeedVarietyQueryRecord record = new SeedVarietyQueryRecord();
        record.setQueryId(queryId);
        record.setQueryKeyword(queryKeyword);
        record.setQueryTime(LocalDateTime.now());
        record.setIpAddress(ipAddress);
        record.setQueryResultCount(queryResultCount);
        record.setViewedPublishId(viewedPublishId);

        // 保存查询记录
        save(record);

        return queryId;
    }
}
