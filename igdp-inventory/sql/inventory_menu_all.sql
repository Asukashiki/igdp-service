-- ==================== 库存管理系统菜单配置 ====================

-- 顶级系统菜单
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('inventory', '{"zh_CN": "出入库管理系统", "en_US": "Inventory Management System"}', '0', 400, 'inventory', NULL, 'Layout', NULL, '1', '0', 'M', '0', '0', '', 'ri-stack-line', 'admin', NOW(), '', NULL, '', NULL);

-- 出入库管理父菜单
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('6001', '{"zh_CN": "出入库管理", "en_US": "Inbound/Outbound Management"}', 'inventory', 1, 'inventory-management', NULL, 'ParentView', NULL, '1', '0', 'M', '0', '0', '', 'ri-exchange-line', 'admin', NOW(), '', NULL, '', 'inventory_app');

-- 入库管理子菜单
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('600101', '{"zh_CN": "入库管理", "en_US": "Inbound Management"}', '6001', 1, 'inventory/inbound', NULL, 'inventory/inbound/index', NULL, '1', '0', 'C', '0', '0', 'inventory:inbound:list', 'ri-inbox-line', 'admin', NOW(), '', NULL, '', 'inventory_app');

-- 出库管理子菜单
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('600102', '{"zh_CN": "出库管理", "en_US": "Outbound Management"}', '6001', 2, 'inventory/outbound', NULL, 'inventory/outbound/index', NULL, '1', '0', 'C', '0', '0', 'inventory:outbound:list', 'ri-truck-line', 'admin', NOW(), '', NULL, '', 'inventory_app');

-- 调拨管理菜单（与出入库管理平级）
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('6002', '{"zh_CN": "调拨管理", "en_US": "Transfer Management"}', 'inventory', 2, 'inventory/transfer', NULL, 'inventory/transfer/index', NULL, '1', '0', 'C', '0', '0', 'inventory:transfer:list', 'ri-arrow-left-right-line', 'admin', NOW(), '', NULL, '', 'inventory_app');
