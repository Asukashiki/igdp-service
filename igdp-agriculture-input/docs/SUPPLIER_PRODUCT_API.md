# 供应商投入品信息管理 API 文档

## 基础路径
```
/supplier/product
```

---

## 接口列表

### 1. 查询供应商投入品列表

#### 接口信息
- **接口路径**: `/supplier/product/list`
- **请求方法**: `GET`
- **接口描述**: 查询供应商投入品关系列表，支持多条件筛选和分页
- **使用对象**: 供应商、供应商信息维护人员、系统管理员

#### 请求参数（Query参数）

| 字段名 | 数据类型 | 是否必填 | 描述 | 默认值 | 示例值 |
|--------|---------|---------|------|--------|--------|
| page | Integer | 否 | 页码 | 1 | 1 |
| pageSize | Integer | 否 | 每页数量 | 10 | 10 |
| supplierId | Long | 否 | 供应商ID（精确查询） | - | 10001 |
| inputType | String | 否 | 投入品类型（精确查询） | - | pesticide |
| inputName | String | 否 | 投入品名称（模糊查询） | - | 农药 |
| inputSku | String | 否 | 投入品编码（精确查询） | - | SKU-001 |
| supplierProductCode | String | 否 | 供应商产品编码（模糊查询） | - | SP-001 |
| qualityRating | String | 否 | 质量评级（精确查询） | - | A |
| keyword | String | 否 | 关键词搜索 | - | 优质 |

#### 投入品类型说明
- `pesticide`: 农药
- `fertilizer`: 化肥
- `seed`: 种子
- `other`: 其他

#### 质量评级说明
- `A`: 优秀
- `B`: 良好
- `C`: 一般
- `D`: 较差

#### 请求示例

```
GET /supplier/product/list?page=1&pageSize=10&supplierId=10001&inputType=pesticide
```

#### 响应示例（成功）

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "list": [
      {
        "supplierProductId": 1,
        "supplierId": 10001,
        "supplierName": "XX科技有限公司",
        "inputId": 1,
        "inputName": "农药A",
        "inputType": "pesticide",
        "inputSku": "SKU-001",
        "supplierProductCode": "SP-001",
        "supplierProductName": "优质农药A",
        "qualityRating": "A",
        "currentPrice": "100.00",
        "certStatus": 2,
        "certStatusDesc": "已通过",
        "notes": "高质量农药产品",
        "createPeople": "system",
        "createTime": "2025-11-25 10:00:00",
        "updatePeople": null,
        "updateTime": null
      }
    ],
    "total": 1,
    "page": 1,
    "pageSize": 10
  }
}
```

---

### 2. 查询供应商投入品详情

#### 接口信息
- **接口路径**: `/supplier/product/{supplierProductId}`
- **请求方法**: `GET`
- **接口描述**: 根据供应关系ID查询供应商投入品详细信息
- **使用对象**: 供应商、供应商信息维护人员、系统管理员

#### 请求参数

| 类型 | 字段名 | 数据类型 | 是否必填 | 描述 | 示例值 |
|------|--------|---------|---------|------|--------|
| 路径参数 | supplierProductId | Long | 是 | 供应关系ID | 1 |

#### 请求示例

```
GET /supplier/product/1
```

#### 响应示例（成功）

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "supplierProductId": 1,
    "supplierId": 10001,
    "supplierName": "XX科技有限公司",
    "inputId": 1,
    "inputName": "农药A",
    "inputType": "pesticide",
    "inputSku": "SKU-001",
    "supplierProductCode": "SP-001",
    "supplierProductName": "优质农药A",
    "qualityRating": "A",
    "currentPrice": "100.00",
    "certStatus": 2,
    "certStatusDesc": "已通过",
    "notes": "高质量农药产品",
    "createPeople": "system",
    "createTime": "2025-11-25 10:00:00",
    "updatePeople": null,
    "updateTime": null
  }
}
```

