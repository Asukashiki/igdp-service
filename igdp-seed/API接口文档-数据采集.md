# 种子育种数据采集模块 - API接口文档

## 概述

本文档描述种子育种数据采集模块的后端API接口,包括7个数据采集功能模块。

### 基础信息

- **基础路径**: `/seed`
- **请求方式**: 主要使用 POST 和 GET 方法
- **响应格式**: JSON
- **标准响应结构**:

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {}
}
```

---

## 1. 试验基础数据采集接口

### 1.1 查询试验基础数据列表

**接口地址**: `/seed/trial/base/list`

**请求方式**: POST

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| trialId | String | 否 | 试验ID |
| cropType | String | 否 | 作物类型 |
| varietyName | String | 否 | 品种名称(模糊查询) |

**请求示例**:

```json
{
  "cropType": "小麦",
  "varietyName": "冬麦"
}
```

**响应示例**:

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "trialId": "T2025001",
      "cropType": "小麦",
      "varietyName": "冬麦8号",
      "researchCenterId": "RC001",
      "programId": "P001",
      "subProgramId": "SP001",
      "thematicResearchAreaId": "TRA001",
      "region": "华北地区",
      "zone": "河北",
      "woreda": "石家庄",
      "kebele": "新华区",
      "agroEcologicalZone": "温带大陆性气候",
      "gpsLocation": "114.502,38.045",
      "startDate": "2025-03-15",
      "activityCode": "AC001",
      "kpiCode": "KPI001",
      "season": "春季",
      "createTime": "2025-11-26 10:00:00",
      "createBy": "admin"
    }
  ]
}
```

### 1.2 获取试验基础数据详情

**接口地址**: `/seed/trial/base/{trialId}`

**请求方式**: GET

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| trialId | String | 是 | 试验ID |

**响应示例**: 同查询列表单条数据格式

### 1.3 新增试验基础数据

**接口地址**: `/seed/trial/base`

**请求方式**: POST

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| cropType | String | 是 | 作物类型 |
| varietyName | String | 是 | 品种名称 |
| researchCenterId | String | 是 | 研究中心ID |
| programId | String | 是 | 程序ID |
| subProgramId | String | 是 | 子程序ID |
| thematicResearchAreaId | String | 是 | 主题研究领域ID |
| region | String | 是 | 地区 |
| zone | String | 是 | 区域 |
| woreda | String | 是 | 县 |
| kebele | String | 是 | 乡 |
| agroEcologicalZone | String | 否 | 农业生态区 |
| gpsLocation | String | 是 | GPS位置 |
| startDate | Date | 是 | 开始日期(格式: yyyy-MM-dd) |
| activityCode | String | 否 | 活动代码 |
| kpiCode | String | 否 | KPI代码 |
| season | String | 是 | 季节 |

**请求示例**:

```json
{
  "cropType": "小麦",
  "varietyName": "冬麦8号",
  "researchCenterId": "RC001",
  "programId": "P001",
  "subProgramId": "SP001",
  "thematicResearchAreaId": "TRA001",
  "region": "华北地区",
  "zone": "河北",
  "woreda": "石家庄",
  "kebele": "新华区",
  "agroEcologicalZone": "温带大陆性气候",
  "gpsLocation": "114.502,38.045",
  "startDate": "2025-03-15",
  "activityCode": "AC001",
  "kpiCode": "KPI001",
  "season": "春季"
}
```

