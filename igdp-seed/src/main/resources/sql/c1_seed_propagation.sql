-- C1种子繁殖申请表
-- @author system
-- @since 2025-12-08

CREATE TABLE IF NOT EXISTS `c1_seed_propagation` (
    `id` varchar(64) NOT NULL COMMENT '主键ID',
    `auth_id` varchar(64) DEFAULT NULL COMMENT '授权ID',
    `applicant_org_type` varchar(50) DEFAULT NULL COMMENT '申请机构类型',
    `applicant_org_name` varchar(200) DEFAULT NULL COMMENT '申请机构名称',
    `applicant_org_id` varchar(64) DEFAULT NULL COMMENT '申请机构ID',
    `propagation_batch_id` varchar(64) DEFAULT NULL COMMENT '繁育批次ID',
    `crop_type` varchar(100) DEFAULT NULL COMMENT '作物种类',
    `variety_name` varchar(200) DEFAULT NULL COMMENT '品种名称',
    `variety_code` varchar(64) DEFAULT NULL COMMENT '品种代码',
    `species` varchar(100) DEFAULT NULL COMMENT '物种',
    `apply_date` date DEFAULT NULL COMMENT '申请日期',
    `apply_description` text COMMENT '申请描述',
    `apply_status` varchar(20) DEFAULT 'pending' COMMENT '申请状态：pending-待审核，approved-通过，rejected-拒绝',
    `audit_result` varchar(20) DEFAULT NULL COMMENT '审核结果：approved-通过，rejected-拒绝',
    `audit_opinion` text COMMENT '审核意见',
    `auditor` varchar(100) DEFAULT NULL COMMENT '审核人',
    `audit_org` varchar(200) DEFAULT NULL COMMENT '审核机构',
    `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
    `operator` varchar(100) DEFAULT NULL COMMENT '操作人',
    `operation_org` varchar(200) DEFAULT NULL COMMENT '操作机构',
    `operation_time` datetime DEFAULT NULL COMMENT '操作时间',
    `created_by` varchar(100) DEFAULT NULL COMMENT '创建人',
    `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` varchar(100) DEFAULT NULL COMMENT '更新人',
    `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` varchar(1) DEFAULT '0' COMMENT '删除标记：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_applicant_org_id` (`applicant_org_id`),
    KEY `idx_propagation_batch_id` (`propagation_batch_id`),
    KEY `idx_apply_status` (`apply_status`),
    KEY `idx_crop_type` (`crop_type`),
    KEY `idx_apply_date` (`apply_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='C1种子繁殖申请表';
