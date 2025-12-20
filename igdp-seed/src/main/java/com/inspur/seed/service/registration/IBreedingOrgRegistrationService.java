package com.inspur.seed.service.registration;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.registration.BreedingOrgRegistration;
import com.inspur.seed.dto.registration.BreedingOrgRegistrationDTO;
import com.inspur.seed.vo.registration.BreedingOrgRegistrationDetailVO;
import com.inspur.common.core.domain.R;

import java.util.Map;

/**
 * 繁殖机构注册申请 Service 接口
 *
 * @author igdp
 */
public interface IBreedingOrgRegistrationService extends IService<BreedingOrgRegistration> {

    /**
     * 提交/修改注册申请
     */
    R<String> submitRegistration(BreedingOrgRegistrationDTO dto);

    /**
     * 查询注册申请列表
     */
    Map<String, Object> queryList(Map<String, Object> params);

    /**
     * 获取注册申请详情 (含审核历史)
     */
    BreedingOrgRegistrationDetailVO getDetail(String id);

    /**
     * 审核注册申请
     */
    R<String> auditRegistration(String id, Integer auditResult, String auditComment);

    /**
     * 检查用户名是否已存在 (含注册表和系统用户表)
     */
    boolean checkUsernameUnique(String username, String id);
}
