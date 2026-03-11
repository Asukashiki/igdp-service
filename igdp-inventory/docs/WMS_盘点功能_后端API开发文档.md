# 奥罗米亚 WMS — 库存盘点功能 后端 API 开发文档

> 本文档面向**后端开发人员**，包含数据库设计、接口规范、业务规则及联调说明。
> 前端文档请参阅《WMS_盘点功能_前端开发文档.md》。

---

## 一、业务背景   

库存盘点流程：**创建盘点单 → 录入实盘数据 → 提交审核 → 审核通过/驳回 → 库存自动调整**。

- 盘点类型固定为**全面盘点**（对指定仓库所有库存品进行核查）
- 盘点与审核**人员分离**，审核人不得与盘点人为同一人
- 审核通过后，系统**自动**触发库存调整，禁止人工修改

---

## 二、数据库设计

### 2.1 盘点主表 `t_stock_check`

> [!IMPORTANT]
> 使用**单表扁平化**设计：每行 = 一张盘点单中的一个商品明细，头部信息冗余到每行，避免多表关联复杂度。

```sql
CREATE TABLE t_stock_check (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '记录ID（主键）',
    check_id        VARCHAR(32)     NOT NULL               COMMENT '盘点单编号，格式：PD+yyyyMMdd+4位流水号，如 PD202603090001；同一次盘点所有行共享此值',
    check_date      DATE            NOT NULL               COMMENT '盘点日期（实际执行日期）',
    warehouse_id    VARCHAR(32)     NOT NULL               COMMENT '盘点仓库ID（关联仓库注册表）',
    warehouse_name  VARCHAR(100)    NOT NULL               COMMENT '盘点仓库名称（冗余）',
    checker_id      VARCHAR(32)     NOT NULL               COMMENT '盘点人ID（关联用户表）',
    checker_name    VARCHAR(50)     NOT NULL               COMMENT '盘点人姓名（冗余）',
    check_status    VARCHAR(20)     NOT NULL               COMMENT '盘点状态：DRAFT/PENDING/APPROVED/REJECTED/ADJUSTED/CANCELLED；同 check_id 所有行状态一致',
    check_remark    TEXT                                   COMMENT '整张盘点单的总体说明',

    -- 商品明细字段
    product_id      VARCHAR(32)     NOT NULL               COMMENT '商品ID',
    product_name    VARCHAR(100)    NOT NULL               COMMENT '商品名称（冗余）',
    category_major  VARCHAR(20)     NOT NULL               COMMENT '商品大类：化肥/种子/农产品/农药',
    category_minor  VARCHAR(50)     NOT NULL               COMMENT '商品小类：氮肥/玉米等',
    batch_no        VARCHAR(50)     NOT NULL               COMMENT '商品生产批次',
    unit            VARCHAR(20)     NOT NULL               COMMENT '计量单位',
    expiry_date     DATE                                   COMMENT '有效期',
    quality_status  VARCHAR(20)                            COMMENT '商品状态：AVAILABLE/RESERVED/DAMAGED',
    system_qty      DECIMAL(15,3)   NOT NULL               COMMENT '盘点时系统库存数量（自动读取，只读）',
    actual_qty      DECIMAL(15,3)                          COMMENT '实盘数量（盘点人填写）',
    diff_type       VARCHAR(10)                            COMMENT '差异类型：SURPLUS/LOSS/NONE（系统计算）',
    diff_qty        DECIMAL(15,3)                          COMMENT '差异数量 = actual_qty - system_qty（系统计算）',
    item_remark     TEXT                                   COMMENT '单条商品盘点说明',

    -- 审核字段
    reviewer_id     VARCHAR(32)                            COMMENT '审核人ID',
    reviewer_name   VARCHAR(50)                            COMMENT '审核人姓名（冗余）',
    review_date     DATE                                   COMMENT '审核日期',
    review_opinion  TEXT                                   COMMENT '审核意见',

    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (id),
    INDEX idx_check_id      (check_id),
    INDEX idx_warehouse_id  (warehouse_id),
    INDEX idx_check_status  (check_status),
    INDEX idx_check_date    (check_date),
    INDEX idx_checker_id    (checker_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存盘点明细表';
```

