-- ============================================
-- 为 breeding_license 表添加冗余字段
-- 说明: 如果字段已存在会报错,但不影响其他字段的添加
-- ============================================

-- 添加批次名称
ALTER TABLE breeding_license ADD COLUMN batch_name VARCHAR(100) COMMENT '批次名称' AFTER batch_id;

-- 添加数据集编号
ALTER TABLE breeding_license ADD COLUMN dataset_code VARCHAR(50) COMMENT '数据集编号' AFTER dataset_id;

-- 添加作物类型
ALTER TABLE breeding_license ADD COLUMN crop_type VARCHAR(50) COMMENT '作物类型' AFTER dataset_code;

-- 添加品种名称
ALTER TABLE breeding_license ADD COLUMN variety_name VARCHAR(100) COMMENT '品种名称' AFTER crop_type;
