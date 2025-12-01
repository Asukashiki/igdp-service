-- ============================================
-- 育种数据集表 (breeding_dataset)
-- 用于汇总育种批次的所有试验数据,提交上级部门审核
-- ============================================
CREATE TABLE breeding_dataset (
    id VARCHAR(36) NOT NULL COMMENT '主键ID',
    dataset_code VARCHAR(50) DEFAULT NULL COMMENT '数据集编号(审核通过后生成)',
    batch_id VARCHAR(36) NOT NULL COMMENT '育种批次ID',
    batch_name VARCHAR(100) DEFAULT NULL COMMENT '育种批次名称(冗余字段)',
    crop_type VARCHAR(50) DEFAULT NULL COMMENT '作物类型(冗余字段)',
    variety_name VARCHAR(100) DEFAULT NULL COMMENT '品种名称(冗余字段)',
    trial_count INT DEFAULT 0 COMMENT '试验记录数',
    field_data_count INT DEFAULT 0 COMMENT '田间数据记录数',
    env_data_count INT DEFAULT 0 COMMENT '环境数据记录数',
    lab_test_count INT DEFAULT 0 COMMENT '实验室检测记录数',
    yield_data_count INT DEFAULT 0 COMMENT '产量数据记录数',
    dataset_status VARCHAR(20) NOT NULL DEFAULT 'draft' COMMENT '数据集状态:draft草稿/submitted已提交/reviewing审核中/approved已通过/rejected已驳回',
    submit_time DATETIME DEFAULT NULL COMMENT '提交时间',
    submit_by VARCHAR(36) DEFAULT NULL COMMENT '提交人ID',
    submit_by_name VARCHAR(50) DEFAULT NULL COMMENT '提交人姓名',
    submit_org_code VARCHAR(36) DEFAULT NULL COMMENT '提交机构代码',
    submit_org_name VARCHAR(100) DEFAULT NULL COMMENT '提交机构名称',
    status CHAR(1) NOT NULL DEFAULT '1' COMMENT '状态:1有效0无效',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人ID',
    created_by_name VARCHAR(50) DEFAULT NULL COMMENT '创建人姓名',
    created_org_code VARCHAR(36) DEFAULT NULL COMMENT '创建机构代码',
    created_org_name VARCHAR(100) DEFAULT NULL COMMENT '创建机构名称',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by VARCHAR(36) DEFAULT NULL COMMENT '更新人ID',
    updated_time DATETIME DEFAULT NULL COMMENT '更新时间',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    deleted CHAR(1) NOT NULL DEFAULT '0' COMMENT '删除标记:0未删除1已删除',
    PRIMARY KEY (id),
    UNIQUE INDEX uk_dataset_code (dataset_code),
    INDEX idx_batch_id (batch_id),
    INDEX idx_dataset_status (dataset_status),
    INDEX idx_submit_time (submit_time),
    INDEX idx_created_time (created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='育种数据集表';

-- 示例数据
-- INSERT INTO breeding_dataset (id, batch_id, batch_name, crop_type, variety_name, dataset_status, created_by)
-- VALUES ('uuid-1', 'batch-1', '2024年小麦育种批次', 'wheat', '优质小麦001', 'draft', 'user-1');
