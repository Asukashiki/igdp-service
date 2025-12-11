# 环境/IoT数据模块 - 前端API接口文档

## 概述

本文档为环境/IoT数据模块的前端API接口文档，包含所有接口的详细说明、请求/响应格式、参数说明及使用示例。

**模块基础路径**: `/seed/environment`

**数据来源类型**:
- `IOT_SYSTEM`: IoT系统自动推送
- `CSV_IMPORT`: CSV文件批量导入
- `MANUAL`: 手动表单录入

---

## 1. 分页查询环境数据列表

### 接口信息
- **URL**: `/seed/environment/list`
- **方法**: `GET`
- **描述**: 分页查询环境数据列表，支持多条件筛选

### 请求参数

| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| pageNum | int | 否 | 页码（默认1） | 1 |
| pageSize | int | 否 | 每页条数（默认10） | 10 |
| trialId | String | 否 | 试验ID | WHT-TR-ARSI-2025-01 |
| batchId | String | 否 | 育种批次ID | BRD-WHT-2025-001 |
| plotId | String | 否 | 地块ID | ARSI-R1-P01 |
| stationId | String | 否 | 气象站ID | AWS-ARSI |
| parameterCode | String | 否 | 参数代码 | RAIN_DAILY |
| dataSource | String | 否 | 数据来源 | IOT_SYSTEM |
| queryStartTime | String | 否 | 查询起始时间 | 2025-01-01 00:00:00 |
| queryEndTime | String | 否 | 查询结束时间 | 2025-12-31 23:59:59 |

### 请求示例

```javascript
// GET请求示例
axios.get('/seed/environment/list', {
  params: {
    pageNum: 1,
    pageSize: 10,
    batchId: 'BRD-WHT-2025-001',
    parameterCode: 'RAIN_DAILY',
    queryStartTime: '2025-08-01 00:00:00',
    queryEndTime: '2025-08-31 23:59:59'
  }
})
```

### 响应格式

```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 156,
  "rows": [
    {
      "envRecordId": "9001",
      "trialId": "WHT-TR-ARSI-2025-01",
      "trialName": "小麦抗旱性试验",
      "batchId": "BRD-WHT-2025-001",
      "batchName": "2025年春小麦育种批次",
      "plotId": "ARSI-R1-P01",
      "plotName": "ARSI站点R1区P01地块",
      "stationId": "AWS-ARSI",
      "stationName": "ARSI自动气象站",
      "timestamp": "2025-08-10 12:00:00",
      "parameterCode": "RAIN_DAILY",
      "parameterName": "日降雨量",
      "value": 8.5,
      "unit": "mm",
      "dataSource": "IOT_SYSTEM",
      "createTime": "2025-08-10 12:05:00",
      "createBy": "admin"
    }
  ]
}
```

### 响应字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应码（200成功，500失败） |
| msg | String | 响应消息 |
| total | int | 总记录数 |
| rows | Array | 数据列表 |
| envRecordId | String | 环境数据记录ID |
| trialId | String | 试验ID |
| trialName | String | 试验名称 |
| batchId | String | 育种批次ID |
| batchName | String | 批次名称 |
| plotId | String | 地块ID |
| plotName | String | 地块名称 |
| stationId | String | 气象站ID |
| stationName | String | 气象站名称 |
| timestamp | String | 数据采集时间 |
| parameterCode | String | 参数代码 |
| parameterName | String | 参数名称 |
| value | Number | 数值 |
| unit | String | 单位 |
| dataSource | String | 数据来源 |

---

## 2. 获取环境数据详情

### 接口信息
- **URL**: `/seed/environment/getInfo`
- **方法**: `GET`
- **描述**: 根据环境数据记录ID获取详细信息

### 请求参数

| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| envRecordId | String | 是 | 环境数据记录ID | 9001 |

### 请求示例

```javascript
axios.get('/seed/environment/getInfo', {
  params: {
    envRecordId: '9001'
  }
})
```

