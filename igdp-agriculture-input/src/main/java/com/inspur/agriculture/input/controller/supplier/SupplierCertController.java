package com.inspur.agriculture.input.controller.supplier;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inspur.agriculture.input.domain.supplier.SupplierCert;
import com.inspur.agriculture.input.dto.supplier.SupplierCertApplyDTO;
import com.inspur.agriculture.input.dto.supplier.SupplierCertApproveDTO;
import com.inspur.agriculture.input.dto.supplier.SupplierCertQueryDTO;
import com.inspur.agriculture.input.dto.supplier.SupplierCertUpdateDTO;
import com.inspur.agriculture.input.service.supplier.ISupplierCertService;
import com.inspur.agriculture.input.vo.supplier.ApproveResponseVO;
import com.inspur.agriculture.input.vo.supplier.CertApplyResponseVO;
import com.inspur.agriculture.input.vo.supplier.CertStatusVO;
import com.inspur.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 供应商认证Controller
 *
 * @author igdp
 */
@RestController
@RequestMapping("/supplier/cert")
public class SupplierCertController {

    @Autowired
    private ISupplierCertService supplierCertService;

    /**
     * 供应商认证申请
     *
     * @param dto 认证申请数据
     * @return 申请响应
     */
    @PostMapping("/apply")
    public AjaxResult apply(@Validated @RequestBody SupplierCertApplyDTO dto) {
        try {
            CertApplyResponseVO response = supplierCertService.applyCert(dto);
            return AjaxResult.success("供应商认证申请提交成功，当前状态：审核中", response);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 供应商认证审批
     *
     * @param certId 认证ID
     * @param dto    审批数据
     * @return 审批响应
     */
    @PutMapping("/approve/{certId}")
    public AjaxResult approve(
            @PathVariable("certId") Long certId,
            @Validated @RequestBody SupplierCertApproveDTO dto
    ) {
        try {
            ApproveResponseVO response = supplierCertService.auditCert(certId, dto);

            String message = response.getStatus() == 2 ? "审核已通过" : "审核已驳回";
            return AjaxResult.success(message, response);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询认证状态
     *
     * @param userId 用户ID
     * @return 认证状态
     */
    @GetMapping("/status/{userId}")
    public AjaxResult getStatus(@PathVariable("userId") String userId) {
        try {
            CertStatusVO status = supplierCertService.getCertStatus(userId);
            if (status == null) {
                return AjaxResult.error("未找到认证记录");
            }
            return AjaxResult.success(status);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询待审核列表（分页，带筛选）
     *
     * @param queryDTO 查询条件
     * @param page     页码
     * @param pageSize 每页数量
     * @return 待审核列表
     */
    @GetMapping("/audit/list")
    public AjaxResult getAuditList(
            SupplierCertQueryDTO queryDTO,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        try {
            // 启动分页
            PageHelper.startPage(page, pageSize);
            List<SupplierCert> list = supplierCertService.getAuditTodoList(queryDTO);
            PageInfo<SupplierCert> pageInfo = new PageInfo<>(list);

            Map<String, Object> result = new HashMap<>();
            result.put("list", pageInfo.getList());
            result.put("total", pageInfo.getTotal());
            result.put("page", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());

            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询供应商认证列表（分页，带筛选）
     *
     * @param queryDTO 查询条件
     * @param page     页码
     * @param pageSize 每页数量
     * @return 认证列表
     */
    @GetMapping("/list")
    public AjaxResult getCertList(
            SupplierCertQueryDTO queryDTO,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        try {
            // 启动分页
            PageHelper.startPage(page, pageSize);
            List<SupplierCert> list = supplierCertService.getCertList(queryDTO);
            PageInfo<SupplierCert> pageInfo = new PageInfo<>(list);

            Map<String, Object> result = new HashMap<>();
            result.put("list", pageInfo.getList());
            result.put("total", pageInfo.getTotal());
            result.put("page", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());

            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询认证详情
     *
     * @param certId 认证ID
     * @return 认证详情
     */
    @GetMapping("/{certId}")
    public AjaxResult getInfo(@PathVariable("certId") String certId) {
        try {
            SupplierCert cert = supplierCertService.getCertById(certId);
            if (cert == null) {
                return AjaxResult.error("认证记录不存在");
            }
            return AjaxResult.success(cert);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 根据用户ID查询认证信息
     *
     * @param userId 用户ID
     * @return 认证详情
     */
    @GetMapping("/user/{userId}")
    public AjaxResult getByUserId(@PathVariable("userId") String userId) {
        try {
            SupplierCert cert = supplierCertService.getCertByUserId(userId);
            if (cert == null) {
                return AjaxResult.error("未找到该用户的认证信息");
            }
            return AjaxResult.success(cert);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 更新供应商认证信息
     *
     * @param dto 更新数据
     * @return 更新结果
     */
    @PostMapping("/update")
    public AjaxResult update(@Validated @RequestBody SupplierCertUpdateDTO dto) {
        try {
            int rows = supplierCertService.updateCert(dto);
            return rows > 0 ? AjaxResult.success("认证信息更新成功") : AjaxResult.error("认证信息更新失败");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
