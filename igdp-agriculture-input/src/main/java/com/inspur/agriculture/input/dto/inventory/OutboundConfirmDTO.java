package com.inspur.agriculture.input.dto.inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 出库确认 DTO
 *
 * @author inspur
 * @date 2025-12-03
 */
@Data
public class OutboundConfirmDTO {

    /** 出库时间 */
    @NotNull(message = "出库时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date outboundTime;

    /** 经办人 */
    @NotBlank(message = "经办人不能为空")
    private String operator;
}
