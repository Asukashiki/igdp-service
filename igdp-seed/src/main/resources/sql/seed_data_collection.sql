-- ----------------------------
-- 种子育种数据采集模块数据库表结构
-- ----------------------------

-- ----------------------------
-- 1. 试验基础数据采集表
-- ----------------------------
DROP TABLE IF EXISTS `seed_trial_base_data`;
CREATE TABLE `seed_trial_base_data` (
    `trial_id` VARCHAR(50) NOT NULL COMMENT '试验ID(主键)',
    `crop_type` VARCHAR(50) NOT NULL COMMENT '作物类型',
    `variety_name` VARCHAR(100) NOT NULL COMMENT '品种名称',
    `research_center_id` VARCHAR(50) NOT NULL COMMENT '研究中心ID',
    `program_id` VARCHAR(50) NOT NULL COMMENT '程序ID',
    `sub_program_id` VARCHAR(50) NOT NULL COMMENT '子程序ID',
    `thematic_research_area_id` VARCHAR(50) NOT NULL COMMENT '主题研究领域ID',
    `region` VARCHAR(100) NOT NULL COMMENT '地区',
    `zone` VARCHAR(100) NOT NULL COMMENT '区域',
    `woreda` VARCHAR(100) NOT NULL COMMENT '县',
    `kebele` VARCHAR(100) NOT NULL COMMENT '乡',
    `agro_ecological_zone` VARCHAR(100) COMMENT '农业生态区',
    `gps_location` VARCHAR(200) NOT NULL COMMENT 'GPS位置',
    `start_date` DATE NOT NULL COMMENT '开始日期',
    `activity_code` VARCHAR(50) COMMENT '活动代码',
    `kpi_code` VARCHAR(50) COMMENT 'KPI代码',
    `season` VARCHAR(50) NOT NULL COMMENT '季节',
    `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志(0正常 2删除)',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(500) COMMENT '备注',
    PRIMARY KEY (`trial_id`),
    KEY `idx_crop_type` (`crop_type`),
    KEY `idx_variety_name` (`variety_name`),
    KEY `idx_start_date` (`start_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='试验基础数据采集表';

-- ----------------------------
-- 2. 农民与地块属性数据采集表
-- ----------------------------
DROP TABLE IF EXISTS `seed_farmer_plot_data`;
CREATE TABLE `seed_farmer_plot_data` (
    `data_id` VARCHAR(50) NOT NULL COMMENT '数据ID(主键)',
    `farmer_name` VARCHAR(100) NOT NULL COMMENT '农民姓名',
    `gender` VARCHAR(10) NOT NULL COMMENT '性别',
    `youth_category` VARCHAR(50) NOT NULL COMMENT '青年类别',
    `cooperative_membership` VARCHAR(100) NOT NULL COMMENT '合作社成员资格',
    `plot_size_m2` DECIMAL(10,2) NOT NULL COMMENT '地块面积(平方米)',
    `household_id` VARCHAR(50) COMMENT '家庭ID',
    `contact_phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
    `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志(0正常 2删除)',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(500) COMMENT '备注',
    PRIMARY KEY (`data_id`),
    KEY `idx_farmer_name` (`farmer_name`),
    KEY `idx_contact_phone` (`contact_phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='农民与地块属性数据采集表';

-- ----------------------------
-- 3. 农事记录数据采集表
-- ----------------------------
DROP TABLE IF EXISTS `seed_farming_record_data`;
CREATE TABLE `seed_farming_record_data` (
    `data_id` VARCHAR(50) NOT NULL COMMENT '数据ID(主键)',
    `management_practice` VARCHAR(50) NOT NULL COMMENT '管理措施',
    `fertilizer_type` VARCHAR(50) COMMENT '肥料类型',
    `fertilizer_rate_kg` DECIMAL(10,2) COMMENT '肥料施用量(公斤)',
    `urea_rate_kg` DECIMAL(10,2) COMMENT '尿素施用量(公斤)',
    `pesticide_type` VARCHAR(50) COMMENT '农药类型',
    `irrigation_type` VARCHAR(50) COMMENT '灌溉类型',
    `irrigation_frequency` INT COMMENT '灌溉频率',
    `weeding_date` DATE COMMENT '除草日期',
    `herbicide_used` VARCHAR(100) COMMENT '除草剂使用',
    `seed_source` VARCHAR(100) COMMENT '种子来源',
    `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志(0正常 2删除)',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(500) COMMENT '备注',
    PRIMARY KEY (`data_id`),
    KEY `idx_management_practice` (`management_practice`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='农事记录数据采集表';

-- ----------------------------
-- 4. 农艺性状数据采集表
-- ----------------------------
DROP TABLE IF EXISTS `seed_agronomic_trait_data`;
CREATE TABLE `seed_agronomic_trait_data` (
    `data_id` VARCHAR(50) NOT NULL COMMENT '数据ID(主键)',
    `plant_height_cm` DECIMAL(10,2) NOT NULL COMMENT '植物高度(CM)',
    `tiller_count` INT NOT NULL COMMENT '分蘖数',
    `spike_length_cm` DECIMAL(10,2) NOT NULL COMMENT '穗长(CM)',
    `days_to_emergence` INT NOT NULL COMMENT '天数至出苗期',
    `days_to_tillering` INT NOT NULL COMMENT '天数至分蘖期',
    `days_to_heading` INT NOT NULL COMMENT '天数至抽穗期',
    `days_to_flowering` INT NOT NULL COMMENT '天数至开花期',
    `days_to_grain_filling` INT NOT NULL COMMENT '天数至灌浆期',
    `days_to_maturity` INT NOT NULL COMMENT '天数至成熟期',
    `lodging_score` INT NOT NULL COMMENT '倒伏评分',
    `biomass_weight_kg` DECIMAL(10,2) NOT NULL COMMENT '生物量重量(KG)',
    `spike_density` DECIMAL(10,2) NOT NULL COMMENT '穗密度',
    `grain_weight_per_spike` DECIMAL(10,2) NOT NULL COMMENT '每穗粒重',
    `disease_score` TEXT NOT NULL COMMENT '疾病评分(JSON格式)',
    `stress_indicators` TEXT NOT NULL COMMENT '压力指标(JSON格式)',
    `pest_observation` VARCHAR(500) NOT NULL COMMENT '害虫观察',
    `photo_evidence` VARCHAR(500) COMMENT '照片证据(文件路径)',
    `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志(0正常 2删除)',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(500) COMMENT '备注',
    PRIMARY KEY (`data_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='农艺性状数据采集表';

-- ----------------------------
-- 5. 环境与土壤属性数据采集表
-- ----------------------------
DROP TABLE IF EXISTS `seed_environment_soil_data`;
CREATE TABLE `seed_environment_soil_data` (
    `data_id` VARCHAR(50) NOT NULL COMMENT '数据ID(主键)',
    `soil_ph` DECIMAL(4,2) NOT NULL COMMENT '土壤pH值',
    `soil_ec` DECIMAL(10,2) NOT NULL COMMENT '土壤电导率',
    `soil_nitrogen_percent` DECIMAL(5,2) NOT NULL COMMENT '土壤氮含量(百分比)',
    `soil_phosphorus_ppm` DECIMAL(10,2) NOT NULL COMMENT '土壤磷含量(PPM)',
    `soil_potassium_ppm` DECIMAL(10,2) NOT NULL COMMENT '土壤钾含量(PPM)',
    `previous_crop` VARCHAR(100) COMMENT '前茬作物',
    `water_source` VARCHAR(100) NOT NULL COMMENT '水源',
    `topography` VARCHAR(50) NOT NULL COMMENT '地貌',
    `slope_percent` DECIMAL(5,2) COMMENT '坡度(百分比)',
    `soil_moisture_percent` DECIMAL(5,2) NOT NULL COMMENT '土壤湿度(百分比)',
    `soil_temperature_c` DECIMAL(5,2) NOT NULL COMMENT '土壤温度(摄氏度)',
    `rainfall_mm` DECIMAL(10,2) COMMENT '降雨量(MM)',
    `air_temperature_c` DECIMAL(5,2) COMMENT '空气温度(摄氏度)',
    `humidity_percent` DECIMAL(5,2) NOT NULL COMMENT '湿度(百分比)',
    `wind_speed_ms` DECIMAL(5,2) NOT NULL COMMENT '风速(M/S)',
    `solar_radiation_wm2` DECIMAL(10,2) NOT NULL COMMENT '太阳辐射(W/m2)',
    `timestamp` DATETIME COMMENT '时间戳',
    `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志(0正常 2删除)',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(500) COMMENT '备注',
    PRIMARY KEY (`data_id`),
    KEY `idx_timestamp` (`timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='环境与土壤属性数据采集表';

-- ----------------------------
-- 6. 品种评估数据采集表
-- ----------------------------
DROP TABLE IF EXISTS `seed_variety_evaluation_data`;
CREATE TABLE `seed_variety_evaluation_data` (
    `data_id` VARCHAR(50) NOT NULL COMMENT '数据ID(主键)',
    `plot_id` VARCHAR(50) NOT NULL COMMENT '地块ID',
    `plot_area_m2` DECIMAL(10,2) NOT NULL COMMENT '地块面积(平方米)',
    `grain_weight_kg` DECIMAL(10,2) NOT NULL COMMENT '籽粒重量(KG)',
    `yield_qt_per_ha` DECIMAL(10,2) NOT NULL COMMENT '产量(公担/公顷)',
    `moisture_content` DECIMAL(5,2) NOT NULL COMMENT '含水量(百分比)',
    `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志(0正常 2删除)',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(500) COMMENT '备注',
    PRIMARY KEY (`data_id`),
    KEY `idx_plot_id` (`plot_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='品种评估数据采集表';

-- ----------------------------
-- 7. 实验室测试数据采集表
-- ----------------------------
DROP TABLE IF EXISTS `seed_laboratory_test_data`;
CREATE TABLE `seed_laboratory_test_data` (
    `data_id` VARCHAR(50) NOT NULL COMMENT '数据ID(主键)',
    `sample_id` VARCHAR(50) NOT NULL COMMENT '样本ID',
    `sample_condition` VARCHAR(100) NOT NULL COMMENT '样本状态',
    `germination_rate` DECIMAL(5,2) NOT NULL COMMENT '发芽率(百分比)',
    `purity_percent` DECIMAL(5,2) NOT NULL COMMENT '纯度(百分比)',
    `moisture_content_percent` DECIMAL(5,2) NOT NULL COMMENT '含水量(百分比)',
    `protein_percent` DECIMAL(5,2) NOT NULL COMMENT '蛋白质(百分比)',
    `toxin_level_ppm` DECIMAL(10,2) COMMENT '毒素水平(PPM)',
    `seed_health_findings` VARCHAR(500) NOT NULL COMMENT '种子健康发现',
    `traceability_link` VARCHAR(200) NOT NULL COMMENT '链路责任',
    `lab_report_file` VARCHAR(500) COMMENT '实验室报告文件(文件路径)',
    `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志(0正常 2删除)',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(500) COMMENT '备注',
    PRIMARY KEY (`data_id`),
    KEY `idx_sample_id` (`sample_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实验室测试数据采集表';
