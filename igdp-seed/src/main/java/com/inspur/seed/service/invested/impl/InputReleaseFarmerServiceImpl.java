package com.inspur.seed.service.invested.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.uuid.IdUtils;
import com.inspur.seed.domain.invested.InputReleaseFarmerDetail;
import com.inspur.seed.domain.invested.InputReleaseFarmerMain;
import com.inspur.seed.dto.invested.InputReleaseFarmerDTO;
import com.inspur.seed.dto.invested.InputReleaseFarmerDetailDTO;
import com.inspur.seed.mapper.invested.InputReleaseFarmerDetailMapper;
import com.inspur.seed.mapper.invested.InputReleaseFarmerMainMapper;
import com.inspur.seed.service.invested.IInputReleaseFarmerService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 农民分发Service实现
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@Service
public class InputReleaseFarmerServiceImpl extends ServiceImpl<InputReleaseFarmerMainMapper, InputReleaseFarmerMain>
        implements IInputReleaseFarmerService {

    @Resource
    private InputReleaseFarmerDetailMapper detailMapper;

    @Override
    public List<InputReleaseFarmerMain> queryReleaseList(String woredaName, String farmerName, String farmerId,
                                                          Integer year, String receiveStatus,
                                                          LocalDate startTime, LocalDate endTime) {
        LambdaQueryWrapper<InputReleaseFarmerMain> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotEmpty(woredaName)) {
            wrapper.like(InputReleaseFarmerMain::getReleaseOrg, woredaName);
        }
        if (StringUtils.isNotEmpty(farmerName)) {
            wrapper.like(InputReleaseFarmerMain::getFarmerName, farmerName);
        }
        if (StringUtils.isNotEmpty(farmerId)) {
            wrapper.eq(InputReleaseFarmerMain::getFarmerId, farmerId);
        }
        if (year != null) {
            wrapper.eq(InputReleaseFarmerMain::getReleaseYear, year);
        }
        if (StringUtils.isNotEmpty(receiveStatus)) {
            wrapper.eq(InputReleaseFarmerMain::getReceiveStatus, receiveStatus);
        }
        if (startTime != null) {
            wrapper.ge(InputReleaseFarmerMain::getReleaseDate, LocalDateTime.of(startTime, LocalTime.MIN));
        }
        if (endTime != null) {
            wrapper.le(InputReleaseFarmerMain::getReleaseDate, LocalDateTime.of(endTime, LocalTime.MAX));
        }

        wrapper.orderByDesc(InputReleaseFarmerMain::getReleaseDate);

        return list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> addRelease(InputReleaseFarmerDTO dto) {
        // 生成分发单编号：FRL+yyyyMMdd+6位随机码
        String releaseId = "FRL" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + IdUtils.fastSimpleUUID().substring(0, 6).toUpperCase();

        // 保存主表
        InputReleaseFarmerMain main = new InputReleaseFarmerMain();
        BeanUtils.copyProperties(dto, main);
        // 将LocalDate转换为LocalDateTime
        if (dto.getReleaseDate() != null) {
            main.setReleaseDate(LocalDateTime.of(dto.getReleaseDate(), LocalTime.MIN));
        }
        main.setId(IdUtils.fastSimpleUUID());
        main.setReleaseId(releaseId);
        main.setOperateBy("admin"); // TODO: 从登录用户获取
        main.setOperateTime(LocalDateTime.now());
        main.setCreateTime(LocalDateTime.now());

        save(main);

        // 保存明细
        if (dto.getDetails() != null && !dto.getDetails().isEmpty()) {
            saveDetails(releaseId, dto.getDetails());
        }

        // 返回结果
        Map<String, String> result = new HashMap<>();
        result.put("id", main.getId());
        result.put("releaseId", releaseId);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editRelease(InputReleaseFarmerDTO dto) {
        if (StringUtils.isEmpty(dto.getId())) {
            throw new ServiceException("分发单ID不能为空");
        }

        // 更新主表
        InputReleaseFarmerMain main = getById(dto.getId());
        if (main == null) {
            throw new ServiceException("分发单不存在");
        }

        BeanUtils.copyProperties(dto, main, "id", "releaseId", "createTime", "createBy", "releaseDate");
        // 将LocalDate转换为LocalDateTime
        if (dto.getReleaseDate() != null) {
            main.setReleaseDate(LocalDateTime.of(dto.getReleaseDate(), LocalTime.MIN));
        }
        main.setOperateBy("admin"); // TODO: 从登录用户获取
        main.setOperateTime(LocalDateTime.now());
        main.setUpdateTime(LocalDateTime.now());

        updateById(main);

        // 删除旧明细
        LambdaQueryWrapper<InputReleaseFarmerDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InputReleaseFarmerDetail::getReleaseId, main.getReleaseId());
        detailMapper.delete(wrapper);

        // 保存新明细
        if (dto.getDetails() != null && !dto.getDetails().isEmpty()) {
            saveDetails(main.getReleaseId(), dto.getDetails());
        }

        return true;
    }

    @Override
    public Map<String, Object> queryReleaseDetail(String id) {
        InputReleaseFarmerMain main = getById(id);
        if (main == null) {
            throw new ServiceException("分发单不存在");
        }

        // 查询明细
        LambdaQueryWrapper<InputReleaseFarmerDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InputReleaseFarmerDetail::getReleaseId, main.getReleaseId());
        wrapper.orderByAsc(InputReleaseFarmerDetail::getCreateTime);
        List<InputReleaseFarmerDetail> details = detailMapper.selectList(wrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("main", main);
        result.put("details", details);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeRelease(List<String> ids) {
        for (String id : ids) {
            InputReleaseFarmerMain main = getById(id);
            if (main != null) {
                // 删除主表
                removeById(id);

                // 删除明细
                LambdaQueryWrapper<InputReleaseFarmerDetail> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(InputReleaseFarmerDetail::getReleaseId, main.getReleaseId());
                detailMapper.delete(wrapper);
            }
        }
        return true;
    }

    /**
     * 保存分发明细
     */
    private void saveDetails(String releaseId, List<InputReleaseFarmerDetailDTO> detailDTOs) {
        for (InputReleaseFarmerDetailDTO detailDTO : detailDTOs) {
            String detailId = "FDT" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                    + IdUtils.fastSimpleUUID().substring(0, 6).toUpperCase();

            InputReleaseFarmerDetail detail = new InputReleaseFarmerDetail();
            BeanUtils.copyProperties(detailDTO, detail);
            detail.setId(IdUtils.fastSimpleUUID());
            detail.setReleaseDetailId(detailId);
            detail.setReleaseId(releaseId);
            detail.setCreateTime(LocalDateTime.now());

            detailMapper.insert(detail);
        }
    }
}