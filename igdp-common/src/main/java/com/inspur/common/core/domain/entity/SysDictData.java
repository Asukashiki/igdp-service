package com.inspur.common.core.domain.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import com.inspur.common.annotation.Excel;
import com.inspur.common.annotation.Excel.ColumnType;
import com.inspur.common.core.domain.BaseEntity;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * 字典数据表 sys_dict_data
 *
 * @author liyunlong
 */
@TableName("sys_dict_data")
@EqualsAndHashCode(callSuper = true)
@Data
public class SysDictData extends BaseEntity {

    /**
     * 字典编码
     */
    @TableId(type = IdType.ASSIGN_ID)
    // 字典编码
    @Excel(name = "Dictionary Code", cellType = ColumnType.NUMERIC)
    private String dictCode;

    /**
     * 字典排序
     */
    @OrderBy()
    // 字典排序
    @Excel(name = "Dictionary Sort", cellType = ColumnType.NUMERIC)
    private Long dictSort;

    /**
     * 字典标签
     */
    // 字典标签
    @Excel(name = "Dictionary Label")
    // 字典标签不能为空
    @NotBlank(message = "Dictionary label cannot be empty")
    // 字典标签长度不能超过100个字符
    @Size(min = 0, max = 100, message = "Dictionary label length cannot exceed 100 characters")
    private String dictLabel;

    /**
     * 字典标签对象（解析后的国际化对象）
     * 非数据库字段，用于前端直接使用
     */
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> dictLabelObject;

    /**
     * 字典键值
     */
    // 字典键值
    @Excel(name = "Dictionary Value")
    // 字典键值不能为空
    @NotBlank(message = "Dictionary key value cannot be empty")
    // 字典键值长度不能超过100个字符
    @Size(min = 0, max = 100, message = "Dictionary key value length cannot exceed 100 characters")
    private String dictValue;

    /**
     * 字典类型
     */
    // 字典类型
    @Excel(name = "Dictionary Type")
    // 字典类型不能为空
    @NotBlank(message = "Dictionary type cannot be empty")
    // 字典类型长度不能超过100个字符
    @Size(min = 0, max = 100, message = "Dictionary type length cannot exceed 100 characters")
    private String dictType;

    /**
     * 样式属性（其他样式扩展）
     */
    // 样式属性长度不能超过100个字符
    @Size(min = 0, max = 100, message = "Style attribute length cannot exceed 100 characters")
    private String cssClass;

    /**
     * 表格字典样式
     */
    private String listClass;

    /**
     * 是否默认（Y是 N否）
     */
    // 是否默认
    // 是否默认（Y是 N否）
    @Excel(name = "Is Default", readConverterExp = "Y=Yes,N=No")
    private String isDefault;

    /**
     * 状态（0正常 1停用）
     */
    // 状态
    // 状态（0正常 1停用）
    @Excel(name = "Status", readConverterExp = "0=Normal,1=Disabled")
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 实际值
     */
    @Excel(name = "实际值")
    @Size(min = 0, max = 200, message = "Actual value length cannot exceed 200 characters")
    private String actualValue;

    public static final String STATUS_VALID = "0";
    public static final String STATUS_INVALID = "1";

    /**
     * 获取解析后的字典标签对象
     * 如果 dictLabel 是 JSON 格式，则解析为 Map 返回
     */
    public Map<String, String> getDictLabelObject() {
        if (this.dictLabelObject != null) {
            return this.dictLabelObject;
        }
        if (this.dictLabel != null && this.dictLabel.startsWith("{")) {
            try {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                this.dictLabelObject = mapper.readValue(this.dictLabel,
                    new com.fasterxml.jackson.core.type.TypeReference<Map<String, String>>() {});
                return this.dictLabelObject;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
