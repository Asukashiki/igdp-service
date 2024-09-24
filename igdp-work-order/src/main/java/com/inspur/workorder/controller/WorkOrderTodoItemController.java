package com.inspur.workorder.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.enums.BusinessType;
import com.inspur.common.utils.LoginHelper;
import com.inspur.common.utils.poi.ExcelUtil;
import com.inspur.system.service.ISysConfigService;
import com.inspur.workorder.domain.TodoItem;
import com.inspur.workorder.domain.TodoItemDetail;
import com.inspur.workorder.domain.payload.ProcessQueryPayload;
import com.inspur.workorder.domain.vo.ProcessStatisticsVo;
import com.inspur.workorder.domain.vo.TodoItemDetailVo;
import com.inspur.workorder.domain.vo.TodoItemPersonalStatisticsVo;
import com.inspur.workorder.domain.vo.TodoItemVo;
import com.inspur.workorder.service.ITodoItemDetailService;
import com.inspur.workorder.service.ITodoItemService;
import com.inspur.workorder.service.ITodoItemStatisticsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author liyunlong
 * @date 2024/4/8
 */
@RestController
@RequestMapping("/work-order/todo-item")
public class WorkOrderTodoItemController extends BaseController {

    @Resource
    private ITodoItemService todoItemService;
    @Resource
    private ITodoItemDetailService todoItemDetailService;
    @Resource
    private ITodoItemStatisticsService todoItemStatisticsService;
    @Resource
    private ISysConfigService configService;

    /**
     * 获取分页列表
     */
    @GetMapping("/list")
    public TableDataInfo<?> getTodoItemPage(TodoItem queryParams) {
        startPage();
        List<TodoItem> itemList = todoItemService.selectList(queryParams);
        return getDataTable(itemList);
    }

    /**
     * 获取工单展示信息列表
     */
    @GetMapping("/voList")
    public TableDataInfo<?> getTodoItemVoPage(TodoItem queryParams) {

        startPage();
        List<TodoItemVo> voList = todoItemService.getVoList(queryParams);
        return getDataTable(voList);
    }
    /**
     * 工单类型查询转拼音
     */
    @GetMapping("/typeList")
    public AjaxResult getTypeList() {

        List<Map<String,String>> typeList = todoItemService.getTypeList();

        return AjaxResult.success(typeList);
    }
    /**
     * 分发起人工单统计
     */
    @GetMapping("/dlList")
    public AjaxResult getDistributeUser(TodoItem queryParams) {

        List<JSONObject> dlList = todoItemService.getDlList(queryParams);

        return AjaxResult.success(dlList);
    }
    /**
     * 分发起部门工单统计
     */
    @GetMapping("/deptList")
    public AjaxResult getDistributeDept(TodoItem queryParams) {

        List<JSONObject> deptList = todoItemService.getDeptList(queryParams);

        return AjaxResult.success(deptList);
    }

    /**
     * 分时间工单统计
     */
    @GetMapping("/timeList")
    public AjaxResult getDistributeTime(TodoItem queryParams) {

        List<Map<String,Object>> timeList = todoItemService.getTimeList(queryParams);

        return AjaxResult.success(timeList);
    }
    /**
     * 获取工单类型选择列表
     */
    @GetMapping("/typeOptions")
    public AjaxResult getTodoItemTypeOptions() {
        return success(todoItemService.selectTypeList());
    }

    @PostMapping("/export")
    public void export(HttpServletResponse response, TodoItem queryParams) {
        List<JSONObject> list = new ArrayList<>();
        List<JSONObject> jsonList = new ArrayList<>();
        String type = queryParams.getParams().get("type").toString();
        queryParams.setParamsJson("export");
        if (type.equals("user")){
            list = todoItemService.getDlList(queryParams);
        }else if(type.equals("dept")){
            list = todoItemService.getDeptList(queryParams);
        }else {
            List<Map<String,Object>> timeList = todoItemService.getTimeList(queryParams);
            list = timeList.stream().map(JSONObject::new).collect(Collectors.toList());
        }

        todoItemService.export(list,response);
    }
    /**
     * 根据id获取详情
     */
    @GetMapping("/info/{id}")
    public AjaxResult getTodoItemById(@PathVariable("id") String id) {
        TodoItem item = todoItemService.getById(id);
        if (null != item) {
            return AjaxResult.success(item);
        } else {
            return AjaxResult.error("未找到相关信息");
        }
    }

    /**
     * 获取个人代办列表
     */
    @GetMapping("/undoList")
    public AjaxResult getTodoItemUndoList() {
        startPage();
        List<TodoItemDetailVo> voList = todoItemDetailService.selectVoList(LoginHelper.getUserId(), TodoItemDetail.STATUS_NEW);
        return success(voList);
    }


