package com.inspur.agriculture.input.service.demand.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.inspur.agriculture.input.domain.demand.DemandInputSummaryAdjustmentHistory;
import com.inspur.agriculture.input.domain.demand.DemandInputSummaryItem;
import com.inspur.agriculture.input.domain.oauth.PubRegion;
import com.inspur.agriculture.input.dto.demand.DemandInputSummaryItemDTO;
import com.inspur.agriculture.input.dto.demand.DemandInputSummaryItemQueryDTO;
import com.inspur.agriculture.input.dto.demand.DemandOrganDTO;
import com.inspur.agriculture.input.dto.demand.DemandSummaryDetailAdjustDTO;
import com.inspur.agriculture.input.dto.demand.DemandSummaryDetailSubmitDTO;
import com.inspur.agriculture.input.mapper.demand.DemandInputSummaryAdjustmentHistoryMapper;
import com.inspur.agriculture.input.mapper.demand.DemandInputSummaryItemMapper;
import com.inspur.agriculture.input.mapper.oauth.PubRegionMapper;
import com.inspur.agriculture.input.service.demand.IDemandInputSummaryItemService;
import com.inspur.agriculture.input.service.demand.IDemandInputSummaryService;
import com.inspur.agriculture.input.vo.demand.DemandInputSummaryItemVO;
import com.inspur.agriculture.input.vo.demand.DemandSummaryAdjustmentHistoryVO;
import com.inspur.agriculture.input.vo.demand.InputAggregationSummaryVO;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * 农资汇聚统计 Service实现
 *
 * @author inspur
 * @date 2025-12-09
 */
@Slf4j
@Service
public class DemandInputSummaryItemServiceImpl implements IDemandInputSummaryItemService {

    @Autowired
    private DemandInputSummaryItemMapper demandInputSummaryItemMapper;

    @Autowired
    private DemandInputSummaryAdjustmentHistoryMapper adjustmentHistoryMapper;

    @Autowired
    private PubRegionMapper regionMapper;

    @Autowired
    private ObjectProvider<IDemandInputSummaryService> demandInputSummaryServiceProvider;

    @Override
    public List<DemandInputSummaryItemVO> getDemandInputSummaryItemList(DemandInputSummaryItemQueryDTO queryDTO) {
        return demandInputSummaryItemMapper.selectDemandInputSummaryItemList(queryDTO);
    }

