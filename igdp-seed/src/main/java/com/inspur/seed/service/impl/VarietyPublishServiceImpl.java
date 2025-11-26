package com.inspur.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.LoginHelper;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.uuid.IdUtils;
import com.inspur.seed.domain.VarietyPublish;
import com.inspur.seed.domain.VarietyRegistration;
import com.inspur.seed.mapper.VarietyPublishMapper;
import com.inspur.seed.service.IVarietyPublishService;
import com.inspur.seed.service.IVarietyRegistrationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

/**
 * 品种发布服务实现类
 *
 * @author system
 */
@Service
public class VarietyPublishServiceImpl extends ServiceImpl<VarietyPublishMapper, VarietyPublish> implements IVarietyPublishService {

    @Resource
    private IVarietyRegistrationService varietyRegistrationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String publishVariety(VarietyPublish varietyPublish) {
        // 校验品种登记申请是否存在
        VarietyRegistration registration = varietyRegistrationService.queryByRegistrationId(varietyPublish.getRegistrationId());
        if (registration == null) {
            throw new ServiceException("品种登记申请不存在");
        }

        // 校验品种登记申请状态是否为待发布
        if (registration.getRecordStatus() != 1) {
            throw new ServiceException("品种登记申请状态不是待发布，无法发布");
        }

        // 生成发布ID
        String publishId = "VAR_PUB" + IdUtils.fastSimpleUUID().substring(0, 16).toUpperCase();
        varietyPublish.setPublishId(publishId);

        // 生成发布编号（格式：PUB+年月日+000001）
        String publishNo = generatePublishNo();
        varietyPublish.setPublishNo(publishNo);

        // 设置发布日期
        if (varietyPublish.getPublishDate() == null) {
            varietyPublish.setPublishDate(LocalDate.now());
        }

        // 设置发布时间
        varietyPublish.setPublishTime(LocalDateTime.now());

        // 设置公示状态为公示中
        if (varietyPublish.getPublishStatus() == null) {
            varietyPublish.setPublishStatus(1);
        }

        // 设置创建信息
        varietyPublish.setCreateBy(LoginHelper.getUsername());
        varietyPublish.setCreateTime(LocalDateTime.now());

        // 保存发布记录
        save(varietyPublish);

        // 更新品种登记申请状态为已发布(3)
        varietyRegistrationService.updateRecordStatus(varietyPublish.getRegistrationId(), 3);

        return publishId;
    }

    @Override
    public List<VarietyPublish> queryPublishList(String varietyName, String cropType, Integer publishStatus) {
        LambdaQueryWrapper<VarietyPublish> wrapper = new LambdaQueryWrapper<>();

        // 品种名称筛选
        if (StringUtils.isNotEmpty(varietyName)) {
            wrapper.like(VarietyPublish::getVarietyName, varietyName);
        }

        // 作物类型筛选
        if (StringUtils.isNotEmpty(cropType)) {
            wrapper.eq(VarietyPublish::getCropType, cropType);
        }

        // 公示状态筛选
        if (publishStatus != null) {
            wrapper.eq(VarietyPublish::getPublishStatus, publishStatus);
        }

        // 按发布时间倒序排列
        wrapper.orderByDesc(VarietyPublish::getPublishTime);

        return list(wrapper);
    }

    @Override
    public VarietyPublish queryByPublishId(String publishId) {
        return getById(publishId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unpublishVariety(String publishId) {
        VarietyPublish varietyPublish = queryByPublishId(publishId);
        if (varietyPublish == null) {
            throw new ServiceException("发布记录不存在");
        }

        // 更新发布状态为已下架(2)
        varietyPublish.setPublishStatus(2);
        varietyPublish.setUpdateBy(LoginHelper.getUsername());
        varietyPublish.setUpdateTime(LocalDateTime.now());
        updateById(varietyPublish);
    }

    @Override
    public VarietyPublish queryByRegistrationId(String registrationId) {
        LambdaQueryWrapper<VarietyPublish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VarietyPublish::getRegistrationId, registrationId);
        wrapper.orderByDesc(VarietyPublish::getPublishTime);
        wrapper.last("LIMIT 1");
        return getOne(wrapper);
    }

    /**
     * 生成发布编号（格式：PUB+年月日+000001）
     *
     * @return 发布编号
     */
    private String generatePublishNo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateStr = LocalDate.now().format(formatter);

        // 查询今天已有的发布记录数
        LambdaQueryWrapper<VarietyPublish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(VarietyPublish::getPublishNo, "PUB" + dateStr);
        long count = count(wrapper);

        // 生成序号
        String sequence = String.format("%06d", count + 1);
        return "PUB" + dateStr + sequence;
    }
}
