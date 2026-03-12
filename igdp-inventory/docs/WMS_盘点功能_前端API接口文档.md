# 奥罗米亚 WMS — 库存盘点功能 前端 API 接口文档

> 本文档面向**前端开发人员**，包含盘点相关的所有接口规范。
> **注意**：基础访问路径为 `/inventory/stock-check`，后端统一采用 **CamelCase (小驼峰)** 的 JSON 命名规范。

---

## 接口清单

| # | 接口名称 | 方法 | 路径 | 说明 |
| --- | :--- | :--- | :--- | :--- |
| 1 | 获取盘点单列表 | GET | `/list` | 支持分页和条件过滤 |
| 2 | 获取单笔盘点详情 | GET | `/{checkId}` | 包含盘点头部与所有明细行 |
| 3 | 新建盘点单 | POST | `/create` | 提交暂存或草稿 |
| 4 | 编辑盘点单 | PUT | `/{checkId}` | 仅针对草稿或驳回单据 |
| 5 | 删除盘点单 | DELETE | `/{checkId}` | 物理删除（仅未提交的草稿可操作） |
| 6 | 提交盘点单审核 | POST | `/{checkId}/submit` | 将状态变更为 PENDING 待审核 |
| 7 | 取消盘点单 | POST | `/{checkId}/cancel` | 将状态变更为 CANCELLED |
| 8 | 审核通过 | POST | `/{checkId}/approve` | 将单据调整为 ADJUSTED 并自动扣库 |
| 9 | 审核驳回 | POST | `/{checkId}/reject` | 将单据变更为 REJECTED 退回修改 |
| 10 | 获取仓库当前库存 | GET | `/warehouse-inventory` | 【前端必调】获取底层库存单据渲染 |
| 11 | 查询仓库盘点状态 | GET | `/warehouse-status` | 查询某仓库当前是否允许进行出入库 |

---

## 全局通用响应格式

接口均包裹在此统一的 Rest 格式中返回（除了少数特殊的分页外）：

```json
{
  "code": 200,      // 200 为成功，其他非 200 均为错误或失败提示
  "msg": "操作成功",  // 提示信息 (注意有些普通接口键名叫 msg，有些叫 message)
  "data": { ... }   // 具体业务载荷
}
```

---

## 接口详细说明

### 1. 获取盘点单列表
**GET** `/inventory/stock-check/list`

**查询参数 (Query)**
| 参数 | 类型 | 必填 | 描述 |
| --- | --- | --- | --- |
| `warehouseId` | String | 否 | 仓库ID筛选 |
| `checkStatus` | String | 否 | 状态，支持多个英文逗号 `,` 分隔。枚举：DRAFT, PENDING, APPROVED, REJECTED, ADJUSTED, CANCELLED |
| `startDate` | String | 否 | 日期起，如 `2026-03-01` |
| `endDate` | String | 否 | 日期止，如 `2026-03-31` |
| `pageNum` | Int | 是 | 分页号，从 1 开始 |
| `pageSize` | Int | 是 | 页面大小 |

**响应体 (结构体不同于标准 Response，请注意)**
```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 50,          // 数据总条数
  "rows": [             // 注意这里的键是 rows 而不是 data
    {
      "checkId": "PD202603090001",
      "checkDate": "2026-03-09",
      "warehouseId": "WH123",
      "warehouseName": "西区主仓库",
      "checkerId": "U001",
      "checkerName": "张三",
      "checkStatus": "PENDING",    // 待审核
      "totalItems": 150,           // 盘点总行数 (自动聚合计算)
      "diffItems": 5,              // 异常(盈亏)条数 (自动聚合计算)
      "surplusItems": 3,           // 盘盈数目
      "lossItems": 2               // 盘亏数目
    }
  ]
}
```

---

### 2. 获取单笔盘点详情
**GET** `/inventory/stock-check/{checkId}`

