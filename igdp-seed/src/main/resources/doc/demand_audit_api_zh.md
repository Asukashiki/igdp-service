# 投入品需求审核模块 - API接口文档 (中文版)

## 一、接口概述

本模块提供农业投入品需求的分级审核流程接口,从村级到农业部逐级审核,包括提交、审核通过、驳回和最终锁定功能。

**基础路径:** `/seed/demand/audit`

**认证方式:** 所有接口需要在请求头中携带有效的认证令牌

---

## 二、接口列表

### 2.1 提交审核

**接口路径:** `POST /seed/demand/audit/submit`

**接口描述:** DA将农民需求提交至村级审核

**请求参数:**

```json
{
    "ids": ["string数组, 必填, 需求ID列表"]
}
```

**响应参数:**

```json
{
    "code": 200,
    "msg": "Submitted successfully",
    "data": {
        "successCount": "integer, 提交成功数量",
        "failCount": "integer, 提交失败数量"
    }
}
```

**业务规则:**
1. 需求必须存在且状态为"草稿(draft)"
2. 当前用户必须是创建该需求的DA
3. 状态更新为"已提交(submitted)"
4. 当前审核层级设置为"村级(village)"
5. 记录提交时间
6. 创建审核记录

**错误提示:**
- `Demand not found`: 需求不存在或已删除
- `Demand is not in draft status`: 只能提交草稿状态的需求
- `User is not the creator of demand`: 权限限制

---

### 2.2 待审核需求列表

**接口路径:** `POST /seed/demand/audit/pending/page`

**接口描述:** 分页查询当前用户待审核的需求列表

**请求参数:**

```json
{
    "pageNum": "integer, 必填, 当前页码, 默认1",
    "pageSize": "integer, 必填, 每页条数, 默认10",
    "batchId": "string, 选填, 批次ID",
    "farmerName": "string, 选填, 农民姓名(模糊查询)",
    "kebele": "string, 选填, Kebele",
    "woreda": "string, 选填, Woreda",
    "village": "string, 选填, 村庄"
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
                "farmerName": "string, 农民姓名",
                "farmerIdNumber": "string, 农民身份证号",
                "kebele": "string, Kebele",
                "woreda": "string, Woreda",
                "village": "string, 村庄",
                "landArea": "number, 地块总面积(公顷)",
                "currentAuditLevel": "string, 当前审核层级",
                "submitTime": "datetime, 提交时间, 格式: yyyy-MM-dd HH:mm:ss"
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
1. 根据当前用户的审核层级过滤数据
2. 只显示状态为"已提交(submitted)"的需求
3. 只显示符合用户行政区划的需求
4. 结果按提交时间倒序排列

**审核层级过滤规则:**
- **村级**: 显示用户所在kebele的需求
- **镇级**: 显示用户所在woreda的需求
- **区级**: 显示用户所在zone的需求
- **州级**: 显示用户所在region的需求
- **农业部级**: 显示所有到达农业部层级的需求

---

### 2.3 审核通过

**接口路径:** `POST /seed/demand/audit/approve`

**接口描述:** 审核通过并提交至上一级审核

**请求参数:**

```json
{
    "ids": ["string数组, 必填, 需求ID列表"],
    "remark": "string, 选填, 审核备注"
}
```

**响应参数:**

```json
{
    "code": 200,
    "msg": "Approved successfully",
    "data": {
        "successCount": "integer, 审核通过数量",
        "failCount": "integer, 审核失败数量"
    }
}
```

**业务规则:**
1. 需求必须存在且状态为"已提交(submitted)"
2. 当前审核层级必须与用户审核层级匹配
3. 需求提升到下一审核层级:
   - 村级 → 镇级
   - 镇级 → 区级
   - 区级 → 州级
   - 州级 → 农业部
   - 农业部 → 已通过(最终)
4. 在农业部层级,状态设置为"已通过(approved)"
5. 创建审核记录

**错误提示:**
- `Demand not found`: 需求不存在
- `Demand is not in submitted status`: 状态限制
- `Audit level mismatch for demand`: 层级不匹配
- `Invalid audit level`: 无效的用户审核层级

---

### 2.4 审核驳回

**接口路径:** `POST /seed/demand/audit/reject`

**接口描述:** 驳回需求至DA重新修改

**请求参数:**

```json
{
    "ids": ["string数组, 必填, 需求ID列表"],
    "auditOpinion": "string, 必填, 审核意见(驳回原因)",
    "remark": "string, 选填, 审核备注"
}
```

**响应参数:**

```json
{
    "code": 200,
    "msg": "Rejected successfully",
    "data": {
        "successCount": "integer, 驳回成功数量",
        "failCount": "integer, 驳回失败数量"
    }
}
```

**业务规则:**
1. 需求必须存在且状态为"已提交(submitted)"
2. 当前审核层级必须与用户审核层级匹配
3. 驳回时审核意见必填
4. 状态更新为"已驳回(rejected)"
5. 当前审核层级清空
6. DA可以修改后重新提交
7. 创建审核记录(包含审核意见)

**错误提示:**
- `Demand not found`: 需求不存在
- `Demand is not in submitted status`: 状态限制
- `Audit level mismatch for demand`: 层级不匹配
- `Audit opinion cannot be empty`: 验证错误

---

### 2.5 锁定批次

**接口路径:** `POST /seed/demand/audit/lock`

**接口描述:** 农业部锁定批次需求(最终审批),使其不可修改

**请求参数:**

```json
{
    "batchId": "string, 必填, 批次ID",
    "version": "integer, 必填, 批次版本号(乐观锁)"
}
```

**响应参数:**

```json
{
    "code": 200,
    "msg": "Batch locked successfully",
    "data": null
}
```

**业务规则:**
1. 当前用户必须是农业部级别
2. 批次必须存在且状态为"审核中(reviewing)"
3. 批次中所有需求必须已通过审核
4. 批次状态更新为"已锁定(locked)"
5. 所有已通过需求更新为"已锁定(locked)"状态
6. 锁定后数据不可修改

**错误提示:**
- `Batch not found`: 批次不存在
- `Batch is not in reviewing status`: 状态限制
- `Not all demands in batch are approved`: 审核未完成
- `Failed to lock batch, please retry`: 乐观锁冲突

---

## 三、审核流程图

```
┌─────────────────────────────────────────────────────────────────┐
│                        需求审核工作流                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌─────────┐   ┌─────────┐   ┌─────────┐   ┌─────────┐        │
│  │   DA    │   │  村级   │   │  镇级   │   │  区级   │        │
│  │  提交   │──>│  审核   │──>│  审核   │──>│  审核   │        │
│  └─────────┘   └─────────┘   └─────────┘   └─────────┘        │
│       ^             │             │             │               │
│       │        驳回 │        驳回 │        驳回 │               │
│       └─────────────┘             │             │               │
│       ^                           │             │               │
│       └───────────────────────────┘             ▼               │
│       ^                                   ┌─────────┐           │
│       └───────────────────────────────────│  州级   │           │
│                                           │  审核   │           │
│                                           └─────────┘           │
│                                                 │               │
│                                                 ▼               │
│                                           ┌──────────┐          │
│                                           │ 农业部   │          │
│                                           │  审核   │          │
│                                           └──────────┘          │
│                                                 │               │
│                                                 ▼               │
│                                           ┌──────────┐          │
│                                           │  锁定    │          │
│                                           │  批次   │          │
│                                           └──────────┘          │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 四、数据字典

