package com.inspur.seed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.entity.OrgEnterpriseLocation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 机构位置运营信息Mapper接口
 *
 * @author system
 */
@Mapper
@Repository("orgEnterpriseLocationMapper")
public interface OrgEnterpriseLocationMapper extends BaseMapper<OrgEnterpriseLocation> {

}
