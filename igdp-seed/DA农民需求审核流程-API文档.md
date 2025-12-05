# DA农民需求审核流程 - 完整API文档

## 📋 业务流程概览

```
┌─────────────────────────────────────────────────────────────────────┐
│                          DA农民需求审核流程                           │
└─────────────────────────────────────────────────────────────────────┘

1. DA录入      → draft (草稿)
   ↓
2. DA提交      → submitted (已提交) + current_audit_level = village
   ↓
3. 村级审核     → 通过: current_audit_level = town
   │            驳回: status = rejected, 退回DA修改
   ↓
4. 镇级审核     → 通过: current_audit_level = district
   │            驳回: status = rejected, 退回DA修改
   ↓
5. 区级审核     → 通过: current_audit_level = state
   │            驳回: status = rejected, 退回DA修改
   ↓
6. 州级审核     → 通过: current_audit_level = ministry
   │            驳回: status = rejected, 退回DA修改
   ↓
7. 部级审核     → 通过: status = approved, current_audit_level = null
   │            驳回: status = rejected, 退回DA修改
   ↓
8. 部级锁定     → status = locked (最终锁定，不可修改)
```

---

## 🔐 权限说明

| 角色 | 审核层级 | 可见范围 |
|-----|---------|---------|
| DA用户 | - | 仅自己录入的需求 |
| 村级审核员 | village | 本村(kebele)的需求 |
| 镇级审核员 | town | 本镇(woreda)的需求 |
| 区级审核员 | district | 本区(zone)的需求 |
| 州级审核员 | state | 本州(region)的需求 |
| 部级审核员 | ministry | 全部需求 |

---

## 📡 API 接口列表

### **1. DA农民需求录入模块**

基础路径: `/seed/demand/farmer`

#### 1.1 新增需求
```http
POST /seed/demand/farmer/add
Content-Type: application/json

{
  "farmerId": "农民ID",
  "farmerName": "农民姓名",
  "farmerIdNumber": "农民身份证号",
  "region": "州",
  "zone": "区",
  "woreda": "镇",
  "kebele": "村",
  "village": "具体村庄",
  "landArea": 10.5,
  "inputItems": [
    {
      "inputCategory": "seed",  // seed/fertilizer/pesticide/tools
      "inputType": "玉米种子",
      "variety": "品种A",
      "specification": "规格",
      "unit": "kg",
      "quantity": 100
    }
  ]
}

响应:
{
  "code": 200,
  "msg": "Operation successful",
  "data": {
    "id": "e5e7d8e5824f11dcf2eb5317104061c1"
  }
}
```

#### 1.2 编辑需求 (仅草稿/驳回状态可编辑)
```http
POST /seed/demand/farmer/update
Content-Type: application/json

{
  "id": "需求ID",
  "farmerName": "修改后的农民姓名",
  // ... 其他字段同add接口
}
```

#### 1.3 查看需求详情
```http
GET /seed/demand/farmer/detail?id=83c36b293c63242681281498b4245723

响应:
{
  "code": 200,
  "msg": "Operation successful",
  "data": {
    "id": "83c36b293c63242681281498b4245723",
    "batchNo": "BATCH-2025",
    "farmerName": "农民姓名001",
    "status": "draft",
    "statusName": "草稿",
    "currentAuditLevel": null,
    "currentAuditLevelName": null,
    "inputItems": [...],
    "auditRecords": []  // 审核记录列表
  }
}
```

#### 1.4 查询需求列表
```http
POST /seed/demand/farmer/page
Content-Type: application/json

{
  "pageNum": 1,
  "pageSize": 10,
  "batchId": "批次ID(可选)",
  "farmerName": "农民姓名(可选)",
  "status": "draft",  // draft/submitted/approved/rejected/locked
  "createdTimeStart": "2025-12-01",
  "createdTimeEnd": "2025-12-31"
}
```

#### 1.5 删除需求 (仅草稿状态可删除)
```http
POST /seed/demand/farmer/delete
Content-Type: application/json

{
  "id": "需求ID"
}
```

---

### **2. 审核流程模块**

基础路径: `/seed/demand/audit`