**响应示例**:

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": 1
}
```

### 1.4 修改试验基础数据

**接口地址**: `/seed/trial/base/edit`

**请求方式**: POST

**请求参数**: 同新增接口,需包含 `trialId`

### 1.5 删除试验基础数据

**接口地址**: `/seed/trial/base/delete`

**请求方式**: POST

**请求参数**:

```json
["T2025001", "T2025002"]
```

---

## 2. 农民与地块属性数据采集接口

### 2.1 查询农民与地块数据列表

**接口地址**: `/seed/farmer/plot/list`

**请求方式**: POST

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| farmerName | String | 否 | 农民姓名(模糊查询) |

**响应数据字段**:

| 字段名 | 类型 | 说明 |
|--------|------|------|
| dataId | String | 数据ID |
| farmerName | String | 农民姓名 |
| gender | String | 性别 |
| youthCategory | String | 青年类别 |
| cooperativeMembership | String | 合作社成员资格 |
| plotSizeM2 | BigDecimal | 地块面积(平方米) |
| householdId | String | 家庭ID |
| contactPhone | String | 联系电话 |
| createTime | Date | 创建时间 |
| createBy | String | 创建人 |

### 2.2 新增农民与地块数据

**接口地址**: `/seed/farmer/plot`

**请求方式**: POST

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| farmerName | String | 是 | 农民姓名 |
| gender | String | 是 | 性别 |
| youthCategory | String | 是 | 青年类别 |
| cooperativeMembership | String | 是 | 合作社成员资格 |
| plotSizeM2 | BigDecimal | 是 | 地块面积(平方米) |
| householdId | String | 否 | 家庭ID |
| contactPhone | String | 是 | 联系电话 |

### 2.3 其他接口

- 获取详情: GET `/seed/farmer/plot/{dataId}`
- 修改: POST `/seed/farmer/plot/edit`
- 删除: POST `/seed/farmer/plot/delete`

---

## 3. 农事记录数据采集接口

### 3.1 查询农事记录列表

**接口地址**: `/seed/farming/record/list`

**请求方式**: POST

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| managementPractice | String | 否 | 管理措施 |

**响应数据字段**:

| 字段名 | 类型 | 说明 |
|--------|------|------|
| dataId | String | 数据ID |
| managementPractice | String | 管理措施 |
| fertilizerType | String | 肥料类型 |
| fertilizerRateKg | BigDecimal | 肥料施用量(公斤) |
| ureaRateKg | BigDecimal | 尿素施用量(公斤) |
| pesticideType | String | 农药类型 |
| irrigationType | String | 灌溉类型 |
| irrigationFrequency | Integer | 灌溉频率 |
| weedingDate | Date | 除草日期 |
| herbicideUsed | String | 除草剂使用 |
| seedSource | String | 种子来源 |

### 3.2 新增农事记录

**接口地址**: `/seed/farming/record`

**请求方式**: POST

**请求参数**: 参见响应数据字段(必填字段: managementPractice)

### 3.3 其他接口

- 获取详情: GET `/seed/farming/record/{dataId}`
- 修改: POST `/seed/farming/record/edit`
- 删除: POST `/seed/farming/record/delete`

---

## 4. 农艺性状数据采集接口

### 4.1 查询农艺性状数据列表

**接口地址**: `/seed/agronomic/trait/list`

**请求方式**: POST

**响应数据字段**:

| 字段名 | 类型 | 说明 |
|--------|------|------|
| dataId | String | 数据ID |
| plantHeightCm | BigDecimal | 植物高度(CM) |
| tillerCount | Integer | 分蘖数 |
| spikeLengthCm | BigDecimal | 穗长(CM) |
| daysToEmergence | Integer | 天数至出苗期 |
| daysToTillering | Integer | 天数至分蘖期 |
| daysToHeading | Integer | 天数至抽穗期 |
| daysToFlowering | Integer | 天数至开花期 |
| daysToGrainFilling | Integer | 天数至灌浆期 |
| daysToMaturity | Integer | 天数至成熟期 |
| lodgingScore | Integer | 倒伏评分 |
| biomassWeightKg | BigDecimal | 生物量重量(KG) |
| spikeDensity | BigDecimal | 穗密度 |
| grainWeightPerSpike | BigDecimal | 每穗粒重 |
| diseaseScore | String | 疾病评分(JSON格式) |
| stressIndicators | String | 压力指标(JSON格式) |
| pestObservation | String | 害虫观察 |
| photoEvidence | String | 照片证据 |

### 4.2 新增农艺性状数据

**接口地址**: `/seed/agronomic/trait`

**请求方式**: POST

**请求参数**: 参见响应数据字段(除dataId外均为必填,photoEvidence可选)

### 4.3 其他接口

- 获取详情: GET `/seed/agronomic/trait/{dataId}`
- 修改: POST `/seed/agronomic/trait/edit`
- 删除: POST `/seed/agronomic/trait/delete`

---

## 5. 环境与土壤属性数据采集接口

### 5.1 查询环境与土壤数据列表

**接口地址**: `/seed/environment/soil/list`

**请求方式**: POST

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| topography | String | 否 | 地貌 |
| waterSource | String | 否 | 水源 |

**响应数据字段**:

| 字段名 | 类型 | 说明 |
|--------|------|------|
| dataId | String | 数据ID |
| soilPh | BigDecimal | 土壤pH值 |
| soilEc | BigDecimal | 土壤电导率 |
| soilNitrogenPercent | BigDecimal | 土壤氮含量(百分比) |
| soilPhosphorusPpm | BigDecimal | 土壤磷含量(PPM) |
| soilPotassiumPpm | BigDecimal | 土壤钾含量(PPM) |
| previousCrop | String | 前茬作物 |
| waterSource | String | 水源 |
| topography | String | 地貌 |
| slopePercent | BigDecimal | 坡度(百分比) |
| soilMoisturePercent | BigDecimal | 土壤湿度(百分比) |
| soilTemperatureC | BigDecimal | 土壤温度(摄氏度) |
| rainfallMm | BigDecimal | 降雨量(MM) |
| airTemperatureC | BigDecimal | 空气温度(摄氏度) |
| humidityPercent | BigDecimal | 湿度(百分比) |
| windSpeedMs | BigDecimal | 风速(M/S) |
| solarRadiationWm2 | BigDecimal | 太阳辐射(W/m2) |
| timestamp | Date | 时间戳 |

### 5.2 新增环境与土壤数据

**接口地址**: `/seed/environment/soil`

**请求方式**: POST

**请求参数**: 参见响应数据字段

**必填字段**: soilPh, soilEc, soilNitrogenPercent, soilPhosphorusPpm, soilPotassiumPpm, waterSource, topography, soilMoisturePercent, soilTemperatureC, humidityPercent, windSpeedMs, solarRadiationWm2

### 5.3 其他接口

- 获取详情: GET `/seed/environment/soil/{dataId}`
- 修改: POST `/seed/environment/soil/edit`
- 删除: POST `/seed/environment/soil/delete`

---

## 6. 品种评估数据采集接口

### 6.1 查询品种评估数据列表

**接口地址**: `/seed/variety/evaluation/list`

**请求方式**: POST

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| plotId | String | 否 | 地块ID |

**响应数据字段**:

| 字段名 | 类型 | 说明 |
|--------|------|------|
| dataId | String | 数据ID |
| plotId | String | 地块ID |
| plotAreaM2 | BigDecimal | 地块面积(平方米) |
| grainWeightKg | BigDecimal | 籽粒重量(KG) |
| yieldQtPerHa | BigDecimal | 产量(公担/公顷) |
| moistureContent | BigDecimal | 含水量(百分比) |

### 6.2 新增品种评估数据

**接口地址**: `/seed/variety/evaluation`

**请求方式**: POST

**请求参数**: 参见响应数据字段(除dataId外均为必填)

### 6.3 其他接口

- 获取详情: GET `/seed/variety/evaluation/{dataId}`
- 修改: POST `/seed/variety/evaluation/edit`
- 删除: POST `/seed/variety/evaluation/delete`

---

## 7. 实验室测试数据采集接口

### 7.1 查询实验室测试数据列表

**接口地址**: `/seed/laboratory/test/list`

**请求方式**: POST

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sampleId | String | 否 | 样本ID |

**响应数据字段**:

| 字段名 | 类型 | 说明 |
|--------|------|------|
| dataId | String | 数据ID |
| sampleId | String | 样本ID |
| sampleCondition | String | 样本状态 |
| germinationRate | BigDecimal | 发芽率(百分比) |
| purityPercent | BigDecimal | 纯度(百分比) |
| moistureContentPercent | BigDecimal | 含水量(百分比) |
| proteinPercent | BigDecimal | 蛋白质(百分比) |
| toxinLevelPpm | BigDecimal | 毒素水平(PPM) |
| seedHealthFindings | String | 种子健康发现 |
| traceabilityLink | String | 链路责任 |
| labReportFile | String | 实验室报告文件 |

### 7.2 新增实验室测试数据

**接口地址**: `/seed/laboratory/test`

**请求方式**: POST

**请求参数**: 参见响应数据字段

**必填字段**: sampleId, sampleCondition, germinationRate, purityPercent, moistureContentPercent, proteinPercent, seedHealthFindings, traceabilityLink

### 7.3 其他接口

- 获取详情: GET `/seed/laboratory/test/{dataId}`
- 修改: POST `/seed/laboratory/test/edit`
- 删除: POST `/seed/laboratory/test/delete`

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 注意事项

1. 所有接口需要携带有效的认证token
2. 日期格式统一为: `yyyy-MM-dd` 或 `yyyy-MM-dd HH:mm:ss`
3. 删除操作为逻辑删除,设置 `del_flag=2`
4. ID字段均为String类型,采用UUID生成策略
5. 所有查询列表接口按创建时间倒序排列
6. BigDecimal类型字段注意精度处理
7. JSON格式字段(如diseaseScore)需要符合JSON规范
8. 文件路径字段(如photoEvidence)存储相对路径或完整URL
