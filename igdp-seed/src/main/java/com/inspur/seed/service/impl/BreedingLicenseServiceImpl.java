package com.inspur.seed.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.dto.BreedingLicenseDTO;
import com.inspur.seed.domain.dto.BreedingLicenseQueryDTO;
import com.inspur.seed.domain.entity.BreedingDataset;
import com.inspur.seed.domain.entity.BreedingLicense;
import com.inspur.seed.domain.entity.BreedingVarietyTraits;
import com.inspur.seed.domain.EnterpriseInfo;
import com.inspur.seed.domain.VarietyRegistration;
import com.inspur.seed.domain.VarietyPublish;
import com.inspur.seed.domain.vo.BreedingLicenseVO;
import com.inspur.seed.mapper.BreedingDatasetMapper;
import com.inspur.seed.mapper.BreedingLicenseMapper;
import com.inspur.seed.mapper.BreedingVarietyTraitsMapper;
import com.inspur.seed.service.IBreedingLicenseService;
import com.inspur.seed.service.IEnterpriseCertifyService;
import com.inspur.seed.service.IVarietyRegistrationService;
import com.inspur.seed.service.IVarietyPublishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 育种许可Service实现
 *
 * @author system
 * @since 2025-01-30
 */
@Slf4j
@Service
public class BreedingLicenseServiceImpl implements IBreedingLicenseService {

    @Autowired
    private BreedingLicenseMapper licenseMapper;

    @Autowired
    private BreedingVarietyTraitsMapper traitsMapper;

    @Autowired
    private BreedingDatasetMapper datasetMapper;

    @Autowired
    private IEnterpriseCertifyService enterpriseCertifyService;

    @Autowired
    private IVarietyRegistrationService varietyRegistrationService;

    @Autowired
    private IVarietyPublishService varietyPublishService;

