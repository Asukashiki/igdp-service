# API 接口文档

## 目录
- [1. 农资汇聚统计管理接口](#1-农资汇聚统计管理接口)
- [2. 农民需求管理接口](#2-农民需求管理接口)

---

## 1. 农资汇聚统计管理接口

### 1.1 查询农资汇聚统计列表（分页）

**接口描述**: 分页查询农资汇聚统计列表，支持多条件筛选

**请求方式**: `GET`

**请求路径**: `/demand/input/summary/list`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| page | Integer | 否 | 页码，默认1 | 1 |
| pageSize | Integer | 否 | 每页大小，默认10 | 10 |
| inputCategory | String | 否 | 农资分类 | seed |
| inputType | String | 否 | 农资类型 | wheat |
| status | String | 否 | 状态：0-待审核/1-成功/2-拒绝 | 1 |
| sourceName | String | 否 | 来源名称（模糊查询） | Kebele A |
| sourceCode | String | 否 | 来源编码 | KB001 |
| targetName | String | 否 | 目标名称（模糊查询） | Woreda B |
| targetCode | String | 否 | 目标编码 | WR001 |
| keyword | String | 否 | 关键字（分类/类型/来源/目标） | wheat |
| startTime | String | 否 | 开始时间 | 2025-01-01 00:00:00 |
| endTime | String | 否 | 结束时间 | 2025-12-31 23:59:59 |

**请求示例**:
```
GET /demand/input/summary/list?page=1&pageSize=10&status=1&inputCategory=seed
```

**响应示例**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "list": [
      {
        "id": "1234567890",
        "inputCategory": "seed",
        "inputType": "wheat",
        "totalQuantity": 1500.00,
        "totalCount": 25,
        "demandCount": 20,
        "varieties": "variety1,variety2",
        "specifications": "50kg,25kg",
        "units": "kg,bag",
        "sourceName": "Kebele A",
        "sourceCode": "KB001",
        "targetName": "Woreda B",
        "targetCode": "WR001",
        "status": "1",
        "statusDesc": "成功",
        "createdTime": "2025-12-09 10:30:00",
        "updatedTime": "2025-12-09 15:20:00"
      }
    ],
    "total": 100,
    "page": 1,
    "pageSize": 10
  }
}
```

---

### 1.2 查询农资汇聚统计详情

**接口描述**: 根据ID查询农资汇聚统计详情

**请求方式**: `GET`

**请求路径**: `/demand/input/summary/{id}`

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | String | 是 | 汇聚统计记录ID |

**请求示例**:
```
GET /demand/input/summary/1234567890
```

**响应示例**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": "1234567890",
    "inputCategory": "seed",
    "inputType": "wheat",
    "totalQuantity": 1500.00,
    "totalCount": 25,
    "demandCount": 20,
    "varieties": "variety1,variety2",
    "specifications": "50kg,25kg",
    "units": "kg,bag",
    "sourceName": "Kebele A",
    "sourceCode": "KB001",
    "targetName": "Woreda B",
    "targetCode": "WR001",
    "status": "1",
    "statusDesc": "成功",
    "createdTime": "2025-12-09 10:30:00",
    "updatedTime": "2025-12-09 15:20:00"
  }
}
```

---

### 1.3 添加农资汇聚统计

**接口描述**: 新增农资汇聚统计记录

**请求方式**: `POST`

**请求路径**: `/demand/input/summary`

**请求头**:
```
Content-Type: application/json
```

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 | 校验规则 |
|--------|------|------|------|----------|
| inputCategory | String | 是 | 农资分类 | 不能为空 |
| inputType | String | 是 | 农资类型 | 不能为空 |
| totalQuantity | BigDecimal | 是 | 总数量 | 不能为空，≥0 |
| totalCount | Integer | 是 | 总项目数 | 不能为空，≥0 |
| demandCount | Integer | 否 | 涉及需求数 | ≥0 |
| varieties | String | 否 | 包含的品种（逗号分隔） | - |
| specifications | String | 否 | 包含的规格（逗号分隔） | - |
| units | String | 否 | 包含的单位（逗号分隔） | - |
| sourceName | String | 否 | 来源名称 | - |
| sourceCode | String | 否 | 来源编码 | - |
| targetName | String | 否 | 目标名称 | - |
| targetCode | String | 否 | 目标编码 | - |
| status | String | 否 | 状态：0-待审核/1-成功/2-拒绝 | - |

**请求示例**:
```json
{
  "inputCategory": "seed",
  "inputType": "wheat",
  "totalQuantity": 1500.00,
  "totalCount": 25,
  "demandCount": 20,
  "varieties": "variety1,variety2",
  "specifications": "50kg,25kg",
  "units": "kg,bag",
  "sourceName": "Kebele A",
  "sourceCode": "KB001",
  "targetName": "Woreda B",
  "targetCode": "WR001",
  "status": "0"
}
```

