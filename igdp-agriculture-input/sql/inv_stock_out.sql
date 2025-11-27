-- 出库单表
DROP TABLE IF EXISTS `inv_stock_out`;
CREATE TABLE `inv_stock_out` (
  `stock_out_id` VARCHAR(30) NOT NULL COMMENT '出库单号',
  `warehouse_id` BIGINT NOT NULL COMMENT '出库仓库ID',
  `batch_no` VARCHAR(50) NOT NULL COMMENT '批次号',
  `operator` VARCHAR(50) NOT NULL COMMENT '经办人',
  `out_time` DATETIME COMMENT '出库时间',
  `customer` VARCHAR(255) NOT NULL COMMENT '客户',
  `type` CHAR(1) NOT NULL COMMENT '出库类型:0-销售出库',
  `status` CHAR(1) NOT NULL DEFAULT '0' COMMENT '状态:0-未出库/1-已出库/2-作废',
  `total_quantity` INT DEFAULT 0 COMMENT '总数量',
  `remark` VARCHAR(255) COMMENT '备注',
  `cancel_reason` VARCHAR(255) COMMENT '作废原因',
  `create_people` VARCHAR(50) NOT NULL COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_people` VARCHAR(50) COMMENT '修改人',
  `update_time` DATETIME COMMENT '修改时间',
  `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志:0-正常/2-删除',
  PRIMARY KEY (`stock_out_id`),
  KEY `idx_warehouse_id` (`warehouse_id`),
  KEY `idx_batch_no` (`batch_no`),
  KEY `idx_customer` (`customer`(100)),
  KEY `idx_status` (`status`),
  KEY `idx_type` (`type`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='出库单表';
