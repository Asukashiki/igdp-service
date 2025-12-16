-- 创建区域分配额度相关表

-- 区域分配额度主表
CREATE TABLE `t_zone_allocation` (
  `id` varchar(36) NOT NULL COMMENT '分配ID',
  `allocation_name` varchar(255) NOT NULL COMMENT '分配名称',
  `year` varchar(4) NOT NULL COMMENT '年份',
  `zone` varchar(36) NOT NULL COMMENT '区域编码',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_year_zone` (`year`,`zone`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='区域分配额度主表';

-- 区域分配额度需求项表
CREATE TABLE `t_zone_allocation_demand` (
  `id` varchar(36) NOT NULL COMMENT '需求项ID',
  `allocation_id` varchar(36) NOT NULL COMMENT '分配ID',
  `input_type` varchar(20) NOT NULL COMMENT '投入品类型',
  `input_category` varchar(20) NOT NULL COMMENT '投入品类别',
  `total_quantity` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '总数量',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_allocation_id` (`allocation_id`),
  KEY `idx_input_type_category` (`input_type`,`input_category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='区域分配额度需求项表';

-- 区域分配额度配项表
CREATE TABLE `t_zone_allocation_quota` (
  `id` varchar(36) NOT NULL COMMENT '配项ID',
  `allocation_id` varchar(36) NOT NULL COMMENT '分配ID',
  `input_type` varchar(20) NOT NULL COMMENT '投入品类型',
  `input_category` varchar(20) NOT NULL COMMENT '投入品类别',
  `total_quantity` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '总数量',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_allocation_id` (`allocation_id`),
  KEY `idx_input_type_category` (`input_type`,`input_category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='区域分配额度配项表';