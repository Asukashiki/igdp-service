-- ========================================
-- 投入品需求采集管理模块 - DA录入农民需求
-- ========================================

-- 需求采集批次表
CREATE TABLE IF NOT EXISTS demand_collection_batch (
    id VARCHAR(36) NOT NULL COMMENT '主键UUID',
    batch_no VARCHAR(50) NOT NULL COMMENT '批次编号',
    batch_name VARCHAR(200) NOT NULL COMMENT '批次名称',
    year INT NOT NULL COMMENT '年度',
    start_date DATE NOT NULL COMMENT '采集开始日期',
    end_date DATE NOT NULL COMMENT '采集结束日期',
    status VARCHAR(20) NOT NULL DEFAULT 'collecting' COMMENT '状态(collecting/reviewing/completed/locked)',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by VARCHAR(36) DEFAULT NULL COMMENT '更新人ID',
    updated_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0:否 1:是)',
    version INT NOT NULL DEFAULT 1 COMMENT '版本号(乐观锁)',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE INDEX uk_batch_no (batch_no),
    INDEX idx_year (year),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='需求采集批次表';

-- 农民需求明细表
CREATE TABLE IF NOT EXISTS demand_farmer_detail (
    id VARCHAR(36) NOT NULL COMMENT '主键UUID',
    batch_id VARCHAR(36) NOT NULL COMMENT '批次ID(关联demand_collection_batch)',
    farmer_id VARCHAR(36) NOT NULL COMMENT '农民ID',
    farmer_name VARCHAR(100) NOT NULL COMMENT '农民姓名',
    farmer_id_number VARCHAR(50) NOT NULL COMMENT '农民身份证号',
    region VARCHAR(100) DEFAULT NULL COMMENT '大区',
    zone VARCHAR(100) DEFAULT NULL COMMENT 'Zone',
    woreda VARCHAR(100) NOT NULL COMMENT 'Woreda',
    kebele VARCHAR(100) NOT NULL COMMENT 'Kebele',
    village VARCHAR(100) NOT NULL COMMENT '村庄',
    land_area DECIMAL(10,2) DEFAULT NULL COMMENT '地块总面积(公顷)',
    max_seed_quantity DECIMAL(15,2) DEFAULT NULL COMMENT '估算最大种子量(kg)',
    max_fertilizer_quantity DECIMAL(15,2) DEFAULT NULL COMMENT '估算最大肥料量(kg)',
    status VARCHAR(20) NOT NULL DEFAULT 'draft' COMMENT '状态(draft/submitted/approved/rejected)',
    current_audit_level VARCHAR(20) DEFAULT NULL COMMENT '当前审核层级(village/town/district/state/ministry)',
    da_user_id VARCHAR(36) NOT NULL COMMENT '录入DA用户ID',
    da_user_name VARCHAR(100) NOT NULL COMMENT '录入DA用户姓名',
    submit_time DATETIME DEFAULT NULL COMMENT '提交时间',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by VARCHAR(36) DEFAULT NULL COMMENT '更新人ID',
    updated_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0:否 1:是)',
    version INT NOT NULL DEFAULT 1 COMMENT '版本号(乐观锁)',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    INDEX idx_batch_id (batch_id),
    INDEX idx_farmer_id (farmer_id),
    INDEX idx_kebele (kebele),
    INDEX idx_status (status),
    INDEX idx_current_audit_level (current_audit_level),
    CONSTRAINT fk_demand_batch FOREIGN KEY (batch_id) REFERENCES demand_collection_batch(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='农民需求明细表';

-- 农民需求投入品明细表
CREATE TABLE IF NOT EXISTS demand_farmer_input_item (
    id VARCHAR(36) NOT NULL COMMENT '主键UUID',
    demand_id VARCHAR(36) NOT NULL COMMENT '需求ID(关联demand_farmer_detail)',
    input_category VARCHAR(50) NOT NULL COMMENT '投入品大类(seed/fertilizer/pesticide)',
    input_type VARCHAR(100) NOT NULL COMMENT '农资类型',
    variety VARCHAR(200) NOT NULL COMMENT '品种',
    specification VARCHAR(100) DEFAULT NULL COMMENT '规格',
    unit VARCHAR(20) NOT NULL COMMENT '单位',
    quantity DECIMAL(15,2) NOT NULL COMMENT '需求数量',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by VARCHAR(36) DEFAULT NULL COMMENT '更新人ID',
    updated_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0:否 1:是)',
    PRIMARY KEY (id),
    INDEX idx_demand_id (demand_id),
    INDEX idx_input_category (input_category),
    CONSTRAINT fk_item_demand FOREIGN KEY (demand_id) REFERENCES demand_farmer_detail(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='农民需求投入品明细表';

-- 需求汇总表
CREATE TABLE IF NOT EXISTS demand_summary (
    id VARCHAR(36) NOT NULL COMMENT '主键UUID',
    batch_id VARCHAR(36) NOT NULL COMMENT '批次ID(关联demand_collection_batch)',
    admin_level VARCHAR(20) NOT NULL COMMENT '行政层级(kebele/woreda/zone/region/state)',
    admin_code VARCHAR(50) NOT NULL COMMENT '行政区划代码',
    admin_name VARCHAR(200) NOT NULL COMMENT '行政区划名称',
    parent_admin_code VARCHAR(50) DEFAULT NULL COMMENT '上级行政区划代码',
    input_category VARCHAR(50) NOT NULL COMMENT '投入品大类(seed/fertilizer/pesticide)',
    input_type VARCHAR(100) NOT NULL COMMENT '农资类型',
    variety VARCHAR(200) DEFAULT NULL COMMENT '品种',
    total_quantity DECIMAL(18,2) NOT NULL COMMENT '汇总数量',
    farmer_count INT NOT NULL COMMENT '农民数量',
    status VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '状态(pending/submitted/approved/rejected)',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by VARCHAR(36) DEFAULT NULL COMMENT '更新人ID',
    updated_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0:否 1:是)',
    version INT NOT NULL DEFAULT 1 COMMENT '版本号(乐观锁)',
    PRIMARY KEY (id),
    INDEX idx_batch_id (batch_id),
    INDEX idx_admin_level (admin_level),
    INDEX idx_admin_code (admin_code),
    INDEX idx_input_category (input_category),
    INDEX idx_status (status),
    UNIQUE INDEX uk_batch_admin_input (batch_id, admin_level, admin_code, input_category, input_type, variety),
    CONSTRAINT fk_summary_batch FOREIGN KEY (batch_id) REFERENCES demand_collection_batch(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='需求汇总表';

-- 需求审核记录表
CREATE TABLE IF NOT EXISTS demand_audit_record (
    id VARCHAR(36) NOT NULL COMMENT '主键UUID',
    batch_id VARCHAR(36) NOT NULL COMMENT '批次ID(关联demand_collection_batch)',
    demand_id VARCHAR(36) DEFAULT NULL COMMENT '农民需求ID(关联demand_farmer_detail,单条审核时)',
    summary_id VARCHAR(36) DEFAULT NULL COMMENT '汇总ID(关联demand_summary,批量审核时)',
    audit_type VARCHAR(20) NOT NULL COMMENT '审核类型(single/batch)',
    audit_level VARCHAR(20) NOT NULL COMMENT '审核层级(village/town/district/state/ministry)',
    admin_code VARCHAR(50) NOT NULL COMMENT '审核行政区划代码',
    admin_name VARCHAR(200) NOT NULL COMMENT '审核行政区划名称',
    audit_user_id VARCHAR(36) NOT NULL COMMENT '审核人ID',
    audit_user_name VARCHAR(100) NOT NULL COMMENT '审核人姓名',
    audit_time DATETIME NOT NULL COMMENT '审核时间',
    audit_action VARCHAR(20) NOT NULL COMMENT '审核动作(submit/approve/reject)',
    audit_result VARCHAR(20) NOT NULL COMMENT '审核结果(passed/rejected)',
    audit_opinion VARCHAR(1000) DEFAULT NULL COMMENT '审核意见(驳回时必填)',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0:否 1:是)',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    INDEX idx_batch_id (batch_id),
    INDEX idx_demand_id (demand_id),
    INDEX idx_summary_id (summary_id),
    INDEX idx_audit_level (audit_level),
    INDEX idx_audit_time (audit_time),
    CONSTRAINT fk_audit_batch FOREIGN KEY (batch_id) REFERENCES demand_collection_batch(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='需求审核记录表';
