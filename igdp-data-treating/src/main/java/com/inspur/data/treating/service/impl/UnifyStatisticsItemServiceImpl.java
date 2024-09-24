package com.inspur.data.treating.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.constant.Constants;
import com.inspur.common.constant.HttpStatus;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.enums.PeriodType;
import com.inspur.common.utils.LoginHelper;
import com.inspur.common.utils.StringUtils;
import com.inspur.data.treating.domain.UnifyStatisticsItem;
import com.inspur.data.treating.mapper.UnifyStatisticsItemMapper;
import com.inspur.data.treating.service.IUnifyStatisticsItemService;
import org.aspectj.weaver.loadtime.Aj;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName UnifyStatisticsItemServiceImpl
 * @date 2024/7/17 14:12
 */
@Service
public class UnifyStatisticsItemServiceImpl extends ServiceImpl<UnifyStatisticsItemMapper, UnifyStatisticsItem> implements IUnifyStatisticsItemService {
    @Override
    public List<UnifyStatisticsItem> getList(UnifyStatisticsItem query) {
        return this.baseMapper.getList(query);
    }

    @Override
    public AjaxResult addItem(UnifyStatisticsItem item) {
        AjaxResult checkResult = checkItem(item);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        item.setCreateTime(LocalDateTime.now());
        item.setCreateBy(LoginHelper.getUsername());
        save(item);
        return AjaxResult.success("保存成功");
    }

    @Override
    public AjaxResult editItem(UnifyStatisticsItem item) {
        if (StringUtils.isEmpty(item.getId())) {
            return AjaxResult.error(HttpStatus.BAD_REQUEST, "ID不能为空");
        }
        AjaxResult checkResult = checkItem(item);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        item.setUpdateTime(LocalDateTime.now());
        item.setUpdateBy(LoginHelper.getUsername());
        updateById(item);
        return AjaxResult.success("修改成功");
    }

    @Override
    public AjaxResult updateStatus(String itemId, String status) {
        if (StringUtils.isEmpty(itemId) || StringUtils.isEmpty(status)) {
            return AjaxResult.error(HttpStatus.BAD_REQUEST, "id与目标状态不能为空");
        }
        LambdaUpdateWrapper<UnifyStatisticsItem> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(UnifyStatisticsItem::getStatus, status);
        wrapper.eq(UnifyStatisticsItem::getId, itemId);
        wrapper.set(UnifyStatisticsItem::getUpdateTime, LocalDateTime.now());
        wrapper.set(UnifyStatisticsItem::getUpdateBy, LoginHelper.getUsername());
        boolean result = update(wrapper);
        if (result) {
            return AjaxResult.success("更新成功");
        } else {
            return AjaxResult.error("更新失败");
        }
    }

    /**
     * 校验指标信息规范
     */
    private AjaxResult checkItem(UnifyStatisticsItem item) {
        //历史数据的统计周期最小范围为天
        if (item.getType().equals(UnifyStatisticsItem.TYPE_HIS)) {
            if (item.getPeriod().equals(PeriodType.MINUTE.getCode())) {
                return AjaxResult.error(HttpStatus.BAD_REQUEST, "历史数据不支持每分钟统计一次");
            }
        }

        //校验sql是否存在敏感字符
        if (StringUtils.isNotEmpty(item.getSelectSql())) {
            if (!item.getSelectSql().toLowerCase().startsWith("select")) {
                return AjaxResult.error(HttpStatus.BAD_REQUEST, "sql语句必须为select语句");
            }
            if (StrUtil.containsAny(item.getSelectSql().toLowerCase(), "insert", "update", "delete", "drop", "truncate", "drop", "truncate")) {
                return AjaxResult.error(HttpStatus.BAD_REQUEST, "sql语句存在敏感操作字符");
            }
        }

        if (checkItemCodeExist(item)) {
            return AjaxResult.error(HttpStatus.BAD_REQUEST, "编码已经存在，请更换其它编码");
        }
        return AjaxResult.success();
    }

    /**
     * 判断code是否存在
     * code相同、id不同的
     * 若存在，则返回true
     * 若不存在，则返回false
     */
    private boolean checkItemCodeExist(UnifyStatisticsItem item) {
        LambdaQueryWrapper<UnifyStatisticsItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnifyStatisticsItem::getCode, item.getCode());
        if (StringUtils.isNotEmpty(item.getId())) {
            wrapper.ne(UnifyStatisticsItem::getId, item.getId());
        }
        wrapper.eq(UnifyStatisticsItem::getDelFlag, Constants.DELETE_FLAG_VALID);
        UnifyStatisticsItem currentItem = this.getOne(wrapper);
        return null != currentItem;
    }
}
