-- ========================================
-- 供应商认证表 (supplier_cert)
-- ========================================

DROP TABLE IF EXISTS `supplier_cert`;

CREATE TABLE `supplier_cert` (
  `cert_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '认证ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `org_name` VARCHAR(100) NOT NULL COMMENT '企业/组织名称',
  `credit_code` VARCHAR(50) NOT NULL COMMENT '统一社会信用代码',
  `legal_person` VARCHAR(50) NOT NULL COMMENT '法定代表人/负责人',
  `legal_id` VARCHAR(18) NOT NULL COMMENT '法定代表人身份证号',
  `ad_code` VARCHAR(20) NOT NULL COMMENT '行政区划代码',
  `business_scope` VARCHAR(255) NOT NULL COMMENT '经营范围/主要产品',
  `license_path` VARCHAR(255) NOT NULL COMMENT '营业执照存储路径',
  `contact_name` VARCHAR(50) NOT NULL COMMENT '联系人姓名',
  `contact_phone` VARCHAR(20) NOT NULL COMMENT '联系人手机',
  `apply_time` DATETIME NOT NULL COMMENT '申请时间',
  `status` INT NOT NULL DEFAULT 1 COMMENT '认证状态：0-未通过，1-审核中，2-已通过',
  `approver_id` BIGINT DEFAULT NULL COMMENT '审批人ID',
  `approve_time` DATETIME DEFAULT NULL COMMENT '审批时间',
  `audit_opinion` VARCHAR(255) DEFAULT NULL COMMENT '审核意见',
  `reject_reason` VARCHAR(255) DEFAULT NULL COMMENT '驳回原因',
  `create_people` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_people` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志：0-正常，2-删除',
  PRIMARY KEY (`cert_id`),
  UNIQUE KEY `uk_credit_code` (`credit_code`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_apply_time` (`apply_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商认证表';

-- ========================================
-- 初始化测试数据（可选）
-- ========================================

-- 插入一条审核中的认证申请示例
INSERT INTO `supplier_cert` (
  `user_id`, `org_name`, `credit_code`, `legal_person`, `legal_id`,
  `ad_code`, `business_scope`, `license_path`, `contact_name`, `contact_phone`,
  `apply_time`, `status`, `create_people`, `create_time`, `del_flag`
) VALUES (
  10001, '测试科技有限公司', '91110105MA01G51234', '张三', '110101199001011234',
  '110105', '电子产品研发、销售', '/uploads/licenses/test_license.jpg', '李四', '13800138000',
  NOW(), 1, 'system', NOW(), '0'
);

-- 插入一条已通过的认证记录示例
INSERT INTO `supplier_cert` (
  `user_id`, `org_name`, `credit_code`, `legal_person`, `legal_id`,
  `ad_code`, `business_scope`, `license_path`, `contact_name`, `contact_phone`,
  `apply_time`, `status`, `approver_id`, `approve_time`, `audit_opinion`,
  `create_people`, `create_time`, `update_people`, `update_time`, `del_flag`
) VALUES (
  10002, '示例农业科技公司', '91110105MA01G56789', '王五', '110101199101021234',
  '110105', '农业技术研发、农资销售', '/uploads/licenses/sample_license.jpg', '赵六', '13900139000',
  DATE_SUB(NOW(), INTERVAL 2 DAY), 2, 30001, DATE_SUB(NOW(), INTERVAL 1 DAY), '材料齐全，符合要求',
  'system', DATE_SUB(NOW(), INTERVAL 2 DAY), 'admin', DATE_SUB(NOW(), INTERVAL 1 DAY), '0'
);
