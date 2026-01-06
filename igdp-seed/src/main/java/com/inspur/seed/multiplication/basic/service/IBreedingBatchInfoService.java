package com.inspur.seed.multiplication.basic.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.multiplication.basic.domain.dto.BreedingBatchAddDTO;
import com.inspur.seed.multiplication.basic.domain.dto.BreedingBatchQueryDTO;
import com.inspur.seed.multiplication.basic.domain.dto.BreedingBatchUpdateDTO;
import com.inspur.seed.multiplication.basic.domain.entity.BreedingBatchInfo;
import com.inspur.seed.multiplication.basic.domain.vo.BreedingBatchVO;

import java.util.List;

/**
 * 繁殖批次信息Service接口
 *
 * @author igdp
 * @date 2025-11-29
 */
public interface IBreedingBatchInfoService extends IService<BreedingBatchInfo> {

    /**
     * 分页查询繁殖批次信息
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    IPage<BreedingBatchVO> queryPage(BreedingBatchQueryDTO queryDTO);

    /**
     * 查询繁殖批次信息列表（不分页）
     *
     * @param queryDTO 查询条件
     * @return 列表数据
     */
    List<BreedingBatchVO> queryList(BreedingBatchQueryDTO queryDTO);

    /**
     * 新增繁殖批次信息
     *
     * @param addDTO 新增数据
     * @return 新增记录的ID
     */
    String add(BreedingBatchAddDTO addDTO);

    /**
     * 修改繁殖批次信息
     *
     * @param updateDTO 修改数据
     * @return 修改是否成功
     */
    boolean update(BreedingBatchUpdateDTO updateDTO);

    /**
     * 查询繁殖批次详情
     *
     * @param id 主键ID
     * @return 繁殖批次详情
     */
    BreedingBatchVO detail(String id);

    /**
     * 删除繁殖批次信息（逻辑删除）
     *
     * @param ids 主键ID列表
     * @return 删除是否成功
     */
    boolean delete(List<String> ids);
}
