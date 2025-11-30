package com.inspur.seed.dto.ose;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * OSE基础信息DTO
 *
 * @author igdp
 */
@Data
public class OseInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * OSE行政编码
     */
    @NotBlank(message = "OSE行政编码不能为空")
    private String oseCode;

    /**
     * OSE名称
     */
    @NotBlank(message = "OSE名称不能为空")
    private String oseName;

    /**
     * 详细地址
     */
    @NotBlank(message = "详细地址不能为空")
    private String location;

    /**
     * 行政区划编码
     */
    @NotBlank(message = "行政区划编码不能为空")
    private String regionCode;

    /**
     * 联系人姓名
     */
    @NotBlank(message = "联系人姓名不能为空")
    private String contactName;

    /**
     * 联系人电话
     */
    @NotBlank(message = "联系人电话不能为空")
    @Pattern(regexp = "^251\\d{9}$", message = "联系电话格式不正确，应为251开头的12位数字")
    private String contactNumber;
}
