-- ============================================
-- 育种模块数据表字段补充 (修正版)
-- 创建时间: 2025-01-30
-- 说明:
-- 1. breeding_dataset 表在建表时已经包含冗余字段,无需添加
-- 2. breeding_license 表需要添加冗余字段
-- ============================================

-- 检查并添加 breeding_license 表的冗余字段
-- 使用安全的方式,先检查字段是否存在

-- 添加 batch_name 字段
SET @col_exists = 0;
SELECT COUNT(*) INTO @col_exists
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_NAME = 'breeding_license'
  AND COLUMN_NAME = 'batch_name';

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE breeding_license ADD COLUMN batch_name VARCHAR(100) COMMENT ''批次名称'' AFTER batch_id',
    'SELECT ''Column batch_name already exists'' AS msg');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加 dataset_code 字段
SET @col_exists = 0;
SELECT COUNT(*) INTO @col_exists
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_NAME = 'breeding_license'
  AND COLUMN_NAME = 'dataset_code';

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE breeding_license ADD COLUMN dataset_code VARCHAR(50) COMMENT ''数据集编号'' AFTER dataset_id',
    'SELECT ''Column dataset_code already exists'' AS msg');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加 crop_type 字段
SET @col_exists = 0;
SELECT COUNT(*) INTO @col_exists
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_NAME = 'breeding_license'
  AND COLUMN_NAME = 'crop_type';

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE breeding_license ADD COLUMN crop_type VARCHAR(50) COMMENT ''作物类型'' AFTER dataset_code',
    'SELECT ''Column crop_type already exists'' AS msg');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加 variety_name 字段
SET @col_exists = 0;
SELECT COUNT(*) INTO @col_exists
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_NAME = 'breeding_license'
  AND COLUMN_NAME = 'variety_name';

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE breeding_license ADD COLUMN variety_name VARCHAR(100) COMMENT ''品种名称'' AFTER crop_type',
    'SELECT ''Column variety_name already exists'' AS msg');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ============================================
-- 验证结果
-- ============================================
SELECT 'breeding_license 表结构修改完成' AS result;

-- 查看最终的表结构
SHOW COLUMNS FROM breeding_license;

-- ============================================
-- 说明:
-- breeding_dataset 表在建表SQL (breeding_dataset.sql) 中已包含以下冗余字段:
-- - batch_name VARCHAR(100) DEFAULT NULL COMMENT '育种批次名称(冗余字段)'
-- - crop_type VARCHAR(50) DEFAULT NULL COMMENT '作物类型(冗余字段)'
-- - variety_name VARCHAR(100) DEFAULT NULL COMMENT '品种名称(冗余字段)'
--
-- 因此只需要为 breeding_license 表添加字段即可
-- ============================================
