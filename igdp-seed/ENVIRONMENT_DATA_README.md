# 环境/IoT数据模块说明

## 模块概述

**Color Layer**: 🟣 Purple - Environment Data  
**表名**: `environment_data`  
**模块路径**: `igdp-seed`

本模块用于管理环境和IoT传感器数据，支持以下三种数据录入方式：
1. **IoT API集成** - 自动从IoT系统接收数据
2. **CSV导入** - 批量导入历史数据
3. **手动录入** - 通过表单手动录入数据

## 数据结构

### 核心字段

| 字段名 | 类型 | 说明 | 示例 |
|--------|------|------|------|
| env_record_id | varchar(64) | 环境数据记录ID(主键) | 9001 |
| trial_id | varchar(64) | 试验ID | WHT-TR-ARSI-2025-01 |
| batch_id | varchar(64) | 育种批次ID | BRD-WHT-2025-001 |
| plot_id | varchar(64) | 地块ID(可选) | ARSI-R1-P01 |
| station_id | varchar(64) | 气象站ID | AWS-ARSI |
| timestamp | datetime | 数据采集时间 | 2025-08-10 12:00:00 |
| parameter_code | varchar(50) | 参数代码 | RAIN_DAILY, TMAX, TMIN |
| value | decimal(18,6) | 数值 | 8.5, 32.5, 18.2 |
| unit | varchar(20) | 单位 | mm, °C, % |
| data_source | varchar(50) | 数据来源 | IOT_SYSTEM, MANUAL, CSV_IMPORT |

## API接口

### 基础CRUD接口

#### 1. 分页查询环境数据列表
```
GET /seed/environment/list
参数: 
  - trialId: 试验ID
  - batchId: 批次ID
  - plotId: 地块ID
  - stationId: 气象站ID
  - parameterCode: 参数代码
  - dataSource: 数据来源
  - queryStartTime: 查询起始时间
  - queryEndTime: 查询结束时间
  - pageNum: 页码
  - pageSize: 每页数量
```

#### 2. 获取环境数据详情
```
GET /seed/environment/getInfo?envRecordId={id}
```

#### 3. 新增环境数据(手动录入)
```
POST /seed/environment/add
Body: {
  "trialId": "WHT-TR-ARSI-2025-01",
  "batchId": "BRD-WHT-2025-001",
  "plotId": "ARSI-R1-P01",
  "stationId": "AWS-ARSI",
  "timestamp": "2025-08-10 12:00:00",
  "parameterCode": "RAIN_DAILY",
  "value": 8.5,
  "unit": "mm"
}
```

#### 4. 修改环境数据
```
POST /seed/environment/edit
Body: {
  "envRecordId": "9001",
  ...其他字段
}
```

#### 5. 删除环境数据
```
GET /seed/environment/remove?envRecordIds=9001,9002,9003
```

### 批量导入接口

#### 6. IoT API批量导入
```
POST /seed/environment/batchAddIoT
Body: [
  {
    "trialId": "WHT-TR-ARSI-2025-01",
    "batchId": "BRD-WHT-2025-001",
    "stationId": "AWS-ARSI",
    "timestamp": "2025-08-10 12:00:00",
    "parameterCode": "RAIN_DAILY",
    "value": 8.5,
    "unit": "mm"
  },
  ...更多数据
]
```

#### 7. CSV批量导入
```
POST /seed/environment/batchImport
Body: [
  {
    "trialId": "WHT-TR-ARSI-2025-01",
    ...
  }
]
```

#### 8. 数据验证接口
```
POST /seed/environment/validate
Body: {
  "trialId": "WHT-TR-ARSI-2025-01",
  ...
}
返回: 验证结果消息
```

## 数据验证规则

系统会自动对环境数据进行以下验证：

### 1. 必填字段验证
- trial_id: 试验ID不能为空
- batch_id: 批次ID不能为空
- station_id: 气象站ID不能为空
- timestamp: 时间戳不能为空
- parameter_code: 参数代码不能为空
- value: 数值不能为空

### 2. 参数代码字典验证
- 参数代码必须存在于系统字典 `environment_parameter` 中

### 3. 数据合理性验证

#### 温度参数 (TEMP, TMAX, TMIN)
- 范围: -100°C ~ 100°C

#### 湿度参数 (HUMIDITY, RH)
- 范围: 0% ~ 100%

#### 降雨量参数 (RAIN_DAILY, RAIN_MONTHLY)
- 范围: 0mm ~ 1000mm
- 不允许负数

## 支持的环境参数

