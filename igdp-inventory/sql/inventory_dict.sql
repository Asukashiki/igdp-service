-- 入库/出库管理系统字典配置

-- ==================== 入库类型字典 ====================
-- 字典类型
INSERT INTO agriculture_igdp_admin.sys_dict_type
(DICT_ID, DICT_NAME, DICT_TYPE, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK)
VALUES('1100', '入库类型', 'inbound_type', '0', 'admin', NOW(), '', NULL, '入库单类型');

-- 字典数据
INSERT INTO agriculture_igdp_admin.sys_dict_data
(DICT_CODE, DICT_SORT, dict_label, DICT_VALUE, DICT_TYPE, CSS_CLASS, LIST_CLASS, IS_DEFAULT, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, actual_value)
VALUES('110000', 1, '{"zh_CN":"一般入库","en_US":"General Inbound"}', 'GENERAL', 'inbound_type', NULL, 'primary', 'Y', '0', 'admin', NOW(), '', NULL, '一般入库', NULL);

INSERT INTO agriculture_igdp_admin.sys_dict_data
(DICT_CODE, DICT_SORT, dict_label, DICT_VALUE, DICT_TYPE, CSS_CLASS, LIST_CLASS, IS_DEFAULT, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, actual_value)
VALUES('110001', 2, '{"zh_CN":"调拨入库","en_US":"Transfer Inbound"}', 'TRANSFER', 'inbound_type', NULL, 'success', 'N', '0', 'admin', NOW(), '', NULL, '调拨入库', NULL);

-- ==================== 出库类型字典 ====================
-- 字典类型
INSERT INTO agriculture_igdp_admin.sys_dict_type
(DICT_ID, DICT_NAME, DICT_TYPE, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK)
VALUES('1101', '出库类型', 'outbound_type', '0', 'admin', NOW(), '', NULL, '出库单类型');

-- 字典数据
INSERT INTO agriculture_igdp_admin.sys_dict_data
(DICT_CODE, DICT_SORT, dict_label, DICT_VALUE, DICT_TYPE, CSS_CLASS, LIST_CLASS, IS_DEFAULT, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, actual_value)
VALUES('110100', 1, '{"zh_CN":"一般出库","en_US":"General Outbound"}', 'GENERAL', 'outbound_type', NULL, 'primary', 'Y', '0', 'admin', NOW(), '', NULL, '一般出库', NULL);

INSERT INTO agriculture_igdp_admin.sys_dict_data
(DICT_CODE, DICT_SORT, dict_label, DICT_VALUE, DICT_TYPE, CSS_CLASS, LIST_CLASS, IS_DEFAULT, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, actual_value)
VALUES('110101', 2, '{"zh_CN":"调拨出库","en_US":"Transfer Outbound"}', 'TRANSFER', 'outbound_type', NULL, 'success', 'N', '0', 'admin', NOW(), '', NULL, '调拨出库', NULL);

-- ==================== 接收人类型字典 ====================
-- 字典类型
INSERT INTO agriculture_igdp_admin.sys_dict_type
(DICT_ID, DICT_NAME, DICT_TYPE, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK)
VALUES('1102', '接收人类型', 'receiver_type', '0', 'admin', NOW(), '', NULL, '出库接收人类型');

-- 字典数据
INSERT INTO agriculture_igdp_admin.sys_dict_data
(DICT_CODE, DICT_SORT, dict_label, DICT_VALUE, DICT_TYPE, CSS_CLASS, LIST_CLASS, IS_DEFAULT, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, actual_value)
VALUES('110200', 1, '{"zh_CN":"农户","en_US":"Farmer"}', 'FARMER', 'receiver_type', NULL, 'primary', 'Y', '0', 'admin', NOW(), '', NULL, '农户', NULL);

INSERT INTO agriculture_igdp_admin.sys_dict_data
(DICT_CODE, DICT_SORT, dict_label, DICT_VALUE, DICT_TYPE, CSS_CLASS, LIST_CLASS, IS_DEFAULT, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, actual_value)
VALUES('110201', 2, '{"zh_CN":"合作社","en_US":"Cooperative"}', 'COOP', 'receiver_type', NULL, 'success', 'N', '0', 'admin', NOW(), '', NULL, '合作社', NULL);

INSERT INTO agriculture_igdp_admin.sys_dict_data
(DICT_CODE, DICT_SORT, dict_label, DICT_VALUE, DICT_TYPE, CSS_CLASS, LIST_CLASS, IS_DEFAULT, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, actual_value)
VALUES('110202', 3, '{"zh_CN":"其他","en_US":"Other"}', 'OTHER', 'receiver_type', NULL, 'info', 'N', '0', 'admin', NOW(), '', NULL, '其他', NULL);