### 响应格式

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "envRecordId": "9001",
    "trialId": "WHT-TR-ARSI-2025-01",
    "trialName": "小麦抗旱性试验",
    "batchId": "BRD-WHT-2025-001",
    "batchName": "2025年春小麦育种批次",
    "plotId": "ARSI-R1-P01",
    "stationId": "AWS-ARSI",
    "timestamp": "2025-08-10 12:00:00",
    "parameterCode": "RAIN_DAILY",
    "parameterName": "日降雨量",
    "value": 8.5,
    "unit": "mm",
    "dataSource": "IOT_SYSTEM",
    "createTime": "2025-08-10 12:05:00",
    "createBy": "admin"
  }
}
```

---

## 3. 新增环境数据（手动录入）

### 接口信息
- **URL**: `/seed/environment/add`
- **方法**: `POST`
- **描述**: 手动新增单条环境数据，数据来源自动设置为MANUAL

### 请求参数

| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| trialId | String | 是 | 试验ID | WHT-TR-ARSI-2025-01 |
| batchId | String | 是 | 育种批次ID | BRD-WHT-2025-001 |
| plotId | String | 否 | 地块ID | ARSI-R1-P01 |
| stationId | String | 是 | 气象站ID | AWS-ARSI |
| timestamp | String | 是 | 数据采集时间 | 2025-08-10 12:00:00 |
| parameterCode | String | 是 | 参数代码 | RAIN_DAILY |
| value | Number | 是 | 数值 | 8.5 |
| unit | String | 否 | 单位 | mm |
| remark | String | 否 | 备注 | 人工测量 |

### 请求示例

```javascript
axios.post('/seed/environment/add', {
  trialId: 'WHT-TR-ARSI-2025-01',
  batchId: 'BRD-WHT-2025-001',
  plotId: 'ARSI-R1-P01',
  stationId: 'AWS-ARSI',
  timestamp: '2025-08-10 12:00:00',
  parameterCode: 'RAIN_DAILY',
  value: 8.5,
  unit: 'mm',
  remark: '人工测量'
})
```

### 响应格式

```json
{
  "code": 200,
  "msg": "新增成功",
  "data": "a1b2c3d4e5f6"
}
```

### 数据验证规则

1. **必填字段验证**: trialId、batchId、stationId、timestamp、parameterCode、value不能为空
2. **参数代码验证**: parameterCode必须存在于参数字典中
3. **数据合理性验证**:
   - 温度范围: -100°C ~ 100°C
   - 湿度范围: 0% ~ 100%
   - 降雨量范围: 0mm ~ 1000mm
   - 降雨量不能为负数

---

## 4. 批量新增环境数据（IoT API集成）

### 接口信息
- **URL**: `/seed/environment/batchAddIoT`
- **方法**: `POST`
- **描述**: IoT系统批量推送数据，数据来源自动设置为IOT_SYSTEM

### 请求参数

请求体为数组，每个元素包含以下字段：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| trialId | String | 是 | 试验ID |
| batchId | String | 是 | 育种批次ID |
| plotId | String | 否 | 地块ID |
| stationId | String | 是 | 气象站ID |
| timestamp | String | 是 | 数据采集时间 |
| parameterCode | String | 是 | 参数代码 |
| value | Number | 是 | 数值 |
| unit | String | 否 | 单位 |

### 请求示例

```javascript
axios.post('/seed/environment/batchAddIoT', [
  {
    trialId: 'WHT-TR-ARSI-2025-01',
    batchId: 'BRD-WHT-2025-001',
    plotId: 'ARSI-R1-P01',
    stationId: 'AWS-ARSI',
    timestamp: '2025-08-10 12:00:00',
    parameterCode: 'RAIN_DAILY',
    value: 8.5,
    unit: 'mm'
  },
  {
    trialId: 'WHT-TR-ARSI-2025-01',
    batchId: 'BRD-WHT-2025-001',
    plotId: 'ARSI-R1-P01',
    stationId: 'AWS-ARSI',
    timestamp: '2025-08-10 14:00:00',
    parameterCode: 'TMAX',
    value: 32.5,
    unit: '°C'
  }
])
```

### 响应格式

```json
{
  "code": 200,
  "msg": "成功导入2条IoT数据"
}
```

### 错误响应示例

```json
{
  "code": 500,
  "msg": "导入失败: 数据验证失败:\n第1条数据: 试验ID不能为空\n第3条数据: 温度值超出合理范围(-100~100°C)"
}
```

---

## 5. 批量新增环境数据（CSV导入）

### 接口信息
- **URL**: `/seed/environment/batchImport`
- **方法**: `POST`
- **描述**: CSV文件批量导入数据，数据来源自动设置为CSV_IMPORT

### 请求参数

同批量新增IoT数据接口，请求体为数组

### 请求示例

```javascript
// 通常由文件上传组件解析CSV后调用
axios.post('/seed/environment/batchImport', csvDataArray)
```

### 响应格式

```json
{
  "code": 200,
  "msg": "成功导入150条数据"
}
```

---

## 6. 修改环境数据

### 接口信息
- **URL**: `/seed/environment/edit`
- **方法**: `POST`
- **描述**: 修改已有环境数据

### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| envRecordId | String | 是 | 环境数据记录ID |
| trialId | String | 是 | 试验ID |
| batchId | String | 是 | 育种批次ID |
| plotId | String | 否 | 地块ID |
| stationId | String | 是 | 气象站ID |
| timestamp | String | 是 | 数据采集时间 |
| parameterCode | String | 是 | 参数代码 |
| value | Number | 是 | 数值 |
| unit | String | 否 | 单位 |
| remark | String | 否 | 备注 |

### 请求示例

```javascript
axios.post('/seed/environment/edit', {
  envRecordId: '9001',
  trialId: 'WHT-TR-ARSI-2025-01',
  batchId: 'BRD-WHT-2025-001',
  plotId: 'ARSI-R1-P01',
  stationId: 'AWS-ARSI',
  timestamp: '2025-08-10 12:00:00',
  parameterCode: 'RAIN_DAILY',
  value: 9.2,
  unit: 'mm',
  remark: '修正后的数据'
})
```

### 响应格式

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

---

## 7. 删除环境数据

### 接口信息
- **URL**: `/seed/environment/remove`
- **方法**: `GET`
- **描述**: 逻辑删除环境数据（支持批量删除）

### 请求参数

| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| envRecordIds | String | 是 | 环境数据记录ID，多个用逗号分隔 | 9001,9002,9003 |

### 请求示例

```javascript
// 单个删除
axios.get('/seed/environment/remove', {
  params: {
    envRecordIds: '9001'
  }
})

