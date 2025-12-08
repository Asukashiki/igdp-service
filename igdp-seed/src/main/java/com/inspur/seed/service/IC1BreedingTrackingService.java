package com.inspur.seed.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.entity.C1BreedingTracking;
import java.util.List;
import java.util.Map;

public interface IC1BreedingTrackingService extends IService<C1BreedingTracking> {
    IPage<C1BreedingTracking> pageList(Map<String, Object> params);
    boolean add(C1BreedingTracking entity);
    boolean update(C1BreedingTracking entity);
    boolean deleteByIds(List<String> ids);
    C1BreedingTracking getDetailById(String id);
}
