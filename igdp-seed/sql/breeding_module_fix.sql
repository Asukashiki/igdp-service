-- ============================================
-- 育种模块数据表字段补充
-- 创建时间: 2025-01-30
-- 说明: 添加冗余字段以提升查询性能,避免频繁的表关联查询
-- ============================================

-- 1. 修改育种数据集表,添加冗余字段
ALTER TABLE breeding_dataset
ADD COLUMN batch_name VARCHAR(100) COMMENT '批次名称' AFTER batch_id,
ADD COLUMN crop_type VARCHAR(50) COMMENT '作物类型' AFTER batch_name,
ADD COLUMN variety_name VARCHAR(100) COMMENT '品种名称' AFTER crop_type;

-- 2. 修改育种许可表,添加冗余字段
ALTER TABLE breeding_license
ADD COLUMN batch_name VARCHAR(100) COMMENT '批次名称' AFTER batch_id,
ADD COLUMN dataset_code VARCHAR(50) COMMENT '数据集编号' AFTER dataset_id,
ADD COLUMN crop_type VARCHAR(50) COMMENT '作物类型' AFTER dataset_code,
ADD COLUMN variety_name VARCHAR(100) COMMENT '品种名称' AFTER crop_type;

-- ============================================
-- 说明:
-- 1. batch_name: 批次名称 - 冗余字段,方便列表显示,避免JOIN breeding_batch表
-- 2. dataset_code: 数据集编号 - 冗余字段,方便列表显示,避免JOIN breeding_dataset表
-- 3. crop_type: 作物类型 - 冗余字段,用于筛选和统计
-- 4. variety_name: 品种名称 - 冗余字段,用于搜索和显示
--
-- 这些冗余字段在新增和修改时需要同步更新:
-- - breeding_dataset: 新增/修改时从breeding_batch表获取batch_name, crop_type, variety_name
-- - breeding_license: 新增/修改时从breeding_batch和breeding_dataset表获取相关字段
-- ============================================