// 批量删除
axios.get('/seed/environment/remove', {
  params: {
    envRecordIds: '9001,9002,9003'
  }
})
```

### 响应格式

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

---

## 8. 验证环境数据

### 接口信息
- **URL**: `/seed/environment/validate`
- **方法**: `POST`
- **描述**: 前端提交前验证数据有效性

### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| trialId | String | 是 | 试验ID |
| batchId | String | 是 | 育种批次ID |
| stationId | String | 是 | 气象站ID |
| timestamp | String | 是 | 数据采集时间 |
| parameterCode | String | 是 | 参数代码 |
| value | Number | 是 | 数值 |

### 请求示例

```javascript
axios.post('/seed/environment/validate', {
  trialId: 'WHT-TR-ARSI-2025-01',
  batchId: 'BRD-WHT-2025-001',
  stationId: 'AWS-ARSI',
  timestamp: '2025-08-10 12:00:00',
  parameterCode: 'RAIN_DAILY',
  value: 8.5
})
```

### 响应格式

成功：
```json
{
  "code": 200,
  "msg": "验证通过"
}
```

失败：
```json
{
  "code": 500,
  "msg": "温度值超出合理范围(-100~100°C)"
}
```

---

## 数据字典

### 环境参数代码 (parameter_code)

| 参数代码 | 参数名称 | 单位 | 说明 |
|---------|---------|------|------|
| RAIN_DAILY | 日降雨量 | mm | 日降雨量 |
| RAIN_MONTHLY | 月降雨量 | mm | 月降雨量 |
| TMAX | 最高温度 | °C | 日最高温度 |
| TMIN | 最低温度 | °C | 日最低温度 |
| TEMP_AVG | 平均温度 | °C | 日平均温度 |
| RH_AVG | 相对湿度 | % | 平均相对湿度 |
| SOLAR_RAD | 光照强度 | MJ/m2 | 太阳辐射强度 |
| SOIL_MOISTURE | 土壤湿度 | % | 土壤含水量 |

### 数据来源 (data_source)

| 代码 | 说明 |
|------|------|
| IOT_SYSTEM | IoT系统自动推送 |
| CSV_IMPORT | CSV文件批量导入 |
| MANUAL | 手动表单录入 |

---

## 验证规则详解

### 1. 必填字段验证
- trialId: 试验ID不能为空
- batchId: 批次ID不能为空
- stationId: 气象站ID不能为空
- timestamp: 时间戳不能为空
- parameterCode: 参数代码不能为空
- value: 数值不能为空

### 2. 参数代码字典验证
- parameterCode必须在sys_dict_data表中存在（dict_type='environment_parameter'）
- 如果参数代码不存在，返回错误："参数代码[XXX]不存在于参数字典中"

### 3. 数据合理性验证

#### 温度参数（TEMP、TMAX、TMIN）
- 范围: -100°C ~ 100°C
- 超出范围错误: "温度值超出合理范围(-100~100°C)"

#### 湿度参数（HUMIDITY、RH）
- 范围: 0% ~ 100%
- 超出范围错误: "湿度值超出合理范围(0~100%)"

#### 降雨量参数（RAIN）
- 范围: 0mm ~ 1000mm
- 不能为负数
- 超出范围错误: "降雨量值超出合理范围(0~1000mm)"
- 负数错误: "降雨量不能为负数"

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 请求成功 |
| 500 | 服务器错误 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 无权限 |
| 404 | 资源不存在 |

---

## 前端集成示例（Vue 3 + Element Plus）

### 1. API封装

```javascript
// api/environment.js
import request from '@/utils/request'

