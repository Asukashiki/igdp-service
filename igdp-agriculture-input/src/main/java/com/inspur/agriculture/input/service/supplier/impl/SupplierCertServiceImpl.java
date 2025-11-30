package com.inspur.agriculture.input.service.supplier.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inspur.agriculture.input.domain.oauth.PubUserRole;
import com.inspur.agriculture.input.domain.supplier.SupplierCert;
import com.inspur.agriculture.input.domain.supplier.enums.CertStatusEnum;
import com.inspur.agriculture.input.dto.supplier.SupplierCertApplyDTO;
import com.inspur.agriculture.input.dto.supplier.SupplierCertApproveDTO;
import com.inspur.agriculture.input.dto.supplier.SupplierCertQueryDTO;
import com.inspur.agriculture.input.dto.supplier.SupplierCertUpdateDTO;
import com.inspur.agriculture.input.mapper.oauth.PubUserRoleMapper;
import com.inspur.agriculture.input.mapper.supplier.SupplierCertMapper;
import com.inspur.agriculture.input.service.supplier.ISupplierCertService;
import com.inspur.agriculture.input.vo.supplier.ApproveResponseVO;
import com.inspur.agriculture.input.vo.supplier.CertApplyResponseVO;
import com.inspur.agriculture.input.vo.supplier.CertStatusVO;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.DateUtils;
import com.inspur.common.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 供应商认证Service业务层处理
 *
 * @author igdp
 */
@Service
public class SupplierCertServiceImpl implements ISupplierCertService {

    @Autowired
    private SupplierCertMapper supplierCertMapper;

    @Autowired
    private PubUserRoleMapper pubUserRoleMapper;

    /**
     * 供应商认证申请
     *
     * @param dto 认证申请数据
     * @return 申请响应
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public CertApplyResponseVO applyCert(SupplierCertApplyDTO dto) {
        // 1. 校验当前用户是否已提交认证申请
        LambdaQueryWrapper<SupplierCert> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SupplierCert::getUserId, dto.getUserId())
               .ne(SupplierCert::getStatus, CertStatusEnum.UN_PASSED.getCode())
               .eq(SupplierCert::getDelFlag, "0");
        SupplierCert existCert = supplierCertMapper.selectOne(wrapper);
        if (existCert != null) {
            throw new ServiceException("您已提交过认证申请，请勿重复提交");
        }

        // 2. 校验统一社会信用代码唯一性
        LambdaQueryWrapper<SupplierCert> codeWrapper = new LambdaQueryWrapper<>();
        codeWrapper.eq(SupplierCert::getCreditCode, dto.getCreditCode())
                   .eq(SupplierCert::getDelFlag, "0");
        SupplierCert existCode = supplierCertMapper.selectOne(codeWrapper);
        if (existCode != null) {
            throw new ServiceException("该统一社会信用代码已存在");
        }

        // 3. 封装认证实体
        SupplierCert cert = new SupplierCert();
        BeanUtils.copyProperties(dto, cert);
        cert.setStatus(CertStatusEnum.AUDITING.getCode());
        cert.setApplyTime(DateUtils.getNowDate());
        cert.setCreateTime(DateUtils.getNowDate());
        try {
            String username = SecurityUtils.getUsername();
            cert.setCreatePeople(username);
        } catch (Exception e) {
            cert.setCreatePeople("system");
        }
        cert.setDelFlag("0");

        // 4. 插入认证记录
        int rows = supplierCertMapper.insert(cert);
        if (rows <= 0) {
            throw new ServiceException("认证申请提交失败");
        }

        // 5. 封装响应
        CertApplyResponseVO response = new CertApplyResponseVO();
        response.setCertId(cert.getCertId());
        response.setStatus(cert.getStatus());
        response.setStatusDesc(CertStatusEnum.getDescByCode(cert.getStatus()));
        response.setApplyTime(cert.getApplyTime());

        return response;
    }

    /**
     * 供应商认证审批
     *
     * @param certId 认证ID
     * @param dto    审批数据
     * @return 审批响应
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ApproveResponseVO auditCert(Long certId, SupplierCertApproveDTO dto) {
        // 1. 校验认证记录是否存在
        SupplierCert cert = supplierCertMapper.selectById(certId);
        if (cert == null || "2".equals(cert.getDelFlag())) {
            throw new ServiceException("认证记录不存在");
        }

        // 2. 校验认证状态是否为审核中
        if (!CertStatusEnum.AUDITING.getCode().equals(cert.getStatus())) {
            throw new ServiceException("该认证记录不是审核中状态，无法审批");
        }

        // 3. 更新认证记录
        Date now = DateUtils.getNowDate();
        cert.setApproverId(dto.getApproverId());
        cert.setApproveTime(now);
        cert.setUpdateTime(now);
        try {
            String username = SecurityUtils.getUsername();
            cert.setUpdatePeople(username);
        } catch (Exception e) {
            cert.setUpdatePeople("system");
        }

        // 4. 根据审核结果更新状态
        if (dto.getAuditResult() == 1) {
            // 审核通过
            cert.setStatus(CertStatusEnum.PASSED.getCode());
            cert.setAuditOpinion(dto.getAuditOpinion());
            cert.setRejectReason(null);
        } else {
            // 审核驳回
            cert.setStatus(CertStatusEnum.UN_PASSED.getCode());
            cert.setRejectReason(dto.getAuditOpinion());
            cert.setAuditOpinion(null);
        }

        int rows = supplierCertMapper.updateById(cert);
        if (rows <= 0) {
            throw new ServiceException("审批操作失败");
        }

        // 5. 审批通过后，向oauth2_bsp.pub_user_role表插入用户角色关联数据
        if (dto.getAuditResult() == 1) {
            PubUserRole pubUserRole = new PubUserRole();
            pubUserRole.setUserCode(String.valueOf(cert.getUserId()));
            pubUserRole.setRoleCode("supplier");
            pubUserRole.setAppCode("inputSupply");
            pubUserRoleMapper.insert(pubUserRole);
        }

        // 6. 封装响应
        ApproveResponseVO response = new ApproveResponseVO();
        response.setCertId(cert.getCertId());
        response.setStatus(cert.getStatus());
        response.setStatusDesc(CertStatusEnum.getDescByCode(cert.getStatus()));
        response.setApproveTime(cert.getApproveTime());
        response.setApproverId(cert.getApproverId());
        response.setRejectReason(cert.getRejectReason());

        return response;
    }

    /**
     * 查询认证状态
     *
     * @param userId 用户ID
     * @return 认证状态
     */
    @Override
    public CertStatusVO getCertStatus(String userId) {
        SupplierCert cert = supplierCertMapper.selectCertByUserId(userId);
        if (cert == null) {
            return null;
        }

        CertStatusVO vo = new CertStatusVO();
        BeanUtils.copyProperties(cert, vo);
        vo.setStatusDesc(CertStatusEnum.getDescByCode(cert.getStatus()));

        return vo;
    }

