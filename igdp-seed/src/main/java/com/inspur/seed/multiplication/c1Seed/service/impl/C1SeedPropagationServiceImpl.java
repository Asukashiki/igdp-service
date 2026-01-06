package com.inspur.seed.multiplication.c1Seed.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.OseBatchCollection;
import com.inspur.seed.mapper.OseBatchCollectionMapper;
import com.inspur.seed.multiplication.c1Seed.domain.dto.AvailableBasicSeedQueryDTO;
import com.inspur.seed.multiplication.c1Seed.domain.dto.C1SeedPropagationDTO;
import com.inspur.seed.multiplication.c1Seed.domain.dto.C1SeedPropagationQueryDTO;
import com.inspur.seed.multiplication.c1Seed.domain.entity.C1SeedPropagation;
import com.inspur.seed.multiplication.c1Seed.domain.vo.AvailableBasicSeedVO;
import com.inspur.seed.multiplication.c1Seed.domain.vo.C1SeedPropagationVO;
import com.inspur.seed.multiplication.c1Seed.mapper.C1SeedPropagationMapper;
import com.inspur.seed.multiplication.c1Seed.service.IC1SeedPropagationService;
import com.inspur.seed.multiplication.oseReceive.domain.entity.OseBreedSeedReceiveConfirm;
import com.inspur.seed.multiplication.oseReceive.mapper.OseReceiveConfirmMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * C1种子繁殖申请Service实现
 *
 * @author system
 * @since 2025-12-08
 */
@Slf4j
@Service
public class C1SeedPropagationServiceImpl implements IC1SeedPropagationService {

    @Autowired
    private C1SeedPropagationMapper propagationMapper;

    @Autowired
    private OseReceiveConfirmMapper receiveConfirmMapper;

    @Autowired
    private OseBatchCollectionMapper batchCollectionMapper;

    /**
     * 获取申请列表（分页）
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @Override
    public AjaxResult getList(C1SeedPropagationQueryDTO queryDTO) {
        try {
            // 构建查询条件
            LambdaQueryWrapper<C1SeedPropagation> wrapper = new LambdaQueryWrapper<>();
            // 只查询未删除的记录
            wrapper.and(w -> w.eq(C1SeedPropagation::getDeleted, "0").or().isNull(C1SeedPropagation::getDeleted));

            // 关键词搜索
            if (StrUtil.isNotBlank(queryDTO.getKeyword())) {
                wrapper.and(w -> w.like(C1SeedPropagation::getApplicantOrgName, queryDTO.getKeyword())
                        .or().like(C1SeedPropagation::getVarietyName, queryDTO.getKeyword()));
            }

            // 作物种类
            if (StrUtil.isNotBlank(queryDTO.getCropType())) {
                wrapper.eq(C1SeedPropagation::getCropType, queryDTO.getCropType());
            }

            // 品种名称
            if (StrUtil.isNotBlank(queryDTO.getVarietyName())) {
                wrapper.like(C1SeedPropagation::getVarietyName, queryDTO.getVarietyName());
            }

            // 申请状态
            if (StrUtil.isNotBlank(queryDTO.getApplyStatus())) {
                wrapper.eq(C1SeedPropagation::getApplyStatus, queryDTO.getApplyStatus());
            }

            // 申请机构名称
            if (StrUtil.isNotBlank(queryDTO.getApplicantOrgName())) {
                wrapper.like(C1SeedPropagation::getApplicantOrgName, queryDTO.getApplicantOrgName());
            }

            // 时间范围
            if (StrUtil.isNotBlank(queryDTO.getQueryDateStart())) {
                wrapper.ge(C1SeedPropagation::getApplyDate, queryDTO.getQueryDateStart());
            }
            if (StrUtil.isNotBlank(queryDTO.getQueryDateEnd())) {
                wrapper.le(C1SeedPropagation::getApplyDate, queryDTO.getQueryDateEnd());
            }

            // 按创建时间倒序
            wrapper.orderByDesc(C1SeedPropagation::getCreatedTime);

            // 分页查询
            Page<C1SeedPropagation> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
            IPage<C1SeedPropagation> pageResult = propagationMapper.selectPage(page, wrapper);

            // 转换为VO
            List<C1SeedPropagationVO> voList = new ArrayList<>();
            for (C1SeedPropagation entity : pageResult.getRecords()) {
                C1SeedPropagationVO vo = BeanUtil.copyProperties(entity, C1SeedPropagationVO.class);
                voList.add(vo);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("list", voList);
            result.put("total", pageResult.getTotal());
            result.put("pageNum", pageResult.getCurrent());
            result.put("pageSize", pageResult.getSize());

            return AjaxResult.success(result);
        } catch (Exception e) {
            log.error("Failed to get propagation list", e);
            return AjaxResult.error("Failed to get propagation list: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取申请详情
     *
     * @param id 申请ID
     * @return 申请详情
     */
    @Override
    public AjaxResult getById(String id) {
        try {
            log.info("查询申请详情,ID: {}", id);

            // 使用条件查询
            LambdaQueryWrapper<C1SeedPropagation> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(C1SeedPropagation::getId, id);
            wrapper.and(w -> w.eq(C1SeedPropagation::getDeleted, "0").or().isNull(C1SeedPropagation::getDeleted));
            C1SeedPropagation entity = propagationMapper.selectOne(wrapper);

            if (entity == null) {
                return AjaxResult.error("Propagation application does not exist");
            }

            C1SeedPropagationVO vo = BeanUtil.copyProperties(entity, C1SeedPropagationVO.class);
            return AjaxResult.success(vo);
        } catch (Exception e) {
            log.error("Failed to get propagation details", e);
            return AjaxResult.error("Failed to get propagation details: " + e.getMessage());
        }
    }

