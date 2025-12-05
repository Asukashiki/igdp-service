-- 修复 demand_audit_record 表结构
-- 添加缺失的 id 主键列和 batch_id 列

-- 方案1: 如果表是空的或者可以删除重建
DROP TABLE IF EXISTS demand_audit_record;

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
    INDEX idx_audit_time (audit_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='需求审核记录表';

-- 方案2: 如果表有数据需要保留，使用ALTER TABLE
-- 请注释掉上面的 DROP TABLE 和 CREATE TABLE，然后使用以下语句：
--
-- -- 添加 id 列作为新的主键
-- ALTER TABLE demand_audit_record ADD COLUMN id VARCHAR(36) NOT NULL FIRST;
--
-- -- 为现有数据生成UUID
-- UPDATE demand_audit_record SET id = UUID();
--
-- -- 删除旧的主键（如果存在）
-- ALTER TABLE demand_audit_record DROP PRIMARY KEY;
--
-- -- 设置 id 为主键
-- ALTER TABLE demand_audit_record ADD PRIMARY KEY (id);
--
-- -- 如果 batch_id 列不存在，添加它
-- -- ALTER TABLE demand_audit_record ADD COLUMN batch_id VARCHAR(36) NOT NULL COMMENT '批次ID' AFTER id;
--
-- -- 如果 batch_id 列已存在但不是 NOT NULL，修改它
-- ALTER TABLE demand_audit_record MODIFY COLUMN batch_id VARCHAR(36) NOT NULL COMMENT '批次ID(关联demand_collection_batch)';
--
-- -- 添加索引
-- ALTER TABLE demand_audit_record ADD INDEX idx_batch_id (batch_id);
