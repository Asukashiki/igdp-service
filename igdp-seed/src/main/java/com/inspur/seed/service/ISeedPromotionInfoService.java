package com.inspur.seed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.SeedPromotionInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 种子推广信息服务接口
 *
 * @author system
 */
public interface ISeedPromotionInfoService extends IService<SeedPromotionInfo> {

    /**
     * 查询推广内容列表
     *
     * @param enterpriseId 企业ID
     * @param title 推广标题
     * @return 推广内容列表
     */
    List<SeedPromotionInfo> queryPromotionList(String enterpriseId, String title);

    /**
     * 上传推广内容（含链接生成）
     *
     * @param enterpriseId 企业ID
     * @param title 推广标题
     * @param videoFile 视频文件
     * @param promotionSummary 推广摘要
     * @param recommendedVarieties 推荐品种
     * @param validPeriod 有效期（天）
     * @return 推广ID和分享链接
     */
    Map<String, String> uploadPromotion(String enterpriseId, String title, MultipartFile videoFile,
                                       String promotionSummary, String recommendedVarieties, Integer validPeriod);

    /**
     * 更新访问次数
     *
     * @param promotionId 推广ID
     * @return 更新后的访问次数
     */
    Integer updateVisitCount(String promotionId);

    /**
     * 根据推广ID查询推广详情
     *
     * @param promotionId 推广ID
     * @return 推广详情
     */
    SeedPromotionInfo queryByPromotionId(String promotionId);

    /**
     * 删除推广内容
     *
     * @param promotionId 推广ID
     */
    void deletePromotion(String promotionId);

    /**
     * 根据品种名称查询关联的推广内容
     *
     * @param varietyName 品种名称
     * @return 推广内容列表
     */
    List<SeedPromotionInfo> queryByVarietyName(String varietyName);
}