    /**
     * 新增申请
     *
     * @param dto 申请信息
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult add(C1SeedPropagationDTO dto) {
        try {
            // 验证必填字段
            if (StrUtil.isBlank(dto.getApplicantOrgId())) {
                return AjaxResult.error("Applicant organization ID cannot be empty");
            }
            if (StrUtil.isBlank(dto.getPropagationBatchId())) {
                return AjaxResult.error("Propagation batch ID cannot be empty");
            }

            // 创建实体
            C1SeedPropagation entity = new C1SeedPropagation();
            BeanUtil.copyProperties(dto, entity);
            entity.setId(IdUtil.randomUUID());

            // 设置申请日期
            if (StrUtil.isNotBlank(dto.getApplyDate())) {
                entity.setApplyDate(LocalDate.parse(dto.getApplyDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            } else {
                entity.setApplyDate(LocalDate.now());
            }

            // 设置初始状态为待审核
            entity.setApplyStatus("pending");
            entity.setDeleted("0");
            entity.setCreatedTime(LocalDateTime.now());
            entity.setOperationTime(LocalDateTime.now());

            // 设置创建人信息
            try {
                String currentUser = SecurityUtils.getUsername();
                entity.setCreatedBy(currentUser);
                entity.setUpdatedBy(currentUser);
                entity.setOperator(currentUser);
            } catch (Exception ex) {
                log.warn("获取当前用户信息失败", ex);
                entity.setCreatedBy("system");
                entity.setUpdatedBy("system");
                entity.setOperator("system");
            }
            entity.setUpdatedTime(LocalDateTime.now());

            propagationMapper.insert(entity);

            log.info("新增申请成功,ID: {}", entity.getId());
            return AjaxResult.success("Application added successfully", entity.getId());
        } catch (Exception e) {
            log.error("新增申请失败", e);
            return AjaxResult.error("Failed to add application: " + e.getMessage());
        }
    }

    /**
     * 修改申请
     *
     * @param dto 申请信息
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult update(C1SeedPropagationDTO dto) {
        try {
            // 验证申请是否存在
            C1SeedPropagation existing = propagationMapper.selectById(dto.getId());
            if (existing == null || "1".equals(existing.getDeleted())) {
                return AjaxResult.error("Application does not exist");
            }

            // 检查状态，已审核的不允许修改
            if (!"pending".equals(existing.getApplyStatus())) {
                return AjaxResult.error("Cannot modify an application that has been audited");
            }

            // 更新实体
            C1SeedPropagation entity = new C1SeedPropagation();
            BeanUtil.copyProperties(dto, entity);

            // 设置申请日期
            if (StrUtil.isNotBlank(dto.getApplyDate())) {
                entity.setApplyDate(LocalDate.parse(dto.getApplyDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }

            entity.setUpdatedTime(LocalDateTime.now());
            entity.setOperationTime(LocalDateTime.now());

            // 设置更新人信息
            try {
                String currentUser = SecurityUtils.getUsername();
                entity.setUpdatedBy(currentUser);
                entity.setOperator(currentUser);
            } catch (Exception ex) {
                log.warn("获取当前用户信息失败", ex);
                entity.setUpdatedBy("system");
                entity.setOperator("system");
            }

            propagationMapper.updateById(entity);

            log.info("修改申请成功,ID: {}", dto.getId());
            return AjaxResult.success("Application updated successfully");
        } catch (Exception e) {
            log.error("修改申请失败", e);
            return AjaxResult.error("Failed to update application: " + e.getMessage());
        }
    }

    /**
     * 删除申请
     *
     * @param ids 申请ID数组
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult delete(String[] ids) {
        try {
            if (ids == null || ids.length == 0) {
                return AjaxResult.error("Please select applications to delete");
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
                log.info("开始删除申请,ID: {}", id);

                // 检查是否已审核
                C1SeedPropagation existing = propagationMapper.selectById(id);
                if (existing != null && !"pending".equals(existing.getApplyStatus())) {
                    log.warn("申请已审核，不允许删除,ID: {}", id);
                    continue;
                }

                // 逻辑删除
                LambdaQueryWrapper<C1SeedPropagation> updateWrapper = new LambdaQueryWrapper<>();
                updateWrapper.eq(C1SeedPropagation::getId, id);

                C1SeedPropagation updateEntity = new C1SeedPropagation();
                updateEntity.setDeleted("1");
                updateEntity.setUpdatedTime(LocalDateTime.now());
                updateEntity.setUpdatedBy(currentUser);

                propagationMapper.update(updateEntity, updateWrapper);
            }

            log.info("删除申请成功,数量: {}", ids.length);
            return AjaxResult.success("Deleted successfully");
        } catch (Exception e) {
            log.error("删除申请失败", e);
            return AjaxResult.error("Failed to delete application: " + e.getMessage());
        }
    }

    /**
     * 提交审核
     *
     * @param dto 审核信息
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult audit(C1SeedPropagationDTO dto) {
        try {
            // 验证申请是否存在
            C1SeedPropagation existing = propagationMapper.selectById(dto.getId());
            if (existing == null || "1".equals(existing.getDeleted())) {
                return AjaxResult.error("Application does not exist");
            }

            // 检查状态
            if (!"pending".equals(existing.getApplyStatus())) {
                return AjaxResult.error("Application has already been audited");
            }

            // 验证审核结果
            if (StrUtil.isBlank(dto.getAuditResult())) {
                return AjaxResult.error("Audit result cannot be empty");
            }
            if (!"approved".equals(dto.getAuditResult()) && !"rejected".equals(dto.getAuditResult())) {
                return AjaxResult.error("Invalid audit result");
            }

            // 更新审核信息
            C1SeedPropagation entity = new C1SeedPropagation();
            entity.setId(dto.getId());
            entity.setAuditResult(dto.getAuditResult());
            entity.setAuditOpinion(dto.getAuditOpinion());
            entity.setApplyStatus(dto.getAuditResult());
            entity.setAuditTime(LocalDateTime.now());
            entity.setUpdatedTime(LocalDateTime.now());

            // 如果审核通过，生成批次号赋值给authId
            if ("approved".equals(dto.getAuditResult())) {
                String batchNo = generateUniqueBatchNo();
                entity.setAuthId(batchNo);
                log.info("审核通过，生成批次号: {}", batchNo);
            }

            // 设置审核人信息
            try {
                String currentUser = SecurityUtils.getUsername();
                entity.setAuditor(StrUtil.isNotBlank(dto.getAuditor()) ? dto.getAuditor() : currentUser);
                entity.setUpdatedBy(currentUser);
            } catch (Exception ex) {
                log.warn("获取当前用户信息失败", ex);
                entity.setAuditor(dto.getAuditor() != null ? dto.getAuditor() : "system");
                entity.setUpdatedBy("system");
            }

            entity.setAuditOrg(dto.getAuditOrg());

            propagationMapper.updateById(entity);

            log.info("审核申请成功,ID: {}, 结果: {}", dto.getId(), dto.getAuditResult());
            return AjaxResult.success("Audit submitted successfully");
        } catch (Exception e) {
            log.error("审核申请失败", e);
            return AjaxResult.error("Failed to submit audit: " + e.getMessage());
        }
    }

    /**
     * 生成唯一批次号
     * 格式: C1B-YYYYMMDD-XXXX
     */
    private String generateUniqueBatchNo() {
        String prefix = "C1B-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-";
        String batchNo;
        int maxAttempts = 100;
        int attempts = 0;

        do {
            batchNo = prefix + String.format("%04d", (int)(Math.random() * 10000));
            // 检查是否已存在
            LambdaQueryWrapper<C1SeedPropagation> checkWrapper = new LambdaQueryWrapper<>();
            checkWrapper.eq(C1SeedPropagation::getAuthId, batchNo);
            if (propagationMapper.selectCount(checkWrapper) == 0) {
                return batchNo;
            }
            attempts++;
        } while (attempts < maxAttempts);

        // 如果随机生成失败，使用UUID后缀
        return prefix + IdUtil.fastSimpleUUID().substring(0, 4).toUpperCase();
    }

