-- ----------------------------
-- 反馈表
-- ----------------------------
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback` (
    `feedback_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '反馈ID',
    `feedback_no` VARCHAR(50) NOT NULL COMMENT '反馈编号',
    `feedback_type` CHAR(1) NOT NULL COMMENT '反馈类型(0-投诉/1-建议/2-咨询/3-故障报告/4-其他)',
    `title` VARCHAR(200) NOT NULL COMMENT '反馈标题',
    `content` TEXT NOT NULL COMMENT '反馈内容',
    `attachments` VARCHAR(1000) DEFAULT NULL COMMENT '附件(多个用逗号分隔)',
    `user_id` VARCHAR(64) NOT NULL COMMENT '用户ID',
    `user_type` CHAR(1) DEFAULT '0' COMMENT '用户类型(0-供应商/1-采购商/2-其他)',
    `contact_name` VARCHAR(100) DEFAULT NULL COMMENT '联系人姓名',
    `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `contact_email` VARCHAR(100) DEFAULT NULL COMMENT '联系邮箱',
    `priority` CHAR(1) DEFAULT '0' COMMENT '优先级(0-低/1-中/2-高/3-紧急)',
    `status` CHAR(1) NOT NULL DEFAULT '0' COMMENT '状态(0-待处理/1-处理中/2-已完成/3-已关闭)',
    `handler_id` VARCHAR(64) DEFAULT NULL COMMENT '处理人ID',
    `handle_time` DATETIME DEFAULT NULL COMMENT '处理时间',
    `handle_result` VARCHAR(500) DEFAULT NULL COMMENT '处理结果',
    `handle_remark` TEXT DEFAULT NULL COMMENT '处理备注',
    `satisfaction` INT(1) DEFAULT NULL COMMENT '满意度评分(1-5分)',
    `evaluation` VARCHAR(500) DEFAULT NULL COMMENT '评价内容',
    `evaluation_time` DATETIME DEFAULT NULL COMMENT '评价时间',
    `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志(0-正常/2-删除)',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`feedback_id`),
    UNIQUE KEY `uk_feedback_no` (`feedback_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_handler_id` (`handler_id`),
    KEY `idx_status` (`status`),
    KEY `idx_feedback_type` (`feedback_type`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='反馈表';

-- ----------------------------
-- 反馈回复表
-- ----------------------------
DROP TABLE IF EXISTS `feedback_reply`;
CREATE TABLE `feedback_reply` (
    `reply_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '回复ID',
    `feedback_id` BIGINT(20) NOT NULL COMMENT '反馈ID',
    `content` TEXT NOT NULL COMMENT '回复内容',
    `attachments` VARCHAR(1000) DEFAULT NULL COMMENT '附件(多个用逗号分隔)',
    `reply_user_id` VARCHAR(64) NOT NULL COMMENT '回复人ID',
    `reply_user_type` CHAR(1) DEFAULT '0' COMMENT '回复人类型(0-用户/1-处理人员)',
    `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志(0-正常/2-删除)',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`reply_id`),
    KEY `idx_feedback_id` (`feedback_id`),
    KEY `idx_reply_user_id` (`reply_user_id`),
    KEY `idx_create_time` (`create_time`),
    CONSTRAINT `fk_feedback_reply_feedback` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`feedback_id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='反馈回复表';

-- ----------------------------
-- 初始化反馈编号序列(用于生成反馈编号)
-- ----------------------------
-- 注: generateFeedbackNo 方法会在 Mapper.xml 中实现，使用以下逻辑:
-- 1. 查询当天最大编号
-- 2. 如果存在则序号+1，否则从1开始
-- 3. 格式: FB + YYYYMMDD + 3位序号，例如: FB20251127001
