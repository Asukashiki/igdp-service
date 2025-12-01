-- =============================================
-- 数据库迁移脚本：添加文件名字段
-- 创建时间：2025-12-01
-- 说明：为育种许可表和实验室测试表添加文件名字段
-- =============================================

-- 1. 为育种许可表添加认证文件名称字段
ALTER TABLE `breeding_license`
ADD COLUMN `certificate_file_name` VARCHAR(255) NULL COMMENT '认证文件名称(原始文件名)'
AFTER `certificate_file`;

-- 2. 为实验室测试数据表添加实验室报告文件名称字段
ALTER TABLE `seed_laboratory_test_data`
ADD COLUMN `lab_report_file_name` VARCHAR(255) NULL COMMENT '实验室报告文件名称(原始文件名)'
AFTER `lab_report_file`;

-- 验证SQL
SELECT 'Migration completed successfully' AS status;
