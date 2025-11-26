-- 入库商品明细表
DROP TABLE IF EXISTS `inv_stock_in_item`;
CREATE TABLE `inv_stock_in_item` (
  `stock_in_item_id` VARCHAR(30) NOT NULL COMMENT '入库商品明细ID',
  `stock_in_id` VARCHAR(30) NOT NULL COMMENT '入库单号',
  `input_id` BIGINT NOT NULL COMMENT '投入品ID',
  `warehouse_id` BIGINT NOT NULL COMMENT '入库仓库ID',
  `quantity` INT NOT NULL COMMENT '入库数量',
  `remarks` VARCHAR(255) COMMENT '备注',
  `create_people` VARCHAR(50) NOT NULL COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_people` VARCHAR(50) COMMENT '修改人',
  `update_time` DATETIME COMMENT '修改时间',
  `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志:0-正常/2-删除',
  PRIMARY KEY (`stock_in_item_id`),
  KEY `idx_stock_in_id` (`stock_in_id`),
  KEY `idx_input_id` (`input_id`),
  KEY `idx_warehouse_id` (`warehouse_id`),
  KEY `idx_del_flag` (`del_flag`),
  CONSTRAINT `chk_quantity_in` CHECK (`quantity` > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='入库商品明细表';
