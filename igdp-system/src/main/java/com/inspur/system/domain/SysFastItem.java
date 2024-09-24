package com.inspur.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 快速开始项目
 *
 * @author liyunlong
 * @version 1.0
 * @ClassName SysFastItem
 * @date 2024/6/14 17:12
 */
@TableName("sys_fast_item")
@Setter
@Getter
public class  SysFastItem extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 标签
     */
    private String icon;

    /**
     * 所属应用id
     */
    private String appId;

    /**
     * 跳转链接
     */
    private String link;

    /**
     * 路由地址
     * 与link二选一
     * 优先以路由打开
     * */
    private String routePath;

    /**
     * 类型
     * inner 内部应用；out 外部应用
     */
    private String type;

    /**
     * 打开方式
     * tab 内部tab打开；dialog 弹窗对话筐；window 新浏览器tab页打开
     * */
    private String openType;

    /**
     * 状态
     * 0正常；1停用
     */
    private String status;

    private String userId;

    private String deptId;

    /**
     * 排序号
     */
    private Integer sortNumber;
}