    /**
     * 获取待审核列表（分页）
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @Override
    public AjaxResult getPendingList(C1SeedPropagationQueryDTO queryDTO) {
        // 强制设置状态为待审核
        queryDTO.setApplyStatus("pending");
        return getList(queryDTO);
    }

    /**
     * 获取已审核通过的申请列表（供批次采集选择）
     *
     * @return 列表结果
     */
    @Override
    public AjaxResult getApprovedList() {
        try {
            LambdaQueryWrapper<C1SeedPropagation> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(C1SeedPropagation::getApplyStatus, "approved");
            wrapper.isNotNull(C1SeedPropagation::getAuthId);
            wrapper.and(w -> w.eq(C1SeedPropagation::getDeleted, "0").or().isNull(C1SeedPropagation::getDeleted));
            wrapper.orderByDesc(C1SeedPropagation::getAuditTime);

            List<C1SeedPropagation> list = propagationMapper.selectList(wrapper);

            // 转换为VO
            List<C1SeedPropagationVO> voList = new ArrayList<>();
            for (C1SeedPropagation entity : list) {
                C1SeedPropagationVO vo = BeanUtil.copyProperties(entity, C1SeedPropagationVO.class);
                voList.add(vo);
            }

            return AjaxResult.success(voList);
        } catch (Exception e) {
            log.error("获取已审核通过列表失败", e);
            return AjaxResult.error("Failed to get approved list: " + e.getMessage());
        }
    }

