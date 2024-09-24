package com.inspur.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 个人配置的快捷开始
 * @author liyunlong
 * @version 1.0
 * @ClassName SysUserFastItem
 * @date 2024/6/14 17:17
 */
@TableName("sys_user_fast_item")
@Setter
@Getter
public class SysUserFastItem {
    private String userId;

    private String itemId;

    private LocalDateTime createTime;
    /**
     * 排序号
     */
    private Integer sortNumber;
}
