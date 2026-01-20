package com.inspur.seed.multiplication.c1Seed.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.multiplication.c1Seed.domain.dto.C1BreedingBatchDTO;
import com.inspur.seed.multiplication.c1Seed.domain.dto.C1BreedingBatchQueryDTO;
import com.inspur.seed.multiplication.c1Seed.domain.entity.C1BreedingBatch;
import com.inspur.seed.multiplication.c1Seed.domain.vo.C1BreedingBatchVO;

/**
 * C1繁殖批次Service接口
 * @author system
 * @since 2025-12-08
 */
public interface IC1BreedingBatchService extends IService<C1BreedingBatch> {

    /**
     * 分页查询C1繁殖批次列表
     */
    IPage<C1BreedingBatchVO> pageList(C1BreedingBatchQueryDTO queryDTO);

    /**
     * 根据ID获取详情
     */
    C1BreedingBatchVO getDetailById(String id);

    /**
     * 新增C1繁殖批次
     */
    boolean add(C1BreedingBatchDTO dto);

    /**
     * 更新C1繁殖批次
     */
    boolean update(C1BreedingBatchDTO dto);

    /**
     * 删除C1繁殖批次（逻辑删除）
     */
    boolean deleteByIds(java.util.List<String> ids);

    /**
     * 审核通过
     */
    boolean approveBatch(String id, String auditComment);

    /**
     * 审核驳回
     */
    boolean rejectBatch(String id,  String auditComment);

    /**
     * 记录打印次数
     */
    boolean recordPrint(String id);
}
