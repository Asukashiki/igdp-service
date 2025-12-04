package com.inspur.seed.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.seed.domain.dto.registration.*;
import com.inspur.seed.domain.entity.OrgEnterpriseInfo;
import com.inspur.seed.domain.vo.registration.EnterpriseDetailVO;
import com.inspur.seed.domain.vo.registration.EnterprisePageVO;

/**
 * 机构注册服务接口
 *
 * @author system
 */
public interface IRegistrationService extends IService<OrgEnterpriseInfo> {

    /**
     * 新增机构注册申请
     *
     * @param dto 新增DTO
     * @return 机构ID
     */
    String addRegistration(EnterpriseAddDTO dto);

    /**
     * 修改机构注册申请
     *
     * @param dto 修改DTO
     */
    void updateRegistration(EnterpriseUpdateDTO dto);

    /**
     * 提交机构注册申请
     *
     * @param dto 提交DTO
     */
    void submitRegistration(EnterpriseSubmitDTO dto);

    /**
     * 查询机构注册申请详情
     *
     * @param id 机构ID
     * @return 详情VO
     */
    EnterpriseDetailVO getDetail(String id);

    /**
     * 分页查询机构注册申请
     *
     * @param dto 查询DTO
     * @return 分页结果
     */
    IPage<EnterprisePageVO> page(EnterprisePageDTO dto);

    /**
     * 删除机构注册申请
     *
     * @param id 机构ID
     */
    void deleteRegistration(String id);
}
