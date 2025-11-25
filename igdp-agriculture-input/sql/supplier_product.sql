-- ========================================
-- 供应商投入品关系表 (supplier_product)
-- ========================================

DROP TABLE IF EXISTS `supplier_product`;

CREATE TABLE `supplier_product` (
  `supplier_product_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '供应关系ID',
  `supplier_id` BIGINT NOT NULL COMMENT '供应商ID（关联supplier_cert表的user_id）',
  `input_id` BIGINT NOT NULL COMMENT '投入品ID（关联agri_input表）',
  `supplier_product_code` VARCHAR(100) DEFAULT NULL COMMENT '供应商产品编码',
  `supplier_product_name` VARCHAR(200) DEFAULT NULL COMMENT '供应商产品名称',
  `quality_rating` CHAR(1) DEFAULT NULL COMMENT '质量评级：A/B/C/D',
  `notes` TEXT DEFAULT NULL COMMENT '供应备注',
  `create_people` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_people` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志：0-正常，2-删除',
  PRIMARY KEY (`supplier_product_id`),
  UNIQUE KEY `uk_supplier_input` (`supplier_id`, `input_id`, `del_flag`),
  KEY `idx_supplier_id` (`supplier_id`),
  KEY `idx_input_id` (`input_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商投入品关系表';

-- ========================================
-- 初始化测试数据（可选）
-- ========================================

-- 假设 supplier_id=10001 是已通过认证的供应商
-- 假设 input_id=1,2,3 是已存在的投入品

-- 插入供应商投入品关系示例
INSERT INTO `supplier_product` (
  `supplier_id`, `input_id`, `supplier_product_code`, `supplier_product_name`,
  `quality_rating`, `notes`, `create_people`, `create_time`, `del_flag`
) VALUES
(10001, 1, 'SP-001', '优质农药A', 'A', '高质量农药产品', 'system', NOW(), '0'),
(10001, 2, 'SP-002', '有机化肥B', 'A', '环保型化肥', 'system', NOW(), '0'),
(10002, 1, 'SP-101', '农药A', 'B', '普通农药产品', 'system', NOW(), '0'),
(10002, 3, 'SP-103', '优质种子C', 'A', '高产种子', 'system', NOW(), '0');

-- ========================================
-- 说明
-- ========================================
-- 1. supplier_id 关联 supplier_cert 表的 user_id（供应商用户ID）
-- 2. input_id 关联 agri_input 表的 input_id（投入品ID）
-- 3. 唯一约束 uk_supplier_input 确保同一供应商对同一投入品只能有一条有效记录（del_flag='0'）
-- 4. quality_rating 质量评级：A-优秀，B-良好，C-一般，D-较差
-- 5. del_flag 逻辑删除标志：0-正常，2-已删除
