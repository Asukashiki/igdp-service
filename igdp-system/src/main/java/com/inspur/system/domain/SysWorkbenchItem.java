package com.inspur.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 工作台内容模块
 *
 * @author liyunlong
 * @version 1.0
 * @ClassName SysWorkbench1
 * @date 2024/6/10 16:48
 */
@TableName("sys_workbench_item")
@Setter
@Getter
public class SysWorkbenchItem extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @NotNull(message = "名称不能为空")
    private String name;

    /**
     * 编号
     */
    @NotNull(message = "编码不能为空")
    private String code;

    /**
     * 描述
     */
    private String description;

    private String userId;

    private String deptId;
}
