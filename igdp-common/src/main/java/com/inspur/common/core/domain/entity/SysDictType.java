package com.inspur.common.core.domain.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.inspur.common.annotation.Excel;
import com.inspur.common.annotation.Excel.ColumnType;
import com.inspur.common.core.domain.BaseEntity;

/**
 * 字典类型表 sys_dict_type
 *
 * @author liyunlong
 */
@TableName("sys_dict_type")
@Setter
@Getter
@ToString
public class SysDictType extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 字典主键
     */
    @Excel(name = "字典主键", cellType = ColumnType.NUMERIC)
    @TableId(type = IdType.ASSIGN_ID)
    private String dictId;

    /**
     * 字典名称
     */
    // 字典名称不能为空
    @NotBlank(message = "Dictionary name cannot be empty")
    // 字典类型名称长度不能超过100个字符
    @Size(min = 0, max = 100, message = "Dictionary type name length cannot exceed 100 characters")
    @Excel(name = "字典名称")
    private String dictName;

    /**
     * 字典类型
     */
    // 字典类型不能为空
    @NotBlank(message = "Dictionary type cannot be empty")
    // 字典类型长度不能超过100个字符
    @Size(min = 0, max = 100, message = "Dictionary type length cannot exceed 100 characters")
    // 字典类型必须以字母开头，且只能为（小写字母，数字，下滑线）
    @Pattern(regexp = "^[a-z][a-z0-9_]*$", message = "Dictionary type must start with a letter and can only contain (lowercase letters, numbers, underscores)")
    @Excel(name = "字典类型")
    private String dictType;

    /**
     * 状态（0正常 1停用）
     */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public static final String STATUS_VALID = "0";
    public static final String STATUS_INVALID = "1";

}
