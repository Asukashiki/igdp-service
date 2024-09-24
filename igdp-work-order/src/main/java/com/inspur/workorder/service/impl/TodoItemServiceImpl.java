package com.inspur.workorder.service.impl;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.inspur.common.constant.Constants;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.domain.entity.SysDept;
import com.inspur.common.core.domain.entity.SysUser;
import com.inspur.common.utils.StringUtils;
import com.inspur.system.service.ISysConfigService;
import com.inspur.system.service.ISysDictDataService;
import com.inspur.system.service.ISysUserService;
import com.inspur.workorder.domain.TodoItem;
import com.inspur.workorder.domain.TodoItemDetail;
import com.inspur.workorder.domain.payload.TodoItemDetailPayload;
import com.inspur.workorder.domain.payload.TodoItemPayload;
import com.inspur.workorder.domain.vo.ProcessStatisticsVo;
import com.inspur.workorder.domain.vo.TodoItemVo;
import com.inspur.workorder.mapper.TodoItemMapper;
import com.inspur.workorder.service.ITodoItemDetailService;
import com.inspur.workorder.service.ITodoItemService;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liyunlong
 * @date 2024/4/8
 */
@Service
@Slf4j
public class TodoItemServiceImpl extends ServiceImpl<TodoItemMapper, TodoItem> implements ITodoItemService {

    @Resource
    private ITodoItemDetailService todoItemDetailService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysConfigService configService;
    @Resource
    private ISysDictDataService dictDataService;

    @Override
    public List<TodoItem> selectList(TodoItem queryParam) {
        return list(new LambdaQueryWrapper<TodoItem>()
                .eq(StrUtil.isNotEmpty(queryParam.getUserId()), TodoItem::getUserId, queryParam.getUserId())
                .like(StrUtil.isNotEmpty(queryParam.getTitle()), TodoItem::getTitle, queryParam.getTitle())
                .eq(StrUtil.isNotEmpty(queryParam.getSource()), TodoItem::getSource, queryParam.getSource())
                .ge(null != queryParam.getParams().get("beginTime"), TodoItem::getCreateTime, queryParam.getParams().get("beginTime"))
                .le(null != queryParam.getParams().get("endTime"), TodoItem::getCreateTime, queryParam.getParams().get("endTime"))
                .like(StrUtil.isNotEmpty(queryParam.getBusinessCode()), TodoItem::getBusinessCode, queryParam.getBusinessCode()));
    }

    @Override
    public List<TodoItemVo> getVoList(TodoItem queryParam) {
        return this.baseMapper.selectVoList(queryParam);
    }

    @Override
    public List<Map<String, String>> getTypeList() {
        LambdaQueryWrapper<TodoItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(TodoItem::getModular);
        wrapper.isNotNull(TodoItem::getModular);
        wrapper.ne(TodoItem::getModular, "");
        wrapper.groupBy(TodoItem::getModular);
        List<TodoItem> todoItemList = list(wrapper);
        List<Map<String, String>> typeList = new ArrayList<>();
        todoItemList.forEach(todoItem -> {
            Map<String, String> typeMap = new HashMap<>();
            typeMap.put(chineseToPinyin(todoItem.getModular()), todoItem.getModular());
            typeList.add(typeMap);
        });
        return typeList;
    }


