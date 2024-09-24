package com.inspur.system.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.domain.SelectEntity;
import com.inspur.common.utils.LoginHelper;
import com.inspur.system.domain.SysUnifyTodo;
import com.inspur.system.mapper.SysUnifyTodoMapper;
import com.inspur.system.service.ISysUnifyTodoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 待办事项实现
 *
 * @author liyunlong
 * @date 2024/1/25
 */
@Service("sysUnifyTodoService")
public class SysUnifyTodoServiceImpl extends ServiceImpl<SysUnifyTodoMapper, SysUnifyTodo> implements ISysUnifyTodoService {
    @Override
    public List<SysUnifyTodo> selectSysTodoList(SysUnifyTodo queryParam) {
        LambdaQueryWrapper<SysUnifyTodo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotEmpty(queryParam.getUserId()), SysUnifyTodo::getUserId, queryParam.getUserId());
        queryWrapper.like(StrUtil.isNotEmpty(queryParam.getTitle()), SysUnifyTodo::getTitle, queryParam.getTitle());
        queryWrapper.eq(StrUtil.isNotEmpty(queryParam.getType()), SysUnifyTodo::getType, queryParam.getType());
        queryWrapper.eq(StrUtil.isNotEmpty(queryParam.getSource()), SysUnifyTodo::getSource, queryParam.getSource());
        queryWrapper.eq(StrUtil.isNotEmpty(queryParam.getModular()), SysUnifyTodo::getModular, queryParam.getModular());
        queryWrapper.eq(StrUtil.isNotEmpty(queryParam.getBusinessId()), SysUnifyTodo::getBusinessId, queryParam.getBusinessId());
        queryWrapper.eq(StrUtil.isNotEmpty(queryParam.getStatus()), SysUnifyTodo::getStatus, queryParam.getStatus());
        queryWrapper.ge(ObjectUtil.isNotEmpty(queryParam.getParams().get("beginTime")), SysUnifyTodo::getCreateTime, queryParam.getParams().get("beginTime"));
        queryWrapper.le(ObjectUtil.isNotEmpty(queryParam.getParams().get("endTime")), SysUnifyTodo::getCreateTime, queryParam.getParams().get("endTime"));
        queryWrapper.orderByDesc(SysUnifyTodo::getCreateTime);
        return list(queryWrapper);
    }

    @Override
    public void addTodo(SysUnifyTodo sysUnifyTodo) {
        //modular、source、businessId不能为空
        if (StrUtil.isEmpty(sysUnifyTodo.getModular()) || StrUtil.isEmpty(sysUnifyTodo.getAppId()) || StrUtil.isEmpty(sysUnifyTodo.getBusinessId())) {
            throw new RuntimeException("appId、modular、businessId不能为空");
        }
        //判断数据是否冲突
        SysUnifyTodo queryParam = new SysUnifyTodo();
        queryParam.setSource(sysUnifyTodo.getSource());
        queryParam.setModular(sysUnifyTodo.getModular());
        queryParam.setBusinessId(sysUnifyTodo.getBusinessId());
        List<SysUnifyTodo> currentList = selectSysTodoList(queryParam);
        if (null != currentList && !currentList.isEmpty()) {
            throw new RuntimeException("已存在businessId");
        }
        sysUnifyTodo.setCreateTime(LocalDateTime.now());
        sysUnifyTodo.setStatus(SysUnifyTodo.STATUS_NEW);
        save(sysUnifyTodo);
    }

    @Override
    public void updateStatus(SysUnifyTodo sysUnifyTodo) {
        //modular、appId、businessId不能为空
        if (StrUtil.isEmpty(sysUnifyTodo.getModular()) || StrUtil.isEmpty(sysUnifyTodo.getAppId()) || StrUtil.isEmpty(sysUnifyTodo.getBusinessId())) {
            throw new RuntimeException("appId、modular、businessId不能为空");
        }
        if (StrUtil.isEmpty(sysUnifyTodo.getStatus())) {
            throw new RuntimeException("目标状态不能为空");
        }
        if (sysUnifyTodo.getStatus().equals(SysUnifyTodo.STATUS_NEW)) {
            throw new RuntimeException("不允许更新为未办");
        }
        LambdaUpdateWrapper<SysUnifyTodo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SysUnifyTodo::getStatus, sysUnifyTodo.getStatus());
        updateWrapper.set(SysUnifyTodo::getUpdateTime, LocalDateTime.now());
        updateWrapper.eq(SysUnifyTodo::getBusinessId, sysUnifyTodo.getBusinessId());
        updateWrapper.eq(SysUnifyTodo::getAppId, sysUnifyTodo.getAppId());
        updateWrapper.eq(StrUtil.isNotEmpty(sysUnifyTodo.getModular()), SysUnifyTodo::getModular, sysUnifyTodo.getModular());
        update(updateWrapper);
    }

    @Override
    public boolean updateTodo(SysUnifyTodo sysUnifyTodo) {
        sysUnifyTodo.setUpdateBy(LoginHelper.getUserId());
        sysUnifyTodo.setUpdateTime(LocalDateTime.now());
        return updateById(sysUnifyTodo);
    }

    @Override
    public List<SelectEntity> selectType() {
        return this.baseMapper.selectType();
    }

    @Override
    public List<SelectEntity> selectSource() {
        return this.baseMapper.selectSource();
    }


}
