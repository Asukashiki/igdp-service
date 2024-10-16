package com.inspur.transformation.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * @author lijieming
 * @date 2024/9/25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CommonCommitParams {
    private String url;
    private Map<String,Object> params;
}
