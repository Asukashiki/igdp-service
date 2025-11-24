package com.inspur.agriculture.input.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.agriculture.input.domain.AgriInput;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 农业投入品Mapper接口
 *
 * @author igdp
 */
public interface AgriInputMapper extends BaseMapper<AgriInput> {

    /**
     * 查询投入品列表（含特性信息）
     *
     * @param agriInput 投入品
     * @return 投入品集合
     */
    List<AgriInput> selectInputListWithProperties(AgriInput agriInput);

    /**
     * 根据ID查询投入品详情（含特性信息）
     *
     * @param inputId 投入品ID
     * @return 投入品
     */
    AgriInput selectInputByIdWithProperties(@Param("inputId") Long inputId);
}
