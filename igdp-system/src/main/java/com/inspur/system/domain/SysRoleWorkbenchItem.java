package com.inspur.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName SysRoleWorkbenchItem
 * @date 2024/6/11 9:35
 */
@TableName("sys_role_workbench_item")
@Setter
@Getter
public class SysRoleWorkbenchItem {
    private String roleId;

    private Long itemId;

    private LocalDateTime createTime;

    private String createBy;

    private String userId;
}
