package com.inspur.common.domain;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BaseLLmEntity {
    @TableId
    private Integer id;
    private String creator;
    private DateTime createdTime;
    private String updater;
    private DateTime updatedTime;
}
