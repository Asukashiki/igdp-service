-- ----------------------------
-- Breeder Seed 生产主表
-- ----------------------------
DROP TABLE IF EXISTS `breed_seed_produce`;
CREATE TABLE `breed_seed_produce` (
  `breed_seed_produce_batch_id` VARCHAR(32) NOT NULL COMMENT '主键（UUID），唯一',
  `breed_batch_id` VARCHAR(32) NOT NULL COMMENT '外键，关联育种批次表',
  `variety_id` VARCHAR(32) NOT NULL COMMENT '外键，关联品种发布表',
  `variety_name` VARCHAR(50) NOT NULL COMMENT '品种名称（自动从variety_publish表带出）',
  `crop_type` VARCHAR(32) NOT NULL COMMENT '作物类型（自动从variety_publish表带出）',
  `time` DATETIME NOT NULL COMMENT '生产时间',
  `land_id` VARCHAR(32) NOT NULL COMMENT '外键，关联土地信息表',
  `land_name` VARCHAR(100) NOT NULL COMMENT '地块名称（自动从land_info表带出）',
  `input_seed_quantity` DECIMAL(10,2) NOT NULL COMMENT '投入种子数量，≥0',
  `produce_seed_quantrity` DECIMAL(10,2) NOT NULL COMMENT '产出种子数量，≥0且≥投入数量',
  `operator_id` VARCHAR(32) NOT NULL COMMENT '外键，关联系统用户表',
  `operator_name` VARCHAR(50) NOT NULL COMMENT '操作人姓名（自动带出）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据修改时间',
  `produce_status` VARCHAR(20) NOT NULL DEFAULT 'FINISHED' COMMENT '生产状态，默认已完成',
  PRIMARY KEY (`breed_seed_produce_batch_id`),
  KEY `idx_breed_batch_id` (`breed_batch_id`),
  KEY `idx_variety_id` (`variety_id`),
  KEY `idx_land_id` (`land_id`),
  KEY `idx_operator_id` (`operator_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='Breeder seed 生产主表';

-- ----------------------------
-- Breeder Seed 分发主表
-- ----------------------------
DROP TABLE IF EXISTS `breed_seed_distribute_main`;
CREATE TABLE `breed_seed_distribute_main` (
  `distribute_id` VARCHAR(32) NOT NULL COMMENT '主键（UUID），唯一',
  `ose_id` VARCHAR(32) NOT NULL COMMENT '外键，关联OSE表',
  `ose_name` VARCHAR(100) NOT NULL COMMENT 'OSE名称（自动从ose_info表带出）',
  `time` DATETIME NOT NULL COMMENT '分发时间',
  `people` VARCHAR(50) NOT NULL COMMENT '分发操作人姓名',
  `organ` VARCHAR(100) NOT NULL COMMENT '种子机构名称（固定值）',
  `remark` VARCHAR(200) DEFAULT NULL COMMENT '备注信息',
  `total_distribute_quantity` DECIMAL(10,2) NOT NULL COMMENT '明细数量总和',
  `distribute_status` VARCHAR(20) NOT NULL DEFAULT '已分发' COMMENT '分发状态，默认已分发',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据修改时间',
  PRIMARY KEY (`distribute_id`),
  KEY `idx_ose_id` (`ose_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='Breeder seed 分发主表';

-- ----------------------------
-- Breeder Seed 分发明细表
-- ----------------------------
DROP TABLE IF EXISTS `breed_seed_distribute_detail`;
CREATE TABLE `breed_seed_distribute_detail` (
  `distribute_detail_id` VARCHAR(32) NOT NULL COMMENT '主键（UUID），唯一',
  `distribute_id` VARCHAR(32) NOT NULL COMMENT '外键，关联分发主表',
  `breed_seed_produce_batch_id` VARCHAR(32) NOT NULL COMMENT '外键，关联生产表',
  `variety_name` VARCHAR(50) NOT NULL COMMENT '品种名称（自动从breed_seed_produce表带出）',
  `crop_type` VARCHAR(32) NOT NULL COMMENT '作物类型（自动从breed_seed_produce表带出）',
  `distribute_quantity` DECIMAL(10,2) NOT NULL COMMENT '分发数量，≥0且≤生产批次剩余量',
  `produce_batch_remaining` DECIMAL(10,2) NOT NULL COMMENT '生产批次剩余可分发量',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据创建时间',
  PRIMARY KEY (`distribute_detail_id`),
  KEY `idx_distribute_id` (`distribute_id`),
  KEY `idx_produce_batch_id` (`breed_seed_produce_batch_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='Breeder seed 分发明细表';

-- ----------------------------
-- OSE基础信息表
-- ----------------------------
DROP TABLE IF EXISTS `ose_info`;
CREATE TABLE `ose_info` (
  `ose_id` VARCHAR(32) NOT NULL COMMENT 'OSE唯一编码，唯一约束',
  `ose_code` VARCHAR(32) NOT NULL COMMENT 'OSE行政编码，唯一约束',
  `ose_name` VARCHAR(100) NOT NULL COMMENT 'OSE名称，非空且唯一',
  `location` VARCHAR(200) NOT NULL COMMENT '详细地址，非空',
  `region_code` VARCHAR(32) NOT NULL COMMENT '外键，关联行政区划表',
  `region_name` VARCHAR(100) NOT NULL COMMENT '行政区划名称（自动从region_info表带出）',
  `contact_name` VARCHAR(50) NOT NULL COMMENT '联系人姓名，非空',
  `contact_number` VARCHAR(20) NOT NULL COMMENT '联系人电话，唯一且符合埃塞号码格式',
  `ose_status` VARCHAR(20) NOT NULL DEFAULT 'ENABLED' COMMENT '状态：启用（ENABLED）/禁用（DISABLED）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据修改时间',
  PRIMARY KEY (`ose_id`),
  UNIQUE KEY `uk_ose_code` (`ose_code`),
  UNIQUE KEY `uk_ose_name` (`ose_name`),
  UNIQUE KEY `uk_contact_number` (`contact_number`),
  KEY `idx_region_code` (`region_code`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='OSE基础信息表';

-- ----------------------------
-- OSE接收育种家种子确认表
-- ----------------------------
DROP TABLE IF EXISTS `ose_breed_seed_receive_confirm`;
CREATE TABLE `ose_breed_seed_receive_confirm` (
  `receive_confirm_id` VARCHAR(32) NOT NULL COMMENT '主键，自生成',
  `distribute_id` VARCHAR(32) NOT NULL COMMENT '外键，关联分发主表',
  `ose_id` VARCHAR(32) NOT NULL COMMENT '外键，关联OSE信息表',
  `confirm_time` DATETIME DEFAULT NULL COMMENT '确认时间',
  `confirm_people` VARCHAR(100) DEFAULT NULL COMMENT '确认操作人姓名',
  `receive_status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '接收状态：待确认（PENDING）/已确认（CONFIRMED）',
  `remark` VARCHAR(200) DEFAULT NULL COMMENT '补充说明',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '状态变更时间',
  PRIMARY KEY (`receive_confirm_id`),
  KEY `idx_distribute_id` (`distribute_id`),
  KEY `idx_ose_id` (`ose_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='OSE接收育种家种子确认表';

-- ----------------------------
-- 注意事项
-- ----------------------------
-- 1. 上述建表语句中关联的 breed_batch（育种批次管理表）、variety_publish（品种管理-发布表）、
--    land_info（土地信息表）、sys_user（系统用户表）、region_info（行政区划表）需提前创建，确保外键关联正常。
-- 2. 字段类型、长度及约束可根据实际业务场景调整，需保证与接口参数一致。
-- 3. 字符集统一使用 utf8mb4，支持多语言字符存储；存储引擎使用 InnoDB，支持事务和外键约束。
-- 4. 分发数量验证通过应用层实现，确保分发数量不超过生产批次剩余可分发量。
-- 5. 建议在分发主表新增记录时，自动在接收确认表中创建对应记录（状态为PENDING）。
