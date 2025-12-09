package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.seed.domain.entity.C1BreedingTracking;
import com.inspur.seed.mapper.C1BreedingTrackingMapper;
import com.inspur.seed.service.IC1BreedingTrackingService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class C1BreedingTrackingServiceImpl extends ServiceImpl<C1BreedingTrackingMapper, C1BreedingTracking> 
        implements IC1BreedingTrackingService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public IPage<C1BreedingTracking> pageList(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
        
        Page<C1BreedingTracking> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<C1BreedingTracking> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(C1BreedingTracking::getDeleted, "0");
        
        if (params.get("batchId") != null && StringUtils.hasText(params.get("batchId").toString())) {
            wrapper.eq(C1BreedingTracking::getBatchId, params.get("batchId").toString());
        }
        if (params.get("stageName") != null && StringUtils.hasText(params.get("stageName").toString())) {
            wrapper.eq(C1BreedingTracking::getStageName, params.get("stageName").toString());
        }
        
        wrapper.orderByDesc(C1BreedingTracking::getCreatedTime);
        return this.page(page, wrapper);
    }

    @Override
    public boolean add(C1BreedingTracking entity) {
        // 生成跟踪编号
        String trackingId = "C1T-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) 
                + "-" + String.format("%04d", (int)(Math.random() * 10000));
        entity.setTrackingId(trackingId);
        entity.setTestCount(0);
        entity.setDeleted("0");
        entity.setCreatedTime(LocalDateTime.now());
        return this.save(entity);
    }

    @Override
    public boolean update(C1BreedingTracking entity) {
        entity.setUpdatedTime(LocalDateTime.now());
        return this.updateById(entity);
    }

    @Override
    public boolean deleteByIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) return false;
        return this.update()
                .set("deleted", "1")
                .set("updated_time", LocalDateTime.now())
                .in("id", ids)
                .update();
    }

    @Override
    public C1BreedingTracking getDetailById(String id) {
        C1BreedingTracking entity = this.getById(id);
        if (entity != null && "1".equals(entity.getDeleted())) return null;
        return entity;
    }
}
