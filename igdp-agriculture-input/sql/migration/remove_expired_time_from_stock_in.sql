-- ===========================================================
-- 数据库迁移脚本: 删除 inv_stock_in 表中的 expired_time 字段
-- 执行日期: 2025-11-26
-- 说明: 过期日期已移至 inv_stock_in_item 表的 expiry_date 字段
-- ===========================================================

-- 删除 expired_time 字段
ALTER TABLE `inv_stock_in`
DROP COLUMN `expired_time`;
