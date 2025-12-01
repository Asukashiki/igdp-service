-- ============================================
-- 育种数据集审核记录表
-- ============================================
CREATE TABLE breeding_dataset_audit (
    id VARCHAR(36) NOT NULL COMMENT '主键ID',
    dataset_id VARCHAR(36) NOT NULL COMMENT '数据集ID',
    batch_id VARCHAR(36) NOT NULL COMMENT '育种批次ID',
    audit_node VARCHAR(50) DEFAULT NULL COMMENT '审核节点',
    audit_order INT DEFAULT 1 COMMENT '审核顺序',
    audit_status VARCHAR(20) NOT NULL COMMENT '审核状态:pending/approved/rejected',
    audit_opinion VARCHAR(1000) DEFAULT NULL COMMENT '审核意见',
    audit_time DATETIME DEFAULT NULL COMMENT '审核时间',
    auditor_id VARCHAR(36) DEFAULT NULL COMMENT '审核人ID',
    auditor_name VARCHAR(50) DEFAULT NULL COMMENT '审核人姓名',
    auditor_org_code VARCHAR(36) DEFAULT NULL COMMENT '审核人机构代码',
    auditor_org_name VARCHAR(100) DEFAULT NULL COMMENT '审核人机构名称',
    submit_time DATETIME NOT NULL COMMENT '提交时间',
    submitter_id VARCHAR(36) NOT NULL COMMENT '提交人ID',
    submitter_name VARCHAR(50) DEFAULT NULL COMMENT '提交人姓名',
    status CHAR(1) NOT NULL DEFAULT '1' COMMENT '状态:1有效0无效',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by VARCHAR(36) DEFAULT NULL COMMENT '更新人ID',
    updated_time DATETIME DEFAULT NULL COMMENT '更新时间',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    deleted CHAR(1) NOT NULL DEFAULT '0' COMMENT '删除标记:0未删除1已删除',
    PRIMARY KEY (id),
    INDEX idx_dataset_id (dataset_id),
    INDEX idx_batch_id (batch_id),
    INDEX idx_audit_status (audit_status),
    INDEX idx_submit_time (submit_time),
    INDEX idx_audit_time (audit_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='育种数据集审核记录表';
