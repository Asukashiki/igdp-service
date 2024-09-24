package com.inspur.workorder.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inspur.common.annotation.DataSource;
import com.inspur.common.enums.DataSourceType;
import com.inspur.system.service.ISysConfigService;
import com.inspur.workorder.domain.TodoItem;
import com.inspur.workorder.domain.TodoItemDetail;
import com.inspur.workorder.domain.vo.ProcessStatisticsVo;
import com.inspur.workorder.domain.vo.TodoItemPersonalStatisticsVo;
import com.inspur.workorder.mapper.IcdWorkFlowProcessMapper;
import com.inspur.workorder.mapper.TodoItemDetailMapper;
import com.inspur.workorder.mapper.TodoItemMapper;
import com.inspur.workorder.service.ITodoItemStatisticsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author liyunlong
 * @version 1.0
 * @ClassName TodoItemStatisticsServiceImpl
 * @date 2024/5/17 10:02
 */
@Service
public class TodoItemStatisticsServiceImpl implements ITodoItemStatisticsService {

    @Resource
    private TodoItemMapper todoItemMapper;
    @Resource
    private TodoItemDetailMapper todoItemDetailMapper;
    @Resource
    private IcdWorkFlowProcessMapper icdWorkFlowProcessMapper;
    @Resource
    private ISysConfigService configService;

    private String getApplicationId() {
        return configService.selectConfigByKey("sys.work-order.applicationId");
    }

    @Override
    public TodoItemPersonalStatisticsVo getPersonalStatistics(String userId, LocalDateTime startTime, LocalDateTime endTime) {
        //我发起的
        Long initiativeCount = icdWorkFlowProcessMapper.countMyApply(userId, getApplicationId());
        //我的办结
        Long concludeCount = icdWorkFlowProcessMapper.countMyHandleFinished(userId, getApplicationId());
        //我的待办
        Long undoCount = icdWorkFlowProcessMapper.countPersonalTodo(userId, getApplicationId());
        //我的已办
        Long doneCount = icdWorkFlowProcessMapper.countMyHandle(userId, getApplicationId());
        TodoItemPersonalStatisticsVo statisticsVo = new TodoItemPersonalStatisticsVo();
        statisticsVo.setInitiativeCount(initiativeCount);
        statisticsVo.setConcludeCount(concludeCount);
        statisticsVo.setUndoCount(undoCount);
        statisticsVo.setDoneCount(doneCount);
        return statisticsVo;
    }

    @Override
    public Long countDetail(String userId, LocalDateTime startTime, LocalDateTime endTime, String status) {
        LambdaQueryWrapper<TodoItemDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(TodoItemDetail::getTodoId);
        queryWrapper.eq(StrUtil.isNotEmpty(userId), TodoItemDetail::getUserId, userId);
        queryWrapper.eq(StrUtil.isNotEmpty(status), TodoItemDetail::getStatus, status);
        queryWrapper.ge(null != startTime, TodoItemDetail::getCreateTime, startTime);
        queryWrapper.le(null != endTime, TodoItemDetail::getCreateTime, endTime);
        return todoItemDetailMapper.selectCount(queryWrapper);
    }

    @Override
    public Long countItem(String userId, LocalDateTime startTime, LocalDateTime endTime, String state) {
        LambdaQueryWrapper<TodoItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(TodoItem::getBusinessId);
        queryWrapper.eq(StrUtil.isNotEmpty(userId), TodoItem::getUserId, userId);
        queryWrapper.eq(StrUtil.isNotEmpty(state), TodoItem::getState, state);
        queryWrapper.ge(null != startTime, TodoItem::getCreateTime, startTime);
        queryWrapper.le(null != endTime, TodoItem::getCreateTime, endTime);
        return todoItemMapper.selectCount(queryWrapper);
    }

