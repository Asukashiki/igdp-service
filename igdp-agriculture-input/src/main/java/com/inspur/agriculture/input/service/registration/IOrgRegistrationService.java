package com.inspur.agriculture.input.service.registration;

import com.inspur.agriculture.input.domain.registration.OrgRegistration;
import com.inspur.agriculture.input.dto.registration.AuditDTO;
import com.inspur.agriculture.input.dto.registration.OrgRegistrationDTO;
import com.inspur.agriculture.input.vo.registration.OrgRegistrationDetailVO;

import java.util.Map;

/**
 * 机构注册申请服务接口
 *
 * @author igdp
 */
public interface IOrgRegistrationService {

    /**
     * 提交/修改注册申请
     *
     * @param dto 注册申请DTO
     * @return 注册申请ID
     */
    String submitRegistration(OrgRegistrationDTO dto);

    /**
     * 审核操作
     *
     * @param dto 审核DTO
     */
    void audit(AuditDTO dto);

    /**
     * 分页查询申请列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Map<String, Object> listRegistrations(OrgRegistration query, Integer page, Integer pageSize);

    /**
     * 获取申请详情（含审核历史）
     *
     * @param id 注册申请ID
     * @return 详情VO
     */
    OrgRegistrationDetailVO getDetail(String id);

    /**
     * 检查用户名是否唯一
     *
     * @param username 用户名
     * @param excludeId 排除的ID（修改时排除自身）
     * @return true-唯一，false-已存在
     */
    boolean checkUsernameUnique(String username, String excludeId);
}
