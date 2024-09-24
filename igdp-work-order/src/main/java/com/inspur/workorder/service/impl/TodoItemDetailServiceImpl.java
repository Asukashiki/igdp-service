package com.inspur.workorder.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.core.domain.entity.SysUser;
import com.inspur.system.domain.SysUnifyTodo;
import com.inspur.system.service.ISysUnifyTodoService;
import com.inspur.system.service.ISysUserService;
import com.inspur.workorder.domain.TodoItem;
import com.inspur.workorder.domain.TodoItemDetail;
import com.inspur.workorder.domain.payload.TodoItemDetailPayload;
import com.inspur.workorder.domain.payload.TodoItemPayload;
import com.inspur.workorder.domain.vo.ProcessStatisticsVo;
import com.inspur.workorder.domain.vo.TodoItemDetailVo;
import com.inspur.workorder.mapper.TodoItemDetailMapper;
import com.inspur.workorder.service.ITodoItemDetailService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liyunlong02
 * @version 1.0
 * @ClassName TodoItemDetailServiceImpl
 * @date 2024/4/16 9:27
 */
@Service
public class TodoItemDetailServiceImpl extends ServiceImpl<TodoItemDetailMapper, TodoItemDetail> implements ITodoItemDetailService {

    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysUnifyTodoService sysUnifyTodoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTodoItemDetail(TodoItem todoItem, TodoItemPayload itemPayload) {
        List<TodoItemDetailPayload> payloadList = itemPayload.getCreateList();
        if (null != payloadList && !payloadList.isEmpty()) {
            List<TodoItemDetail> detailList = new ArrayList<>(payloadList.size());
            for (TodoItemDetailPayload payload : payloadList) {
                TodoItemDetail detail = new TodoItemDetail();
                detail.setId(payload.getDetailId());
                detail.setTodoId(todoItem.getTodoId());
                if (StrUtil.isNotEmpty(itemPayload.getType())) {
                    detail.setType(itemPayload.getType());
                }
                detail.setDetailId(payload.getDetailId());
                detail.setDataId(payload.getDataId());
                detail.setBusinessCode(itemPayload.getBusinessCode());
                detail.setBusinessId(itemPayload.getBusinessId());
                detail.setUserId(payload.getRecipientUserId());
                SysUser sysUser = sysUserService.getById(detail.getUserId());
                if (null != sysUser) {
                    detail.setDeptId(sysUser.getDeptId());
                }
                detail.setCreateTime(LocalDateTime.now());
                detail.setYear(detail.getCreateTime().getYear());
                detail.setMonth(detail.getCreateTime().getMonthValue());
                detail.setReadStatus(TodoItemDetail.READ_STATUS_NEW);
                detail.setStatus(TodoItemDetail.STATUS_NEW);
                detailList.add(detail);
            }
            saveBatch(detailList);
            //推送到系统的统一待办
            asyncAddUnityTodo(todoItem, detailList);
        }
    }

    @Async
    void asyncAddUnityTodo(TodoItem item, List<TodoItemDetail> detailList) {
        if (null != detailList && !detailList.isEmpty()) {
            for (TodoItemDetail detail : detailList) {
                SysUnifyTodo sysUnifyTodo = createUnifyTodo(item, detail);
                sysUnifyTodoService.addTodo(sysUnifyTodo);
            }
        }
    }

