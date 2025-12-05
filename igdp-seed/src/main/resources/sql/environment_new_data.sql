-- ============================================
-- Environment New Data Table
-- 环境监测新数据表
-- ============================================

CREATE TABLE `environment_new_data` (
  `env_record_id` varchar(64) NOT NULL COMMENT '环境记录ID(主键) / Environment Record ID (PK)',
  `trial_id` varchar(64) DEFAULT NULL COMMENT '试验ID(外键) / Trial ID (FK)',
  `batch_id` varchar(64) DEFAULT NULL COMMENT '育种批次ID(外键) / Batch ID (FK)',
  `plot_id` varchar(64) DEFAULT NULL COMMENT '地块ID(可选外键) / Plot ID (Optional FK)',
  `station_id` varchar(50) NOT NULL COMMENT '气象站ID / Weather Station ID',
  `timestamp` datetime NOT NULL COMMENT '数据采集时间 / Data Collection Timestamp',
  `parameter_code` varchar(50) NOT NULL COMMENT '参数代码 / Parameter Code (e.g., RAIN_DAILY, TMAX, TMIN)',
  `value` decimal(15,4) DEFAULT NULL COMMENT '测量值 / Measured Value',
  `unit` varchar(20) DEFAULT NULL COMMENT '测量单位 / Measurement Unit',
  `source` varchar(100) DEFAULT NULL COMMENT '数据来源 / Data Source',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注 / Remarks',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者 / Created By',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间 / Create Time',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者 / Updated By',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间 / Update Time',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除标识(0=未删除,1=已删除) / Logical Delete Flag',
  PRIMARY KEY (`env_record_id`),
  KEY `idx_trial_id` (`trial_id`),
  KEY `idx_batch_id` (`batch_id`),
  KEY `idx_plot_id` (`plot_id`),
  KEY `idx_station_id` (`station_id`),
  KEY `idx_timestamp` (`timestamp`),
  KEY `idx_parameter_code` (`parameter_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='环境监测新数据表 / Environment New Data Table';

-- ============================================
-- Sample Data / 示例数据
-- ============================================

INSERT INTO `environment_new_data` 
(`env_record_id`, `trial_id`, `batch_id`, `plot_id`, `station_id`, `timestamp`, `parameter_code`, `value`, `unit`, `source`, `create_by`, `create_time`) 
VALUES 
('9001', 'WHT-TR-ARSI-2025-01', 'BRD-WHT-2025-001', 'ARSI-R1-P01', 'AWS-ARSI', '2025-08-10 12:00:00', 'RAIN_DAILY', 8.5000, 'mm', 'AWS', 'admin', NOW()),
('9002', 'WHT-TR-ARSI-2025-01', 'BRD-WHT-2025-001', 'ARSI-R1-P01', 'AWS-ARSI', '2025-08-10 12:00:00', 'TMAX', 28.5000, '°C', 'AWS', 'admin', NOW()),
('9003', 'WHT-TR-ARSI-2025-01', 'BRD-WHT-2025-001', 'ARSI-R1-P01', 'AWS-ARSI', '2025-08-10 12:00:00', 'TMIN', 15.2000, '°C', 'AWS', 'admin', NOW()),
('9004', 'WHT-TR-ARSI-2025-01', 'BRD-WHT-2025-001', NULL, 'AWS-ARSI', '2025-08-11 12:00:00', 'HUMIDITY', 65.0000, '%', 'AWS', 'admin', NOW()),
('9005', 'WHT-TR-ARSI-2025-01', 'BRD-WHT-2025-001', NULL, 'AWS-ARSI', '2025-08-11 12:00:00', 'WIND_SPEED', 12.3000, 'm/s', 'AWS', 'admin', NOW());
