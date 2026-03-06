-- 1. 仓库表
DROP TABLE IF EXISTS inventory_warehouse;
CREATE TABLE inventory_warehouse (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '仓库ID',
  warehouse_code VARCHAR(64) NOT NULL COMMENT '仓库编码',
  warehouse_name VARCHAR(100) DEFAULT '' COMMENT '仓库名称',
  type VARCHAR(20) DEFAULT '' COMMENT '仓库类型（中央仓库、联盟仓库、合作社仓库、企业仓库）',
  store_type VARCHAR(100) DEFAULT '' COMMENT '存储类型（化肥、农药、种子、农产品，多选）',
  org_name VARCHAR(100) DEFAULT '' COMMENT '所属机构（奥罗米亚农业局、联盟、合作社、奥罗米亚种子企业）',
  admin_level VARCHAR(20) DEFAULT '' COMMENT '行政层级（省、市、县）',
  parent_id BIGINT DEFAULT NULL COMMENT '上级仓库ID',
  location VARCHAR(255) DEFAULT '' COMMENT '地理位置',
  capacity DECIMAL(10,2) DEFAULT 0.00 COMMENT '存储容量',
  address VARCHAR(255) DEFAULT '' COMMENT '地址',
  status CHAR(1) DEFAULT '0' COMMENT '运营状态（0活跃 1停用 2维护中）',
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
  category_name VARCHAR(50) DEFAULT '' COMMENT '分类名称（冗余）',
  main_category VARCHAR(50) DEFAULT '' COMMENT '商品大类（化肥、种子、农产品、农药）',
  sub_category VARCHAR(50) DEFAULT '' COMMENT '商品小类（如氮肥、玉米等）',
  brand VARCHAR(100) DEFAULT '' COMMENT '商品品牌',
  model VARCHAR(100) DEFAULT '' COMMENT '商品规格型号',
  unit VARCHAR(20) DEFAULT '' COMMENT '单位',
  price DECIMAL(10,2) DEFAULT 0.00 COMMENT '参考价格',
  license_no VARCHAR(100) DEFAULT '' COMMENT '生产许可证号',
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
  product_id BIGINT DEFAULT NULL COMMENT '商品ID（冗余）',
  warehouse_id BIGINT NOT NULL COMMENT '仓库ID',
  main_category VARCHAR(50) DEFAULT '' COMMENT '商品大类（冗余）',
  sub_category VARCHAR(50) DEFAULT '' COMMENT '商品小类（冗余）',
  available_qty DECIMAL(10,2) DEFAULT 0.00 COMMENT '可用库存',
  locked_qty DECIMAL(10,2) DEFAULT 0.00 COMMENT '锁定库存',
  quality_grade VARCHAR(20) DEFAULT '' COMMENT '质量等级',
  stock_status VARCHAR(20) DEFAULT 'AVAILABLE' COMMENT '商品状态（AVAILABLE可用、RESERVED预留、DAMAGED损坏）',
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
  product_id BIGINT DEFAULT NULL COMMENT '商品ID（冗余）',
  warehouse_id BIGINT NOT NULL COMMENT '仓库ID',
  batch_no VARCHAR(64) DEFAULT '' COMMENT '批次号',
  production_date DATETIME COMMENT '生产日期',
  expire_date DATETIME COMMENT '过期日期',
  qty DECIMAL(10,2) DEFAULT 0.00 COMMENT '批次当前数量',
  quality_grade VARCHAR(20) DEFAULT '' COMMENT '质量等级',
  stock_status VARCHAR(20) DEFAULT 'AVAILABLE' COMMENT '商品状态',
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
  product_id BIGINT DEFAULT NULL COMMENT '商品ID（冗余）',
  warehouse_id BIGINT NOT NULL COMMENT '仓库ID',
  batch_no VARCHAR(64) DEFAULT '' COMMENT '批次号',
  change_type VARCHAR(20) DEFAULT '' COMMENT '变更类型（IN入库、OUT出库、LOCK锁定、RELEASE释放、CHECK盘点）',
  change_quantity DECIMAL(10,2) DEFAULT 0.00 COMMENT '变更数量',
  before_quantity DECIMAL(10,2) DEFAULT 0.00 COMMENT '变更前数量',
  after_quantity DECIMAL(10,2) DEFAULT 0.00 COMMENT '变更后数量',
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
DROP TABLE IF EXISTS inventory_inbound_order;
CREATE TABLE inventory_inbound_order (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  inbound_no VARCHAR(64) DEFAULT '' COMMENT '入库单号',
  warehouse_id BIGINT NOT NULL COMMENT '仓库ID',
  type VARCHAR(20) DEFAULT '' COMMENT '入库类型（一般入库、调拨入库）',
  status VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态',
  biz_no VARCHAR(64) DEFAULT '' COMMENT '关联业务单号',
  operator VARCHAR(64) DEFAULT '' COMMENT '入库操作人',
  order_date DATETIME COMMENT '入库时间',
  create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME COMMENT '创建时间',
  update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME COMMENT '更新时间',
  remark VARCHAR(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_inbound_no (inbound_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='入库单主表';

-- 8. 入库单明细表
DROP TABLE IF EXISTS inventory_inbound_order_detail;
CREATE TABLE inventory_inbound_order_detail (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  inbound_id BIGINT NOT NULL COMMENT '入库单ID',
  product_id BIGINT DEFAULT NULL COMMENT '商品ID',
  sku_id BIGINT NOT NULL COMMENT 'SKU ID',
  main_category VARCHAR(50) DEFAULT '' COMMENT '商品大类',
  sub_category VARCHAR(50) DEFAULT '' COMMENT '商品小类',
  batch_no VARCHAR(64) DEFAULT '' COMMENT '商品生产批次',
  supplier VARCHAR(100) DEFAULT '' COMMENT '供应商',
  plan_qty DECIMAL(10,2) DEFAULT 0.00 COMMENT '计划数量',
  real_qty DECIMAL(10,2) DEFAULT 0.00 COMMENT '实际数量',
  unit VARCHAR(20) DEFAULT '' COMMENT '单位',
  expire_date DATETIME COMMENT '有效期',
  create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME COMMENT '创建时间',
  update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME COMMENT '更新时间',
  remark VARCHAR(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='入库单明细表';

-- 9. 出库单主表
DROP TABLE IF EXISTS inventory_outbound_order;
CREATE TABLE inventory_outbound_order (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  outbound_no VARCHAR(64) DEFAULT '' COMMENT '出库单号',
  warehouse_id BIGINT NOT NULL COMMENT '仓库ID',
  type VARCHAR(20) DEFAULT '' COMMENT '出库类型（一般出库、调拨出库）',
  status VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态',
  receiver_type VARCHAR(20) DEFAULT '' COMMENT '接收人类型',
  receiver VARCHAR(64) DEFAULT '' COMMENT '接收人',
  biz_no VARCHAR(64) DEFAULT '' COMMENT '关联业务单号',
  operator VARCHAR(64) DEFAULT '' COMMENT '出库操作人',
  order_date DATETIME COMMENT '出库时间',
  create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME COMMENT '创建时间',
  update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME COMMENT '更新时间',
  remark VARCHAR(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_outbound_no (outbound_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='出库单主表';

-- 10. 出库单明细表
DROP TABLE IF EXISTS inventory_outbound_order_detail;
CREATE TABLE inventory_outbound_order_detail (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  outbound_id BIGINT NOT NULL COMMENT '出库单ID',
  product_id BIGINT DEFAULT NULL COMMENT '商品ID',
  sku_id BIGINT NOT NULL COMMENT 'SKU ID',
  main_category VARCHAR(50) DEFAULT '' COMMENT '商品大类',
  sub_category VARCHAR(50) DEFAULT '' COMMENT '商品小类',
  batch_no VARCHAR(64) DEFAULT '' COMMENT '商品生产批次',
  apply_qty DECIMAL(10,2) DEFAULT 0.00 COMMENT '申请数量',
  real_qty DECIMAL(10,2) DEFAULT 0.00 COMMENT '实际数量',
  unit VARCHAR(20) DEFAULT '' COMMENT '单位',
  create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME COMMENT '创建时间',
  update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME COMMENT '更新时间',
  remark VARCHAR(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='出库单明细表';

-- 11. 盘点单主表
DROP TABLE IF EXISTS inventory_check_order;
CREATE TABLE inventory_check_order (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  check_no VARCHAR(64) DEFAULT '' COMMENT '盘点单号',
  warehouse_id BIGINT NOT NULL COMMENT '盘点仓库ID',
  check_date DATETIME COMMENT '盘点日期',
  check_by VARCHAR(64) DEFAULT '' COMMENT '盘点人',
  status VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态',
  audit_by VARCHAR(64) DEFAULT '' COMMENT '审核人',
  audit_time DATETIME COMMENT '审核日期',
  audit_comment VARCHAR(500) DEFAULT '' COMMENT '审核意见',
  create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME COMMENT '创建时间',
  update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME COMMENT '更新时间',
  remark VARCHAR(500) DEFAULT '' COMMENT '盘点说明',
  PRIMARY KEY (id),
  UNIQUE KEY uk_check_no (check_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='盘点单主表';

-- 12. 盘点单明细表
DROP TABLE IF EXISTS inventory_check_order_detail;
CREATE TABLE inventory_check_order_detail (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  check_id BIGINT NOT NULL COMMENT '盘点单ID',
  product_id BIGINT DEFAULT NULL COMMENT '商品ID',
  sku_id BIGINT NOT NULL COMMENT 'SKU ID',
  main_category VARCHAR(50) DEFAULT '' COMMENT '商品大类',
  sub_category VARCHAR(50) DEFAULT '' COMMENT '商品小类',
  batch_no VARCHAR(64) DEFAULT '' COMMENT '商品生产批次',
  book_qty DECIMAL(10,2) DEFAULT 0.00 COMMENT '库存数据（账面）',
  real_qty DECIMAL(10,2) DEFAULT 0.00 COMMENT '实盘数据',
  diff_type VARCHAR(20) DEFAULT '' COMMENT '差异类型（PROFIT盘盈、LOSS盘亏）',
  diff_qty DECIMAL(10,2) DEFAULT 0.00 COMMENT '差异数据',
  unit VARCHAR(20) DEFAULT '' COMMENT '单位',
  remark VARCHAR(500) DEFAULT '' COMMENT '盘点说明',
  create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME COMMENT '创建时间',
  update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='盘点单明细表';

-- 13. 库存预警规则表
DROP TABLE IF EXISTS inventory_warning_rule;
CREATE TABLE inventory_warning_rule (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  warehouse_id BIGINT DEFAULT NULL COMMENT '仓库ID（空代表所有仓库）',
  product_id BIGINT DEFAULT NULL COMMENT '商品ID',
  sku_id BIGINT DEFAULT NULL COMMENT 'SKU ID',
  min_stock DECIMAL(10,2) DEFAULT 0.00 COMMENT '最低库存预警值',
  max_stock DECIMAL(10,2) DEFAULT 0.00 COMMENT '最高库存预警值',
  create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME COMMENT '创建时间',
  update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME COMMENT '更新时间',
  remark VARCHAR(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存预警规则表';

-- 14. 溯源信息表
DROP TABLE IF EXISTS inventory_trace_info;
CREATE TABLE inventory_trace_info (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  trace_code VARCHAR(64) NOT NULL COMMENT '追溯码',
  product_id BIGINT NOT NULL COMMENT '商品ID',
  product_name VARCHAR(100) DEFAULT '' COMMENT '商品名称',
  batch_no VARCHAR(64) DEFAULT '' COMMENT '商品生产批次',
  supplier_info VARCHAR(255) DEFAULT '' COMMENT '供应商信息',
  flow_type VARCHAR(20) DEFAULT '' COMMENT '流转类型（IN入库、OUT出库、TRANSFER调拨）',
  flow_time DATETIME COMMENT '流转时间',
  biz_id BIGINT DEFAULT NULL COMMENT '操作记录ID（关联单据ID）',
  inbound_party_id BIGINT DEFAULT NULL COMMENT '入库方ID',
  inbound_warehouse_id BIGINT DEFAULT NULL COMMENT '入库仓库ID',
  outbound_party_id BIGINT DEFAULT NULL COMMENT '出库方ID',
  outbound_warehouse_id BIGINT DEFAULT NULL COMMENT '出库仓库ID',
  update_time DATETIME COMMENT '追溯信息更新时间',
  create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME COMMENT '创建时间',
  remark VARCHAR(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_trace_code (trace_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='溯源信息表';
