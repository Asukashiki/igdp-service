ALTER TABLE inventory_inbound_order ADD COLUMN flag VARCHAR(1) DEFAULT '0' COMMENT '数据标识（0/1）';
ALTER TABLE inventory_outbound_order ADD COLUMN flag VARCHAR(1) DEFAULT '0' COMMENT '数据标识（0/1）';