// 分页查询环境数据列表
export function listEnvironmentData(query) {
  return request({
    url: '/seed/environment/list',
    method: 'get',
    params: query
  })
}

// 获取环境数据详情
export function getEnvironmentData(envRecordId) {
  return request({
    url: '/seed/environment/getInfo',
    method: 'get',
    params: { envRecordId }
  })
}

// 新增环境数据
export function addEnvironmentData(data) {
  return request({
    url: '/seed/environment/add',
    method: 'post',
    data: data
  })
}

// IoT批量导入
export function batchAddIoT(data) {
  return request({
    url: '/seed/environment/batchAddIoT',
    method: 'post',
    data: data
  })
}

// CSV批量导入
export function batchImport(data) {
  return request({
    url: '/seed/environment/batchImport',
    method: 'post',
    data: data
  })
}

// 修改环境数据
export function updateEnvironmentData(data) {
  return request({
    url: '/seed/environment/edit',
    method: 'post',
    data: data
  })
}

// 删除环境数据
export function delEnvironmentData(envRecordIds) {
  return request({
    url: '/seed/environment/remove',
    method: 'get',
    params: { envRecordIds }
  })
}

// 验证环境数据
export function validateEnvironmentData(data) {
  return request({
    url: '/seed/environment/validate',
    method: 'post',
    data: data
  })
}
```

### 2. 列表页面示例

```vue
<template>
  <div class="environment-data-container">
    <!-- 查询条件 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="批次" prop="batchId">
        <el-select v-model="queryParams.batchId" placeholder="请选择批次">
          <el-option
            v-for="item in batchList"
            :key="item.batchId"
            :label="item.batchName"
            :value="item.batchId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="参数类型" prop="parameterCode">
        <el-select v-model="queryParams.parameterCode" placeholder="请选择参数类型">
          <el-option label="日降雨量" value="RAIN_DAILY" />
          <el-option label="最高温度" value="TMAX" />
          <el-option label="最低温度" value="TMIN" />
          <el-option label="相对湿度" value="RH_AVG" />
        </el-select>
      </el-form-item>
      <el-form-item label="时间范围">
        <el-date-picker
          v-model="dateRange"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据表格 -->
    <el-table :data="dataList" v-loading="loading">
      <el-table-column label="批次名称" prop="batchName" />
      <el-table-column label="地块名称" prop="plotName" />
      <el-table-column label="采集时间" prop="timestamp" width="180" />
      <el-table-column label="参数名称" prop="parameterName" />
      <el-table-column label="数值" prop="value" />
      <el-table-column label="单位" prop="unit" />
      <el-table-column label="数据来源" prop="dataSource">
        <template #default="scope">
          <el-tag v-if="scope.row.dataSource === 'IOT_SYSTEM'" type="success">IoT系统</el-tag>
          <el-tag v-else-if="scope.row.dataSource === 'CSV_IMPORT'" type="warning">CSV导入</el-tag>
          <el-tag v-else type="info">手动录入</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button link type="primary" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button link type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-model:current-page="queryParams.pageNum"
      v-model:page-size="queryParams.pageSize"
      :total="total"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleQuery"
      @current-change="handleQuery"
    />
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { listEnvironmentData, delEnvironmentData } from '@/api/environment'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const dateRange = ref([])

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  batchId: null,
  parameterCode: null,
  queryStartTime: null,
  queryEndTime: null
})