    @Override
    public List<ProcessStatisticsVo> countByState(String userId, LocalDateTime startTime, LocalDateTime endTime, String type) {

        List<ProcessStatisticsVo> voList = todoItemMapper.countGroupByState(userId, startTime, endTime, type);
        Long total = 0L;
        Map<String, ProcessStatisticsVo> voMap;
        if (null != voList && !voList.isEmpty()) {
            voMap = voList.stream().collect(Collectors.toMap(ProcessStatisticsVo::getCode, vo -> vo));
        } else {
            voMap = new HashMap<>(1);
        }
        List<ProcessStatisticsVo> resultList = new ArrayList<>();
        Map<String, String> stateMap = TodoItem.STATE_MAP;
        for (String code : stateMap.keySet()) {
            Long value = 0L;
            ProcessStatisticsVo vo = voMap.get(code);
            if (null != vo) {
                value = vo.getValue();
            } else {
                vo = new ProcessStatisticsVo();
            }
            vo.setName(stateMap.get(code));
            vo.setValue(value);
            total += vo.getValue();
            resultList.add(vo);
        }
        ProcessStatisticsVo totalVo = new ProcessStatisticsVo();
        totalVo.setValue(total);
        totalVo.setName("总数");
        resultList.add(totalVo);
        return resultList;
    }


    /**
     * 统计不同状态的工单数据
     * 工单总量、处理中、已办结、未解决
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计结果
     */
    @Override
    public List<ProcessStatisticsVo> countAllWithState(LocalDateTime startTime, LocalDateTime endTime) {
        String applicationId = getApplicationId();
        //统计所有
        Long total = icdWorkFlowProcessMapper.countByStage(applicationId, null,startTime,endTime);
        //统计处理中对用低代码中的状态未1、3、5
        Integer[] stages = new Integer[]{1, 3, 5};

        Long countDoing = icdWorkFlowProcessMapper.countByStage(applicationId, new Integer[]{1, 3, 5},null,null);
        //统计已解决
        Long countSuccess = icdWorkFlowProcessMapper.countByStage(applicationId, new Integer[]{2},startTime,endTime);
        //统计未解决
        Long countFailed = icdWorkFlowProcessMapper.countByStage(applicationId, new Integer[]{4},null,null);
        List<ProcessStatisticsVo> resultList = new ArrayList<>();
        Map<String, String> stateMap = TodoItem.STATE_MAP;
        for (String key : stateMap.keySet()) {
            ProcessStatisticsVo vo = new ProcessStatisticsVo();
            vo.setName(stateMap.get(key));
            vo.setCode(key);
            Long value = 0L;
            switch (key) {
                case TodoItem.STATE_ACTIVE:
                    value = countDoing;
                    break;
                case TodoItem.STATE_SUCCESS:
                    value = countSuccess;
                    break;
                case TodoItem.STATE_FAILURE:
                    value = countFailed;
                    break;
                default:
                    break;
            }
            vo.setValue(value);
            resultList.add(vo);
        }
        ProcessStatisticsVo totalVo = new ProcessStatisticsVo();
        totalVo.setName("总数");
        totalVo.setCode("total");
        totalVo.setValue(total);
        resultList.add(totalVo);
        return resultList;
    }

    @Override
    public List<ProcessStatisticsVo> statisticsWithType(String userId, LocalDateTime startTime, LocalDateTime endTime, String modular) {
        return todoItemMapper.countGroupByType(userId, startTime, endTime, modular, null);
    }

    @Override
    public JSONObject statisticsResolution(String userId, LocalDateTime startTime, LocalDateTime endTime, String modular) {

        JSONObject result = new JSONObject();
        //获取类别列表
        List<String> typeList = todoItemMapper.selectTypeList();
        //所有的工单数据
        List<ProcessStatisticsVo> allVoList = todoItemMapper.countGroupByType(userId, startTime, endTime, modular, null);
        Map<String, Long> allVoMap = allVoList.stream().collect(Collectors.toMap(ProcessStatisticsVo::getType, ProcessStatisticsVo::getValue));
        //已解决工单数据
        List<ProcessStatisticsVo> doneVoList = todoItemMapper.countGroupByType(userId, startTime, endTime, modular, TodoItem.STATE_SUCCESS);
        Map<String, Long> doneVoMap = doneVoList.stream().collect(Collectors.toMap(ProcessStatisticsVo::getType, ProcessStatisticsVo::getValue));
        if (null != typeList && !typeList.isEmpty()) {
            Long[] totalList = new Long[typeList.size()];
            Long[] unDoneList = new Long[typeList.size()];
            Long[] doneList = new Long[typeList.size()];
            for (int i = 0; i < typeList.size(); i++) {
                String type = typeList.get(i);
                Long total = allVoMap.get(type);
                long unDoneCount = 0L;
                if (null == total) {
                    total = 0L;
                }
                Long doneCount = doneVoMap.get(type);
                if (null == doneCount) {
                    doneCount = 0L;
                }
                doneCount = total - doneCount;
                totalList[i] = total;
                unDoneList[i] = unDoneCount;
                doneList[i] = doneCount;
            }
            result.set("typeList", typeList);
            result.set("未解决", unDoneList);
            result.set("已解决", doneList);
        }


        return result;
    }


