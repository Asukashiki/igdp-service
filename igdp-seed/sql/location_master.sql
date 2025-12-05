-- 研究中心表
CREATE TABLE IF NOT EXISTS `location_master` (
  `location_id` varchar(64) NOT NULL COMMENT '位置ID',
  `location_name` varchar(100) DEFAULT NULL COMMENT '位置名称',
  `region` varchar(100) DEFAULT NULL COMMENT '地区',
  `zone` varchar(100) DEFAULT NULL COMMENT '区域',
  `woneda` varchar(100) DEFAULT NULL COMMENT '沃雷达（行政区域）',
  `latitude` decimal(10,2) DEFAULT NULL COMMENT '纬度',
  `longitude` decimal(10,2) DEFAULT NULL COMMENT '经度',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`location_id`),
  KEY `idx_location_name` (`location_name`),
  KEY `idx_region` (`region`),
  KEY `idx_zone` (`zone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='研究中心表';