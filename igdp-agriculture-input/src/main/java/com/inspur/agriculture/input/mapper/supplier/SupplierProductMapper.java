package com.inspur.agriculture.input.mapper.supplier;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.supplier.SupplierProduct;
import com.inspur.agriculture.input.dto.supplier.SupplierProductQueryDTO;
import com.inspur.agriculture.input.vo.supplier.SupplierProductVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 供应商投入品关系Mapper接口
 *
 * @author igdp
 */
public interface SupplierProductMapper extends BaseMapper<SupplierProduct> {

    /**
     * 查询供应商投入品列表（带关联信息）
     *
     * @param queryDTO 查询条件
     * @return 供应商投入品列表
     */
    List<SupplierProductVO> selectSupplierProductList(@Param("query") SupplierProductQueryDTO queryDTO);

    /**
     * 根据ID查询供应商投入品详情（带关联信息）
     *
     * @param supplierProductId 供应关系ID
     * @return 供应商投入品详情
     */
    SupplierProductVO selectSupplierProductById(@Param("supplierProductId") Long supplierProductId);

    /**
     * 检查供应商和投入品的关联是否已存在
     *
     * @param supplierId 供应商ID
     * @param inputId    投入品ID
     * @param excludeId  排除的ID（用于编辑时排除自己）
     * @return 存在的记录数
     */
    int checkSupplierProductExists(@Param("supplierId") Long supplierId,
                                    @Param("inputId") Long inputId,
                                    @Param("excludeId") Long excludeId);
}
