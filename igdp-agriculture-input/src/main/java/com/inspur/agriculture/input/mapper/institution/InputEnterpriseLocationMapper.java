package com.inspur.agriculture.input.mapper.institution;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.institution.entity.InputEnterpriseLocation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 机构位置运营信息Mapper接口
 *
 * @author system
 */
@Mapper
@Repository("inputEnterpriseLocationMapper")
public interface InputEnterpriseLocationMapper extends BaseMapper<InputEnterpriseLocation> {

}
