package com.inspur.seed.domain.invested;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 鎶曞叆鍝佸垎鍙戜富琛ㄥ疄浣撶被
 *
 * @author igdp-seed
 * @date 2025-12-03
 */
@TableName("t_boa_zone_release_main")
@Setter
@Getter
public class BoaZoneReleaseMain extends BaseEntity {

    /**
     * 涓婚敭锛圲UID锛?
     */
    @TableId
    private String id;

    /**
     * 鍒嗗彂鍗曠紪鍙凤紙绯荤粺鐢熸垚锛?
     */
    private String releaseId;

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
    private String releaseYear;

    /**
     * 鍒嗗彂鏃ユ湡
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @TableField("source_organization")
    @JsonProperty("source_organization")
    private String sourceOrganization;

    @TableField("allocation_type")
    @JsonProperty("allocation_type")
    private String allocationType;

    @TableField("approved_by")
    @JsonProperty("approved_by")
    private String approvedBy;

    @TableField("approval_date")
    @JsonProperty("approval_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate approvalDate;

    @TableField("minute_file")
    @JsonProperty("minute_file")
    private String minuteFile;

    @TableField("minute_file_name")
    @JsonProperty("minute_file_name")
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
     * 鍒嗗彂鍗曠姸鎬侊細宸插垎鍙慸istributed銆佸凡瀹屾垚completed銆佹湭鍑哄簱notDelivery
     */
    private String status;

    /**
     * 鎿嶄綔浜?
     */
    private String operateBy;

    /**
     * 鎿嶄綔鏃堕棿
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operateTime;

    /**
     * 閫昏緫鍒犻櫎鏍囪瘑(0=鏈垹闄?1=宸插垹闄?
     */
    @TableLogic
    private Integer isDeleted;
}

