package com.inspur.agriculture.input.service.demand.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inspur.agriculture.input.domain.demand.DemandInputSummary;
import com.inspur.agriculture.input.domain.oauth.PubRegion;
import com.inspur.agriculture.input.dto.demand.DemandInputSummaryDTO;
import com.inspur.agriculture.input.dto.demand.DemandInputSummaryQueryDTO;
import com.inspur.agriculture.input.mapper.demand.DemandInputSummaryMapper;
import com.inspur.agriculture.input.mapper.oauth.PubRegionMapper;
import com.inspur.agriculture.input.service.demand.IDemandInputSummaryItemService;
import com.inspur.agriculture.input.service.demand.IDemandInputSummaryService;
import com.inspur.agriculture.input.vo.demand.DemandInputSummaryVO;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.DateUtils;
import com.inspur.farmland.domain.FarmerInfo;
import com.inspur.farmland.service.IFarmerInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 农资需求汇总 Service实现
 *
 * @author inspur
 * @date 2025-12-10
 */
@Service
public class DemandInputSummaryServiceImpl implements IDemandInputSummaryService {

    @Autowired
    private DemandInputSummaryMapper demandInputSummaryMapper;

    @Autowired
    private PubRegionMapper regionMapper;

    @Autowired
    private IFarmerInfoService farmerInfoService;

    @Autowired
    private IDemandInputSummaryItemService demandInputSummaryItemService;

    @Override
    public List<DemandInputSummaryVO> getDemandInputSummaryList(DemandInputSummaryQueryDTO queryDTO) {
        return demandInputSummaryMapper.selectDemandInputSummaryList(queryDTO);
    }

    @Override
    public DemandInputSummaryVO getDemandInputSummaryById(String id) {
        return demandInputSummaryMapper.selectDemandInputSummaryById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addDemandInputSummary(DemandInputSummaryDTO dto) {

        //先搞区划
        PubRegion region = regionMapper.selectByRegionCode(dto.getSourceCode());
        String sourceName = region.getName();
        String targetCode = region.getParentCode();
        PubRegion fatherRegion = regionMapper.selectByRegionCode(targetCode);
        String targetName = fatherRegion.getName();
        dto.setSourceName(sourceName);
        dto.setTargetName(targetName);
        dto.setTargetCode(targetCode);

        //处理分发需求单时的下级数量，

        //level0 kebele 拿所属农民数量
        if("0".equals(dto.getLevel())){
            FarmerInfo farmerInfo = new FarmerInfo();
            farmerInfo.setKebeleCode(dto.getSourceCode());
            List<FarmerInfo> list = farmerInfoService.selectFarmerInfoList(farmerInfo);
            dto.setSubQuantity(list.size());
        }else{
            //level1 woreda 拿所属kebele数量
            //level2 zone 拿所属woreda数量
            //level3 region 拿所属zone数量
            dto.setSubQuantity(regionMapper.getCountByParentCode(dto.getSourceCode()));
        }
        // DTO转Entity
        DemandInputSummary summary = new DemandInputSummary();
        BeanUtils.copyProperties(dto, summary);

        // 设置默认值
        if (summary.getStatus() == null || summary.getStatus().isEmpty()) {
            summary.setStatus("0"); // 默认待审核
        }
        summary.setCreateTime(DateUtils.getNowDate());

        // 如果没有设置年份，使用当前年份
        if (summary.getYear() == null || summary.getYear().isEmpty()) {
            summary.setYear(String.valueOf(java.time.Year.now().getValue()));
        }

        // 插入数据
        return demandInputSummaryMapper.insert(summary);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateDemandInputSummary(DemandInputSummaryDTO dto) {
        if (dto.getId() == null || dto.getId().isEmpty()) {
            throw new ServiceException("主键ID不能为空");
        }

        // 查询原记录
        DemandInputSummary summary = demandInputSummaryMapper.selectById(dto.getId());
        if (summary == null) {
            throw new ServiceException("农资需求汇总记录不存在");
        }

        String status = dto.getStatus();

        if(status.equals("2")){
             demandInputSummaryItemService.updateDemandItemStatus(dto.getId());
        }
        if (status.equals("3")){
            //状态为拒绝，删除关联的子表数据
            demandInputSummaryItemService.deleteDeandInputItemBySummaryId(dto.getId());
        }

        // DTO转Entity
        BeanUtils.copyProperties(dto, summary);

        // 更新数据
        return demandInputSummaryMapper.updateById(summary);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteDemandInputSummary(String id) {
        // 查询原记录
        DemandInputSummary summary = demandInputSummaryMapper.selectById(id);
        if (summary == null) {
            throw new ServiceException("农资需求汇总记录不存在");
        }

        // 物理删除
        return demandInputSummaryMapper.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int batchDeleteDemandInputSummary(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("删除的ID列表不能为空");
        }

        // 批量物理删除
        QueryWrapper<DemandInputSummary> wrapper = new QueryWrapper<>();
        wrapper.in("id", ids);
        return demandInputSummaryMapper.delete(wrapper);
    }
}
