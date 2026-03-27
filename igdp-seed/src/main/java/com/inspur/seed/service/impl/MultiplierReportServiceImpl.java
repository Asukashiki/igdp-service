package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.common.utils.StringUtils;
import com.inspur.seed.domain.MultiplierReport;
import com.inspur.seed.mapper.MultiplierReportMapper;
import com.inspur.seed.service.IMultiplierReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MultiplierReportServiceImpl extends ServiceImpl<MultiplierReportMapper, MultiplierReport>
        implements IMultiplierReportService {

    @Override
    public List<MultiplierReport> selectList(MultiplierReport query) {
        LambdaQueryWrapper<MultiplierReport> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(query.getKeyword())) {
            wrapper.and(w -> w
                    .like(MultiplierReport::getMultiplierId, query.getKeyword())
                    .or().like(MultiplierReport::getDistributionId, query.getKeyword())
                    .or().like(MultiplierReport::getVarietyName, query.getKeyword())
                    .or().like(MultiplierReport::getCertificateId, query.getKeyword()));
        }
        wrapper.like(StringUtils.isNotBlank(query.getCropType()), MultiplierReport::getCropType, query.getCropType())
                .like(StringUtils.isNotBlank(query.getSeedClassReceived()), MultiplierReport::getSeedClassReceived, query.getSeedClassReceived())
                .eq(StringUtils.isNotBlank(query.getStatus()), MultiplierReport::getStatus, query.getStatus())
                .orderByDesc(MultiplierReport::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public MultiplierReport selectById(Long id) {
        return this.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(MultiplierReport report) {
        report.setCreateTime(LocalDateTime.now());
        report.setUpdateTime(LocalDateTime.now());
        report.setCreateBy(getCurrentUsername());
        report.setUpdateBy(getCurrentUsername());
        return this.save(report);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean edit(MultiplierReport report) {
        report.setUpdateTime(LocalDateTime.now());
        report.setUpdateBy(getCurrentUsername());
        return this.updateById(report);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean remove(Long id) {
        return this.removeById(id);
    }

    private String getCurrentUsername() {
        try {
            return SecurityUtils.getUsername();
        } catch (Exception ex) {
            return "system";
        }
    }
}
