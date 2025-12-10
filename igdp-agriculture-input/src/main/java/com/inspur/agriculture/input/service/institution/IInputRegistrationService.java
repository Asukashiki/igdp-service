package com.inspur.agriculture.input.service.institution;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.agriculture.input.domain.institution.dto.InputEnterpriseAddDTO;
import com.inspur.agriculture.input.domain.institution.dto.InputEnterprisePageDTO;
import com.inspur.agriculture.input.domain.institution.dto.InputEnterpriseSubmitDTO;
import com.inspur.agriculture.input.domain.institution.dto.InputEnterpriseUpdateDTO;
import com.inspur.agriculture.input.domain.institution.entity.InputEnterpriseInfo;
import com.inspur.agriculture.input.domain.institution.vo.InputEnterpriseDetailVO;
import com.inspur.agriculture.input.domain.institution.vo.InputEnterprisePageVO;


/**
 * 机构注册服务接口
 *
 * @author system
 */
public interface IInputRegistrationService extends IService<InputEnterpriseInfo> {

    /**
     * 新增机构注册申请
     *
     * @param dto 新增DTO
     * @return 机构ID
     */
    String addRegistration(InputEnterpriseAddDTO dto);

    /**
     * 修改机构注册申请
     *
     * @param dto 修改DTO
     */
    void updateRegistration(InputEnterpriseUpdateDTO dto);

    /**
     * 提交机构注册申请
     *
     * @param dto 提交DTO
     */
    void submitRegistration(InputEnterpriseSubmitDTO dto);

    /**
     * 查询机构注册申请详情
     *
     * @param id 机构ID
     * @return 详情VO
     */
    InputEnterpriseDetailVO getDetail(String id);

    /**
     * 分页查询机构注册申请
     *
     * @param dto 查询DTO
     * @return 分页结果
     */
    IPage<InputEnterprisePageVO> page(InputEnterprisePageDTO dto);

    /**
     * 删除机构注册申请
     *
     * @param id 机构ID
     */
    void deleteRegistration(String id);
}
