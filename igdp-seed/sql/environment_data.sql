-- =============================================
-- 环境/IoT数据表
-- Color layer: Purple - Environment Data
-- Table: environment_data
-- =============================================

-- 删除表（如果存在）
DROP TABLE IF EXISTS `environment_data`;

-- 创建表
CREATE TABLE `environment_data` (
  `env_record_id` varchar(64) NOT NULL COMMENT '环境数据记录ID(主键)',
  `trial_id` varchar(64) NOT NULL COMMENT '试验ID',
  `batch_id` varchar(64) NOT NULL COMMENT '育种批次ID',
  `plot_id` varchar(64) DEFAULT NULL COMMENT '地块ID(可选)',
  `station_id` varchar(64) NOT NULL COMMENT '气象站ID',
  `timestamp` datetime NOT NULL COMMENT '时间戳(数据采集时间)',
  `parameter_code` varchar(50) NOT NULL COMMENT '参数代码(如RAIN_DAILY, TMAX, TMIN等)',
  `value` decimal(18,6) NOT NULL COMMENT '数值',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位(如mm, °C, %等)',
  `data_source` varchar(50) DEFAULT NULL COMMENT '数据来源(IOT_SYSTEM/MANUAL/CSV_IMPORT)',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志(0=未删除,2=已删除)',
  PRIMARY KEY (`env_record_id`),
  KEY `idx_trial_id` (`trial_id`),
  KEY `idx_batch_id` (`batch_id`),
  KEY `idx_plot_id` (`plot_id`),
  KEY `idx_station_id` (`station_id`),
  KEY `idx_parameter_code` (`parameter_code`),
  KEY `idx_timestamp` (`timestamp`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='环境/IoT数据表';

-- =============================================
-- 插入示例数据
-- =============================================

-- 示例数据：降雨量数据
INSERT INTO `environment_data`
(`env_record_id`, `trial_id`, `batch_id`, `plot_id`, `station_id`, `timestamp`, `parameter_code`, `value`, `unit`, `data_source`, `create_time`, `create_by`, `del_flag`)
VALUES
('9001', 'WHT-TR-ARSI-2025-01', 'BRD-WHT-2025-001', 'ARSI-R1-P01', 'AWS-ARSI', '2025-08-10 12:00:00', 'RAIN_DAILY', 8.5, 'mm', 'IOT_SYSTEM', NOW(), 'admin', '0');

-- 示例数据：最高温度数据
INSERT INTO `environment_data`
(`env_record_id`, `trial_id`, `batch_id`, `plot_id`, `station_id`, `timestamp`, `parameter_code`, `value`, `unit`, `data_source`, `create_time`, `create_by`, `del_flag`)
VALUES
('9002', 'WHT-TR-ARSI-2025-01', 'BRD-WHT-2025-001', 'ARSI-R1-P01', 'AWS-ARSI', '2025-08-10 14:00:00', 'TMAX', 32.5, '°C', 'IOT_SYSTEM', NOW(), 'admin', '0');

-- 示例数据：最低温度数据
INSERT INTO `environment_data`
(`env_record_id`, `trial_id`, `batch_id`, `plot_id`, `station_id`, `timestamp`, `parameter_code`, `value`, `unit`, `data_source`, `create_time`, `create_by`, `del_flag`)
VALUES
('9003', 'WHT-TR-ARSI-2025-01', 'BRD-WHT-2025-001', 'ARSI-R1-P01', 'AWS-ARSI', '2025-08-10 06:00:00', 'TMIN', 18.2, '°C', 'IOT_SYSTEM', NOW(), 'admin', '0');

-- 示例数据：相对湿度数据
INSERT INTO `environment_data`
(`env_record_id`, `trial_id`, `batch_id`, `plot_id`, `station_id`, `timestamp`, `parameter_code`, `value`, `unit`, `data_source`, `create_time`, `create_by`, `del_flag`)
VALUES
('9004', 'WHT-TR-ARSI-2025-01', 'BRD-WHT-2025-001', 'ARSI-R1-P01', 'AWS-ARSI', '2025-08-10 12:00:00', 'RH_AVG', 65.3, '%', 'IOT_SYSTEM', NOW(), 'admin', '0');

-- =============================================
-- 创建视图：环境数据汇总视图
-- =============================================

CREATE OR REPLACE VIEW `v_environment_data_summary` AS
SELECT
    e.env_record_id,
    e.trial_id,
    t.trial_name,
    e.batch_id,
    b.batch_name,
    e.plot_id,
    p.plot_field_name,
    e.station_id,
    e.timestamp,
    e.parameter_code,
    d.dict_label AS parameter_name,
    e.value,
    e.unit,
    e.data_source,
    e.create_time
FROM environment_data e
LEFT JOIN trial_basic t ON e.trial_id = t.trial_id AND t.del_flag = '0'
LEFT JOIN breeding_batch b ON e.batch_id = b.batch_id AND b.del_flag = '0'
LEFT JOIN plot_info p ON e.plot_id = p.ground_id AND p.del_flag = '0'
LEFT JOIN sys_dict_data d ON e.parameter_code = d.dict_value
    AND d.dict_type = 'environment_parameter'
    AND d.status = '0'
    AND d.del_flag = '0'
WHERE e.del_flag = '0';
