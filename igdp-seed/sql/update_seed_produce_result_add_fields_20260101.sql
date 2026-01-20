-- 添加新字段到 prebasic_seed_produce_result 表
-- Add new fields to prebasic_seed_produce_result table
ALTER TABLE `prebasic_seed_produce_result` 
ADD COLUMN `breed_batch_id` VARCHAR(128) NULL COMMENT '育种批次ID' AFTER `to_seed_level`,
ADD COLUMN `variety_id` VARCHAR(128) NULL COMMENT '品种ID' AFTER `breed_batch_id`,
ADD COLUMN `crop_type` VARCHAR(50) NULL COMMENT '作物类型' AFTER `variety_id`;

-- 添加新字段到 basic_seed_produce_result 表
-- Add new fields to basic_seed_produce_result table
ALTER TABLE `basic_seed_produce_result` 
ADD COLUMN `breed_batch_id` VARCHAR(128) NULL COMMENT '育种批次ID' AFTER `to_seed_level`,
ADD COLUMN `variety_id` VARCHAR(128) NULL COMMENT '品种ID' AFTER `breed_batch_id`,
ADD COLUMN `crop_type` VARCHAR(50) NULL COMMENT '作物类型' AFTER `variety_id`;


ALTER TABLE `breed_seed_distribute_detail` 
ADD COLUMN `breed_batch_id` VARCHAR(128) NULL COMMENT '育种批次ID' AFTER `to_seed_level`;