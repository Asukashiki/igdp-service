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
    @Excel(name = "字典编码", cellType = ColumnType.NUMERIC)
    private String dictCode;

    /**
     * 字典排序
     */
    @OrderBy()
    @Excel(name = "字典排序", cellType = ColumnType.NUMERIC)
    private Long dictSort;

    /**
     * 字典标签
     */
    @Excel(name = "字典标签")
    @NotBlank(message = "字典标签不能为空")
    @Size(min = 0, max = 100, message = "字典标签长度不能超过100个字符")
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
    @Excel(name = "字典键值")
    @NotBlank(message = "字典键值不能为空")
    @Size(min = 0, max = 100, message = "字典键值长度不能超过100个字符")
    private String dictValue;

    /**
     * 字典类型
     */
    @Excel(name = "字典类型")
    @NotBlank(message = "字典类型不能为空")
    @Size(min = 0, max = 100, message = "字典类型长度不能超过100个字符")
    private String dictType;

    /**
     * 样式属性（其他样式扩展）
     */
    @Size(min = 0, max = 100, message = "样式属性长度不能超过100个字符")
    private String cssClass;

    /**
     * 表格字典样式
     */
    private String listClass;

    /**
     * 是否默认（Y是 N否）
     */
    @Excel(name = "是否默认", readConverterExp = "Y=是,N=否")
    private String isDefault;

    /**
     * 状态（0正常 1停用）
     */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 备注
     */
    private String remark;

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
