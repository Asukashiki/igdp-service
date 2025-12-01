-- ============================================
-- 育种许可表字段补充
-- 创建时间: 2025-01-30
-- 说明: 添加冗余字段以提升查询性能,避免频繁的表关联查询
-- ============================================

-- 检查 breeding_license 表是否缺少冗余字段,如果缺少则添加
-- 注意: breeding_dataset 表在建表时已经包含这些字段,不需要再添加

-- 为 breeding_license 表添加冗余字段
ALTER TABLE breeding_license
ADD COLUMN batch_name VARCHAR(100) COMMENT '批次名称' AFTER batch_id,
ADD COLUMN dataset_code VARCHAR(50) COMMENT '数据集编号' AFTER dataset_id,
ADD COLUMN crop_type VARCHAR(50) COMMENT '作物类型' AFTER dataset_code,
ADD COLUMN variety_name VARCHAR(100) COMMENT '品种名称' AFTER crop_type;

-- ============================================
-- 说明:
-- 1. batch_name: 批次名称 - 冗余字段,方便列表显示
-- 2. dataset_code: 数据集编号 - 冗余字段,方便列表显示
-- 3. crop_type: 作物类型 - 冗余字段,用于筛选和统计
-- 4. variety_name: 品种名称 - 冗余字段,用于搜索和显示
--
-- breeding_dataset 表在建表SQL中已包含以下冗余字段,无需再添加:
-- - batch_name (Line 9)
-- - crop_type (Line 10)
-- - variety_name (Line 11)
-- ============================================