    /**
     * 个人相关信息统计
     * 我的待办、我发起的、我的已办、我的办结等数据
     */
    @GetMapping("/statistics/personal-statistics")
    public AjaxResult getPersonalStatistics(Integer year, Integer month) {

        LocalDateTime[] dates = handleDate(year, month, null, null);
        TodoItemPersonalStatisticsVo statisticsVo = todoItemStatisticsService.getPersonalStatistics(LoginHelper.getUserId(), dates[0], dates[1]);
        return success(statisticsVo);
    }


    /**
     * 统计所有的工单
     * 工单总量、处理中、已办结、未解决
     */
    @GetMapping("/statistics/all")
    public AjaxResult getAllStatistics(Integer year, Integer month, String type) {
        LocalDateTime[] dates = handleDate(year, month, null, null);
        List<ProcessStatisticsVo> voList = todoItemStatisticsService.countByState(null, dates[0], dates[1], type);
        return success(voList);

    }


    /**
     * 统计，我发起的数据
     *
     * @param year  年
     * @param month 月
     */
    @GetMapping("/statistics/personal-initiative")
    public AjaxResult getPersonalInitiative(Integer year, Integer month) {

        LocalDateTime[] dates = handleDate(year, month, null, null);
        Long count = todoItemStatisticsService.countItem(LoginHelper.getUserId(), dates[0], dates[1], null);
        return success(count);
    }

    /**
     * 统计我的办结工单
     */
    @GetMapping("/statistics/personal-conclude")
    public AjaxResult getPersonalConclude(Integer year, Integer month) {
        LocalDateTime[] dates = handleDate(year, month, null, null);
        Long count = todoItemStatisticsService.countItem(LoginHelper.getUserId(), dates[0], dates[1], "办结");
        return success(count);
    }

    /**
     * 统计我的待办数量
     */
    @GetMapping("/personal-undo")
    public AjaxResult getPersonalUndo(Integer year, Integer month) {
        LocalDateTime[] dates = handleDate(year, month, null, null);
        Long count = todoItemStatisticsService.countDetail(LoginHelper.getUserId(), dates[0], dates[1], TodoItemDetail.STATUS_NEW);
        return success(count);
    }

    /**
     * 已办工单统计
     */
    @GetMapping("/statistics/personal-done")
    public AjaxResult getPersonalDone(Integer year, Integer month) {
        LocalDateTime[] dates = handleDate(year, month, null, null);
        Long count = todoItemStatisticsService.countDetail(LoginHelper.getUserId(), dates[0], dates[1], TodoItemDetail.STATUS_COMPLETE);
        return success(count);
    }


    /**
     * 工单类别数据统计
     */
    @GetMapping("/statistics/with-type")
    public AjaxResult getStatisticsByType(Integer year, Integer month) {
        LocalDateTime[] dates = handleDate(year, month, null, null);
        List<ProcessStatisticsVo> voList = todoItemStatisticsService.statisticsWithType(getUserId(), dates[0], dates[1], null);
        return success(voList);
    }

    /**
     * 领导工单类别数据统计
     */
    @GetMapping("/statistics/leader-type")
    public AjaxResult getLeaderByType(Integer year, Integer month) {
        LocalDateTime[] dates = handleDate(year, month, null, null);
        String userId = "";
        List<ProcessStatisticsVo> voList = todoItemStatisticsService.statisticsWithType(userId, dates[0], dates[1], null);
        return success(voList);
    }

    /**
     * 工单类别解决率
     */
    @GetMapping("/statistics/resolution-type")
    public AjaxResult getStatisticsByResolution(Integer year, Integer month) {
        LocalDateTime[] dates = handleDate(year, month, null, null);
        JSONObject result = todoItemStatisticsService.statisticsResolution(getUserId(), dates[0], dates[1], null);
        return success(result);
    }


    /**
     * 按人员统计工单处理数量统计
     */
    @GetMapping("/statistics/with-person")
    public AjaxResult getStatisticsByWithPerson(Integer year, Integer months, String type) {
        LocalDateTime[] dates = handleDate(year, months, null, null);
        String deptIdStr = configService.selectConfigByKey("gdzfw.statistics.dept.ids");
        List<ProcessStatisticsVo> resultList = new ArrayList<>();
        if (StrUtil.isNotEmpty(deptIdStr)) {
            String[] deptIds = deptIdStr.split(",");
            for (String deptId : deptIds) {
                List<ProcessStatisticsVo> voList = todoItemStatisticsService.statisticsDetailWithUser(deptId, null, dates[0], dates[1], null, type, null);
                resultList.addAll(voList);
            }
        } else {
            resultList = todoItemStatisticsService.statisticsDetailWithUser(null, null, dates[0], dates[1], null, type, null);
        }
        return success(resultList);
    }