    /**
     * 获取许可列表(分页)
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @Override
    public AjaxResult getLicenseList(BreedingLicenseQueryDTO queryDTO) {
        try {
            // 构建查询条件
            LambdaQueryWrapper<BreedingLicense> wrapper = new LambdaQueryWrapper<>();
            // 只查询未删除的记录（兼容 NULL 和 "0"）
            wrapper.and(w -> w.eq(BreedingLicense::getDeleted, "0").or().isNull(BreedingLicense::getDeleted));

            // 关键词搜索(许可证号、批次名称)
            if (StrUtil.isNotBlank(queryDTO.getKeyword())) {
                wrapper.and(w -> w.like(BreedingLicense::getLicenseNo, queryDTO.getKeyword())
                        .or().like(BreedingLicense::getBatchName, queryDTO.getKeyword()));
            }

            // 许可状态
            if (StrUtil.isNotBlank(queryDTO.getLicenseStatus())) {
                // 如果查询状态为"expired"（已过期），需要筛选有效期结束日期小于当前日期的数据
                if ("expired".equals(queryDTO.getLicenseStatus())) {
                    wrapper.lt(BreedingLicense::getValidEndDate, LocalDate.now());
                }
               /* else {
                    wrapper.eq(BreedingLicense::getLicenseStatus, queryDTO.getLicenseStatus());
                }*/
            }

            // 批次ID
            if (StrUtil.isNotBlank(queryDTO.getBatchId())) {
                wrapper.eq(BreedingLicense::getBatchId, queryDTO.getBatchId());
            }

            // 审批日期范围
            if (StrUtil.isNotBlank(queryDTO.getApprovalDateStart())) {
                wrapper.ge(BreedingLicense::getApprovalDate, queryDTO.getApprovalDateStart());
            }
            if (StrUtil.isNotBlank(queryDTO.getApprovalDateEnd())) {
                wrapper.le(BreedingLicense::getApprovalDate, queryDTO.getApprovalDateEnd());
            }

            // 按创建时间倒序
            wrapper.orderByDesc(BreedingLicense::getCreatedTime);

            // 分页查询
            Page<BreedingLicense> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
            IPage<BreedingLicense> pageResult = licenseMapper.selectPage(page, wrapper);

            // 转换为VO并关联物种特性
            List<BreedingLicenseVO> voList = new ArrayList<>();
            for (BreedingLicense license : pageResult.getRecords()) {
                BreedingLicenseVO vo = BeanUtil.copyProperties(license, BreedingLicenseVO.class);

                // 查询关联的物种特性（只查询未删除的）
                LambdaQueryWrapper<BreedingVarietyTraits> traitsWrapper = new LambdaQueryWrapper<>();
                traitsWrapper.eq(BreedingVarietyTraits::getLicenseId, license.getId());
                traitsWrapper.and(w -> w.eq(BreedingVarietyTraits::getDeleted, "0").or().isNull(BreedingVarietyTraits::getDeleted));
                BreedingVarietyTraits traits = traitsMapper.selectOne(traitsWrapper);
                if (traits != null) {
                    // 保存 traits 的 ID
                    String traitsId = traits.getId();
                    // 复制 traits 的其他属性到 vo（会覆盖 id）
                    BeanUtil.copyProperties(traits, vo);
                    // 恢复 license 的 ID（重要！）
                    vo.setId(license.getId());
                    // 设置 traits 的 ID 到专门的字段
                    vo.setTraitsId(traitsId);
                }

                voList.add(vo);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("list", voList);
            result.put("total", pageResult.getTotal());
            result.put("pageNum", pageResult.getCurrent());
            result.put("pageSize", pageResult.getSize());

            return AjaxResult.success(result);
        } catch (Exception e) {
            log.error("Failed to get license list", e);
            return AjaxResult.error("Failed to get license list: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取许可详情(包含物种特性)
     *
     * @param id 许可ID
     * @return 许可详情
     */
    @Override
    public AjaxResult getLicenseById(String id) {
        try {
            log.info("查询许可详情,ID: {}", id);

            // 使用条件查询，确保只查询未删除的记录（兼容 NULL 和 "0"）
            LambdaQueryWrapper<BreedingLicense> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(BreedingLicense::getId, id);
            wrapper.and(w -> w.eq(BreedingLicense::getDeleted, "0").or().isNull(BreedingLicense::getDeleted));
            BreedingLicense license = licenseMapper.selectOne(wrapper);

            log.info("查询结果: {}", license != null ? "找到记录,deleted=" + (license != null ? license.getDeleted() : "null") : "未找到记录");

            if (license == null) {
                // 检查是否是因为已删除
                BreedingLicense deletedLicense = licenseMapper.selectById(id);
                if (deletedLicense != null) {
                    log.warn("License ID: {} has been deleted, deleted={}", id, deletedLicense.getDeleted());
                    return AjaxResult.error("License has been deleted");
                }
                log.warn("License ID: {} does not exist in database", id);
                return AjaxResult.error("License does not exist");
            }

            BreedingLicenseVO vo = BeanUtil.copyProperties(license, BreedingLicenseVO.class);

            // 查询关联的物种特性（只查询未删除的）
            LambdaQueryWrapper<BreedingVarietyTraits> traitsWrapper = new LambdaQueryWrapper<>();
            traitsWrapper.eq(BreedingVarietyTraits::getLicenseId, id);
            traitsWrapper.and(w -> w.eq(BreedingVarietyTraits::getDeleted, "0").or().isNull(BreedingVarietyTraits::getDeleted));
            BreedingVarietyTraits traits = traitsMapper.selectOne(traitsWrapper);
            if (traits != null) {
                // 保存 traits 的 ID
                String traitsId = traits.getId();
                // 将traits字段复制到vo中（会覆盖 id）
                BeanUtil.copyProperties(traits, vo);
                // 恢复 license 的 ID（重要！）
                vo.setId(license.getId());
                // 设置 traits 的 ID 到专门的字段
                vo.setTraitsId(traitsId);
            }

            return AjaxResult.success(vo);
        } catch (Exception e) {
            log.error("Failed to get license details", e);
            return AjaxResult.error("Failed to get license details: " + e.getMessage());
        }
    }

    /**
     * 根据批次ID获取许可详情
     *
     * @param batchId 批次ID
     * @return 许可详情
     */
    @Override
    public AjaxResult getLicenseByBatchId(String batchId) {
        try {
            // 只查询未删除的记录（兼容 NULL 和 "0"）
            LambdaQueryWrapper<BreedingLicense> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(BreedingLicense::getBatchId, batchId);
            wrapper.and(w -> w.eq(BreedingLicense::getDeleted, "0").or().isNull(BreedingLicense::getDeleted));
            BreedingLicense license = licenseMapper.selectOne(wrapper);

            if (license == null) {
                return AjaxResult.error("No license information for this batch");
            }

            BreedingLicenseVO vo = BeanUtil.copyProperties(license, BreedingLicenseVO.class);

            // 查询关联的物种特性（只查询未删除的）
            LambdaQueryWrapper<BreedingVarietyTraits> traitsWrapper = new LambdaQueryWrapper<>();
            traitsWrapper.eq(BreedingVarietyTraits::getLicenseId, license.getId());
            traitsWrapper.and(w -> w.eq(BreedingVarietyTraits::getDeleted, "0").or().isNull(BreedingVarietyTraits::getDeleted));
            BreedingVarietyTraits traits = traitsMapper.selectOne(traitsWrapper);
            if (traits != null) {
                // 保存 traits 的 ID
                String traitsId = traits.getId();
                // 将traits字段复制到vo中（会覆盖 id）
                BeanUtil.copyProperties(traits, vo);
                // 恢复 license 的 ID（重要！）
                vo.setId(license.getId());
                // 设置 traits 的 ID 到专门的字段
                vo.setTraitsId(traitsId);
            }

            return AjaxResult.success(vo);
        } catch (Exception e) {
            log.error("Failed to get license by batch ID", e);
            return AjaxResult.error("Failed to get license: " + e.getMessage());
        }
    }

    /**
     * 新增许可(包含物种特性)
     *
     * @param dto 许可信息
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addLicense(BreedingLicenseDTO dto) {
        try {
            // 1. 验证必填字段
            if (StrUtil.isBlank(dto.getBatchId())) {
                return AjaxResult.error("Batch ID cannot be empty");
            }

            // 2. 验证数据集是否已审核通过
            if (StrUtil.isNotBlank(dto.getDatasetId())) {
                BreedingDataset dataset = datasetMapper.selectById(dto.getDatasetId());
                if (dataset == null || "1".equals(dataset.getDeleted())) {
                    return AjaxResult.error("Dataset does not exist");
                }
                if (!"approved".equals(dataset.getDatasetStatus())) {
                    return AjaxResult.error("Dataset has not been approved yet, cannot enter license");
                }
            }

            // 3. 检查该批次是否已有许可
            LambdaQueryWrapper<BreedingLicense> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(BreedingLicense::getBatchId, dto.getBatchId());
            wrapper.eq(BreedingLicense::getDeleted, "0");
            Long count = licenseMapper.selectCount(wrapper);
            if (count > 0) {
                return AjaxResult.error("License already exists for this batch, only one license per batch is allowed");
            }

            // 4. 检查许可证号是否重复
            if (StrUtil.isNotBlank(dto.getLicenseNo())) {
                LambdaQueryWrapper<BreedingLicense> licenseNoWrapper = new LambdaQueryWrapper<>();
                licenseNoWrapper.eq(BreedingLicense::getLicenseNo, dto.getLicenseNo());
                licenseNoWrapper.eq(BreedingLicense::getDeleted, "0");
                Long licenseNoCount = licenseMapper.selectCount(licenseNoWrapper);
                if (licenseNoCount > 0) {
                    return AjaxResult.error("License number already exists");
                }
            }

            // 5. 保存许可信息
            BreedingLicense license = new BreedingLicense();
            BeanUtil.copyProperties(dto, license);
            license.setId(IdUtil.randomUUID());

            // 如果有数据集,设置数据集编号
            if (StrUtil.isNotBlank(dto.getDatasetId())) {
                BreedingDataset dataset = datasetMapper.selectById(dto.getDatasetId());
                if (dataset != null) {
                    license.setDatasetCode(dataset.getDatasetCode());
                }
            }

            license.setDeleted("0");
            license.setCreatedTime(LocalDateTime.now());

            // 设置创建人信息
            try {
                String currentUser = SecurityUtils.getUsername();
                license.setCreatedBy(currentUser);
                license.setUpdatedBy(currentUser);
            } catch (Exception ex) {
                log.warn("获取当前用户信息失败", ex);
                license.setCreatedBy("system");
                license.setUpdatedBy("system");
            }
            license.setUpdatedTime(LocalDateTime.now());

            licenseMapper.insert(license);

            // 6. 保存物种特性
            BreedingVarietyTraits traits = new BreedingVarietyTraits();
            BeanUtil.copyProperties(dto, traits);
            traits.setId(IdUtil.randomUUID());
            traits.setLicenseId(license.getId());
            traits.setDeleted("0");
            traits.setCreatedTime(LocalDateTime.now());

            // 设置物种特性的创建人信息
            try {
                String currentUser = SecurityUtils.getUsername();
                traits.setCreatedBy(currentUser);
                traits.setUpdatedBy(currentUser);
            } catch (Exception ex) {
                log.warn("获取当前用户信息失败", ex);
                traits.setCreatedBy("system");
                traits.setUpdatedBy("system");
            }
            traits.setUpdatedTime(LocalDateTime.now());

            traitsMapper.insert(traits);

            log.info("新增许可成功,ID: {}", license.getId());

            // 7. 推送数据到种子信息公示系统
            try {
                pushToPublicSystem(license, dto);
            } catch (Exception e) {
                log.warn("推送到种子信息公示系统失败,但许可证已创建成功: {}", e.getMessage());
                // 不影响许可证创建，只记录警告日志
            }

            return AjaxResult.success("License added successfully", license.getId());
        } catch (Exception e) {
            log.error("新增许可失败", e);
            return AjaxResult.error("Failed to add license: " + e.getMessage());
        }
    }

    /**
     * 推送数据到种子信息公示系统
     *
     * @param license 许可证信息
     * @param dto 许可证DTO
     */
    private void pushToPublicSystem(BreedingLicense license, BreedingLicenseDTO dto) {
        try {
            log.info("开始推送数据到种子信息公示系统,许可证ID: {}", license.getId());

            // 获取当前用户的企业信息
            String currentUser;
            try {
                currentUser = SecurityUtils.getUsername();
            } catch (Exception ex) {
                log.warn("获取当前用户信息失败: {}", ex.getMessage());
                currentUser = null;
            }

            if (StrUtil.isBlank(currentUser)) {
                log.warn("当前用户为空,跳过推送到公示系统");
                return;
            }

            // 去除企业信息查询和验证逻辑
            // 原因：育种许可数据录入时企业还没有认证，不应依赖企业信息
            log.info("育种许可数据录入不依赖企业认证，企业字段将设置为空字符串");

            // Step 1: 创建品种登记记录
            VarietyRegistration registration = new VarietyRegistration();
            // enterprise_id设置为null（已修改数据库允许NULL），其他企业字段设置为空字符串
            registration.setEnterpriseId(null);
            registration.setEnterpriseName("");
            registration.setUnifiedSocialCreditCode("");
            registration.setEnterpriseType("");
            registration.setSeedLicenseNo("");
            registration.setVarietyName(license.getVarietyName());
            registration.setVarietyCode(license.getLicenseNo());
            registration.setCropType(license.getCropType());
            registration.setRecordType("Breeding License Registration");
            registration.setRecordDate(LocalDate.now());
            registration.setApprovalOrg(license.getApprovalOrg() != null ? license.getApprovalOrg() : "");
            registration.setApprovalDate(license.getApprovalDate());
            registration.setOperator(currentUser);
            registration.setOperationOrg("");  // 不依赖企业信息，设置为空字符串

            // 设置培育年份（从审批日期获取，如果没有则使用当前年份）
            Integer breedingYear;
            if (license.getApprovalDate() != null) {
                breedingYear = license.getApprovalDate().getYear();
            } else {
                breedingYear = LocalDate.now().getYear();
            }
            registration.setBreedingYear(breedingYear);

            // 设置生物分类学字段（必填字段，使用默认值）
            registration.setSpecies(license.getCropType() != null ? license.getCropType() : "");
            registration.setGenus("");
            registration.setFamily("");
            registration.setBreedingMethod("");

            // 设置物种特性相关字段（从DTO中获取，必填字段需要提供默认值）
            registration.setMinYieldPotential(dto.getMinYieldPotential() != null ? dto.getMinYieldPotential() : BigDecimal.ZERO);
            registration.setMaxYieldPotential(dto.getMaxYieldPotential() != null ? dto.getMaxYieldPotential() : BigDecimal.ZERO);
            registration.setDiseaseResistance(dto.getDiseaseResistance() != null ? dto.getDiseaseResistance() : "");
            registration.setStressResistance(dto.getStressTolerance() != null ? dto.getStressTolerance() : "");
            registration.setGrowthPeriod(dto.getMaturityDays() != null ? dto.getMaturityDays() : 0);
            registration.setPlantHeight(dto.getPlantHeight() != null ? dto.getPlantHeight() : BigDecimal.ZERO);
            registration.setGrainQualityTraits(dto.getGrainQualityTraits() != null ? dto.getGrainQualityTraits() : "");

            // 设置试验相关字段（必填字段，使用默认值）
            registration.setTestLocation("");
            registration.setTestYear(breedingYear);
            registration.setAverageYield(BigDecimal.ZERO);
            registration.setStabilityScore(BigDecimal.ZERO);
            registration.setTestReportUrl("");
            registration.setPhotoUrl("");
            registration.setApprovalDocNo("");
            registration.setCertificationDocUrl("");
            registration.setMethodPedigree("");

            String registrationId = varietyRegistrationService.submitRegistration(registration);
            log.info("品种登记成功,登记ID: {}", registrationId);

            if (StrUtil.isBlank(registrationId)) {
                log.warn("品种登记返回的ID为空,跳过发布步骤");
                return;
            }

            // 更新登记状态为待发布（1），因为育种许可证已经批准
            varietyRegistrationService.updateRecordStatus(registrationId, 1);
            log.info("品种登记状态已更新为待发布");

            // Step 2: 使用返回的 registrationId 推送到公示系统
            VarietyPublish varietyPublish = new VarietyPublish();
            varietyPublish.setRegistrationId(registrationId);
            varietyPublish.setVarietyName(license.getVarietyName());
            varietyPublish.setCropType(license.getCropType());
            varietyPublish.setPublishDate(LocalDate.now());
            varietyPublish.setPublishDept(license.getApprovalOrg() != null ? license.getApprovalOrg() : "");
            varietyPublish.setDecisionExplanation(license.getRemark() != null && !license.getRemark().isEmpty()
                ? license.getRemark() : "Breeding license approved");
            varietyPublish.setPublicDescription(license.getVarietyName() + " - " + license.getCropType());
            varietyPublish.setRecommendedRegion("");
            varietyPublish.setSowingGuide("");
            varietyPublish.setPublishStatus(1);
            varietyPublish.setPublisher("");

            String publishId = varietyPublishService.publishVariety(varietyPublish);
            log.info("品种发布成功,发布ID: {}", publishId);

        } catch (Exception e) {
            log.error("推送到种子信息公示系统失败: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 修改许可(包含物种特性)
     *
     * @param dto 许可信息
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateLicense(BreedingLicenseDTO dto) {
        try {
            // 1. 验证许可是否存在
            BreedingLicense existingLicense = licenseMapper.selectById(dto.getId());
            if (existingLicense == null || "1".equals(existingLicense.getDeleted())) {
                return AjaxResult.error("License does not exist");
            }

            // 2. 验证数据集是否已审核通过
            if (StrUtil.isNotBlank(dto.getDatasetId())) {
                BreedingDataset dataset = datasetMapper.selectById(dto.getDatasetId());
                if (dataset == null || "1".equals(dataset.getDeleted())) {
                    return AjaxResult.error("Dataset does not exist");
                }
                if (!"approved".equals(dataset.getDatasetStatus())) {
                    return AjaxResult.error("Dataset has not been approved yet, cannot associate license");
                }
            }

            // 3. 如果修改了批次,检查新批次是否已有许可
            if (!existingLicense.getBatchId().equals(dto.getBatchId())) {
                LambdaQueryWrapper<BreedingLicense> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(BreedingLicense::getBatchId, dto.getBatchId());
                wrapper.eq(BreedingLicense::getDeleted, "0");
                wrapper.ne(BreedingLicense::getId, dto.getId());
                Long count = licenseMapper.selectCount(wrapper);
                if (count > 0) {
                    return AjaxResult.error("License already exists for this batch, only one license per batch is allowed");
                }
            }

            // 4. 如果修改了许可证号,检查是否重复
            if (StrUtil.isNotBlank(dto.getLicenseNo()) &&
                !dto.getLicenseNo().equals(existingLicense.getLicenseNo())) {
                LambdaQueryWrapper<BreedingLicense> licenseNoWrapper = new LambdaQueryWrapper<>();
                licenseNoWrapper.eq(BreedingLicense::getLicenseNo, dto.getLicenseNo());
                licenseNoWrapper.eq(BreedingLicense::getDeleted, "0");
                licenseNoWrapper.ne(BreedingLicense::getId, dto.getId());
                Long licenseNoCount = licenseMapper.selectCount(licenseNoWrapper);
                if (licenseNoCount > 0) {
                    return AjaxResult.error("License number already exists");
                }
            }

            // 5. 更新许可信息
            BreedingLicense license = new BreedingLicense();
            BeanUtil.copyProperties(dto, license);

            // 如果有数据集,设置数据集编号
            if (StrUtil.isNotBlank(dto.getDatasetId())) {
                BreedingDataset dataset = datasetMapper.selectById(dto.getDatasetId());
                if (dataset != null) {
                    license.setDatasetCode(dataset.getDatasetCode());
                }
            }

            license.setUpdatedTime(LocalDateTime.now());

            // 设置更新人信息
            try {
                String currentUser = SecurityUtils.getUsername();
                license.setUpdatedBy(currentUser);
            } catch (Exception ex) {
                log.warn("获取当前用户信息失败", ex);
                license.setUpdatedBy("system");
            }

            licenseMapper.updateById(license);

            // 7. 更新或新增物种特性
            if (StrUtil.isNotBlank(dto.getTraitsId())) {
                // 更新现有特性
                BreedingVarietyTraits traits = new BreedingVarietyTraits();
                BeanUtil.copyProperties(dto, traits);
                traits.setId(dto.getTraitsId());
                traits.setUpdatedTime(LocalDateTime.now());

                // 设置更新人信息
                try {
                    String currentUser = SecurityUtils.getUsername();
                    traits.setUpdatedBy(currentUser);
                } catch (Exception ex) {
                    log.warn("获取当前用户信息失败", ex);
                    traits.setUpdatedBy("system");
                }

                traitsMapper.updateById(traits);
            } else {
                // 新增特性
                BreedingVarietyTraits traits = new BreedingVarietyTraits();
                BeanUtil.copyProperties(dto, traits);
                traits.setId(IdUtil.randomUUID());
                traits.setLicenseId(dto.getId());
                traits.setDeleted("0");
                traits.setCreatedTime(LocalDateTime.now());

                // 设置创建人信息
                try {
                    String currentUser = SecurityUtils.getUsername();
                    traits.setCreatedBy(currentUser);
                    traits.setUpdatedBy(currentUser);
                } catch (Exception ex) {
                    log.warn("获取当前用户信息失败", ex);
                    traits.setCreatedBy("system");
                    traits.setUpdatedBy("system");
                }
                traits.setUpdatedTime(LocalDateTime.now());

                traitsMapper.insert(traits);
            }

            log.info("修改许可成功,ID: {}", dto.getId());
            return AjaxResult.success("License updated successfully");
        } catch (Exception e) {
            log.error("修改许可失败", e);
            return AjaxResult.error("Failed to update license: " + e.getMessage());
        }
    }

    /**
     * 删除许可
     *
     * @param ids 许可ID数组
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult deleteLicense(String[] ids) {
        try {
            if (ids == null || ids.length == 0) {
                return AjaxResult.error("Please select licenses to delete");
            }

            // 获取当前用户
            String currentUser;
            try {
                currentUser = SecurityUtils.getUsername();
            } catch (Exception ex) {
                log.warn("获取当前用户信息失败", ex);
                currentUser = "system";
            }

            for (String id : ids) {
                log.info("开始删除许可,ID: {}", id);

                // 使用 UpdateWrapper 强制更新 deleted 字段
                LambdaQueryWrapper<BreedingLicense> updateWrapper = new LambdaQueryWrapper<>();
                updateWrapper.eq(BreedingLicense::getId, id);

                BreedingLicense updateEntity = new BreedingLicense();
                updateEntity.setDeleted("1");
                updateEntity.setUpdatedTime(LocalDateTime.now());
                updateEntity.setUpdatedBy(currentUser);

                int updateCount = licenseMapper.update(updateEntity, updateWrapper);
                log.info("许可删除结果,ID: {}, 更新行数: {}", id, updateCount);

                if (updateCount > 0) {
                    // 验证是否真的更新成功
                    BreedingLicense verifyLicense = licenseMapper.selectById(id);
                    log.info("验证删除结果,ID: {}, deleted={}", id, verifyLicense != null ? verifyLicense.getDeleted() : "null");

                    // 删除关联的物种特性
                    LambdaQueryWrapper<BreedingVarietyTraits> traitsQueryWrapper = new LambdaQueryWrapper<>();
                    traitsQueryWrapper.eq(BreedingVarietyTraits::getLicenseId, id);
                    List<BreedingVarietyTraits> traitsList = traitsMapper.selectList(traitsQueryWrapper);

                    for (BreedingVarietyTraits trait : traitsList) {
                        LambdaQueryWrapper<BreedingVarietyTraits> traitsUpdateWrapper = new LambdaQueryWrapper<>();
                        traitsUpdateWrapper.eq(BreedingVarietyTraits::getId, trait.getId());

                        BreedingVarietyTraits updateTrait = new BreedingVarietyTraits();
                        updateTrait.setDeleted("1");
                        updateTrait.setUpdatedTime(LocalDateTime.now());
                        updateTrait.setUpdatedBy(currentUser);

                        traitsMapper.update(updateTrait, traitsUpdateWrapper);
                    }
                    log.info("物种特性删除结果,许可ID: {}, 删除数量: {}", id, traitsList.size());
                } else {
                    log.error("许可删除失败,ID: {}, 更新行数为0", id);
                }
            }

            log.info("删除许可成功,数量: {}", ids.length);
            return AjaxResult.success("Deleted successfully");
        } catch (Exception e) {
            log.error("删除许可失败", e);
            return AjaxResult.error("Failed to delete license: " + e.getMessage());
        }
    }
}