    /**
     * 统计人员处理工单的数量
     *
     * @param deptId    部门id
     * @param userId    用户id
     * @param modular   所属模块
     * @param type      类型
     * @param startTime 开始时间
     * @param endTime   截至时间
     * @return 统计结果列表
     */
    @Override
    public List<ProcessStatisticsVo> statisticsDetailWithUser(String deptId, String userId, LocalDateTime startTime, LocalDateTime endTime, String modular, String type, String itemState) {

        return todoItemDetailMapper.countByUser(deptId, userId, startTime, endTime, modular, type, itemState);

    }

    /**
     * 按部门统计发起的工单数据
     *
     * @param deptId    部门id
     * @param startTime 开始时间
     * @param endTime   截止时间
     * @param status    状态
     * @return 统计结果列表
     */
    @Override
    public List<ProcessStatisticsVo> statisticsWithDept(String deptId, LocalDateTime startTime, LocalDateTime endTime, String type, String status) {
        return todoItemMapper.countGroupByDept(deptId, startTime, endTime, type, status);
    }


    /**
     * 统计各个部门不同类型的工单数量
     * deptList
     * 电脑维修：[]
     * 网络故障 []
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param status    状态
     * @return 统计结果
     */
    @Override
    public JSONObject statisticsDeptType(LocalDateTime startTime, LocalDateTime endTime, String status) {
        JSONObject result = new JSONObject();
        List<ProcessStatisticsVo> allVoList = todoItemMapper.countGroupByDeptType(null, startTime, endTime, status);
        if (null != allVoList && !allVoList.isEmpty()) {
            //根据部门分组
            Map<String, List<ProcessStatisticsVo>> mapByDept = allVoList.stream().collect(Collectors.groupingBy(ProcessStatisticsVo::getName));
            List<String> deptList = new ArrayList<>(mapByDept.keySet());
            Map<String, List<ProcessStatisticsVo>> mapByType = allVoList.stream().collect(Collectors.groupingBy(ProcessStatisticsVo::getType));
            List<String> typeList = new ArrayList<>(mapByType.keySet());
            for (String type : typeList) {
                List<ProcessStatisticsVo> voList = mapByType.get(type);
                Map<String, ProcessStatisticsVo> map = voList.stream().collect(Collectors.toMap(ProcessStatisticsVo::getName, vo -> vo));
                List<Long> valueList = new ArrayList<>(deptList.size());
                for (String dept : deptList) {
                    ProcessStatisticsVo vo = map.get(dept);
                    Long value = 0L;
                    if (null != vo) {
                        value = vo.getValue();
                    }
                    valueList.add(value);
                }
                result.set(type, valueList);
            }

            result.set("typeList", typeList);
        }
        return result;
    }

