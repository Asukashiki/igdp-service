package com.inspur.seed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.BreedingTracking;

import java.time.LocalDate;
import java.util.List;

/**
 * 育种跟踪记录服务接口
 *
 * @author system
 */
public interface IBreedingTrackingService extends IService<BreedingTracking> {

    /**
     * 新增育种跟踪记录
     *
     * @param breedingTracking 育种跟踪记录信息
     * @return 跟踪ID
     */
    String addBreedingTracking(BreedingTracking breedingTracking);

    /**
     * 查询育种跟踪记录列表
     *
     * @param batchId 育种批次ID
     * @param stageName 阶段名称
     * @param stageCompletionDateStart 阶段完成日期起始
     * @param stageCompletionDateEnd 阶段完成日期结束
     * @return 育种跟踪记录列表
     */
    List<BreedingTracking> queryBreedingTrackingList(String batchId, String stageName, LocalDate stageCompletionDateStart, LocalDate stageCompletionDateEnd);

    /**
     * 根据跟踪ID查询育种跟踪记录详情
     *
     * @param trackingId 跟踪ID
     * @return 育种跟踪记录详情
     */
    BreedingTracking queryByTrackingId(String trackingId);

    /**
     * 编辑育种跟踪记录
     *
     * @param breedingTracking 育种跟踪记录信息
     * @return 更新结果
     */
    boolean editBreedingTracking(BreedingTracking breedingTracking);

    /**
     * 删除育种跟踪记录
     *
     * @param trackingId 跟踪ID
     * @return 删除结果
     */
    boolean removeBreedingTracking(String trackingId);
}
