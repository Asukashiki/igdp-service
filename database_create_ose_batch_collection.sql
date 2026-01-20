-- =============================================
-- OSE繁殖批次信息数据采集表
-- OSE Batch Information Data Collection Table
-- =============================================

CREATE TABLE IF NOT EXISTS `ose_batch_collection` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `batch_id` VARCHAR(100) NOT NULL COMMENT '批次ID（自动生成，格式：OSE_YYYYMMDD_HHmmss_XXX）',
  `breeding_batch_id` VARCHAR(200) NOT NULL COMMENT '繁殖批次ID（关联breeding_batch表）',
  `parental_seed_source` VARCHAR(200) DEFAULT NULL COMMENT '亲本种子来源',
  `variety_name` VARCHAR(200) DEFAULT NULL COMMENT '品种名称',
  `crop_type` VARCHAR(50) DEFAULT NULL COMMENT '作物类型',
  `breeding_level` VARCHAR(50) DEFAULT NULL COMMENT '繁殖级别（Pre-Basic, Basic, C1等）',
  `to_multiply_quantity` DECIMAL(15,2) DEFAULT NULL COMMENT '繁殖数量(kg)',
  `collection_date` DATETIME DEFAULT NULL COMMENT '采集日期',
  `operator` VARCHAR(100) DEFAULT NULL COMMENT '操作员',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志（0正常 2删除）',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_batch_id` (`batch_id`),
  KEY `idx_breeding_batch_id` (`breeding_batch_id`),
  KEY `idx_variety_name` (`variety_name`),
  KEY `idx_collection_date` (`collection_date`),
  KEY `idx_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OSE繁殖批次信息数据采集表';