    /**
     * 按部门统计工单数量
     *
     * @param year  年
     * @param months 月
     * @param type  类型
     */
    @GetMapping("/statistics/with-dept")
    public AjaxResult getStatisticsByWithDept(Integer year, Integer months, String type, String state) {
        LocalDateTime[] dates = handleDate(year, months, null, null);
        List<ProcessStatisticsVo> voList = todoItemStatisticsService.statisticsWithDept(null, dates[0], dates[1], type, state);
        return success(voList);
    }


    /**
     * 按年统计各个月的工单数量
     */
    @GetMapping("/statistics/with-year")
    public AjaxResult getStatisticsByWithYear(@RequestParam("year") Integer year, String type, String state) {

        List<ProcessStatisticsVo> voList = todoItemStatisticsService.statisticsWithYear(year, type, state);
        return success(voList);
    }


    /**
     * 按时间统计工单趋势
     */
    @GetMapping("/statistics/trend-type")
    public AjaxResult getTrendWithType(Integer year, Integer months, String state) {
        JSONObject result = todoItemStatisticsService.statisticsTrendByMonthType(year, months, state);
        return success(result);
    }


    /**
     * 统计部门工单数量表格
     * 工单发起数量统计
     */
    @GetMapping("/statistics/dept-item-list")
    public AjaxResult personalList(ProcessQueryPayload queryParam) {
        Map<String, Object> params = queryParam.getParams();
        LocalDateTime[] dates = handleDate(queryParam.getYear(), queryParam.getMonth(),
                null != params.get("beginTime") ? params.get("beginTime").toString() : null, null != params.get("endTime") ? params.get("endTime").toString() : null);
        List<ProcessStatisticsVo> resultList = todoItemStatisticsService.statisticsWithDeptType(queryParam.getDeptId(),
                dates[0], dates[1], queryParam.getState());
        return success(resultList);
    }


    /**
     * 导出统计的部门统计数据
     */
    @Log(title = "流程统计", businessType = BusinessType.EXPORT)
    @PostMapping("/statistics/export-dept-item-list")
    public void export(HttpServletResponse response, ProcessQueryPayload queryParam) {
        Map<String, Object> params = queryParam.getParams();
        LocalDateTime[] dates = handleDate(queryParam.getYear(), queryParam.getMonth(),
                null != params.get("beginTime") ? params.get("beginTime").toString() : null, null != params.get("endTime") ? params.get("endTime").toString() : null);
        List<ProcessStatisticsVo> resultList = todoItemStatisticsService.statisticsWithDeptType(queryParam.getDeptId(),
                dates[0], dates[1], queryParam.getState());
        List<ProcessStatisticsVo> exportList = new ArrayList<>();
        if (null != resultList && !resultList.isEmpty()) {
            for (ProcessStatisticsVo vo : resultList) {
                exportList.add(vo);
                if (null != vo.getChildren() && !vo.getChildren().isEmpty()) {
                    exportList.addAll(vo.getChildren());
                }
            }
        }
        ExcelUtil<ProcessStatisticsVo> util = new ExcelUtil<>(ProcessStatisticsVo.class);
        util.exportExcel(response, exportList, "工单统计");
    }

    /**
     * 统计部门人员处理工单信息
     * 工单处理统计
     */
    @GetMapping("/statistics/dept-user-list")
    public AjaxResult statisticsDeptUserList(ProcessQueryPayload queryParam) {
        Map<String, Object> params = queryParam.getParams();
        LocalDateTime[] dates = handleDate(queryParam.getYear(), queryParam.getMonth(),
                null != params.get("beginTime") ? params.get("beginTime").toString() : null, null != params.get("endTime") ? params.get("endTime").toString() : null);
        List<ProcessStatisticsVo> resultList = todoItemStatisticsService.statisticsDetailWithUserType(
                dates[0], dates[1], TodoItemDetail.STATUS_COMPLETE, queryParam.getItemState());
        return success(resultList);
    }



