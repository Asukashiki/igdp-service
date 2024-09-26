package com.inspur.transformation.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class CommonCommitParams {
    private String url;
    private Map<String,Object> params;
}
