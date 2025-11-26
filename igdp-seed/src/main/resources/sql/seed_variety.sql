-- 种子品种管理系统数据库脚本

-- 1. 品种登记信息表
CREATE TABLE `variety_registration` (
  `registration_id` VARCHAR(32) NOT NULL COMMENT '登记申请唯一标识（主键，系统生成）',
  `registration_no` VARCHAR(50) NOT NULL COMMENT '登记申请号（唯一索引，格式：VAR+年月日+6位随机数）',
  `enterprise_id` VARCHAR(32) NOT NULL COMMENT '关联企业唯一标识',
  `enterprise_name` VARCHAR(100) NOT NULL COMMENT '企业名称（冗余字段，便于查询）',
  `unified_social_credit_code` VARCHAR(20) NOT NULL COMMENT '统一社会信用代码（冗余）',
  `enterprise_type` VARCHAR(50) NOT NULL COMMENT '企业类型（枚举：生产型/贸易型/综合型）',
  `seed_license_no` VARCHAR(50) NOT NULL COMMENT '种子许可证编号（冗余）',
  -- 备案基础信息
  `record_type` VARCHAR(50) NOT NULL COMMENT '备案类型',
  `record_date` DATE NOT NULL COMMENT '备案日期（默认当前日期）',
  `record_status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '备案状态（0-审核中/1-待发布/2-审核未通过/3-已发布）',
  -- 品种标识信息
  `variety_name` VARCHAR(100) NOT NULL COMMENT '品种名称',
  `variety_code` VARCHAR(50) NOT NULL COMMENT '品种代码',
  `crop_type` VARCHAR(50) NOT NULL COMMENT '作物类型',
  `species` VARCHAR(50) NOT NULL COMMENT '物种',
  `genus` VARCHAR(50) NOT NULL COMMENT '属',
  `family` VARCHAR(50) NOT NULL COMMENT '科',
  `breeding_method` VARCHAR(100) NOT NULL COMMENT '育种方法',
  `method_pedigree` VARCHAR(255) NULL COMMENT '方法系谱',
  `breeding_year` INT NOT NULL COMMENT '培育年份',
  -- 技术性状信息
  `min_yield_potential` DECIMAL(10,2) NOT NULL COMMENT '最低产量潜力（公担/公顷）',
  `max_yield_potential` DECIMAL(10,2) NOT NULL COMMENT '最高产量潜力（公担/公顷）',
  `disease_resistance` VARCHAR(255) NOT NULL COMMENT '抗病性',
  `stress_resistance` VARCHAR(255) NOT NULL COMMENT '抗逆性',
  `growth_period` INT NOT NULL COMMENT '生育期（天）',
  `plant_height` DECIMAL(10,2) NOT NULL COMMENT '株高（厘米）',
  `grain_quality_traits` VARCHAR(255) NOT NULL COMMENT '谷物质量性状',
  -- 试验和性能数据
  `test_location` VARCHAR(255) NOT NULL COMMENT '试验地点',
  `test_year` INT NOT NULL COMMENT '试验年份',
  `average_yield` DECIMAL(10,2) NOT NULL COMMENT '平均产量',
  `stability_score` DECIMAL(3,2) NOT NULL COMMENT '稳定性评分',
  `test_report_url` VARCHAR(255) NOT NULL COMMENT '试验报告存储路径',
  `photo_url` VARCHAR(255) NULL COMMENT '照片存储路径',
  -- 监管数据
  `approval_doc_no` VARCHAR(50) NOT NULL COMMENT '核准文件编号',
  `approval_org` VARCHAR(100) NOT NULL COMMENT '核准机构',
  `approval_date` DATE NOT NULL COMMENT '核准日期',
  `certification_doc_url` VARCHAR(255) NOT NULL COMMENT '认证文件存储路径',
  -- 操作信息
  `operator` VARCHAR(50) NOT NULL COMMENT '操作人',
  `operation_org` VARCHAR(100) NOT NULL COMMENT '操作机构',
  `operation_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`registration_id`),
  UNIQUE KEY `idx_registration_no` (`registration_no`),
  KEY `idx_enterprise_id` (`enterprise_id`),
  KEY `idx_variety_name` (`variety_name`),
  KEY `idx_record_status` (`record_status`),
  CONSTRAINT `fk_variety_enterprise` FOREIGN KEY (`enterprise_id`) REFERENCES `enterprise_info` (`enterprise_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='种子品种登记信息表';

-- 2. 品种审核记录表
CREATE TABLE `variety_audit` (
  `audit_id` VARCHAR(32) NOT NULL COMMENT '审核记录唯一标识（主键，系统生成）',
  `registration_id` VARCHAR(32) NOT NULL COMMENT '关联登记申请唯一标识',
  `enterprise_id` VARCHAR(32) NOT NULL COMMENT '关联企业唯一标识',
  `variety_name` VARCHAR(100) NOT NULL COMMENT '品种名称（冗余）',
  `audit_result` TINYINT(1) NOT NULL COMMENT '审核结果（1-通过/2-驳回）',
  `audit_opinion` VARCHAR(500) NULL COMMENT '审核意见',
  `reject_reason` VARCHAR(500) NULL COMMENT '驳回原因（驳回时必填）',
  `auditor` VARCHAR(50) NOT NULL COMMENT '审核人',
  `audit_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '审核时间',
  `audit_stage` VARCHAR(50) NOT NULL COMMENT '审核阶段（枚举：初审/复审/终审）',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`audit_id`),
  KEY `idx_registration_id` (`registration_id`),
  KEY `idx_enterprise_id` (`enterprise_id`),
  KEY `idx_audit_result` (`audit_result`),
  CONSTRAINT `fk_audit_variety` FOREIGN KEY (`registration_id`) REFERENCES `variety_registration` (`registration_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_audit_variety_enterprise` FOREIGN KEY (`enterprise_id`) REFERENCES `enterprise_info` (`enterprise_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='种子品种审核记录表';

-- 3. 品种发布记录表
CREATE TABLE `variety_publish` (
  `publish_id` VARCHAR(32) NOT NULL COMMENT '发布记录唯一标识（主键，系统生成）',
  `publish_no` VARCHAR(50) NOT NULL COMMENT '发布编号（唯一索引）',
  `registration_id` VARCHAR(32) NOT NULL COMMENT '关联登记申请唯一标识',
  `variety_name` VARCHAR(100) NOT NULL COMMENT '品种名称（冗余）',
  `crop_type` VARCHAR(50) NOT NULL COMMENT '作物类型（冗余）',
  `publish_date` DATE NOT NULL COMMENT '发布日期（默认当前日期）',
  `publish_dept` VARCHAR(100) NOT NULL COMMENT '发布主管部门',
  `decision_explanation` VARCHAR(500) NULL COMMENT '决策说明',
  `public_description` VARCHAR(1000) NULL COMMENT '公开描述',
  `recommended_region` VARCHAR(255) NULL COMMENT '推荐地区',
  `sowing_guide` VARCHAR(1000) NULL COMMENT '播种指南',
  `publish_status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '公示状态（1-公示中/2-已下架）',
  `publisher` VARCHAR(50) NOT NULL COMMENT '发布人',
  `publish_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`publish_id`),
  UNIQUE KEY `idx_publish_no` (`publish_no`),
  KEY `idx_registration_id` (`registration_id`),
  KEY `idx_variety_name` (`variety_name`),
  KEY `idx_publish_status` (`publish_status`),
  CONSTRAINT `fk_publish_variety` FOREIGN KEY (`registration_id`) REFERENCES `variety_registration` (`registration_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='种子品种发布记录表';
