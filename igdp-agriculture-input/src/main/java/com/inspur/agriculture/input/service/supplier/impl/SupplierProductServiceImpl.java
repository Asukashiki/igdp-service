package com.inspur.agriculture.input.service.supplier.impl;

import com.inspur.agriculture.input.domain.supplier.SupplierProduct;
import com.inspur.agriculture.input.domain.supplier.enums.CertStatusEnum;
import com.inspur.agriculture.input.dto.supplier.SupplierProductDTO;
import com.inspur.agriculture.input.dto.supplier.SupplierProductQueryDTO;
import com.inspur.agriculture.input.mapper.supplier.SupplierProductMapper;
import com.inspur.agriculture.input.service.supplier.ISupplierProductService;
import com.inspur.agriculture.input.vo.supplier.SupplierProductVO;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.DateUtils;
import com.inspur.common.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 供应商投入品Service业务层处理
 *
 * @author igdp
 */
@Service
public class SupplierProductServiceImpl implements ISupplierProductService {

    @Autowired
    private SupplierProductMapper supplierProductMapper;

    /**
     * 查询供应商投入品列表
     *
     * @param queryDTO 查询条件
     * @return 供应商投入品列表
     */
    @Override
    public List<SupplierProductVO> getSupplierProductList(SupplierProductQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new SupplierProductQueryDTO();
        }
        List<SupplierProductVO> list = supplierProductMapper.selectSupplierProductList(queryDTO);

        // 补充认证状态描述
        for (SupplierProductVO vo : list) {
            if (vo.getCertStatus() != null) {
                vo.setCertStatusDesc(CertStatusEnum.getDescByCode(vo.getCertStatus()));
            }
        }

        return list;
    }

    /**
     * 根据ID查询供应商投入品详情
     *
     * @param supplierProductId 供应关系ID
     * @return 供应商投入品详情
     */
    @Override
    public SupplierProductVO getSupplierProductById(Long supplierProductId) {
        SupplierProductVO vo = supplierProductMapper.selectSupplierProductById(supplierProductId);
        if (vo != null && vo.getCertStatus() != null) {
            vo.setCertStatusDesc(CertStatusEnum.getDescByCode(vo.getCertStatus()));
        }
        return vo;
    }

    /**
     * 添加供应商投入品关系
     *
     * @param dto 供应商投入品数据
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addSupplierProduct(SupplierProductDTO dto) {
        // 1. 检查供应商和投入品的关联是否已存在
        int count = supplierProductMapper.checkSupplierProductExists(
                dto.getSupplierId(), dto.getInputId(), null);
        if (count > 0) {
            throw new ServiceException("该供应商与投入品的关联已存在");
        }

        // 2. 封装实体
        SupplierProduct supplierProduct = new SupplierProduct();
        BeanUtils.copyProperties(dto, supplierProduct);
        supplierProduct.setCreateTime(DateUtils.getNowDate());
        try {
            String username = SecurityUtils.getUsername();
            supplierProduct.setCreatePeople(username);
        } catch (Exception e) {
            supplierProduct.setCreatePeople("system");
        }
        supplierProduct.setDelFlag("0");

        // 3. 插入数据
        return supplierProductMapper.insert(supplierProduct);
    }

    /**
     * 更新供应商投入品关系
     *
     * @param dto 供应商投入品数据
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateSupplierProduct(SupplierProductDTO dto) {
        // 1. 检查记录是否存在
        SupplierProduct existProduct = supplierProductMapper.selectById(dto.getSupplierProductId());
        if (existProduct == null || "2".equals(existProduct.getDelFlag())) {
            throw new ServiceException("供应商投入品关系不存在");
        }

        // 2. 检查供应商和投入品的关联是否重复（排除自己）
        int count = supplierProductMapper.checkSupplierProductExists(
                dto.getSupplierId(), dto.getInputId(), dto.getSupplierProductId());
        if (count > 0) {
            throw new ServiceException("该供应商与投入品的关联已存在");
        }

        // 3. 更新数据
        SupplierProduct supplierProduct = new SupplierProduct();
        BeanUtils.copyProperties(dto, supplierProduct);
        supplierProduct.setUpdateTime(DateUtils.getNowDate());
        try {
            String username = SecurityUtils.getUsername();
            supplierProduct.setUpdatePeople(username);
        } catch (Exception e) {
            supplierProduct.setUpdatePeople("system");
        }

        return supplierProductMapper.updateById(supplierProduct);
    }

    /**
     * 删除供应商投入品关系（逻辑删除）
     *
     * @param supplierProductId 供应关系ID
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteSupplierProduct(Long supplierProductId) {
        SupplierProduct supplierProduct = new SupplierProduct();
        supplierProduct.setSupplierProductId(supplierProductId);
        supplierProduct.setDelFlag("2");
        supplierProduct.setUpdateTime(DateUtils.getNowDate());
        try {
            String username = SecurityUtils.getUsername();
            supplierProduct.setUpdatePeople(username);
        } catch (Exception e) {
            supplierProduct.setUpdatePeople("system");
        }
        return supplierProductMapper.updateById(supplierProduct);
    }

    /**
     * 批量删除供应商投入品关系
     *
     * @param supplierProductIds 供应关系ID数组
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int batchDeleteSupplierProduct(Long[] supplierProductIds) {
        int rows = 0;
        for (Long id : supplierProductIds) {
            rows += deleteSupplierProduct(id);
        }
        return rows;
    }
}
