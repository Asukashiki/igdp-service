package com.inspur.seed.dto.invested;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 农民分发DTO
 * 用于新增和编辑农民分发单
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@Setter
@Getter
public class InputReleaseFarmerDTO {

    /**
     * 主键（编辑时必填）
     */
    private String id;

    /**
     * 农民ID
     */
    private String farmerId;

    /**
     * 农民姓名
     */
    private String farmerName;

    /**
     * 联系电话
     */
    private String farmerPhone;

    /**
     * 农民地址
     */
    private String farmerAddress;

    /**
     * 领用状态（未领用/已领用）
     */
    private String receiveStatus;

    /**
     * 分发年度
     */
    private Integer releaseYear;

    /**
     * 分发日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    /**
     * 分发人
     */
    private String releaseBy;

    /**
     * 审核日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auditDate;

    /**
     * 审核人
     */
    private String auditBy;

    /**
     * 分发机构（Woreda）
     */
    private String releaseOrg;

    /**
     * 分发明细列表
     */
    private List<InputReleaseFarmerDetailDTO> details;
}