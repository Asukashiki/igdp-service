package com.inspur.agriculture.inventory.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class StockCheckListVO {
    private String checkId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkDate;

    private String warehouseId;
    private String warehouseName;

    private String checkerId;
    private String checkerName;

    private String checkStatus;
    private String checkRemark;

    private Integer totalItems;
    private Integer diffItems;
    private Integer surplusItems;
    private Integer lossItems;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date updateTime;
}
