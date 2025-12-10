package com.inspur.seed.dto.invested;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 投入品分发单DTO（用于新增和编辑）
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@Setter
@Getter
public class InputReleaseDTO {

    /**
     * 主键（编辑时必填）
     */
    private String id;

    /**
     * 分发单名称
     */
    private String releaseName;

    /**
     * zoneId
     */
    private String zoneId;

    /**
     * 分发对象ID（UnionID）
     */
    private String targetId;

    /**
     * 分发对象收获地址
     */
    private String targetAddress;

    /**
     * 分发对象联系人
     */
    private String targetContact;

    /**
     * 分发对象联系电话
     */
    private String targetPhone;

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
     * 分发机构（OSE）
     */
    private String releaseOrg;

    /**
     * 分发类型（OSE_TO_UNION: OSE分发到Union, UNION_TO_WOREDA: Union分发到Woreda）
     */
    private String releaseType;

    /**
     * 分发明细列表
     */
    private List<InputReleaseDetailDTO> details;
}
