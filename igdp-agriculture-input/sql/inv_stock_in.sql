-- 入库单表
DROP TABLE IF EXISTS `inv_stock_in`;
CREATE TABLE `inv_stock_in` (
  `stock_in_id` VARCHAR(30) NOT NULL COMMENT '入库单号',
  `warehouse_id` BIGINT NOT NULL COMMENT '入库仓库ID',
  `batch_no` VARCHAR(50) NOT NULL COMMENT '批次号',
  `operator` VARCHAR(50) NOT NULL COMMENT '经办人',
  `supplier_id` BIGINT NOT NULL COMMENT '供应商ID',
  `type` CHAR(1) NOT NULL COMMENT '入库类型:0-采购入库/1-退货入库',
  `status` CHAR(1) NOT NULL DEFAULT '0' COMMENT '状态:0-未入库/1-已入库/2-作废',
  `total_quantity` INT DEFAULT 0 COMMENT '总数量',
  `qr_code` VARCHAR(255) COMMENT '二维码',
  `remarks` VARCHAR(255) COMMENT '备注',
  `cancel_reason` VARCHAR(255) COMMENT '作废原因',
  `confirm_time` DATETIME COMMENT '确认入库时间',
  `create_people` VARCHAR(50) NOT NULL COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_people` VARCHAR(50) COMMENT '修改人',
  `update_time` DATETIME COMMENT '修改时间',
  `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志:0-正常/2-删除',
  PRIMARY KEY (`stock_in_id`),
  KEY `idx_warehouse_id` (`warehouse_id`),
  KEY `idx_supplier_id` (`supplier_id`),
  KEY `idx_batch_no` (`batch_no`),
  KEY `idx_status` (`status`),
  KEY `idx_type` (`type`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='入库单表';
