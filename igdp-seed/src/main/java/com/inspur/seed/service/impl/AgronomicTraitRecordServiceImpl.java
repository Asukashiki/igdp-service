package com.inspur.seed.service.impl;

import com.inspur.common.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.inspur.seed.domain.dto.AgronomicTraitDetailDTO;
import com.inspur.seed.domain.dto.AgronomicTraitRecordDTO;
import com.inspur.seed.domain.entity.AgronomicTraitDetail;
import com.inspur.seed.domain.entity.AgronomicTraitRecord;
import com.inspur.seed.domain.vo.AgronomicTraitDetailVO;
import com.inspur.seed.domain.vo.AgronomicTraitRecordVO;
import com.inspur.seed.mapper.AgronomicTraitDetailMapper;
import com.inspur.seed.mapper.AgronomicTraitRecordMapper;
import com.inspur.seed.service.IAgronomicTraitRecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 农艺性状采集主记录Service实现
 * 
 * @author inspur
 */
@Service
public class AgronomicTraitRecordServiceImpl implements IAgronomicTraitRecordService {

    @Autowired
    private AgronomicTraitRecordMapper recordMapper;

    @Autowired
    private AgronomicTraitDetailMapper detailMapper;

    @Override
    public List<AgronomicTraitRecord> selectRecordList(AgronomicTraitRecord record) {
        return recordMapper.selectRecordList(record);
    }

    @Override
    public AgronomicTraitRecordVO selectRecordById(String recordId) {
        // 查询主记录（含明细）
        AgronomicTraitRecord record = recordMapper.selectRecordByIdWithDetails(recordId);
        if (record == null) {
            return null;
        }

        // 转换为VO
        AgronomicTraitRecordVO vo = new AgronomicTraitRecordVO();
        BeanUtils.copyProperties(record, vo);

        // 转换明细列表
        if (!CollectionUtils.isEmpty(record.getDetailList())) {
            List<AgronomicTraitDetailVO> detailVOList = record.getDetailList().stream()
                    .map(detail -> {
                        AgronomicTraitDetailVO detailVO = new AgronomicTraitDetailVO();
                        BeanUtils.copyProperties(detail, detailVO);
                        return detailVO;
                    })
                    .collect(Collectors.toList());
            vo.setDetailList(detailVOList);
            vo.setTraitCount(detailVOList.size());
        }

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertRecord(AgronomicTraitRecordDTO dto) {
        String username = SecurityUtils.getUsername();
        LocalDateTime now = LocalDateTime.now();

        // 生成记录ID
        String recordId = generateRecordId(dto.getPlotId());

        // 构建主记录实体
        AgronomicTraitRecord record = new AgronomicTraitRecord();
        BeanUtils.copyProperties(dto, record);
        record.setRecordId(recordId);
        record.setCreateBy(username);
        record.setCreateTime(now);
        record.setUpdateBy(username);
        record.setUpdateTime(now);

        // 默认状态
        if (record.getStatus() == null) {
            record.setStatus("draft");
        }

        // 插入主记录
        recordMapper.insert(record);

        // 插入明细列表
        if (!CollectionUtils.isEmpty(dto.getDetailList())) {
            insertDetailList(recordId, dto.getDetailList(), username, now);
        }

        return recordId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRecord(AgronomicTraitRecordDTO dto) {
        String username = SecurityUtils.getUsername();
        LocalDateTime now = LocalDateTime.now();

        // 更新主记录
        AgronomicTraitRecord record = new AgronomicTraitRecord();
        BeanUtils.copyProperties(dto, record);
        record.setUpdateBy(username);
        record.setUpdateTime(now);

        int result = recordMapper.updateById(record);

        // 删除旧明细
        detailMapper.deleteDetailsByRecordId(dto.getRecordId());

        // 插入新明细
        if (!CollectionUtils.isEmpty(dto.getDetailList())) {
            insertDetailList(dto.getRecordId(), dto.getDetailList(), username, now);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRecordByIds(String[] recordIds) {
        LocalDateTime now = LocalDateTime.now();
        int count = 0;

        for (String recordId : recordIds) {
            // 逻辑删除主记录
            LambdaUpdateWrapper<AgronomicTraitRecord> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(AgronomicTraitRecord::getRecordId, recordId)
                    .set(AgronomicTraitRecord::getIsDeleted, 1)
                    .set(AgronomicTraitRecord::getUpdateTime, now);
            count += recordMapper.update(null, wrapper);

            // 逻辑删除明细
            detailMapper.deleteDetailsByRecordId(recordId);
        }

        return count;
    }

    @Override
    public String generateRecordId(String plotId) {
        Integer sequence = recordMapper.generateRecordSequence(plotId);
        if (sequence == null) {
            sequence = 1;
        }
        return String.format("%s-TR%03d", plotId, sequence);
    }

    /**
     * 插入明细列表
     */
    private void insertDetailList(String recordId, List<AgronomicTraitDetailDTO> detailDTOList, 
                                   String username, LocalDateTime now) {
        List<AgronomicTraitDetail> detailList = new ArrayList<>();
        
        // 获取起始序号（基于所有记录，包括已删除的）
        Integer startSequence = detailMapper.generateDetailSequence(recordId);
        if (startSequence == null) {
            startSequence = 1;
        }
        
        for (int i = 0; i < detailDTOList.size(); i++) {
            AgronomicTraitDetailDTO detailDTO = detailDTOList.get(i);
            
            // 生成明细ID，使用数据库序号
            String detailId = String.format("%s-D%03d", recordId, startSequence + i);
            
            AgronomicTraitDetail detail = new AgronomicTraitDetail();
            BeanUtils.copyProperties(detailDTO, detail);
            detail.setDetailId(detailId);
            detail.setRecordId(recordId);
            detail.setSortOrder(i + 1);
            detail.setCreateBy(username);
            detail.setCreateTime(now);
            detail.setUpdateBy(username);
            detail.setUpdateTime(now);
            
            detailList.add(detail);
        }

        if (!detailList.isEmpty()) {
            detailMapper.batchInsertDetails(detailList);
        }
    }
}
