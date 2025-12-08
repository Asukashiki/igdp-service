-- C1繁殖批次跟踪记录表
-- @author system
-- @since 2025-12-08

CREATE TABLE IF NOT EXISTS `c1_breeding_tracking` (
    `id` varchar(64) NOT NULL COMMENT '主键ID',
    `tracking_id` varchar(64) DEFAULT NULL COMMENT '跟踪编号',
    `batch_id` varchar(64) DEFAULT NULL COMMENT '批次编号',
    `stage_name` varchar(20) DEFAULT NULL COMMENT '阶段名称：01-亲本预备，02-原种，03-基础种，04-认证种',
    `location` varchar(500) DEFAULT NULL COMMENT '繁殖地点',
    `start_date` date DEFAULT NULL COMMENT '开始日期',
    `end_date` date DEFAULT NULL COMMENT '结束日期',
    `tracking_result` varchar(20) DEFAULT NULL COMMENT '跟踪结论：01-正常，02-异常，03-观察中',
    `tracking_desc` text COMMENT '跟踪描述',
    `test_count` int DEFAULT 0 COMMENT '检测记录数',
    `operator` varchar(100) DEFAULT NULL COMMENT '操作人',
    `created_by` varchar(100) DEFAULT NULL COMMENT '创建人',
    `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` varchar(100) DEFAULT NULL COMMENT '更新人',
    `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` varchar(1) DEFAULT '0' COMMENT '删除标记：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tracking_id` (`tracking_id`),
    KEY `idx_batch_id` (`batch_id`),
    KEY `idx_stage_name` (`stage_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='C1繁殖批次跟踪记录表';

-- C1繁殖批次检测记录表
CREATE TABLE IF NOT EXISTS `c1_breeding_test` (
    `id` varchar(64) NOT NULL COMMENT '主键ID',
    `test_id` varchar(64) DEFAULT NULL COMMENT '检测编号',
    `batch_id` varchar(64) DEFAULT NULL COMMENT '批次编号',
    `tracking_id` varchar(64) DEFAULT NULL COMMENT '跟踪编号',
    `crop_type` varchar(100) DEFAULT NULL COMMENT '作物种类',
    `test_date` date DEFAULT NULL COMMENT '检测日期',
    `test_item` varchar(200) DEFAULT NULL COMMENT '检测项目',
    `test_value` varchar(200) DEFAULT NULL COMMENT '检测值',
    `test_result` varchar(20) DEFAULT NULL COMMENT '检测结论：01-合格，02-不合格，03-待复检',
    `test_desc` text COMMENT '检测描述',
    `tester` varchar(100) DEFAULT NULL COMMENT '检测人',
    `test_org` varchar(200) DEFAULT NULL COMMENT '检测机构',
    `created_by` varchar(100) DEFAULT NULL COMMENT '创建人',
    `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` varchar(100) DEFAULT NULL COMMENT '更新人',
    `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` varchar(1) DEFAULT '0' COMMENT '删除标记：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_test_id` (`test_id`),
    KEY `idx_batch_id` (`batch_id`),
    KEY `idx_tracking_id` (`tracking_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='C1繁殖批次检测记录表';
