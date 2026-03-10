INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('1000', '{"zh_CN":"育种数据管理","en_US":"Breeding Data Management"}', 'research', 1, 'breeding-data-management', NULL, 'ParentView', NULL, '1', '0', 'M', '0', '0', '', 'ri-database-2-line', NULL, '2026-01-12 09:53:14', 'superAdmin', '2026-02-04 15:56:52', '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('1001', '{"zh_CN": "育种批次", "en_US": "Breeding Batch"}', '1000', 1, 'research/breeding-data/batch', NULL, 'research/breeding-data/batch/index', NULL, '1', '0', 'C', '0', '0', 'research:breeding-data:batch:list', 'ri-calendar-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('1002', '{"zh_CN": "育种批次审核", "en_US": "Breeding Batch Audit"}', '1000', 2, 'research/breeding-data/batch/approve', NULL, 'research/breeding-data/batch/approve/index', NULL, '1', '0', 'C', '0', '0', 'research:breeding-data:batch:approve', 'ri-calendar-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('1003', '{"zh_CN": "试验基础信息", "en_US": "Trial Basic Information"}', '1000', 3, 'research/breeding-data/trial', NULL, 'research/breeding-data/trial/index', NULL, '1', '0', 'C', '0', '0', 'research:breeding-data:trial:list', 'ri-test-tube-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('1004', '{"zh_CN": "试验基础信息审核", "en_US": "Trial Basic Information Audit"}', '1000', 4, 'research/breeding-data/trial-audit', NULL, 'research/breeding-data/trial-audit/index', NULL, '1', '0', 'C', '0', '0', 'research:breeding-data:trial-audit:list', 'ri-pass-pending-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('1005', '{"zh_CN": "小区与播种信息", "en_US": "Plot and Sowing Information"}', '1000', 5, 'research/breeding-data/plot', NULL, 'research/breeding-data/plot/index', NULL, '1', '0', 'C', '0', '0', 'research:breeding-data:plot:list', 'ri-map-pin-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('1006', '{"zh_CN": "小区与播种信息审核", "en_US": "Plot and Sowing Information Audit"}', '1000', 6, 'research/breeding-data/plot-audit', NULL, 'research/breeding-data/plot-audit/index', NULL, '1', '0', 'C', '0', '0', 'research:breeding-data:plot-audit:list', 'ri-pass-pending-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('1007', '{"zh_CN": "农事记录采集", "en_US": "Farming Record Data Collection"}', '1000', 7, 'research/breeding-data/farming', NULL, 'research/breeding-data/farming/index', NULL, '1', '0', 'C', '0', '0', 'research:breeding-data:farming:list', 'ri-plant-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('1008', '{"zh_CN": "农事记录审核", "en_US": "Farming Record Data Collection Audit"}', '1000', 8, 'research/breeding-data/farming/farming-index', NULL, 'research/breeding-data/farming/farming-index/index', NULL, '1', '0', 'C', '0', '0', 'research:breeding-data:farming:audit', 'ri-file-check-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('1009', '{"zh_CN": "农艺性状数据采集", "en_US": "Agronomic Trait Data Collection"}', '1000', 9, 'research/breeding-data/trait', NULL, 'research/breeding-data/trait/index', NULL, '1', '0', 'C', '0', '0', 'research:breeding-data:trait:list', 'ri-leaf-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('1010', '{"zh_CN": "农艺性状数据审核", "en_US": "Agronomic Trait Data Audit"}', '1000', 10, 'research/breeding-data/trait-audit', NULL, 'research/breeding-data/trait-audit/index', NULL, '1', '0', 'C', '0', '0', 'research:breeding-data:trait-audit:list', 'ri-leaf-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('1011', '{"zh_CN": "环境监测数据", "en_US": "Environment Monitoring Data"}', '1000', 11, 'research/data-collection/environment-new-data', NULL, 'research/data-collection/environment-new-data/index', NULL, '1', '0', 'C', '0', '0', 'research:environment-data:list', 'ri-cloud-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('1012', '{"zh_CN": "环境监测数据审核", "en_US": "Environment Monitoring Data Approval"}', '1000', 12, 'research/data-collection/environment-new-data/approve', NULL, 'research/data-collection/environment-new-data/approve/index', NULL, '1', '0', 'C', '0', '0', 'research:environment-data:approve', 'ri-cloud-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('1013', '{"zh_CN": "田间检验采集", "en_US": "Field Inspection Data Collection"}', '1000', 13, 'research/breeding-data/field-inspection', NULL, 'research/breeding-data/field-inspection/index', NULL, '1', '0', 'C', '0', '0', 'research:field-inspection:list', 'ri-bar-chart-box-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('1014', '{"zh_CN": "田间检验审核", "en_US": "Field Inspection Data Collection Audit"}', '1000', 14, 'research/breeding-data/field-inspection-audit', NULL, 'research/breeding-data/field-inspection-audit/index', NULL, '1', '0', 'C', '0', '0', 'research:field-inspection:audit', 'ri-file-check-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('1015', '{"zh_CN": "实验室检测采集", "en_US": "Laboratory Test Data Collection"}', '1000', 15, 'research/breeding-data/laboratory-test', NULL, 'research/breeding-data/laboratory-test/index', NULL, '1', '0', 'C', '0', '0', 'research:laboratory-test:list', 'ri-flask-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('1016', '{"zh_CN": "实验室检测审核", "en_US": "Laboratory Test Data Audit"}', '1000', 16, 'research/breeding-data/laboratory-test-audit', NULL, 'research/breeding-data/laboratory-test-audit/index', NULL, '1', '0', 'C', '0', '0', 'research:laboratory-test:audit', 'ri-file-check-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('1017', '{"zh_CN": "育种数据集编制", "en_US": "Breeding Dataset Compilation"}', '1000', 17, 'research/breeding-data/dataset-compilation', NULL, 'research/breeding-data/dataset-compilation/index', NULL, '1', '0', 'C', '0', '0', 'research:dataset:list', 'ri-file-list-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('1018', '{"zh_CN": "育种数据集审核", "en_US": "Breeding Dataset Audit"}', '1000', 18, 'research/breeding-data/dataset-audit', NULL, 'research/breeding-data/dataset-audit/index', NULL, '1', '0', 'C', '0', '0', 'research:dataset:audit', 'ri-checkbox-circle-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('1019', '{"zh_CN": "育种种子生产数据", "en_US": "Seed Production Data"}', '1000', 19, 'research/breeding/seed-production', NULL, 'research/breeding/seed-production/index', NULL, '1', '0', 'C', '0', '0', 'research:breeding:production:list', 'ri-seedling-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('1020', '{"zh_CN": "生产数据采集", "en_US": "Production Data Collection"}', '1000', 20, 'research/breeding/seed-production-result', NULL, 'research/breeding/seed-production-result/index', NULL, '1', '0', 'C', '0', '0', 'research:breeding:production-result:list', 'ri-seedling-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('1021', '{"zh_CN": "原原种生产批次信息", "en_US": "Pre-Basic Seed Batch Info"}', '1000', 21, 'research/breeding/prebasic-seed-production', NULL, 'research/breeding/prebasic-seed-production/index', NULL, '1', '0', 'C', '0', '0', 'research:breeding:prebasic:list', 'ri-seedling-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('1022', '{"zh_CN": "原原种生产批次数据", "en_US": "Pre-Basic Seed Batch Data"}', '1000', 22, 'research/breeding/prebasic-seed-production-result', NULL, 'research/breeding/prebasic-seed-production-result/index', NULL, '1', '0', 'C', '0', '0', 'research:breeding:prebasic-result:list', 'ri-bar-chart-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('1023', '{"zh_CN": "原种生产批次信息", "en_US": "Basic Seed Batch Info"}', '1000', 23, 'research/breeding/basic-seed-production', NULL, 'research/breeding/basic-seed-production/index', NULL, '1', '0', 'C', '0', '0', 'research:breeding:basic:list', 'ri-seedling-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('1024', '{"zh_CN": "原种生产批次数据", "en_US": "Basic Seed Batch Data"}', '1000', 24, 'research/breeding/basic-seed-production-result', NULL, 'research/breeding/basic-seed-production-result/index', NULL, '1', '0', 'C', '0', '0', 'research:breeding:basic-result:list', 'ri-bar-chart-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('1025', '{"zh_CN": "育种种子分发数据", "en_US": "Seed Distribution Data"}', '1000', 25, 'research/breeding/seed-distribution', NULL, 'research/breeding/seed-distribution/index', NULL, '1', '0', 'C', '0', '0', 'research:breeding:distribution:list', 'ri-share-forward-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('2000', '{"zh_CN": "繁育数据管理", "en_US": "Multiplication Data Management"}', 'research', 2, 'propagation-data-management', NULL, 'ParentView', NULL, '1', '0', 'M', '0', '0', '', 'ri-plant-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('2001', '{"zh_CN": "OSE接收原原种确认", "en_US": "OSE Confirm Receipt of Breeder Seeds"}', '2000', 1, 'research/breeding/ose-receive-confirm', NULL, 'research/breeding/ose-receive-confirm/index', NULL, '1', '0', 'C', '0', '0', 'research:breeding:ose-confirm:list', 'ri-checkbox-circle-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('2002', '{"zh_CN": "繁育批次信息", "en_US": "Seed Multiplication Batch Info"}', '2000', 2, 'research/breeding', NULL, 'research/breeding/index', NULL, '1', '0', 'C', '0', '0', 'research:breeding:propagation:list', 'ri-folders-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('2003', '{"zh_CN": "繁育批次数据", "en_US": "Seed Multiplication Batch Data"}', '2000', 3, 'research/breeding/ose-batch-collection', NULL, 'research/breeding/ose-batch-collection/index', NULL, '1', '0', 'C', '0', '0', 'research:breeding:batch-data:list', 'ri-database-2-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('2004', '{"zh_CN": "种子繁育申请", "en_US": "Seed Multiplication Application"}', '2000', 4, 'research/c1-propagation', NULL, 'research/c1-propagation/index', NULL, '1', '0', 'C', '0', '0', 'research:c1:application:list', 'ri-plant-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('2005', '{"zh_CN": "种子繁育申请审核", "en_US": "Seed Multiplication Application Audit"}', '2000', 5, 'research/c1-propagation-audit', NULL, 'research/c1-propagation-audit/index', NULL, '1', '0', 'C', '0', '0', 'research:c1:audit:list', 'ri-pass-pending-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('2006', '{"zh_CN": "种子繁育信息", "en_US": "Seed Multiplication Information"}', '2000', 6, 'research/c1-breeding-batch', NULL, 'research/c1-breeding-batch/index', NULL, '1', '0', 'C', '0', '0', 'research:c1:batch:list', 'ri-folders-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('2007', '{"zh_CN": "田间检测", "en_US": "Field Detection"}', '2000', 7, 'research/field-detection', NULL, 'research/field-detection/index', NULL, '1', '0', 'C', '0', '0', 'research:field-detection:list', 'ri-bar-chart-box-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('2008', '{"zh_CN": "实验室检测", "en_US": "Lab Testing"}', '2000', 8, 'research/lab-testing', NULL, 'research/lab-testing/index', NULL, '1', '0', 'C', '0', '0', 'research:lab-testing:list', 'ri-flask-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('2009', '{"zh_CN": "种子繁育审核", "en_US": "Seed Multiplication Audit"}', '2000', 9, 'research/c1-breeding-batch-audit', NULL, 'research/c1-breeding-batch-audit/index', NULL, '1', '0', 'C', '0', '0', 'research:c1:batch-audit', 'ri-pass-pending-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('2010', '{"zh_CN": "种子证书核发", "en_US": "Seed Certificate Issuance"}', '2000', 10, 'research/c1-breeding-certificate', NULL, 'research/c1-breeding-certificate/index', NULL, '1', '0', 'C', '0', '0', 'research:c1:certificate', 'ri-award-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('22d275ab-fdc9-4e72-b4a1-e8c44f973acb', '{"zh_CN": "角色管理", "en_US": "Role Management"}', 'system', 2, 'system/role', NULL, 'system/role/index', NULL, '1', '0', 'C', '0', '0', 'system:role:list', 'ri-shield-user-line', '', '2025-12-09 14:29:44', '', NULL, '', NULL);
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('25acb5ebcf324d2b952a937c84cbc6b9', '{"en_US":"Data Overview","zh_CN":"数据概览"}', 'research', 5, 'research/data-overview', NULL, 'research/data-overview/index', NULL, '1', '0', 'C', '0', '0', 'research:dashboard:list', ' ri-dashboard-line', 'superAdmin', '2026-02-04 15:49:31', 'superAdmin', '2026-02-04 15:57:09', '', NULL);
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('3000', '{"zh_CN": "种子机构管理", "en_US": "Seed Institution Management"}', 'research', 3, 'seed-institution-management', NULL, 'ParentView', NULL, '1', '0', 'M', '0', '0', '', 'ri-bank-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('3001', '{"zh_CN": "繁育单位注册申请", "en_US": "Multiplier Registration Application"}', '3000', 1, 'research/institution/registration', NULL, 'research/institution/registration/index', NULL, '1', '0', 'C', '0', '0', 'research:inst:reg:list', 'ri-community-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('3002', '{"zh_CN": "繁育单位注册审核", "en_US": "Multiplier Registration Application Audit"}', '3000', 2, 'research/institution/approval', NULL, 'research/institution/approval/index', NULL, '1', '0', 'C', '0', '0', 'research:inst:approval:list', 'ri-community-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('3003', '{"zh_CN": "OSE维护管理", "en_US": "OSE Maintenance Management"}', '3000', 3, 'research/breeding/ose-management', NULL, 'research/breeding/ose-management/index', NULL, '1', '0', 'C', '0', '0', 'research:ose-management:list', 'ri-building-4-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('3004', '{"zh_CN": "研究中心管理", "en_US": "Research Center Management"}', '3000', 4, 'research/institution/research-center', NULL, 'research/institution/research-center/index', NULL, '1', '0', 'C', '0', '0', 'research:research-center:list', 'ri-building-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('4000', '{"zh_CN": "品种信息服务", "en_US": "Breed Information Service"}', 'research', 4, 'seed-service', NULL, 'ParentView', NULL, '1', '0', 'M', '0', '0', '', 'ri-information-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('4001', '{"zh_CN": "种子推广管理", "en_US": "Seed Promotion Management"}', '4000', 1, 'research/seed/promotion', NULL, 'research/seed/promotion/index', NULL, '1', '0', 'C', '0', '0', 'research:seed:promotion:list', 'ri-megaphone-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('4002', '{"zh_CN": "品种信息公示", "en_US": "Varietal Information Publicity"}', '4000', 2, 'research/seed/info', NULL, 'research/seed/info/index', NULL, '1', '0', 'C', '0', '0', 'research:seed:info:list', 'ri-file-list-3-line', NULL, '2026-01-12 09:53:14', '', NULL, '', 'breeding_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('5000', '{"zh_CN":"数据概览","en_US":"Data Overview"}', 'input', 11, 'input/dashboard', NULL, 'input/dashboard/index', NULL, '1', '0', 'C', '0', '0', 'input:dashboard:list', 'ri-dashboard-line', 'admin', '2026-01-12 10:23:43', 'superAdmin', '2026-01-12 10:24:11', '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('5001', '{"zh_CN": "注册管理", "en_US": "Registration Management"}', 'input', 2, 'registration-management', NULL, 'ParentView', NULL, '1', '0', 'M', '0', '0', '', 'ri-building-2-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('500101', '{"zh_CN": "联合会/合作社注册申请", "en_US": "Union/Cooperative Registration Application"}', '5001', 1, 'input/registration', NULL, 'input/registration/index', NULL, '1', '0', 'C', '0', '0', 'input:registration:list', 'ri-file-add-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('500102', '{"zh_CN": "联合会/合作社注册审核", "en_US": "Union/Cooperative Registration Approval"}', '5001', 2, 'input/registration/approval', NULL, 'input/registration/approval/index', NULL, '1', '0', 'C', '0', '0', 'input:registration:approval', 'ri-checkbox-circle-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('5002', '{"zh_CN": "农田管理", "en_US": "Farm Management"}', 'input', 3, 'newFarmManagement', NULL, 'ParentView', NULL, '1', '0', 'M', '0', '0', '', 'ri-plant-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('500201', '{"zh_CN": "DA管理", "en_US": "DA Management"}', '5002', 1, 'input/da', NULL, 'input/da/index', NULL, '1', '0', 'C', '0', '0', 'input:da:list', 'ri-user-star-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('500202', '{"zh_CN": "农民管理", "en_US": "Farmer Management"}', '5002', 2, 'input/farmer', NULL, 'input/farmer/index', NULL, '1', '0', 'C', '0', '0', 'input:farmer:list', 'ri-user-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('500203', '{"zh_CN": "土地管理", "en_US": "Land Management"}', '5002', 3, 'input/land', NULL, 'input/land/index', NULL, '1', '0', 'C', '0', '0', 'input:land:list', 'ri-landscape-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('500204', '{"zh_CN":"领用地点管理","en_US":"Territory Management"}', '5002', 4, 'input/territory', NULL, 'input/territory/index', NULL, '1', '0', 'C', '0', '0', 'input:territory:list', 'ri-map-2-line', 'admin', '2026-03-05 11:49:25', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('5003', '{"zh_CN": "投入品管理", "en_US": "Input Management"}', 'input', 4, 'input-management', NULL, 'ParentView', NULL, '1', '0', 'M', '0', '0', '', 'ri-box-3-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('500301', '{"zh_CN": "投入品目录管理", "en_US": "Input Catalog Management"}', '5003', 1, 'input/catalog', NULL, 'input/catalog/index', NULL, '1', '0', 'C', '0', '0', 'input:catalog:list', 'ri-list-check', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('5004', '{"zh_CN": "投入品需求管理", "en_US": "Input Demand Management"}', 'input', 5, 'demand-management', NULL, 'ParentView', NULL, '1', '0', 'M', '0', '0', '', 'ri-file-edit-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('500401', '{"zh_CN": "DA农民需求录入", "en_US": "DA Farmer Demand Entry"}', '5004', 1, 'input/demand/farmer', NULL, 'input/demand/farmer/index', NULL, '1', '0', 'C', '0', '0', 'input:demand:farmer:list', 'ri-user-add-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('500402', '{"zh_CN": "村级需求汇总", "en_US": "Kebele Demand Aggregation"}', '5004', 2, 'input/demand/aggregation', NULL, 'input/demand/aggregation/index', NULL, '1', '0', 'C', '0', '0', 'input:demand:village:list', 'ri-database-2-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('500403', '{"zh_CN": "镇级需求汇总", "en_US": "Woreda Demand Aggregation"}', '5004', 3, 'input/demand/aggregation-town', NULL, 'input/demand/aggregation-town/index', NULL, '1', '0', 'C', '0', '0', 'input:demand:town:list', 'ri-community-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('500404', '{"zh_CN": "区级需求汇总", "en_US": "Zone Demand Aggregation"}', '5004', 4, 'input/demand/aggregation-district', NULL, 'input/demand/aggregation-district/index', NULL, '1', '0', 'C', '0', '0', 'input:demand:district:list', 'ri-government-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('500405', '{"zh_CN": "省级需求汇总", "en_US": "Regional Demand Aggregation"}', '5004', 5, 'input/demand/aggregation-state', NULL, 'input/demand/aggregation-state/index', NULL, '1', '0', 'C', '0', '0', 'input:demand:state:list', 'ri-building-4-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('5005', '{"zh_CN": "投入品流通管理", "en_US": "Input Circulation Management"}', 'input', 6, 'input-circulation', NULL, 'ParentView', NULL, '1', '0', 'M', '0', '0', '', 'ri-truck-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('500501', '{"zh_CN": "OSE分发至联合会", "en_US": "Release Agri Input to Union"}', '5005', 1, 'input/input-circulation/ose-release', NULL, 'input/input-circulation/ose-release/index', NULL, '1', '0', 'C', '0', '0', 'input:circulation:ose:list', 'ri-share-forward-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('500502', '{"zh_CN": "联合会接收确认", "en_US": "Union Receive Confirmation"}', '5005', 2, 'input/input-circulation/union-receive', NULL, 'input/input-circulation/union-receive/index', NULL, '1', '0', 'C', '0', '0', 'input:circulation:union-receive:list', 'ri-checkbox-circle-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('500503', '{"zh_CN": "联合会分发至合作社", "en_US": "Union Distribution to Cooperative"}', '5005', 3, 'input/input-circulation/union-release', NULL, 'input/input-circulation/union-release/index', NULL, '1', '0', 'C', '0', '0', 'input:circulation:union-release:list', 'ri-send-plane-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('500504', '{"zh_CN": "合作社接收确认", "en_US": "Cooperative Receive Confirmation"}', '5005', 4, 'input/input-circulation/woreda-receive', NULL, 'input/input-circulation/woreda-receive/index', NULL, '1', '0', 'C', '0', '0', 'input:circulation:coop-receive:list', 'ri-check-double-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('500505', '{"zh_CN": "合作社分发至农民", "en_US": "Cooperative Distribution to Farmer"}', '5005', 5, 'input/input-circulation/farmer-release', NULL, 'input/input-circulation/farmer-release/index', NULL, '1', '0', 'C', '0', '0', 'input:circulation:farmer-release:list', 'ri-user-received-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('500506', '{"zh_CN": "农民接收列表", "en_US": "Farmer Receive List"}', '5005', 6, 'input/input-circulation/farmer-receive', NULL, 'input/input-circulation/farmer-receive/index', NULL, '1', '0', 'C', '0', '0', 'input:circulation:farmer-receive:list', 'ri-user-follow-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('5006', '{"zh_CN": "投入品分配额度管理", "en_US": "Input Allocation Quota Management"}', 'input', 7, 'quota-allocation', NULL, 'ParentView', NULL, '1', '0', 'M', '0', '0', '', 'ri-slice-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('500601', '{"zh_CN": "区级分配额度管理", "en_US": "Zone Allocation Quota Management"}', '5006', 1, 'input/allocation/zone', NULL, 'input/allocation/zone/index', NULL, '1', '0', 'C', '0', '0', 'input:quota:zone:list', 'ri-community-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('500602', '{"zh_CN": "镇级分配额度管理", "en_US": "Woreda Allocation Quota Management"}', '5006', 2, 'input/allocation/woreda', NULL, 'input/allocation/woreda/index', NULL, '1', '0', 'C', '0', '0', 'input:quota:woreda:list', 'ri-community-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('500603', '{"zh_CN": "农民分配管理", "en_US": "Farmer Allocation Management"}', '5006', 3, 'input/allocation/farmer', NULL, 'input/allocation/farmer/index', NULL, '1', '0', 'C', '0', '0', 'input:quota:farmer:list', 'ri-user-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('5007', '{"zh_CN": "库存管理", "en_US": "Inventory Management"}', 'input', 8, 'inventory-management', NULL, 'ParentView', NULL, '1', '0', 'M', '0', '0', '', 'ri-archive-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('500701', '{"zh_CN":"仓库管理","en_US":"Warehouse Management"}', '5007', 1, 'input/inventory/warehouse-manage', NULL, 'input/inventory/warehouse/index', NULL, '1', '0', 'C', '0', '0', 'input:inventory:warehouse:list', 'ri-home-3-line', 'admin', '2026-01-12 10:23:43', 'superAdmin', '2026-03-07 14:39:12', '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('500702', '{"zh_CN": "入库管理", "en_US": "Stock In Management"}', '5007', 2, 'input/inventory/stock-in', NULL, 'input/inventory/stock-in/index', NULL, '1', '0', 'C', '0', '0', 'input:inventory:stockin:list', 'ri-inbox-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('500703', '{"zh_CN": "出库管理", "en_US": "Stock Out Management"}', '5007', 3, 'input/inventory/stock-out', NULL, 'input/inventory/stock-out/index', NULL, '1', '0', 'C', '0', '0', 'input:inventory:stockout:list', 'ri-inbox-unarchive-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('500704', '{"zh_CN": "库存查询", "en_US": "Inventory Query"}', '5007', 4, 'input/inventory/stock', NULL, 'input/inventory/stock/index', NULL, '1', '0', 'C', '0', '0', 'input:inventory:query:list', 'ri-stack-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('5099', '{"zh_CN": "信息反馈", "en_US": "Information Feedback"}', 'input', 99, 'input/feedback', NULL, 'input/feedback/index', NULL, '1', '0', 'C', '0', '0', 'input:feedback:list', 'ri-feedback-line', 'admin', '2026-01-12 10:23:43', '', NULL, '', 'input_app');
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('67d29ade-e5a2-4b6a-8b71-5a51d36f7e1f', '{"zh_CN": "菜单管理", "en_US": "Menu Management"}', 'system', 3, 'system/menu', NULL, 'system/menu/index', NULL, '1', '0', 'C', '0', '0', 'system:menu:list', 'ri-menu-2-line', '', '2025-12-09 14:29:44', '', NULL, '', NULL);
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('945f543a60da40b89ecba953c32b5261', '{"en_US":"Dict detail","zh_CN":"字典详情"}', 'system', 0, 'system/dict-data/:id', NULL, '/system/dict-data', NULL, '1', '0', 'C', '1', '0', 'system/dict-data', '', 'superAdmin', '2025-12-11 16:38:58', 'superAdmin', '2025-12-11 17:46:33', '', NULL);
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('a74b2a48-03f8-476f-bb33-48bc6e0a8d73', '{"zh_CN":"用户管理","en_US":"User Management"}', 'system', 1, 'system/user', NULL, 'system/user/index', NULL, '1', '0', 'C', '0', '0', 'system:user:list', 'ri-user-settings-line', '', '2025-12-09 14:29:44', 'superAdmin', '2025-12-09 16:52:53', '', NULL);
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('b6af6b2a1cb2439480e93f8a514c8d4c', '{"en_US":"product management","zh_CN":"商品管理"}', '5007', 1, 'input/inventory/product-manage', NULL, 'input/inventory/product/index', NULL, '1', '0', 'C', '0', '0', 'input:inventory:product:list', '', 'superAdmin', '2026-03-07 14:40:16', 'superAdmin', '2026-03-07 14:59:59', '', NULL);
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('bcc091a2a0dc4a29992e8f4cbb069107', '{"en_US":"Breeding license data entry","zh_CN":"育种许可数据录入"}', '1000', 18, 'research/breeding-data/breeding-license', NULL, 'research/breeding-data/breeding-license/index', NULL, '1', '0', 'C', '0', '0', 'research:license:list', 'ri-file-list-line', 'superAdmin', '2026-01-30 11:51:27', 'superAdmin', '2026-01-30 14:21:15', '', NULL);
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('c479f54a-109f-41f6-9b08-b1ed881ffea8', '{"zh_CN": "部门管理", "en_US": "Department Management"}', 'system', 4, 'system/dept', NULL, 'system/dept/index', NULL, '1', '0', 'C', '0', '0', 'system:dept:list', 'ri-building-2-line', '', '2025-12-09 14:29:44', '', NULL, '', NULL);
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('d0e6d9594a474c379cd71b1c77feef3a', '{"zh_CN":"公告管理","en_US":"Notice Management"}', 'system', 6, 'system/notice', NULL, 'system/notice/index', NULL, '1', '0', 'C', '0', '0', 'system:notice:list', 'ri-notification-line', 'superAdmin', '2025-12-10 16:03:55', 'superAdmin', '2025-12-10 16:09:11', '', NULL);
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('d615221049624ec695c4f88655094f71', '{"zh_CN":"参数配置","en_US":"System Config"}', 'system', 7, 'system/config', NULL, 'system/config/index', NULL, '1', '0', 'C', '0', '0', 'system:config:list', 'ri-settings-3-line', 'superAdmin', '2025-12-10 16:04:51', 'superAdmin', '2025-12-10 16:08:41', '', NULL);
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('fac5f464-f68a-4ccb-a836-f0227e27debf', '{"zh_CN": "字典管理", "en_US": "Dictionary Management"}', 'system', 5, 'system/dict', NULL, 'system/dict/index', NULL, '1', '0', 'C', '0', '0', 'system:dict:list', 'ri-book-2-line', '', '2025-12-09 14:29:44', '', NULL, '', NULL);
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('input', '{"zh_CN": "农业投入品供应管理系统", "en_US": "Agricultural Input Supply Management System"}', '0', 300, 'input', NULL, 'Layout', NULL, '1', '0', 'M', '0', '0', '', 'ri-database-2-line', NULL, '2025-12-09 14:29:44', '', NULL, '', NULL);
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('research', '{"zh_CN": "研究与开发管理系统", "en_US": "Research & Development Management System"}', '0', 200, 'research', NULL, 'Layout', NULL, '1', '0', 'M', '0', '0', '', 'ri-flask-line', NULL, '2025-12-09 14:29:44', '', NULL, '', NULL);
INSERT INTO agriculture_igdp_admin.sys_menu
(MENU_ID, menu_name, PARENT_ID, ORDER_NUM, `PATH`, LINK, COMPONENT, QUERY, IS_FRAME, IS_CACHE, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK, APP_ID)
VALUES('system', '{"zh_CN": "系统管理", "en_US": "System Management"}', '0', 100, 'system', NULL, 'Layout', NULL, '1', '0', 'M', '0', '0', '', 'ri-settings-3-line', NULL, '2025-12-09 14:29:44', '', NULL, '', NULL);