    /**
     * 字符串转拼音
     *
     * @param chinese
     * @return
     */
    public static String chineseToPinyin(String chinese) {
        StringBuilder pinyin = new StringBuilder();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        char[] chars = chinese.toCharArray();
        for (char c : chars) {
            try {
                String[] arr = PinyinHelper.toHanyuPinyinStringArray(c, format);
                if (arr == null || arr.length == 0) {
                    pinyin.append(c);
                } else {
                    pinyin.append(arr[0]);
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        }
        return pinyin.toString();
    }

    @Override
    public List<JSONObject> getDlList(TodoItem queryParam) {
        StringBuffer sql = new StringBuffer();
        List<String> typeList = selectTypeList();
        if (null != typeList && !typeList.isEmpty()) {
            typeList.forEach(type -> {
                if (StringUtils.isEmpty(queryParam.getParamsJson())) {
                    sql.append(" COUNT(case when modular = " + "'").append(type).append("'").append(" then 1 end)").append(chineseToPinyin(type)).append(" ,");
                } else {
                    sql.append(" COUNT(case when modular = " + "'").append(type).append("'").append(" then 1 end)").append(type).append(" ,");
                }
            });
        }
        String getSql = sql.toString();
        queryParam.setSql(getSql);
        return this.baseMapper.selectDlList(queryParam);
    }

    @Override
    public List<JSONObject> getDeptList(TodoItem queryParam) {
        StringBuffer sql = new StringBuffer();
        List<String> typeList = selectTypeList();
        if (null != typeList && !typeList.isEmpty()) {
            typeList.forEach(type -> {
                if (StringUtils.isEmpty(queryParam.getParamsJson())) {
                    sql.append(" COUNT(case when modular = " + "'").append(type).append("'").append(" then 1 end)").append(chineseToPinyin(type)).append(" ,");
                } else {
                    sql.append(" COUNT(case when modular = " + "'").append(type).append("'").append(" then 1 end)").append(type).append(" ,");
                }
            });
        }
        String getSql = sql.toString();
        queryParam.setSql(getSql);
        return this.baseMapper.selectDeptList(queryParam);
    }

    @Override
    public List<Map<String, Object>> getTimeList(TodoItem queryParam) {
        StringBuffer sql = new StringBuffer();
        List<String> typeList = selectTypeList();
        if (null != typeList && !typeList.isEmpty()) {
            typeList.forEach(type -> {
                if (StringUtils.isEmpty(queryParam.getParamsJson())) {
                    sql.append(" COUNT(case when modular = " + "'").append(type).append("'").append(" then 1 end)").append(chineseToPinyin(type)).append(" ,");
                } else {
                    sql.append(" COUNT(case when modular = " + "'").append(type).append("'").append(" then 1 end)").append(type).append(" ,");
                }
            });
        }
        QueryWrapper<TodoItem> wrapper = new QueryWrapper<>();
        StringBuilder group = new StringBuilder();
        if (queryParam.getTimeType().equals("2")) {
            if (StringUtils.isEmpty(queryParam.getParamsJson())) {
                sql.append("left(submit_time,7) as 时间,");
            } else {
                sql.append("left(submit_time,7) as date,");
            }
            group.append("left(submit_time,7)");
        } else if (queryParam.getTimeType().equals("4")) {

            if (StringUtils.isEmpty(queryParam.getParamsJson())) {
                sql.append("left(submit_time,4) as 时间,");
            } else {
                sql.append("left(submit_time,4) as date,");
            }
            group.append("left(submit_time,4)");
        } else {
            if (StringUtils.isEmpty(queryParam.getParamsJson())) {
                sql.append("submit_time as 时间,");
            } else {
                sql.append("submit_time as date,");
            }

            group.append("submit_time");
        }
        if (StringUtils.isEmpty(queryParam.getParamsJson())) {
            wrapper.select(sql + "count(1) as 总数");
        } else {
            wrapper.select(sql + "count(1) as count");
        }

        wrapper.isNotNull("submit_time");
        wrapper.in(StringUtils.isNotEmpty(queryParam.getUserId()), "user_id", queryParam.getUserId());
        wrapper.in(StringUtils.isNotEmpty(queryParam.getDeptId()), "dept_id", queryParam.getDeptId());
        wrapper.in(StringUtils.isNotEmpty(queryParam.getType()), "type", queryParam.getType());
        wrapper.eq(StringUtils.isNotEmpty(queryParam.getModular()), "modular", queryParam.getModular());
        wrapper.ge(null != queryParam.getParams().get("beginTime"), "submit_time", queryParam.getParams().get("beginTime"));
        wrapper.le(null != queryParam.getParams().get("endTime"), "submit_time", queryParam.getParams().get("endTime"));

        wrapper.groupBy(String.valueOf(group));
        if (StringUtils.isEmpty(queryParam.getParamsJson())) {
            wrapper.orderByDesc("时间");
        } else {
            wrapper.orderByDesc("date");
        }

        List<Map<String, Object>> wrapperMap = listMaps(wrapper);
        if (queryParam.getTimeType().equals("1")) {
            for (int i = 0; i < wrapperMap.size(); i++) {
                Calendar calendar = Calendar.getInstance();
                Object todoDate = new Object();
                String mapDate;
                if (StringUtils.isEmpty(queryParam.getParamsJson())) {
                    todoDate = wrapperMap.get(i).get("时间");
                    mapDate = "时间";
                } else {
                    todoDate = wrapperMap.get(i).get("DATE");
                    mapDate = "DATE";
                }

                Date date = (Date) todoDate;
                calendar.setTime(date);
                int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
                wrapperMap.get(i).put(mapDate, weekOfYear);
            }
            wrapperMap = areMapsEqual(wrapperMap);
        } else if (queryParam.getTimeType().equals("3")) {
            for (int i = 0; i < wrapperMap.size(); i++) {
                Calendar calendar = Calendar.getInstance();
                Object todoDate = new Object();
                String mapDate;
                if (StringUtils.isEmpty(queryParam.getParamsJson())) {
                    todoDate = wrapperMap.get(i).get("时间");
                    mapDate = "时间";
                } else {
                    todoDate = wrapperMap.get(i).get("DATE");
                    mapDate = "DATE";
                }
                Date date = (Date) todoDate;
                calendar.setTime(date);
                int month = calendar.get(Calendar.MONTH) + 1;
                month = (month - 1) / 3 + 1;
                wrapperMap.get(i).put(mapDate, month);
            }
            wrapperMap = areMapsEqual(wrapperMap);
        }
        return wrapperMap;
    }

    private static List<Map<String, Object>> areMapsEqual(List<Map<String, Object>> wrapperMap) {

        Map<String, Object> m1 = new HashMap<>();
        Map<String, Object> m2 = new HashMap<>();
        for (int i = 0; i < wrapperMap.size() - 1; i++) {
            List<Integer> numList = new ArrayList<Integer>();
            for (int j = i + 1; j < wrapperMap.size(); j++) {
                m1 = wrapperMap.get(i);
                m2 = wrapperMap.get(j);
                for (String m : m1.keySet()) {
                    String k = null;
                    if (m.equals("DATE") || m.equals("时间")) {
                        k = m;
                    }
                    if (k != null && m1.get(k).equals(m2.get(k))) {
                        for (Map.Entry<String, Object> entry : m2.entrySet()) {
                            String key = entry.getKey();
                            int value = Integer.parseInt(String.valueOf(entry.getValue()));
                            if (!key.equals(k) && !key.equals(k)) {
                                int value2 = Integer.parseInt(String.valueOf(m1.getOrDefault(key, 0)));
                                m1.put(key, value2 + value);
                            }
                        }
                        numList.add(j);

                    }
                }
            }
            if (numList.size() != 0) {
                for (int num = numList.size() - 1; num >= 0; num--) {
                    int i1 = numList.get(num);
                    wrapperMap.remove(i1);
                }
                ;
            }

        }

        return wrapperMap;
    }

    @Override
    public List<String> selectTypeList() {
        LambdaQueryWrapper<TodoItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(TodoItem::getModular);
        wrapper.isNotNull(TodoItem::getModular);
        wrapper.ne(TodoItem::getModular, "");
        wrapper.groupBy(TodoItem::getModular);
        return listObjs(wrapper);
    }

    @Override
    public TodoItem selectByBusinessCode(String modular, String type, String businessCode, String source) {
        return getOne(new LambdaQueryWrapper<TodoItem>()
                .eq(TodoItem::getModular, modular)
                .eq(TodoItem::getType, type)
                .eq(TodoItem::getBusinessCode, businessCode)
                .eq(TodoItem::getSource, source));
    }

    @Override
    public TodoItem selectByBusinessId(String modular, String type, String businessId, String source) {
        return getOne(new LambdaQueryWrapper<TodoItem>()
                .eq(TodoItem::getModular, modular)
                .eq(StrUtil.isNotEmpty(type), TodoItem::getType, type)
                .eq(TodoItem::getBusinessId, businessId)
                .eq(TodoItem::getSource, source));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult pushTodoItem(TodoItemPayload todoItemPayload) {
        AjaxResult checkResult = checkPayload(todoItemPayload);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        log.info("工单推送内容：{}", todoItemPayload);
        //获取当前数据是否已经存在
        String modularDictType = configService.selectConfigByKey("sys.work_order.modular.dict_type");
        if (StringUtils.isNotEmpty(modularDictType)) {
            String modular = dictDataService.selectDictLabel(modularDictType, todoItemPayload.getFormId());
            if (StringUtils.isNotEmpty(modular)) {
                todoItemPayload.setModular(modular);
            }
        }
        TodoItem todoItem = selectByBusinessId(todoItemPayload.getModular(), todoItemPayload.getType(), todoItemPayload.getBusinessId(), todoItemPayload.getSource());
        String todoId;
        if (todoItem != null) {
            todoId = todoItem.getTodoId();
            updateTodoItem(todoItemPayload, todoItem);
        } else {
            todoItem = addTodoItem(todoItemPayload);
            todoId = todoItem.getTodoId();
        }
        //先更新明细，再新建最新处理人相关明细
        todoItemDetailService.updateItemDetail(todoItem, todoItemPayload);
        todoItemDetailService.addTodoItemDetail(todoItem, todoItemPayload);
        return AjaxResult.success(todoId);
    }

    @Override
    public String getLinkUri(String appId, String formId, String dataId, String source) {
        if (source.equals(TodoItem.SOURCE_ICD)) {
            String runtimeUri = configService.selectConfigByKey("icd.runtime.uri");
            String detailUri = configService.selectConfigByKey("ist.workflow.form.detail.uri");
            if (StrUtil.isNotEmpty(runtimeUri) && StrUtil.isNotEmpty(detailUri)) {
                if (!runtimeUri.endsWith("/") && !detailUri.startsWith("/")) {
                    runtimeUri = runtimeUri + "/";
                }
                return runtimeUri + detailUri.replace("${dataId}", dataId).replace("${appId}", appId).replace("${formId}", formId);
            } else {
                log.error("获取icd的运行态地址配置或者流程表单明细uri为空：icd.runtime.uri/ist.workflow.form.detail.uri");
            }
        }
        return "";
    }

    @Override
    public AjaxResult cancelTodoItem(TodoItemPayload todoItemPayload) {
        TodoItem item = selectByBusinessId(todoItemPayload.getModular(), null, todoItemPayload.getBusinessId(), todoItemPayload.getSource());
        if (null == item) {
            return AjaxResult.error("没有相关事项内容");
        } else {
            removeById(item.getTodoId());
            //删除明细信息
            todoItemDetailService.remove(new LambdaQueryWrapper<TodoItemDetail>().eq(TodoItemDetail::getTodoId, item.getTodoId()));
            return AjaxResult.success();
        }
    }

    @Override
    public List<ProcessStatisticsVo> countGroupByType(String userId, LocalDateTime startTime, LocalDateTime endTime, String modular, String state) {
        return this.baseMapper.countGroupByType(userId, startTime, endTime, modular, state);
    }

    @Override
    public List<ProcessStatisticsVo> countGroupByTypeState(String userId, LocalDateTime startTime, LocalDateTime endTime, String modular) {
        return this.baseMapper.countGroupByStateType(userId, startTime, endTime, modular);
    }

    @Override
    public List<ProcessStatisticsVo> countGroupByState(String userId, LocalDateTime startTime, LocalDateTime endTime, String type) {
        return this.baseMapper.countGroupByState(userId, startTime, endTime, type);
    }

    @Override
    public List<ProcessStatisticsVo> countGroupByDept(String deptId, LocalDateTime startTime, LocalDateTime endTime, String type, String state) {
        return this.baseMapper.countGroupByDept(deptId, startTime, endTime, type, state);
    }

    @Override
    public List<ProcessStatisticsVo> countGroupByDeptType(String deptId, LocalDateTime startTime, LocalDateTime endTime, String state) {
        return this.baseMapper.countGroupByDeptType(deptId, startTime, endTime, state);
    }

    @Override
    public List<ProcessStatisticsVo> countGroupByMonth(String userId, String deptId, LocalDateTime startTime, LocalDateTime endTime, String type, String state) {
        return this.baseMapper.countGroupByYearMonth(userId, deptId, startTime, endTime, type, state);
    }

    @Override
    public List<ProcessStatisticsVo> countGroupByMonthType(String userId, String deptId, LocalDateTime startTime, LocalDateTime endTime, String state) {
        return this.baseMapper.countGroupByYearMonthType(userId, deptId, startTime, endTime, state);
    }


    private TodoItem addTodoItem(TodoItemPayload payload) {

        TodoItem item = new TodoItem();
        item.setTodoId(payload.getBusinessId());
        item.setBusinessCode(payload.getBusinessCode());
        if (StrUtil.isNotEmpty(payload.getBusinessId())) {
            item.setBusinessId(payload.getBusinessId());
        } else {
            item.setBusinessId(payload.getBusinessCode());
        }
        item.setSource(payload.getSource());
        item.setAppId(payload.getAppId());
        item.setFormId(payload.getFormId());
        item.setDataId(payload.getDataId());
        item.setModular(payload.getModular());
        item.setType(payload.getType());
        item.setTitle(payload.getTitle());
        item.setBusinessContent(payload.getContent());
        item.setBusinessStatus(payload.getCurrentStatus());
        item.setState(getStateFromBusinessStatus(TodoItem.SOURCE_ICD, payload.getCurrentStatus()));
        item.setCallbackLink(payload.getCallbackLink());
        if (StrUtil.isNotEmpty(item.getCallbackLink())) {
            String callbackLink = item.getCallbackLink().trim();
            if (!(callbackLink.startsWith(Constants.HTTP) || callbackLink.startsWith(Constants.HTTPS))) {
                item.setCallbackLink(getLinkUri(item.getAppId(), item.getFormId(), item.getBusinessId(), item.getSource()));
            }
        }
        item.setSource(payload.getSource());
        item.setCreateTime(LocalDateTime.now());
        item.setSubmitTime(LocalDateTime.now());
        item.setUserId(payload.getInitiatorUserId());
        item.setYear(item.getCreateTime().getYear());
        item.setMonth(item.getCreateTime().getMonthValue());
        SysUser sysUser = sysUserService.getById(item.getUserId());
        if (null != sysUser) {
            item.setDeptId(sysUser.getDeptId());
        }
        if (null != payload.getCreateList()) {
            StringBuilder processorIds = new StringBuilder();
            for (TodoItemDetailPayload detailPayload : payload.getCreateList()) {
                if (processorIds.length() > 0) {
                    processorIds.append(",").append(detailPayload.getRecipientUserId());
                } else {
                    processorIds.append(detailPayload.getRecipientUserId());
                }
            }
            item.setCurrentProcessorIds(processorIds.toString());
        }
        save(item);
        return item;
    }

    private void updateTodoItem(TodoItemPayload itemPayload, TodoItem currentItem) {
        LambdaUpdateWrapper<TodoItem> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(TodoItem::getTodoId, currentItem.getTodoId());
        updateWrapper.set(TodoItem::getModular, itemPayload.getModular());
        updateWrapper.set(TodoItem::getUpdateTime, LocalDateTime.now());
        updateWrapper.set(TodoItem::getBusinessStatus, itemPayload.getCurrentStatus());
        updateWrapper.set(TodoItem::getState, getStateFromBusinessStatus(TodoItem.SOURCE_ICD, itemPayload.getCurrentStatus()));
        if (StrUtil.isNotEmpty(itemPayload.getType())) {
            updateWrapper.set(TodoItem::getType, itemPayload.getType());
        }
        if (null != itemPayload.getCreateList() && !itemPayload.getCreateList().isEmpty()) {
            StringBuilder processorIds = new StringBuilder();
            for (TodoItemDetailPayload detailPayload : itemPayload.getCreateList()) {
                if (processorIds.length() > 0) {
                    processorIds.append(",").append(detailPayload.getRecipientUserId());
                } else {
                    processorIds.append(detailPayload.getRecipientUserId());
                }
            }
            updateWrapper.set(TodoItem::getCurrentProcessorIds, processorIds.toString());
        }
        update(updateWrapper);
    }


    private AjaxResult checkPayload(TodoItemPayload payload) {
        if (StringUtils.isEmpty(payload.getModular())) {
            return AjaxResult.error("所属模块[modular]不能为空");
        }
        if (StringUtils.isEmpty(payload.getBusinessCode())) {
            return AjaxResult.error("唯一编号[businessCode]不能为空");
        }
        return AjaxResult.success();
    }

    /**
     * 将业务系统的工单状态转为统一待办的工单状态
     */
    private String getStateFromBusinessStatus(String source, String businessStatus) {
        if (StrUtil.isNotEmpty(source) || source.equals(TodoItem.SOURCE_ICD)) {
            switch (businessStatus) {
                case TodoItem.BUSINESS_STATUS_INVALID:
                    return TodoItem.STATE_INVALID;
                case TodoItem.BUSINESS_STATUS_SUCCESS:
                    return TodoItem.STATE_SUCCESS;
                case TodoItem.BUSINESS_STATUS_FAILURE:
                    return TodoItem.STATE_FAILURE;
                default:
                    return TodoItem.STATE_ACTIVE;
            }
        } else {
            return businessStatus;
        }
    }

    @Override
    public List<TodoItemVo> getState() {
        return this.baseMapper.getState();
    }

    @Override
    public void export(List<JSONObject> list, HttpServletResponse response) {
        // 1.获取ExcelWriter对象
        ExcelWriter writer = ExcelUtil.getBigWriter();
        // 2.写出表头
        // 自定义标题别名
        list.get(0).forEach(ItemList -> {
            String name = String.valueOf(ItemList.getKey());

            writer.addHeaderAlias(name, name);
        });

        // ...
        // 3.定义表头单元格样式(可选)
        StyleSet style = writer.getStyleSet();
        CellStyle headCellStyle = style.getHeadCellStyle();
        // 自动换行
        headCellStyle.setWrapText(true);
        // 水平居中
        headCellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 上下居中
        headCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置表头字体大小
        Font headFont = writer.createFont();
        headFont.setFontName("宋体");
        // 设置字体大小为15磅
        headFont.setFontHeightInPoints((short) 15);
        headCellStyle.setFont(headFont);
        // 4.定义内容单元格样式(可选)
        CellStyle cellStyle = style.getCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        // 设置字体大小
        Font font = writer.createFont();
        font.setFontName("宋体");
        cellStyle.setFont(font);
        // 5.其他设置(可选)
        // 只写出设置别名的属性
        writer.setOnlyAlias(true);
        // 冻结行
        writer.setFreezePane(1);
        // 6.写入数据 设置列宽
        writer.write(list);
        writer.autoSizeColumnAll();
        // 7.开启筛选(可选)
        // 这里数值为列数
        String rangeString = CellReference.convertNumToColString(3) + "1";
        CellRangeAddress filterRange = CellRangeAddress.valueOf("A1:" + rangeString);
        writer.getSheet().setAutoFilter(filterRange);
        // 8.导出
        response.setCharacterEncoding(CharsetUtil.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename=Item.xlsx");
        response.setContentType("application/vnd.ms-excel;" + CharsetUtil.UTF_8);
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            writer.flush(outputStream);
            writer.close();
        } catch (Exception e) {
            log.warn("批量导出出错：{}", e.getMessage());
        }
    }

}
