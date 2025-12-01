-- ============================================
-- 育种许可信息表
-- ============================================
CREATE TABLE breeding_license (
    id VARCHAR(36) NOT NULL COMMENT '主键ID',
    batch_id VARCHAR(36) NOT NULL COMMENT '育种批次ID',
    dataset_id VARCHAR(36) NOT NULL COMMENT '数据集ID',
    license_no VARCHAR(50) NOT NULL COMMENT '许可证号',
    approval_org VARCHAR(100) NOT NULL COMMENT '审批机构',
    approval_date DATE NOT NULL COMMENT '批准日期',
    valid_start_date DATE DEFAULT NULL COMMENT '有效期开始日期',
    valid_end_date DATE DEFAULT NULL COMMENT '有效期结束日期',
    certificate_file VARCHAR(255) DEFAULT NULL COMMENT '认证文件路径',
    license_status VARCHAR(20) NOT NULL DEFAULT 'valid' COMMENT '许可状态:valid/expired/revoked',
    status CHAR(1) NOT NULL DEFAULT '1' COMMENT '状态:1有效0无效',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人ID',
    created_by_name VARCHAR(50) DEFAULT NULL COMMENT '创建人姓名',
    created_org_code VARCHAR(36) DEFAULT NULL COMMENT '创建机构代码',
    created_org_name VARCHAR(100) DEFAULT NULL COMMENT '创建机构名称',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by VARCHAR(36) DEFAULT NULL COMMENT '更新人ID',
    updated_time DATETIME DEFAULT NULL COMMENT '更新时间',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    deleted CHAR(1) NOT NULL DEFAULT '0' COMMENT '删除标记:0未删除1已删除',
    PRIMARY KEY (id),
    UNIQUE INDEX uk_batch_id (batch_id),
    UNIQUE INDEX uk_license_no (license_no),
    INDEX idx_dataset_id (dataset_id),
    INDEX idx_license_status (license_status),
    INDEX idx_approval_date (approval_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='育种许可信息表';

-- ============================================
-- 物种特性表
-- ============================================
CREATE TABLE breeding_variety_traits (
    id VARCHAR(36) NOT NULL COMMENT '主键ID',
    license_id VARCHAR(36) NOT NULL COMMENT '许可ID',
    batch_id VARCHAR(36) NOT NULL COMMENT '育种批次ID',
    min_yield_potential DECIMAL(10,2) DEFAULT NULL COMMENT '最小产量潜力',
    max_yield_potential DECIMAL(10,2) DEFAULT NULL COMMENT '最大产量潜力',
    disease_resistance TEXT DEFAULT NULL COMMENT '抗病性(JSON)',
    stress_tolerance TEXT DEFAULT NULL COMMENT '压力耐受性(JSON)',
    maturity_days INT DEFAULT NULL COMMENT '成熟期(天)',
    plant_height DECIMAL(6,2) DEFAULT NULL COMMENT '株高(cm)',
    grain_quality_traits VARCHAR(500) DEFAULT NULL COMMENT '谷物品质性状',
    other_traits TEXT DEFAULT NULL COMMENT '其他特性(JSON)',
    status CHAR(1) NOT NULL DEFAULT '1' COMMENT '状态:1有效0无效',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by VARCHAR(36) DEFAULT NULL COMMENT '更新人ID',
    updated_time DATETIME DEFAULT NULL COMMENT '更新时间',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    deleted CHAR(1) NOT NULL DEFAULT '0' COMMENT '删除标记:0未删除1已删除',
    PRIMARY KEY (id),
    INDEX idx_license_id (license_id),
    INDEX idx_batch_id (batch_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物种特性表';
