-- 15. 入库单表补充审批字段
ALTER TABLE inventory_inbound_order ADD COLUMN audit_by VARCHAR(64) DEFAULT '' COMMENT '审批人';
ALTER TABLE inventory_inbound_order ADD COLUMN audit_time DATETIME COMMENT '审批时间';
ALTER TABLE inventory_inbound_order ADD COLUMN audit_comment VARCHAR(500) DEFAULT '' COMMENT '审批意见';

-- 16. 出库单表补充审批字段
ALTER TABLE inventory_outbound_order ADD COLUMN audit_by VARCHAR(64) DEFAULT '' COMMENT '审批人';
ALTER TABLE inventory_outbound_order ADD COLUMN audit_time DATETIME COMMENT '审批时间';
ALTER TABLE inventory_outbound_order ADD COLUMN audit_comment VARCHAR(500) DEFAULT '' COMMENT '审批意见';
