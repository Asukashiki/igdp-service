package com.inspur.seed.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.entity.C1BreedingTest;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IC1BreedingTestService extends IService<C1BreedingTest> {
    IPage<C1BreedingTest> pageList(Map<String, Object> params);
    boolean add(C1BreedingTest entity);
    boolean update(C1BreedingTest entity);
    boolean deleteByIds(List<String> ids);
    C1BreedingTest getDetailById(String id);
    boolean submit(String id);
    boolean approve(String id, String auditComment);
    boolean reject(String id, String auditComment);
    Set<String> getApprovedBatchIdsBySeedClass(String seedClass);
}
