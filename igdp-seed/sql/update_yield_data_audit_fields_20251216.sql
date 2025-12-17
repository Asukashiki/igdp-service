-- 田间检验/产量数据采集 表结构变更脚本
-- 表：breeding_yield_data
-- 说明：新增 状态(status)、流程审核状态(workflow_status)、审核人(audit_by)、审核时间(audit_time)
-- 注意：为兼容低版本 MySQL，去除 ADD COLUMN IF NOT EXISTS 与 AFTER 子句

-- 业务状态：示例值 submit/approve
ALTER TABLE breeding_yield_data 
  ADD COLUMN `status` varchar(32) NULL COMMENT '业务状态：submit/approve';

-- 流程状态：字典 flow_status
ALTER TABLE breeding_yield_data 
  ADD COLUMN `workflow_status` varchar(32) NULL COMMENT '流程状态：字典 flow_status';

-- 审核人
ALTER TABLE breeding_yield_data 
  ADD COLUMN `audit_by` varchar(64) NULL COMMENT '审核人';

-- 审核时间
ALTER TABLE breeding_yield_data 
  ADD COLUMN `audit_time` datetime NULL COMMENT '审核时间';

-- 可选：索引（按需执行）
-- CREATE INDEX idx_breeding_yield_status ON breeding_yield_data(`status`);
-- CREATE INDEX idx_breeding_yield_wf_status ON breeding_yield_data(`workflow_status`);
-- CREATE INDEX idx_breeding_yield_audit_time ON breeding_yield_data(`audit_time`);
