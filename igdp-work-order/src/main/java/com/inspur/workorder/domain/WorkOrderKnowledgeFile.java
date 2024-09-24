package com.inspur.workorder.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName WorkOrderKnowledgeFile
 * @date 2024/7/2 19:26
 */
@TableName("work_order_knowledge_file")
@Setter
@Getter
public class WorkOrderKnowledgeFile {
    @TableId(type = IdType.ASSIGN_ID)
    private String fileId;

    /**
     * 知识库id
     * */
    private String baseId;

    /**
     * 原文件名
     * */
    private String originalFileName;

    private String filePath;

    private String fileName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
