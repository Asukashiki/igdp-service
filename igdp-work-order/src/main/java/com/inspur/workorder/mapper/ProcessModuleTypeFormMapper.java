package com.inspur.workorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.common.annotation.DataSource;
import com.inspur.common.enums.DataSourceType;
import com.inspur.workorder.domain.ProcessModuleTypeForm;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * @author liyunlong
 * */
@Mapper
@DataSource(value = DataSourceType.SLAVE)
public interface ProcessModuleTypeFormMapper extends BaseMapper<ProcessModuleTypeForm> {
}
