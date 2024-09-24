package com.inspur.data.treating.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.constant.HttpStatus;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.data.treating.domain.UnifyStatisticsItem;
import com.inspur.common.core.domain.entity.UnifyStatisticsItemValue;
import com.inspur.common.core.redis.RedisCache;
import com.inspur.common.utils.StringUtils;
import com.inspur.data.treating.mapper.UnifyStatisticsItemValueMapper;
import com.inspur.data.treating.service.IUnifyStatisticsItemValueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName UnifyStatisticsItemValueServiceImpl
 * @date 2024/7/17 14:12
 */
@Service
public class UnifyStatisticsItemValueServiceImpl extends ServiceImpl<UnifyStatisticsItemValueMapper, UnifyStatisticsItemValue> implements IUnifyStatisticsItemValueService {
    @Resource
    private RedisCache redisCache;

    @Override
    public UnifyStatisticsItemValue getByKey(String key) {

        //历史数据，可以放入缓存中使用
        UnifyStatisticsItemValue itemValue;
        if (redisCache.hasKey(key)) {
            itemValue = redisCache.getCacheObject(key);
        } else {
            itemValue = getById(key);
            if (itemValue != null && UnifyStatisticsItem.TYPE_HIS.equals(itemValue.getType())) {
                redisCache.setCacheObject(key, itemValue);
                redisCache.expire(key, 1, TimeUnit.HOURS);
            }
        }
        return itemValue;
    }

    @Override
    public List<UnifyStatisticsItemValue> getList(UnifyStatisticsItemValue queryParam) {
        LambdaQueryWrapper<UnifyStatisticsItemValue> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(queryParam.getKey())) {
            queryWrapper.eq(UnifyStatisticsItemValue::getKey, queryParam.getKey());
        }
        if (StringUtils.isNotBlank(queryParam.getDate())) {
            queryWrapper.eq(UnifyStatisticsItemValue::getDate, queryParam.getDate());
        }
        if (StringUtils.isNotBlank(queryParam.getType())) {
            queryWrapper.eq(UnifyStatisticsItemValue::getType, queryParam.getType());
        }
        if (StringUtils.isNotBlank(queryParam.getTarget())) {
            queryWrapper.eq(UnifyStatisticsItemValue::getTarget, queryParam.getTarget());
        }
        if (null != queryParam.getCategory()) {
            queryWrapper.eq(UnifyStatisticsItemValue::getCategory, queryParam.getCategory());
        }
        if (StringUtils.isNotBlank(queryParam.getTargetName())) {
            queryWrapper.eq(UnifyStatisticsItemValue::getTargetName, queryParam.getTargetName());
        }
        if (StringUtils.isNotEmpty(queryParam.getItemId())) {
            queryWrapper.eq(UnifyStatisticsItemValue::getItemId, queryParam.getItemId());
        }
        if (StringUtils.isNotEmpty(queryParam.getStatisticsCode())) {
            queryWrapper.eq(UnifyStatisticsItemValue::getStatisticsCode, queryParam.getStatisticsCode());
        }
        if (StringUtils.isNotEmpty(queryParam.getItemCode())) {
            queryWrapper.eq(UnifyStatisticsItemValue::getItemCode, queryParam.getItemCode());
        }
        Map<String, Object> params = queryParam.getParams();
        if (null != params) {
            String beginDate = null != params.get("beginDate") ? params.get("beginDate").toString() : null;
            String endDate = null != params.get("endDate") ? params.get("endDate").toString() : null;
            if (StringUtils.isNotBlank(beginDate)) {
                queryWrapper.ge(UnifyStatisticsItemValue::getDate, beginDate);
            }
            if (StringUtils.isNotBlank(endDate)) {
                queryWrapper.le(UnifyStatisticsItemValue::getDate, endDate);
            }
        }
        return list(queryWrapper);
    }

    /**
     * 保存统计内容
     * key，category::item::target::date
     */
    @Override
    public void saveValue(UnifyStatisticsItemValue itemValue) {
        AjaxResult checkResult = checkForSave(itemValue);
        if (!checkResult.isSuccess()) {
            throw new RuntimeException(checkResult.get(AjaxResult.MSG_TAG).toString());
        }
        handleKey(itemValue);
        itemValue.setCreateTime(LocalDateTime.now());
        itemValue.setUpdateTime(LocalDateTime.now());
        saveOrUpdate(itemValue);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveValueList(List<UnifyStatisticsItemValue> itemValueList) {
        if (null != itemValueList && !itemValueList.isEmpty()) {
            for (UnifyStatisticsItemValue itemValue : itemValueList) {
                AjaxResult checkResult = checkForSave(itemValue);
                if (!checkResult.isSuccess()) {
                    throw new RuntimeException(checkResult.get(AjaxResult.MSG_TAG).toString());
                }
                handleKey(itemValue);
                itemValue.setCreateTime(LocalDateTime.now());
                itemValue.setUpdateTime(LocalDateTime.now());
            }
            saveOrUpdateBatch(itemValueList);
        }
    }

    private void handleKey(UnifyStatisticsItemValue itemValue) {
        String key = itemValue.getItemCode() + "::" +
                itemValue.getTarget() + "::" +
                itemValue.getStatisticsCode();
        if (itemValue.getType().equals(UnifyStatisticsItem.TYPE_CUR)) {
            key = key + "::" + itemValue.getType();
        } else {
            key = key + "::" + itemValue.getDate();
        }
        itemValue.setKey(key);
    }

    private AjaxResult checkForSave(UnifyStatisticsItemValue unifyStatisticsItemValue) {
        if (unifyStatisticsItemValue == null) {
            return AjaxResult.error(HttpStatus.BAD_REQUEST, "信息为空");
        }
        if (StringUtils.isEmpty(unifyStatisticsItemValue.getCategory())) {
            return AjaxResult.error(HttpStatus.BAD_REQUEST, "所属分类不能为空");
        }
        if (StringUtils.isEmpty(unifyStatisticsItemValue.getStatisticsCode())) {
            return AjaxResult.error(HttpStatus.BAD_REQUEST, "指标不能为空");
        }
        if (StringUtils.isEmpty(unifyStatisticsItemValue.getTarget())) {
            return AjaxResult.error(HttpStatus.BAD_REQUEST, "实列不能为空");
        }
        return AjaxResult.success();
    }
}
