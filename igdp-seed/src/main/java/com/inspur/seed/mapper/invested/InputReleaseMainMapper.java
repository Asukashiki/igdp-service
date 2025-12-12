package com.inspur.seed.mapper.invested;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.invested.InputReleaseMain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 投入品分发主表Mapper接口
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@Mapper
public interface InputReleaseMainMapper extends BaseMapper<InputReleaseMain> {
    /**
     * 获取未出库分发单对应投入品、种类的总需求量
     *
     * @param inputId
     * @param releaseType
     * @return
     */
    BigDecimal getRequiredFromNotDeliveryInputRelease(@Param("inputId") String inputId, @Param("releaseType") String releaseType);
}
