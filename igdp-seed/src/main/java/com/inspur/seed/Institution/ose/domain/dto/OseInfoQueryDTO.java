package com.inspur.seed.Institution.ose.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * OSE基础信息查询DTO
 *
 * @author igdp
 */
@Data
public class OseInfoQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * OSE名称
     */
    private String oseName;

    /**
     * 查询起始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    /**
     * 查询结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}
