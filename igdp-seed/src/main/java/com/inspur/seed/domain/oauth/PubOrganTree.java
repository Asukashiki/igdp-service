package com.inspur.seed.domain.oauth;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@TableName("oauth2_bsp.pub_organ_tree")
public class PubOrganTree implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String orgCode;
    private String parentCode;
    private String viewCode;
    private String isLeaf;
    private Integer sortOrder;
    private String creator;
    private LocalDate createTime;
    private String remark;
    private String status;
    private String pathParentCode;
}