**响应示例**:
```json
{
  "code": 200,
  "msg": "添加成功"
}
```

**错误响应**:
```json
{
  "code": 500,
  "msg": "该农资分类和类型组合已存在"
}
```

---

### 1.4 更新农资汇聚统计

**接口描述**: 更新农资汇聚统计记录

**请求方式**: `POST`

**请求路径**: `/demand/input/summary/update`

**请求头**:
```
Content-Type: application/json
```

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 | 校验规则 |
|--------|------|------|------|----------|
| id | String | 是 | 记录ID | 不能为空 |
| inputCategory | String | 是 | 农资分类 | 不能为空 |
| inputType | String | 是 | 农资类型 | 不能为空 |
| totalQuantity | BigDecimal | 是 | 总数量 | 不能为空，≥0 |
| totalCount | Integer | 是 | 总项目数 | 不能为空，≥0 |
| demandCount | Integer | 否 | 涉及需求数 | ≥0 |
| varieties | String | 否 | 包含的品种（逗号分隔） | - |
| specifications | String | 否 | 包含的规格（逗号分隔） | - |
| units | String | 否 | 包含的单位（逗号分隔） | - |
| sourceName | String | 否 | 来源名称 | - |
| sourceCode | String | 否 | 来源编码 | - |
| targetName | String | 否 | 目标名称 | - |
| targetCode | String | 否 | 目标编码 | - |
| status | String | 否 | 状态：0-待审核/1-成功/2-拒绝 | - |

**请求示例**:
```json
{
  "id": "1234567890",
  "inputCategory": "seed",
  "inputType": "wheat",
  "totalQuantity": 1600.00,
  "totalCount": 26,
  "demandCount": 21,
  "status": "1"
}
```

**响应示例**:
```json
{
  "code": 200,
  "msg": "更新成功"
}
```

---

### 1.5 删除农资汇聚统计

**接口描述**: 删除单条农资汇聚统计记录

**请求方式**: `POST`

**请求路径**: `/demand/input/summary/delete/{id}`

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | String | 是 | 记录ID |

**请求示例**:
```
POST /demand/input/summary/delete/1234567890
```

**响应示例**:
```json
{
  "code": 200,
  "msg": "删除成功"
}
```

---

### 1.6 批量删除农资汇聚统计

**接口描述**: 批量删除农资汇聚统计记录

**请求方式**: `POST`

**请求路径**: `/demand/input/summary/batchDelete`

**请求头**:
```
Content-Type: application/json
```

**请求参数**: 字符串数组（记录ID列表）

**请求示例**:
```json
["1234567890", "0987654321", "1122334455"]
```

**响应示例**:
```json
{
  "code": 200,
  "msg": "删除成功，共删除3条记录"
}
```

---

### 1.7 二次汇聚统计

**接口描述**: 从汇聚统计表中按来源编码查询并再次汇总，用于上级组织汇总下级组织的数据

**请求方式**: `POST`

**请求路径**: `/demand/input/summary/aggregate`

**请求头**:
```
Content-Type: application/json
```

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sourceCode | String | 是 | 来源组织编码（用于筛选下级组织的汇聚数据） |
| sourceName | String | 否 | 来源组织名称 |
| targetCode | String | 否 | 目标组织编码（当前组织编码） |
| targetName | String | 否 | 目标组织名称（当前组织名称） |

**业务逻辑**:
1. 根据 sourceCode 从 `demand_input_summary_item` 表中查询状态为"待审核"(status='0')的记录
2. 按农资分类（inputCategory）和农资类型（inputType）进行分组统计
3. 统计每组的记录数（COUNT）和总数量汇总（SUM）
4. 将统计结果批量插入到汇聚统计表作为新的汇聚记录
5. 返回成功插入的记录数

**SQL查询逻辑**:
```sql
SELECT
    input_category AS inputCategory,
    input_type AS inputType,
    COUNT(*) AS totalCount,
    SUM(total_quantity) AS totalQuantity
FROM demand_input_summary_item
WHERE source_code = #{sourceCode}
  AND status = '0'
GROUP BY input_category, input_type
```

**请求示例**:
```json
{
  "sourceCode": "WR001",
  "sourceName": "Woreda B",
  "targetCode": "ZN001",
  "targetName": "Zone C"
}
```

**响应示例**:
```json
{
  "code": 200,
  "msg": "汇聚成功，共汇聚5条记录",
  "data": 5
}
```

