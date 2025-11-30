package com.inspur.seed.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.dto.BreedingSeedCertificationDTO;
import com.inspur.seed.domain.entity.BreedingSeedCertification;
import com.inspur.seed.domain.vo.BreedingSeedCertificationVO;

import java.util.List;

/**
 * 繁殖种子认证申请Service接口
 *
 * @author igdp
 * @date 2025-11-29
 */
public interface IBreedingSeedCertificationService extends IService<BreedingSeedCertification> {

    /**
     * 分页查询繁殖种子认证申请列表
     *
     * @param page 分页参数
     * @param dto 查询条件
     * @return 分页结果
     */
    IPage<BreedingSeedCertificationVO> selectBreedingSeedCertificationPage(IPage<BreedingSeedCertification> page, BreedingSeedCertificationDTO dto);

    /**
     * 查询繁殖种子认证申请列表
     *
     * @param dto 查询条件
     * @return 列表
     */
    List<BreedingSeedCertificationVO> selectBreedingSeedCertificationList(BreedingSeedCertificationDTO dto);

    /**
     * 查询繁殖种子认证申请详情
     *
     * @param dataId 数据ID
     * @return 详情
     */
    BreedingSeedCertificationVO selectBreedingSeedCertificationById(String dataId);

    /**
     * 新增繁殖种子认证申请
     *
     * @param dto 数据
     * @return 结果
     */
    int insertBreedingSeedCertification(BreedingSeedCertificationDTO dto);

    /**
     * 修改繁殖种子认证申请
     *
     * @param dto 数据
     * @return 结果
     */
    int updateBreedingSeedCertification(BreedingSeedCertificationDTO dto);

    /**
     * 批量删除繁殖种子认证申请
     *
     * @param dataIds 需要删除的数据ID
     * @return 结果
     */
    int deleteBreedingSeedCertificationByIds(String[] dataIds);
}
