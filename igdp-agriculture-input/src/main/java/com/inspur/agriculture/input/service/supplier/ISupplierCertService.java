package com.inspur.agriculture.input.service.supplier;

import com.inspur.agriculture.input.domain.supplier.SupplierCert;
import com.inspur.agriculture.input.dto.supplier.SupplierCertApplyDTO;
import com.inspur.agriculture.input.dto.supplier.SupplierCertApproveDTO;
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
    CertStatusVO getCertStatus(Long userId);

    /**
     * 查询待审核列表（分页）
     *
     * @return 待审核列表
     */
    List<SupplierCert> getAuditTodoList();

    /**
     * 根据认证ID查询详情
     *
     * @param certId 认证ID
     * @return 认证详情
     */
    SupplierCert getCertById(Long certId);
}