### 4.1 需求状态

| 代码      | 显示名称   | 说明           |
|-----------|-----------|----------------|
| draft     | Draft     | 草稿状态       |
| submitted | Submitted | 已提交审核     |
| approved  | Approved  | 已通过         |
| rejected  | Rejected  | 已驳回         |
| locked    | Locked    | 已锁定(最终确定) |

### 4.2 审核层级

| 代码     | 显示名称  | 说明                  |
|----------|----------|----------------------|
| village  | Village  | 村级 (Kebele)        |
| town     | Town     | 镇级 (Woreda)        |
| district | District | 区级 (Zone)          |
| state    | State    | 州级 (Region)        |
| ministry | Ministry | 农业部级 (联邦级)     |

### 4.3 审核动作

| 代码    | 显示名称 | 说明 |
|---------|---------|------|
| submit  | Submit  | 提交 |
| approve | Approve | 通过 |
| reject  | Reject  | 驳回 |

### 4.4 审核结果

| 代码     | 显示名称  | 说明 |
|----------|----------|------|
| passed   | Passed   | 通过 |
| rejected | Rejected | 驳回 |

### 4.5 批次状态

| 代码       | 显示名称    | 说明          |
|------------|------------|---------------|
| collecting | Collecting | 数据采集阶段  |
| reviewing  | Reviewing  | 审核中        |
| completed  | Completed  | 审核完成      |
| locked     | Locked     | 已锁定(最终确定) |

---

## 五、注意事项

### 5.1 认证与权限

- 所有接口需要认证
- 用户只能审核其管辖范围内的需求
- 审核层级由用户角色决定
- 数据根据行政层级过滤

### 5.2 审核层级权限

| 层级   | 可审核范围                |
|--------|--------------------------|
| 村级   | 同一kebele的需求          |
| 镇级   | 同一woreda的需求          |
| 区级   | 同一zone的需求            |
| 州级   | 同一region的需求          |
| 农业部 | 所有提交到农业部层级的需求 |

### 5.3 并发控制

- 批次锁定使用乐观锁(version字段)
- 如遇到版本冲突,返回错误提示用户重试

### 5.4 数据完整性

- 所有操作都是事务性的
- 审核记录会被保留用于审计追踪
- 被驳回的需求可以修改后重新提交
- 锁定的数据不可修改

### 5.5 业务规则

