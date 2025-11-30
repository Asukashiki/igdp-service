package com.inspur.seed.controller;

import com.inspur.common.annotation.Log;
import com.inspur.common.core.controller.BaseController;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.page.TableDataInfo;
import com.inspur.common.enums.BusinessType;
import com.inspur.common.exception.ServiceException;
import com.inspur.common.utils.LoginHelper;
import com.inspur.seed.domain.VarietyAudit;
import com.inspur.seed.domain.VarietyPublish;
import com.inspur.seed.domain.VarietyRegistration;
import com.inspur.seed.domain.vo.VarietyAuditTaskVO;
import com.inspur.seed.service.IVarietyAuditService;
import com.inspur.seed.service.IVarietyPublishService;
import com.inspur.seed.service.IVarietyRegistrationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 品种审核控制器
 *
 * @author system
 */
@RestController
@RequestMapping("/seed/variety/audit")
public class VarietyAuditController extends BaseController {

    @Resource
    private IVarietyAuditService varietyAuditService;

    @Resource
    private IVarietyRegistrationService varietyRegistrationService;

     @Resource
    private IVarietyPublishService varietyPublishService;

    /**
     * 处理品种审核
     * 修改当前审核单状态，根据审核结果执行不同操作：
     * 1. 通过审核(audit_result = '1'): 保存audit_opinion，生成待发布单子
     * 2. 不通过审核(audit_result = '2'): 保存reject_reason
     *
     * @param varietyAudit 审核信息
     * @return 审核结果
     */
    //@SaCheckPermission("seed:variety:audit:handle")
    @Log(title = "品种审核", businessType = BusinessType.UPDATE)
    @PostMapping("/handle")
    public AjaxResult handle(@Validated @RequestBody VarietyAudit varietyAudit) {
        try {
            // 校验品种登记申请是否存在
            VarietyRegistration registration = varietyRegistrationService.queryByRegistrationId(varietyAudit.getRegistrationId());
            if (registration == null) {
                throw new ServiceException("品种登记申请不存在");
            }

            // 校验品种登记申请状态是否为审核中
            if (registration.getRecordStatus() != 0) {
                throw new ServiceException("品种登记申请状态不是审核中，无法审核");
            }

            // 修改当前审核单
            String auditId = varietyAuditService.handleAudit(varietyAudit);

            // 更新品种登记申请状态
            // 审核通过：更新为待发布(1)，驳回：更新为审核未通过(2)
            Integer recordStatus = varietyAudit.getAuditResult() == 1 ? 1 : 2;
            varietyRegistrationService.updateRecordStatus(varietyAudit.getRegistrationId(), recordStatus);

            // 获取更新后的状态
            registration = varietyRegistrationService.queryByRegistrationId(varietyAudit.getRegistrationId());

            Map<String, Object> data = new HashMap<>();
            data.put("registrationId", varietyAudit.getRegistrationId());
            data.put("recordStatus", registration != null ? registration.getRecordStatus() : null);
            data.put("auditTime", varietyAudit.getAuditTime());

            // 如果审核通过，生成待发布单子
            if (varietyAudit.getAuditResult() == 1 && registration != null) {
                VarietyPublish varietyPublish = new VarietyPublish();

                // 基础关联信息
                varietyPublish.setRegistrationId(varietyAudit.getRegistrationId());
                varietyPublish.setAuditId(varietyAudit.getAuditId());
                varietyPublish.setVarietyName(registration.getVarietyName());
                varietyPublish.setCropType(registration.getCropType());

                // 发布时间和状态
                varietyPublish.setPublishDate(java.time.LocalDate.now());
                varietyPublish.setPublishTime(LocalDateTime.now());
                // 公示中
                varietyPublish.setPublishStatus(1);
                varietyPublish.setPublisher(LoginHelper.getUsername());

                // 发布主管部门（可从登记信息的操作机构获取）
                varietyPublish.setPublishDept(registration.getOperationOrg());

                // 决策说明（使用审核意见）
                varietyPublish.setDecisionExplanation(varietyAudit.getAuditOpinion());

                // 公开描述（基于品种信息生成）
                StringBuilder publicDesc = new StringBuilder();
                publicDesc.append("品种名称：").append(registration.getVarietyName());
                publicDesc.append("，作物类型：").append(registration.getCropType());
                if (registration.getBreedingMethod() != null) {
                    publicDesc.append("，育种方法：").append(registration.getBreedingMethod());
                }
                if (registration.getGrowthPeriod() != null) {
                    publicDesc.append("，生育期：").append(registration.getGrowthPeriod()).append("天");
                }
                varietyPublish.setPublicDescription(publicDesc.toString());

                // 推荐地区（可从试验地点获取）
                varietyPublish.setRecommendedRegion(registration.getTestLocation());

                // 播种指南（基于品种特性生成）
                StringBuilder sowingGuide = new StringBuilder();
                if (registration.getGrowthPeriod() != null) {
                    sowingGuide.append("生育期约").append(registration.getGrowthPeriod()).append("天。");
                }
                if (registration.getDiseaseResistance() != null) {
                    sowingGuide.append("抗病性：").append(registration.getDiseaseResistance()).append("。");
                }
                if (registration.getStressResistance() != null) {
                    sowingGuide.append("抗逆性：").append(registration.getStressResistance()).append("。");
                }
                if (sowingGuide.length() > 0) {
                    varietyPublish.setSowingGuide(sowingGuide.toString());
                }

                String publishId = varietyPublishService.publishVariety(varietyPublish);
                data.put("publishId", publishId);
                data.put("publishNo", varietyPublish.getPublishNo());
            }

            return AjaxResult.success("审核完成", data);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询审核任务列表
     * 列表展示审核中的品种，包含申请号、品种名称、作物类型、提交单位
     * 支持按照品种、提交单位、审核状态进行条件筛选
     *
     * @param varietyName 品种名称
     * @param enterpriseName 提交单位
     * @param auditResult 审核结果
     * @return 查询结果
     */
    //@SaCheckPermission("seed:variety:audit:list")
    @GetMapping("/list")
    public TableDataInfo list(
            @RequestParam(required = false) String varietyName,
            @RequestParam(required = false) String enterpriseName,
            @RequestParam(required = false) Integer auditResult) {
        // 使用RuoYi框架提供的分页方法
        startPage();
        
        // 调用服务方法查询数据
        List<VarietyAuditTaskVO> list = varietyAuditService.queryAuditTaskList(varietyName, enterpriseName, auditResult);
        
        // 使用框架提供的方法格式化返回结果
        return getDataTable(list);
    }
    
    /**
     * 查询审核任务详情
     * 顶部为品种基础信息区：包括名称、作物类型、申请号、提交单位
     * 中部为审核操作区：包括审核结果、审核意见、审核人、审核时间
     * 底部为品种详细信息区：包括品种标识信息、技术性状信息、试验和性能数据、监管数据
     *
     * @param registrationId 登记ID
     * @return 查询结果
     */
    //@SaCheckPermission("seed:variety:audit:detail")
    @GetMapping("/detail/{registrationId}")
    public AjaxResult getAuditTaskDetail(@PathVariable String registrationId) {
        VarietyAuditTaskVO taskVO = varietyAuditService.queryAuditTaskDetail(registrationId);
        if (taskVO == null) {
            return AjaxResult.error("审核任务不存在");
        }
        return AjaxResult.success(taskVO);
    }

    /**
     * 根据审核ID查询审核记录详情
     *
     * @param auditId 审核ID
     * @return 查询结果
     */
    //@SaCheckPermission("seed:variety:audit:query")
    @GetMapping("/{auditId}")
    public AjaxResult getInfo(@PathVariable String auditId) {
        VarietyAudit audit = varietyAuditService.queryByAuditId(auditId);
        if (audit == null) {
            return AjaxResult.error("审核记录不存在");
        }
        return AjaxResult.success(audit);
    }

    /**
     * 查询待审核列表（备案状态为审核中的品种）
     *
     * @param varietyName 品种名称
     * @param enterpriseName 企业名称
     * @return 查询结果
     */
    //@SaCheckPermission("seed:variety:audit:pending")
    @GetMapping("/pending")
    public TableDataInfo pendingList(
            @RequestParam(required = false) String varietyName,
            @RequestParam(required = false) String enterpriseName) {
        // 使用RuoYi框架提供的分页方法
        startPage();
        
        // 查询备案状态为0（审核中）的品种登记
        List<VarietyRegistration> list = varietyRegistrationService.queryRegistrationList(varietyName, enterpriseName, "0");

        // 过滤出审核中的记录
        list.removeIf(item -> item.getRecordStatus() != 0);
        
        // 使用框架提供的方法格式化返回结果
        return getDataTable(list);
    }

    /**
     * 根据登记ID查询最新审核记录
     *
     * @param registrationId 登记ID
     * @return 查询结果
     */
    //@SaCheckPermission("seed:variety:audit:query")
    @GetMapping("/latest/{registrationId}")
    public AjaxResult getLatest(@PathVariable String registrationId) {
        VarietyAudit audit = varietyAuditService.queryLatestByRegistrationId(registrationId);
        if (audit == null) {
            return AjaxResult.error("暂无审核记录");
        }
        return AjaxResult.success(audit);
    }
}