// 查询列表
function handleQuery() {
  if (dateRange.value && dateRange.value.length === 2) {
    queryParams.queryStartTime = dateRange.value[0]
    queryParams.queryEndTime = dateRange.value[1]
  } else {
    queryParams.queryStartTime = null
    queryParams.queryEndTime = null
  }

  loading.value = true
  listEnvironmentData(queryParams).then(response => {
    dataList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

// 重置查询
function resetQuery() {
  dateRange.value = []
  queryParams.batchId = null
  queryParams.parameterCode = null
  queryParams.queryStartTime = null
  queryParams.queryEndTime = null
  queryParams.pageNum = 1
  handleQuery()
}

// 删除
function handleDelete(row) {
  ElMessageBox.confirm('是否确认删除该环境数据?', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    return delEnvironmentData(row.envRecordId)
  }).then(() => {
    handleQuery()
    ElMessage.success('删除成功')
  })
}

// 初始化
handleQuery()
</script>
```

### 3. 新增/编辑表单示例

```vue
<template>
  <el-dialog :title="title" v-model="open" width="600px">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="试验" prop="trialId">
        <el-select v-model="form.trialId" placeholder="请选择试验">
          <el-option
            v-for="item in trialList"
            :key="item.trialId"
            :label="item.trialName"
            :value="item.trialId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="批次" prop="batchId">
        <el-select v-model="form.batchId" placeholder="请选择批次">
          <el-option
            v-for="item in batchList"
            :key="item.batchId"
            :label="item.batchName"
            :value="item.batchId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="气象站" prop="stationId">
        <el-input v-model="form.stationId" placeholder="请输入气象站ID" />
      </el-form-item>
      <el-form-item label="采集时间" prop="timestamp">
        <el-date-picker
          v-model="form.timestamp"
          type="datetime"
          placeholder="选择日期时间"
          value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>
      <el-form-item label="参数类型" prop="parameterCode">
        <el-select v-model="form.parameterCode" placeholder="请选择参数类型">
          <el-option label="日降雨量" value="RAIN_DAILY" />
          <el-option label="最高温度" value="TMAX" />
          <el-option label="最低温度" value="TMIN" />
          <el-option label="平均温度" value="TEMP_AVG" />
          <el-option label="相对湿度" value="RH_AVG" />
          <el-option label="光照强度" value="SOLAR_RAD" />
          <el-option label="土壤湿度" value="SOIL_MOISTURE" />
        </el-select>
      </el-form-item>
      <el-form-item label="数值" prop="value">
        <el-input-number v-model="form.value" :precision="2" :step="0.1" />
      </el-form-item>
      <el-form-item label="单位" prop="unit">
        <el-input v-model="form.unit" placeholder="如: mm, °C, %" />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="form.remark" type="textarea" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="open = false">取消</el-button>
      <el-button type="primary" @click="submitForm">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { addEnvironmentData, updateEnvironmentData, validateEnvironmentData } from '@/api/environment'
import { ElMessage } from 'element-plus'

const open = ref(false)
const title = ref('')
const formRef = ref()

const form = reactive({
  envRecordId: null,
  trialId: null,
  batchId: null,
  stationId: null,
  timestamp: null,
  parameterCode: null,
  value: null,
  unit: null,
  remark: null
})

const rules = {
  trialId: [{ required: true, message: '请选择试验', trigger: 'change' }],
  batchId: [{ required: true, message: '请选择批次', trigger: 'change' }],
  stationId: [{ required: true, message: '请输入气象站ID', trigger: 'blur' }],
  timestamp: [{ required: true, message: '请选择采集时间', trigger: 'change' }],
  parameterCode: [{ required: true, message: '请选择参数类型', trigger: 'change' }],
  value: [{ required: true, message: '请输入数值', trigger: 'blur' }]
}

// 提交表单
function submitForm() {
  formRef.value.validate(valid => {
    if (valid) {
      // 先验证数据
      validateEnvironmentData(form).then(() => {
        // 验证通过后提交
        if (form.envRecordId) {
          updateEnvironmentData(form).then(() => {
            ElMessage.success('修改成功')
            open.value = false
            emit('refresh')
          })
        } else {
          addEnvironmentData(form).then(() => {
            ElMessage.success('新增成功')
            open.value = false
            emit('refresh')
          })
        }
      }).catch(error => {
        ElMessage.error(error.msg || '数据验证失败')
      })
    }
  })
}
</script>
```

---

## 性能优化建议

1. **分页查询优化**
   - 建议默认pageSize为10-20条
   - 使用时间范围过滤减少数据量
   - 避免查询全表数据

2. **批量导入优化**
   - 单次批量导入建议不超过1000条
   - 大批量数据建议分批次导入
   - 使用异步处理机制

3. **缓存策略**
   - 参数字典数据可缓存
   - 批次、试验列表可缓存（设置合理过期时间）

4. **前端性能**
   - 使用虚拟滚动处理大数据量列表
   - 合理使用防抖、节流
   - 图表展示时使用数据采样

---

## 联系支持

如有问题，请联系后端开发团队或查看项目文档。
