package com.inspur.agriculture.input.mapper.institution;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.institution.entity.InputRegistrationAudit;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 机构注册审核记录Mapper接口
 *
 * @author system
 */
@Mapper
@Repository("inputRegistrationAuditMapper")
public interface InputRegistrationAuditMapper extends BaseMapper<InputRegistrationAudit> {

}
