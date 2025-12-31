package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.seed.domain.entity.C1BreedingTest;
import com.inspur.seed.mapper.C1BreedingTestMapper;
import com.inspur.seed.service.IC1BreedingTestService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class C1BreedingTestServiceImpl extends ServiceImpl<C1BreedingTestMapper, C1BreedingTest> 
        implements IC1BreedingTestService {

    @Override
    public IPage<C1BreedingTest> pageList(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
        
        Page<C1BreedingTest> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<C1BreedingTest> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(C1BreedingTest::getDeleted, "0");
        
        if (params.get("batchId") != null && StringUtils.hasText(params.get("batchId").toString())) {
            wrapper.eq(C1BreedingTest::getBatchId, params.get("batchId").toString());
        }
        if (params.get("trackingId") != null && StringUtils.hasText(params.get("trackingId").toString())) {
            wrapper.eq(C1BreedingTest::getTrackingId, params.get("trackingId").toString());
        }
        
        wrapper.orderByDesc(C1BreedingTest::getCreatedTime);
        return this.page(page, wrapper);
    }

    @Override
    public boolean add(C1BreedingTest entity) {
        // 根据种子级别生成不同的检测编号前缀
        String prefix;
        if ("Basic".equalsIgnoreCase(entity.getSeedClass())) {
            prefix = "BTS"; // Breeder seed Test
        } else {
            prefix = "C1TS"; // C1 Test
        }

        String testId = prefix + "-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-" + String.format("%04d", (int)(Math.random() * 10000));
        entity.setTestId(testId);
        entity.setDeleted("0");
        entity.setCreatedTime(LocalDateTime.now());
        return this.save(entity);
    }

    @Override
    public boolean update(C1BreedingTest entity) {
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
    public C1BreedingTest getDetailById(String id) {
        C1BreedingTest entity = this.getById(id);
        if (entity != null && "1".equals(entity.getDeleted())) return null;
        return entity;
    }
}
