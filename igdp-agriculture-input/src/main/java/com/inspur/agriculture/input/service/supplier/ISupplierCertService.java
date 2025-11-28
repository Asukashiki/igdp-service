package com.inspur.agriculture.input.service.supplier;

import com.inspur.agriculture.input.domain.supplier.SupplierCert;
import com.inspur.agriculture.input.dto.supplier.SupplierCertApplyDTO;
import com.inspur.agriculture.input.dto.supplier.SupplierCertApproveDTO;
import com.inspur.agriculture.input.dto.supplier.SupplierCertQueryDTO;
import com.inspur.agriculture.input.dto.supplier.SupplierCertUpdateDTO;
import com.inspur.agriculture.input.vo.supplier.ApproveResponseVO;
import com.inspur.agriculture.input.vo.supplier.CertApplyResponseVO;
import com.inspur.agriculture.input.vo.supplier.CertStatusVO;

import java.util.List;

/**
 * 供应商认证Service接口
 *
 * @author igdp
 */
public interface ISupplierCertService {

    /**
     * 供应商认证申请
     *
     * @param dto 认证申请数据
     * @return 申请响应
     */
    CertApplyResponseVO applyCert(SupplierCertApplyDTO dto);

    /**
     * 供应商认证审批
     *
     * @param certId 认证ID
     * @param dto    审批数据
     * @return 审批响应
     */
    ApproveResponseVO auditCert(Long certId, SupplierCertApproveDTO dto);

    /**
     * 查询认证状态
     *
     * @param userId 用户ID
     * @return 认证状态
     */
    CertStatusVO getCertStatus(String userId);

    /**
     * 查询待审核列表（分页，带筛选条件）
     *
     * @param queryDTO 查询条件
     * @return 待审核列表
     */
    List<SupplierCert> getAuditTodoList(SupplierCertQueryDTO queryDTO);

    /**
     * 查询供应商认证列表（分页，带筛选条件）
     *
     * @param queryDTO 查询条件
     * @return 认证列表
     */
    List<SupplierCert> getCertList(SupplierCertQueryDTO queryDTO);

    /**
     * 根据认证ID查询详情
     *
     * @param certId 认证ID
     * @return 认证详情
     */
    SupplierCert getCertById(String certId);

    /**
     * 根据用户ID查询认证信息
     *
     * @param userId 用户ID
     * @return 认证详情
     */
    SupplierCert getCertByUserId(String userId);

    /**
     * 更新供应商认证信息
     *
     * @param dto 更新数据
     * @return 更新结果
     */
    int updateCert(SupplierCertUpdateDTO dto);
}
