package com.inspur.seed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.entity.OrgEnterpriseInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 机构基础信息Mapper接口
 *
 * @author system
 */
@Mapper
@Repository("orgEnterpriseInfoMapper")
public interface OrgEnterpriseInfoMapper extends BaseMapper<OrgEnterpriseInfo> {

}
