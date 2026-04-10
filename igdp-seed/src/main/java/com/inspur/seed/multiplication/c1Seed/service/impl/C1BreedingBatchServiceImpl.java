package com.inspur.seed.multiplication.c1Seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.multiplication.c1Seed.domain.dto.C1BreedingBatchDTO;
import com.inspur.seed.multiplication.c1Seed.domain.dto.C1BreedingBatchQueryDTO;
import com.inspur.seed.multiplication.oseReceive.domain.entity.OseBreedSeedReceiveConfirm;
import com.inspur.seed.multiplication.oseReceive.mapper.OseReceiveConfirmMapper;
import com.inspur.seed.multiplication.c1Seed.domain.entity.C1BreedingBatch;
import com.inspur.seed.multiplication.c1Seed.domain.vo.C1BreedingBatchVO;
import com.inspur.seed.multiplication.c1Seed.mapper.C1BreedingBatchMapper;
import com.inspur.seed.multiplication.c1Seed.service.IC1BreedingBatchService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

/**
 * C1繁殖批次Service实现
 * @author system
 * @since 2025-12-08
 */
@Service
public class C1BreedingBatchServiceImpl extends ServiceImpl<C1BreedingBatchMapper, C1BreedingBatch>
        implements IC1BreedingBatchService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private OseReceiveConfirmMapper oseReceiveConfirmMapper;

    @Override
    public IPage<C1BreedingBatchVO> pageList(C1BreedingBatchQueryDTO queryDTO) {
        return pageListInternal(queryDTO, null);
    }

    @Override
    public IPage<C1BreedingBatchVO> pageCertificateEligibleList(C1BreedingBatchQueryDTO queryDTO, List<String> batchIds) {
        if (batchIds == null || batchIds.isEmpty()) {
            return new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize(), 0);
        }
        return pageListInternal(queryDTO, batchIds);
    }

    private IPage<C1BreedingBatchVO> pageListInternal(C1BreedingBatchQueryDTO queryDTO, List<String> includedBatchIds) {
        Page<C1BreedingBatch> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        LambdaQueryWrapper<C1BreedingBatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(C1BreedingBatch::getDeleted, "0");

        if (includedBatchIds != null) {
            if (includedBatchIds.isEmpty()) {
                return new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize(), 0);
            }
            wrapper.in(C1BreedingBatch::getBatchId, includedBatchIds);
        }

        // 批次编号模糊搜索
        if (StringUtils.hasText(queryDTO.getBatchId())) {
            wrapper.like(C1BreedingBatch::getBatchId, queryDTO.getBatchId());
        }

        // 关键词搜索（批次编号或品种名称）
        if (StringUtils.hasText(queryDTO.getKeyword())) {
            wrapper.and(w -> w.like(C1BreedingBatch::getBatchId, queryDTO.getKeyword())
                    .or().like(C1BreedingBatch::getVarietyName, queryDTO.getKeyword())
                    .or().like(C1BreedingBatch::getOrgName, queryDTO.getKeyword()));
        }

        // 品种名称模糊搜索
        if (StringUtils.hasText(queryDTO.getVarietyName())) {
            wrapper.like(C1BreedingBatch::getVarietyName, queryDTO.getVarietyName());
        }

        // 作物种类
        if (StringUtils.hasText(queryDTO.getCropType())) {
            wrapper.eq(C1BreedingBatch::getCropType, queryDTO.getCropType());
        }

        // 批次状态
        if (StringUtils.hasText(queryDTO.getBatchStatus())) {
            wrapper.eq(C1BreedingBatch::getBatchStatus, queryDTO.getBatchStatus());
        }

        // 机构ID
        if (StringUtils.hasText(queryDTO.getOrgId())) {
            wrapper.eq(C1BreedingBatch::getOrgId, queryDTO.getOrgId());
        }

        // 机构类型
        if (StringUtils.hasText(queryDTO.getOrgType())) {
            wrapper.eq(C1BreedingBatch::getOrgType, queryDTO.getOrgType());
        }

        // 开始日期范围
        if (StringUtils.hasText(queryDTO.getStartDateBegin())) {
            wrapper.ge(C1BreedingBatch::getStartDate, LocalDate.parse(queryDTO.getStartDateBegin(), DATE_FORMATTER));
        }
        if (StringUtils.hasText(queryDTO.getStartDateEnd())) {
            wrapper.le(C1BreedingBatch::getStartDate, LocalDate.parse(queryDTO.getStartDateEnd(), DATE_FORMATTER));
        }

        // 审核状态
        if (StringUtils.hasText(queryDTO.getAuditStatus())) {
            wrapper.eq(C1BreedingBatch::getAuditStatus, queryDTO.getAuditStatus());
        }

        wrapper.orderByDesc(C1BreedingBatch::getCreatedTime);

        IPage<C1BreedingBatch> result = this.page(page, wrapper);

        return result.convert(this::convertToVO);
    }

    @Override
    public C1BreedingBatchVO getDetailById(String id) {
        C1BreedingBatch entity = this.getById(id);
        if (entity == null || "1".equals(entity.getDeleted())) {
            return null;
        }
        return convertToVO(entity);
    }

    @Override
    public boolean add(C1BreedingBatchDTO dto) {
        C1BreedingBatch entity = new C1BreedingBatch();
        BeanUtils.copyProperties(dto, entity);

        // 生成批次编号：C1-年月日-随机4位
        String batchId = "C1-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-" + String.format("%04d", (int)(Math.random() * 10000));
        entity.setBatchId(batchId);

        // 设置默认值
        entity.setBatchStatus("01"); // 进行中
        entity.setAuditStatus("pending"); // 待审核
        entity.setTrackingCount(0);
        entity.setTestCount(0);
        entity.setPrintCount(0);
        entity.setDeleted("0");
        entity.setCreatedTime(LocalDateTime.now());

        // 处理日期
        if (StringUtils.hasText(dto.getStartDate())) {
            entity.setStartDate(LocalDate.parse(dto.getStartDate(), DATE_FORMATTER));
        }
        if (StringUtils.hasText(dto.getEndDate())) {
            entity.setEndDate(LocalDate.parse(dto.getEndDate(), DATE_FORMATTER));
        }

        return this.save(entity);
    }

    @Override
    public boolean update(C1BreedingBatchDTO dto) {
        C1BreedingBatch entity = this.getById(dto.getId());
        if (entity == null) {
            return false;
        }

        BeanUtils.copyProperties(dto, entity);
        entity.setUpdatedTime(LocalDateTime.now());

        // 处理日期
        if (StringUtils.hasText(dto.getStartDate())) {
            entity.setStartDate(LocalDate.parse(dto.getStartDate(), DATE_FORMATTER));
        }
        if (StringUtils.hasText(dto.getEndDate())) {
            entity.setEndDate(LocalDate.parse(dto.getEndDate(), DATE_FORMATTER));
        }

        return this.updateById(entity);
    }

    @Override
    public boolean deleteByIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        // 逻辑删除
        return this.update()
                .set("deleted", "1")
                .set("updated_time", LocalDateTime.now())
                .in("id", ids)
                .update();
    }

    /**
     * 实体转VO
     */
    private C1BreedingBatchVO convertToVO(C1BreedingBatch entity) {
        C1BreedingBatchVO vo = new C1BreedingBatchVO();
        BeanUtils.copyProperties(entity, vo);

        // 格式化日期
        if (entity.getStartDate() != null) {
            vo.setStartDate(entity.getStartDate().format(DATE_FORMATTER));
        }
        if (entity.getEndDate() != null) {
            vo.setEndDate(entity.getEndDate().format(DATE_FORMATTER));
        }
        if (entity.getCreatedTime() != null) {
            vo.setCreatedTime(entity.getCreatedTime().format(DATETIME_FORMATTER));
        }
        if (entity.getUpdatedTime() != null) {
            vo.setUpdatedTime(entity.getUpdatedTime().format(DATETIME_FORMATTER));
        }
        if (entity.getAuditTime() != null) {
            vo.setAuditTime(entity.getAuditTime().format(DATETIME_FORMATTER));
        }
        if (entity.getLastPrintTime() != null) {
            vo.setLastPrintTime(entity.getLastPrintTime().format(DATETIME_FORMATTER));
        }

        return vo;
    }

    @Override
    public boolean approveBatch(String id, String auditComment) {
        C1BreedingBatch entity = this.getById(id);
        if (entity == null) {
            return false;
        }
        // 从登录用户获取审核人和组织信息
        String auditor = SecurityUtils.getUsername();
        String auditorOrgId = SecurityUtils.getDeptId();

        // 获取机构名称（这里假设需要从其他服务或缓存中获取，暂时使用ID作为名称）
        // 实际项目中可能需要根据 deptId 查询机构名称
        String auditorOrgName = SecurityUtils.getDeptName();
        entity.setAuditStatus("approved");
        entity.setAuditor(auditor);
        entity.setAuditorOrgId(auditorOrgId);
        entity.setAuditorOrgName(auditorOrgName);
        entity.setAuditTime(LocalDateTime.now());
        entity.setAuditComment(auditComment);
        entity.setUpdatedTime(LocalDateTime.now());
        boolean result = this.updateById(entity);

        return result;
    }

    @Override
    public boolean rejectBatch(String id, String auditComment) {
        C1BreedingBatch entity = this.getById(id);
        if (entity == null) {
            return false;
        }

        // 从登录用户获取审核人和组织信息
        String auditor = SecurityUtils.getUsername();
        String auditorOrgId = SecurityUtils.getDeptId();

        // 获取机构名称（这里假设需要从其他服务或缓存中获取，暂时使用ID作为名称）
        // 实际项目中可能需要根据 deptId 查询机构名称
        String auditorOrgName = auditorOrgId;
        entity.setAuditStatus("rejected");
        entity.setAuditor(auditor);
        entity.setAuditorOrgId(auditorOrgId);
        entity.setAuditorOrgName(auditorOrgName);
        entity.setAuditTime(LocalDateTime.now());
        entity.setAuditComment(auditComment);
        entity.setUpdatedTime(LocalDateTime.now());
        return this.updateById(entity);
    }

    @Override
    public boolean recordPrint(String id) {
        C1BreedingBatch entity = this.getById(id);
        if (entity == null) {
            return false;
        }
        Integer currentCount = entity.getPrintCount() != null ? entity.getPrintCount() : 0;
        entity.setPrintCount(currentCount + 1);
        entity.setLastPrintTime(LocalDateTime.now());
        entity.setUpdatedTime(LocalDateTime.now());
        return this.updateById(entity);
    }
}
