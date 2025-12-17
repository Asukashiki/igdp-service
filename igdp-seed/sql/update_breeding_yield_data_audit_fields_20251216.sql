-- 田间检验/产量数据采集 表结构变更脚本（实体 @TableName("breeding_yield_data")）
-- 表：breeding_yield_data
-- 说明：新增 业务状态(status)、流程状态(workflow_status)、审核人(audit_by)、审核时间(audit_time)
-- 注意：MySQL 8+ 可用 IF NOT EXISTS，低版本请移除后执行

ALTER TABLE breeding_yield_data 
  ADD COLUMN IF NOT EXISTS `status` varchar(32) NULL COMMENT '业务状态：submit/approve' AFTER `recorder_name`;

ALTER TABLE breeding_yield_data 
  ADD COLUMN IF NOT EXISTS `workflow_status` varchar(32) NULL COMMENT '流程状态：字典 flow_status' AFTER `status`;

ALTER TABLE breeding_yield_data 
  ADD COLUMN IF NOT EXISTS `audit_by` varchar(64) NULL COMMENT '审核人' AFTER `updated_by`;

ALTER TABLE breeding_yield_data 
  ADD COLUMN IF NOT EXISTS `audit_time` datetime NULL COMMENT '审核时间' AFTER `audit_by`;

-- 可选索引
-- CREATE INDEX idx_breeding_yield_status ON breeding_yield_data(`status`);
-- CREATE INDEX idx_breeding_yield_wf ON breeding_yield_data(`workflow_status`);
-- CREATE INDEX idx_breeding_yield_audit_time ON breeding_yield_data(`audit_time`);
