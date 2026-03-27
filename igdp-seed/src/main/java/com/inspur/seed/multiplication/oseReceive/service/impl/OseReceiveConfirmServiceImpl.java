package com.inspur.seed.multiplication.oseReceive.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.exception.ServiceException;
import com.inspur.seed.breeding.seedDistribution.domain.entity.BreedSeedDistributeDetail;
import com.inspur.seed.breeding.seedDistribution.domain.entity.BreedSeedDistributeMain;
import com.inspur.seed.breeding.breederSeed.domain.entity.BreedSeedProduce;
import com.inspur.seed.multiplication.oseReceive.domain.entity.OseBreedSeedReceiveConfirm;
import com.inspur.seed.Institution.ose.domain.entity.OseInfo;
import com.inspur.seed.multiplication.oseReceive.domain.dto.OseReceiveConfirmDTO;
import com.inspur.seed.multiplication.oseReceive.domain.dto.OseReceiveConfirmQueryDTO;
import com.inspur.seed.breeding.seedDistribution.mapper.BreedSeedDistributeDetailMapper;
import com.inspur.seed.breeding.seedDistribution.mapper.BreedSeedDistributeMapper;
import com.inspur.seed.domain.basic.BasicSeedProduceResult;
import com.inspur.seed.domain.prebasic.PrebasicSeedProduceResult;
import com.inspur.seed.mapper.prebasic.PrebasicSeedProduceResultMapper;
import com.inspur.seed.mapper.basic.BasicSeedProduceResultMapper;
import com.inspur.seed.domain.Organization;
import com.inspur.seed.mapper.OrganizationMapper;
import com.inspur.seed.breeding.breederSeed.mapper.BreedSeedProduceMapper;
import com.inspur.seed.breeding.breederSeed.mapper.BreedSeedProduceResultMapper;
import com.inspur.seed.breeding.breederSeed.domain.vo.BreedSeedProduceResultVO;
import com.inspur.seed.mapper.oauth.PubOrganMapper;
import com.inspur.seed.Institution.ose.mapper.OseInfoMapper;
import com.inspur.seed.multiplication.oseReceive.mapper.OseReceiveConfirmMapper;
import com.inspur.seed.multiplication.oseReceive.service.IOseReceiveConfirmService;
import com.inspur.seed.multiplication.oseReceive.domain.vo.OseReceiveConfirmVO;


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
    private BreedSeedProduceMapper breedSeedProduceMapper;

    @Autowired
    private PrebasicSeedProduceResultMapper prebasicSeedProduceResultMapper;

    @Autowired
    private BasicSeedProduceResultMapper basicSeedProduceResultMapper;

    @Autowired
    private PubOrganMapper pubOrganMapper;

    @Autowired
    private BreedSeedProduceResultMapper breedSeedProduceResultMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

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

        // 根据关键字过滤
        if (queryDTO.getSearchKeyword() != null && !queryDTO.getSearchKeyword().isEmpty()) {
            String keyword = queryDTO.getSearchKeyword();
            List<String> distributeIds = getDistributeIdsByKeyword(keyword);
            List<String> oseIds = getOseIdsByKeyword(keyword);

            queryWrapper.and(w -> {
                boolean hasCondition = false;
                if (!distributeIds.isEmpty()) {
                    w.in(OseBreedSeedReceiveConfirm::getDistributeId, distributeIds);
                    hasCondition = true;
                }
                if (!oseIds.isEmpty()) {
                    if (hasCondition) {
                        w.or().in(OseBreedSeedReceiveConfirm::getOseId, oseIds);
                    } else {
                        w.in(OseBreedSeedReceiveConfirm::getOseId, oseIds);
                        hasCondition = true;
                    }
                }
                // 如果搜索框有值但没匹到任何ID，应返回空，所以强制一个无法匹配的条件
                if (!hasCondition) {
                    w.eq(OseBreedSeedReceiveConfirm::getReceiveConfirmId, "NONE_MATCHED");
                }
            });
        }

        // 按创建时间倒序排列
        queryWrapper.orderByDesc(OseBreedSeedReceiveConfirm::getCreateTime);

        // 执行查询
        List<OseBreedSeedReceiveConfirm> confirmList = this.list(queryWrapper);

        // 转换为VO对象
        return confirmList.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public OseReceiveConfirmVO getReceiveConfirmDetail(String receiveConfirmId) {
        // 根据ID查询接收确认记录
        OseBreedSeedReceiveConfirm confirm = receiveConfirmMapper.selectById(receiveConfirmId);
        if (confirm == null) {
            throw new ServiceException("接收确认记录不存在");
        }

        // 转换为VO对象并返回
        return convertToVO(confirm);
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
     * 根据关键字获取分发ID列表
     */
    private List<String> getDistributeIdsByKeyword(String keyword) {
        LambdaQueryWrapper<BreedSeedDistributeDetail> detailQuery = new LambdaQueryWrapper<>();
        detailQuery.and(w -> w.like(BreedSeedDistributeDetail::getProduceBatchId, keyword)
                .or().like(BreedSeedDistributeDetail::getVarietyName, keyword)
                .or().like(BreedSeedDistributeDetail::getProduceBatchName, keyword));
        List<BreedSeedDistributeDetail> detailList = distributeDetailMapper.selectList(detailQuery);
        return detailList.stream()
                .map(BreedSeedDistributeDetail::getDistributeId)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 根据关键字获取OSE ID列表
     */
    private List<String> getOseIdsByKeyword(String keyword) {
        LambdaQueryWrapper<OseInfo> oseQuery = new LambdaQueryWrapper<>();
        oseQuery.like(OseInfo::getOseName, keyword);
        List<OseInfo> oseList = oseInfoMapper.selectList(oseQuery);
        return oseList.stream()
                .map(OseInfo::getOseId)
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

        // 设置组织名称（优先从organization表查，兼容旧数据从ose_info查）
        if (entity.getOseId() != null && !entity.getOseId().isEmpty()) {
            Organization org = organizationMapper.selectById(entity.getOseId());
            if (org != null) {
                vo.setOseName(org.getOrgName());
            } else {
                OseInfo oseInfo = oseInfoMapper.selectById(entity.getOseId());
                if (oseInfo != null) {
                    vo.setOseName(oseInfo.getOseName());
                }
            }
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
            item.setParentalSeedSource(detail.getParentalSeedSource()); // 从分发明细获取parentalSeedSource
            item.setVarietyName(detail.getVarietyName());
            item.setCropType(detail.getCropType());
            item.setDistributeQuantity(detail.getDistributeQuantity());
            item.setProduceBatchName(detail.getProduceBatchName());

            // 获取种子类型 (toSeedLevel)
            String produceBatchId = detail.getProduceBatchId();
            if (produceBatchId != null && !produceBatchId.isEmpty()) {
                PrebasicSeedProduceResult produce = prebasicSeedProduceResultMapper.selectResultByProduceBatchId(produceBatchId);
                if (produce != null) {
                    item.setSeedType(produce.getToSeedLevel());
                    item.setCropType(produce.getCropType());
                } else {
                    BasicSeedProduceResult basicProduce = basicSeedProduceResultMapper.selectResultByProduceBatchId(produceBatchId);
                    if (basicProduce != null) {
                        item.setSeedType(basicProduce.getToSeedLevel());
                        item.setCropType(basicProduce.getCropType());
                    } else {
                        // Breeder级别：查询育种家种子生产结果
                        BreedSeedProduceResultVO breedResult = breedSeedProduceResultMapper.getResultByProduceBatchId(produceBatchId);
                        if (breedResult != null) {
                            item.setSeedType(breedResult.getToSeedLevel());
                            item.setCropType(breedResult.getCropType());
                        }
                    }
                }
            }

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

        // 更新分发主表状态为"已接收"
        if (confirm.getDistributeId() != null) {
            UpdateWrapper<BreedSeedDistributeMain> distributeUpdateWrapper = new UpdateWrapper<>();
            distributeUpdateWrapper.eq("distribute_id", confirm.getDistributeId());
            distributeUpdateWrapper.set("distribute_status", "Received");
            distributeUpdateWrapper.set("update_time", new Date());
            distributeMainMapper.update(null, distributeUpdateWrapper);
        }

        return receiveConfirmMapper.selectReceiveConfirmById(receiveConfirmId);
    }
}
