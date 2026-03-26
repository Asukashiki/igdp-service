package com.inspur.seed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.MultiplierReport;

import java.util.List;

public interface IMultiplierReportService extends IService<MultiplierReport> {
    List<MultiplierReport> selectList(MultiplierReport query);
    MultiplierReport selectById(Long id);
    boolean create(MultiplierReport report);
    boolean edit(MultiplierReport report);
    boolean remove(Long id);
}
