# DA农民需求录入模块 - API接口文档 (中文版)

## 一、接口概述

本模块提供DA(发展代理)录入农民投入品需求的相关接口,包括需求的增删改查功能。

**基础路径:** `/seed/demand/farmer`

**认证方式:** 所有接口需要在请求头中携带有效的认证令牌

---

## 二、接口列表

### 2.1 新增农民需求

**接口路径:** `POST /seed/demand/farmer/add`

**接口描述:** DA录入单个农民的投入品需求数据

**请求参数:**

```json
{
    "batchId": "string, 必填, 批次ID",
    "farmerId": "string, 必填, 农民ID",
    "farmerName": "string, 必填, 农民姓名, 最大100字符",
    "farmerIdNumber": "string, 必填, 农民身份证号, 最大50字符",
    "region": "string, 选填, 大区",
    "zone": "string, 选填, Zone",
    "woreda": "string, 必填, Woreda",
    "kebele": "string, 必填, Kebele",
    "village": "string, 必填, 村庄",
    "landArea": "number, 选填, 地块总面积(公顷)",
    "remark": "string, 选填, 备注, 最大500字符",
    "inputItems": [
        {
            "inputCategory": "string, 必填, 投入品大类(seed/fertilizer/pesticide)",
            "inputType": "string, 必填, 农资类型",
            "variety": "string, 必填, 品种",
            "specification": "string, 选填, 规格",
            "unit": "string, 必填, 单位",
            "quantity": "number, 必填, 需求数量"
        }
    ]
}
```

**响应参数:**

```json
{
    "code": 200,
    "msg": "Operation successful",
    "data": {
        "id": "string, 需求ID"
    }
}
```

**业务规则:**
1. 批次必须存在且状态为"采集中(collecting)"
2. 至少需要一条投入品明细
3. 投入品大类必须为有效枚举值(seed/fertilizer/pesticide)
4. 系统自动计算最大种子量和最大化肥量
5. 状态初始化为"草稿(draft)"

**错误提示:**
- `Batch not found`: 批次不存在
- `Batch is not in collecting status`: 批次不在采集中状态
- `Invalid input category`: 无效的投入品大类
- `Failed to save farmer demand`: 保存失败

---

### 2.2 修改农民需求

**接口路径:** `POST /seed/demand/farmer/update`

**接口描述:** 修改草稿或被驳回状态的农民需求

**请求参数:**

```json
{
    "id": "string, 必填, 需求ID",
    "version": "integer, 必填, 版本号(乐观锁)",
    "farmerId": "string, 必填, 农民ID",
    "farmerName": "string, 必填, 农民姓名",
    "farmerIdNumber": "string, 必填, 农民身份证号",
    "region": "string, 选填, 大区",
    "zone": "string, 选填, Zone",
    "woreda": "string, 必填, Woreda",
    "kebele": "string, 必填, Kebele",
    "village": "string, 必填, 村庄",
    "landArea": "number, 选填, 地块总面积(公顷)",
    "remark": "string, 选填, 备注",
    "inputItems": [
        {
            "inputCategory": "string, 必填, 投入品大类(seed/fertilizer/pesticide)",
            "inputType": "string, 必填, 农资类型",
            "variety": "string, 必填, 品种",
            "specification": "string, 选填, 规格",
            "unit": "string, 必填, 单位",
            "quantity": "number, 必填, 需求数量"
        }
    ]
}
```

**响应参数:**

```json
{
    "code": 200,
    "msg": "Operation successful",
    "data": null
}
```

**业务规则:**
1. 需求必须存在且未删除
2. 状态必须为"草稿(draft)"或"已驳回(rejected)"
3. 只有创建者(DA)可以修改
4. 版本号用于乐观锁控制
5. 旧的投入品明细会被删除,新的明细会被插入
6. 更新后状态重置为"草稿(draft)"

**错误提示:**
- `Demand not found`: 需求不存在
- `Can only update demand in draft or rejected status`: 只能修改草稿或已驳回状态的需求
- `Only the creator can update this demand`: 只有创建者可以修改
- `Invalid input category`: 无效的投入品大类
- `Update failed, please retry`: 更新失败,请重试(乐观锁冲突)

---

### 2.3 农民需求详情

**接口路径:** `GET /seed/demand/farmer/detail`

**接口描述:** 查询农民需求的详细信息,包括投入品明细和审核记录

**请求参数:**

| 参数名 | 类型   | 必填 | 说明    |
|--------|--------|------|---------|
| id     | string | 是   | 需求ID  |

**响应参数:**

