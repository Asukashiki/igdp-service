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
    public List<DemandInputSummaryVO> getDemandInputSummaryList1(DemandInputSummaryQueryDTO queryDTO) {
        return demandInputSummaryMapper.selectDemandInputSummaryList1(queryDTO);
    }
    @Override
    public DemandInputSummaryVO getDemandInputSummaryById(String id) {
        return demandInputSummaryMapper.selectDemandInputSummaryById(id);
    }
    @Override
    public List<DemandInputSummaryVO> getDemandInputSummaryList2(DemandInputSummaryQueryDTO queryDTO) {
        return demandInputSummaryMapper.selectDemandInputSummaryList2(queryDTO);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addDemandInputSummary(DemandInputSummaryDTO dto) {
        // 首先判断该年份是否存在
        QueryWrapper<DemandInputSummary> wrapper = new QueryWrapper<>();
        wrapper.eq("year", dto.getYear());
        wrapper.eq("source_code", dto.getSourceCode());
        List<DemandInputSummary> res = demandInputSummaryMapper.selectList(wrapper);
        if (res.size() > 0) {
            throw new ServiceException("Current year's record has already existed");
        }
        // 先搞区划
        PubRegion region = regionMapper.selectByRegionCode(dto.getSourceCode());
        String sourceName = region.getName();
        String targetCode = region.getParentCode();
        PubRegion fatherRegion = regionMapper.selectByRegionCode(targetCode);
        String targetName = fatherRegion.getName();
        dto.setSourceName(sourceName);
        dto.setTargetName(targetName);
        dto.setTargetCode(targetCode);

        // 处理分发需求单时的下级数量，

        // level0 kebele 拿所属农民数量
        if ("0".equals(dto.getLevel())) {
            FarmerInfo farmerInfo = new FarmerInfo();
            farmerInfo.setKebeleCode(dto.getSourceCode());
            List<FarmerInfo> list = farmerInfoService.selectFarmerInfoList(farmerInfo);
            dto.setSubQuantity(list.size());
        } else {
            // level1 woreda 拿所属kebele数量
            // level2 zone 拿所属woreda数量
            // level3 region 拿所属zone数量
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

        if (status.equals("2")) {
            demandInputSummaryItemService.updateDemandItemStatus(dto.getId());
        }
        if (status.equals("3")) {
            // 状态为拒绝，删除关联的子表数据
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

    /**
     * 创建任务结果统计类
     */
    public static class CreateTaskResult {
        private String levelName;
        private int totalCount;
        private int successCount;
        private int failCount;

        public CreateTaskResult(String levelName) {
            this.levelName = levelName;
            this.totalCount = 0;
            this.successCount = 0;
            this.failCount = 0;
        }

        public void incrementTotal() {
            this.totalCount++;
        }

        public void incrementSuccess() {
            this.successCount++;
        }

        public void incrementFail() {
            this.failCount++;
        }

        public String getLevelName() {
            return levelName;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public int getSuccessCount() {
            return successCount;
        }

        public int getFailCount() {
            return failCount;
        }

        @Override
        public String toString() {
            return String.format("%s - Total: %d, Success: %d, Failed: %d",
                    levelName, totalCount, successCount, failCount);
        }
    }

    /**
     * 创建所有层级的主任务
     *
     * @return 各层级的创建结果统计
     */
    public java.util.Map<String, CreateTaskResult> createAllMainTask(String year) {
//        String year = String.valueOf(java.time.Year.now().getValue());
        String regionCode = "102000000";

        // 初始化各层级统计结果
        CreateTaskResult regionResult = new CreateTaskResult("Region");
        CreateTaskResult zoneResult = new CreateTaskResult("Zone");
        CreateTaskResult woredaResult = new CreateTaskResult("Woreda");
        CreateTaskResult kebeleResult = new CreateTaskResult("Kebele");

        // 1. 处理 Region 层级
        regionResult.incrementTotal();
        boolean regionSuccess = processLevelRecord(regionCode, "1", year);
        if (regionSuccess) {
            regionResult.incrementSuccess();
        } else {
            regionResult.incrementFail();
        }

        // 2. 获取并处理 Zone 层级
        if (regionSuccess) {
            List<PubRegion> zoneList = getChildRegions(regionCode);
            for (PubRegion zone : zoneList) {
                zoneResult.incrementTotal();
                boolean zoneSuccess = processLevelRecord(zone.getCode(), "2", year);
                if (zoneSuccess) {
                    zoneResult.incrementSuccess();
                } else {
                    zoneResult.incrementFail();
                }

                // 3. 获取并处理 Woreda 层级
                List<PubRegion> woredaList = getChildRegions(zone.getCode());
                for (PubRegion woreda : woredaList) {
                    woredaResult.incrementTotal();
                    boolean woredaSuccess = processLevelRecord(woreda.getCode(), "3", year);
                    if (woredaSuccess) {
                        woredaResult.incrementSuccess();
                    } else {
                        woredaResult.incrementFail();
                    }

                    // 4. 获取并处理 Kebele 层级
                    List<PubRegion> kebeleList = getChildRegions(woreda.getCode());
                    for (PubRegion kebele : kebeleList) {
                        kebeleResult.incrementTotal();
                        boolean kebeleSuccess = processLevelRecord(kebele.getCode(), "4", year);
                        if (kebeleSuccess) {
                            kebeleResult.incrementSuccess();
                        } else {
                            kebeleResult.incrementFail();
                        }
                    }
                }
            }
        }

        // 汇总结果
        java.util.Map<String, CreateTaskResult> resultMap = new java.util.LinkedHashMap<>();
        resultMap.put("Region", regionResult);
        resultMap.put("Zone", zoneResult);
        resultMap.put("Woreda", woredaResult);
        resultMap.put("Kebele", kebeleResult);

        // 打印统计结果
        System.out.println("===== Create All Main Task Statistics =====");
        resultMap.values().forEach(result -> System.out.println(result.toString()));
        System.out.println("============================================");

        return resultMap;
    }

    /**
     * 获取子区域列表
     *
     * @param parentCode 父区域编码
     * @return 子区域列表
     */
    private List<PubRegion> getChildRegions(String parentCode) {
        QueryWrapper<PubRegion> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_code", parentCode);
        wrapper.eq("type", "1");
        return regionMapper.selectList(wrapper);
    }

    /**
     * 处理单条层级记录
     *
     * @param sourceCode 区域编码
     * @param level      层级
     * @param year       年份
     * @return 是否成功
     */
    private boolean processLevelRecord(String sourceCode, String level, String year) {
        try {
            DemandInputSummaryDTO dto = new DemandInputSummaryDTO();
            dto.setLevel(level);
            dto.setYear(year);
            dto.setSourceCode(sourceCode);
            int result = this.addDemandInputSummary(dto);
            return result > 0;
        } catch (Exception e) {
            System.err.println("Failed to add record for sourceCode: " + sourceCode + ", level: " + level + ", error: "
                    + e.getMessage());
            return false;
        }
    }
}