    @Override
    public DemandInputSummaryItemVO getDemandInputSummaryItemById(String id) {
        return demandInputSummaryItemMapper.selectDemandInputSummaryItemById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public DemandInputSummaryItemVO adjustDetail(DemandSummaryDetailAdjustDTO dto) {
        DemandInputSummaryItem detail = getAndValidateDetail(dto.getDetailId(), dto.getSummaryId(),
                dto.getYear(), dto.getSourceCode(), dto.getTargetCode());

        BigDecimal originalQuantity = defaultQuantity(detail.getTotalQuantity());
        BigDecimal beforeQuantity = isAdjusted(detail) ? defaultQuantity(detail.getAdjustedQuantity()) : BigDecimal.ZERO;

        detail.setAdjustedQuantity(dto.getAdjustedQuantity());
        detail.setHasAdjustment(1);
        detail.setAdjustmentRemark(dto.getAdjustmentRemark());
        detail.setAdjustmentStatus("adjusted");
        detail.setUpdatedTime(DateUtils.getNowDate());
        demandInputSummaryItemMapper.updateById(detail);

        insertHistory(detail, dto.getYear(), originalQuantity, beforeQuantity, dto.getAdjustedQuantity(),
                dto.getAdjustmentRemark(), dto.getCurrentUserId(), dto.getCurrentUserName(), "adjust");

        return demandInputSummaryItemMapper.selectDemandInputSummaryItemById(detail.getId());
    }

    @Override
    public List<DemandSummaryAdjustmentHistoryVO> getAdjustmentHistory(String detailId) {
        if (StrUtil.isBlank(detailId)) {
            throw new ServiceException("Detail ID cannot be empty");
        }
        return adjustmentHistoryMapper.selectHistoryByDetailId(detailId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public DemandInputSummaryItemVO submitDetailToZone(DemandSummaryDetailSubmitDTO dto) {
        DemandInputSummaryItem detail = getAndValidateDetail(dto.getDetailId(), dto.getSummaryId(),
                dto.getYear(), dto.getSourceCode(), dto.getTargetCode());

        if ("submitted".equals(detail.getAdjustmentStatus())) {
            return demandInputSummaryItemMapper.selectDemandInputSummaryItemById(detail.getId());
        }

        BigDecimal submitQuantity = isAdjusted(detail)
                ? defaultQuantity(detail.getAdjustedQuantity())
                : defaultQuantity(detail.getTotalQuantity());

        detail.setAdjustmentStatus("submitted");
        detail.setSubmittedToZoneTime(DateUtils.getNowDate());
        detail.setUpdatedTime(DateUtils.getNowDate());
        demandInputSummaryItemMapper.updateById(detail);

        insertHistory(detail, dto.getYear(), defaultQuantity(detail.getTotalQuantity()), submitQuantity, submitQuantity,
                "Submitted to Zone", dto.getCurrentUserId(), dto.getCurrentUserName(), "submit_to_zone");

        boolean processSuccess = demandInputSummaryServiceProvider.getObject().processLevelRecord(
                dto.getTargetCode(), dto.getLevel(), dto.getYear());
        if (!processSuccess) {
            log.warn("Failed to process demand input summary level record, sourceCode: {}, level: {}, year: {}",
                    dto.getTargetCode(), dto.getLevel(), dto.getYear());
        }

        return demandInputSummaryItemMapper.selectDemandInputSummaryItemById(detail.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addDemandInputSummaryItem(DemandInputSummaryItemDTO dto) {
        // 检查农资分类和类型组合是否已存在
        // DTO转Entity
        DemandInputSummaryItem item = new DemandInputSummaryItem();
        BeanUtils.copyProperties(dto, item);

        // 设置默认值
        if (item.getDemandCount() == null) {
            item.setDemandCount(0);
        }
        if (item.getStatus() == null || item.getStatus().isEmpty()) {
            item.setStatus("0"); // 默认待审核
        }
        item.setCreatedTime(DateUtils.getNowDate());
        item.setUpdatedTime(DateUtils.getNowDate());

        // 插入数据
        return demandInputSummaryItemMapper.insert(item);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateDemandInputSummaryItem(DemandInputSummaryItemDTO dto) {
        if (dto.getId() == null || dto.getId().isEmpty()) {
            throw new ServiceException("主键ID不能为空");
        }

        // 查询原记录
        DemandInputSummaryItem item = demandInputSummaryItemMapper.selectById(dto.getId());
        if (item == null) {
            throw new ServiceException("农资汇聚统计记录不存在");
        }

        // 检查农资分类和类型组合是否已存在(排除当前记录)
        int count = demandInputSummaryItemMapper.checkCategoryTypeExists(
                dto.getInputCategory(),
                dto.getInputType(),
                dto.getId()
        );
        if (count > 0) {
            throw new ServiceException("该农资分类和类型组合已存在");
        }

        // DTO转Entity
        BeanUtils.copyProperties(dto, item);

        // 设置更新时间
        item.setUpdatedTime(DateUtils.getNowDate());

        // 更新数据
        return demandInputSummaryItemMapper.updateById(item);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteDemandInputSummaryItem(String id) {
        // 查询原记录
        DemandInputSummaryItem item = demandInputSummaryItemMapper.selectById(id);
        if (item == null) {
            throw new ServiceException("农资汇聚统计记录不存在");
        }

        // 物理删除(根据表结构没有del_flag字段)
        return demandInputSummaryItemMapper.deleteById(id);
    }

    @Override
    public int deleteDeandInputItemBySummaryId(String summaryId){
        DemandInputSummaryItem item = new DemandInputSummaryItem();
        item.setSummaryId(summaryId);
        QueryWrapper<DemandInputSummaryItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("summary_id",summaryId);

        return demandInputSummaryItemMapper.delete(queryWrapper);
    }



    @Override
    public int updateDemandItemStatus(String summaryId){
        // 将指定汇总ID下的所有明细状态更新为“成功”（状态码：1）
        DemandInputSummaryItem entity = new DemandInputSummaryItem();
        entity.setStatus("1");

        QueryWrapper<DemandInputSummaryItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("summary_id", summaryId);

        return demandInputSummaryItemMapper.update(entity, queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int batchDeleteDemandInputSummaryItem(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("删除的ID列表不能为空");
        }

        // 批量物理删除
        QueryWrapper<DemandInputSummaryItem> wrapper = new QueryWrapper<>();
        wrapper.in("id", ids);
        return demandInputSummaryItemMapper.delete(wrapper);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public int submitInputAggregation(DemandOrganDTO demandOrganDTO) {

        //根据sourceCode获取上级区划code、name
        PubRegion region = regionMapper.selectByRegionCode(demandOrganDTO.getSourceCode());
        String sourceCode = demandOrganDTO.getSourceCode();
        String sourceName = region.getName();
        String targetCode = region.getParentCode();
        PubRegion fatherRegion = regionMapper.selectByRegionCode(targetCode);
        String targetName = fatherRegion.getName();

        // 从汇聚统计表中查询并再次汇总（二次汇聚）
        List<InputAggregationSummaryVO> aggregationList = demandInputSummaryItemMapper.getInputAggregation(demandOrganDTO);
        int count = 0;
        for (InputAggregationSummaryVO aggregation : aggregationList){
            DemandInputSummaryItem item = new DemandInputSummaryItem();
            item.setSeason(aggregation.getSeason());
            item.setInputCategory(aggregation.getInputCategory());
            item.setInputType(aggregation.getInputType());
            item.setTotalCount(aggregation.getTotalCount());
            item.setTotalQuantity(aggregation.getTotalQuantity());
            item.setVarieties(aggregation.getVariety());
            item.setSourceCode(sourceCode);
            item.setSourceName(sourceName);
            item.setTargetCode(targetCode);
            item.setTargetName(targetName);
            item.setSummaryId(demandOrganDTO.getSummaryId());
            int tempCount = demandInputSummaryItemMapper.insert(item);
            count+=tempCount;
        }
        if (count > 0) {
            boolean processSuccess = demandInputSummaryServiceProvider.getObject().processLevelRecord(
                    demandOrganDTO.getTargetCode(), demandOrganDTO.getLevel(), demandOrganDTO.getYear());
            if (!processSuccess) {
                log.warn("Failed to process demand input summary level record, sourceCode: {}, level: {}, year: {}",
                        demandOrganDTO.getTargetCode(), demandOrganDTO.getLevel(), demandOrganDTO.getYear());
            }
        }
        return count;
    }


    @Override
    public List<InputAggregationSummaryVO> getInputAggregation(DemandOrganDTO demandOrganDTO) {
        return demandInputSummaryItemMapper.getInputAggregation(demandOrganDTO);
    }
    @Override
    public List<InputAggregationSummaryVO> getInputAggregationZone(DemandOrganDTO demandOrganDTO) {
        return demandInputSummaryItemMapper.getInputAggregationZone(demandOrganDTO);
    }

    private DemandInputSummaryItem getAndValidateDetail(String detailId, String summaryId, String year,
                                                        String sourceCode, String targetCode) {
        DemandInputSummaryItem detail = demandInputSummaryItemMapper.selectById(detailId);
        if (detail == null) {
            throw new ServiceException("Demand summary detail does not exist");
        }
        if (!Objects.equals(detail.getSummaryId(), summaryId)) {
            throw new ServiceException("Summary ID does not match");
        }
        if (!Objects.equals(detail.getSourceCode(), sourceCode)) {
            throw new ServiceException("Source code does not match");
        }
        if (!Objects.equals(detail.getTargetCode(), targetCode)) {
            throw new ServiceException("Target code does not match");
        }
        if (StrUtil.isBlank(year)) {
            throw new ServiceException("Year cannot be empty");
        }
        return detail;
    }

    private void insertHistory(DemandInputSummaryItem detail, String year, BigDecimal originalQuantity,
                               BigDecimal beforeQuantity, BigDecimal afterQuantity, String remark,
                               String operatorId, String operatorName, String operationType) {
        DemandInputSummaryAdjustmentHistory history = new DemandInputSummaryAdjustmentHistory();
        history.setDetailId(detail.getId());
        history.setSummaryId(detail.getSummaryId());
        history.setYear(year);
        history.setSourceCode(detail.getSourceCode());
        history.setSourceName(detail.getSourceName());
        history.setTargetCode(detail.getTargetCode());
        history.setTargetName(detail.getTargetName());
        history.setInputType(detail.getInputType());
        history.setInputCategory(detail.getInputCategory());
        history.setVariety(detail.getVarieties());
        history.setSeason(detail.getSeason());
        history.setUnit(detail.getUnits());
        history.setOriginalQuantity(originalQuantity);
        history.setBeforeQuantity(beforeQuantity);
        history.setAfterQuantity(afterQuantity);
        history.setRemark(remark);
        history.setOperatorId(operatorId);
        history.setOperatorName(operatorName);
        history.setOperationType(operationType);
        history.setCreatedTime(DateUtils.getNowDate());
        history.setDeleted(0);
        adjustmentHistoryMapper.insert(history);
    }

    private boolean isAdjusted(DemandInputSummaryItem detail) {
        return detail.getHasAdjustment() != null && detail.getHasAdjustment() == 1;
    }

    private BigDecimal defaultQuantity(BigDecimal quantity) {
        return quantity == null ? BigDecimal.ZERO : quantity;
    }
}