| 参数代码 | 参数名称 | 单位 | 说明 |
|----------|----------|------|------|
| RAIN_DAILY | 日降雨量 | mm | 每日降雨总量 |
| RAIN_MONTHLY | 月降雨量 | mm | 每月降雨总量 |
| TMAX | 最高温度 | °C | 日最高气温 |
| TMIN | 最低温度 | °C | 日最低气温 |
| TEMP_AVG | 平均温度 | °C | 日平均气温 |
| RH_AVG | 相对湿度 | % | 平均相对湿度 |
| SOLAR_RAD | 光照强度 | MJ/m² | 太阳辐射强度 |
| SOIL_MOISTURE | 土壤湿度 | % | 土壤含水量 |

## 代码结构

```
igdp-seed/
├── src/main/java/com/inspur/seed/
│   ├── domain/
│   │   └── EnvironmentData.java          # 实体类
│   ├── mapper/
│   │   └── EnvironmentDataMapper.java    # Mapper接口
│   ├── service/
│   │   ├── IEnvironmentDataService.java  # Service接口
│   │   └── impl/
│   │       └── EnvironmentDataServiceImpl.java  # Service实现
│   └── controller/
│       └── EnvironmentDataController.java # Controller
├── src/main/resources/mapper/seed/
│   └── EnvironmentDataMapper.xml          # MyBatis映射文件
└── sql/
    ├── environment_data.sql               # 建表SQL
    └── environment_dict_data.sql          # 字典数据SQL
```

## 使用示例

### 示例1: IoT系统推送数据

```java
// IoT系统调用接口推送数据
POST /seed/environment/batchAddIoT

[
  {
    "trialId": "WHT-TR-ARSI-2025-01",
    "batchId": "BRD-WHT-2025-001",
    "plotId": "ARSI-R1-P01",
    "stationId": "AWS-ARSI",
    "timestamp": "2025-08-10 12:00:00",
    "parameterCode": "RAIN_DAILY",
    "value": 8.5,
    "unit": "mm"
  },
  {
    "trialId": "WHT-TR-ARSI-2025-01",
    "batchId": "BRD-WHT-2025-001",
    "plotId": "ARSI-R1-P01",
    "stationId": "AWS-ARSI",
    "timestamp": "2025-08-10 14:00:00",
    "parameterCode": "TMAX",
    "value": 32.5,
    "unit": "°C"
  }
]
```

### 示例2: 手动录入单条数据

```java
POST /seed/environment/add

{
  "trialId": "WHT-TR-ARSI-2025-01",
  "batchId": "BRD-WHT-2025-001",
  "plotId": "ARSI-R1-P01",
  "stationId": "AWS-ARSI",
  "timestamp": "2025-08-10 12:00:00",
  "parameterCode": "RAIN_DAILY",
  "value": 8.5,
  "unit": "mm",
  "remark": "人工观测"
}
```

### 示例3: 查询特定时间段的温度数据

```java
GET /seed/environment/list?parameterCode=TMAX&queryStartTime=2025-08-01&queryEndTime=2025-08-31
```

## 数据库视图

系统提供了汇总视图 `v_environment_data_summary`，自动关联相关表数据：

```sql
SELECT * FROM v_environment_data_summary
WHERE trial_id = 'WHT-TR-ARSI-2025-01'
  AND parameter_code = 'RAIN_DAILY'
  AND timestamp BETWEEN '2025-08-01' AND '2025-08-31'
ORDER BY timestamp DESC;
```

视图包含以下额外字段：
- trial_name: 试验名称
- batch_name: 批次名称
- plot_field_name: 地块名称
- parameter_name: 参数名称（从字典翻译）

## 注意事项

1. **数据来源标识**: 不同来源的数据会自动标记 `data_source` 字段
   - IOT_SYSTEM: IoT系统自动上传
   - CSV_IMPORT: CSV文件导入
   - MANUAL: 手动录入

2. **逻辑删除**: 删除操作为逻辑删除，`del_flag` 设置为 '2'

3. **批量导入**: 批量导入会进行完整的数据验证，任何一条数据验证失败都会导致整批数据导入失败

4. **时间格式**: timestamp 字段格式为 `yyyy-MM-dd HH:mm:ss`

5. **数值精度**: value 字段支持 18 位总长度，6 位小数精度

## 扩展建议

如需添加新的环境参数：
1. 在 `sys_dict_data` 表添加字典项，类型为 `environment_parameter`
2. 在 `EnvironmentDataServiceImpl.validateEnvironmentData()` 方法中添加相应的验证规则