    /**
     * 按月份统计工单变化趋势
     * 全部、各个类型工单数据
     * 响应内容：月份 [1月，2月，3月]
     * 类型 [全部、电脑维修、网络故障]
     * 电脑维修： [20、15、5]
     * 　网络故障：[20、15、5]
     *
     * @param year   年
     * @param months 区间段：-1 按照年查询；大于0 则查询几个月，最大不能超过12
     * @param state  状态
     * @return 统计结果
     */
    @Override
    public JSONObject statisticsTrendByMonthType(Integer year, Integer months, String state) {
        JSONObject result = new JSONObject();
        if (null == months || 0 == months || months > 12) {
            throw new RuntimeException("月份条件不能为空");
        }
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        int realMonths = 0;
        //查全年
        if (null != year && months.equals(-1)) {
            startTime = LocalDateTime.of(year, 1, 1, 0, 0);
            if (year == LocalDate.now().getYear()) {
                endTime = LocalDateTime.now();
            } else {
                endTime = LocalDateTime.of(year, 12, 31, 23, 59);
            }
                realMonths = endTime.getMonthValue() - startTime.getMonthValue() + 1;
        }
        if (months != -1) {
            endTime = LocalDateTime.now();
            startTime = endTime.plusMonths(-months);
            realMonths = months;
        }
        if (null == startTime) {
            throw new RuntimeException("查询条件异常");
        }
        //统计信息
        List<String> typeList = todoItemMapper.selectTypeList();
        List<ProcessStatisticsVo> voList = todoItemMapper.countGroupByYearMonthType(null, null, startTime, endTime, state);
        Map<String, List<ProcessStatisticsVo>> groupMap = voList.stream().collect(Collectors.groupingBy(ProcessStatisticsVo::getType));
        //组装月份列表
        String[] monthArr = new String[realMonths];
        LocalDate lastDate = LocalDate.of(endTime.getYear(), endTime.getMonthValue(), 1);
        LocalDate firstDate = LocalDate.of(startTime.getYear(), startTime.getMonthValue(), 1);
        Long[] totalList = new Long[realMonths];
        int index = 0;
        while (firstDate.isBefore(lastDate) || firstDate.isEqual(lastDate)) {
            int thisYear = firstDate.getYear();
            int thisMonth = firstDate.getMonthValue();
            String yearMonth = thisYear + "-" + thisMonth;
            monthArr[index] = (yearMonth);
            totalList[index] = 0L;
            index++;
            firstDate = firstDate.plusMonths(1);
        }
        result.set("date", monthArr);
        result.set("typeList", typeList);

        for (String type : typeList) {
            Long[] valueArr = new Long[realMonths];
            List<ProcessStatisticsVo> listByType = groupMap.get(type);
            Map<String, Long> voMap = new HashMap<>(voList.size());
            if (null != listByType && !listByType.isEmpty()) {
                for (ProcessStatisticsVo vo : listByType) {
                    String key = vo.getYear() + "-" + vo.getMonth();
                    Long value = vo.getValue();
                    voMap.put(key, value);
                }
            }
            for (int i = 0; i < realMonths; i++) {
                String key = monthArr[i];
                Long value = null != voMap.get(key) ? voMap.get(key) : 0L;
                valueArr[i] = value;
                totalList[i] = totalList[i] + value;
            }
            result.set(type, valueArr);
        }
        result.set("全部", totalList);
        return result;
    }

