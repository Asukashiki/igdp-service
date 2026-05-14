package com.inspur.seed.service.invested;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.invested.InputReleaseFarmerMain;
import com.inspur.seed.dto.invested.InputReleaseFarmerDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 农民分发Service接口
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
public interface IInputReleaseFarmerService extends IService<InputReleaseFarmerMain> {

    /**
     * 查询农民分发列表
     *
     * @param woredaName    Woreda名称
     * @param farmerName    农民姓名
     * @param farmerId      农民ID
     * @param year          年度
     * @param receiveStatus 领用状态
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param flag          数据标识
     * @return 分发列表
     */
    List<InputReleaseFarmerMain> queryReleaseList(String woredaName, String farmerName, String farmerId,
                                                   Integer year, String receiveStatus,
                                                   LocalDate startTime, LocalDate endTime, String flag);

    /**
     * 新增农民分发单
     *
     * @param dto 分发单DTO
     * @return 新增结果（包含分发单ID和编号）
     */
    Map<String, String> addRelease(InputReleaseFarmerDTO dto);

    /**
     * 编辑农民分发单
     *
     * @param dto 分发单DTO
     * @return 是否成功
     */
    boolean editRelease(InputReleaseFarmerDTO dto);

    /**
     * 查询农民分发单详情
     *
     * @param id 主键ID
     * @return 分发单详情（包含主表和明细）
     */
    Map<String, Object> queryReleaseDetail(String id);

    /**
     * 删除农民分发单
     *
     * @param ids 主键ID列表
     * @return 是否成功
     */
    boolean removeRelease(List<String> ids);

    /**
     * 确认农民领用
     *
     * @param id 分发单ID
     * @return 是否成功
     */
    boolean confirmReceive(String id);
}
