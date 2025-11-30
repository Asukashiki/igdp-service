-- ============================================================
-- 繁殖种子认证申请相关表结构
-- 数据库: MySQL
-- 作者: igdp
-- 日期: 2025-11-29
-- ============================================================

-- ----------------------------
-- 1. 繁殖种子认证申请主表
-- ----------------------------
DROP TABLE IF EXISTS `seed_breeding_seed_certification`;
CREATE TABLE `seed_breeding_seed_certification` (
  `data_id` varchar(64) NOT NULL COMMENT '数据ID(主键)',
  `breeding_batch_id` varchar(64) DEFAULT NULL COMMENT '繁育批次ID',
  `auth_id` varchar(64) NOT NULL COMMENT '认证ID',
  `apply_org_name` varchar(200) DEFAULT NULL COMMENT '申请机构名称',
  `apply_org_id` varchar(64) DEFAULT NULL COMMENT '申请机构ID',
  `record_date` date DEFAULT NULL COMMENT '备案日期',
  `crop_type` varchar(50) NOT NULL COMMENT '作物类型',
  `variety_name` varchar(200) NOT NULL COMMENT '品种名称',
  `record_status` varchar(20) NOT NULL COMMENT '备案状态',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志(0正常 2删除)',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`data_id`),
  KEY `idx_auth_id` (`auth_id`),
  KEY `idx_breeding_batch_id` (`breeding_batch_id`),
  KEY `idx_apply_org` (`apply_org_id`),
  KEY `idx_crop_variety` (`crop_type`, `variety_name`(100)),
  KEY `idx_record_date` (`record_date`),
  KEY `idx_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='繁殖种子认证申请表';

-- ----------------------------
-- 2. 繁殖种子品种信息表
-- ----------------------------
DROP TABLE IF EXISTS `seed_breeding_seed_variety_info`;
CREATE TABLE `seed_breeding_seed_variety_info` (
  `data_id` varchar(64) NOT NULL COMMENT '数据ID(主键)',
  `breeding_batch_id` varchar(64) NOT NULL COMMENT '繁育批次ID',
  `auth_id` varchar(64) NOT NULL COMMENT '认证ID',
  `variety_name` varchar(200) NOT NULL COMMENT '品种名称',
  `variety_code` varchar(100) NOT NULL COMMENT '品种代码',
  `crop_type` varchar(50) NOT NULL COMMENT '作物类型',
  `species` varchar(200) NOT NULL COMMENT '物种',
  `genus` varchar(100) NOT NULL COMMENT '属',
  `family` varchar(100) NOT NULL COMMENT '科',
  `breeding_method` varchar(50) NOT NULL COMMENT '培育方法',
  `pedigree` varchar(500) NOT NULL COMMENT '系谱',
  `breeding_year` int(11) NOT NULL COMMENT '培育年份',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志(0正常 2删除)',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`data_id`),
  KEY `idx_auth_id` (`auth_id`),
  KEY `idx_breeding_batch_id` (`breeding_batch_id`),
  KEY `idx_variety_code` (`variety_code`),
  KEY `idx_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='繁殖种子品种信息表';

-- ----------------------------
-- 3. 繁殖种子技术性状信息表
-- ----------------------------
DROP TABLE IF EXISTS `seed_breeding_seed_technical_trait`;
CREATE TABLE `seed_breeding_seed_technical_trait` (
  `data_id` varchar(64) NOT NULL COMMENT '数据ID(主键)',
  `breeding_batch_id` varchar(64) NOT NULL COMMENT '繁育批次ID',
  `auth_id` varchar(64) NOT NULL COMMENT '认证ID',
  `min_yield_potential` decimal(10,2) NOT NULL COMMENT '最低产量潜力',
  `max_yield_potential` decimal(10,2) NOT NULL COMMENT '最高产量潜力',
  `disease_resistance` varchar(500) NOT NULL COMMENT '抗病性',
  `stress_resistance` varchar(500) NOT NULL COMMENT '抗逆性',
  `maturity_period` int(11) NOT NULL COMMENT '成熟期',
  `plant_height` decimal(10,2) NOT NULL COMMENT '株高',
  `grain_quality_trait` varchar(500) NOT NULL COMMENT '谷物质量性状',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志(0正常 2删除)',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`data_id`),
  KEY `idx_auth_id` (`auth_id`),
  KEY `idx_breeding_batch_id` (`breeding_batch_id`),
  KEY `idx_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='繁殖种子技术性状信息表';

-- ----------------------------
-- 4. 繁殖种子试验与性能信息表
-- ----------------------------
DROP TABLE IF EXISTS `seed_breeding_seed_trial_performance`;
CREATE TABLE `seed_breeding_seed_trial_performance` (
  `data_id` varchar(64) NOT NULL COMMENT '数据ID(主键)',
  `breeding_batch_id` varchar(64) NOT NULL COMMENT '繁育批次ID',
  `auth_id` varchar(64) NOT NULL COMMENT '认证ID',
  `trial_location` varchar(200) NOT NULL COMMENT '试验地点',
  `trial_year` int(11) NOT NULL COMMENT '试验年份',
  `average_yield` decimal(10,2) NOT NULL COMMENT '平均产量',
  `stability_score` decimal(10,2) NOT NULL COMMENT '稳定性评分',
  `trial_report` varchar(500) NOT NULL COMMENT '试验报告(文件路径)',
  `photo` varchar(500) NOT NULL COMMENT '照片(文件路径)',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志(0正常 2删除)',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`data_id`),
  KEY `idx_auth_id` (`auth_id`),
  KEY `idx_breeding_batch_id` (`breeding_batch_id`),
  KEY `idx_trial_year` (`trial_year`),
  KEY `idx_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='繁殖种子试验与性能信息表';

-- ----------------------------
-- 5. 繁殖种子监管信息表
-- ----------------------------
DROP TABLE IF EXISTS `seed_breeding_seed_supervision`;
CREATE TABLE `seed_breeding_seed_supervision` (
  `data_id` varchar(64) NOT NULL COMMENT '数据ID(主键)',
  `breeding_batch_id` varchar(64) NOT NULL COMMENT '繁育批次ID',
  `auth_id` varchar(64) NOT NULL COMMENT '认证ID',
  `approval_number` varchar(100) NOT NULL COMMENT '批准编号',
  `approval_organization` varchar(200) NOT NULL COMMENT '批准机构',
  `approval_date` date NOT NULL COMMENT '批准日期',
  `certification_document` varchar(500) NOT NULL COMMENT '认证文件(文件路径)',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志(0正常 2删除)',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`data_id`),
  KEY `idx_auth_id` (`auth_id`),
  KEY `idx_breeding_batch_id` (`breeding_batch_id`),
  KEY `idx_approval_number` (`approval_number`),
  KEY `idx_approval_date` (`approval_date`),
  KEY `idx_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='繁殖种子监管信息表';