    @Async
    void asyncUpdateUnityTodo(TodoItem item, List<String> userIdList) {
        //更新统一待办的状态
        LambdaQueryWrapper<TodoItemDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TodoItemDetail::getTodoId, item.getTodoId());
        queryWrapper.in(TodoItemDetail::getUserId, userIdList);
        List<TodoItemDetail> detailList = list(queryWrapper);
        if (null != detailList && !detailList.isEmpty()) {
            for (TodoItemDetail detail : detailList) {
                SysUnifyTodo sysUnifyTodo = createUnifyTodo(item, detail);
                sysUnifyTodo.setStatus(SysUnifyTodo.STATUS_DONE);
                sysUnifyTodoService.updateStatus(sysUnifyTodo);
            }
        }
    }

    private SysUnifyTodo createUnifyTodo(TodoItem item, TodoItemDetail detail) {
        SysUnifyTodo sysUnifyTodo = new SysUnifyTodo();
        sysUnifyTodo.setTitle(item.getTitle());
        sysUnifyTodo.setUserId(detail.getUserId());
        sysUnifyTodo.setBusinessId(detail.getBusinessId());
        sysUnifyTodo.setDeptId(detail.getDeptId());
        sysUnifyTodo.setSource(item.getSource());
        sysUnifyTodo.setModular(item.getModular());
        sysUnifyTodo.setBusinessCode(item.getBusinessCode());
        sysUnifyTodo.setDataId(detail.getDataId());
        sysUnifyTodo.setInitiateUserId(item.getUserId());
        sysUnifyTodo.setSubmitTime(item.getSubmitTime());
        sysUnifyTodo.setCallbackLink(item.getCallbackLink());
        sysUnifyTodo.setAppId("icd");
        return sysUnifyTodo;
    }


    @Override
    public void updateItemDetail(TodoItem todoItem, TodoItemPayload itemPayload) {
        List<TodoItemDetailPayload> payloadList = itemPayload.getUpdateList();
        if (null != payloadList && !payloadList.isEmpty()) {
            List<String> userIdList = payloadList.stream().map(TodoItemDetailPayload::getRecipientUserId).collect(Collectors.toList());
            LambdaUpdateWrapper<TodoItemDetail> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(TodoItemDetail::getHandleTime, LocalDateTime.now());
            updateWrapper.set(TodoItemDetail::getUpdateTime, LocalDateTime.now());
            if (StrUtil.isNotEmpty(itemPayload.getType())) {
                updateWrapper.set(TodoItemDetail::getType, itemPayload.getType());
            }
            updateWrapper.set(TodoItemDetail::getStatus, TodoItemDetail.STATUS_COMPLETE);
            if (StrUtil.isNotEmpty(itemPayload.getDataId())) {
                updateWrapper.set(TodoItemDetail::getDataId, itemPayload.getDataId());
            }
            updateWrapper.eq(TodoItemDetail::getTodoId, todoItem.getTodoId());
            updateWrapper.in(TodoItemDetail::getUserId, userIdList);
            update(updateWrapper);

            //更新统一待办的状态
            asyncUpdateUnityTodo(todoItem, userIdList);
        }

    }

    @Override
    public List<TodoItemDetailVo> selectVoList(String userId, String status) {
        return this.baseMapper.selectVoList(userId, status);
    }

    /**
     * 统计用户处理工单明细
     * 状态是已处理的
     *
     * @param userId    用户id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param type      类型
     * @param itemState 主流程状态
     * @return 统计结果
     */
    @Override
    public List<ProcessStatisticsVo> countGroupByUser(String userId, LocalDateTime startTime, LocalDateTime endTime, String type, String status, String itemState) {
        return this.baseMapper.countByUser(null, userId, startTime, endTime, type, status, itemState);
    }

    @Override
    public List<ProcessStatisticsVo> countGroupByUserType(String userId, LocalDateTime startTime, LocalDateTime endTime, String type, String status, String itemState) {
        return this.baseMapper.countByUserType(userId, startTime, endTime, type, status, itemState);
    }


    @Override
    public List<ProcessStatisticsVo> countGroupByType(String userId, LocalDateTime startTime, LocalDateTime endTime, String status) {
        return this.baseMapper.countPersonalByType(userId, startTime, endTime, status);
    }

    @Override
    public List<ProcessStatisticsVo> countGroupByStatus(String userId, LocalDateTime startTime, LocalDateTime endTime, String type) {
        return this.baseMapper.countPersonalByStatus(userId, startTime, endTime, type);
    }
}
