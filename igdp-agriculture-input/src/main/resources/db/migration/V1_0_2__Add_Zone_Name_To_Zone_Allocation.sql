-- 为区域分配额度主表添加区域名称字段

ALTER TABLE `t_zone_allocation`
ADD COLUMN `zone_name` varchar(255) NULL COMMENT '区域名称' AFTER `zone`;