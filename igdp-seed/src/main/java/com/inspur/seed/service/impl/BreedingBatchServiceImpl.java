package com.inspur.seed.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.MessageUtils;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.BreedingBatch;
import com.inspur.seed.domain.dto.BreedingBatchDTO;
import com.inspur.seed.domain.entity.ApprovalComment;
import com.inspur.seed.domain.vo.BreedingBatchDetailVO;
import com.inspur.seed.mapper.BreedingBatchMapper;
import com.inspur.seed.service.IApprovalCommentService;
import com.inspur.seed.service.IBreedingBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 育种批次信息Service实现类
 *
 * @author inspur
 */
@Service
public class BreedingBatchServiceImpl extends ServiceImpl<BreedingBatchMapper, BreedingBatch> implements IBreedingBatchService {

    @Autowired
    private BreedingBatchMapper breedingBatchMapper;

    @Autowired
    private IApprovalCommentService approvalCommentService;

    @Override
    public List<BreedingBatch> selectBreedingBatchList(BreedingBatch breedingBatch) {
        List<BreedingBatch> list = breedingBatchMapper.selectBreedingBatchList(breedingBatch);
        // 为每个对象设置中文名称
        list.forEach(this::setChineseNames);
        return list;
    }

    @Override
    public List<BreedingBatch> selectBreedingBatchVoidedList(BreedingBatch breedingBatch) {
        List<BreedingBatch> breedingBatches = breedingBatchMapper.selectBreedingBatchVoidedList(breedingBatch);
        return breedingBatches;
    }

    @Override
    public BreedingBatchDetailVO selectBreedingBatchById(String dataId) {
        // 查询批次基本信息
        BreedingBatch batch = breedingBatchMapper.selectById(dataId);
        if (batch == null) {
            return null;
        }

        // 设置中文名称
        setChineseNames(batch);

        // 转换为VO对象
        BreedingBatchDetailVO detailVO = new BreedingBatchDetailVO();
        // 复制基本属性

        BeanUtil.copyProperties(batch, detailVO);

        // 查询审批意见列表
        List<ApprovalComment> approvalComments = approvalCommentService.selectApprovalCommentByDataId(dataId);

        detailVO.setApprovalComments(approvalComments);

        return detailVO;
    }

    @Override
    public String insertBreedingBatch(BreedingBatch breedingBatch) {
        // 生成主键
        String dataId = IdUtil.simpleUUID();
        breedingBatch.setDataId(dataId);

        // 生成育种批次ID: B_{cropType}_{year}_serial(6位)
        String batchId = generateBatchId(breedingBatch.getCropType(), breedingBatch.getYear());
        breedingBatch.setBatchId(batchId);

        // 设置创建信息
        breedingBatch.setCreateTime(LocalDateTime.now());
        breedingBatch.setCreateBy(SecurityUtils.getUsername());

        // 数据校验
        validateBreedingBatch(breedingBatch);

        breedingBatchMapper.insert(breedingBatch);
        return dataId;
    }

    @Override
    public int updateBreedingBatch(BreedingBatch breedingBatch) {
        // 数据校验
        validateBreedingBatch(breedingBatch);

        // 设置更新信息
        breedingBatch.setUpdateTime(LocalDateTime.now());
        breedingBatch.setUpdateBy(SecurityUtils.getUsername());

        return breedingBatchMapper.updateById(breedingBatch);
    }

    @Override
    public int deleteBreedingBatchByIds(String[] dataIds) {
//        int count = 0;
//        for (String dataId : dataIds) {
//            BreedingBatch batch = new BreedingBatch();
//            batch.setDataId(dataId);
//            batch.setIsDeleted(1);
//            count += breedingBatchMapper.updateById(batch);
//        }
//        return count;
        return breedingBatchMapper.deleteBatchIds(Arrays.asList(dataIds));
    }

    @Override
    public List<BreedingBatch> selectBatchOptions() {
        List<BreedingBatch> list = breedingBatchMapper.selectBatchOptions();
        // 为每个对象设置中文名称
        list.forEach(this::setChineseNames);
        return list;
    }

    /**
     * 生成育种批次ID
     * 格式: B_{cropType}_{year}_serial(6位)
     */
    private String generateBatchId(String cropType, Integer year) {
        if (cropType == null || cropType.isEmpty()) {
            throw new ServiceException("作物类型不能为空");
        }
        if (year == null) {
            throw new ServiceException("年份不能为空");
        }

        String batchId = breedingBatchMapper.generateBatchIdByCropTypeAndYear(cropType, year);
        if (batchId == null) {
            batchId = "B_" + cropType + "_" + year + "_000001";
        }
        return batchId;
    }

    /**
     * 设置中文名称
     */
    private void setChineseNames(BreedingBatch batch) {
        if (batch == null) {
            return;
        }
        // 设置作物类型中文名称
        batch.setCropTypeName(getCropTypeName(batch.getCropType()));
        // 设置繁育方法中文名称
        batch.setBreedingMethodName(getBreedingMethodName(batch.getBreedingMethod()));
    }

    /**
     * 获取作物类型名称（支持国际化）
     */
    private String getCropTypeName(String cropType) {
        if (cropType == null || cropType.isEmpty()) {
            return "";
        }
        // 转换为小写进行匹配
        String lowerCropType = cropType.toLowerCase();
        String messageKey = "crop.type." + lowerCropType;
        try {
            return MessageUtils.message(messageKey);
        } catch (Exception e) {
            // 如果找不到对应的国际化key，返回原值
            return cropType;
        }
    }