-- ==================== 入库状态字典 ====================
-- 字典类型
INSERT INTO agriculture_igdp_admin.sys_dict_type
(DICT_ID, DICT_NAME, DICT_TYPE, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK)
VALUES('1103', '入库单状态', 'inbound_status', '0', 'admin', NOW(), '', NULL, '入库单状态');

-- 字典数据
INSERT INTO agriculture_igdp_admin.sys_dict_data
(DICT_CODE, DICT_SORT, dict_label, DICT_VALUE, DICT_TYPE, CSS_CLASS, LIST_CLASS, IS_DEFAULT, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, actual_value)
VALUES('110300', 1, '{"zh_CN":"草稿","en_US":"Draft"}', 'DRAFT', 'inbound_status', NULL, 'info', 'Y', '0', 'admin', NOW(), '', NULL, '草稿', NULL);

INSERT INTO agriculture_igdp_admin.sys_dict_data
(DICT_CODE, DICT_SORT, dict_label, DICT_VALUE, DICT_TYPE, CSS_CLASS, LIST_CLASS, IS_DEFAULT, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, actual_value)
VALUES('110301', 2, '{"zh_CN":"待审批","en_US":"Pending Approval"}', 'SUBMITTED', 'inbound_status', NULL, 'warning', 'N', '0', 'admin', NOW(), '', NULL, '待审批', NULL);

INSERT INTO agriculture_igdp_admin.sys_dict_data
(DICT_CODE, DICT_SORT, dict_label, DICT_VALUE, DICT_TYPE, CSS_CLASS, LIST_CLASS, IS_DEFAULT, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, actual_value)
VALUES('110302', 3, '{"zh_CN":"已完成","en_US":"Approved"}', 'APPROVED', 'inbound_status', NULL, 'success', 'N', '0', 'admin', NOW(), '', NULL, '已完成', NULL);

INSERT INTO agriculture_igdp_admin.sys_dict_data
(DICT_CODE, DICT_SORT, dict_label, DICT_VALUE, DICT_TYPE, CSS_CLASS, LIST_CLASS, IS_DEFAULT, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, actual_value)
VALUES('110303', 4, '{"zh_CN":"未通过","en_US":"Rejected"}', 'REJECTED', 'inbound_status', NULL, 'danger', 'N', '0', 'admin', NOW(), '', NULL, '未通过', NULL);

-- ==================== 出库状态字典 ====================
-- 字典类型
INSERT INTO agriculture_igdp_admin.sys_dict_type
(DICT_ID, DICT_NAME, DICT_TYPE, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK)
VALUES('1104', '出库单状态', 'outbound_status', '0', 'admin', NOW(), '', NULL, '出库单状态');

-- 字典数据
INSERT INTO agriculture_igdp_admin.sys_dict_data
(DICT_CODE, DICT_SORT, dict_label, DICT_VALUE, DICT_TYPE, CSS_CLASS, LIST_CLASS, IS_DEFAULT, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, actual_value)
VALUES('110400', 1, '{"zh_CN":"草稿","en_US":"Draft"}', 'DRAFT', 'outbound_status', NULL, 'info', 'Y', '0', 'admin', NOW(), '', NULL, '草稿', NULL);

INSERT INTO agriculture_igdp_admin.sys_dict_data
(DICT_CODE, DICT_SORT, dict_label, DICT_VALUE, DICT_TYPE, CSS_CLASS, LIST_CLASS, IS_DEFAULT, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, actual_value)
VALUES('110401', 2, '{"zh_CN":"待审批","en_US":"Pending Approval"}', 'SUBMITTED', 'outbound_status', NULL, 'warning', 'N', '0', 'admin', NOW(), '', NULL, '待审批', NULL);

INSERT INTO agriculture_igdp_admin.sys_dict_data
(DICT_CODE, DICT_SORT, dict_label, DICT_VALUE, DICT_TYPE, CSS_CLASS, LIST_CLASS, IS_DEFAULT, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, actual_value)
VALUES('110402', 3, '{"zh_CN":"已完成","en_US":"Approved"}', 'APPROVED', 'outbound_status', NULL, 'success', 'N', '0', 'admin', NOW(), '', NULL, '已完成', NULL);

INSERT INTO agriculture_igdp_admin.sys_dict_data
(DICT_CODE, DICT_SORT, dict_label, DICT_VALUE, DICT_TYPE, CSS_CLASS, LIST_CLASS, IS_DEFAULT, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, actual_value)
VALUES('110403', 4, '{"zh_CN":"未通过","en_US":"Rejected"}', 'REJECTED', 'outbound_status', NULL, 'danger', 'N', '0', 'admin', NOW(), '', NULL, '未通过', NULL);
