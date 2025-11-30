package com.inspur.seed.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.dto.BreedingTestAddDTO;
import com.inspur.seed.domain.dto.BreedingTestQueryDTO;
import com.inspur.seed.domain.dto.BreedingTestUpdateDTO;
import com.inspur.seed.domain.entity.BreedingTestInfo;
import com.inspur.seed.domain.vo.BreedingTestVO;

import java.util.List;

/**
 * 繁殖检测信息Service接口
 *
 * @author igdp
 * @date 2025-11-29
 */
public interface IBreedingTestInfoService extends IService<BreedingTestInfo> {

    /**
     * 分页查询繁殖检测信息
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    IPage<BreedingTestVO> queryPage(BreedingTestQueryDTO queryDTO);

    /**
     * 查询繁殖检测信息列表（不分页）
     *
     * @param queryDTO 查询条件
     * @return 列表数据
     */
    List<BreedingTestVO> queryList(BreedingTestQueryDTO queryDTO);

    /**
     * 新增繁殖检测信息
     *
     * @param addDTO 新增数据
     * @return 新增记录的ID
     */
    String add(BreedingTestAddDTO addDTO);

    /**
     * 修改繁殖检测信息
     *
     * @param updateDTO 修改数据
     * @return 修改是否成功
     */
    boolean update(BreedingTestUpdateDTO updateDTO);

    /**
     * 查询繁殖检测详情
     *
     * @param id 主键ID
     * @return 繁殖检测详情
     */
    BreedingTestVO detail(String id);

    /**
     * 删除繁殖检测信息（逻辑删除）
     *
     * @param ids 主键ID列表
     * @return 删除是否成功
     */
    boolean delete(List<String> ids);
}