    /**
     * 查询待审核列表（分页，带筛选条件）
     *
     * @param queryDTO 查询条件
     * @return 待审核列表
     */
    @Override
    public List<SupplierCert> getAuditTodoList(SupplierCertQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new SupplierCertQueryDTO();
        }
        return supplierCertMapper.selectAuditListWithConditions(queryDTO);
    }

    /**
     * 查询供应商认证列表（分页，带筛选条件）
     *
     * @param queryDTO 查询条件
     * @return 认证列表
     */
    @Override
    public List<SupplierCert> getCertList(SupplierCertQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new SupplierCertQueryDTO();
        }
        return supplierCertMapper.selectCertListWithConditions(queryDTO);
    }

    /**
     * 根据认证ID查询详情
     *
     * @param certId 认证ID
     * @return 认证详情
     */
    @Override
    public SupplierCert getCertById(String certId) {
        return supplierCertMapper.selectCertById(certId);
    }

    /**
     * 根据用户ID查询认证信息
     *
     * @param userId 用户ID
     * @return 认证详情
     */
    @Override
    public SupplierCert getCertByUserId(String userId) {
        return supplierCertMapper.selectCertByUserId(userId);
    }

    /**
     * 更新供应商认证信息
     *
     * @param dto 更新数据
     * @return 更新结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateCert(SupplierCertUpdateDTO dto) {
        // 1. 校验认证记录是否存在
        SupplierCert cert = supplierCertMapper.selectById(dto.getCertId());
        if (cert == null || "2".equals(cert.getDelFlag())) {
            throw new ServiceException("认证记录不存在");
        }

        // 2. 只允许修改未通过或审核中的认证信息
        if (CertStatusEnum.PASSED.getCode().equals(cert.getStatus())) {
            throw new ServiceException("已通过审核的认证信息不允许修改，如需变更请联系管理员");
        }

        // 3. 如果修改了统一社会信用代码，需要校验唯一性
        if (!cert.getCreditCode().equals(dto.getCreditCode())) {
            LambdaQueryWrapper<SupplierCert> codeWrapper = new LambdaQueryWrapper<>();
            codeWrapper.eq(SupplierCert::getCreditCode, dto.getCreditCode())
                       .ne(SupplierCert::getCertId, dto.getCertId())
                       .eq(SupplierCert::getDelFlag, "0");
            SupplierCert existCode = supplierCertMapper.selectOne(codeWrapper);
            if (existCode != null) {
                throw new ServiceException("该统一社会信用代码已存在");
            }
        }

        // 4. 更新认证信息
        BeanUtils.copyProperties(dto, cert);
        cert.setUpdateTime(DateUtils.getNowDate());
        try {
            String username = SecurityUtils.getUsername();
            cert.setUpdatePeople(username);
        } catch (Exception e) {
            cert.setUpdatePeople("system");
        }

        // 5. 如果之前是未通过状态，更新后重新设置为审核中
        if (CertStatusEnum.UN_PASSED.getCode().equals(cert.getStatus())) {
            cert.setStatus(CertStatusEnum.AUDITING.getCode());
            cert.setRejectReason(null);
        }

        return supplierCertMapper.updateById(cert);
    }
}
