package com.inspur.farmland.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import com.baomidou.mybatisplus.annotation.TableField;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DA信息实体类
 *
 * @author inspur
 */
@TableName("t_da_info")
@Setter
@Getter
public class DaInfo extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * DA编码（业务主键）
     */
    private String daId;

    /**
     * DA姓名
     */
    private String daName;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 性别：M-男 F-女
     */
    private String gender;

    /**
     * 出生日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 州代码
     */
    private String regionCode;

    /**
     * 区代码
     */
    private String zoneCode;

    /**
     * 镇代码
     */
    private String woredaCode;

    /**
     * 负责的村代码（多个用逗号分隔）
     */
    private String kebeleCodes;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 登录账号
     */
    private String account;

    /**
     * 登录密码（加密存储）
     */
    private String password;

    /**
     * 账号状态：1-启用 0-禁用
     */
    private String accountStatus;

    /**
     * 数据状态：1-正常 0-删除
     */
    private String status;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建人姓名
     */
    private String createByName;

    /**
     * 创建机构代码
     */
    private String createOrg;

    /**
     * 创建机构名称
     */
    private String createOrgName;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新人姓名
     */
    private String updateByName;

    private String remark;

    /**
     * DA所属农民数量
     */
    @TableField(exist = false)
    private Long farmerCount;

    /**
     * DA所属土地数量
     */
    @TableField(exist = false)
    private Long landCount;

    /**
     * DA所属土地面积
     */
    @TableField(exist = false)
    private BigDecimal landArea;

}
