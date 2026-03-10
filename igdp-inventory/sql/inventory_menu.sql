-- Inventory Management System Menu (Independent System - Top Level)
-- Step 1: Add top-level system menu (like 'research', 'input')
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('inventory', '{"zh_CN": "出入库管理系统", "en_US": "Inventory Management System"}', '0', 400, 'inventory', NULL, 'Layout', NULL, '1', '0', 'M', '0', '0', '', 'ri-stack-line', 'admin', '2026-03-07 12:00:00', '', NULL, '', NULL);

-- Step 2: Add parent menu for inbound/outbound (child of 'inventory')
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('6001', '{"zh_CN": "出入库管理", "en_US": "Inbound/Outbound Management"}', 'inventory', 1, 'inventory-management', NULL, 'ParentView', NULL, '1', '0', 'M', '0', '0', '', 'ri-exchange-line', 'admin', '2026-03-07 12:00:00', '', NULL, '', 'inventory_app');

-- Step 3: Inbound Management sub-menu
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('600101', '{"zh_CN": "入库管理", "en_US": "Inbound Management"}', '6001', 1, 'inventory/inbound', NULL, 'inventory/inbound/index', NULL, '1', '0', 'C', '0', '0', 'inventory:inbound:list', 'ri-inbox-line', 'admin', '2026-03-07 12:00:00', '', NULL, '', 'inventory_app');

-- Step 4: Outbound Management sub-menu (FIX: icon changed to ri-truck-line)
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('600102', '{"zh_CN": "出库管理", "en_US": "Outbound Management"}', '6001', 2, 'inventory/outbound', NULL, 'inventory/outbound/index', NULL, '1', '0', 'C', '0', '0', 'inventory:outbound:list', 'ri-truck-line', 'admin', '2026-03-07 12:00:00', '', NULL, '', 'inventory_app');
