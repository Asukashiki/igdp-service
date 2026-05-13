package com.inspur.seed.service.invested;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.invested.InputReceiveUnion;
import com.inspur.seed.domain.vo.InputCirculationSummaryVO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Union接收确认Service接口
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
public interface IInputReceiveUnionService extends IService<InputReceiveUnion> {

    /**
     * 查询接收确认列表
     *
     * @param releaseBy 分发人名称
     * @param batchId 繁殖批次ID
     * @param cropType 作物种类
     * @param varietyName 品种名称
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param receiveStatus 接收状态
     * @return 接收确认列表
     */
    List<InputReceiveUnion> queryReceiveList(String releaseBy, String batchId, String cropType,
                                              String varietyName, LocalDate startTime, LocalDate endTime,
                                              String receiveStatus);

    /**
     * 按数据标识查询接收确认列表
     */
    List<InputReceiveUnion> queryReceiveList(String releaseBy, String batchId, String cropType,
                                              String varietyName, LocalDate startTime, LocalDate endTime,
                                              String receiveStatus, String flag);

    /**
     * 查询Union接收明细汇总列表
     */
    List<InputCirculationSummaryVO> queryReceiveSummaryList(String releaseBy, String batchId, String cropType,
                                                            String varietyName, LocalDate startTime, LocalDate endTime,
                                                            String receiveStatus);

    /**
     * 确认接收
     *
     * @param id 接收确认表ID
     * @param confirmBy 确认人
     * @param confirmOrg 确认机构
     * @return 是否成功
     */
    boolean confirmReceive(String id, String confirmBy, String confirmOrg);

    /**
     * 按数据标识确认接收
     */
    boolean confirmReceive(String id, String confirmBy, String confirmOrg, String flag);

    /**
     * 根据分发单ID查询接收确认详情
     *
     * @param id 接收确认表ID
     * @return 接收确认详情
     */
    Map<String, Object> queryById(String id);

    /**
     * 根据接收确认ID查询详情（明细按投入品汇总）
     */
    Map<String, Object> querySummaryById(String id);
}
