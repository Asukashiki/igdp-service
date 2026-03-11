package com.inspur.agriculture.inventory.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inspur.agriculture.inventory.domain.StockCheck;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class StockCheckDetailVO {
    private String checkId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkDate;

    private String warehouseId;
    private String warehouseName;

    private String checkerId;
    private String checkerName;

    private String checkStatus;
    private String checkRemark;

    private String reviewerId;
    private String reviewerName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reviewDate;

    private String reviewOpinion;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date updateTime;

    private List<StockCheck> details;
}
