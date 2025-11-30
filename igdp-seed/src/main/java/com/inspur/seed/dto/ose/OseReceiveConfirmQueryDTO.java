package com.inspur.seed.dto.ose;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * OSE接收确认查询DTO
 *
 * @author igdp
 */
@Data
public class OseReceiveConfirmQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 生产批次ID
     */
    private String breedSeedProduceBatchId;

    /**
     * 作物种类
     */
    private String cropType;

    /**
     * 品种名称
     */
    private String varietyName;

    /**
     * 查询起始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    /**
     * 查询结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 接收状态(PENDING/CONFIRMED)
     */
    private String receiveStatus;
}
