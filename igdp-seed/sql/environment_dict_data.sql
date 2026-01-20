-- 环境参数字典数据
INSERT INTO sys_dict_type (dict_id, dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES (uuid(), '环境参数', 'environment_parameter', '0', 'admin', sysdate(), '', null, '环境/IoT数据参数定义');

INSERT INTO sys_dict_data (dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES
(uuid(), 1, '日降雨量', 'RAIN_DAILY', 'environment_parameter', '', 'default', 'N', '0', 'admin', sysdate(), '', null, '单位: mm'),
(uuid(), 2, '月降雨量', 'RAIN_MONTHLY', 'environment_parameter', '', 'default', 'N', '0', 'admin', sysdate(), '', null, '单位: mm'),
(uuid(), 3, '最高温度', 'TMAX', 'environment_parameter', '', 'default', 'N', '0', 'admin', sysdate(), '', null, '单位: °C'),
(uuid(), 4, '最低温度', 'TMIN', 'environment_parameter', '', 'default', 'N', '0', 'admin', sysdate(), '', null, '单位: °C'),
(uuid(), 5, '平均温度', 'TEMP_AVG', 'environment_parameter', '', 'default', 'N', '0', 'admin', sysdate(), '', null, '单位: °C'),
(uuid(), 6, '相对湿度', 'RH_AVG', 'environment_parameter', '', 'default', 'N', '0', 'admin', sysdate(), '', null, '单位: %'),
(uuid(), 7, '光照强度', 'SOLAR_RAD', 'environment_parameter', '', 'default', 'N', '0', 'admin', sysdate(), '', null, '单位: MJ/m2'),
(uuid(), 8, '土壤湿度', 'SOIL_MOISTURE', 'environment_parameter', '', 'default', 'N', '0', 'admin', sysdate(), '', null, '单位: %');
