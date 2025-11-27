-- ----------------------------
-- 更新feedback表结构
-- 日期: 2025-11-27
-- 说明: 为feedback表添加缺失字段，使其与实体类定义匹配
-- ----------------------------

-- 注意：此脚本仅在需要添加这些字段时执行
-- 当前代码已配置为不使用 priority 和 attachments 字段（标记为 @TableField(exist = false)）

-- 添加priority字段（如果需要在数据库中存储优先级）
-- ALTER TABLE feedback ADD COLUMN priority CHAR(1) DEFAULT '0' COMMENT '优先级(0-低/1-中/2-高/3-紧急)' AFTER contact_email;

-- 添加attachments字段（如果需要在数据库中存储附件）
-- ALTER TABLE feedback ADD COLUMN attachments VARCHAR(1000) DEFAULT NULL COMMENT '附件(多个用逗号分隔)' AFTER content;

-- 说明：
-- 1. priority字段已在实体类中标记为 @TableField(exist = false)，不会写入数据库
-- 2. 如果将来需要在数据库中存储这些字段，取消注释上述SQL并执行
-- 3. 执行后需要从实体类中移除 @TableField(exist = false) 注解
