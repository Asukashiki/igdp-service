-- 埃塞俄比亚奥罗米亚州研究中心样本数据
INSERT INTO `location_master` (`location_id`, `location_name`, `region`, `zone`, `woneda`, `latitude`, `longitude`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
('LOC001', 'Bishoftu Agricultural Research Center', 'Oromia', 'East Shewa', 'Bishoftu', 8.7833, 38.9833, 'admin', NOW(), 'admin', NOW(), 'Main agricultural research center in Bishoftu'),
('LOC002', 'Holeta Agricultural Research Center', 'Oromia', 'West Shewa', 'Holeta', 9.0833, 38.5000, 'admin', NOW(), 'admin', NOW(), 'Agricultural research center specializing in crop improvement'),
('LOC003', 'Sinana Agricultural Research Center', 'Oromia', 'Bale', 'Sinana', 7.0833, 40.2833, 'admin', NOW(), 'admin', NOW(), 'Research center for highland crops and livestock'),
('LOC004', 'Adami Tulu Agricultural Research Center', 'Oromia', 'East Shewa', 'Adami Tulu', 7.8667, 38.7167, 'admin', NOW(), 'admin', NOW(), 'Research center for semi-arid agriculture'),
('LOC005', 'Melkassa Agricultural Research Center', 'Oromia', 'East Shewa', 'Melkassa', 8.4000, 39.3500, 'admin', NOW(), 'admin', NOW(), 'National agricultural research center for dryland farming'),
('LOC006', 'Bako Agricultural Research Center', 'Oromia', 'West Shewa', 'Bako', 9.1500, 37.0667, 'admin', NOW(), 'admin', NOW(), 'Research center for maize and other cereals'),
('LOC007', 'Jimma Agricultural Research Center', 'Oromia', 'Jimma', 'Jimma', 7.6667, 36.8333, 'admin', NOW(), 'admin', NOW(), 'Research center for coffee and forest agriculture'),
('LOC008', 'Haromaya Agricultural Research Center', 'Oromia', 'East Hararghe', 'Haromaya', 9.0833, 42.1667, 'admin', NOW(), 'admin', NOW(), 'Research center for highland crops and horticulture');

-- 查询奥罗米亚州的数据
SELECT * FROM `location_master` WHERE `region` = 'Oromia' ORDER BY `location_name`;