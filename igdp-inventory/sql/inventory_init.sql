-- 1. 仓库表
DROP TABLE IF EXISTS inventory_warehouse;
-- agriculture_igdp_admin.inventory_warehouse definition
CREATE TABLE `inventory_warehouse` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '仓库ID',
`warehouse_code` varchar(50) DEFAULT NULL COMMENT '仓库编码',
`warehouse_name` varchar(100) DEFAULT NULL COMMENT '仓库名称',
`type` varchar(20) DEFAULT NULL COMMENT '仓库类型',
`store_type` varchar(100) DEFAULT '' COMMENT '存储类型（化肥、农药、种子、农产品，多选）',
`org_name` varchar(100) DEFAULT '' COMMENT '所属机构（奥罗米亚农业局、联盟、合作社、奥罗米亚种子企业）',
`admin_level` varchar(20) DEFAULT '' COMMENT '行政层级（省、市、县）',
`parent_id` bigint DEFAULT NULL COMMENT '上级仓库ID',
`location` varchar(255) DEFAULT '' COMMENT '地理位置',
`capacity` decimal(10,2) DEFAULT '0.00' COMMENT '存储容量',
`address` varchar(255) DEFAULT NULL COMMENT '仓库地址',
`status` varchar(10) DEFAULT '1' COMMENT '状态(0-停用/1-启用)',
`create_by` varchar(64) DEFAULT '' COMMENT '创建者',
`create_time` datetime DEFAULT NULL COMMENT '创建时间',
`update_by` varchar(64) DEFAULT '' COMMENT '更新者',
`update_time` datetime DEFAULT NULL COMMENT '更新时间',
`remark` varchar(500) DEFAULT '' COMMENT '备注',
`max_stock` decimal(18,2) DEFAULT NULL COMMENT '库存上限',
PRIMARY KEY (`id`),
UNIQUE KEY `uk_warehouse_code` (`warehouse_code`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='仓库表';

-- 2. 商品表
DROP TABLE IF EXISTS inventory_product;
-- agriculture_igdp_admin.inventory_product definition
CREATE TABLE `inventory_product` (
 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品ID',
 `product_code` varchar(64) NOT NULL COMMENT '商品编码',
 `product_name` varchar(100) DEFAULT '' COMMENT '商品名称',
 `category_id` bigint DEFAULT NULL COMMENT '分类ID',
 `category_name` varchar(50) DEFAULT '' COMMENT '分类名称（冗余）',
 `main_category` varchar(50) DEFAULT '' COMMENT '商品大类（化肥、种子、农产品、农药）',
 `sub_category` varchar(50) DEFAULT '' COMMENT '商品小类（如氮肥、玉米等）',
 `brand` varchar(100) DEFAULT '' COMMENT '商品品牌',
 `model` varchar(100) DEFAULT '' COMMENT '商品规格型号',
 `unit` varchar(20) DEFAULT '' COMMENT '单位',
 `price` decimal(10,2) DEFAULT '0.00' COMMENT '参考价格',
 `license_no` varchar(100) DEFAULT '' COMMENT '生产许可证号',
 `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
 `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
 `create_time` datetime DEFAULT NULL COMMENT '创建时间',
 `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
 `update_time` datetime DEFAULT NULL COMMENT '更新时间',
 `remark` varchar(500) DEFAULT '' COMMENT '备注',
 `parent_id` bigint DEFAULT NULL COMMENT '父ID',
 PRIMARY KEY (`id`),
 UNIQUE KEY `uk_product_code` (`product_code`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品表';

-- 4. 库存总表
DROP TABLE IF EXISTS inventory_stock;
-- agriculture_igdp_admin.inventory_stock definition
CREATE TABLE `inventory_stock` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`product_id` bigint DEFAULT NULL COMMENT '商品ID',
`warehouse_id` bigint NOT NULL COMMENT '仓库ID',
`available_qty` decimal(10,2) DEFAULT '0.00' COMMENT '可用库存(KG)',
`locked_qty` decimal(10,2) DEFAULT '0.00' COMMENT '锁定库存',
`quality_grade` varchar(20) DEFAULT '' COMMENT '质量等级',
`stock_status` varchar(20) DEFAULT 'AVAILABLE' COMMENT '商品状态（AVAILABLE可用、RESERVED预留、DAMAGED损坏）',
`version` bigint DEFAULT '0' COMMENT '版本号',
`create_by` varchar(64) DEFAULT '' COMMENT '创建者',
`create_time` datetime DEFAULT NULL COMMENT '创建时间',
`update_by` varchar(64) DEFAULT '' COMMENT '更新者',
`update_time` datetime DEFAULT NULL COMMENT '更新时间',
`remark` varchar(500) DEFAULT '' COMMENT '备注',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='库存总表';

-- 5. 批次库存表
DROP TABLE IF EXISTS inventory_stock_batch;
-- agriculture_igdp_admin.inventory_stock_batch definition
CREATE TABLE `inventory_stock_batch` (
 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
 `product_id` bigint DEFAULT NULL COMMENT '商品ID',
 `warehouse_id` bigint NOT NULL COMMENT '仓库ID',
 `batch_no` varchar(64) DEFAULT '' COMMENT '批次号',
 `production_date` datetime DEFAULT NULL COMMENT '生产日期',
 `expire_date` datetime DEFAULT NULL COMMENT '过期日期',
 `qty` decimal(10,2) DEFAULT '0.00' COMMENT '批次当前数量',
 `quality_grade` varchar(20) DEFAULT '' COMMENT '质量等级',
 `stock_status` varchar(20) DEFAULT 'AVAILABLE' COMMENT '商品状态',
 `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
 `create_time` datetime DEFAULT NULL COMMENT '创建时间',
 `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
 `update_time` datetime DEFAULT NULL COMMENT '更新时间',
 `remark` varchar(500) DEFAULT '' COMMENT '备注',
 `main_category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '商品大类',
 `sub_category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '商品小类',
 `stock_id` bigint DEFAULT NULL COMMENT '库存总表ID',
 `unit` varchar(100) DEFAULT NULL COMMENT '单位',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='批次库存表';

-- 6. 库存流水表
DROP TABLE IF EXISTS inventory_stock_log;
-- agriculture_igdp_admin.inventory_stock_log definition
CREATE TABLE `inventory_stock_log` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`product_id` bigint DEFAULT NULL COMMENT '商品ID（冗余）',
`warehouse_id` bigint NOT NULL COMMENT '仓库ID',
`batch_no` varchar(64) DEFAULT '' COMMENT '批次号',
`change_type` varchar(20) DEFAULT '' COMMENT '变更类型（IN入库、OUT出库、LOCK锁定、RELEASE释放、CHECK盘点）',
`change_quantity` decimal(10,2) DEFAULT '0.00' COMMENT '变更数量',
`before_quantity` decimal(10,2) DEFAULT '0.00' COMMENT '变更前数量',
`after_quantity` decimal(10,2) DEFAULT '0.00' COMMENT '变更后数量',
`biz_type` varchar(50) DEFAULT '' COMMENT '业务类型',
`biz_id` bigint DEFAULT NULL COMMENT '业务单据ID',
`biz_no` varchar(64) DEFAULT '' COMMENT '业务单号',
`create_by` varchar(64) DEFAULT '' COMMENT '创建者',
`create_time` datetime DEFAULT NULL COMMENT '创建时间',
`update_by` varchar(64) DEFAULT '' COMMENT '更新者',
`update_time` datetime DEFAULT NULL COMMENT '更新时间',
`remark` varchar(500) DEFAULT '' COMMENT '备注',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='库存流水表';

-- 7. 入库单主表
DROP TABLE IF EXISTS inventory_inbound_order;
-- agriculture_igdp_admin.inventory_inbound_order definition
CREATE TABLE `inventory_inbound_order` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`inbound_no` varchar(64) DEFAULT '' COMMENT '入库单号',
`type` varchar(20) DEFAULT '' COMMENT '入库类型（一般入库、调拨入库）',
`status` varchar(20) DEFAULT 'DRAFT' COMMENT '状态',
`biz_no` varchar(64) DEFAULT '' COMMENT '关联业务单号',
`operator` varchar(64) DEFAULT '' COMMENT '入库操作人',
`order_date` datetime DEFAULT NULL COMMENT '入库时间',
`create_by` varchar(64) DEFAULT '' COMMENT '创建者',
`create_time` datetime DEFAULT NULL COMMENT '创建时间',
`update_by` varchar(64) DEFAULT '' COMMENT '更新者',
`update_time` datetime DEFAULT NULL COMMENT '更新时间',
`remark` varchar(500) DEFAULT '' COMMENT '备注',
`audit_by` varchar(64) DEFAULT '' COMMENT '审批人',
`audit_time` datetime DEFAULT NULL COMMENT '审批时间',
`audit_comment` varchar(500) DEFAULT '' COMMENT '审批意见',
`warehouse_code` varchar(100) DEFAULT NULL COMMENT '仓库编码',
PRIMARY KEY (`id`),
UNIQUE KEY `uk_inbound_no` (`inbound_no`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='入库单主表';

-- 8. 入库单明细表
DROP TABLE IF EXISTS inventory_inbound_order_detail;
-- agriculture_igdp_admin.inventory_inbound_order_detail definition
CREATE TABLE `inventory_inbound_order_detail` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`inbound_id` bigint NOT NULL COMMENT '入库单ID',
`product_id` bigint DEFAULT NULL COMMENT '商品ID',
`main_category` varchar(50) DEFAULT '' COMMENT '商品大类',
`sub_category` varchar(50) DEFAULT '' COMMENT '商品小类',
`batch_no` varchar(50) DEFAULT NULL COMMENT '批次号',
`supplier` varchar(100) DEFAULT '' COMMENT '供应商',
`qty` decimal(18,2) DEFAULT NULL COMMENT '数量',
`unit` varchar(20) DEFAULT '' COMMENT '单位',
`expire_date` datetime DEFAULT NULL COMMENT '有效期',
`create_by` varchar(64) DEFAULT '' COMMENT '创建者',
`create_time` datetime DEFAULT NULL COMMENT '创建时间',
`update_by` varchar(64) DEFAULT '' COMMENT '更新者',
`update_time` datetime DEFAULT NULL COMMENT '更新时间',
`remark` varchar(500) DEFAULT '' COMMENT '备注',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='入库单明细表';

-- 9. 出库单主表
DROP TABLE IF EXISTS inventory_outbound_order;
-- agriculture_igdp_admin.inventory_outbound_order definition
CREATE TABLE `inventory_outbound_order` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`outbound_no` varchar(64) DEFAULT '' COMMENT '出库单号',
`type` varchar(20) DEFAULT '' COMMENT '出库类型（一般出库、调拨出库）',
`status` varchar(20) DEFAULT 'DRAFT' COMMENT '状态',
`receiver_type` varchar(20) DEFAULT '' COMMENT '接收人类型',
`receiver` varchar(64) DEFAULT '' COMMENT '接收人',
`biz_no` varchar(64) DEFAULT '' COMMENT '关联业务单号',
`operator` varchar(64) DEFAULT '' COMMENT '出库操作人',
`order_date` datetime DEFAULT NULL COMMENT '出库时间',
`create_by` varchar(64) DEFAULT '' COMMENT '创建者',
`create_time` datetime DEFAULT NULL COMMENT '创建时间',
`update_by` varchar(64) DEFAULT '' COMMENT '更新者',
`update_time` datetime DEFAULT NULL COMMENT '更新时间',
`remark` varchar(500) DEFAULT '' COMMENT '备注',
`audit_by` varchar(64) DEFAULT '' COMMENT '审批人',
`audit_time` datetime DEFAULT NULL COMMENT '审批时间',
`audit_comment` varchar(500) DEFAULT '' COMMENT '审批意见',
`warehouse_code` varchar(100) DEFAULT NULL COMMENT '仓库编码',
PRIMARY KEY (`id`),
UNIQUE KEY `uk_outbound_no` (`outbound_no`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='出库单主表';

-- 10. 出库单明细表
DROP TABLE IF EXISTS inventory_outbound_order_detail;
-- agriculture_igdp_admin.inventory_outbound_order_detail definition

CREATE TABLE `inventory_outbound_order_detail` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`outbound_id` bigint NOT NULL COMMENT '出库单ID',
`product_id` bigint DEFAULT NULL COMMENT '商品ID',
`main_category` varchar(50) DEFAULT '' COMMENT '商品大类',
`sub_category` varchar(50) DEFAULT '' COMMENT '商品小类',
`batch_no` varchar(50) DEFAULT NULL COMMENT '批次号',
`qty` decimal(18,2) DEFAULT NULL COMMENT '数量',
`unit` varchar(20) DEFAULT '' COMMENT '单位',
`create_by` varchar(64) DEFAULT '' COMMENT '创建者',
`create_time` datetime DEFAULT NULL COMMENT '创建时间',
`update_by` varchar(64) DEFAULT '' COMMENT '更新者',
`update_time` datetime DEFAULT NULL COMMENT '更新时间',
`remark` varchar(500) DEFAULT '' COMMENT '备注',
`supplier` varchar(100) DEFAULT NULL COMMENT '供应商',
`expire_date` datetime DEFAULT NULL COMMENT '有效期',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='出库单明细表';

-- 11. 盘点单主表
DROP TABLE IF EXISTS inventory_check_order;
-- agriculture_igdp_admin.inventory_check_order definition
CREATE TABLE `inventory_check_order` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`check_no` varchar(64) DEFAULT '' COMMENT '盘点单号',
`check_date` datetime DEFAULT NULL COMMENT '盘点日期',
`check_by` varchar(64) DEFAULT '' COMMENT '盘点人',
`status` varchar(20) DEFAULT 'DRAFT' COMMENT '状态',
`audit_by` varchar(64) DEFAULT '' COMMENT '审核人',
`audit_time` datetime DEFAULT NULL COMMENT '审核日期',
`audit_comment` varchar(500) DEFAULT '' COMMENT '审核意见',
`create_by` varchar(64) DEFAULT '' COMMENT '创建者',
`create_time` datetime DEFAULT NULL COMMENT '创建时间',
`update_by` varchar(64) DEFAULT '' COMMENT '更新者',
`update_time` datetime DEFAULT NULL COMMENT '更新时间',
`remark` varchar(500) DEFAULT '' COMMENT '盘点说明',
`warehouse_code` varchar(100) DEFAULT NULL COMMENT '仓库编码',
PRIMARY KEY (`id`),
UNIQUE KEY `uk_check_no` (`check_no`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='盘点单主表';

-- 12. 盘点单明细表
DROP TABLE IF EXISTS inventory_check_order_detail;
-- agriculture_igdp_admin.inventory_check_order_detail definition
CREATE TABLE `inventory_check_order_detail` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`check_id` bigint NOT NULL COMMENT '盘点单ID',
`product_id` bigint DEFAULT NULL COMMENT '商品ID',
`main_category` varchar(50) DEFAULT '' COMMENT '商品大类',
`sub_category` varchar(50) DEFAULT '' COMMENT '商品小类',
`batch_no` varchar(64) DEFAULT '' COMMENT '商品生产批次',
`book_qty` decimal(10,2) DEFAULT '0.00' COMMENT '库存数据（账面）',
`real_qty` decimal(10,2) DEFAULT '0.00' COMMENT '实盘数据',
`diff_type` varchar(20) DEFAULT '' COMMENT '差异类型（PROFIT盘盈、LOSS盘亏）',
`diff_qty` decimal(10,2) DEFAULT '0.00' COMMENT '差异数据',
`unit` varchar(20) DEFAULT '' COMMENT '单位',
`remark` varchar(500) DEFAULT '' COMMENT '盘点说明',
`create_by` varchar(64) DEFAULT '' COMMENT '创建者',
`create_time` datetime DEFAULT NULL COMMENT '创建时间',
`update_by` varchar(64) DEFAULT '' COMMENT '更新者',
`update_time` datetime DEFAULT NULL COMMENT '更新时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='盘点单明细表';

-- 13. 库存预警规则表
DROP TABLE IF EXISTS inventory_warning_rule;
-- agriculture_igdp_admin.inventory_warning_rule definition
CREATE TABLE `inventory_warning_rule` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`warehouse_id` bigint DEFAULT NULL COMMENT '仓库ID（空代表所有仓库）',
`product_id` bigint DEFAULT NULL COMMENT '商品ID',
`min_stock` decimal(10,2) DEFAULT '0.00' COMMENT '最低库存预警值',
`max_stock` decimal(10,2) DEFAULT '0.00' COMMENT '最高库存预警值',
`create_by` varchar(64) DEFAULT '' COMMENT '创建者',
`create_time` datetime DEFAULT NULL COMMENT '创建时间',
`update_by` varchar(64) DEFAULT '' COMMENT '更新者',
`update_time` datetime DEFAULT NULL COMMENT '更新时间',
`remark` varchar(500) DEFAULT '' COMMENT '备注',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='库存预警规则表';

-- 14. 溯源信息表
DROP TABLE IF EXISTS inventory_trace_info;
-- agriculture_igdp_admin.inventory_trace_info definition

CREATE TABLE `inventory_trace_info` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`trace_code` varchar(64) NOT NULL COMMENT '追溯码',
`product_id` bigint NOT NULL COMMENT '商品ID',
`product_name` varchar(100) DEFAULT '' COMMENT '商品名称',
`batch_no` varchar(64) DEFAULT '' COMMENT '商品生产批次',
`supplier_info` varchar(255) DEFAULT '' COMMENT '供应商信息',
`flow_type` varchar(20) DEFAULT '' COMMENT '流转类型（IN入库、OUT出库、TRANSFER调拨）',
`flow_time` datetime DEFAULT NULL COMMENT '流转时间',
`biz_id` bigint DEFAULT NULL COMMENT '操作记录ID（关联单据ID）',
`inbound_party_id` bigint DEFAULT NULL COMMENT '入库方ID',
`inbound_warehouse_id` bigint DEFAULT NULL COMMENT '入库仓库ID',
`outbound_party_id` bigint DEFAULT NULL COMMENT '出库方ID',
`outbound_warehouse_id` bigint DEFAULT NULL COMMENT '出库仓库ID',
`update_time` datetime DEFAULT NULL COMMENT '追溯信息更新时间',
`create_by` varchar(64) DEFAULT '' COMMENT '创建者',
`create_time` datetime DEFAULT NULL COMMENT '创建时间',
`remark` varchar(500) DEFAULT '' COMMENT '备注',
PRIMARY KEY (`id`),
UNIQUE KEY `uk_trace_code` (`trace_code`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='溯源信息表';





-- 15. 仓库所有权表
DROP TABLE IF EXISTS inventory_warehouse_owner;
CREATE TABLE inventory_warehouse_owner (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  warehouse_id BIGINT NOT NULL COMMENT '仓库ID',
  owner_user_id VARCHAR(64) NOT NULL COMMENT '所有人用户ID',
  owner_user_name VARCHAR(100) DEFAULT '' COMMENT '所有人姓名',
  owner_org_id VARCHAR(64) DEFAULT NULL COMMENT '所有人所属机构ID',
  owner_org_name VARCHAR(100) DEFAULT '' COMMENT '所有人所属机构',
  owner_role VARCHAR(20) DEFAULT 'PRIMARY' COMMENT '所有权角色（PRIMARY主所有者/CO_OWNER协同所有者）',
  start_time DATETIME COMMENT '生效时间',
  end_time DATETIME COMMENT '失效时间',
  status CHAR(1) DEFAULT '1' COMMENT '状态（1有效 0失效）',
  is_primary CHAR(1) DEFAULT '0' COMMENT '是否主所有者（1是 0否）',
  create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME COMMENT '创建时间',
  update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME COMMENT '更新时间',
  remark VARCHAR(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (id),
  KEY idx_owner_warehouse (warehouse_id),
  KEY idx_owner_user (owner_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='仓库所有权表';

-- 16. 仓库使用权表（部门权限）
DROP TABLE IF EXISTS inventory_warehouse_permission;
CREATE TABLE inventory_warehouse_permission (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  warehouse_id BIGINT NOT NULL COMMENT '仓库ID',
  dept_id VARCHAR(64) NOT NULL COMMENT '部门ID',
  dept_name VARCHAR(100) DEFAULT '' COMMENT '部门名称',
  allow_inbound CHAR(1) DEFAULT '0' COMMENT '是否允许入库（1是 0否）',
  allow_outbound CHAR(1) DEFAULT '0' COMMENT '是否允许出库（1是 0否）',
  allow_transfer CHAR(1) DEFAULT '0' COMMENT '是否允许调拨（1是 0否）',
  allow_adjust CHAR(1) DEFAULT '0' COMMENT '是否允许库存调整（1是 0否）',
  allow_view CHAR(1) DEFAULT '1' COMMENT '是否允许查看（1是 0否）',
  start_time DATETIME COMMENT '生效时间',
  end_time DATETIME COMMENT '失效时间',
  status CHAR(1) DEFAULT '1' COMMENT '状态（1启用 0停用）',
  assigner_id BIGINT DEFAULT NULL COMMENT '分配人ID',
  assigner_name VARCHAR(100) DEFAULT '' COMMENT '分配人姓名',
  assign_time DATETIME COMMENT '分配时间',
  assign_reason VARCHAR(500) DEFAULT '' COMMENT '分配说明',
  create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME COMMENT '创建时间',
  update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME COMMENT '更新时间',
  remark VARCHAR(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (id),
  KEY idx_perm_warehouse (warehouse_id),
  KEY idx_perm_dept (dept_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='仓库使用权表';
