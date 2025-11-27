-- ===========================================================
-- 数据库迁移脚本: 添加 expiry_date 字段到 inv_stock_in_item 表
-- 执行日期: 2025-11-26
-- ===========================================================

-- 1. 添加 expiry_date 字段
ALTER TABLE `inv_stock_in_item`
ADD COLUMN `expiry_date` DATE NULL COMMENT '过期日期' AFTER `quantity`;

-- 2. 如果有现有数据，设置默认值（可选，根据实际情况调整）
-- UPDATE `inv_stock_in_item` SET `expiry_date` = DATE_ADD(NOW(), INTERVAL 365 DAY) WHERE `expiry_date` IS NULL;

-- 3. 将字段设置为 NOT NULL（在确保所有记录都有值之后）
-- ALTER TABLE `inv_stock_in_item` MODIFY COLUMN `expiry_date` DATE NOT NULL COMMENT '过期日期';
