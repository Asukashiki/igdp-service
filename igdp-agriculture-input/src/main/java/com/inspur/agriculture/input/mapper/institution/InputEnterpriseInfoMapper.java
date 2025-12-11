package com.inspur.agriculture.input.mapper.institution;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.institution.entity.InputEnterpriseInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 机构基础信息Mapper接口
 *
 * @author system
 */
@Mapper
@Repository("inputEnterpriseInfoMapper")
public interface InputEnterpriseInfoMapper extends BaseMapper<InputEnterpriseInfo> {

}
