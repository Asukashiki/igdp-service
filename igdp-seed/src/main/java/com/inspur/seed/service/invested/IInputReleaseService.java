package com.inspur.seed.service.invested;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.invested.InputReleaseMain;
import com.inspur.seed.dto.invested.InputReleaseDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 投入品分发Service接口
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
public interface IInputReleaseService extends IService<InputReleaseMain> {

    /**
     * 查询OSE分发单列表
     *
     * @param unionName 分发对象名称
     * @param inputType 投入品类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 分发单列表
     */
    List<InputReleaseMain> queryReleaseList(String releaseType, String releaseName, String inputType,
                                             LocalDate startTime, LocalDate endTime);

    /**
     * 新增分发单（主表+明细）
     *
     * @param dto 分发单DTO
     * @return 分发单ID和编号
     */
    Map<String, String> addRelease(InputReleaseDTO dto);

    /**
     * 编辑分发单（主表+明细）
     *
     * @param dto 分发单DTO
     * @return 是否成功
     */
    boolean editRelease(InputReleaseDTO dto);

    /**
     * 查询分发单详情（包含明细）
     *
     * @param id 分发主表ID
     * @return 分发单详情（包含main和details）
     */
    Map<String, Object> queryReleaseDetail(String id);

    /**
     * 根据releaseId查询分发单详情（包含明细）
     *
     * @param releaseId 分发单编号
     * @return 分发单详情（包含main和details）
     */
    Map<String, Object> queryReleaseDetailByReleaseId(String releaseId);

    /**
     * 删除分发单（主表+明细）
     *
     * @param ids 分发主表ID集合
     * @return 是否成功
     */
    boolean removeRelease(List<String> ids);

    /**
     * 查询分发单出入库状态
     *
     * @param releaseIds 分发单编号集合
     * @return Map<releaseId, stockStatus> 出入库状态映射
     */
    Map<String, String> queryStockStatus(List<String> releaseIds);

    /**
     * 查询可用库存
     *
     * @param inputCategory 投入品类别
     * @param organCode 组织编码
     * @return 可用库存信息
     */
    Map<String, Object> queryAvailableStock(String inputCategory, String organCode);
}

