package com.inspur.assets.monitor.domain.payload;

import lombok.Data;

/**
 * 普罗查询接口参数
 *
 * @author liyunlong
 * @version 1.0
 * @ClassName PrometheusQueryPayload
 * @date 2024/9/20 10:45
 */
@Data
public class PrometheusQueryPayload {

    private String query;

    private Long time;

    private Long start;

    private Long end;

    private Integer step;
}
