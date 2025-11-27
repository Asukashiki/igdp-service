-- 农田管理系统初始化数据

-- 插入默认管理员用户
INSERT INTO `user` (`USER_ID`, `ACCOUNT`, `PASSWORD`, `USER_NAME`, `ID_CARD`, `GENDER`, `PHONE`, `EMAIL`, `AD_CODE`, `REG_DATE`, `STATUS`) VALUES
(1, 'admin', '$2a$10$xyzXYZ123456', '系统管理员', NULL, '男', '13800138000', 'admin@example.com', NULL, NOW(), 1);

-- 插入测试农民用户
INSERT INTO `user` (`USER_ID`, `ACCOUNT`, `PASSWORD`, `USER_NAME`, `ID_CARD`, `GENDER`, `PHONE`, `EMAIL`, `AD_CODE`, `REG_DATE`, `STATUS`) VALUES
(2, 'farmer1', '$2a$10$xyzXYZ123456', '张三', '110101199001011234', '男', '13800138001', 'zhangsan@example.com', '110101', NOW(), 1),
(3, 'farmer2', '$2a$10$xyzXYZ123456', '李四', '110101199001011235', '女', '13800138002', 'lisi@example.com', '110101', NOW(), 1);

-- 插入农民认证信息
INSERT INTO `farmer_certification` (`CERT_ID`, `USER_ID`, `REAL_NAME`, `ID_CARD`, `AD_CODE`, `FARM_TYPE`, `CERT_DOC_PATH`, `DETAIL_ADDRESS`, `APPLY_TIME`, `STATUS`, `APPROVER_ID`, `APPROVE_TIME`, `REJECT_REASON`) VALUES
(1, 2, '张三', '110101199001011234', '110101', '小麦种植', '/certs/zhangsan_idcard.jpg', '北京市朝阳区某某街道123号', NOW(), 2, 1, NOW(), NULL),
(2, 3, '李四', '110101199001011235', '110101', '玉米种植', '/certs/lisi_idcard.jpg', '北京市朝阳区某某街道124号', NOW(), 2, 1, NOW(), NULL);

-- 插入土地信息
INSERT INTO `land_info` (`LAND_ID`, `LAND_NAME`, `OWNER_TYPE`, `AD_CODE`, `DETAIL_ADDRESS`, `AREA_SIZE`, `LAND_TYPE`, `CURRENT_STATUS`, `LATITUDE`, `LONGITUDE`, `FARMER_USER_ID`, `CREATE_BY`, `CREATE_TIME`, `UPDATE_TIME`, `REMARK`) VALUES
(1, '一号地块', '个人', '110101', '北京市朝阳区某某街道123号', 10.50, '耕地', '正常', 39.904211, 116.407395, 2, 2, NOW(), NOW(), '优质耕地'),
(2, '二号地块', '个人', '110101', '北京市朝阳区某某街道124号', 15.75, '耕地', '正常', 39.904311, 116.407495, 3, 3, NOW(), NOW(), '高标准农田');