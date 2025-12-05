package com.inspur.agriculture.input.dto.inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 入库审核 DTO
 *
 * @author inspur
 * @date 2025-12-03
 */
@Data
public class InboundAuditDTO {

    /** 审核状态：approved-通过/rejected-驳回 */
    @NotBlank(message = "审核状态不能为空")
    private String auditStatus;

    /** 审核人 */
    @NotBlank(message = "审核人不能为空")
    private String auditUser;

    /** 审核时间 */
    @NotNull(message = "审核时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditTime;

    /** 备注 */
    private String remark;
}
