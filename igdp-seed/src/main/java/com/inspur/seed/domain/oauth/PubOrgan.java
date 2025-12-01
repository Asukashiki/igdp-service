package com.inspur.seed.domain.oauth;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("oauth2_bsp.pub_organ")
public class PubOrgan implements Serializable{
    private static final long serialVersionUID = 1L;

    private String id;

    private String code;

    private String name;

    private String pinyin;

    private String icon;

    private String organType;

    private String shortName;

    private String regionCode;

    private String regionName;

    private Integer sortOrder;

    private String creator;

    private LocalDateTime createTime;

    private String lastEditor;

    private LocalDateTime lastTime;

    private String remark;

    private String status;

    private String appCode;

    private Integer childs;

    private String isBusiness;

    private String organLevel;

    private String type;

    private String shortCode;

    private String organPathCode;

    private LocalDateTime updateTime;

    private String submitSystem;

    private String orgNum;

    /**
     * 组织机构条线
     */
    private String organLine;

    /**
     * 统一社会信用代码
     */
    private String societyCode;

    private String orgParentIds;
}
