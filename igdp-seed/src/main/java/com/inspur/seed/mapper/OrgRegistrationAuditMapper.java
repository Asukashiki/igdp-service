package com.inspur.seed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.entity.OrgRegistrationAudit;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 机构注册审核记录Mapper接口
 *
 * @author system
 */
@Mapper
@Repository("orgRegistrationAuditMapper")
public interface OrgRegistrationAuditMapper extends BaseMapper<OrgRegistrationAudit> {

}
