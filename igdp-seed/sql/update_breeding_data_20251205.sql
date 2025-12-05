-- =========================================================================================================
-- 育种数据管理模块 - 数据库更新脚本
-- 更新日期: 2025-12-05
-- 说明: 根据前端表单修改同步更新数据库结构
-- =========================================================================================================

-- =========================================================================================================
-- 1. 地块信息表 (plot_info) 结构调整
-- 说明: 移除位置信息字段(region, zone, woreda, kebele)，保留GPS坐标
-- =========================================================================================================

-- 删除地块信息表中的位置字段
ALTER TABLE `plot_info` DROP COLUMN IF EXISTS `region`;
ALTER TABLE `plot_info` DROP COLUMN IF EXISTS `zone`;
ALTER TABLE `plot_info` DROP COLUMN IF EXISTS `woreda`;
ALTER TABLE `plot_info` DROP COLUMN IF EXISTS `kebele`;

-- 修改表注释
ALTER TABLE `plot_info` COMMENT='地块及播种信息表（已移除区域位置字段）';

-- =========================================================================================================
-- 2. 农艺性状数据表 (agronomic_trait) 结构调整
-- 说明: 添加基础信息模块的新字段，保留原有形态学、生长期、照片信息字段
-- =========================================================================================================

-- 添加性状记录编号字段
ALTER TABLE `agronomic_trait` ADD COLUMN `trait_record_id` varchar(64) NULL COMMENT '性状记录编号 - 格式: {plot_id}-T{record_no}' AFTER `trait_id`;

-- 添加地块ID字段
ALTER TABLE `agronomic_trait` ADD COLUMN `plot_id` varchar(32) NULL COMMENT '地块ID' AFTER `trait_record_id`;

-- 添加观测日期字段（替代原record_time）
ALTER TABLE `agronomic_trait` ADD COLUMN `observation_date` date NULL COMMENT '观测日期' AFTER `batch_id`;

-- 添加生长阶段字段
ALTER TABLE `agronomic_trait` ADD COLUMN `growth_stage` varchar(50) NULL COMMENT '生长阶段（如：苗期、分蘖期、抽穗期等）' AFTER `observation_date`;

-- 添加性状代码字段
ALTER TABLE `agronomic_trait` ADD COLUMN `trait_code` varchar(32) NULL COMMENT '性状代码（标准化性状编码）' AFTER `growth_stage`;

-- 添加性状名称字段
ALTER TABLE `agronomic_trait` ADD COLUMN `trait_name` varchar(100) NULL COMMENT '性状名称（如：株高、分蘖数等）' AFTER `trait_code`;

-- 添加性状值字段
ALTER TABLE `agronomic_trait` ADD COLUMN `trait_value` decimal(10,2) NULL COMMENT '性状观测值' AFTER `trait_name`;

-- 添加单位字段
ALTER TABLE `agronomic_trait` ADD COLUMN `unit` varchar(20) NULL COMMENT '测量单位（如：cm、个、天等）' AFTER `trait_value`;

-- 添加观测员ID字段
ALTER TABLE `agronomic_trait` ADD COLUMN `observer_id` varchar(32) NULL COMMENT '观测员ID（当前登录用户）' AFTER `unit`;

-- 修改原有字段为可空（兼容性考虑）
ALTER TABLE `agronomic_trait` MODIFY COLUMN `plant_height_cm` decimal(8,2) NULL COMMENT '植物高度(CM)';
ALTER TABLE `agronomic_trait` MODIFY COLUMN `tiller_count` int(10) NULL COMMENT '分蘖数';
ALTER TABLE `agronomic_trait` MODIFY COLUMN `spike_length_cm` decimal(8,2) NULL COMMENT '穗长(CM)';
ALTER TABLE `agronomic_trait` MODIFY COLUMN `days_to_emergence` int(10) NULL COMMENT '出苗天数';
ALTER TABLE `agronomic_trait` MODIFY COLUMN `days_to_tillering` int(10) NULL COMMENT '分蘖天数';
ALTER TABLE `agronomic_trait` MODIFY COLUMN `days_to_heading` int(10) NULL COMMENT '抽穗期天数';

-- 添加索引以提升查询性能
ALTER TABLE `agronomic_trait` ADD INDEX `idx_plot_id` (`plot_id`) COMMENT '地块ID索引';
ALTER TABLE `agronomic_trait` ADD INDEX `idx_observation_date` (`observation_date`) COMMENT '观测日期索引';
ALTER TABLE `agronomic_trait` ADD INDEX `idx_trait_code` (`trait_code`) COMMENT '性状代码索引';
ALTER TABLE `agronomic_trait` ADD INDEX `idx_observer_id` (`observer_id`) COMMENT '观测员ID索引';

-- 修改表注释
ALTER TABLE `agronomic_trait` COMMENT='农艺性状数据表（包含基础信息、形态学、生长期、照片信息）';

-- =========================================================================================================
-- 3. 数据迁移说明
-- =========================================================================================================

-- 注意: 以下数据迁移操作需要根据实际业务数据情况执行

-- 3.1 将 record_time 数据迁移到 observation_date
UPDATE `agronomic_trait`
SET `observation_date` = `record_time`
WHERE `observation_date` IS NULL AND `record_time` IS NOT NULL;

-- 3.2 为现有数据生成 trait_record_id（如果有plot_id数据）
-- UPDATE `agronomic_trait`
-- SET `trait_record_id` = CONCAT(`plot_id`, '-T', LPAD(ROW_NUMBER() OVER (PARTITION BY `plot_id` ORDER BY `observation_date`), 3, '0'))
-- WHERE `trait_record_id` IS NULL AND `plot_id` IS NOT NULL;

-- 3.3 为现有数据设置默认 observer_id（如果需要）
-- UPDATE `agronomic_trait`
-- SET `observer_id` = `create_by`
-- WHERE `observer_id` IS NULL AND `create_by` IS NOT NULL;

-- =========================================================================================================
-- 4. 验证查询
-- =========================================================================================================

-- 验证地块信息表结构
SHOW CREATE TABLE `plot_info`;

-- 验证农艺性状表结构
SHOW CREATE TABLE `agronomic_trait`;

-- 验证农艺性状表索引
SHOW INDEX FROM `agronomic_trait`;

-- 统计分析
SELECT
    COUNT(*) as total_records,
    COUNT(DISTINCT plot_id) as unique_plots,
    COUNT(DISTINCT observer_id) as unique_observers,
    MIN(observation_date) as earliest_date,
    MAX(observation_date) as latest_date
FROM `agronomic_trait`
WHERE `is_deleted` = 0;

-- =========================================================================================================
-- 脚本执行完毕
-- =========================================================================================================
