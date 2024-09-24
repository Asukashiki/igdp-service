package com.inspur.common.core.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 下拉选择
 * @author liyunlong
 * @version 1.0
 * @ClassName SelectEntity
 * @date 2024/7/3 10:36
 */
@Setter
@Getter
public class SelectEntity {
    /**
     * 键值
     * */
    private String value;

    /**
     * 展示内容
     * */
    private String label;

    /**
     * 父类value
     * */
    private String parentValue;

    private String number;

    private List<SelectEntity> children;
}