    /**
     * 获取可用的Basic种子列表
     * 聚合OSE接收确认和批次采集两个数据源
     *
     * @param queryDTO 查询条件
     * @return 可用种子列表
     */
    @Override
    public AjaxResult getAvailableBasicSeeds(AvailableBasicSeedQueryDTO queryDTO) {
        try {
            log.info("开始查询可用Basic种子列表");
            List<AvailableBasicSeedVO> resultList = new ArrayList<>();

            // 1. 从OSE接收确认模块查询Basic种子
            if (queryDTO == null || queryDTO.getSourceType() == null || "OSE_RECEIVE".equals(queryDTO.getSourceType())) {
                List<AvailableBasicSeedVO> receiveSeeds = getAvailableSeedsFromReceiveConfirm(queryDTO);
                resultList.addAll(receiveSeeds);
                log.info("从OSE接收确认查询到 {} 条Basic种子", receiveSeeds.size());
            }

            // 2. 从批次采集模块查询Basic种子（breedingLevel='Basic'）
            if (queryDTO == null || queryDTO.getSourceType() == null || "OSE_BATCH_COLLECTION".equals(queryDTO.getSourceType())) {
                List<AvailableBasicSeedVO> batchSeeds = getAvailableSeedsFromBatchCollection(queryDTO);
                resultList.addAll(batchSeeds);
                log.info("从批次采集查询到 {} 条Basic种子", batchSeeds.size());
            }

            // 3. 过滤只返回有可用数量的种子
            if (queryDTO != null && queryDTO.getOnlyAvailable() != null && queryDTO.getOnlyAvailable()) {
                resultList.removeIf(vo -> vo.getAvailableQuantity() == null
                    || vo.getAvailableQuantity().compareTo(BigDecimal.ZERO) <= 0);
            }

            log.info("共查询到 {} 条可用Basic种子", resultList.size());
            return AjaxResult.success(resultList);
        } catch (Exception e) {
            log.error("查询可用Basic种子列表失败", e);
            return AjaxResult.error("Failed to get available Basic seeds: " + e.getMessage());
        }
    }

    /**
     * 从OSE接收确认模块获取可用种子
     */
    private List<AvailableBasicSeedVO> getAvailableSeedsFromReceiveConfirm(AvailableBasicSeedQueryDTO queryDTO) {
        List<AvailableBasicSeedVO> resultList = new ArrayList<>();

        // 查询已确认的接收记录，关联分发明细获取Basic种子信息
        // 使用自定义SQL查询来获取完整的种子信息
        LambdaQueryWrapper<OseBreedSeedReceiveConfirm> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OseBreedSeedReceiveConfirm::getReceiveStatus, "CONFIRMED");

