package com.inspur.seed.mapper.invested;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.seed.domain.invested.BoaZoneReleaseMain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 鎶曞叆鍝佸垎鍙戜富琛∕apper鎺ュ彛
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@Mapper
public interface BoaZoneReleaseMainMapper extends BaseMapper<BoaZoneReleaseMain> {
    /**
     * 鑾峰彇鏈嚭搴撳垎鍙戝崟瀵瑰簲鎶曞叆鍝併€佺绫荤殑鎬婚渶姹傞噺
     *
     * @param inputId
     * @param releaseType
     * @return
     */
    BigDecimal getRequiredFromNotDeliveryInputRelease(@Param("inputId") String inputId, @Param("releaseType") String releaseType);
}

