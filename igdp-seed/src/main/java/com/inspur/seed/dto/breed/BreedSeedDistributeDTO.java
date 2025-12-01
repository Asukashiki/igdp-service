package com.inspur.seed.dto.breed;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Breeder Seed 分发数据DTO
 *
 * @author igdp
 */
@Data
public class BreedSeedDistributeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * OSE ID
     */
    @NotBlank(message = "OSE ID不能为空")
    private String oseId;

    /**
     * 分发时间
     */
    @NotNull(message = "分发时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    /**
     * 分发操作人姓名
     */
    @NotBlank(message = "分发操作人姓名不能为空")
    private String people;

    /**
     * 种子机构名称(固定值)
     */
    @NotBlank(message = "种子机构名称不能为空")
    private String organ;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 分发明细列表
     */
    @NotEmpty(message = "分发明细不能为空")
    @Valid
    private List<DistributeDetailItem> detailList;

    @Data
    public static class DistributeDetailItem implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 生产批次ID
         */
        @NotBlank(message = "生产批次ID不能为空")
        private String breedSeedProduceBatchId;

        /**
         * 分发数量
         */
        @NotNull(message = "分发数量不能为空")
        private java.math.BigDecimal distributeQuantity;
    }
}
