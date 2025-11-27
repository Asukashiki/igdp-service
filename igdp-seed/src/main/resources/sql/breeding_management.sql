-- 育种管理模块数据库建表语句
-- 适配RuoYi-V4.7.5框架

-- =====================================================
-- 1. 育种计划表
-- =====================================================
CREATE TABLE `breeding_plan` (
  `plan_id` varchar(32) NOT NULL COMMENT '育种计划唯一标识',
  `enterprise_id` varchar(32) NOT NULL COMMENT '关联企业唯一标识',
  `plan_name` varchar(100) NOT NULL COMMENT '计划名称',
  `breeding_year` year NOT NULL COMMENT '育种年度',
  `batch_id` varchar(32) NOT NULL COMMENT '育种批次ID',
  `planting_base` varchar(100) NOT NULL COMMENT '种植基地',
  `crop_type` varchar(50) NOT NULL COMMENT '作物类型',
  `variety_name` varchar(100) NOT NULL COMMENT '品种名称',
  `propagation_level` varchar(50) NOT NULL COMMENT '繁殖级别',
  `parent_seed_source` varchar(255) NOT NULL COMMENT '亲本种子来源',
  `person_in_charge` varchar(50) NOT NULL COMMENT '负责人',
  `start_date` date NOT NULL COMMENT '计划起始时间',
  `end_date` date NOT NULL COMMENT '计划结束时间',
  `breeding_goal` varchar(500) NOT NULL COMMENT '育种目标',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`plan_id`),
  UNIQUE KEY `uk_batch_id` (`batch_id`),
  KEY `idx_enterprise_id` (`enterprise_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='育种计划表';

-- =====================================================
-- 2. 育种材料登记表
-- =====================================================
CREATE TABLE `breeding_material` (
  `material_id` varchar(32) NOT NULL COMMENT '材料登记唯一标识',
  `registration_code` varchar(50) NOT NULL COMMENT '登记编码',
  `batch_id` varchar(32) NOT NULL COMMENT '育种批次ID',
  `warehouse_in_id` varchar(32) NOT NULL COMMENT '入库ID',
  `seed_type` varchar(50) NOT NULL COMMENT '种子类别',
  `quantity` decimal(10,2) NOT NULL COMMENT '数量（千克）',
  `source_entity` varchar(100) NOT NULL COMMENT '来源实体',
  `receive_date` date NOT NULL COMMENT '接收日期',
  `lab_test_report_url` varchar(255) DEFAULT NULL COMMENT '实验室检测报告路径',
  `operator` varchar(50) NOT NULL COMMENT '操作人',
  `operation_org` varchar(100) NOT NULL COMMENT '操作机构',
  `operation_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`material_id`),
  UNIQUE KEY `uk_registration_code` (`registration_code`),
  KEY `idx_batch_id` (`batch_id`),
  CONSTRAINT `fk_material_batch_id` FOREIGN KEY (`batch_id`) REFERENCES `breeding_plan` (`batch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='育种材料登记表';

-- =====================================================
-- 3. 育种跟踪记录表
-- =====================================================
CREATE TABLE `breeding_tracking` (
  `tracking_id` varchar(32) NOT NULL COMMENT '跟踪记录唯一标识',
  `batch_id` varchar(32) NOT NULL COMMENT '育种批次ID',
  `stage_name` varchar(50) NOT NULL COMMENT '阶段名称',
  `location` varchar(100) NOT NULL COMMENT '位置',
  `coordinates` varchar(50) DEFAULT NULL COMMENT '坐标（经纬度）',
  `expected_yield` decimal(10,2) NOT NULL COMMENT '预期产量',
  `actual_yield` decimal(10,2) DEFAULT NULL COMMENT '实际产量',
  `field_inspection_score` decimal(3,2) DEFAULT NULL COMMENT '田间检查评分',
  `disease_observation` varchar(500) DEFAULT NULL COMMENT '病害观察',
  `stage_completion_date` date DEFAULT NULL COMMENT '阶段完成日期',
  `recorder` varchar(50) NOT NULL COMMENT '记录人',
  `record_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`tracking_id`),
  KEY `idx_batch_id` (`batch_id`),
  CONSTRAINT `fk_tracking_batch_id` FOREIGN KEY (`batch_id`) REFERENCES `breeding_plan` (`batch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='育种跟踪记录表';
