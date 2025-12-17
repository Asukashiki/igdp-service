package com.inspur.agriculture.input.service.allocation.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.agriculture.input.domain.allocation.dto.AllocationAddDTO;
import com.inspur.agriculture.input.domain.allocation.dto.AllocationDeleteDTO;
import com.inspur.agriculture.input.domain.allocation.dto.AllocationUpdateDTO;
import com.inspur.agriculture.input.domain.allocation.entity.Allocation;
import com.inspur.agriculture.input.domain.allocation.entity.AllocationDemand;
import com.inspur.agriculture.input.domain.allocation.entity.AllocationQuota;
import com.inspur.agriculture.input.domain.allocation.vo.AllocationDetailVO;
import com.inspur.agriculture.input.domain.allocation.vo.AllocationVO;
import com.inspur.agriculture.input.mapper.allocation.AllocationDemandMapper;
import com.inspur.agriculture.input.mapper.allocation.AllocationMapper;
import com.inspur.agriculture.input.mapper.allocation.AllocationQuotaMapper;
import com.inspur.agriculture.input.service.allocation.AllocationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Zone Allocation Service Implementation
 * 区域分配额度服务实现类
 */
@Service
public class AllocationServiceImpl extends ServiceImpl<AllocationMapper, Allocation> implements AllocationService {

    @Resource
    private AllocationMapper allocationMapper;

    @Resource
    private AllocationDemandMapper allocationDemandMapper;

    @Resource
    private AllocationQuotaMapper allocationQuotaMapper;

    /**
     * Add zone allocation
     *
     * @param dto Add DTO
     * @return ZoneAllocationVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AllocationVO add(AllocationAddDTO dto) {
        // Save main allocation
        Allocation allocation = new Allocation();
        allocation.setAllocationName(dto.getAllocationName());
        allocation.setYear(dto.getYear());
        allocation.setZone(dto.getZone());
        allocation.setZoneName(dto.getZoneName());
        allocation.setLevel(dto.getLevel());
        allocation.setCreateTime(LocalDateTime.now());
        allocation.setUpdateTime(LocalDateTime.now());
        allocationMapper.insert(allocation);

        // Save demand items
        List<AllocationDemand> demandList = dto.getDemandList();
        if (demandList != null && !demandList.isEmpty()) {
            for (AllocationDemand demand : demandList) {
                demand.setAllocationId(allocation.getId());
                allocationDemandMapper.insert(demand);
            }
        }

        // Save quota items
        List<AllocationQuota> quotaList = dto.getQuotaList();
        if (quotaList != null && !quotaList.isEmpty()) {
            for (AllocationQuota quota : quotaList) {
                quota.setAllocationId(allocation.getId());
                allocationQuotaMapper.insert(quota);
            }
        }


        // 计算woreda 每个inputCategory 的需求AllocationDemand和分配AllocationQuota的比例
        if(allocation.getLevel().equals("woreda")){
            // 创建一个映射来存储每个inputCategory的需求总量
            Map<String, BigDecimal> demandTotalByCategory = new HashMap<>();

            // 创建一个映射存储比例
            Map<String, BigDecimal> ratioByCategory = new HashMap<>();


            // 统计每种inputCategory的需求总量
            for (AllocationDemand demand : demandList) {
                String inputCategory = demand.getInputCategory();
                BigDecimal totalQuantity = demand.getTotalQuantity();

                demandTotalByCategory.put(inputCategory,
                    demandTotalByCategory.getOrDefault(inputCategory, BigDecimal.ZERO)
                        .add(totalQuantity != null ? totalQuantity : BigDecimal.ZERO));
            }

            // 计算每种inputCategory的配额与需求的比例
            for (AllocationQuota quota : quotaList) {
                String inputCategory = quota.getInputCategory();
                BigDecimal quotaQuantity = quota.getTotalQuantity();
                BigDecimal demandQuantity = demandTotalByCategory.get(inputCategory);

                if (demandQuantity != null && demandQuantity.compareTo(BigDecimal.ZERO) > 0) {
                    // 计算配额与需求的比例
                    BigDecimal ratio = quotaQuantity.divide(demandQuantity, 6, BigDecimal.ROUND_HALF_UP);
                    // 这里可以保存或使用ratio变量进行后续处理
                    ratioByCategory.put(inputCategory, ratio);
                }
            }

            // 查询woreda 下的农民 需求
            List<Map<String, Object>> farmers = allocationMapper.selectByWoredaIdAndYear(allocation.getZone(), allocation.getYear());
            for (Map<String, Object> farmer : farmers) {
                AllocationAddDTO farmerDemandDTO = new AllocationAddDTO();
                farmerDemandDTO.setAllocationName(farmer.get("farmer_name").toString()+"-"+allocation.getYear());
                farmerDemandDTO.setYear(allocation.getYear());
                farmerDemandDTO.setZone(farmer.get("farmer_id").toString());
                farmerDemandDTO.setZoneName(farmer.get("farmer_name").toString());
                farmerDemandDTO.setLevel("farmer");
                List<Map<String, Object>> farmerDemandItems = allocationMapper.selectByDemandId(farmer.get("id").toString());
                List<AllocationDemand> farmerDemandList = new ArrayList<>();
                List<AllocationQuota> farmerQuotaList = new ArrayList<>();
                farmerDemandDTO.setDemandList(farmerDemandList);
                for (Map<String, Object> farmerDemandItemMap : farmerDemandItems) {
                    AllocationDemand farmerDemand = new AllocationDemand();
                    AllocationQuota  farmerQuota = new AllocationQuota();
                    farmerDemand.setInputType(farmerDemandItemMap.get("input_type").toString());
                    farmerDemand.setInputCategory(farmerDemandItemMap.get("input_category").toString());
                    farmerDemand.setTotalQuantity(new BigDecimal(farmerDemandItemMap.get("quantity").toString()));
                    farmerDemandList.add(farmerDemand);

                    farmerQuota.setInputType(farmerDemandItemMap.get("input_type").toString());
                    farmerQuota.setInputCategory(farmerDemandItemMap.get("input_category").toString());
                    farmerQuota.setTotalQuantity(new BigDecimal(farmerDemandItemMap.get("quantity").toString()).multiply(ratioByCategory.get(farmerDemandItemMap.get("input_category").toString())));
                    farmerQuotaList.add(farmerQuota);
                }
                farmerDemandDTO.setQuotaList(farmerQuotaList);

                add(farmerDemandDTO);


            }
        }

        // Return VO
        AllocationVO vo = new AllocationVO();
        vo.setId(allocation.getId());
        vo.setAllocationName(allocation.getAllocationName());
        vo.setYear(allocation.getYear());
        vo.setZone(allocation.getZone());
        vo.setCreateTime(allocation.getCreateTime());
        vo.setUpdateTime(allocation.getUpdateTime());
        return vo;
    }

    /**
     * Page query zone allocation
     *
     * @param pageNum Page number
     * @param pageSize Page size
     * @param allocationName Allocation name filter
     * @param year Year filter
     * @param zone Zone filter
     * @return IPage result
     */
    @Override
    public IPage<AllocationVO> page(Integer pageNum, Integer pageSize, String allocationName, String year, String zone, String level) {
        Page<AllocationVO> page = new Page<>(pageNum, pageSize);
        return allocationMapper.selectPageWithDetail(page, allocationName, year, zone, level);
    }

