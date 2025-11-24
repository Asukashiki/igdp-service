-- =============================================
-- 农业投入品管理模块数据库脚本
-- =============================================

-- ----------------------------
-- 1. 农业投入品基础表
-- ----------------------------
DROP TABLE IF EXISTS `agri_input`;
CREATE TABLE `agri_input` (
  `input_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '投入品ID',
  `input_name` varchar(100) NOT NULL COMMENT '投入品名称',
  `type` varchar(20) NOT NULL COMMENT '类型(pesticide-农药/fertilizer-化肥/seed-种子/other-其他)',
  `input_sku` varchar(100) NOT NULL COMMENT '唯一产品标识码/SKU',
  `trademark` varchar(100) NOT NULL COMMENT '注册商标',
  `register_code` varchar(100) NOT NULL COMMENT '登记批号',
  `production_license` varchar(100) NOT NULL COMMENT '生产许可证号',
  `production_standard` varchar(100) NOT NULL COMMENT '产品标准证号',
  `producer_name` varchar(200) NOT NULL COMMENT '生产企业名称',
  `producer_address` varchar(255) NOT NULL COMMENT '生产企业地址',
  `status` varchar(20) DEFAULT 'active' COMMENT '状态(active-正常/inactive-停用)',
  `create_people` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_people` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志(0-正常/2-删除)',
  PRIMARY KEY (`input_id`),
  UNIQUE KEY `uk_input_sku` (`input_sku`),
  KEY `idx_type` (`type`),
  KEY `idx_status` (`status`),
  KEY `idx_register_code` (`register_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='农业投入品基础表';

-- ----------------------------
-- 2. 农药特性表
-- ----------------------------
DROP TABLE IF EXISTS `agri_pesticide_properties`;
CREATE TABLE `agri_pesticide_properties` (
  `pesticide_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '农药特性ID',
  `input_id` bigint(20) NOT NULL COMMENT '关联产品ID',
  `total_ingredient_content` varchar(50) DEFAULT NULL COMMENT '总有效成分含量',
  `toxicity_level` varchar(20) DEFAULT NULL COMMENT '毒性等级(微毒/低毒/中等毒/高毒/剧毒)',
  `target_crops` text COMMENT '适用作物',
  `control_targets` text COMMENT '防治对象',
  `application_method` varchar(200) DEFAULT NULL COMMENT '施用方法',
  `dosage` varchar(200) DEFAULT NULL COMMENT '使用剂量',
  `dilution_ratio` varchar(100) DEFAULT NULL COMMENT '稀释倍数',
  `safety_interval` int(11) DEFAULT NULL COMMENT '安全间隔期(天)',
  `precautions` text COMMENT '注意事项',
  `first_aid` text COMMENT '中毒急救措施',
  `storage_requirements` text COMMENT '储存要求',
  PRIMARY KEY (`pesticide_id`),
  UNIQUE KEY `uk_input_id` (`input_id`),
  CONSTRAINT `fk_pesticide_input` FOREIGN KEY (`input_id`) REFERENCES `agri_input` (`input_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='农药特性表';

-- ----------------------------
-- 3. 化肥特性表
-- ----------------------------
DROP TABLE IF EXISTS `agri_fertilizer_properties`;
CREATE TABLE `agri_fertilizer_properties` (
  `fertilizer_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '化肥特性ID',
  `input_id` bigint(20) NOT NULL COMMENT '关联产品ID',
  `fertilizer_type` varchar(50) DEFAULT NULL COMMENT '肥料类型',
  `total_nutrient_content` varchar(50) DEFAULT NULL COMMENT '总养分含量',
  `nitrogen_content` varchar(50) DEFAULT NULL COMMENT '氮含量',
  `phosphorus_content` varchar(50) DEFAULT NULL COMMENT '磷含量(P2O5)',
  `potassium_content` varchar(50) DEFAULT NULL COMMENT '钾含量(K2O)',
  `organic_matter_content` varchar(50) DEFAULT NULL COMMENT '有机质含量',
  `medium_trace_elements` text COMMENT '中微量元素',
  `ph_value` varchar(20) DEFAULT NULL COMMENT 'pH值',
  `suitable_crops` text COMMENT '适用作物',
  `application_period` varchar(200) DEFAULT NULL COMMENT '施用时期',
  `application_method` varchar(200) DEFAULT NULL COMMENT '施用方法',
  `recommended_dosage` varchar(200) DEFAULT NULL COMMENT '建议用量',
  PRIMARY KEY (`fertilizer_id`),
  UNIQUE KEY `uk_input_id` (`input_id`),
  CONSTRAINT `fk_fertilizer_input` FOREIGN KEY (`input_id`) REFERENCES `agri_input` (`input_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='化肥特性表';

-- ----------------------------
-- 4. 种子特性表
-- ----------------------------
DROP TABLE IF EXISTS `agri_seed_properties`;
CREATE TABLE `agri_seed_properties` (
  `seed_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '种子特性ID',
  `input_id` bigint(20) NOT NULL COMMENT '关联产品ID',
  `crop_type` varchar(100) DEFAULT NULL COMMENT '作物种类',
  `variety_name` varchar(200) DEFAULT NULL COMMENT '品种名称',
  `variety_approval_code` varchar(100) DEFAULT NULL COMMENT '品种审定编号',
  `variety_source` text COMMENT '品种来源',
  `purity` decimal(5,2) DEFAULT NULL COMMENT '纯度(%)',
  `cleanliness` decimal(5,2) DEFAULT NULL COMMENT '净度(%)',
  `germination_rate` decimal(5,2) DEFAULT NULL COMMENT '发芽率(%)',
  `moisture_content` decimal(5,2) DEFAULT NULL COMMENT '水分含量(%)',
  PRIMARY KEY (`seed_id`),
  UNIQUE KEY `uk_input_id` (`input_id`),
  CONSTRAINT `fk_seed_input` FOREIGN KEY (`input_id`) REFERENCES `agri_input` (`input_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='种子特性表';

-- ----------------------------
-- 初始化测试数据
-- ----------------------------
INSERT INTO `agri_input` VALUES
(1, '高效氯氟氰菊酯乳油', 'pesticide', 'PEST-2024-001', '绿盾', 'PD20240001', 'XK13-003-00123', 'GB 20684-2006', '山东绿盾农药有限公司', '山东省济南市历城区工业园区', 'active', 'system', NOW(), NULL, NULL, '0'),
(2, '草甘膦异丙胺盐水剂', 'pesticide', 'PEST-2024-002', '除草王', 'PD20240002', 'XK13-003-00124', 'GB 20685-2006', '江苏农化科技股份有限公司', '江苏省南京市江宁区科技园', 'active', 'system', NOW(), NULL, NULL, '0'),
(3, '复合肥料(15-15-15)', 'fertilizer', 'FERT-2024-001', '丰收', 'FD20240001', 'XK13-001-00230', 'GB 15063-2020', '河北丰收化肥集团有限公司', '河北省石家庄市藁城区化肥工业园', 'active', 'system', NOW(), NULL, NULL, '0'),
(4, '有机肥料', 'fertilizer', 'FERT-2024-002', '绿野', 'FD20240002', 'XK13-001-00231', 'NY 525-2021', '山西绿野有机肥业有限公司', '山西省太原市清徐县农业示范园区', 'active', 'system', NOW(), NULL, NULL, '0'),
(5, '登海605玉米种子', 'seed', 'SEED-2024-001', '登海', 'SD20240001', 'XK13-002-00156', 'GB 4404.1-2008', '山东登海种业股份有限公司', '山东省莱州市城港路1336号', 'active', 'system', NOW(), NULL, NULL, '0'),
(6, '郑单958玉米种子', 'seed', 'SEED-2024-002', '郑单', 'SD20240002', 'XK13-002-00157', 'GB 4404.1-2008', '河南省农业科学院', '河南省郑州市花园路116号', 'active', 'system', NOW(), NULL, NULL, '0'),
(7, '农用塑料薄膜', 'other', 'OTHER-2024-001', '强力', 'OD20240001', 'XK13-004-00089', 'GB 13735-2017', '江苏强力塑料制品有限公司', '江苏省常州市武进区塑料工业园', 'active', 'system', NOW(), NULL, NULL, '0');

INSERT INTO `agri_pesticide_properties` VALUES
(1, 1, '4.5%', '低毒', '水稻、小麦、玉米、果树', '蚜虫、菜青虫、小菜蛾、红蜘蛛', '喷雾', '30-50ml/亩', '1500-2000倍', 7, '不可在强风天气或雨前施药；施药人员应佩戴防护用具', '如误服，立即催吐并送医；如皮肤接触，用清水冲洗15分钟以上', '存放于阴凉干燥处，远离食品和饲料'),
(2, 2, '41%', '低毒', '果园、玉米田、大豆田', '一年生和多年生杂草', '茎叶喷雾', '100-200ml/亩', '100-150倍', 0, '定向喷雾，避免药液飘移到作物上；施药后6小时内遇雨需重喷', '误服立即催吐并就医；眼睛接触用大量清水冲洗', '密封保存于阴凉通风处，避免阳光直射');

INSERT INTO `agri_fertilizer_properties` VALUES
(1, 3, '复合肥', '≥45%', '15%', '15%', '15%', '', '硫≥3%，锌≥0.5%', '5.5-7.5', '水稻、小麦、玉米、蔬菜', '基肥、追肥', '撒施、条施、穴施', '40-80kg/亩'),
(2, 4, '有机肥', '≥5%', '≥2%', '≥1.5%', '≥1.5%', '≥45%', '钙、镁、硼、锌等', '5.5-8.5', '果树、蔬菜、粮食作物、经济作物', '基肥为主', '撒施后翻耕', '200-400kg/亩');

INSERT INTO `agri_seed_properties` VALUES
(1, 5, '玉米', '登海605', '国审玉20196065', '登海3632×登海Y3269', 98.00, 99.00, 92.00, 12.00),
(2, 6, '玉米', '郑单958', '国审玉2000016', '郑58×昌7-2', 98.00, 99.00, 90.00, 13.00);