- 只有"草稿"状态的需求可以提交
- 只有"已提交"状态的需求可以审核
- 驳回时审核意见必填
- 批次只能在所有需求都通过审核后锁定
- 锁定状态是最终状态,不可逆

---

## 六、错误码说明

| 错误码 | 类型 | 说明                              |
|--------|------|-----------------------------------|
| 200    | 成功 | 操作成功                          |
| 500    | 错误 | 服务器错误,详细信息在"msg"字段中  |

**常见错误消息:**
- `Demand not found`: 需求不存在或已删除
- `Demand is not in draft status`: 只能提交草稿状态的需求
- `Demand is not in submitted status`: 只能审核已提交状态的需求
- `User is not the creator of demand`: 权限被拒绝
- `Audit level mismatch for demand`: 用户审核层级与需求层级不匹配
- `Invalid audit level`: 无效的用户审核层级配置
- `Batch not found`: 批次不存在
- `Batch is not in reviewing status`: 批次状态不允许锁定
- `Not all demands in batch are approved`: 不能锁定有未通过需求的批次
- `Failed to lock batch, please retry`: 乐观锁冲突

---

## 七、示例代码

### 7.1 提交审核

```javascript
// 请求
POST /seed/demand/audit/submit
Content-Type: application/json
Authorization: Bearer {token}

{
    "ids": [
        "660e8400-e29b-41d4-a716-446655440001",
        "660e8400-e29b-41d4-a716-446655440002"
    ]
}

// 响应
{
    "code": 200,
    "msg": "Submitted successfully",
    "data": {
        "successCount": 2,
        "failCount": 0
    }
}
```

### 7.2 查询待审核需求

```javascript
// 请求
POST /seed/demand/audit/pending/page
Content-Type: application/json
Authorization: Bearer {token}

{
    "pageNum": 1,
    "pageSize": 10,
    "batchId": "550e8400-e29b-41d4-a716-446655440000",
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
                "farmerName": "Abebe Kebede",
                "farmerIdNumber": "ET123456789",
                "kebele": "Kebele 01",
                "woreda": "Adama",
                "village": "Village A",
                "landArea": 2.5,
                "currentAuditLevel": "village",
                "submitTime": "2025-12-04 10:30:00"
            }
        ],
        "total": 1,
        "size": 10,
        "current": 1,
        "pages": 1
    }
}
```

### 7.3 审核通过

```javascript
// 请求
POST /seed/demand/audit/approve
Content-Type: application/json
Authorization: Bearer {token}

{
    "ids": [
        "660e8400-e29b-41d4-a716-446655440001",
        "660e8400-e29b-41d4-a716-446655440002"
    ],
    "remark": "所有信息已核实并通过"
}

// 响应
{
    "code": 200,
    "msg": "Approved successfully",
    "data": {
        "successCount": 2,
        "failCount": 0
    }
}
```

### 7.4 审核驳回

```javascript
// 请求
POST /seed/demand/audit/reject
Content-Type: application/json
Authorization: Bearer {token}

{
    "ids": ["660e8400-e29b-41d4-a716-446655440001"],
    "auditOpinion": "地块面积信息不正确,请核实后重新提交",
    "remark": "需要额外验证"
}

// 响应
{
    "code": 200,
    "msg": "Rejected successfully",
    "data": {
        "successCount": 1,
        "failCount": 0
    }
}
```

### 7.5 锁定批次

```javascript
// 请求
POST /seed/demand/audit/lock
Content-Type: application/json
Authorization: Bearer {token}

{
    "batchId": "550e8400-e29b-41d4-a716-446655440000",
    "version": 1
}

// 响应
{
    "code": 200,
    "msg": "Batch locked successfully",
    "data": null
}
```

---

## 八、审核记录结构

每次审核操作都会创建包含以下信息的记录:

- **批次ID**: 需求所属批次
- **需求ID**: 被审核的需求
- **审核类型**: single(单条) 或 batch(批量)
- **审核层级**: village/town/district/state/ministry
- **行政区划代码和名称**: 审核人所在行政区划
- **审核人**: 审核人ID和姓名
- **审核时间**: 审核执行时间
- **审核动作**: submit/approve/reject
- **审核结果**: passed/rejected
- **审核意见**: 驳回原因(驳回时必填)
- **备注**: 附加说明

---

## 九、性能考虑

### 9.1 批量操作

- 提交、审核通过、驳回操作支持多条需求
- 每条操作在事务内独立处理
- 支持部分成功(部分成功,部分失败)
- 结果包含successCount和failCount

### 9.2 查询优化

- 待审核查询按用户行政区划过滤
- 关键字段创建索引(status, audit_level, admin codes)
- 使用分页限制结果集大小

### 9.3 并发处理

- 批次锁定使用乐观锁
- 单条需求更新使用数据库级锁
- 审核操作记录日志用于审计追踪

---

## 十、更新日志

| 版本号 | 日期       | 作者 | 说明               |
|--------|------------|------|--------------------|
| 1.0.0  | 2025-12-04 | igdp | 初始版本 - 需求审核模块 |
