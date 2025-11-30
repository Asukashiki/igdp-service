package com.inspur.seed.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.dto.BreedingTrackingAddDTO;
import com.inspur.seed.domain.dto.BreedingTrackingQueryDTO;
import com.inspur.seed.domain.dto.BreedingTrackingUpdateDTO;
import com.inspur.seed.domain.entity.BreedingTrackingInfo;
import com.inspur.seed.domain.vo.BreedingTrackingVO;

import java.util.List;

/**
 * 繁殖跟踪信息Service接口
 *
 * @author igdp
 * @date 2025-11-29
 */
public interface IBreedingTrackingInfoService extends IService<BreedingTrackingInfo> {

    /**
     * 分页查询繁殖跟踪信息
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    IPage<BreedingTrackingVO> queryPage(BreedingTrackingQueryDTO queryDTO);

    /**
     * 查询繁殖跟踪信息列表（不分页）
     *
     * @param queryDTO 查询条件
     * @return 列表数据
     */
    List<BreedingTrackingVO> queryList(BreedingTrackingQueryDTO queryDTO);

    /**
     * 新增繁殖跟踪信息
     *
     * @param addDTO 新增数据
     * @return 新增记录的ID
     */
    String add(BreedingTrackingAddDTO addDTO);

    /**
     * 修改繁殖跟踪信息
     *
     * @param updateDTO 修改数据
     * @return 修改是否成功
     */
    boolean update(BreedingTrackingUpdateDTO updateDTO);

    /**
     * 查询繁殖跟踪详情
     *
     * @param id 主键ID
     * @return 繁殖跟踪详情
     */
    BreedingTrackingVO detail(String id);

    /**
     * 删除繁殖跟踪信息（逻辑删除）
     *
     * @param ids 主键ID列表
     * @return 删除是否成功
     */
    boolean delete(List<String> ids);
}
