package com.inspur.agriculture.input.mapper.institution;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.institution.entity.InputEnterpriseLicense;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 机构许可证件Mapper接口
 *
 * @author system
 */
@Mapper
@Repository("inputEnterpriseLicenseMapper")
public interface InputEnterpriseLicenseMapper extends BaseMapper<InputEnterpriseLicense> {

}