**响应说明**:
- `data`: 成功汇聚并插入的统计记录数
- 例如：如果该 sourceCode 下有5种不同的农资分类和类型组合，则返回5

**应用场景**:
- Kebele → Woreda：将多个Kebele的汇聚数据再次汇总到Woreda级别
- Woreda → Zone：将多个Woreda的汇聚数据再次汇总到Zone级别
- Zone → Region：将多个Zone的汇聚数据再次汇总到Region级别

**注意事项**:
- 仅统计状态为"待审核"(status='0')的记录
- 新生成的汇聚记录默认状态也为"待审核"
- 汇聚操作支持事务回滚，保证数据一致性
- sourceCode 是必填参数，用于筛选下级组织的数据

**错误响应**:
```json
{
  "code": 500,
  "msg": "来源编码不能为空"
}
```

---

## 2. 农民需求管理接口

### 2.1 添加农民需求

**接口描述**: 创建新的农民需求记录

**请求方式**: `POST`

**请求路径**: `/seed/demand/farmer/add`

**请求头**:
```
Content-Type: application/json
```

**请求参数**: 参考 FarmerDemandAddDTO

**响应示例**:
```json
{
  "code": 200,
  "msg": "Operation successful",
  "data": {
    "id": "demand-uuid-123"
  }
}
```

---

### 2.2 更新农民需求

**接口描述**: 更新农民需求记录（仅草稿和被拒绝状态可更新）

**请求方式**: `POST`

**请求路径**: `/seed/demand/farmer/update`

**请求头**:
```
Content-Type: application/json
```

**请求参数**: 参考 FarmerDemandUpdateDTO

**响应示例**:
```json
{
  "code": 200,
  "msg": "Operation successful"
}
```

---

### 2.3 查询农民需求详情

**接口描述**: 根据ID查询农民需求详细信息

**请求方式**: `GET`

**请求路径**: `/seed/demand/farmer/detail`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | String | 是 | 需求ID |

**请求示例**:
```
GET /seed/demand/farmer/detail?id=demand-uuid-123
```

**响应示例**:
```json
{
  "code": 200,
  "msg": "Operation successful",
  "data": {
    "id": "demand-uuid-123",
    "batchId": "batch-001",
    "batchNo": "2025-001",
    "farmerName": "John Doe",
    "farmerIdNumber": "ID123456",
    "kebele": "Kebele A",
    "woreda": "Woreda B",
    "zone": "Zone C",
    "village": "Village D",
    "landArea": 5.5,
    "status": "1",
    "statusName": "已提交",
    "currentAuditLevel": "L1",
    "currentAuditLevelName": "一级审核",
    "inputItems": [
      {
        "id": "item-001",
        "inputCategory": "seed",
        "inputCategoryName": "种子",
        "inputType": "wheat",
        "variety": "variety1",
        "specification": "50kg",
        "unit": "kg",
        "quantity": 100.00
      }
    ],
    "auditRecords": [
      {
        "id": "audit-001",
        "auditLevel": "L1",
        "auditLevelName": "一级审核",
        "auditResult": "approved",
        "auditComment": "通过",
        "auditorName": "Auditor A",
        "auditTime": "2025-12-09 15:00:00"
      }
    ],
    "createdTime": "2025-12-09 10:00:00"
  }
}
```

---

### 2.4 分页查询农民需求列表

**接口描述**: 分页查询农民需求列表，支持多条件筛选

**请求方式**: `POST`

**请求路径**: `/seed/demand/farmer/page`

**请求头**:
```
Content-Type: application/json
```

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页大小 |
| batchId | String | 否 | 批次ID |
| farmerName | String | 否 | 农民姓名（模糊查询） |
| farmerIdNumber | String | 否 | 农民身份证号 |
| kebele | String | 否 | Kebele |
| woreda | String | 否 | Woreda |
| zone | String | 否 | Zone |
| village | String | 否 | Village |
| status | String | 否 | 状态 |
| currentAuditLevel | String | 否 | 当前审核级别 |
| createdTimeStart | String | 否 | 创建开始时间 |
| createdTimeEnd | String | 否 | 创建结束时间 |

**请求示例**:
```json
{
  "pageNum": 1,
  "pageSize": 10,
  "status": "1",
  "kebele": "Kebele A"
}
```

**响应示例**:
```json
{
  "code": 200,
  "msg": "Operation successful",
  "data": {
    "records": [
      {
        "id": "demand-uuid-123",
        "batchNo": "2025-001",
        "farmerName": "John Doe",
        "farmerIdNumber": "ID123456",
        "kebele": "Kebele A",
        "landArea": 5.5,
        "status": "1",
        "statusName": "已提交",
        "createdTime": "2025-12-09 10:00:00"
      }
    ],
    "total": 100,
    "size": 10,
    "current": 1,
    "pages": 10
  }
}
```

