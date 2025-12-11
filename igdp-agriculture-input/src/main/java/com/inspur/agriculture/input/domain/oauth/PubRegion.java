package com.inspur.agriculture.input.domain.oauth;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@TableName("oauth2_bsp.pub_region")
public class PubRegion implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;

    private String name;

    private String shortCode;

    private String grade;

    private String map;

    private String coordX;

    private String coordY;

    private String parentCode;

    private Integer sortOrder;

    private String creator;

    private LocalDate createTime;

    private String lastEditor;

    private LocalDate lastTime;

    private String remark;

    private String status;

    private String type;

    private Integer childs;

    private String regionCode;

    private String treeCode;

    private String regParentIds;
}