#### 2.1 DA提交审核 (草稿 → 已提交)
```http
POST /seed/demand/audit/submit
Content-Type: application/json

{
  "ids": [
    "e5e7d8e5824f11dcf2eb5317104061c1",
    "83c36b293c63242681281498b4245723"
  ]
}

响应:
{
  "code": 200,
  "msg": "Submitted successfully",
  "data": {
    "successCount": 2,
    "failCount": 0
  }
}

说明:
- 只能提交状态为 draft 的需求
- 只能提交自己录入的需求
- 提交后状态变为 submitted
- 审核层级设为 village (村级)
- 自动生成汇总数据
```

#### 2.2 查询待审核列表
```http
POST /seed/demand/audit/pending/page
Content-Type: application/json

{
  "pageNum": 1,
  "pageSize": 10,
  "batchId": "批次ID(可选)",
  "farmerName": "农民姓名(可选)",
  "kebele": "村(可选)",
  "woreda": "镇(可选)"
}

响应:
{
  "code": 200,
  "msg": "Operation successful",
  "data": {
    "total": 10,
    "records": [
      {
        "id": "需求ID",
        "batchNo": "BATCH-2025",
        "farmerName": "农民姓名",
        "landArea": 10.5,
        "status": "submitted",
        "currentAuditLevel": "village",
        "submitTime": "2025-12-05 11:00:00"
      }
    ]
  }
}

说明:
- 自动根据当前用户的审核层级和行政区划过滤
- 只显示状态为 submitted 且当前审核层级匹配的需求
```

#### 2.3 审核通过 (提交到下一级)
```http
POST /seed/demand/audit/approve
Content-Type: application/json

{
  "ids": [
    "e5e7d8e5824f11dcf2eb5317104061c1",
    "83c36b293c63242681281498b4245723"
  ],
  "remark": "审核意见(可选)"
}

响应:
{
  "code": 200,
  "msg": "Approved successfully",
  "data": {
    "successCount": 2,
    "failCount": 0
  }
}

说明:
- 村级审核通过 → current_audit_level = town
- 镇级审核通过 → current_audit_level = district
- 区级审核通过 → current_audit_level = state
- 州级审核通过 → current_audit_level = ministry
- 部级审核通过 → status = approved, current_audit_level = null
```

#### 2.4 审核驳回 (退回DA修改)
```http
POST /seed/demand/audit/reject
Content-Type: application/json

{
  "ids": [
    "e5e7d8e5824f11dcf2eb5317104061c1"
  ],
  "auditOpinion": "需要修改的内容(必填)",
  "remark": "备注(可选)"
}

响应:
{
  "code": 200,
  "msg": "Rejected successfully",
  "data": {
    "successCount": 1,
    "failCount": 0
  }
}

说明:
- 驳回后 status = rejected
- 清空 current_audit_level
- DA可以重新编辑并提交
```

#### 2.5 部级锁定 (最终确认)
```http
POST /seed/demand/audit/lock
Content-Type: application/json

{
  "batchId": "0f18015bc6b9a6fe19f14f8d2ba04139"
}

响应:
{
  "code": 200,
  "msg": "Batch locked successfully"
}

说明:
- 只能由部级(ministry)用户执行
- 批次状态必须为 reviewing
- 批次内所有需求必须为 approved 状态
- 锁定后批次状态变为 locked
- 所有需求状态变为 locked (不可再修改)
```

---

## 📊 状态说明

### 需求状态 (status)

| 状态 | 代码 | 说明 |
|-----|------|------|
| 草稿 | draft | DA正在编辑 |
| 已提交 | submitted | 正在审核流程中 |
| 已通过 | approved | 部级审核通过 |
| 已驳回 | rejected | 被某级审核员驳回,需DA修改 |
| 已锁定 | locked | 部级最终确认,不可修改 |

### 审核层级 (current_audit_level)

| 层级 | 代码 | 说明 |
|-----|------|------|
| 村级 | village | Kebele |
| 镇级 | town | Woreda |
| 区级 | district | Zone |
| 州级 | state | Region |
| 部级 | ministry | Ministry |

---

## 🔍 常见场景示例

### 场景1: DA录入并提交审核