    /**
     * 获取繁育方法名称（支持国际化）
     */
    private String getBreedingMethodName(String breedingMethod) {
        if (breedingMethod == null || breedingMethod.isEmpty()) {
            return "";
        }
        // 转换为小写进行匹配
        String lowerMethod = breedingMethod.toLowerCase();
        String messageKey = "breeding.method." + lowerMethod;
        try {
            return MessageUtils.message(messageKey);
        } catch (Exception e) {
            // 如果找不到对应的国际化key，返回原值
            return breedingMethod;
        }
    }

    /**
     * 数据校验
     */
    private void validateBreedingBatch(BreedingBatch breedingBatch) {
//        // 批次时间校验
//        if (breedingBatch.getBatchTime() != null && breedingBatch.getStartDate() != null) {
//            if (breedingBatch.getBatchTime().after(breedingBatch.getStartDate())) {
//                throw new ServiceException("批次时间不能晚于计划起始时间");
//            }
//        }
//
//        // 起止时间校验
//        if (breedingBatch.getStartDate() != null && breedingBatch.getEndDate() != null) {
//            if (breedingBatch.getStartDate().after(breedingBatch.getEndDate())) {
//                throw new ServiceException("计划起始时间不能晚于计划结束时间");
//            }
//        }
//
//        // 繁育年份校验
//        if (breedingBatch.getYearOfDevelopment() != null) {
//            if (breedingBatch.getYearOfDevelopment() < 1900 || breedingBatch.getYearOfDevelopment() > 2100) {
//                throw new ServiceException("繁育年份需在1900-2100范围内");
//            }
//        }
    }

    @Override
    public int submitAudit(String dataId) {
        BreedingBatch batch = new BreedingBatch();
        batch.setDataId(dataId);
        batch.setWorkflowStatus("S1"); // 设置为待审核状态
        batch.setUpdateTime(LocalDateTime.now());
        batch.setUpdateBy(SecurityUtils.getUsername());
        return breedingBatchMapper.updateById(batch);
    }

    @Override
    public int approve(BreedingBatchDTO breedingBatchDTO) {
        String dataId = breedingBatchDTO.getDataId();

        // 更新育种批次状态为审核通过
        BreedingBatch batch = new BreedingBatch();
        batch.setDataId(dataId);
        batch.setWorkflowStatus("S2"); // 设置为审核通过状态
        batch.setUpdateTime(LocalDateTime.now());
        batch.setUpdateBy(SecurityUtils.getUsername());
        int result = breedingBatchMapper.updateById(batch);

        // 保存审批意见
        ApprovalComment approvalComment = breedingBatchDTO.getApprovalComment();
        if (approvalComment != null) {
            approvalComment.setDataId(dataId);
            approvalComment.setApproverId(SecurityUtils.getUserId());
            approvalComment.setApproverName(SecurityUtils.getUsername());
            approvalComment.setApprovalTime(new Date());
            approvalCommentService.insertApprovalComment(approvalComment);
        }

        return result;
    }

    @Override
    public int reject(BreedingBatchDTO breedingBatchDTO) {
        String dataId = breedingBatchDTO.getDataId();

        // 更新育种批次状态为审核驳回
        BreedingBatch batch = new BreedingBatch();
        batch.setDataId(dataId);
        batch.setWorkflowStatus("S3"); // 设置为审核驳回状态
        batch.setUpdateTime(LocalDateTime.now());
        batch.setUpdateBy(SecurityUtils.getUsername());
        int result = breedingBatchMapper.updateById(batch);

        // 保存审批意见
        ApprovalComment approvalComment = breedingBatchDTO.getApprovalComment();
        if (approvalComment != null) {
            approvalComment.setDataId(dataId);
            approvalComment.setApproverId(SecurityUtils.getUserId());
            approvalComment.setApproverName(SecurityUtils.getUsername());
            approvalComment.setApprovalTime(new Date());
            approvalCommentService.insertApprovalComment(approvalComment);
        }

        return result;
    }

    @Override
    public int archive(String dataId) {
        BreedingBatch batch = new BreedingBatch();
        batch.setDataId(dataId);
        batch.setWorkflowStatus("S9"); // 设置为已归档状态
        batch.setUpdateTime(LocalDateTime.now());
        batch.setUpdateBy(SecurityUtils.getUsername());
        return breedingBatchMapper.updateById(batch);
    }

    @Override
    public int cancel(String dataId) {
//        BreedingBatch batch = new BreedingBatch();
//        batch.setDataId(dataId);
//        batch.setIsDeleted(1);
//        batch.setUpdateTime(LocalDateTime.now());
//        batch.setUpdateBy(SecurityUtils.getUsername());
        // 已审批的状态不能删除
        LambdaQueryWrapper<BreedingBatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BreedingBatch::getDataId, dataId);
        queryWrapper.eq(BreedingBatch::getWorkflowStatus, "S2");
        long count = this.count(queryWrapper);

        if (count > 0) {
            throw new ServiceException("Approved breeding batches cannot be deleted.");
        }
        return breedingBatchMapper.deleteById(dataId);
    }


    @Override
    public boolean finished(String batchId) {
        // 更新 Breeding Batch 的状态
        BreedingBatch breedingBatch = new BreedingBatch();
        breedingBatch.setStatus("Finished");
        LambdaQueryWrapper<BreedingBatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BreedingBatch::getBatchId, batchId);
        return this.update(breedingBatch, queryWrapper);
    }
}
