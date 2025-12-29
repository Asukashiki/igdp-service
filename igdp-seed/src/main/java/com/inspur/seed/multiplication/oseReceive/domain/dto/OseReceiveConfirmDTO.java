package com.inspur.seed.multiplication.oseReceive.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * OSE接收确认DTO
 *
 * @author igdp
 */
@Data
public class OseReceiveConfirmDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 确认操作人姓名
     */
    @NotBlank(message = "确认操作人姓名不能为空")
    private String confirmPeople;

    /**
     * 补充说明
     */
    private String remark;
}
