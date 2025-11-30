-- ============================================
-- 田间检验和实验室测试数据采集模块
-- 包含两张表:
-- 1. breeding_lab_test - 实验室测试数据表
-- 2. breeding_yield_data - 产量数据表
-- ============================================

-- ============================================
-- 1. 实验室测试数据表
-- ============================================
CREATE TABLE breeding_lab_test (
    id VARCHAR(36) NOT NULL COMMENT '主键ID',
    batch_id VARCHAR(36) NOT NULL COMMENT '育种批次ID',
    trial_id VARCHAR(36) NOT NULL COMMENT '试验ID',
    sample_id VARCHAR(50) NOT NULL COMMENT '样本编号',
    sample_status VARCHAR(50) DEFAULT NULL COMMENT '样本状态',
    germination_rate DECIMAL(5,2) NOT NULL COMMENT '发芽率(%)',
    purity_percent DECIMAL(5,2) NOT NULL COMMENT '纯度(%)',
    moisture_content_percent DECIMAL(5,2) NOT NULL COMMENT '水分含量(%)',
    protein_percent DECIMAL(5,2) DEFAULT NULL COMMENT '蛋白质含量(%)',
    toxin_level_ppm DECIMAL(10,4) DEFAULT NULL COMMENT '毒素水平(PPM)',
    seed_health_findings VARCHAR(500) DEFAULT NULL COMMENT '种子健康发现',
    chain_responsibility VARCHAR(200) DEFAULT NULL COMMENT '链路责任',
    lab_report_file VARCHAR(255) DEFAULT NULL COMMENT '实验室报告文件路径',
    test_date DATE NOT NULL COMMENT '检测日期',
    test_organization VARCHAR(100) DEFAULT NULL COMMENT '检测机构',
    tester_name VARCHAR(50) DEFAULT NULL COMMENT '检测人员',
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
    INDEX idx_batch_id (batch_id),
    INDEX idx_trial_id (trial_id),
    INDEX idx_sample_id (sample_id),
    INDEX idx_test_date (test_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实验室测试数据表';

-- ============================================
-- 2. 产量数据表
-- ============================================
CREATE TABLE breeding_yield_data (
    id VARCHAR(36) NOT NULL COMMENT '主键ID',
    batch_id VARCHAR(36) NOT NULL COMMENT '育种批次ID',
    trial_id VARCHAR(36) NOT NULL COMMENT '试验ID',
    plot_id VARCHAR(50) NOT NULL COMMENT '地块编号',
    plot_area_m2 DECIMAL(12,2) NOT NULL COMMENT '地块面积(m²)',
    grain_weight_kg DECIMAL(12,3) NOT NULL COMMENT '谷物重量(kg)',
    yield_qt_per_ha DECIMAL(10,2) NOT NULL COMMENT '产量(公担/公顷)',
    moisture_content DECIMAL(5,2) DEFAULT NULL COMMENT '含水量(%)',
    harvest_date DATE NOT NULL COMMENT '收获日期',
    recorder_name VARCHAR(50) DEFAULT NULL COMMENT '记录人员',
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
    INDEX idx_batch_id (batch_id),
    INDEX idx_trial_id (trial_id),
    INDEX idx_harvest_date (harvest_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产量数据表';