        List<OseBreedSeedReceiveConfirm> confirmList = receiveConfirmMapper.selectList(wrapper);

        for (OseBreedSeedReceiveConfirm confirm : confirmList) {
            // 通过自定义mapper获取完整信息
            try {
                com.inspur.seed.multiplication.oseReceive.domain.vo.OseReceiveConfirmVO vo =
                    receiveConfirmMapper.selectReceiveConfirmById(confirm.getReceiveConfirmId());

                if (vo != null && vo.getDistributeDetail() != null && vo.getDistributeDetail().getDetailList() != null) {
                    for (com.inspur.seed.multiplication.oseReceive.domain.vo.OseReceiveConfirmVO.DetailItem detail : vo.getDistributeDetail().getDetailList()) {
                        // 只选择Basic级别的种子
                        if ("Basic".equals(detail.getSeedType())) {
                            // 应用查询条件过滤
                            if (queryDTO != null) {
                                if (StrUtil.isNotBlank(queryDTO.getVarietyName())
                                    && !detail.getVarietyName().contains(queryDTO.getVarietyName())) {
                                    continue;
                                }
                                if (StrUtil.isNotBlank(queryDTO.getCropType())
                                    && !queryDTO.getCropType().equals(detail.getCropType())) {
                                    continue;
                                }
                            }

                            AvailableBasicSeedVO seedVO = new AvailableBasicSeedVO();
                            seedVO.setBatchId(detail.getBreedSeedProduceBatchId());
                            seedVO.setSourceType("OSE_RECEIVE");
                            seedVO.setSourceId(confirm.getReceiveConfirmId());
                            seedVO.setVarietyName(detail.getVarietyName());
                            seedVO.setCropType(detail.getCropType());
                            seedVO.setBreedingLevel(detail.getSeedType());
                            seedVO.setParentalSeedSource(detail.getParentalSeedSource());
                            seedVO.setTotalQuantity(detail.getDistributeQuantity());
                            seedVO.setReceiveDate(confirm.getConfirmTime() != null ?
                                new java.text.SimpleDateFormat("yyyy-MM-dd").format(confirm.getConfirmTime()) : null);
                            seedVO.setRemark(confirm.getRemark());

                            // 计算已申请数量和可用数量
                            BigDecimal appliedQuantity = calculateAppliedQuantity(
                                detail.getBreedSeedProduceBatchId(), "OSE_RECEIVE");
                            seedVO.setAppliedQuantity(appliedQuantity);

                            BigDecimal availableQuantity = detail.getDistributeQuantity()
                                .subtract(appliedQuantity != null ? appliedQuantity : BigDecimal.ZERO);
                            seedVO.setAvailableQuantity(availableQuantity);

                            resultList.add(seedVO);
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("处理接收确认记录失败: {}", confirm.getReceiveConfirmId(), e);
            }
        }

        return resultList;
    }

    /**
     * 从批次采集模块获取可用种子
     */
    private List<AvailableBasicSeedVO> getAvailableSeedsFromBatchCollection(AvailableBasicSeedQueryDTO queryDTO) {
        List<AvailableBasicSeedVO> resultList = new ArrayList<>();

        // 查询繁殖级别为Basic的批次
        LambdaQueryWrapper<OseBatchCollection> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OseBatchCollection::getBreedingLevel, "Basic");
        wrapper.and(w -> w.eq(OseBatchCollection::getDelFlag, "0").or().isNull(OseBatchCollection::getDelFlag));

        // 应用查询条件
        if (queryDTO != null) {
            if (StrUtil.isNotBlank(queryDTO.getVarietyName())) {
                wrapper.like(OseBatchCollection::getVarietyName, queryDTO.getVarietyName());
            }
            if (StrUtil.isNotBlank(queryDTO.getCropType())) {
                wrapper.eq(OseBatchCollection::getCropType, queryDTO.getCropType());
            }
        }

        wrapper.orderByDesc(OseBatchCollection::getCollectionDate);

        List<OseBatchCollection> batchList = batchCollectionMapper.selectList(wrapper);

        for (OseBatchCollection batch : batchList) {
            AvailableBasicSeedVO seedVO = new AvailableBasicSeedVO();
            seedVO.setBatchId(batch.getBatchId());
            seedVO.setSourceType("OSE_BATCH_COLLECTION");
            seedVO.setSourceId(String.valueOf(batch.getId()));
            seedVO.setVarietyName(batch.getVarietyName());
            seedVO.setCropType(batch.getCropType());
            seedVO.setBreedingLevel(batch.getBreedingLevel());
            seedVO.setParentalSeedSource(batch.getParentalSeedSource());
            seedVO.setTotalQuantity(batch.getToMultiplyQuantity());
            seedVO.setReceiveDate(batch.getCollectionDate() != null ?
                new java.text.SimpleDateFormat("yyyy-MM-dd").format(batch.getCollectionDate()) : null);
            seedVO.setRemark(batch.getRemark());

            // 计算已申请数量和可用数量
            BigDecimal appliedQuantity = calculateAppliedQuantity(batch.getBatchId(), "OSE_BATCH_COLLECTION");
            seedVO.setAppliedQuantity(appliedQuantity);

            BigDecimal availableQuantity = batch.getToMultiplyQuantity()
                .subtract(appliedQuantity != null ? appliedQuantity : BigDecimal.ZERO);
            seedVO.setAvailableQuantity(availableQuantity);

            resultList.add(seedVO);
        }

        return resultList;
    }

    /**
     * 计算指定批次的已申请数量（排除被拒绝的申请）
     *
     * @param batchId 批次ID
     * @param sourceType 数据来源类型
     * @return 已申请数量
     */
    private BigDecimal calculateAppliedQuantity(String batchId, String sourceType) {
        LambdaQueryWrapper<C1SeedPropagation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(C1SeedPropagation::getPropagationBatchId, batchId);
        // 排除被拒绝的申请
        wrapper.ne(C1SeedPropagation::getApplyStatus, "rejected");
        wrapper.and(w -> w.eq(C1SeedPropagation::getDeleted, "0").or().isNull(C1SeedPropagation::getDeleted));

        List<C1SeedPropagation> applications = propagationMapper.selectList(wrapper);

        BigDecimal totalApplied = BigDecimal.ZERO;
        for (C1SeedPropagation app : applications) {
            if (app.getDemandQuantity() != null) {
                totalApplied = totalApplied.add(new BigDecimal(app.getDemandQuantity()));
            }
        }

        return totalApplied;
    }

    /**
     * 获取指定批次的可用数量
     *
     * @param batchId 批次ID
     * @param sourceType 数据来源类型 (OSE_RECEIVE/OSE_BATCH_COLLECTION)
     * @return 可用数量信息
     */
    @Override
    public AjaxResult getAvailableQuantity(String batchId, String sourceType) {
        try {
            log.info("查询批次可用数量,batchId: {}, sourceType: {}", batchId, sourceType);

            BigDecimal totalQuantity = BigDecimal.ZERO;
            BigDecimal appliedQuantity = calculateAppliedQuantity(batchId, sourceType);

            // 根据来源类型查询总数量
            if ("OSE_RECEIVE".equals(sourceType)) {
                // 从OSE接收确认查询
                // 这里需要通过批次ID查找对应的分发记录
                // 由于数据结构复杂，这里简化处理，实际使用时需要关联查询
                totalQuantity = BigDecimal.ZERO; // TODO: 实现完整查询逻辑
            } else if ("OSE_BATCH_COLLECTION".equals(sourceType)) {
                // 从批次采集查询
                LambdaQueryWrapper<OseBatchCollection> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(OseBatchCollection::getBatchId, batchId);
                wrapper.and(w -> w.eq(OseBatchCollection::getDelFlag, "0").or().isNull(OseBatchCollection::getDelFlag));
                OseBatchCollection batch = batchCollectionMapper.selectOne(wrapper);
                if (batch != null) {
                    totalQuantity = batch.getToMultiplyQuantity();
                }
            }

            BigDecimal availableQuantity = totalQuantity.subtract(
                appliedQuantity != null ? appliedQuantity : BigDecimal.ZERO);

            Map<String, Object> result = new HashMap<>();
            result.put("batchId", batchId);
            result.put("sourceType", sourceType);
            result.put("totalQuantity", totalQuantity);
            result.put("appliedQuantity", appliedQuantity);
            result.put("availableQuantity", availableQuantity);

            return AjaxResult.success(result);
        } catch (Exception e) {
            log.error("查询批次可用数量失败", e);
            return AjaxResult.error("Failed to get available quantity: " + e.getMessage());
        }
    }
}

