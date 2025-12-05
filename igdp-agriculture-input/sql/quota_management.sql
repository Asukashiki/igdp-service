-- ====================================================
-- Agricultural Input Quota Management System
-- Tables: t_state_annual_quota, t_quota_allocation
-- ====================================================

-- Table 1: State Annual Quota (州级投入品年度配额表)
DROP TABLE IF EXISTS `t_state_annual_quota`;
CREATE TABLE `t_state_annual_quota` (
    `quota_id` VARCHAR(36) NOT NULL COMMENT '配额唯一标识(UUID)',
    `quota_name` VARCHAR(200) NOT NULL COMMENT '配额名称(格式:年度_州名_投入品类别)',
    `year` INT NOT NULL COMMENT '配额年度',
    `category_id` VARCHAR(36) NOT NULL COMMENT '投入品类别ID',
    `total_quota` DECIMAL(18, 2) NOT NULL COMMENT '年度总配额量',
    `operator_id` VARCHAR(36) NOT NULL COMMENT '录入操作员ID',
    `operator_division_id` VARCHAR(36) NOT NULL COMMENT '操作员所属州级区划ID',
    `modifier_id` VARCHAR(36) DEFAULT NULL COMMENT '修改人ID',
    `modifier_division_id` VARCHAR(36) DEFAULT NULL COMMENT '修改人所属区划ID',
    `operate_time` DATETIME NOT NULL COMMENT '操作时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`quota_id`),
    INDEX `idx_year` (`year`),
    INDEX `idx_category_id` (`category_id`),
    INDEX `idx_operator_division_id` (`operator_division_id`),
    UNIQUE KEY `uk_year_category_division` (`year`, `category_id`, `operator_division_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='州级投入品年度配额表';


-- Table 2: Quota Allocation (投入品配额逐级分配表)
DROP TABLE IF EXISTS `t_quota_allocation`;
CREATE TABLE `t_quota_allocation` (
    `allocation_id` VARCHAR(36) NOT NULL COMMENT '分配记录唯一标识(UUID)',
    `allocation_name` VARCHAR(300) NOT NULL COMMENT '分配记录名称(格式:年度_类别_分配方层级_分配方名称_to_接收方层级_接收方名称)',
    `year` INT NOT NULL COMMENT '分配年度',
    `category_id` VARCHAR(36) NOT NULL COMMENT '投入品类别ID',
    `from_division_id` VARCHAR(36) NOT NULL COMMENT '分配方区划ID',
    `from_division_level` INT NOT NULL COMMENT '分配方层级(1=州、2=区、3=镇、4=村)',
    `from_parent_division_id` VARCHAR(36) DEFAULT NULL COMMENT '分配方父级区划ID',
    `to_division_id` VARCHAR(36) DEFAULT NULL COMMENT '接收方区划ID(分配给农民时为空)',
    `to_farmer_id` VARCHAR(36) DEFAULT NULL COMMENT '接收方农民ID(分配给农民时必填)',
    `allocated_quota` DECIMAL(18, 2) NOT NULL COMMENT '单次分配量',
    `quota_id` VARCHAR(36) NOT NULL COMMENT '关联州级总配额ID',
    `total_received_quota` DECIMAL(18, 2) NOT NULL COMMENT '分配方本级接收的总配额量',
    `total_allocated_quota` DECIMAL(18, 2) NOT NULL DEFAULT 0.00 COMMENT '分配方累计已分配量',
    `remaining_quota` DECIMAL(18, 2) NOT NULL COMMENT '分配方剩余配额量',
    `allocation_status` INT NOT NULL DEFAULT 0 COMMENT '分配状态(0=未分配、1=部分分配、2=已完成)',
    `operator_id` VARCHAR(36) NOT NULL COMMENT '分配操作员ID',
    `operator_division_id` VARCHAR(36) NOT NULL COMMENT '操作员所属区划ID',
    `modifier_id` VARCHAR(36) DEFAULT NULL COMMENT '修改人ID',
    `modifier_division_id` VARCHAR(36) DEFAULT NULL COMMENT '修改人所属区划ID',
    `operate_time` DATETIME NOT NULL COMMENT '操作时间',
    `progress_update_time` DATETIME NOT NULL COMMENT '进度更新时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`allocation_id`),
    INDEX `idx_year` (`year`),
    INDEX `idx_category_id` (`category_id`),
    INDEX `idx_from_division` (`from_division_id`, `from_division_level`),
    INDEX `idx_to_division` (`to_division_id`),
    INDEX `idx_to_farmer` (`to_farmer_id`),
    INDEX `idx_quota_id` (`quota_id`),
    INDEX `idx_allocation_status` (`allocation_status`),
    INDEX `idx_operator_division_id` (`operator_division_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投入品配额逐级分配表';
