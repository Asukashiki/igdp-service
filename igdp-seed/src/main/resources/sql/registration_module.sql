-- ========================================
-- 注册管理模块 - 数据库表结构
-- ========================================

-- 机构基础信息表
CREATE TABLE IF NOT EXISTS org_enterprise_info (
    id VARCHAR(36) NOT NULL COMMENT '主键UUID',
    enterprise_name VARCHAR(200) NOT NULL COMMENT '机构名称',
    enterprise_registration_id VARCHAR(100) DEFAULT NULL COMMENT '企业注册号',
    unified_social_credit_code VARCHAR(50) DEFAULT NULL COMMENT '统一社会信用代码',
    seed_enterprise_license_number VARCHAR(100) NOT NULL COMMENT '种子企业许可证号',
    license_validity_start DATE NOT NULL COMMENT '许可证有效期开始',
    license_validity_end DATE NOT NULL COMMENT '许可证有效期结束',
    enterprise_type VARCHAR(50) NOT NULL COMMENT '企业类型',
    org_type VARCHAR(20) NOT NULL COMMENT '机构类型(union/cooperative)',
    input_types VARCHAR(100) NOT NULL COMMENT '投入品类型(多选,逗号分隔:seed,fertilizer,pesticide)',
    sales_regions TEXT NOT NULL COMMENT '销售区域(组织机构代码,多选,逗号分隔)',
    application_status VARCHAR(20) NOT NULL DEFAULT 'draft' COMMENT '申请状态(draft/pending/approved/rejected)',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by VARCHAR(36) DEFAULT NULL COMMENT '更新人ID',
    updated_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0:否 1:是)',
    version INT NOT NULL DEFAULT 1 COMMENT '版本号(乐观锁)',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    INDEX idx_org_type (org_type),
    INDEX idx_application_status (application_status),
    INDEX idx_created_time (created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机构基础信息表';

-- 机构位置运营信息表
CREATE TABLE IF NOT EXISTS org_enterprise_location (
    id VARCHAR(36) NOT NULL COMMENT '主键UUID',
    enterprise_id VARCHAR(36) NOT NULL COMMENT '机构ID(关联org_enterprise_info)',
    region VARCHAR(100) DEFAULT NULL COMMENT '大区',
    zone VARCHAR(100) DEFAULT NULL COMMENT 'Zone',
    woreda VARCHAR(100) NOT NULL COMMENT 'Woreda',
    kebele VARCHAR(100) NOT NULL COMMENT 'Kebele',
    full_address VARCHAR(500) NOT NULL COMMENT '详细地址',
    gps_latitude DECIMAL(10,7) DEFAULT NULL COMMENT 'GPS纬度',
    gps_longitude DECIMAL(10,7) DEFAULT NULL COMMENT 'GPS经度',
    business_scope VARCHAR(500) DEFAULT NULL COMMENT '经营范围',
    annual_production_capacity DECIMAL(15,2) DEFAULT NULL COMMENT '年生产能力',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by VARCHAR(36) DEFAULT NULL COMMENT '更新人ID',
    updated_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0:否 1:是)',
    PRIMARY KEY (id),
    INDEX idx_enterprise_id (enterprise_id),
    CONSTRAINT fk_location_enterprise FOREIGN KEY (enterprise_id) REFERENCES org_enterprise_info(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机构位置运营信息表';

-- 机构许可证件表
CREATE TABLE IF NOT EXISTS org_enterprise_license (
    id VARCHAR(36) NOT NULL COMMENT '主键UUID',
    enterprise_id VARCHAR(36) NOT NULL COMMENT '机构ID(关联org_enterprise_info)',
    license_type VARCHAR(50) NOT NULL COMMENT '证件类型(business_license/seed_license/tax_certificate/factory_permit)',
    license_number VARCHAR(100) DEFAULT NULL COMMENT '证件号码',
    license_file_url VARCHAR(500) NOT NULL COMMENT '证件文件URL',
    issue_date DATE DEFAULT NULL COMMENT '发证日期',
    expiry_date DATE DEFAULT NULL COMMENT '到期日期',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by VARCHAR(36) DEFAULT NULL COMMENT '更新人ID',
    updated_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0:否 1:是)',
    PRIMARY KEY (id),
    INDEX idx_enterprise_id (enterprise_id),
    INDEX idx_license_type (license_type),
    CONSTRAINT fk_license_enterprise FOREIGN KEY (enterprise_id) REFERENCES org_enterprise_info(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机构许可证件表';

-- 机构注册审核记录表
CREATE TABLE IF NOT EXISTS org_registration_audit (
    id VARCHAR(36) NOT NULL COMMENT '主键UUID',
    enterprise_id VARCHAR(36) NOT NULL COMMENT '机构ID(关联org_enterprise_info)',
    audit_user_id VARCHAR(36) NOT NULL COMMENT '审核人ID',
    audit_user_name VARCHAR(100) NOT NULL COMMENT '审核人姓名',
    audit_time DATETIME NOT NULL COMMENT '审核时间',
    audit_result VARCHAR(20) NOT NULL COMMENT '审核结果(approved/rejected)',
    audit_opinion VARCHAR(1000) DEFAULT NULL COMMENT '审核意见(驳回时必填)',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0:否 1:是)',
    PRIMARY KEY (id),
    INDEX idx_enterprise_id (enterprise_id),
    INDEX idx_audit_time (audit_time),
    CONSTRAINT fk_audit_enterprise FOREIGN KEY (enterprise_id) REFERENCES org_enterprise_info(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机构注册审核记录表';
