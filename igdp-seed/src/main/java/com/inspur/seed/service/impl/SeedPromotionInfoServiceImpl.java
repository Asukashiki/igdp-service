package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.LoginHelper;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.file.FileUploadUtils;
import com.inspur.common.utils.uuid.IdUtils;
import com.inspur.seed.domain.SeedPromotionInfo;
import com.inspur.seed.mapper.SeedPromotionInfoMapper;
import com.inspur.seed.service.ISeedPromotionInfoService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 种子推广信息服务实现类
 *
 * @author system
 */
@Service
public class SeedPromotionInfoServiceImpl extends ServiceImpl<SeedPromotionInfoMapper, SeedPromotionInfo> implements ISeedPromotionInfoService {

    @Override
    public List<SeedPromotionInfo> queryPromotionList(String enterpriseId, String title) {
        LambdaQueryWrapper<SeedPromotionInfo> wrapper = new LambdaQueryWrapper<>();

        // 企业ID筛选
        if (StringUtils.isNotEmpty(enterpriseId)) {
            wrapper.eq(SeedPromotionInfo::getEnterpriseId, enterpriseId);
        }

        // 推广标题模糊查询
        if (StringUtils.isNotEmpty(title)) {
            wrapper.like(SeedPromotionInfo::getTitle, title);
        }

        // 按发布时间倒序排列
        wrapper.orderByDesc(SeedPromotionInfo::getPublishTime);

        return list(wrapper);
    }

    @Override
    public Map<String, String> uploadPromotion(String enterpriseId, String title, MultipartFile videoFile,
                                              String promotionSummary, String recommendedVarieties, Integer validPeriod) {
        // 校验文件格式
        if (videoFile == null || videoFile.isEmpty()) {
            throw new ServiceException("视频文件不能为空");
        }

        String fileName = videoFile.getOriginalFilename();
        if (!fileName.toLowerCase().endsWith(".mp4")) {
            throw new ServiceException("仅支持MP4格式视频文件");
        }

        // 校验文件大小（100MB）
        if (videoFile.getSize() > 100 * 1024 * 1024) {
            throw new ServiceException("视频文件大小不能超过100MB");
        }

        // 生成推广ID
        String promotionId = "PROMO" + IdUtils.fastSimpleUUID().substring(0, 16).toUpperCase();

        try {
            // 上传视频文件
            String videoUrl = FileUploadUtils.upload(videoFile);

            // 生成分享链接
            String shareLink = "https://domain.com/seed/promotion/" + promotionId;

            // 创建推广信息对象
            SeedPromotionInfo promotionInfo = new SeedPromotionInfo();
            promotionInfo.setPromotionId(promotionId);
            promotionInfo.setEnterpriseId(enterpriseId);
            promotionInfo.setTitle(title);
            promotionInfo.setVideoUrl(videoUrl);
            promotionInfo.setPromotionSummary(promotionSummary);
            promotionInfo.setRecommendedVarieties(recommendedVarieties);
            promotionInfo.setPublishTime(LocalDateTime.now());
            promotionInfo.setValidPeriod(validPeriod);
            promotionInfo.setShareLink(shareLink);
            promotionInfo.setVisitCount(0);

            // 设置创建信息
            promotionInfo.setCreateBy(LoginHelper.getUsername());
            promotionInfo.setCreateTime(LocalDateTime.now());

            // 保存推广信息
            save(promotionInfo);

            // 返回推广ID和分享链接
            Map<String, String> result = new HashMap<>();
            result.put("promotionId", promotionId);
            result.put("shareLink", shareLink);
            return result;
        } catch (Exception e) {
            throw new ServiceException("视频上传失败: " + e.getMessage());
        }
    }

    @Override
    public Integer updateVisitCount(String promotionId) {
        // 查询推广信息
        SeedPromotionInfo promotionInfo = getById(promotionId);
        if (promotionInfo == null) {
            throw new ServiceException("推广信息不存在");
        }

        // 更新访问次数
        Integer newVisitCount = promotionInfo.getVisitCount() + 1;
        promotionInfo.setVisitCount(newVisitCount);
        updateById(promotionInfo);

        return newVisitCount;
    }

    @Override
    public SeedPromotionInfo queryByPromotionId(String promotionId) {
        return getById(promotionId);
    }

    @Override
    public void deletePromotion(String promotionId) {
        // 查询推广信息
        SeedPromotionInfo promotionInfo = getById(promotionId);
        if (promotionInfo == null) {
            throw new ServiceException("推广信息不存在");
        }
        // 删除推广信息
        removeById(promotionId);
    }
}