---

### 3. 添加供应商投入品关系

#### 接口信息
- **接口路径**: `/supplier/product`
- **请求方法**: `POST`
- **接口描述**: 添加供应商与投入品的关联关系
- **使用对象**: 供应商信息维护人员、系统管理员

#### 请求参数（Body - JSON）

| 字段名 | 数据类型 | 是否必填 | 描述 | 校验规则 | 示例值 |
|--------|---------|---------|------|---------|--------|
| supplierId | Long | 是 | 供应商ID | 必须是已审核通过的供应商 | 10001 |
| inputId | Long | 是 | 投入品ID | 必须是系统中的有效投入品 | 1 |
| supplierProductCode | String | 否 | 供应商产品编码 | 最大长度100字符 | SP-001 |
| supplierProductName | String | 否 | 供应商产品名称 | 最大长度200字符 | 优质农药A |
| qualityRating | String | 否 | 质量评级 | A/B/C/D | A |
| notes | String | 否 | 供应备注 | 最大500字符 | 高质量产品 |

#### 请求示例

```json
{
  "supplierId": 10001,
  "inputId": 1,
  "supplierProductCode": "SP-001",
  "supplierProductName": "优质农药A",
  "qualityRating": "A",
  "notes": "高质量农药产品"
}
```

#### 响应示例（成功）

```json
{
  "code": 200,
  "msg": "添加成功",
  "data": null
}
```

#### 错误示例

```json
{
  "code": 500,
  "msg": "该供应商与投入品的关联已存在",
  "data": null
}
```

---

### 4. 更新供应商投入品关系

#### 接口信息
- **接口路径**: `/supplier/product/{supplierProductId}`
- **请求方法**: `PUT`
- **接口描述**: 更新供应商与投入品的关联关系信息
- **使用对象**: 供应商信息维护人员、系统管理员

#### 请求参数

| 类型 | 字段名 | 数据类型 | 是否必填 | 描述 | 示例值 |
|------|--------|---------|---------|------|--------|
| 路径参数 | supplierProductId | Long | 是 | 供应关系ID | 1 |
| Body参数 | supplierId | Long | 是 | 供应商ID | 10001 |
| Body参数 | inputId | Long | 是 | 投入品ID | 1 |
| Body参数 | supplierProductCode | String | 否 | 供应商产品编码 | SP-001 |
| Body参数 | supplierProductName | String | 否 | 供应商产品名称 | 优质农药A |
| Body参数 | qualityRating | String | 否 | 质量评级 | A |
| Body参数 | notes | String | 否 | 供应备注 | 高质量产品 |

#### 请求示例

```json
PUT /supplier/product/1

{
  "supplierId": 10001,
  "inputId": 1,
  "supplierProductCode": "SP-001-V2",
  "supplierProductName": "优质农药A（升级版）",
  "qualityRating": "A",
  "notes": "升级后的高质量农药产品"
}
```

#### 响应示例（成功）

```json
{
  "code": 200,
  "msg": "更新成功",
  "data": null
}
```

---

### 5. 删除供应商投入品关系

#### 接口信息
- **接口路径**: `/supplier/product/{supplierProductId}`
- **请求方法**: `DELETE`
- **接口描述**: 删除供应商与投入品的关联关系（逻辑删除）
- **使用对象**: 供应商信息维护人员、系统管理员

#### 请求参数

| 类型 | 字段名 | 数据类型 | 是否必填 | 描述 | 示例值 |
|------|--------|---------|---------|------|--------|
| 路径参数 | supplierProductId | Long | 是 | 供应关系ID | 1 |

#### 请求示例

```
DELETE /supplier/product/1
```

#### 响应示例（成功）

```json
{
  "code": 200,
  "msg": "删除成功",
  "data": null
}
```

---

### 6. 批量删除供应商投入品关系

