package com.inspur.agriculture.input.service.inventory.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inspur.agriculture.input.domain.inventory.Warehouse;
import com.inspur.agriculture.input.dto.inventory.WarehouseDTO;
import com.inspur.agriculture.input.dto.inventory.WarehouseQueryDTO;
import com.inspur.agriculture.input.mapper.inventory.WarehouseMapper;
import com.inspur.agriculture.input.service.inventory.IWarehouseService;
import com.inspur.agriculture.input.vo.inventory.WarehouseVO;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.DateUtils;
import com.inspur.common.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 仓库 Service实现
 *
 * @author inspur
 * @date 2025-11-26
 */
@Service
public class WarehouseServiceImpl implements IWarehouseService {

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Override
    public List<WarehouseVO> getWarehouseList(WarehouseQueryDTO queryDTO) {
        return warehouseMapper.selectWarehouseList(queryDTO);
    }

    @Override
    public WarehouseVO getWarehouseById(Long warehouseId) {
        return warehouseMapper.selectWarehouseById(warehouseId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addWarehouse(WarehouseDTO dto) {
        // 生成仓库编号
        String warehouseCode = generateWarehouseCode();

        // DTO转Entity
        Warehouse warehouse = new Warehouse();
        BeanUtils.copyProperties(dto, warehouse);
        warehouse.setWarehouseCode(warehouseCode);
        warehouse.setUsedCapacity(java.math.BigDecimal.ZERO);

        // 设置审计字段
        warehouse.setCreateTime(DateUtils.getNowDate());
        try {
            warehouse.setCreatePeople(SecurityUtils.getUsername());
        } catch (Exception e) {
            warehouse.setCreatePeople("system");
        }
        warehouse.setDelFlag("0");

        // 插入数据
        return warehouseMapper.insert(warehouse);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateWarehouse(WarehouseDTO dto) {
        if (dto.getWarehouseId() == null) {
            throw new ServiceException("仓库ID不能为空");
        }

        // 查询原记录
        Warehouse warehouse = warehouseMapper.selectById(dto.getWarehouseId());
        if (warehouse == null || "2".equals(warehouse.getDelFlag())) {
            throw new ServiceException("仓库不存在");
        }

        // DTO转Entity
        BeanUtils.copyProperties(dto, warehouse);

        // 设置审计字段
        warehouse.setUpdateTime(DateUtils.getNowDate());
        try {
            warehouse.setUpdatePeople(SecurityUtils.getUsername());
        } catch (Exception e) {
            warehouse.setUpdatePeople("system");
        }

        // 更新数据
        return warehouseMapper.updateById(warehouse);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteWarehouse(Long warehouseId) {
        // 查询原记录
        Warehouse warehouse = warehouseMapper.selectById(warehouseId);
        if (warehouse == null || "2".equals(warehouse.getDelFlag())) {
            throw new ServiceException("仓库不存在");
        }

        // 检查是否存在库存记录
        // 此处应该检查库存表,简化处理直接删除

        // 逻辑删除
        warehouse.setDelFlag("2");
        warehouse.setUpdateTime(DateUtils.getNowDate());
        try {
            warehouse.setUpdatePeople(SecurityUtils.getUsername());
        } catch (Exception e) {
            warehouse.setUpdatePeople("system");
        }

        return warehouseMapper.updateById(warehouse);
    }

    /**
     * 生成仓库编号: WH-{yyyyMMdd}-{4位序号}
     */
    private String generateWarehouseCode() {
        String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String prefix = "WH-" + dateStr + "-";

        // 查询当天最大序号
        QueryWrapper<Warehouse> wrapper = new QueryWrapper<>();
        wrapper.likeRight("warehouse_code", prefix);
        wrapper.eq("del_flag", "0");
        wrapper.orderByDesc("warehouse_code");
        wrapper.last("limit 1");

        Warehouse lastWarehouse = warehouseMapper.selectOne(wrapper);
        int nextSeq = 1;

        if (lastWarehouse != null) {
            String lastCode = lastWarehouse.getWarehouseCode();
            String seqStr = lastCode.substring(lastCode.lastIndexOf("-") + 1);
            nextSeq = Integer.parseInt(seqStr) + 1;
        }

        return prefix + String.format("%04d", nextSeq);
    }
}
