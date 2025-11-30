-- 物联网传感器信息表
CREATE TABLE `iot_sensor_info` (
  `data_id` varchar(32) NOT NULL COMMENT '主键ID',
  `iot_id` varchar(32) NOT NULL COMMENT '传感器编号',
  `iot_name` varchar(100) DEFAULT NULL COMMENT '传感器名称',
  `iot_type` char(2) NOT NULL COMMENT '传感器类型（01-温度 02-湿度 03-光照 04-土壤 05-气体 99-其他）',
  `manufacturer` varchar(32) NOT NULL COMMENT '制造商',
  `calibration_date` datetime NOT NULL COMMENT '校准日期',
  `firmware_version` varchar(32) NOT NULL COMMENT '固件版本',
  `battery_status` varchar(64) DEFAULT NULL COMMENT '电池状态',
  `org_id` varchar(32) DEFAULT NULL COMMENT '操作机构ID',
  `org_name` varchar(100) DEFAULT NULL COMMENT '操作机构名称',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0-正常 2-删除）',
  PRIMARY KEY (`data_id`),
  KEY `idx_iot_id` (`iot_id`),
  KEY `idx_iot_type` (`iot_type`),
  KEY `idx_org_id` (`org_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物联网传感器信息表';
