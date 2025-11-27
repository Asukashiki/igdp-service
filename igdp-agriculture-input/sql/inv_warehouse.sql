-- 仓库表
DROP TABLE IF EXISTS `inv_warehouse`;
CREATE TABLE `inv_warehouse` (
  `warehouse_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '仓库ID',
  `warehouse_code` VARCHAR(50) NOT NULL COMMENT '仓库编号',
  `warehouse_name` VARCHAR(100) NOT NULL COMMENT '仓库名称',
  `warehouse_type` VARCHAR(20) NOT NULL COMMENT '仓库类型：normal-普通仓库/cold-冷藏仓库/dangerous-危险品仓库',
  `location` VARCHAR(255) NOT NULL COMMENT '仓库位置',
  `capacity` DECIMAL(15,2) NOT NULL COMMENT '仓库容量',
  `used_capacity` DECIMAL(15,2) DEFAULT 0 COMMENT '已用容量',
  `belongs` VARCHAR(50) NOT NULL COMMENT '拥有者',
  `supplier_id` BIGINT COMMENT '关联供应商ID',
  `status` CHAR(1) DEFAULT '1' COMMENT '状态：0-停用/1-启用',
  `contact_person` VARCHAR(50) COMMENT '联系人',
  `contact_phone` VARCHAR(20) COMMENT '联系电话',
  `remark` VARCHAR(255) COMMENT '备注',
  `create_people` VARCHAR(50) NOT NULL COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_people` VARCHAR(50) COMMENT '修改人',
  `update_time` DATETIME COMMENT '修改时间',
  `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志：0-正常/2-删除',
  PRIMARY KEY (`warehouse_id`),
  UNIQUE KEY `uk_warehouse_code` (`warehouse_code`, `del_flag`),
  KEY `idx_supplier_id` (`supplier_id`),
  KEY `idx_warehouse_type` (`warehouse_type`),
  KEY `idx_status` (`status`),
  KEY `idx_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='仓库表';

-- 示例数据
INSERT INTO `inv_warehouse` (`warehouse_code`, `warehouse_name`, `warehouse_type`, `location`, `capacity`, `used_capacity`, `belongs`, `supplier_id`, `status`, `contact_person`, `contact_phone`, `remark`, `create_people`, `create_time`, `del_flag`)
VALUES
('WH-20251126-0001', '主仓库A', 'normal', '北京市朝阳区XX路XX号', 10000.00, 0.00, 'XX科技有限公司', NULL, '1', '张三', '13800138000', '主要存储农药类产品', 'admin', NOW(), '0'),
('WH-20251126-0002', '冷藏仓库B', 'cold', '北京市海淀区YY路YY号', 5000.00, 0.00, 'YY农业有限公司', NULL, '1', '李四', '13900139000', '存储需要冷藏的产品', 'admin', NOW(), '0');
