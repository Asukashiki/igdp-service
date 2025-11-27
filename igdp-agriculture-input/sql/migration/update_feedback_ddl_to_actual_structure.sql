-- ----------------------------
-- 反馈表实际结构（基于当前数据库）
-- 日期: 2025-11-27
-- 说明: 此文件记录了feedback表的实际结构
-- ----------------------------

-- 实际的feedback表结构不包含以下字段（与DDL文件定义不同）:
-- - user_id / user_type (已替换为 input_id/input_name, supplier_id/supplier_name)
-- - attachments (暂未使用)
-- - priority (暂未使用)

-- 当前实际表结构包含的字段:
-- feedback_id, feedback_no, feedback_type, title, content,
-- input_id, input_name, supplier_id, supplier_name,
-- contact_name, contact_phone, contact_email,
-- status, handler_id, handle_time, handle_result, handle_remark,
-- satisfaction, evaluation, evaluation_time,
-- create_time, create_by, update_time, update_by, del_flag

-- 如果需要从头创建表（实际结构），使用以下SQL:
/*
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback` (
    `feedback_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '反馈ID',
    `feedback_no` VARCHAR(50) NOT NULL COMMENT '反馈编号',
    `feedback_type` CHAR(1) NOT NULL COMMENT '反馈类型(0-投诉/1-建议/2-咨询/3-故障报告/4-其他)',
    `title` VARCHAR(200) NOT NULL COMMENT '反馈标题',
    `content` TEXT NOT NULL COMMENT '反馈内容',
    `input_id` INT(11) DEFAULT NULL COMMENT '农资ID',
    `input_name` VARCHAR(200) DEFAULT NULL COMMENT '农资名称',
    `supplier_id` INT(11) DEFAULT NULL COMMENT '供应商ID',
    `supplier_name` VARCHAR(200) DEFAULT NULL COMMENT '供应商名称',
    `contact_name` VARCHAR(100) DEFAULT NULL COMMENT '联系人姓名',
    `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `contact_email` VARCHAR(100) DEFAULT NULL COMMENT '联系邮箱',
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
    PRIMARY KEY (`feedback_id`),
    UNIQUE KEY `uk_feedback_no` (`feedback_no`),
    KEY `idx_input_id` (`input_id`),
    KEY `idx_supplier_id` (`supplier_id`),
    KEY `idx_handler_id` (`handler_id`),
    KEY `idx_status` (`status`),
    KEY `idx_feedback_type` (`feedback_type`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='反馈表';
*/