#### 接口信息
- **接口路径**: `/supplier/product/batch`
- **请求方法**: `DELETE`
- **接口描述**: 批量删除供应商与投入品的关联关系（逻辑删除）
- **使用对象**: 供应商信息维护人员、系统管理员

#### 请求参数（Body - JSON）

| 字段名 | 数据类型 | 是否必填 | 描述 | 示例值 |
|--------|---------|---------|------|--------|
| supplierProductIds | Long[] | 是 | 供应关系ID数组 | [1, 2, 3] |

#### 请求示例

```json
DELETE /supplier/product/batch

[1, 2, 3]
```

#### 响应示例（成功）

```json
{
  "code": 200,
  "msg": "批量删除成功",
  "data": null
}
```

---

## 数据字典

### 投入品类型（inputType）

| 值 | 描述 |
|----|------|
| pesticide | 农药 |
| fertilizer | 化肥 |
| seed | 种子 |
| other | 其他 |

### 质量评级（qualityRating）

| 值 | 描述 |
|----|------|
| A | 优秀 |
| B | 良好 |
| C | 一般 |
| D | 较差 |

### 认证状态（certStatus）

| 值 | 描述 |
|----|------|
| 0 | 未通过 |
| 1 | 审核中 |
| 2 | 已通过 |

### 删除标志（delFlag）

| 值 | 描述 |
|----|------|
| 0 | 正常 |
| 2 | 删除 |

---

## 业务规则说明

### 1. 唯一性约束
- 同一供应商对同一投入品只能有一条有效的供应记录
- 唯一性约束：`(supplier_id, input_id, del_flag='0')`
- 添加或编辑时会自动检查是否违反此约束

### 2. 关联关系
- `supplierId` 关联 `supplier_cert` 表的 `user_id`（供应商用户ID）
- `inputId` 关联 `agri_input` 表的 `input_id`（投入品ID）
- 查询时自动关联获取供应商名称、投入品信息、认证状态等

### 3. 逻辑删除
- 删除操作为逻辑删除，`del_flag` 设置为 '2'
- 查询时自动过滤已删除记录（`del_flag='0'`）
- 删除后释放唯一约束，允许重新建立关联

### 4. 筛选功能
- **模糊查询字段**: inputName、supplierProductCode
- **精确查询字段**: supplierId、inputType、inputSku、qualityRating
- **关键词搜索**: keyword 参数会同时搜索投入品名称、供应商产品名称、供应商产品编码
- 所有筛选条件之间是 AND 关系

---

## 错误码说明

| 错误码 | 描述 |
|--------|------|
| 200 | 操作成功 |
| 500 | 系统错误或业务异常 |

---

## 注意事项

1. **唯一性校验**：
   - 添加时检查供应商和投入品的关联是否已存在
   - 编辑时检查供应商和投入品的关联是否与其他记录重复（排除自己）

2. **数据完整性**：
   - supplierId 必须是已审核通过的供应商（certStatus=2）
   - inputId 必须是系统中的有效投入品（delFlag='0'）

3. **质量评级**：
   - 可选值为 A/B/C/D
   - A表示优秀，B表示良好，C表示一般，D表示较差

4. **分页查询**：
   - 默认按创建时间倒序排列
   - 支持多条件组合筛选

5. **关联查询**：
   - 列表查询自动关联供应商名称、投入品信息、认证状态
   - 提升查询效率，减少前端二次查询

---

## 使用示例

### 示例1：查询某供应商的所有投入品

```
GET /supplier/product/list?supplierId=10001&page=1&pageSize=10
```

### 示例2：查询某类型的投入品供应关系

```
GET /supplier/product/list?inputType=pesticide&page=1&pageSize=10
```

### 示例3：使用关键词搜索

```
GET /supplier/product/list?keyword=优质&page=1&pageSize=10
```

### 示例4：查询指定质量评级的供应关系

```
GET /supplier/product/list?qualityRating=A&page=1&pageSize=10
```
