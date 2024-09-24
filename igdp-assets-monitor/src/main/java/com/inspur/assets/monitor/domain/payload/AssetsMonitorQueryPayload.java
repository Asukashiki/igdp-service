package com.inspur.assets.monitor.domain.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 资产监控数据查询接口所需参数载体
 *
 * @author liyunlong
 * @version 1.0
 * @ClassName PrometheusQueryPayload
 * @date 2024/9/20 9:15
 */
@Data
public class AssetsMonitorQueryPayload {

    /**
     * 指标标签
     */
    @NotBlank(message = "查询指标不能为空")
    private String label;

    /**
     * 主机/实例列表
     * ip/资产编号等，与夜莺中的探针ident对应上
     */
    private List<String> identList;

    private Integer step;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
