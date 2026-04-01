package com.inspur.seed.dto.invested;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 鎶曞叆鍝佸垎鍙戝崟DTO锛堢敤浜庢柊澧炲拰缂栬緫锛?
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@Setter
@Getter
public class BoaZoneReleaseDTO {

    /**
     * 涓婚敭锛堢紪杈戞椂蹇呭～锛?
     */
    private String id;

    /**
     * 鍒嗗彂鍗曞悕绉?
     */
    private String releaseName;

    /**
     * zoneId
     */
    private String zoneId;

    /**
     * 鍒嗗彂瀵硅薄ID锛圲nionID锛?
     */
    private String targetId;

    /**
     * 鍒嗗彂瀵硅薄鏀惰幏鍦板潃
     */
    private String targetAddress;

    /**
     * 鍒嗗彂瀵硅薄鑱旂郴浜?
     */
    private String targetContact;

    /**
     * 鍒嗗彂瀵硅薄鑱旂郴鐢佃瘽
     */
    private String targetPhone;

    /**
     * 鍒嗗彂骞村害
     */
    private Integer releaseYear;

    /**
     * 鍒嗗彂鏃ユ湡
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @JsonProperty("source_organization")
    @JsonAlias("sourceOrganization")
    private String sourceOrganization;

    @JsonProperty("allocation_type")
    @JsonAlias("allocationType")
    private String allocationType;

    @JsonProperty("approved_by")
    @JsonAlias("approvedBy")
    private String approvedBy;

    @JsonProperty("approval_date")
    @JsonAlias("approvalDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate approvalDate;

    @JsonProperty("minute_file")
    @JsonAlias("minuteFile")
    private String minuteFile;

    @JsonProperty("minute_file_name")
    @JsonAlias("minuteFileName")
    private String minuteFileName;

    /**
     * 鍒嗗彂浜?
     */
    private String releaseBy;

    /**
     * 瀹℃牳鏃ユ湡
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auditDate;

    /**
     * 瀹℃牳浜?
     */
    private String auditBy;

    /**
     * 鍒嗗彂鏈烘瀯锛圤SE锛?
     */
    private String releaseOrg;

    /**
     * 鍒嗗彂绫诲瀷锛圤SE_TO_UNION: OSE鍒嗗彂鍒癠nion, UNION_TO_WOREDA: Union鍒嗗彂鍒癢oreda锛?
     */
    private String releaseType;

    /**
     * 鍒嗗彂鏄庣粏鍒楄〃
     */
    private List<BoaZoneReleaseDetailDTO> details;
}