```bash
# 1. DA新增需求
POST /seed/demand/farmer/add
{
  "farmerName": "张三",
  "landArea": 10,
  "inputItems": [...]
}
# 得到 id: "abc123"

# 2. DA提交审核
POST /seed/demand/audit/submit
{
  "ids": ["abc123"]
}

# 3. 查看状态
GET /seed/demand/farmer/detail?id=abc123
# status: submitted
# current_audit_level: village
```

### 场景2: 村级审核员审核

```bash
# 1. 查询待审核列表(自动过滤本村数据)
POST /seed/demand/audit/pending/page
{
  "pageNum": 1,
  "pageSize": 10
}

# 2. 审核通过
POST /seed/demand/audit/approve
{
  "ids": ["abc123"],
  "remark": "村级审核通过"
}

# 3. 查看状态
GET /seed/demand/farmer/detail?id=abc123
# status: submitted
# current_audit_level: town (已提交到镇级)
```

### 场景3: 审核驳回后重新提交

```bash
# 1. 镇级审核员驳回
POST /seed/demand/audit/reject
{
  "ids": ["abc123"],
  "auditOpinion": "土地面积填写错误,请修改"
}

# 2. DA查看被驳回的需求
GET /seed/demand/farmer/detail?id=abc123
# status: rejected
# auditRecords: [...包含驳回记录]

# 3. DA修改需求
POST /seed/demand/farmer/update
{
  "id": "abc123",
  "landArea": 12  # 修改土地面积
}

# 4. DA重新提交
POST /seed/demand/audit/submit
{
  "ids": ["abc123"]
}

# 5. 重新进入审核流程
# status: submitted
# current_audit_level: village (从村级重新开始)
```

---

## 🚨 错误处理

### 常见错误码

```json
// 需求不存在
{
  "code": 500,
  "msg": "Demand not found"
}

// 状态不允许操作
{
  "code": 500,
  "msg": "Can only update demand in draft or rejected status"
}

// 无权限操作
{
  "code": 500,
  "msg": "Only the creator can update this demand"
}

// 审核层级不匹配
{
  "code": 500,
  "msg": "Audit level mismatch for demand"
}
```

---

## 📝 注意事项

1. **数据库表必须先修复**: 执行 `fix_demand_audit_record.sql` 创建正确的表结构
2. **权限控制**: 目前代码中使用 `current_user_id` 占位符,需要对接实际的用户认证系统
3. **批次管理**: 系统会自动按年份创建批次,批次号格式: `BATCH-{year}`
4. **审核记录**: 每次提交/审核/驳回操作都会生成审核记录,可在详情中查看完整历史
5. **乐观锁**: 编辑和删除操作使用版本号(version)防止并发冲突

---

## 🎯 前端开发建议

### 页面结构

```
投入品需求管理/
├── DA需求录入/
│   ├── 需求列表 (farmer/page)
│   ├── 新增需求 (farmer/add)
│   ├── 编辑需求 (farmer/update)
│   ├── 查看详情 (farmer/detail)
│   └── 提交审核 (audit/submit)
│
└── 需求审核/
    ├── 待审核列表 (audit/pending/page)
    ├── 审核详情 (farmer/detail)
    ├── 审核通过 (audit/approve)
    ├── 审核驳回 (audit/reject)
    └── 批次锁定 (audit/lock) - 仅部级
```

### 状态显示

```javascript
const statusConfig = {
  draft: { label: '草稿', color: 'info', actions: ['编辑', '删除', '提交'] },
  submitted: { label: '审核中', color: 'warning', actions: ['查看'] },
  approved: { label: '已通过', color: 'success', actions: ['查看'] },
  rejected: { label: '已驳回', color: 'danger', actions: ['编辑', '重新提交'] },
  locked: { label: '已锁定', color: 'default', actions: ['查看'] }
}

const levelConfig = {
  village: '村级审核中',
  town: '镇级审核中',
  district: '区级审核中',
  state: '州级审核中',
  ministry: '部级审核中'
}
```

---

## 📞 技术支持

如有问题,请查看:
- 后端代码: `e:\work\companyProject\igdp-service\igdp-seed`
- 数据库脚本: `src/main/resources/sql/demand_farmer.sql`
- 修复脚本: `fix_demand_audit_record.sql`
