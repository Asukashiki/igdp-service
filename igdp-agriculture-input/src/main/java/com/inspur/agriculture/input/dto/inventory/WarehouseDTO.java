package com.inspur.agriculture.input.dto.inventory;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 仓库 DTO
 *
 * @author inspur
 * @date 2025-11-26
 */
@Data
public class WarehouseDTO {

    /** 仓库ID (更新时使用) */
    private Long warehouseId;

    /** 仓库名称 */
    @NotBlank(message = "仓库名称不能为空")
    private String warehouseName;

    /** 仓库类型: normal-普通仓库/cold-冷藏仓库/dangerous-危险品仓库 */
    @NotBlank(message = "仓库类型不能为空")
    private String warehouseType;

    /** 仓库位置 */
    @NotBlank(message = "仓库位置不能为空")
    private String location;

    /** 仓库容量 */
    @NotNull(message = "仓库容量不能为空")
    @DecimalMin(value = "0.01", message = "仓库容量必须大于0")
    private BigDecimal capacity;

    private BigDecimal warehouseArea;

    /** 状态: 0-停用/1-启用 */
    private String status;

    /** 联系人 */
    private String contactPerson;

    /** 联系电话 */
    private String contactPhone;

    /** 备注 */
    private String remark;

    private String organName;

    private String organCode;

    private String siteCertificate;
}
