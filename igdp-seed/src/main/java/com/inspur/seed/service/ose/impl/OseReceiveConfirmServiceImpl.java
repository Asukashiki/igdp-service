package com.inspur.seed.service.ose.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.seed.domain.breed.BreedSeedDistributeDetail;
import com.inspur.seed.domain.breed.BreedSeedDistributeMain;
import com.inspur.seed.domain.ose.OseBreedSeedReceiveConfirm;
import com.inspur.seed.domain.ose.OseInfo;
import com.inspur.seed.dto.ose.OseReceiveConfirmDTO;
import com.inspur.seed.dto.ose.OseReceiveConfirmQueryDTO;
import com.inspur.seed.mapper.breed.BreedSeedDistributeDetailMapper;
import com.inspur.seed.mapper.breed.BreedSeedDistributeMapper;
import com.inspur.seed.mapper.oauth.PubOrganMapper;
import com.inspur.seed.mapper.ose.OseInfoMapper;
import com.inspur.seed.mapper.ose.OseReceiveConfirmMapper;
import com.inspur.seed.service.ose.IOseReceiveConfirmService;
import com.inspur.seed.vo.ose.OseReceiveConfirmVO;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * OSE接收确认Service实现
 *
 * @author igdp
 */
@Service
public class OseReceiveConfirmServiceImpl extends ServiceImpl<OseReceiveConfirmMapper, OseBreedSeedReceiveConfirm> implements IOseReceiveConfirmService {

    @Autowired
    private OseReceiveConfirmMapper receiveConfirmMapper;

    @Autowired
    private OseInfoMapper oseInfoMapper;

    @Autowired
    private BreedSeedDistributeMapper distributeMainMapper;

    @Autowired
    private BreedSeedDistributeDetailMapper distributeDetailMapper;

    @Autowired
    private PubOrganMapper pubOrganMapper;

