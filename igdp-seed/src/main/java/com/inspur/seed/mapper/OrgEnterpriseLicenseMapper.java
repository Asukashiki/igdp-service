package com.inspur.seed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.entity.OrgEnterpriseLicense;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 机构许可证件Mapper接口
 *
 * @author system
 */
@Mapper
@Repository("orgEnterpriseLicenseMapper")
public interface OrgEnterpriseLicenseMapper extends BaseMapper<OrgEnterpriseLicense> {

}
