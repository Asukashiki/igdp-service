package com.inspur.seed.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 物联网传感器删除DTO
 *
 * @author igdp
 * @date 2025-11-30
 */
@Data
public class IotSensorDeleteDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID列表
     */
    @NotEmpty(message = "请选择要删除的数据")
    private List<String> dataIds;
}
