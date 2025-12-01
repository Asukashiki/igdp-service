-- =========================================================================================================
-- 育种数据管理模块 - 数据库建表脚本
-- Version: 1.0
-- Description: 包含育种批次、地块信息、播种信息、试验基础信息、农艺性状、农事记录、环境数据等表
-- =========================================================================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =========================================================================================================
-- 1. 育种批次信息表 (breeding_batch)
-- =========================================================================================================
DROP TABLE IF EXISTS `breeding_batch`;
CREATE TABLE `breeding_batch` (
  `data_id` varchar(32) NOT NULL COMMENT '数据标识(主键)',
  `batch_name` varchar(100) NOT NULL COMMENT '计划名称',
  `batch_id` varchar(32) NOT NULL COMMENT '育种批次ID',
  `batch_time` date NOT NULL COMMENT '批次时间',
  `crop_type` varchar(50) NOT NULL COMMENT '作物类型',
  `variety_name` varchar(100) NOT NULL COMMENT '品种名称',
  `species` varchar(100) NOT NULL COMMENT '物种',
  `genus` varchar(100) NOT NULL COMMENT '属',
  `family` varchar(100) NOT NULL COMMENT '科',
  `breeding_method` varchar(100) NOT NULL COMMENT '繁育方法',
  `pedigree` varchar(100) NOT NULL COMMENT '血统',
  `year_of_development` int(4) NOT NULL COMMENT '繁育年份',
  `product_place` varchar(100) NOT NULL COMMENT '生产地',
  `yield` varchar(200) NOT NULL COMMENT '产量说明',
  `person_in_charge` varchar(50) NOT NULL COMMENT '负责人',
  `start_date` date NOT NULL COMMENT '计划起始时间',
  `end_date` date NOT NULL COMMENT '计划结束时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标识(0=未删除,1=已删除)',
  PRIMARY KEY (`data_id`),
  UNIQUE KEY `uk_batch_id` (`batch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='育种批次信息表';

-- =========================================================================================================
-- 2. 地块信息表 (plot_info)
-- =========================================================================================================
DROP TABLE IF EXISTS `plot_info`;
CREATE TABLE `plot_info` (
  `ground_id` varchar(32) NOT NULL COMMENT '地块ID(主键)',
  `batch_id` varchar(32) NOT NULL COMMENT '育种批次ID',
  `trial_id` varchar(32) DEFAULT NULL COMMENT '试验ID',
  `trial_field_name` varchar(100) NOT NULL COMMENT '试验田名称',
  `research_center_id` varchar(32) NOT NULL COMMENT '研究中心ID',
  `program_id` varchar(32) NOT NULL COMMENT '程序ID',
  `sub_program_id` varchar(32) NOT NULL COMMENT '子程序ID',
  `research_field_id` varchar(32) NOT NULL COMMENT '主题研究领域ID',
  `region` varchar(50) NOT NULL COMMENT '地区',
  `zone` varchar(50) NOT NULL COMMENT '区域',
  `woreda` varchar(50) NOT NULL COMMENT '县',
  `kebele` varchar(50) NOT NULL COMMENT '乡',
  `agricultural_eco_zone` varchar(50) DEFAULT NULL COMMENT '农业生态区',
  `gps_location` varchar(50) NOT NULL COMMENT 'GPS位置',
  `start_date` date NOT NULL COMMENT '开始日期',
  `season` varchar(20) NOT NULL COMMENT '季节',
  `activity_code` varchar(32) DEFAULT NULL COMMENT '活动代码',
  `kpi_code` varchar(32) DEFAULT NULL COMMENT 'KPI代码',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标识(0=未删除,1=已删除)',
  PRIMARY KEY (`ground_id`),
  KEY `idx_batch_id` (`batch_id`),
  KEY `idx_trial_id` (`trial_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='地块信息表';

-- =========================================================================================================
-- 3. 播种信息表 (sowing_info)
-- =========================================================================================================
DROP TABLE IF EXISTS `sowing_info`;
CREATE TABLE `sowing_info` (
  `sowing_id` varchar(32) NOT NULL COMMENT '播种记录ID(主键)',
  `ground_id` varchar(32) NOT NULL COMMENT '关联地块ID',
  `seed_quantity` decimal(10,2) NOT NULL COMMENT '播种种子数量(kg)',
  `sowing_method` varchar(50) NOT NULL COMMENT '播种方式',
  `sowing_time` date NOT NULL COMMENT '播种时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标识(0=未删除,1=已删除)',
  PRIMARY KEY (`sowing_id`),
  KEY `idx_ground_id` (`ground_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='播种信息表';

-- =========================================================================================================
-- 4. 试验基础信息表 (trial_basic)
-- =========================================================================================================
DROP TABLE IF EXISTS `trial_basic`;
CREATE TABLE `trial_basic` (
  `trial_id` varchar(32) NOT NULL COMMENT '试验ID(主键)',
  `trial_name` varchar(100) NOT NULL COMMENT '试验名称',
  `batch_id` varchar(32) NOT NULL COMMENT '育种批次ID',
  `crop_type` varchar(50) NOT NULL COMMENT '作物类型',
  `variety_name` varchar(100) NOT NULL COMMENT '品种名称',
  `research_center_id` varchar(32) NOT NULL COMMENT '研究中心ID',
  `project_id` varchar(32) NOT NULL COMMENT '项目ID',
  `sub_project_id` varchar(32) NOT NULL COMMENT '子项目ID',
  `theme_field_id` varchar(32) NOT NULL COMMENT '主题领域ID',
  `region` varchar(50) NOT NULL COMMENT '地区',
  `zone` varchar(50) NOT NULL COMMENT '区域',
  `woreda` varchar(50) NOT NULL COMMENT '县',
  `kebele` varchar(50) NOT NULL COMMENT '乡',
  `agricultural_eco_zone` varchar(50) DEFAULT NULL COMMENT '农业生态区',
  `gps_location` varchar(50) NOT NULL COMMENT 'GPS位置',
  `start_date` date NOT NULL COMMENT '开始日期',
  `activity_code` varchar(32) DEFAULT NULL COMMENT '活动代码',
  `kpi_code` varchar(32) DEFAULT NULL COMMENT 'KPI代码',
  `season` varchar(20) NOT NULL COMMENT '季节',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标识(0=未删除,1=已删除)',
  PRIMARY KEY (`trial_id`),
  UNIQUE KEY `uk_trial_name` (`trial_name`),
  KEY `idx_batch_id` (`batch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='试验基础信息表';

-- =========================================================================================================
-- 5. 试验-地块关联表 (trial_plot_relation)
-- =========================================================================================================
DROP TABLE IF EXISTS `trial_plot_relation`;
CREATE TABLE `trial_plot_relation` (
  `relation_id` varchar(32) NOT NULL COMMENT '关联记录ID(主键)',
  `trial_id` varchar(32) NOT NULL COMMENT '试验ID',
  `ground_id` varchar(32) NOT NULL COMMENT '地块ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标识(0=未删除,1=已删除)',
  PRIMARY KEY (`relation_id`),
  UNIQUE KEY `uk_trial_ground` (`trial_id`,`ground_id`),
  KEY `idx_trial_id` (`trial_id`),
  KEY `idx_ground_id` (`ground_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='试验-地块关联表';

-- =========================================================================================================
-- 6. 农艺性状数据表 (agronomic_trait)
-- =========================================================================================================
DROP TABLE IF EXISTS `agronomic_trait`;
CREATE TABLE `agronomic_trait` (
  `trait_id` varchar(32) NOT NULL COMMENT '性状记录ID(主键)',
  `batch_id` varchar(32) NOT NULL COMMENT '育种批次ID',
  `trial_id` varchar(32) NOT NULL COMMENT '试验ID',
  `record_time` date NOT NULL COMMENT '记录时间',
  `plant_height_cm` decimal(8,2) NOT NULL COMMENT '植物高度(CM)',
  `tiller_count` int(10) NOT NULL COMMENT '分蘖数',
  `spike_length_cm` decimal(8,2) NOT NULL COMMENT '穗长(CM)',
  `days_to_emergence` int(10) NOT NULL COMMENT '出苗天数',
  `days_to_tillering` int(10) NOT NULL COMMENT '分蘖天数',
  `days_to_heading` int(10) NOT NULL COMMENT '抽穗期天数',
  `photo_url` varchar(255) DEFAULT NULL COMMENT '照片URL',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标识(0=未删除,1=已删除)',
  PRIMARY KEY (`trait_id`),
  KEY `idx_batch_id` (`batch_id`),
  KEY `idx_trial_id` (`trial_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='农艺性状数据表';

-- =========================================================================================================
-- 7. 农事记录表 (farming_record)
-- =========================================================================================================
DROP TABLE IF EXISTS `farming_record`;
CREATE TABLE `farming_record` (
  `farming_id` varchar(32) NOT NULL COMMENT '农事记录ID(主键)',
  `batch_id` varchar(32) NOT NULL COMMENT '育种批次ID',
  `trial_id` varchar(32) NOT NULL COMMENT '试验ID',
  `operation_type` varchar(50) NOT NULL COMMENT '操作类型',
  `operation_time` date NOT NULL COMMENT '操作时间',
  `fertilizer_type` varchar(50) DEFAULT NULL COMMENT '肥料类型(施肥操作必填)',
  `fertilizer_amount` decimal(10,2) DEFAULT NULL COMMENT '施肥量(kg/亩，施肥操作必填)',
  `irrigation_method` varchar(50) DEFAULT NULL COMMENT '灌溉方式(灌溉操作必填)',
  `pesticide_type` varchar(50) DEFAULT NULL COMMENT '农药类型(病虫害防治必填)',
  `pesticide_dosage` varchar(50) DEFAULT NULL COMMENT '农药用量(病虫害防治必填)',
  `operation_desc` varchar(500) DEFAULT NULL COMMENT '操作描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标识(0=未删除,1=已删除)',
  PRIMARY KEY (`farming_id`),
  KEY `idx_batch_id` (`batch_id`),
  KEY `idx_trial_id` (`trial_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='农事记录表';

-- =========================================================================================================
-- 8. 环境属性数据表 (environment_data)
-- =========================================================================================================
DROP TABLE IF EXISTS `environment_data`;
CREATE TABLE `environment_data` (
  `env_id` varchar(32) NOT NULL COMMENT '环境数据ID(主键)',
  `batch_id` varchar(32) NOT NULL COMMENT '育种批次ID',
  `trial_id` varchar(32) NOT NULL COMMENT '试验ID',
  `ground_id` varchar(32) NOT NULL COMMENT '地块ID',
  `data_type` varchar(50) NOT NULL COMMENT '数据类型',
  `collect_time` date NOT NULL COMMENT '采集时间',
  `soil_ph` decimal(3,2) DEFAULT NULL COMMENT '土壤pH值(土壤数据必填)',
  `soil_temperature` decimal(5,2) DEFAULT NULL COMMENT '土壤温度(℃，土壤数据必填)',
  `soil_moisture` decimal(5,2) DEFAULT NULL COMMENT '土壤湿度(%)，土壤数据必填)',
  `air_temperature` decimal(5,2) DEFAULT NULL COMMENT '空气温度(℃，气候数据必填)',
  `air_humidity` decimal(5,2) DEFAULT NULL COMMENT '空气湿度(%)，气候数据必填)',
  `rainfall` decimal(10,2) DEFAULT NULL COMMENT '降雨量(mm，气候数据必填)',
  `water_ph` decimal(3,2) DEFAULT NULL COMMENT '水体pH值(水文数据必填)',
  `data_source` varchar(20) NOT NULL COMMENT '数据来源',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标识(0=未删除,1=已删除)',
  PRIMARY KEY (`env_id`),
  KEY `idx_batch_id` (`batch_id`),
  KEY `idx_trial_id` (`trial_id`),
  KEY `idx_ground_id` (`ground_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='环境属性数据表';

SET FOREIGN_KEY_CHECKS = 1;

-- =========================================================================================================
-- 数据表说明
-- =========================================================================================================
-- 1. breeding_batch     - 育种批次信息表，存储育种计划基础信息
-- 2. plot_info          - 地块信息表，存储试验地块详细信息
-- 3. sowing_info        - 播种信息表，存储播种记录（地块子表）
-- 4. trial_basic        - 试验基础信息表，存储试验核心信息
-- 5. trial_plot_relation - 试验-地块关联表，试验与地块多对多关系
-- 6. agronomic_trait    - 农艺性状数据表，存储植物生长性状数据
-- 7. farming_record     - 农事记录表，存储农事操作记录
-- 8. environment_data   - 环境属性数据表，存储环境监测数据
-- =========================================================================================================
