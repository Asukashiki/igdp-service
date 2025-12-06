-- ============================================
-- 育种数据集审核记录表(breeding_dataset_audit) - 添加数据集快照字段
-- 在审核时保存数据集的快照信息，便于审核历史追溯
-- Database: MySQL
-- Date: 2025-12-05
-- ============================================

USE igdp_seed;

-- 添加数据集编号字段（冗余，便于查询）
ALTER TABLE breeding_dataset_audit
ADD COLUMN dataset_code VARCHAR(50) DEFAULT NULL COMMENT '数据集编号' AFTER dataset_id;

-- 添加育种批次名称字段（冗余，便于查询）
ALTER TABLE breeding_dataset_audit
ADD COLUMN batch_name VARCHAR(100) DEFAULT NULL COMMENT '育种批次名称' AFTER batch_id;

-- 添加作物类型字段（快照）
ALTER TABLE breeding_dataset_audit
ADD COLUMN crop_type VARCHAR(50) DEFAULT NULL COMMENT '作物类型' AFTER batch_name;

-- 添加品种名称字段（快照）
ALTER TABLE breeding_dataset_audit
ADD COLUMN variety_name VARCHAR(100) DEFAULT NULL COMMENT '品种名称' AFTER crop_type;

-- 添加试验ID字段（快照）
ALTER TABLE breeding_dataset_audit
ADD COLUMN trial_id VARCHAR(50) DEFAULT NULL COMMENT '试验ID' AFTER variety_name;

-- 添加版本号字段（快照）
ALTER TABLE breeding_dataset_audit
ADD COLUMN version_no VARCHAR(20) DEFAULT '1.0' COMMENT '版本号' AFTER trial_id;

-- 添加编制人ID字段（快照）
ALTER TABLE breeding_dataset_audit
ADD COLUMN compiled_by VARCHAR(36) DEFAULT NULL COMMENT '编制人ID' AFTER version_no;

-- 添加编制人姓名字段（快照）
ALTER TABLE breeding_dataset_audit
ADD COLUMN compiled_by_name VARCHAR(50) DEFAULT NULL COMMENT '编制人姓名' AFTER compiled_by;

-- 添加编制时间字段（快照）
ALTER TABLE breeding_dataset_audit
ADD COLUMN compiled_at DATETIME DEFAULT NULL COMMENT '编制时间' AFTER compiled_by_name;

-- 添加记录数量字段（快照）
ALTER TABLE breeding_dataset_audit
ADD COLUMN record_count INT DEFAULT 0 COMMENT '记录数量' AFTER compiled_at;

-- 添加数据集状态字段（快照）
ALTER TABLE breeding_dataset_audit
ADD COLUMN dataset_status VARCHAR(20) DEFAULT NULL COMMENT '数据集状态' AFTER record_count;

-- 添加各类数据统计字段（快照）
ALTER TABLE breeding_dataset_audit
ADD COLUMN trial_count INT DEFAULT 0 COMMENT '试验记录数' AFTER dataset_status;

ALTER TABLE breeding_dataset_audit
ADD COLUMN field_data_count INT DEFAULT 0 COMMENT '田间数据记录数' AFTER trial_count;

ALTER TABLE breeding_dataset_audit
ADD COLUMN env_data_count INT DEFAULT 0 COMMENT '环境数据记录数' AFTER field_data_count;

ALTER TABLE breeding_dataset_audit
ADD COLUMN lab_test_count INT DEFAULT 0 COMMENT '实验室检测记录数' AFTER env_data_count;

ALTER TABLE breeding_dataset_audit
ADD COLUMN yield_data_count INT DEFAULT 0 COMMENT '产量数据记录数' AFTER lab_test_count;

-- 添加索引以优化查询性能
CREATE INDEX idx_dataset_code ON breeding_dataset_audit(dataset_code);
CREATE INDEX idx_trial_id ON breeding_dataset_audit(trial_id);
CREATE INDEX idx_version_no ON breeding_dataset_audit(version_no);
CREATE INDEX idx_compiled_by ON breeding_dataset_audit(compiled_by);

-- 验证字段添加
SELECT
    COLUMN_NAME,
    COLUMN_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT,
    COLUMN_COMMENT
FROM
    INFORMATION_SCHEMA.COLUMNS
WHERE
    TABLE_SCHEMA = 'igdp_seed'
    AND TABLE_NAME = 'breeding_dataset_audit'
    AND COLUMN_NAME IN (
        'dataset_code', 'batch_name', 'crop_type', 'variety_name',
        'trial_id', 'version_no', 'compiled_by', 'compiled_by_name',
        'compiled_at', 'record_count', 'dataset_status',
        'trial_count', 'field_data_count', 'env_data_count',
        'lab_test_count', 'yield_data_count'
    )
ORDER BY
    ORDINAL_POSITION;

-- 查询示例 - 查看新增字段
-- SELECT id, dataset_code, batch_name, trial_id, version_no,
--        compiled_by_name, compiled_at, record_count,
--        audit_status, audit_time, auditor_name
-- FROM breeding_dataset_audit
-- ORDER BY created_time DESC
-- LIMIT 10;

-- 说明：
-- 1. 这些字段保存数据集在提交审核时的快照信息
-- 2. dataset_code, batch_name: 冗余字段，便于直接查询显示
-- 3. crop_type, variety_name, trial_id等: 记录数据集的基本信息
-- 4. compiled_by, compiled_by_name, compiled_at: 记录编制信息
-- 5. record_count: 总记录数
-- 6. dataset_status: 数据集当时的状态
-- 7. trial_count等统计字段: 记录各类数据的数量统计
-- 8. 添加索引优化基于数据集编号、试验ID等字段的查询性能
