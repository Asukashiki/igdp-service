package com.inspur.seed.service.invested.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.StringUtils;
import com.inspur.seed.domain.invested.InputReceiveWoreda;
import com.inspur.seed.domain.invested.InputReleaseDetail;
import com.inspur.seed.domain.invested.InputReleaseMain;
import com.inspur.seed.domain.vo.InputCirculationSummaryVO;
import com.inspur.seed.mapper.invested.InputReceiveWoredaMapper;
import com.inspur.seed.mapper.invested.InputReleaseDetailMapper;
import com.inspur.seed.mapper.invested.InputReleaseMainMapper;
import com.inspur.seed.service.invested.IInputReceiveWoredaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Woreda接收确认Service实现
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@Service
public class InputReceiveWoredaServiceImpl extends ServiceImpl<InputReceiveWoredaMapper, InputReceiveWoreda>
        implements IInputReceiveWoredaService {

    @Resource
    private InputReleaseDetailMapper detailMapper;
    @Resource
    private InputReleaseMainMapper inputReleaseMainMapper;

    @Override
    public List<InputReceiveWoreda> queryReceiveList(String woredaName, String receiveStatus,
                                                      LocalDate startTime, LocalDate endTime,String releaseName) {
        return queryReceiveList(woredaName, receiveStatus, startTime, endTime, releaseName, null);
    }

    @Override
    public List<InputReceiveWoreda> queryReceiveList(String woredaName, String receiveStatus,
                                                      LocalDate startTime, LocalDate endTime, String releaseName, String flag) {
        LambdaQueryWrapper<InputReceiveWoreda> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotEmpty(releaseName)){
            wrapper.like(InputReceiveWoreda::getReleaseName, releaseName);
        }
        if (StringUtils.isNotEmpty(woredaName)) {
            wrapper.like(InputReceiveWoreda::getTargetId, woredaName);
        }
        if (StringUtils.isNotEmpty(receiveStatus)) {
            wrapper.eq(InputReceiveWoreda::getReceiveStatus, receiveStatus);
        }
        if (startTime != null) {
            wrapper.ge(InputReceiveWoreda::getReleaseDate, LocalDateTime.of(startTime, LocalTime.MIN));
        }
        if (endTime != null) {
            wrapper.le(InputReceiveWoreda::getReleaseDate, LocalDateTime.of(endTime, LocalTime.MAX));
        }
        if (StringUtils.isNotEmpty(flag)) {
            wrapper.eq(InputReceiveWoreda::getFlag, flag);
        }

        wrapper.orderByDesc(InputReceiveWoreda::getReleaseDate);

        return list(wrapper);
    }

    @Override
    public List<InputCirculationSummaryVO> queryReceiveSummaryList(String woredaName, String receiveStatus,
                                                                   LocalDate startTime, LocalDate endTime, String releaseName) {
        List<InputReceiveWoreda> mainList = queryReceiveList(woredaName, receiveStatus, startTime, endTime, releaseName);
        Map<String, InputCirculationSummaryVO> summaryMap = new LinkedHashMap<>();
        for (InputReceiveWoreda receive : mainList) {
            LambdaQueryWrapper<InputReleaseDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(InputReleaseDetail::getReleaseId, receive.getReleaseId());
            List<InputReleaseDetail> details = detailMapper.selectList(wrapper);
            mergeSummary(summaryMap, details);
        }
        return new ArrayList<>(summaryMap.values());
    }

    @Override
    public Map<String, Object> queryReceiveDetail(String id) {
        InputReceiveWoreda receive = getById(id);
        if (receive == null) {
            throw new ServiceException("接收确认记录不存在");
        }

        // 查询关联的分发明细
        LambdaQueryWrapper<InputReleaseDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InputReleaseDetail::getReleaseId, receive.getReleaseId());
        wrapper.orderByAsc(InputReleaseDetail::getCreateTime);
        List<InputReleaseDetail> details = detailMapper.selectList(wrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("main", receive);
        result.put("details", details);
        return result;
    }

    @Override
    public Map<String, Object> queryReceiveSummaryDetail(String id) {
        Map<String, Object> detail = queryReceiveDetail(id);
        Object detailsObj = detail.get("details");
        List<InputReleaseDetail> details = detailsObj instanceof List ? (List<InputReleaseDetail>) detailsObj : new ArrayList<>();
        Map<String, InputCirculationSummaryVO> summaryMap = new LinkedHashMap<>();
        mergeSummary(summaryMap, details);
        Map<String, Object> result = new HashMap<>();
        result.put("main", detail.get("main"));
        result.put("details", new ArrayList<>(summaryMap.values()));
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmReceive(String id, String confirmBy, String confirmOrg) {
        return confirmReceive(id, confirmBy, confirmOrg, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmReceive(String id, String confirmBy, String confirmOrg, String flag) {
        InputReceiveWoreda receive = getById(id);
        if (receive == null) {
            throw new ServiceException("接收确认记录不存在");
        }
        if (StringUtils.isNotEmpty(flag) && StringUtils.isNotEmpty(receive.getFlag()) && !flag.equals(receive.getFlag())) {
            throw new ServiceException("接收记录数据标识不匹配");
        }

        if ("已确认".equals(receive.getReceiveStatus())) {
            throw new ServiceException("该记录已确认，无需重复操作");
        }

        receive.setReceiveStatus("Confirmed");
        receive.setConfirmBy(confirmBy);
        receive.setConfirmOrg(confirmOrg);
        receive.setConfirmTime(LocalDateTime.now());
        if (StringUtils.isNotEmpty(flag)) {
            receive.setFlag(flag);
        }
        receive.setUpdateTime(LocalDateTime.now());

        // woreda
        LambdaQueryWrapper<InputReleaseMain> releaseWrapper = new LambdaQueryWrapper<>();
        releaseWrapper.eq(InputReleaseMain::getReleaseId, receive.getReleaseId());
        if (StringUtils.isNotEmpty(flag)) {
            releaseWrapper.eq(InputReleaseMain::getFlag, flag);
        }
        InputReleaseMain releaseMain = inputReleaseMainMapper.selectOne(releaseWrapper);
        if (releaseMain != null) {
            releaseMain.setStatus("completed");
            inputReleaseMainMapper.updateById(releaseMain);
        }

        return updateById(receive);
    }

    private void mergeSummary(Map<String, InputCirculationSummaryVO> summaryMap, List<InputReleaseDetail> details) {
        Set<String> countedKeys = new HashSet<>();
        for (InputReleaseDetail detail : details) {
            String key = buildSummaryKey(detail.getInputType(), detail.getInputCategory(), detail.getUnit());
            InputCirculationSummaryVO summary = summaryMap.computeIfAbsent(key, k -> {
                InputCirculationSummaryVO vo = new InputCirculationSummaryVO();
                vo.setInputType(detail.getInputType());
                vo.setInputCategory(detail.getInputCategory());
                vo.setUnit(detail.getUnit());
                return vo;
            });
            if (detail.getRequired() != null) {
                summary.setRequired(summary.getRequired().add(detail.getRequired()));
            }
            if (detail.getQuantity() != null) {
                summary.setQuantity(summary.getQuantity().add(detail.getQuantity()));
            }
            if (countedKeys.add(key)) {
                summary.setReleaseCount(summary.getReleaseCount() + 1);
            }
        }
    }

    private String buildSummaryKey(String inputType, String inputCategory, String unit) {
        return (inputType == null ? "" : inputType) + "|" +
                (inputCategory == null ? "" : inputCategory) + "|" +
                (unit == null ? "" : unit);
    }
}