### 2.2 盘点单编号生成规则

```
格式：PD + yyyyMMdd + 4位流水号（当日重置）
示例：PD202603090001

实现：SELECT COUNT(*) FROM t_stock_check WHERE check_id LIKE 'PD20260309%'
     取最大流水号 +1，不足4位左补零
```

### 2.3 状态枚举

| 枚举值 | 中文 | 说明 |
| :--- | :--- | :--- |
| `DRAFT` | 草稿 | 初始状态，可编辑删除 |
| `PENDING` | 待审核 | 提交后等待审核，不可编辑 |
| `APPROVED` | 审核通过 | 仅为中间状态，系统立即触发调整 |
| `REJECTED` | 审核驳回 | 退回盘点人可重新编辑提交 |
| `ADJUSTED` | 已调整 | 库存调整完成，终态 |
| `CANCELLED` | 已取消 | 已取消，终态 |

---

## 三、接口清单

### 3.1 Base URL

```
/api/v1/inventory/stock-check
```

### 3.2 完整接口列表

| # | 接口名称 | 方法 | 路径 | 权限 |
| --- | :--- | :--- | :--- | :--- |
| 1 | 获取盘点单列表 | GET | `/list` | 盘点人/审核人 |
| 2 | 获取盘点单详情 | GET | `/{checkId}` | 盘点人/审核人 |
| 3 | 新建盘点单 | POST | `/create` | 盘点人 |
| 4 | 编辑盘点单 | PUT | `/{checkId}` | 盘点人 |
| 5 | 删除盘点单 | DELETE | `/{checkId}` | 盘点人（仅草稿） |
| 6 | 提交盘点单 | POST | `/{checkId}/submit` | 盘点人 |
| 7 | 取消盘点单 | POST | `/{checkId}/cancel` | 盘点人（仅草稿） |
| 8 | 审核通过 | POST | `/{checkId}/approve` | 审核人 |
| 9 | 审核驳回 | POST | `/{checkId}/reject` | 审核人 |
| 10 | 获取仓库当前库存 | GET | `/warehouse-inventory` | 盘点人 |

---

## 四、接口详细设计

### 4.1 获取盘点单列表

**GET** `/api/v1/inventory/stock-check/list`

**查询参数（Query Params）**

| 参数 | 类型 | 必填 | 说明 |
| :--- | :--- | :---: | :--- |
| `warehouseId` | String | ❌ | 筛选仓库 |
| `checkStatus` | String | ❌ | 状态枚举，多个用逗号分隔 |
| `startDate` | String | ❌ | 盘点日期起，格式 `YYYY-MM-DD` |
| `endDate` | String | ❌ | 盘点日期止 |
| `pageNum` | Integer | ✅ | 页码，从 1 开始 |
| `pageSize` | Integer | ✅ | 每页条数，默认 10 |