```json
{
    "code": 200,
    "msg": "Operation successful",
    "data": {
        "id": "string, 需求ID",
        "batchId": "string, 批次ID",
        "batchNo": "string, 批次编号",
        "farmerId": "string, 农民ID",
        "farmerName": "string, 农民姓名",
        "farmerIdNumber": "string, 农民身份证号",
        "region": "string, 大区",
        "zone": "string, Zone",
        "woreda": "string, Woreda",
        "kebele": "string, Kebele",
        "village": "string, 村庄",
        "landArea": "number, 地块总面积(公顷)",
        "maxSeedQuantity": "number, 估算最大种子量(kg)",
        "maxFertilizerQuantity": "number, 估算最大肥料量(kg)",
        "status": "string, 状态代码",
        "statusName": "string, 状态名称",
        "currentAuditLevel": "string, 当前审核层级代码",
        "currentAuditLevelName": "string, 当前审核层级名称",
        "daUserName": "string, 录入DA姓名",
        "submitTime": "datetime, 提交时间, 格式: yyyy-MM-dd HH:mm:ss",
        "version": "integer, 版本号",
        "remark": "string, 备注",
        "createdTime": "datetime, 创建时间, 格式: yyyy-MM-dd HH:mm:ss",
        "inputItems": [
            {
                "id": "string, 明细ID",
                "inputCategory": "string, 投入品大类代码",
                "inputCategoryName": "string, 投入品大类名称",
                "inputType": "string, 农资类型",
                "variety": "string, 品种",
                "specification": "string, 规格",
                "unit": "string, 单位",
                "quantity": "number, 需求数量"
            }
        ],
        "auditRecords": [
            {
                "id": "string, 审核记录ID",
                "auditLevel": "string, 审核层级代码",
                "auditLevelName": "string, 审核层级名称",
                "auditUserName": "string, 审核人姓名",
                "auditTime": "datetime, 审核时间, 格式: yyyy-MM-dd HH:mm:ss",
                "auditAction": "string, 审核动作(submit/approve/reject)",
                "auditResult": "string, 审核结果(passed/rejected)",
                "auditOpinion": "string, 审核意见"
            }
        ]
    }
}
```

**错误提示:**
- `Demand not found`: 需求不存在

---

### 2.4 农民需求分页查询

**接口路径:** `POST /seed/demand/farmer/page`

**接口描述:** 分页查询农民需求列表,支持多条件筛选

**请求参数:**

```json
{
    "pageNum": "integer, 必填, 当前页码, 默认1",
    "pageSize": "integer, 必填, 每页条数, 默认10",
    "batchId": "string, 选填, 批次ID",
    "farmerName": "string, 选填, 农民姓名(模糊查询)",
    "farmerIdNumber": "string, 选填, 农民身份证号",
    "kebele": "string, 选填, Kebele",
    "woreda": "string, 选填, Woreda",
    "zone": "string, 选填, Zone",
    "village": "string, 选填, 村庄",
    "status": "string, 选填, 状态",
    "currentAuditLevel": "string, 选填, 当前审核层级",
    "inputCategory": "string, 选填, 投入品大类",
    "createdTimeStart": "string, 选填, 创建时间开始, 格式: yyyy-MM-dd",
    "createdTimeEnd": "string, 选填, 创建时间结束, 格式: yyyy-MM-dd"
}
```

**响应参数:**

```json
{
    "code": 200,
    "msg": "Operation successful",
    "data": {
        "records": [
            {
                "id": "string, 需求ID",
                "batchNo": "string, 批次编号",
                "farmerId": "string, 农民ID",
                "farmerName": "string, 农民姓名",
                "farmerIdNumber": "string, 农民身份证号",
                "kebele": "string, Kebele",
                "woreda": "string, Woreda",
                "village": "string, 村庄",
                "landArea": "number, 地块总面积",
                "status": "string, 状态代码",
                "statusName": "string, 状态名称",
                "currentAuditLevel": "string, 当前审核层级",
                "daUserName": "string, 录入DA姓名",
                "createdTime": "datetime, 创建时间, 格式: yyyy-MM-dd HH:mm:ss"
            }
        ],
        "total": "long, 总记录数",
        "size": "long, 每页条数",
        "current": "long, 当前页码",
        "pages": "long, 总页数"
    }
}
```

**业务规则:**
1. 根据当前用户的行政区划和角色过滤数据
2. DA只能查看自己创建的需求数据
3. 审核人员可查看其管辖范围内的数据
4. 结果按创建时间倒序排列

---

### 2.5 删除农民需求

**接口路径:** `POST /seed/demand/farmer/delete`

**接口描述:** 删除草稿状态的农民需求(逻辑删除)

**请求参数:**

```json
{
    "id": "string, 必填, 需求ID"
}
```

**响应参数:**

```json
{
    "code": 200,
    "msg": "Deleted successfully",
    "data": null
}
```

**业务规则:**
1. 需求必须存在且未删除
2. 状态必须为"草稿(draft)"
3. 只有创建者(DA)可以删除
4. 执行逻辑删除(设置is_deleted = 1)
5. 关联的投入品明细也会被逻辑删除

**错误提示:**
- `Demand not found`: 需求不存在
- `Can only delete demand in draft status`: 只能删除草稿状态的需求
- `Only the creator can delete this demand`: 只有创建者可以删除
- `Delete failed`: 删除失败

---

## 三、数据字典

### 3.1 状态枚举 (Status)

| 代码      | 显示名称 | 说明        |
|-----------|----------|-------------|
| draft     | Draft    | 草稿状态    |
| submitted | Submitted | 已提交审核  |
| approved  | Approved | 已通过      |
| rejected  | Rejected | 已驳回      |
| locked    | Locked   | 已锁定(最终确定) |

