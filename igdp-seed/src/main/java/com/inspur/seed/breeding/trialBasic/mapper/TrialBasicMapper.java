package com.inspur.seed.breeding.trialBasic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.breeding.trialBasic.domain.entity.TrialBasic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 试验基础信息Mapper接口
 *
 * @author inspur
 */
@Mapper
public interface TrialBasicMapper extends BaseMapper<TrialBasic> {

    /**
     * 查询试验基础信息列表
     *
     * @param trialBasic 查询条件
     * @return 试验列表
     */
    List<TrialBasic> selectTrialBasicList(TrialBasic trialBasic);

    /**
     * 根据试验ID查询试验详情（含关联地块）
     *
     * @param trialId 试验ID
     * @return 试验信息
     */
    TrialBasic selectTrialBasicById(@Param("trialId") String trialId);

    /**
     * 检查试验名称是否唯一
     *
     * @param trialName 试验名称
     * @param trialId 排除的试验ID
     * @return 数量
     */
    int checkTrialNameUnique(@Param("trialName") String trialName, @Param("trialId") String trialId);

    /**
     * 根据育种批次、研究中心和年份生成新的试验ID
     * 格式: TR-{variety_code}-{location_id}-{year}-序号
     *
     * @param batchId 育种批次ID
     * @param locationId 研究中心ID
     * @param year 年份
     * @return 试验ID
     */
    String generateTrialIdByBatchAndLocationAndYear(@Param("batchId") String batchId,
                                                      @Param("locationId") String locationId,
                                                      @Param("year") Integer year);

    /**
     * 根据作物类型与年份生成新的试验ID
     * 新格式: T_{cropType}_{year}_{6位序列号}
     *
     * @param cropType 作物种类代码
     * @param year 年度
     * @return 最新的试验ID（按规则自增生成）
     */
    String generateTrialIdByCropTypeAndYear(@Param("cropType") String cropType,
                                            @Param("year") Integer year);

    /**
     * 获取试验下拉列表
     *
     * @param batchId 批次ID（可选）
     * @return 试验列表
     */
    List<TrialBasic> selectTrialOptions(@Param("batchId") String batchId);
}
