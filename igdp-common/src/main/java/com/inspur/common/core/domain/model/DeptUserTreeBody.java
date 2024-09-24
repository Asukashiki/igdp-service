package com.inspur.common.core.domain.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门用户树结构载体
 * @author liyunlong
 * @version 1.0
 * @ClassName DeptUserTreeBody
 * @date 2024/5/7 16:38
 */
@Data
public class DeptUserTreeBody {
    private String id;

    private String name;

    private String phoneNumber;

    private String parentId;

    private String type;

    List<DeptUserTreeBody> children = new ArrayList<>();
}