    /**
     * 导出统计的部门统计数据
     */
    @Log(title = "流程统计", businessType = BusinessType.EXPORT)
    @PostMapping("/statistics/export-dept-user-list")
    public void exportStatisticsDeptUserList(HttpServletResponse response, ProcessQueryPayload queryParam) {
        Map<String, Object> params = queryParam.getParams();
        LocalDateTime[] dates = handleDate(queryParam.getYear(), queryParam.getMonth(),
                null != params.get("beginTime") ? params.get("beginTime").toString() : null, null != params.get("endTime") ? params.get("endTime").toString() : null);
        List<ProcessStatisticsVo> resultList = todoItemStatisticsService.statisticsDetailWithUserType(
                dates[0], dates[1], TodoItemDetail.STATUS_COMPLETE, TodoItem.STATE_SUCCESS);
        List<ProcessStatisticsVo> exportList = new ArrayList<>();
        if (null != resultList && !resultList.isEmpty()) {
            for (ProcessStatisticsVo vo : resultList) {
                exportList.add(vo);
                if (null != vo.getChildren() && !vo.getChildren().isEmpty()) {
                    for (ProcessStatisticsVo innerVo : vo.getChildren()) {
                        exportList.add(innerVo);
                        exportList.addAll(innerVo.getChildren());
                    }
                }
            }
        }
        ExcelUtil<ProcessStatisticsVo> util = new ExcelUtil<>(ProcessStatisticsVo.class);
        util.exportExcel(response, exportList, "工单统计");
    }


    /**
     * 统计部门人员发起工单信息
     * 工单处理统计
     */
    @GetMapping("/statistics/dept-user-item-list")
    public AjaxResult statisticsItemDeptUserList(ProcessQueryPayload queryParam) {
        Map<String, Object> params = queryParam.getParams();
        LocalDateTime[] dates = handleDate(queryParam.getYear(), queryParam.getMonth(),
                null != params.get("beginTime") ? params.get("beginTime").toString() : null, null != params.get("endTime") ? params.get("endTime").toString() : null);
        List<ProcessStatisticsVo> resultList = todoItemStatisticsService.statisticsWithDeptUserType(queryParam.getDeptId(), queryParam.getUserId(),
                queryParam.getItemType(), dates[0], dates[1], queryParam.getState());
        return success(resultList);
    }


    /**
     * 导出统计的部门发起数据
     */
    @Log(title = "流程统计", businessType = BusinessType.EXPORT)
    @PostMapping("/statistics/export-dept-user-item-list")
    public void exportStatisticsDeptUserItemList(HttpServletResponse response, ProcessQueryPayload queryParam) {
        Map<String, Object> params = queryParam.getParams();
        LocalDateTime[] dates = handleDate(queryParam.getYear(), queryParam.getMonth(),
                null != params.get("beginTime") ? params.get("beginTime").toString() : null, null != params.get("endTime") ? params.get("endTime").toString() : null);
        List<ProcessStatisticsVo> resultList = todoItemStatisticsService.statisticsWithDeptUserType(queryParam.getDeptId(), queryParam.getUserId(),
                queryParam.getItemType(), dates[0], dates[1], queryParam.getState());
        List<ProcessStatisticsVo> exportList = new ArrayList<>();
        if (null != resultList && !resultList.isEmpty()) {
            for (ProcessStatisticsVo vo : resultList) {
                exportList.add(vo);
                if (null != vo.getChildren() && !vo.getChildren().isEmpty()) {
                    for (ProcessStatisticsVo innerVo : vo.getChildren()) {
                        exportList.add(innerVo);
                        exportList.addAll(innerVo.getChildren());
                    }
                }
            }
        }
        ExcelUtil<ProcessStatisticsVo> util = new ExcelUtil<>(ProcessStatisticsVo.class);
        util.exportExcel(response, exportList, "工单发起统计");
    }


    private LocalDateTime[] handleDate(Integer year, Integer month, String startTimeStr, String endTimeStr) {
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (null != year || null != month) {
            if (null != month) {
                if (null == year) {
                    year = LocalDate.now().getYear();
                }
                LocalDate firstDay = LocalDate.of(year, month, 1);
                startTime = LocalDateTime.of(firstDay, LocalTime.MIN);
                LocalDate lastDay = firstDay.with(TemporalAdjusters.lastDayOfMonth());
                //截止日期是下月1号
                endTime = LocalDateTime.of(lastDay, LocalTime.MAX);
            } else {
                startTime = LocalDateTime.of(LocalDate.of(year, 1, 1), LocalTime.MIN);
                endTime = LocalDateTime.of(LocalDate.of(year, 12, 31), LocalTime.MAX);
            }
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if (StrUtil.isNotEmpty(startTimeStr)) {
                startTime = LocalDateTime.parse(startTimeStr, formatter);
            }
            if (StrUtil.isNotEmpty(endTimeStr)) {
                endTime = LocalDateTime.parse(endTimeStr, formatter);
            }
        }
        return new LocalDateTime[]{startTime, endTime};
    }
    /**
     * 获取工单状态
     * @return
     */
    @GetMapping("/statistics/state")
    public AjaxResult state() {
        return success(todoItemService.getState());
    }

}