    @Override
    public List<OseReceiveConfirmVO> getReceiveConfirmList(OseReceiveConfirmQueryDTO queryDTO) {
        // 构建查询条件
        LambdaQueryWrapper<OseBreedSeedReceiveConfirm> queryWrapper = new LambdaQueryWrapper<>();

        // 根据生产批次ID过滤
        if (queryDTO.getProduceBatchId() != null && !queryDTO.getProduceBatchId().isEmpty()) {
            // 需要关联分发明细表进行过滤
            List<String> distributeIds = getDistributeIdsByProduceBatchId(queryDTO.getProduceBatchId());
            if (!distributeIds.isEmpty()) {
                queryWrapper.in(OseBreedSeedReceiveConfirm::getDistributeId, distributeIds);
            } else {
                // 如果没有匹配的分发记录，返回空列表
                return new ArrayList<>();
            }
        }

        // 根据作物类型过滤
        if (queryDTO.getCropType() != null && !queryDTO.getCropType().isEmpty()) {
            List<String> distributeIds = getDistributeIdsByCropType(queryDTO.getCropType());
            if (!distributeIds.isEmpty()) {
                queryWrapper.in(OseBreedSeedReceiveConfirm::getDistributeId, distributeIds);
            } else {
                return new ArrayList<>();
            }
        }

        // 根据品种名称过滤
        if (queryDTO.getVarietyName() != null && !queryDTO.getVarietyName().isEmpty()) {
            List<String> distributeIds = getDistributeIdsByVarietyName(queryDTO.getVarietyName());
            if (!distributeIds.isEmpty()) {
                queryWrapper.in(OseBreedSeedReceiveConfirm::getDistributeId, distributeIds);
            } else {
                return new ArrayList<>();
            }
        }

        // 根据开始时间过滤
        if (queryDTO.getStartTime() != null) {
            queryWrapper.ge(OseBreedSeedReceiveConfirm::getCreateTime, queryDTO.getStartTime());
        }

        // 根据结束时间过滤
        if (queryDTO.getEndTime() != null) {
            queryWrapper.le(OseBreedSeedReceiveConfirm::getCreateTime, queryDTO.getEndTime());
        }

        // 根据接收状态过滤
        if (queryDTO.getReceiveStatus() != null && !queryDTO.getReceiveStatus().isEmpty()) {
            queryWrapper.eq(OseBreedSeedReceiveConfirm::getReceiveStatus, queryDTO.getReceiveStatus());
        }

        // 根据OSE ID过滤
        if (queryDTO.getOseId() != null && !queryDTO.getOseId().isEmpty()) {
            queryWrapper.eq(OseBreedSeedReceiveConfirm::getOseId, queryDTO.getOseId());
        }

        // 按创建时间倒序排列
        queryWrapper.orderByDesc(OseBreedSeedReceiveConfirm::getCreateTime);

        // 执行查询
        List<OseBreedSeedReceiveConfirm> confirmList = this.list(queryWrapper);

        // 转换为VO对象
        return confirmList.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    /**
     * 根据生产批次ID获取分发ID列表
     */
    private List<String> getDistributeIdsByProduceBatchId(String produceBatchId) {
        LambdaQueryWrapper<BreedSeedDistributeDetail> detailQuery = new LambdaQueryWrapper<>();
        detailQuery.eq(BreedSeedDistributeDetail::getProduceBatchId, produceBatchId);
        List<BreedSeedDistributeDetail> detailList = distributeDetailMapper.selectList(detailQuery);
        return detailList.stream()
                .map(BreedSeedDistributeDetail::getDistributeId)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 根据作物类型获取分发ID列表
     */
    private List<String> getDistributeIdsByCropType(String cropType) {
        LambdaQueryWrapper<BreedSeedDistributeDetail> detailQuery = new LambdaQueryWrapper<>();
        detailQuery.eq(BreedSeedDistributeDetail::getCropType, cropType);
        List<BreedSeedDistributeDetail> detailList = distributeDetailMapper.selectList(detailQuery);
        return detailList.stream()
                .map(BreedSeedDistributeDetail::getDistributeId)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 根据品种名称获取分发ID列表
     */
    private List<String> getDistributeIdsByVarietyName(String varietyName) {
        LambdaQueryWrapper<BreedSeedDistributeDetail> detailQuery = new LambdaQueryWrapper<>();
        detailQuery.like(BreedSeedDistributeDetail::getVarietyName, varietyName);
        List<BreedSeedDistributeDetail> detailList = distributeDetailMapper.selectList(detailQuery);
        return detailList.stream()
                .map(BreedSeedDistributeDetail::getDistributeId)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 将实体对象转换为VO对象
     */
    private OseReceiveConfirmVO convertToVO(OseBreedSeedReceiveConfirm entity) {
        OseReceiveConfirmVO vo = new OseReceiveConfirmVO();
        vo.setReceiveConfirmId(entity.getReceiveConfirmId());
        vo.setDistributeId(entity.getDistributeId());
        vo.setOseId(entity.getOseId());
        vo.setConfirmTime(entity.getConfirmTime());
        vo.setConfirmPeople(entity.getConfirmPeople());
        vo.setReceiveStatus(entity.getReceiveStatus());
        vo.setRemark(entity.getRemark());
        vo.setCreateTime(entity.getCreateTime());
        vo.setUpdateTime(entity.getUpdateTime());

        // 设置OSE名称
        OseInfo oseInfo = oseInfoMapper.selectById(entity.getOseId());
        if (oseInfo != null) {
            vo.setOseName(oseInfo.getOseName());
        }

        // 设置分发明细
        OseReceiveConfirmVO.DistributeDetailWrapper detailWrapper = new OseReceiveConfirmVO.DistributeDetailWrapper();

        // 查询分发主表信息
        BreedSeedDistributeMain main = distributeMainMapper.selectById(entity.getDistributeId());
        if (main != null) {
            detailWrapper.setTotalDistributeQuantity(main.getTotalDistributeQuantity());
        }

        // 查询分发明细列表
        LambdaQueryWrapper<BreedSeedDistributeDetail> detailQuery = new LambdaQueryWrapper<>();
        detailQuery.eq(BreedSeedDistributeDetail::getDistributeId, entity.getDistributeId());
        detailQuery.orderByAsc(BreedSeedDistributeDetail::getCreateTime);
        List<BreedSeedDistributeDetail> detailList = distributeDetailMapper.selectList(detailQuery);

        List<OseReceiveConfirmVO.DetailItem> voDetailList = detailList.stream().map(detail -> {
            OseReceiveConfirmVO.DetailItem item = new OseReceiveConfirmVO.DetailItem();
            item.setBreedSeedProduceBatchId(detail.getProduceBatchId());
            item.setVarietyName(detail.getVarietyName());
            item.setCropType(detail.getCropType());
            item.setDistributeQuantity(detail.getDistributeQuantity());
            return item;
        }).collect(Collectors.toList());

        detailWrapper.setDetailList(voDetailList);
        vo.setDistributeDetail(detailWrapper);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OseReceiveConfirmVO confirmReceive(String receiveConfirmId, OseReceiveConfirmDTO dto) {
        // 查询接收确认记录
        OseBreedSeedReceiveConfirm confirm = receiveConfirmMapper.selectById(receiveConfirmId);
        if (confirm == null) {
            throw new ServiceException("接收确认记录不存在");
        }

        // 验证状态
        if ("CONFIRMED".equals(confirm.getReceiveStatus())) {
            throw new ServiceException("该记录已确认，不能重复确认");
        }

        // 更新确认信息
        UpdateWrapper<OseBreedSeedReceiveConfirm> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("receive_confirm_id", receiveConfirmId);
        updateWrapper.set("confirm_time", new Date());
        updateWrapper.set("confirm_people", dto.getConfirmPeople());
        updateWrapper.set("receive_status", "CONFIRMED");
        updateWrapper.set("remark", dto.getRemark());
        updateWrapper.set("update_time", new Date());

        receiveConfirmMapper.update(null, updateWrapper);

        return receiveConfirmMapper.selectReceiveConfirmById(receiveConfirmId);
    }
}
