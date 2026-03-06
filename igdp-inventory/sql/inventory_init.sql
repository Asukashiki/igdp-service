-- 1. 仓库表
DROP TABLE IF EXISTS inventory_warehouse;
CREATE TABLE inventory_warehouse (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '仓库ID',
  warehouse_code VARCHAR(64) NOT NULL COMMENT '仓库编码',
  warehouse_name VARCHAR(100) DEFAULT '' COMMENT '仓库名称',
  type VARCHAR(20) DEFAULT '' COMMENT '仓库类型',
  address VARCHAR(255) DEFAULT '' COMMENT '地址',
  status CHAR(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME COMMENT '创建时间',
  update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME COMMENT '更新时间',
  remark VARCHAR(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_warehouse_code (warehouse_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='仓库表';

-- 2. 商品表
DROP TABLE IF EXISTS inventory_product;
CREATE TABLE inventory_product (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  product_code VARCHAR(64) NOT NULL COMMENT '商品编码',
  product_name VARCHAR(100) DEFAULT '' COMMENT '商品名称',
  category_id BIGINT DEFAULT NULL COMMENT '分类ID',
  category_name VARCHAR(50) DEFAULT '' COMMENT '分类名称',
  status CHAR(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME COMMENT '创建时间',
  update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME COMMENT '更新时间',
  remark VARCHAR(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 3. SKU表
DROP TABLE IF EXISTS inventory_sku;
CREATE TABLE inventory_sku (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'SKU ID',
  sku_code VARCHAR(64) NOT NULL COMMENT 'SKU编码',
  sku_name VARCHAR(100) DEFAULT '' COMMENT 'SKU名称',
  product_id BIGINT DEFAULT NULL COMMENT '商品ID',
  unit VARCHAR(20) DEFAULT '' COMMENT '单位',
  spec VARCHAR(100) DEFAULT '' COMMENT '规格',
  status CHAR(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME COMMENT '创建时间',
  update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME COMMENT '更新时间',
  remark VARCHAR(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_sku_code (sku_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='SKU表';

-- 4. 库存总表
DROP TABLE IF EXISTS inventory_stock;
CREATE TABLE inventory_stock (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  sku_id BIGINT NOT NULL COMMENT 'SKU ID',
  warehouse_id BIGINT NOT NULL COMMENT '仓库ID',
  available_qty DECIMAL(10,2) DEFAULT 0.00 COMMENT '可用库存',
  locked_qty DECIMAL(10,2) DEFAULT 0.00 COMMENT '锁定库存',
  version BIGINT DEFAULT 0 COMMENT '版本号',
  create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME COMMENT '创建时间',
  update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME COMMENT '更新时间',
  remark VARCHAR(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_sku_warehouse (sku_id, warehouse_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存总表';

-- 5. 批次库存表
DROP TABLE IF EXISTS inventory_stock_batch;
CREATE TABLE inventory_stock_batch (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  sku_id BIGINT NOT NULL COMMENT 'SKU ID',
  warehouse_id BIGINT NOT NULL COMMENT '仓库ID',
  batch_no VARCHAR(64) DEFAULT '' COMMENT '批次号',
  production_date DATETIME COMMENT '生产日期',
  expire_date DATETIME COMMENT '过期日期',
  qty DECIMAL(10,2) DEFAULT 0.00 COMMENT '批次当前数量',
  create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME COMMENT '创建时间',
  update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME COMMENT '更新时间',
  remark VARCHAR(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='批次库存表';

-- 6. 库存流水表
DROP TABLE IF EXISTS inventory_stock_log;
CREATE TABLE inventory_stock_log (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  sku_id BIGINT NOT NULL COMMENT 'SKU ID',
  warehouse_id BIGINT NOT NULL COMMENT '仓库ID',
  batch_no VARCHAR(64) DEFAULT '' COMMENT '批次号',
  change_type VARCHAR(20) DEFAULT '' COMMENT '变更类型',
  change_qty DECIMAL(10,2) DEFAULT 0.00 COMMENT '变更数量',
  before_qty DECIMAL(10,2) DEFAULT 0.00 COMMENT '变更前数量',
  after_qty DECIMAL(10,2) DEFAULT 0.00 COMMENT '变更后数量',
  biz_type VARCHAR(50) DEFAULT '' COMMENT '业务类型',
  biz_id BIGINT DEFAULT NULL COMMENT '业务单据ID',
  biz_no VARCHAR(64) DEFAULT '' COMMENT '业务单号',
  create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME COMMENT '创建时间',
  update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME COMMENT '更新时间',
  remark VARCHAR(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存流水表';

-- 7. 入库单主表
DROP TABLE IF EXISTS inventory_inbound;
CREATE TABLE inventory_inbound (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  warehouse_id BIGINT NOT NULL COMMENT '仓库ID',
  type VARCHAR(20) DEFAULT '' COMMENT '入库类型',
  status VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态',
  order_date DATETIME COMMENT '单据日期',
  create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME COMMENT '创建时间',
  update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME COMMENT '更新时间',
  remark VARCHAR(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='入库单主表';

-- 8. 入库单明细表
DROP TABLE IF EXISTS inventory_inbound_detail;
CREATE TABLE inventory_inbound_detail (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  inbound_id BIGINT NOT NULL COMMENT '入库单ID',
  sku_id BIGINT NOT NULL COMMENT 'SKU ID',
  batch_no VARCHAR(64) DEFAULT '' COMMENT '批次号',
  plan_qty DECIMAL(10,2) DEFAULT 0.00 COMMENT '计划数量',
  real_qty DECIMAL(10,2) DEFAULT 0.00 COMMENT '实际数量',
  create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME COMMENT '创建时间',
  update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME COMMENT '更新时间',
  remark VARCHAR(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='入库单明细表';

-- 9. 出库单主表
DROP TABLE IF EXISTS inventory_outbound;
CREATE TABLE inventory_outbound (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  warehouse_id BIGINT NOT NULL COMMENT '仓库ID',
  type VARCHAR(20) DEFAULT '' COMMENT '出库类型',
  status VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态',
  create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME COMMENT '创建时间',
  update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME COMMENT '更新时间',
  remark VARCHAR(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='出库单主表';

-- 10. 出库单明细表
DROP TABLE IF EXISTS inventory_outbound_detail;
CREATE TABLE inventory_outbound_detail (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  outbound_id BIGINT NOT NULL COMMENT '出库单ID',
  sku_id BIGINT NOT NULL COMMENT 'SKU ID',
  batch_no VARCHAR(64) DEFAULT '' COMMENT '批次号',
  apply_qty DECIMAL(10,2) DEFAULT 0.00 COMMENT '申请数量',
  real_qty DECIMAL(10,2) DEFAULT 0.00 COMMENT '实际数量',
  create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME COMMENT '创建时间',
  update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME COMMENT '更新时间',
  remark VARCHAR(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='出库单明细表';