    /**
     * Get zone allocation detail
     *
     * @param id Allocation ID
     * @return ZoneAllocationDetailVO
     */
    @Override
    public AllocationDetailVO detail(String id) {
        AllocationDetailVO detailVO = new AllocationDetailVO();

        // Get main allocation
        AllocationVO main = allocationMapper.selectDetailById(id);
        detailVO.setMain(main);

        // Get demand list
        List<AllocationDemand> demandList = allocationDemandMapper.selectByAllocationId(id);
        detailVO.setDemandList(demandList);

        // Get quota list
        List<AllocationQuota> quotaList = allocationQuotaMapper.selectByAllocationId(id);
        detailVO.setQuotaList(quotaList);

        return detailVO;
    }

    /**
     * Update zone allocation
     *
     * @param dto Update DTO
     * @return ZoneAllocationVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AllocationVO update(AllocationUpdateDTO dto) {
        // Update main allocation
        Allocation allocation = new Allocation();
        allocation.setId(dto.getId());
        allocation.setAllocationName(dto.getAllocationName());
        allocation.setYear(dto.getYear());
        allocation.setZone(dto.getZone());
        allocation.setUpdateTime(LocalDateTime.now());
        allocationMapper.updateById(allocation);

        // Delete old demand and quota items
        allocationDemandMapper.deleteByAllocationId(dto.getId());
        allocationQuotaMapper.deleteByAllocationId(dto.getId());

        // Save new demand items
        List<AllocationDemand> demandList = dto.getDemandList();
        if (demandList != null && !demandList.isEmpty()) {
            for (AllocationDemand demand : demandList) {
                demand.setAllocationId(dto.getId());
                allocationDemandMapper.insert(demand);
            }
        }

        // Save new quota items
        List<AllocationQuota> quotaList = dto.getQuotaList();
        if (quotaList != null && !quotaList.isEmpty()) {
            for (AllocationQuota quota : quotaList) {
                quota.setAllocationId(dto.getId());
                allocationQuotaMapper.insert(quota);
            }
        }

        // Return VO
        AllocationVO vo = new AllocationVO();
        vo.setId(allocation.getId());
        vo.setAllocationName(allocation.getAllocationName());
        vo.setYear(allocation.getYear());
        vo.setZone(allocation.getZone());
        vo.setCreateTime(allocation.getCreateTime());
        vo.setUpdateTime(allocation.getUpdateTime());
        return vo;
    }

    /**
     * Delete zone allocation
     *
     * @param dto Delete DTO
     * @return Delete result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(AllocationDeleteDTO dto) {
        // Delete demand and quota items first
        allocationDemandMapper.deleteByAllocationId(dto.getId());
        allocationQuotaMapper.deleteByAllocationId(dto.getId());

        // Delete main allocation
        return allocationMapper.deleteById(dto.getId()) > 0;
    }
}
