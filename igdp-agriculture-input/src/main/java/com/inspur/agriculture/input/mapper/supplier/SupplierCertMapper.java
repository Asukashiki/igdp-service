package com.inspur.agriculture.input.mapper.supplier;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.supplier.SupplierCert;
import com.inspur.agriculture.input.dto.supplier.SupplierCertQueryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 供应商认证Mapper接口
 *
 * @author igdp
 */
public interface SupplierCertMapper extends BaseMapper<SupplierCert> {

    /**
     * 查询供应商认证列表
     *
     * @param supplierCert 供应商认证
     * @return 供应商认证集合
     */
    List<SupplierCert> selectCertList(SupplierCert supplierCert);

    /**
     * 根据用户ID查询认证记录
     *
     * @param userId 用户ID
     * @return 供应商认证
     */
    SupplierCert selectCertByUserId(@Param("userId") Long userId);

    /**
     * 根据认证ID查询认证详情
     *
     * @param certId 认证ID
     * @return 供应商认证
     */
    SupplierCert selectCertById(@Param("certId") Long certId);

    /**
     * 查询待审核列表（带筛选条件）
     *
     * @param queryDTO 查询条件
     * @return 供应商认证集合
     */
    List<SupplierCert> selectAuditListWithConditions(@Param("query") SupplierCertQueryDTO queryDTO);

    /**
     * 查询供应商认证列表（带筛选条件）
     *
     * @param queryDTO 查询条件
     * @return 供应商认证集合
     */
    List<SupplierCert> selectCertListWithConditions(@Param("query") SupplierCertQueryDTO queryDTO);
}