    @Override
    public List<ProcessStatisticsVo> statisticsWithYear(Integer year, String type, String state) {
        LocalDateTime startTime = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(year, 12, 31, 23, 59);
        if (year == LocalDate.now().getYear()) {
            endTime = LocalDateTime.now();
        }
        List<ProcessStatisticsVo> voList = todoItemMapper.countGroupByYearMonth(null, null, startTime, endTime, type, state);
        Map<Integer, ProcessStatisticsVo> voMap = new HashMap<>();
        if (null != voList && !voList.isEmpty()) {
            voMap = voList.stream().collect(Collectors.toMap(ProcessStatisticsVo::getMonth, vo -> vo));
        }
        List<ProcessStatisticsVo> resultList = new ArrayList<>(endTime.getMonthValue() - startTime.getMonthValue() + 1);
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, endTime.getMonthValue(), 1);
        while (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
            ProcessStatisticsVo vo = voMap.get(startDate.getMonthValue());
            if (null == vo) {
                vo = new ProcessStatisticsVo();
                vo.setYear(year);
                vo.setMonth(startDate.getMonthValue());
                vo.setValue(0L);
            }
            resultList.add(vo);
            startDate = startDate.plusMonths(1);
        }
        return resultList;
    }

    @Override
    public List<ProcessStatisticsVo> statisticsWithDeptType(String deptId, LocalDateTime startTime, LocalDateTime endTime, String state) {
        //获取统计信息，以所属部门和类型分组
        List<ProcessStatisticsVo> voList = todoItemMapper.countGroupByDeptType(deptId, startTime, endTime, state);
        if (null != voList && !voList.isEmpty()) {
            Map<String, List<ProcessStatisticsVo>> groupMap = voList.stream().collect(Collectors.groupingBy(ProcessStatisticsVo::getName));
            Set<String> deptNameSet = groupMap.keySet();
            List<ProcessStatisticsVo> resultList = new ArrayList<>(deptNameSet.size());
            for (String deptName : deptNameSet) {
                ProcessStatisticsVo resultVo = new ProcessStatisticsVo();
                resultVo.setName(deptName);
                resultVo.setCode(deptName);
                resultVo.setType("合计");
                Long value = 0L;
                List<ProcessStatisticsVo> childList = groupMap.get(deptName);
                if (null != childList && !childList.isEmpty()) {
                    resultVo.setChildren(childList);
                    for (ProcessStatisticsVo child : childList) {
                        value += child.getValue();
                        child.setCode(deptName + "-" + child.getType());
                    }
                }
                resultVo.setValue(value);
                resultList.add(resultVo);
            }
            return resultList;
        }

        return null;
    }


    /**
     * 统计部门人员发起工单数量
     *
     * @param deptId    部门id
     * @param userId    用户id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param state     状态
     * @param type      类型
     * @return 统计结果
     */
    @Override
    public List<ProcessStatisticsVo> statisticsWithDeptUserType(String deptId, String userId, String type, LocalDateTime startTime, LocalDateTime endTime, String state) {
        List<ProcessStatisticsVo> voList = todoItemMapper.countGroupByDeptUserType(deptId, userId, type, state, startTime, endTime);
        return handResultDeptUserType(voList);
    }

    @Override
    public List<ProcessStatisticsVo> statisticsDetailWithUserType(LocalDateTime startTime, LocalDateTime endTime, String state, String itemState) {
        List<ProcessStatisticsVo> voList = todoItemDetailMapper.countByUserType(null, startTime, endTime, null, state, itemState);
        return handResultDeptUserType(voList);
    }



    private List<ProcessStatisticsVo> handResultDeptUserType(List<ProcessStatisticsVo> voList) {
        List<ProcessStatisticsVo> resultList = new ArrayList<>();
        if (null != voList && !voList.isEmpty()) {
            Map<String, List<ProcessStatisticsVo>> deptMap = voList.stream().collect(Collectors.groupingBy(ProcessStatisticsVo::getDeptName));
            Set<String> deptNameSet = deptMap.keySet();
            for (String deptName : deptNameSet) {
                List<ProcessStatisticsVo> list = deptMap.get(deptName);
                ProcessStatisticsVo resultVo = new ProcessStatisticsVo();
                resultVo.setName(deptName);
                resultVo.setCode(deptName);
                resultVo.setType("合计");
                long value = 0L;
                if (null != list && !list.isEmpty()) {
                    List<ProcessStatisticsVo> childList = new ArrayList<>();
                    Map<String, List<ProcessStatisticsVo>> userMap = list.stream().collect(Collectors.groupingBy(ProcessStatisticsVo::getCode));
                    Set<String> userIdSet = userMap.keySet();
                    for (String userId : userIdSet) {
                        List<ProcessStatisticsVo> listByUserId = userMap.get(userId);
                        ProcessStatisticsVo childVo = new ProcessStatisticsVo();
                        childVo.setName(listByUserId.get(0).getName());
                        childVo.setType("合计");
                        childVo.setCode(userId);
                        Long childValue = 0L;
                        for (ProcessStatisticsVo child : listByUserId) {
                            child.setCode(child.getCode() + "-" + child.getType());
                            childValue += child.getValue();
                        }
                        childVo.setChildren(listByUserId);
                        childVo.setValue(childValue);
                        childList.add(childVo);
                        value += childValue;
                    }
                    resultVo.setChildren(childList);
                }
                resultVo.setValue(value);
                resultList.add(resultVo);
            }
        }
        return resultList;
    }


}
