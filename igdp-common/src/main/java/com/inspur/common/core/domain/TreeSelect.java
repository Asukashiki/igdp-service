package com.inspur.common.core.domain;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inspur.common.core.domain.entity.SysDept;
import com.inspur.common.core.domain.entity.SysMenu;
import com.inspur.common.core.domain.model.DeptUserTreeBody;
import lombok.Getter;
import lombok.Setter;

/**
 * TreeSelect树结构实体类
 *
 * @author liyunlong
 */
@Setter
@Getter
public class TreeSelect implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 节点ID
     */
    private String id;

    /**
     * 节点名称
     */
    private String label;

    /**
     * 节点类型
     */
    private String type;

    private String phoneNumber;

    private String parentId;

    private String idPath;

    /**
     * 子节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelect> children;

    public TreeSelect() {

    }

    public TreeSelect(SysDept dept) {
        this.id = dept.getDeptId();
        this.label = dept.getDeptName();
        this.phoneNumber = dept.getPhone();
        this.parentId = dept.getParentId();
        this.children = dept.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
        this.type = "dept";
    }

    public TreeSelect(SysMenu menu) {
        this.id = menu.getMenuId();
        this.label = menu.getMenuName();
        this.children = menu.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
        if (!this.children.isEmpty()) {
            this.type = "catalogue";
        } else {
            this.type = "node";
        }
    }

    public TreeSelect(DeptUserTreeBody treeBody) {
        this.id = treeBody.getId();
        this.label = treeBody.getName();
        this.phoneNumber = treeBody.getPhoneNumber();
        this.children = treeBody.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
        this.type = treeBody.getType();
    }

    public TreeSelect(SelectEntity selectEntity) {
        this.id = selectEntity.getValue();
        this.label = selectEntity.getLabel();
        this.children = selectEntity.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }

}
