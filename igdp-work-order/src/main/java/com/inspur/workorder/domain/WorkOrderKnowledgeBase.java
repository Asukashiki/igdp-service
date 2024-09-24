package com.inspur.workorder.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.annotation.Excel;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName WorkOrderKnowlageBase
 * @date 2024/7/2 16:11
 */
@TableName("work_order_knowledge_base")
@Setter
@Getter
@ToString
public class WorkOrderKnowledgeBase extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 知识库id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /***
     * 知识主题
     */
    @Excel(name = "问题描述", prompt = "问题描述")
    private String subject;
    /***
     * 解决方式
     */
    @Excel(name = "解决方式", prompt = "解决方式")
    private String solution;
    /***
     * 知识分类
     */
    private String classification;
    /***
     * 知识分类名称
     */
    @Excel(name = "知识分类", prompt = "知识分类")
    private String classificationName;
    /***
     * 所属系统
     */
    private String system;
    /***
     * 创建时间
     */

    private String baseCreateTime;
    /***
     * 更新时间
     */
    private String baseUpdateTime;
    /***
     * 创建人
     */
    @Excel(name = "发布人", type = Excel.Type.EXPORT)
    private String createBy;
    /***
     * 审核状态
     */
    private String auditStatus;
    /***
     * 更新人
     */
    private String updateBy;
    /***
     * 状态
     * 0有效；1无效；
     */
    private String status;
    /***
     * 编码
     */
    private String code;
    /***
     * 附件地址
     */
    private String attachments;
    @Excel(name = "发布时间", type = Excel.Type.EXPORT)
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    /***
     * 所属系统名称
     */
    private String systemName;

    /***
     * 关联资产
     */
    private String assetsInvolve;
    private transient List<WorkOrderKnowledgeFile> fileList;
}
