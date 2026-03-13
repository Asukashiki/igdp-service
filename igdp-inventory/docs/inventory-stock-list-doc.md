# 库存查询列表接口

## 基本信息
- **接口路径**: `GET /inventory/stock/list`
- **所属模块**: igdp-inventory
- **Controller**: `InventoryStockController.list`
- **鉴权**: Sa-Token（`Authorization: Bearer <token>`）

## 请求参数
### Query 参数（过滤/分页）
> 接口接收 `InventoryStock` 作为查询条件，未传参数则分页返回全部数据。

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| pageNum | number | 否 | 页码（PageHelper 标准参数） |
| pageSize | number | 否 | 每页条数（PageHelper 标准参数） |
| warehouseId | number | 否 | 仓库 ID（SQL 过滤支持） |
| productName | string | 否 | 商品名称（SQL 过滤支持，模糊匹配） |
| warehouseName | string | 否 | 仓库名称（SQL 过滤支持，模糊匹配） |
| productId | number | 否 | 商品 ID（实体字段，当前 SQL 未显式过滤） |
| mainCategory | string | 否 | 主类目（实体字段，当前 SQL 未显式过滤） |
| subCategory | string | 否 | 子类目（实体字段，当前 SQL 未显式过滤） |
| availableQty | number | 否 | 可用库存（实体字段，当前 SQL 未显式过滤） |
| lockedQty | number | 否 | 锁定库存（实体字段，当前 SQL 未显式过滤） |
| qualityGrade | string | 否 | 质量等级（实体字段，当前 SQL 未显式过滤） |
| stockStatus | string | 否 | 库存状态（实体字段，当前 SQL 未显式过滤） |
| remark | string | 否 | 备注（实体字段，当前 SQL 未显式过滤） |
| unit | string | 否 | 单位（结果字段，当前 SQL 未显式过滤） |
| batchId | number | 否 | 批次 ID（结果字段，当前 SQL 未显式过滤） |
| batchNo | string | 否 | 批次号（结果字段，当前 SQL 未显式过滤） |

## 响应参数
### 返回结构
| 字段 | 类型 | 说明 |
| --- | --- | --- |
| code | number | 状态码（200 为成功） |
| msg | string | 提示信息 |
| total | number | 总记录数 |
| rows | array | 库存列表 |

### rows 字段（InventoryStock）
| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | number | 记录 ID |
| productId | number | 商品 ID |
| warehouseId | number | 仓库 ID |
| mainCategory | string | 主类目 |
| subCategory | string | 子类目 |
| availableQty | number | 可用库存 |
| lockedQty | number | 锁定库存 |
| qualityGrade | string | 质量等级 |
| stockStatus | string | 库存状态 |
| remark | string | 备注 |
| version | number | 乐观锁版本号 |
| productName | string | 商品名称（结果字段） |
| warehouseName | string | 仓库名称（结果字段） |
| unit | string | 单位（结果字段） |
| batchId | number | 批次 ID（结果字段） |
| batchNo | string | 批次号（结果字段） |
| createBy | string | 创建人（BaseEntity） |
| createTime | string | 创建时间（yyyy-MM-dd HH:mm:ss） |
| updateBy | string | 更新人（BaseEntity） |
| updateTime | string | 更新时间（yyyy-MM-dd HH:mm:ss） |

## 请求示例
```http
GET /inventory/stock/list?pageNum=1&pageSize=10&warehouseId=1 HTTP/1.1
Host: 127.0.0.1:9702
Authorization: Bearer 932e69e1-d3ed-4a4d-8bf8-7156092a3aca
```

## 响应示例（真实返回：当前环境）
```json
{"msg":"\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column 's.main_category' in 'field list'\n### The error may exist in file [/Users/kether/back/igdp-service/igdp-inventory/target/classes/mapper/inventory/InventoryStockMapper.xml]\n### The error may involve com.inspur.agriculture.inventory.mapper.InventoryStockMapper.selectStockList-Inline\n### The error occurred while setting parameters\n### SQL: SELECT s.id, s.sku_id, s.warehouse_id, s.available_qty, s.locked_qty, s.version,                s.create_by, s.create_time, s.update_by, s.update_time, s.remark,                s.main_category, s.sub_category,s.product_id,                k.sku_code, k.sku_name, p.product_name, w.warehouse_name,                lb.batch_id AS batch_id, lb.batch_no AS batch_no, p.unit AS unit         FROM inventory_stock s         LEFT JOIN inventory_sku k ON s.sku_id = k.id         LEFT JOIN inventory_product p ON k.product_id = p.id         LEFT JOIN inventory_warehouse w ON s.warehouse_id = w.id         LEFT JOIN (             SELECT b.sku_id AS sku_id, b.product_id AS product_id, b.warehouse_id AS warehouse_id,                    b.id AS batch_id, b.batch_no AS batch_no             FROM inventory_stock_batch b             INNER JOIN (                 SELECT sku_id, product_id, warehouse_id, MAX(id) AS max_id                 FROM inventory_stock_batch                 GROUP BY sku_id, product_id, warehouse_id             ) m ON b.id = m.max_id         ) lb ON s.sku_id = lb.sku_id AND p.id = lb.product_id AND s.warehouse_id = lb.warehouse_id  LIMIT ?\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column 's.main_category' in 'field list'","code":500}
```