---

### 2.5 删除农民需求

**接口描述**: 删除农民需求记录（仅草稿状态可删除）

**请求方式**: `POST`

**请求路径**: `/seed/demand/farmer/delete`

**请求头**:
```
Content-Type: application/json
```

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | String | 是 | 需求ID |

**请求示例**:
```json
{
  "id": "demand-uuid-123"
}
```

**响应示例**:
```json
{
  "code": 200,
  "msg": "Deleted successfully"
}
```

---

### 2.6 农资需求汇聚统计

**接口描述**: 根据组织信息（Kebele）汇聚农资需求统计数据，并保存到汇聚统计表

**请求方式**: `POST`

**请求路径**: `/seed/demand/farmer/input/aggregation`

**请求头**:
```
Content-Type: application/json
```

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sourceCode | String | 是 | 来源组织编码（Kebele编码） |
| sourceName | String | 是 | 来源组织名称（Kebele名称） |
| targetCode | String | 否 | 目标组织编码（Woreda编码） |
| targetName | String | 否 | 目标组织名称（Woreda名称） |

**业务逻辑**:
1. 根据 sourceCode（Kebele）查询该区域内所有已审核通过（status='2'）且未删除的农资需求项
2. 按农资分类（inputCategory）和农资类型（inputType）进行分组统计
3. 统计每组的总数量（totalQuantity）和总项目数（totalCount）
4. 将统计结果批量插入到农资汇聚统计表（demand_input_summary_item）
5. 返回成功插入的记录数

**SQL查询逻辑**:
```sql
SELECT
    i.input_category AS inputCategory,
    i.input_type AS inputType,
    COUNT(*) AS totalCount,
    SUM(i.quantity) AS totalQuantity
FROM demand_farmer_input_item i, demand_farmer_detail d
WHERE
    i.demand_id = d.id
    AND d.kebele = #{kebele}      -- 按Kebele过滤
    AND d.status = '2'             -- 仅统计已审核通过的需求
    AND i.is_deleted = 0           -- 排除已删除记录
GROUP BY i.input_category, i.input_type
```

**请求示例**:
```json
{
  "sourceCode": "KB001",
  "sourceName": "Kebele A",
  "targetCode": "WR001",
  "targetName": "Woreda B"
}
```

**响应示例**:
```json
{
  "code": 200,
  "msg": "aggregation successful",
  "data": 5
}
```

**响应说明**:
- `data`: 成功汇聚并插入的统计记录数
- 例如：如果该Kebele有5种不同的农资分类和类型组合，则返回5

**处理流程**:
1. 接收请求参数（sourceCode, sourceName, targetCode, targetName）
2. 调用 Mapper 层执行汇聚SQL，获取统计数据列表
3. 遍历统计结果，为每条记录创建 DemandInputSummaryItemDTO：
   - 设置来源和目标信息
   - 设置农资分类和类型
   - 设置统计数量
   - 默认状态为待审核（status='0'）
4. 调用 DemandInputSummaryItemService 批量保存统计记录
5. 返回成功保存的记录数

**错误处理**:
```json
{
  "code": 500,
  "msg": "该农资分类和类型组合已存在"
}
```

**注意事项**:
- 仅统计已通过审核的需求（status='2'）
- 同一分类和类型的组合不能重复汇聚（有唯一索引约束）
- 汇聚操作支持事务回滚，保证数据一致性

---

## 数据字典

### 农资分类（inputCategory）
- `seed`: 种子
- `fertilizer`: 化肥
- `pesticide`: 农药

### 需求状态（status - demand_farmer_detail）
- `0`: 草稿
- `1`: 已提交
- `2`: 审核通过
- `3`: 已拒绝

### 汇聚统计状态（status - demand_input_summary_item）
- `0`: 待审核
- `1`: 成功
- `2`: 拒绝

### 审核级别（auditLevel）
- `L1`: 一级审核
- `L2`: 二级审核
- `L3`: 三级审核

---

## 通用错误码

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 500 | 服务器内部错误 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |

---

## 附录

### 响应格式说明

所有接口统一使用 AjaxResult 响应格式：

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {}
}
```

### 分页响应格式

分页查询接口统一返回以下格式：

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "list": [],           // 数据列表
    "total": 100,         // 总记录数
    "page": 1,            // 当前页码
    "pageSize": 10        // 每页大小
  }
}
```

或使用 MyBatis Plus 的 Page 格式：

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "records": [],        // 数据列表
    "total": 100,         // 总记录数
    "size": 10,           // 每页大小
    "current": 1,         // 当前页码
    "pages": 10           // 总页数
  }
}
```
