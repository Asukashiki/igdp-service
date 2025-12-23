package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.uuid.IdUtils;
import com.inspur.seed.domain.SeedVarietyQueryRecord;
import com.inspur.seed.domain.VarietyPublish;
import com.inspur.seed.domain.VarietyRegistration;
import com.inspur.seed.domain.entity.BreedingLicense;
import com.inspur.seed.mapper.BreedingLicenseMapper;
import com.inspur.seed.mapper.SeedVarietyQueryRecordMapper;
import com.inspur.seed.mapper.VarietyPublishMapper;
import com.inspur.seed.mapper.VarietyRegistrationMapper;
import com.inspur.seed.service.ISeedVarietyQueryRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Resource
    private BreedingLicenseMapper breedingLicenseMapper;

    @Override
    public List<Map<String, Object>> queryVarietyPublicList(String varietyName, String year, String cropType) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        
        // ==================== 1. 查询登记公示数据 ====================
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

        for (VarietyPublish publish : publishList) {
            // 根据年度筛选
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
                item.put("dataType", "registration");  // 登记公示
                item.put("licenseNo", null);
                item.put("approvalOrg", null);
                item.put("approvalDate", null);
                item.put("validStartDate", null);
                item.put("validEndDate", null);
                resultList.add(item);
            }
        }

        // ==================== 2. 查询育种许可数据（自动公示） ====================
        LambdaQueryWrapper<BreedingLicense> licenseWrapper = new LambdaQueryWrapper<>();
        // 只查询有效状态的许可
        licenseWrapper.eq(BreedingLicense::getLicenseStatus, "valid");
        licenseWrapper.eq(BreedingLicense::getDeleted, "0");
        
        // 品种名称模糊查询
        if (StringUtils.isNotEmpty(varietyName)) {
            licenseWrapper.like(BreedingLicense::getVarietyName, varietyName);
        }
        // 作物类型筛选
        if (StringUtils.isNotEmpty(cropType)) {
            licenseWrapper.eq(BreedingLicense::getCropType, cropType);
        }
        // 按创建时间倒序排列
        licenseWrapper.orderByDesc(BreedingLicense::getCreatedTime);

        List<BreedingLicense> licenseList = breedingLicenseMapper.selectList(licenseWrapper);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (BreedingLicense license : licenseList) {
            // 根据年度筛选
            if (StringUtils.isNotEmpty(year)) {
                if (license.getApprovalDate() == null || !license.getApprovalDate().toString().startsWith(year)) {
                    continue;
                }
            }

            Map<String, Object> item = new HashMap<>();
            // 使用 LICENSE_ 前缀区分许可数据
            item.put("publishId", "LICENSE_" + license.getId());
            item.put("varietyName", license.getVarietyName());
            item.put("varietyType", license.getCropType());
            item.put("enterpriseName", license.getApprovalOrg());  // 审批机构作为企业名称显示
            item.put("registerNo", license.getLicenseNo());  // 许可证号作为登记号显示
            item.put("dataType", "license");  // 自动公示（许可）
            item.put("licenseNo", license.getLicenseNo());
            item.put("approvalOrg", license.getApprovalOrg());
            item.put("approvalDate", license.getApprovalDate() != null ? license.getApprovalDate().format(dateFormatter) : null);
            item.put("validStartDate", license.getValidStartDate() != null ? license.getValidStartDate().format(dateFormatter) : null);
            item.put("validEndDate", license.getValidEndDate() != null ? license.getValidEndDate().format(dateFormatter) : null);
            item.put("batchName", license.getBatchName());
            item.put("datasetCode", license.getDatasetCode());
            resultList.add(item);
        }

        return resultList;
    }

    @Override
    public Map<String, Object> queryVarietyDetail(String publishId) {
        Map<String, Object> result = new HashMap<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        // ==================== 判断数据类型 ====================
        if (publishId != null && publishId.startsWith("LICENSE_")) {
            // 许可数据详情
            String licenseId = publishId.substring(8);  // 去掉 LICENSE_ 前缀
            BreedingLicense license = breedingLicenseMapper.selectById(licenseId);
            if (license == null) {
                throw new ServiceException("许可信息不存在");
            }
            
            result.put("publishId", publishId);
            result.put("dataType", "license");
            result.put("varietyName", license.getVarietyName());
            result.put("varietyType", license.getCropType());
            result.put("enterpriseName", license.getApprovalOrg());
            result.put("registerNo", license.getLicenseNo());
            result.put("licenseNo", license.getLicenseNo());
            result.put("approvalOrg", license.getApprovalOrg());
            result.put("approvalDate", license.getApprovalDate() != null ? license.getApprovalDate().format(dateFormatter) : null);
            result.put("validStartDate", license.getValidStartDate() != null ? license.getValidStartDate().format(dateFormatter) : null);
            result.put("validEndDate", license.getValidEndDate() != null ? license.getValidEndDate().format(dateFormatter) : null);
            result.put("batchName", license.getBatchName());
            result.put("batchId", license.getBatchId());
            result.put("datasetCode", license.getDatasetCode());
            result.put("datasetId", license.getDatasetId());
            result.put("certificateFile", license.getCertificateFile());
            result.put("certificateFileName", license.getCertificateFileName());
            result.put("licenseStatus", license.getLicenseStatus());
            result.put("remark", license.getRemark());
            result.put("createdTime", license.getCreatedTime() != null ? license.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null);
            result.put("createdByName", license.getCreatedByName());
            result.put("baseInfo", license.getRemark());  // 备注作为描述信息
            result.put("photoUrl", null);
        } else {
            // 登记公示数据详情
            VarietyPublish publish = varietyPublishMapper.selectById(publishId);
            if (publish == null) {
                throw new ServiceException("品种发布信息不存在");
            }

            VarietyRegistration registration = varietyRegistrationMapper.selectById(publish.getRegistrationId());
            if (registration == null) {
                throw new ServiceException("品种登记信息不存在");
            }

            result.put("publishId", publish.getPublishId());
            result.put("dataType", "registration");
            result.put("varietyName", publish.getVarietyName());
            result.put("varietyType", publish.getCropType());
            result.put("enterpriseName", registration.getEnterpriseName());
            result.put("registerNo", registration.getRegistrationNo());
            result.put("baseInfo", publish.getPublicDescription());
            result.put("photoUrl", registration.getPhotoUrl());
            result.put("publishDate", publish.getPublishDate() != null ? publish.getPublishDate().format(dateFormatter) : null);
            result.put("publishDept", publish.getPublishDept());
            result.put("recommendedRegion", publish.getRecommendedRegion());
            result.put("sowingGuide", publish.getSowingGuide());
            result.put("decisionExplanation", publish.getDecisionExplanation());
            // 许可相关字段置空
            result.put("licenseNo", null);
            result.put("approvalOrg", null);
            result.put("approvalDate", null);
            result.put("validStartDate", null);
            result.put("validEndDate", null);
        }

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
