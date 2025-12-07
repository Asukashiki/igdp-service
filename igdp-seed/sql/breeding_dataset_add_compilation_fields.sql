-- ============================================
-- 育种数据集表(breeding_dataset) - 添加编制相关字段
-- 添加试验ID、版本号、编制人、编制时间、记录数量等字段
-- Database: MySQL
-- Date: 2025-12-05
-- ============================================

USE igdp_seed;

-- 添加试验ID字段
ALTER TABLE breeding_dataset
ADD COLUMN trial_id VARCHAR(50) DEFAULT NULL COMMENT '试验ID' AFTER variety_name;

-- 添加版本号字段
ALTER TABLE breeding_dataset
ADD COLUMN version_no VARCHAR(20) DEFAULT '1.0' COMMENT '版本号' AFTER trial_id;

-- 添加编制人ID字段
ALTER TABLE breeding_dataset
ADD COLUMN compiled_by VARCHAR(36) DEFAULT NULL COMMENT '编制人ID' AFTER version_no;

-- 添加编制人姓名字段
ALTER TABLE breeding_dataset
ADD COLUMN compiled_by_name VARCHAR(50) DEFAULT NULL COMMENT '编制人姓名' AFTER compiled_by;

-- 添加编制时间字段
ALTER TABLE breeding_dataset
ADD COLUMN compiled_at DATETIME DEFAULT NULL COMMENT '编制时间' AFTER compiled_by_name;

-- 添加记录数量字段
ALTER TABLE breeding_dataset
ADD COLUMN record_count INT DEFAULT 0 COMMENT '记录数量(所有数据记录总数)' AFTER compiled_at;

-- 添加索引以优化查询性能
CREATE INDEX idx_trial_id ON breeding_dataset(trial_id);
CREATE INDEX idx_compiled_by ON breeding_dataset(compiled_by);
CREATE INDEX idx_compiled_at ON breeding_dataset(compiled_at);

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
    AND TABLE_NAME = 'breeding_dataset'
    AND COLUMN_NAME IN ('trial_id', 'version_no', 'compiled_by', 'compiled_by_name', 'compiled_at', 'record_count')
ORDER BY
    ORDINAL_POSITION;

-- 查询示例 - 查看新增字段
-- SELECT id, dataset_code, batch_name, trial_id, version_no, compiled_by_name, compiled_at, record_count
-- FROM breeding_dataset
-- LIMIT 10;

-- 说明：
-- 1. trial_id: 关联的试验ID，用于追溯数据来源
-- 2. version_no: 数据集版本号，默认为'1.0'，支持版本管理
-- 3. compiled_by: 编制人用户ID
-- 4. compiled_by_name: 编制人姓名，冗余字段便于显示
-- 5. compiled_at: 数据集编制完成时间
-- 6. record_count: 记录数量总计，等于 trial_count + field_data_count + env_data_count + lab_test_count + yield_data_count
-- 7. 添加了三个索引以优化基于试验ID、编制人、编制时间的查询性能