**响应体（按 `check_id` 聚合，每行代表一张盘点单）**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 25,
    "list": [
      {
        "checkId": "PD202603090001",
        "checkDate": "2026-03-09",
        "warehouseId": "WH003",
        "warehouseName": "卢梅合作社仓库",
        "checkerId": "U001",
        "checkerName": "Abebe Bekele",
        "checkStatus": "PENDING",
        "checkRemark": "3月例行盘点",
        "totalItems": 42,
        "diffItems": 5,
        "surplusItems": 2,
        "lossItems": 3,
        "createTime": "2026-03-09T10:30:00",
        "updateTime": "2026-03-09T11:00:00"
      }
    ]
  }
}
```

> [!NOTE]
> `totalItems`、`diffItems`、`surplusItems`、`lossItems` 通过 `GROUP BY check_id` 聚合计算得出，无需前端计算。

---

### 4.2 获取盘点单详情

**GET** `/api/v1/inventory/stock-check/{checkId}`

**响应体**

```json
{
  "code": 200,
  "data": {
    "checkId": "PD202603090001",
    "checkDate": "2026-03-09",
    "warehouseId": "WH003",
    "warehouseName": "卢梅合作社仓库",
    "checkerId": "U001",
    "checkerName": "Abebe Bekele",
    "checkStatus": "ADJUSTED",
    "checkRemark": "3月例行盘点",
    "reviewerId": "U010",
    "reviewerName": "Kebede Alemu",
    "reviewDate": "2026-03-10",
    "reviewOpinion": "核查无误，准予通过",
    "createTime": "2026-03-09T10:30:00",
    "updateTime": "2026-03-10T14:00:00",
    "details": [
      {
        "id": 1001,
        "productId": "P001",
        "productName": "氮肥（尿素）",
        "categoryMajor": "化肥",
        "categoryMinor": "氮肥",
        "batchNo": "B20260101",
        "unit": "千克",
        "expiryDate": "2027-12-31",
        "qualityStatus": "AVAILABLE",
        "systemQty": 1000.000,
        "actualQty": 998.500,
        "diffType": "LOSS",
        "diffQty": -1.500,
        "itemRemark": "包装轻微破损"
      }
    ]
  }
}
```

---

### 4.3 新建盘点单

**POST** `/api/v1/inventory/stock-check/create`

**请求体**

```json
{
  "checkDate": "2026-03-09",
  "warehouseId": "WH003",
  "checkRemark": "3月例行盘点",
  "details": [
    {
      "productId": "P001",
      "batchNo": "B20260101",
      "systemQty": 1000.000,
      "actualQty": 998.500,
      "unit": "千克",
      "itemRemark": "包装有轻微破损"
    }
  ]
}
```

> [!NOTE]
> `details` 中的 `systemQty` 由前端调用「获取仓库当前库存」接口后自动填充，后端需再次**校验 `systemQty` 与当前库存是否一致**（防止并发下的数据不一致）。

**后端处理逻辑**

1. 生成 `check_id`（PD + yyyyMMdd + 4位流水号）
2. 按 `warehouseId` + 当日 检查是否已存在 `DRAFT` 或 `PENDING` 状态的盘点单（规则 R005）
3. 根据 `productId` + `batchNo` 自动填充 `product_name`、`category_major`、`category_minor`、`expiry_date`、`quality_status`
4. 计算每行 `diff_type` 和 `diff_qty`
5. 初始状态设为 `DRAFT`
6. 批量插入所有明细行

**响应体**

```json
{
  "code": 200,
  "message": "盘点单创建成功",
  "data": { "checkId": "PD202603090001" }
}
```

---

### 4.4 编辑盘点单

**PUT** `/api/v1/inventory/stock-check/{checkId}`

> [!IMPORTANT]
> 仅允许编辑 `DRAFT` 或 `REJECTED` 状态的盘点单。

**请求体**（与新建相同结构，支持局部更新）

```json
{
  "checkDate": "2026-03-09",
  "checkRemark": "更新后的说明",
  "details": [
    {
      "id": 1001,
      "actualQty": 999.000,
      "itemRemark": "重新清点后数据已修正"
    }
  ]
}
```

**后端处理逻辑**

1. 校验状态为 `DRAFT` 或 `REJECTED`，否则返回 403
2. 更新头部信息
3. 按 `id` 更新对应明细行的 `actual_qty`、`item_remark`
4. **重新计算** 所有行的 `diff_type`、`diff_qty`

---

### 4.5 提交盘点单

**POST** `/api/v1/inventory/stock-check/{checkId}/submit`

**请求体**：无

**后端校验（按顺序）**

| 规则 | 校验内容 | 失败响应 |
| :--- | :--- | :--- |
| R003 | 所有明细行的 `actual_qty` 不为 null | `"请填写全部商品的实盘数量"` |
| R004 | `diff_type != 'NONE'` 的行，`item_remark` 不为空 | `"存在差异商品未填写盘点说明"` |
| R009 | 当前状态为 `DRAFT` 或 `REJECTED` | `"当前状态不允许提交"` |

**成功后处理**

- 更新全部同 `check_id` 行的 `check_status` 为 `PENDING`
- 更新 `update_time`

---

### 4.6 审核通过

**POST** `/api/v1/inventory/stock-check/{checkId}/approve`

**请求体**

```json
{
  "reviewOpinion": "核查无误，准予通过"
}
```

**后端处理逻辑（事务内执行）**

1. 校验状态为 `PENDING`，否则拒绝
2. 校验审核人与盘点人不为同一人（R006）
3. 将 `check_status` 更新为 `APPROVED`，写入审核人信息
4. 对每条 `diff_type != 'NONE'` 的明细行执行库存调整：
   - `SURPLUS`：`inventory.qty += diff_qty`，生成 `SURPLUS_IN` 调整记录
   - `LOSS`：`inventory.qty -= |diff_qty|`，生成 `LOSS_OUT` 调整记录
5. 所有调整记录同步写入溯源信息，流转类型为 `ADJUSTMENT`，关联 `check_id`
6. 全部完成后更新 `check_status` 为 `ADJUSTED`

> [!CAUTION]
> 步骤 3-6 须在**同一数据库事务**中执行。任何步骤失败需回滚，状态保持 `PENDING`，返回 `"库存调整异常，请联系管理员"`。

**响应体**

```json
{
  "code": 200,
  "message": "审核通过，库存已自动调整",
  "data": {
    "checkId": "PD202603090001",
    "checkStatus": "ADJUSTED",
    "adjustments": [
      {
        "adjustmentId": "ADJ202603090001",
        "productId": "P001",
        "batchNo": "B20260101",
        "adjustmentType": "LOSS_OUT",
        "adjustmentQty": 1.500,
        "beforeQty": 1000.000,
        "afterQty": 998.500
      }
    ]
  }
}
```

---

### 4.7 审核驳回

**POST** `/api/v1/inventory/stock-check/{checkId}/reject`

**请求体**

```json
{
  "reviewOpinion": "实盘数据与现场照片不符，请重新核查第3、7行"
}
```

**校验**

- `reviewOpinion` 不能为空（R007）
- 状态必须为 `PENDING`

**处理**：`check_status` 更新为 `REJECTED`，不执行库存调整

---

### 4.8 获取仓库当前库存

**GET** `/api/v1/inventory/stock-check/warehouse-inventory`

**查询参数**

| 参数 | 类型 | 必填 | 说明 |
| :--- | :--- | :---: | :--- |
| `warehouseId` | String | ✅ | 仓库ID |

**响应体**（盘点表单初始化用，前端据此生成明细行）

```json
{
  "code": 200,
  "data": {
    "warehouseId": "WH003",
    "warehouseName": "卢梅合作社仓库",
    "items": [
      {
        "productId": "P001",
        "productName": "氮肥（尿素）",
        "categoryMajor": "化肥",
        "categoryMinor": "氮肥",
        "batchNo": "B20260101",
        "unit": "千克",
        "expiryDate": "2027-12-31",
        "qualityStatus": "AVAILABLE",
        "currentQty": 1000.000
      }
    ]
  }
}
```

---

## 五、通用响应规范

### 5.1 响应结构

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

### 5.2 错误码

| code | 说明 |
| :--- | :--- |
| `200` | 成功 |
| `400` | 请求参数错误（含业务校验失败） |
| `401` | 未登录/Token 失效 |
| `403` | 无权限操作 |
| `404` | 资源不存在 |
| `409` | 状态冲突（如提交已提交的单） |
| `500` | 服务器内部错误 |

### 5.3 分页响应结构

```json
{
  "total": 100,
  "list": []
}
```

---

## 六、业务规则汇总

| 编号 | 规则 | 触发时机 | 违反时响应码 |
| :--- | :--- | :--- | :--- |
| R001 | `actual_qty >= 0` | 新建/编辑 | 400 |
| R002 | `actual_qty` 最多3位小数 | 新建/编辑 | 400 |
| R003 | 所有明细行 `actual_qty` 不为空 | 提交 | 400 |
| R004 | 有差异的行 `item_remark` 不为空 | 提交 | 400 |
| R005 | 同仓库同日不得有两笔 `DRAFT`/`PENDING` 全面盘点 | 新建 | 409 |
| R006 | 审核人 ≠ 盘点人 | 审核通过/驳回 | 403 |
| R007 | 驳回时 `reviewOpinion` 不为空 | 审核驳回 | 400 |
| R008 | 审核通过后自动生成库存调整记录，不可人工修改 | 审核通过 | — |
| R009 | 非 `DRAFT`/`REJECTED` 状态不可编辑或删除 | 编辑/删除 | 403 |
| R010 | 仓库处于盘点中时，出入库操作需返回提示标识 | 出入库接口 | — |

---

## 七、权限矩阵

| 角色 | 新建 | 编辑/删除 | 提交 | 审核 | 查询范围 |
| :--- | :---: | :---: | :---: | :---: | :--- |
| 合作社仓管员 | ✅ | ✅（仅自建） | ✅ | ❌ | 仅自身仓库 |
| 联盟仓管员 | ✅ | ✅（仅自建） | ✅ | ❌ | 联盟及下属仓库 |
| 合作社管理员 | ❌ | ❌ | ❌ | ✅（仅自身仓库） | 自身仓库 |
| 联盟管理员 | ❌ | ❌ | ❌ | ✅（联盟及下属） | 联盟及下属 |
| OAB 管理员 | ✅ | ✅ | ✅ | ✅ | 所有仓库 |
| OSE 管理员 | ✅ | ✅ | ✅ | ✅ | 仅种子仓库 |

数据过滤建议通过 interceptor/AOP 在 Service 层注入 `warehouseIds` 白名单，SQL 层使用 `WHERE warehouse_id IN (...)` 实现。

---

## 八、与其他模块的集成接口

### 8.1 盘点中状态查询（供出入库模块调用）

**GET** `/api/v1/inventory/stock-check/warehouse-status?warehouseId={warehouseId}`

```json
{
  "code": 200,
  "data": {
    "warehouseId": "WH003",
    "isChecking": true,
    "checkId": "PD202603090001",
    "message": "该仓库正在盘点中，建议盘点完成后再操作"
  }
}
```

### 8.2 溯源信息同步（库存调整时内部调用）

审核通过生成的调整记录需同步写入溯源表，字段映射：

| 溯源字段 | 盘点来源 |
| :--- | :--- |
| `transfer_type` | `ADJUSTMENT` |
| `related_order_id` | `check_id` |
| `product_id` | `product_id` |
| `batch_no` | `batch_no` |

---

## 九、并发与性能

| 场景 | 方案 |
| :--- | :--- |
| 多人同时编辑同一盘点单 | 乐观锁：`t_stock_check` 增加 `version` 字段，更新时校验 |
| 审核时库存已发生变化 | 审核接口返回 `latestQty`（最新库存），由前端提示审核人注意 |
| 大仓库库存品数量多 | `warehouse-inventory` 接口支持 `pageSize` 或一次性返回（由前端决定加载策略） |

---

## 十、联调说明

| 事项 | 说明 |
| :--- | :--- |
| **接口文档** | 后端需提供 Swagger/OpenAPI 文档 |
| **Mock 环境** | 前端开发期间使用 Mock 数据，Mock 结构与本文档响应体一致 |
| **字段命名** | 响应字段统一使用 **camelCase** |
| **时间格式** | 日期使用 `YYYY-MM-DD`，时间使用 `YYYY-MM-DDTHH:mm:ss` |
| **跨域** | 后端配置 CORS 允许前端开发服务器 origin（`http://localhost:8080`） |
| **分页** | 列表接口统一支持 `pageNum`（从1起）+ `pageSize`，响应包含 `total` |
