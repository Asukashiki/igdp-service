-- 库存表
DROP TABLE IF EXISTS `inv_inventory`;
CREATE TABLE `inv_inventory` (
  `inventory_id` VARCHAR(30) NOT NULL COMMENT '库存记录ID',
  `input_id` BIGINT NOT NULL COMMENT '投入品ID',
  `batch_no` VARCHAR(50) NOT NULL COMMENT '批次号',
  `warehouse_id` BIGINT NOT NULL COMMENT '仓库ID',
  `current_quantity` INT NOT NULL DEFAULT 0 COMMENT '当前库存数量',
  `in_date` DATE NOT NULL COMMENT '入库日期',
  `expired_date` DATE NOT NULL COMMENT '过期日期',
  `stock_status` CHAR(1) NOT NULL DEFAULT '0' COMMENT '库存状态:0-正常/1-临期/2-过期',
  `create_people` VARCHAR(50) NOT NULL COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_people` VARCHAR(50) COMMENT '修改人',
  `update_time` DATETIME COMMENT '修改时间',
  `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志:0-正常/2-删除',
  PRIMARY KEY (`inventory_id`),
  UNIQUE KEY `uk_input_batch_warehouse` (`input_id`, `batch_no`, `warehouse_id`, `del_flag`),
  KEY `idx_input_id` (`input_id`),
  KEY `idx_batch_no` (`batch_no`),
  KEY `idx_warehouse_id` (`warehouse_id`),
  KEY `idx_stock_status` (`stock_status`),
  KEY `idx_expired_date` (`expired_date`),
  KEY `idx_del_flag` (`del_flag`),
  CONSTRAINT `chk_current_quantity` CHECK (`current_quantity` >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存表';