**响应体**
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "checkId": "PD202603090001",
    "checkDate": "2026-03-09",
    "warehouseName": "西区主仓库",
    "checkStatus": "DRAFT",
    // ... 其他头部通用字段 ...
    "details": [
      {
        "id": 1024,
        "productId": "PROD-A",
        "productName": "尿素三号",
        "systemQty": 100.0,      // 系统应有库存数量
        "actualQty": 99.0,       // 盘点人员实盘填的数量
        "diffType": "LOSS",      // LOSS/SURPLUS/NONE
        "diffQty": -1.0,         // 结余差异数字
        "itemRemark": "包装破损1包"  // 单条备注
      }
    ]
  }
}
```

---

### 3. 新建盘点单 (草稿)
**POST** `/inventory/stock-check/create`

当用户在前端选定仓库后，应**先**通过接口 10 拉取到所有的库存档案，然后在表单里供用户输入 `actualQty`。在收集完输入提交本接口。

**请求体 (JSON)**
```json
{
  "checkDate": "2026-03-09",     // 必填
  "warehouseId": "WH123",        // 必填
  "checkRemark": "三月份春季全面盘点",
  "details": [
    {
      "productId": "PROD-A",     // 必填
      "batchNo": "BATCH2026",    // 必填
      "systemQty": 100.0,        // 必填，由前端从获取库存接口透传来
      "actualQty": 99.0,         // 可选，如果没有填传 null 或不传
      "unit": "KG",              // 必填
      "itemRemark": "破损"        // 可选
    }
  ]
}
```

**响应体**
```json
{
  "code": 200,
  "msg": "盘点单创建成功",
  "data": {
    "checkId": "PD202603090001"
  }
}
```

---

### 4. 编辑盘点草稿
**PUT** `/inventory/stock-check/{checkId}`

**请求体 (JSON)**
要求与新建基本一致，但 `details` 需要带有后端之前下发的 `id`，以便我们通过行主键精准局部更新。
```json
{
  "checkDate": "2026-03-10",
  "checkRemark": "更新后的备注说明",
  "details": [
    {
      "id": 1024,                // 必须带 id
      "actualQty": 100.0,
      "itemRemark": "重新清点后数据修复"
    }
  ]
}
```

---

### 5. 删除草稿盘点单
**DELETE** `/inventory/stock-check/{checkId}`
纯 RESTFul 物理删除请求，没有任何 body，返回 `{"code": 200}` 即删除成功。

---

### 6. 提交单据审核 (从 DRAFT -> PENDING)
**POST** `/inventory/stock-check/{checkId}/submit`

**请求体：无**
此接口触发系统的逻辑校验，如果有未填写的实盘数量 `actualQty` 会返回错误提示。

---

### 7. 取消盘点单
**POST** `/inventory/stock-check/{checkId}/cancel`
**请求体：无**

---

### 8. 审核盘点单 - 通过 (并触发库存调整)
**POST** `/inventory/stock-check/{checkId}/approve`

**请求体 (JSON)**
```json
{
  "reviewOpinion": "核对账目数据无误，同意调账。"  // 必填，理由
}
```
**注意**：接口通过后，该仓库实际库存将被直接篡改加减。

---

### 9. 审核盘点单 - 驳回
**POST** `/inventory/stock-check/{checkId}/reject`
**请求体 (JSON)**：同上，`reviewOpinion` （驳回理由）必填。

---

### 10. 获取指定仓库当前库存明细（核心底层接口）
**GET** `/inventory/stock-check/warehouse-inventory`

**说明**：在用户点击“新建盘点单”，并选择完下拉列表的某个仓库后，**立即调用该接口**得到这个仓库下存放的所有物品的明细作为底表（即系统真实库存），并使用以渲染成前端的可编辑 `table` 或 `List`。

**查询参数**
`?warehouseId=WH123`

**响应体**
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "warehouseId": "WH123",
    "items": [
      {
        "productId": "PROD-A",
        "productName": "尿素三号",
        "categoryMajor": "化肥",
        "categoryMinor": "氮肥",
        "batchNo": "BATCH2026",
        "unit": "KG",
        "qualityStatus": "AVAILABLE",   // 正常状态
        "currentQty": 100.0             // 前端拿其作为 systemQty 参数提交保存
      }
    ]
  }
}
```

---

### 11. 查询仓库防冲撞状态
**GET** `/inventory/stock-check/warehouse-status`

**查询参数**：`?warehouseId=WH123`

**响应体**
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "warehouseId": "WH123",
    "isChecking": true,      // true 代表库房锁死盘点中，前端在其他出入库操作界面应予以阻止
    "message": "该仓库正在盘点中，建议盘点完成后再操作"
  }
}
```
