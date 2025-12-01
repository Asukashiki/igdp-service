package com.inspur.seed.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.dto.BreedingDatasetDTO;
import com.inspur.seed.domain.dto.BreedingDatasetQueryDTO;
import com.inspur.seed.domain.entity.BreedingDataset;
import com.inspur.seed.domain.vo.BreedingDatasetVO;
import com.inspur.seed.mapper.BreedingDatasetMapper;
import com.inspur.seed.domain.entity.BreedingDatasetAudit;
import com.inspur.seed.mapper.BreedingDatasetAuditMapper;
import com.inspur.seed.service.IBreedingDatasetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 育种数据集Service实现类
 *
 * @author system
 * @date 2025-01-30
 */
@Slf4j
@Service
public class BreedingDatasetServiceImpl extends ServiceImpl<BreedingDatasetMapper, BreedingDataset>
        implements IBreedingDatasetService {

    @Autowired
    private BreedingDatasetAuditMapper auditMapper;

    @Override
    public AjaxResult getDatasetList(BreedingDatasetQueryDTO queryDTO) {
        try {
            // 构建查询条件
            QueryWrapper<BreedingDataset> wrapper = new QueryWrapper<>();
            wrapper.eq("deleted", "0");

            // 批次ID
            if (StrUtil.isNotBlank(queryDTO.getBatchId())) {
                wrapper.eq("batch_id", queryDTO.getBatchId());
            }

            // 批次名称模糊查询
            if (StrUtil.isNotBlank(queryDTO.getBatchName())) {
                wrapper.like("batch_name", queryDTO.getBatchName());
            }

            // 作物类型
            if (StrUtil.isNotBlank(queryDTO.getCropType())) {
                wrapper.eq("crop_type", queryDTO.getCropType());
            }

            // 品种名称模糊查询
            if (StrUtil.isNotBlank(queryDTO.getVarietyName())) {
                wrapper.like("variety_name", queryDTO.getVarietyName());
            }

            // 数据集状态
            if (StrUtil.isNotBlank(queryDTO.getDatasetStatus())) {
                wrapper.eq("dataset_status", queryDTO.getDatasetStatus());
            }

            // 时间范围
            if (StrUtil.isNotBlank(queryDTO.getStartTime())) {
                wrapper.ge("created_time", queryDTO.getStartTime());
            }
            if (StrUtil.isNotBlank(queryDTO.getEndTime())) {
                wrapper.le("created_time", queryDTO.getEndTime());
            }

            // 按创建时间倒序
            wrapper.orderByDesc("created_time");

            // 分页查询
            Integer pageNum = queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1;
            Integer pageSize = queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10;
            Page<BreedingDataset> page = new Page<>(pageNum, pageSize);

            Page<BreedingDataset> result = this.page(page, wrapper);

            // 转换为VO
            List<BreedingDatasetVO> voList = result.getRecords().stream()
                    .map(entity -> BeanUtil.copyProperties(entity, BreedingDatasetVO.class))
                    .collect(Collectors.toList());

            Map<String, Object> data = new HashMap<>();
            data.put("list", voList);
            data.put("total", result.getTotal());

            return AjaxResult.success("查询成功", data);
        } catch (Exception e) {
            log.error("查询育种数据集列表失败", e);
            return AjaxResult.error("查询失败:" + e.getMessage());
        }
    }

    @Override
    public AjaxResult getDatasetById(String id) {
        try {
            BreedingDataset dataset = this.getById(id);
            if (dataset == null || "1".equals(dataset.getDeleted())) {
                return AjaxResult.error("数据集不存在");
            }

            BreedingDatasetVO vo = BeanUtil.copyProperties(dataset, BreedingDatasetVO.class);
            return AjaxResult.success("查询成功", vo);
        } catch (Exception e) {
            log.error("查询育种数据集详情失败", e);
            return AjaxResult.error("查询失败:" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addDataset(BreedingDatasetDTO dto) {
        try {
            // 验证必填字段
            if (StrUtil.isBlank(dto.getBatchId())) {
                return AjaxResult.error("批次ID不能为空");
            }

            BreedingDataset dataset = new BreedingDataset();
            BeanUtil.copyProperties(dto, dataset);

            // 设置默认值
            dataset.setId(IdUtil.simpleUUID());
            dataset.setDatasetStatus("draft");
            dataset.setTrialCount(0);
            dataset.setFieldDataCount(0);
            dataset.setEnvDataCount(0);
            dataset.setLabTestCount(0);
            dataset.setYieldDataCount(0);
            dataset.setStatus("1");
            dataset.setDeleted("0");
            dataset.setCreatedTime(LocalDateTime.now());

            // TODO: 设置创建人信息(从当前登录用户获取)
            dataset.setCreatedBy(SecurityUtils.getUsername());
            // dataset.setCreatedBy(currentUserId);
            // dataset.setCreatedByName(currentUserName);
            // dataset.setCreatedOrgCode(currentUserOrgCode);
            // dataset.setCreatedOrgName(currentUserOrgName);

            this.save(dataset);

            return AjaxResult.success("新增成功", dataset.getId());
        } catch (Exception e) {
            log.error("新增育种数据集失败", e);
            return AjaxResult.error("新增失败:" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateDataset(BreedingDatasetDTO dto) {
        try {
            if (StrUtil.isBlank(dto.getId())) {
                return AjaxResult.error("数据集ID不能为空");
            }

            BreedingDataset dataset = this.getById(dto.getId());
            if (dataset == null || "1".equals(dataset.getDeleted())) {
                return AjaxResult.error("数据集不存在");
            }

            // 已审核通过或审核中的数据集不允许修改
            if (!"draft".equals(dataset.getDatasetStatus()) && !"rejected".equals(dataset.getDatasetStatus())) {
                return AjaxResult.error("只有草稿或驳回状态的数据集可以修改");
            }

            // 更新字段(允许修改批次ID和冗余字段)
            if (StrUtil.isNotBlank(dto.getBatchId())) {
                dataset.setBatchId(dto.getBatchId());
            }
            if (StrUtil.isNotBlank(dto.getBatchName())) {
                dataset.setBatchName(dto.getBatchName());
            }
            if (StrUtil.isNotBlank(dto.getCropType())) {
                dataset.setCropType(dto.getCropType());
            }
            if (StrUtil.isNotBlank(dto.getVarietyName())) {
                dataset.setVarietyName(dto.getVarietyName());
            }
            if (StrUtil.isNotBlank(dto.getRemark())) {
                dataset.setRemark(dto.getRemark());
            }

            dataset.setUpdatedTime(LocalDateTime.now());
            // TODO: 设置更新人信息
            // dataset.setUpdatedBy(currentUserId);

            this.updateById(dataset);

            return AjaxResult.success("修改成功");
        } catch (Exception e) {
            log.error("修改育种数据集失败", e);
            return AjaxResult.error("修改失败:" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult deleteDataset(String[] ids) {
        try {
            if (ids == null || ids.length == 0) {
                return AjaxResult.error("请选择要删除的数据集");
            }

            List<BreedingDataset> datasets = this.listByIds(Arrays.asList(ids));

            if (datasets == null || datasets.isEmpty()) {
                return AjaxResult.error("数据集不存在");
            }

            // 检查状态
            for (BreedingDataset dataset : datasets) {
                if (!"draft".equals(dataset.getDatasetStatus()) && !"rejected".equals(dataset.getDatasetStatus())) {
                    return AjaxResult.error("只能删除草稿或驳回状态的数据集,当前状态:" + dataset.getDatasetStatus());
                }
            }

            // 使用MyBatis-Plus的removeByIds方法进行逻辑删除
            boolean success = this.removeByIds(Arrays.asList(ids));
            
            if (!success) {
                log.error("删除数据集失败");
                return AjaxResult.error("删除失败");
            }

            log.info("成功删除 {} 条数据集记录", ids.length);
            return AjaxResult.success("删除成功");
        } catch (Exception e) {
            log.error("删除育种数据集失败", e);
            return AjaxResult.error("删除失败:" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public  AjaxResult submitDataset(String id) {
        try {
            BreedingDataset dataset = this.getById(id);
            if (dataset == null || "1".equals(dataset.getDeleted())) {
                return AjaxResult.error("数据集不存在");
            }

            if (!"draft".equals(dataset.getDatasetStatus()) && !"rejected".equals(dataset.getDatasetStatus())) {
                return AjaxResult.error("只有草稿或驳回状态的数据集可以提交");
            }

            // 检查数据完整性 - 放宽限制，允许暂无数据时提交
            // if (dataset.getLabTestCount() == null || dataset.getLabTestCount() == 0) {
            //     return AjaxResult.error("至少需要一条实验室检测记录才能提交");
            // }
            // if (dataset.getYieldDataCount() == null || dataset.getYieldDataCount() == 0) {
            //     return AjaxResult.error("至少需要一条产量数据记录才能提交");
            // }

            // 更新状态
            dataset.setDatasetStatus("submitted");
            LocalDateTime submitTime = LocalDateTime.now();
            dataset.setSubmitTime(submitTime);
            dataset.setCreatedBy(SecurityUtils.getUsername());


            // 设置提交人信息(从当前登录用户获取)
            String submitBy = null;
            String submitByName = null;
            try {
                submitBy = SecurityUtils.getUsername();
                submitByName = submitBy; // 如果没有用户名,使用用户ID
                dataset.setSubmitBy(submitBy);
                dataset.setSubmitByName(submitByName);
            } catch (Exception ex) {
                log.warn("获取当前用户信息失败,使用创建人信息", ex);
                // 使用创建人信息作为提交人
                submitBy = dataset.getCreatedBy();
                submitByName = dataset.getCreatedByName() != null ? dataset.getCreatedByName() : dataset.getCreatedBy();
                dataset.setSubmitBy(submitBy);
                dataset.setSubmitByName(submitByName);
            }

            dataset.setUpdatedTime(LocalDateTime.now());

            // 更新数据集
            boolean updateSuccess = this.updateById(dataset);
            if (!updateSuccess) {
                log.error("更新数据集状态失败,数据集ID: {}", id);
                return AjaxResult.error("提交失败:更新数据集状态失败");
            }

            // 查询是否已存在审核记录
            QueryWrapper<BreedingDatasetAudit> wrapper = new QueryWrapper<>();
            wrapper.eq("dataset_id", dataset.getId());
            wrapper.eq("deleted", "0");
            wrapper.orderByDesc("created_time");
            wrapper.last("LIMIT 1");
            BreedingDatasetAudit existingAudit = auditMapper.selectOne(wrapper);

            if (existingAudit != null) {
                // 如果是被驳回后重新提交,更新现有审核记录
                existingAudit.setAuditStatus("pending"); // 重置为待审核
                existingAudit.setAuditOpinion(null); // 清空之前的审核意见
                existingAudit.setAuditTime(null); // 清空审核时间
                existingAudit.setAuditorId(null); // 清空审核人
                existingAudit.setAuditorName(null);
                existingAudit.setSubmitTime(submitTime); // 更新提交时间
                existingAudit.setSubmitterId(submitBy);
                existingAudit.setSubmitterName(submitByName);
                existingAudit.setUpdatedTime(LocalDateTime.now());
                existingAudit.setUpdatedBy(submitBy);

                int updateResult = auditMapper.updateById(existingAudit);
                if (updateResult <= 0) {
                    log.error("更新审核记录失败,数据集ID: {}", id);
                    throw new RuntimeException("更新审核记录失败");
                }
                log.info("数据集重新提交成功,数据集ID: {}, 审核记录ID: {}", id, existingAudit.getId());
            } else {
                // 首次提交,创建新的审核记录
                BreedingDatasetAudit audit = new BreedingDatasetAudit();
                audit.setId(IdUtil.simpleUUID());
                audit.setDatasetId(dataset.getId());
                audit.setBatchId(dataset.getBatchId());
                audit.setAuditNode("数据集审核");
                audit.setAuditOrder(1);
                audit.setAuditStatus("pending");
                audit.setSubmitTime(submitTime);
                audit.setSubmitterId(submitBy);
                audit.setSubmitterName(submitByName);
                audit.setStatus("1");
                audit.setDeleted("0");
                audit.setCreatedTime(LocalDateTime.now());
                audit.setCreatedBy(submitBy); // 设置创建人
                audit.setUpdatedBy(submitBy); // 设置更新人
                audit.setUpdatedTime(LocalDateTime.now()); // 设置更新时间

                int auditInsertResult = auditMapper.insert(audit);
                if (auditInsertResult <= 0) {
                    log.error("创建审核记录失败,数据集ID: {}", id);
                    throw new RuntimeException("创建审核记录失败");
                }
                log.info("数据集首次提交成功,数据集ID: {}, 审核记录ID: {}", id, audit.getId());
            }

            return AjaxResult.success("提交成功");
        } catch (Exception e) {
            log.error("提交育种数据集失败", e);
            return AjaxResult.error("提交失败:" + e.getMessage());
        }
    }

    @Override
    public AjaxResult statisticsData(String batchId) {
        try {
            if (StrUtil.isBlank(batchId)) {
                return AjaxResult.error("育种批次ID不能为空");
            }

            Map<String, Object> statistics = new HashMap<>();

            // TODO: 统计各项数据记录数
            // 1. 查询试验记录数 (从breeding_trial表)
            // 2. 查询田间数据记录数 (从相关表)
            // 3. 查询环境数据记录数 (从相关表)
            // 4. 查询实验室检测记录数 (从breeding_lab_test表)
            // 5. 查询产量数据记录数 (从breeding_yield_data表)

            // 暂时返回模拟数据
            statistics.put("trialCount", 0);
            statistics.put("fieldDataCount", 0);
            statistics.put("envDataCount", 0);
            statistics.put("labTestCount", 0);
            statistics.put("yieldDataCount", 0);

            return AjaxResult.success("统计成功", statistics);
        } catch (Exception e) {
            log.error("统计数据失败", e);
            return AjaxResult.error("统计失败:" + e.getMessage());
        }
    }
}
