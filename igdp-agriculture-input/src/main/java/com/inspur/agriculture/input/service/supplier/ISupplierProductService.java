package com.inspur.agriculture.input.service.supplier;

import com.inspur.agriculture.input.dto.supplier.SupplierProductDTO;
import com.inspur.agriculture.input.dto.supplier.SupplierProductQueryDTO;
import com.inspur.agriculture.input.vo.supplier.SupplierProductVO;

import java.util.List;

/**
 * 供应商投入品Service接口
 *
 * @author igdp
 */
public interface ISupplierProductService {

    /**
     * 查询供应商投入品列表
     *
     * @param queryDTO 查询条件
     * @return 供应商投入品列表
     */
    List<SupplierProductVO> getSupplierProductList(SupplierProductQueryDTO queryDTO);

    /**
     * 根据ID查询供应商投入品详情
     *
     * @param supplierProductId 供应关系ID
     * @return 供应商投入品详情
     */
    SupplierProductVO getSupplierProductById(Long supplierProductId);

    /**
     * 添加供应商投入品关系
     *
     * @param dto 供应商投入品数据
     * @return 操作结果
     */
    int addSupplierProduct(SupplierProductDTO dto);

    /**
     * 更新供应商投入品关系
     *
     * @param dto 供应商投入品数据
     * @return 操作结果
     */
    int updateSupplierProduct(SupplierProductDTO dto);

    /**
     * 删除供应商投入品关系
     *
     * @param supplierProductId 供应关系ID
     * @return 操作结果
     */
    int deleteSupplierProduct(Long supplierProductId);

    /**
     * 批量删除供应商投入品关系
     *
     * @param supplierProductIds 供应关系ID数组
     * @return 操作结果
     */
    int batchDeleteSupplierProduct(Long[] supplierProductIds);
}