### 3.2 投入品大类 (Input Category)

| 代码       | 显示名称    | 说明 |
|------------|------------|------|
| seed       | Seed       | 种子 |
| fertilizer | Fertilizer | 化肥 |
| pesticide  | Pesticide  | 农药 |

### 3.3 审核层级 (Audit Level)

| 代码     | 显示名称  | 说明   |
|----------|----------|--------|
| village  | Village  | 村级   |
| town     | Town     | 镇级   |
| district | District | 区级   |
| state    | State    | 州级   |
| ministry | Ministry | 农业部 |

### 3.4 审核动作 (Audit Action)

| 代码    | 显示名称 | 说明 |
|---------|---------|------|
| submit  | Submit  | 提交 |
| approve | Approve | 通过 |
| reject  | Reject  | 驳回 |

### 3.5 审核结果 (Audit Result)

| 代码     | 显示名称  | 说明 |
|----------|----------|------|
| passed   | Passed   | 通过 |
| rejected | Rejected | 驳回 |

---

## 四、注意事项

### 4.1 认证与权限
- 所有接口需要认证
- DA用户只能操作自己创建的需求数据
- 需根据用户的行政区划过滤数据

### 4.2 并发控制
- 更新操作使用乐观锁(version字段)
- 如遇到版本冲突,返回错误提示用户重试

### 4.3 数据完整性
- 删除操作为逻辑删除
- 删除主记录时同时删除关联的投入品明细
- 外键关联需校验数据存在性

### 4.4 业务规则
- 批次状态必须为"collecting"才能录入需求
- 只有"draft"和"rejected"状态的需求可以修改
- 只有"draft"状态的需求可以删除
- 系统自动计算最大种子量和化肥量供参考

---

## 五、错误码说明

| 错误码 | 消息类型 | 说明 |
|--------|----------|------|
| 200  | 成功 | 操作成功 |
| 500  | 错误 | 服务器错误,详细错误信息在"msg"字段中 |

**常见错误消息:**
- `Batch not found`: 批次不存在或已删除
- `Batch is not in collecting status`: 批次不在数据采集阶段
- `Invalid input category`: 无效的投入品大类值
- `Demand not found`: 需求不存在或已删除
- `Can only update demand in draft or rejected status`: 状态限制
- `Only the creator can update this demand`: 权限限制
- `Update failed, please retry`: 乐观锁冲突
- `Can only delete demand in draft status`: 状态限制
- `Only the creator can delete this demand`: 权限限制

---

## 六、示例代码

### 6.1 新增农民需求

```javascript
// 请求
POST /seed/demand/farmer/add
Content-Type: application/json
Authorization: Bearer {token}

{
    "batchId": "550e8400-e29b-41d4-a716-446655440000",
    "farmerId": "F001",
    "farmerName": "Abebe Kebede",
    "farmerIdNumber": "ET123456789",
    "region": "Oromia",
    "zone": "East Shewa",
    "woreda": "Adama",
    "kebele": "Kebele 01",
    "village": "Village A",
    "landArea": 2.5,
    "remark": "第一个种植季",
    "inputItems": [
        {
            "inputCategory": "seed",
            "inputType": "小麦",
            "variety": "Digelu",
            "specification": "A级",
            "unit": "kg",
            "quantity": 50
        },
        {
            "inputCategory": "fertilizer",
            "inputType": "DAP",
            "variety": "标准型",
            "unit": "kg",
            "quantity": 100
        }
    ]
}

// 响应
{
    "code": 200,
    "msg": "Operation successful",
    "data": {
        "id": "660e8400-e29b-41d4-a716-446655440001"
    }
}
```

### 6.2 分页查询

```javascript
// 请求
POST /seed/demand/farmer/page
Content-Type: application/json
Authorization: Bearer {token}

{
    "pageNum": 1,
    "pageSize": 10,
    "batchId": "550e8400-e29b-41d4-a716-446655440000",
    "status": "draft",
    "woreda": "Adama"
}

// 响应
{
    "code": 200,
    "msg": "Operation successful",
    "data": {
        "records": [
            {
                "id": "660e8400-e29b-41d4-a716-446655440001",
                "batchNo": "DEMAND-2025-001",
                "farmerId": "F001",
                "farmerName": "Abebe Kebede",
                "farmerIdNumber": "ET123456789",
                "kebele": "Kebele 01",
                "woreda": "Adama",
                "village": "Village A",
                "landArea": 2.5,
                "status": "draft",
                "statusName": "Draft",
                "currentAuditLevel": null,
                "daUserName": "DA User",
                "createdTime": "2025-12-04 10:30:00"
            }
        ],
        "total": 1,
        "size": 10,
        "current": 1,
        "pages": 1
    }
}
```

---

## 七、更新日志

| 版本号  | 日期       | 作者 | 说明 |
|---------|------------|------|------|
| 1.0.0   | 2025-12-04 | igdp | 初始版本 - DA农民需求录入模块 |
