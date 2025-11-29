package com.inspur.seed.domain.dto;

import com.inspur.seed.domain.UnionInfo;
import com.inspur.seed.domain.UnionLicenseInfo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Union注册申请DTO
 *
 * @author system
 */
@Setter
@Getter
public class UnionRegistrationDTO {

    /**
     * Union基本信息
     */
    @NotNull(message = "Union基本信息不能为空")
    @Valid
    private UnionInfo unionInfo;

    /**
     * Union许可信息
     */
    @NotNull(message = "Union许可信息不能为空")
    @Valid
    private UnionLicenseInfo unionLicenseInfo;
}
