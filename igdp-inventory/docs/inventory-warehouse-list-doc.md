# 仓库查询列表接口

## 基本信息
- **接口路径**: `GET /inventory/inventory-warehouse/list`
- **所属模块**: igdp-inventory
- **Controller**: `InventoryWarehouseController.list`
- **鉴权**: Sa-Token（`Authorization: Bearer <token>`）

## 请求参数
### Query 参数（过滤/分页）
> 该接口接收 `InventoryWarehouse` 作为查询条件，未传参数则返回全部（分页后）数据。

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| pageNum | number | 否 | 页码（PageHelper 标准参数） |
| pageSize | number | 否 | 每页条数（PageHelper 标准参数） |
| id | number | 否 | 仓库 ID |
| warehouseCode | string | 否 | 仓库编码（模糊匹配） |
| warehouseName | string | 否 | 仓库名称（模糊匹配） |
| type | string | 否 | 仓库类型（精确匹配） |
| address | string | 否 | 地址（模糊匹配） |
| status | string | 否 | 状态（精确匹配） |
| storeType | string | 否 | 存储类型（实体字段，当前实现未显式过滤） |
| orgName | string | 否 | 机构名称（实体字段，当前实现未显式过滤） |
| adminLevel | string | 否 | 行政级别（实体字段，当前实现未显式过滤） |
| parentId | number | 否 | 上级仓库 ID（实体字段，当前实现未显式过滤） |
| location | string | 否 | 位置（实体字段，当前实现未显式过滤） |
| capacity | number | 否 | 容量（实体字段，当前实现未显式过滤） |
| remark | string | 否 | 备注（实体字段，当前实现未显式过滤） |

## 响应参数
### 返回结构
| 字段 | 类型 | 说明 |
| --- | --- | --- |
| code | number | 状态码（200 为成功） |
| msg | string | 提示信息 |
| total | number | 总记录数 |
| rows | array | 仓库列表 |

### rows 字段（InventoryWarehouse）
| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | number | 仓库 ID |
| warehouseCode | string | 仓库编码 |
| warehouseName | string | 仓库名称 |
| type | string | 仓库类型 |
| storeType | string | 存储类型 |
| orgName | string | 机构名称 |
| adminLevel | string | 行政级别 |
| parentId | number | 上级仓库 ID |
| location | string | 位置 |
| capacity | number | 容量 |
| address | string | 地址 |
| status | string | 状态 |
| remark | string | 备注 |
| parentWarehouseName | string | 上级仓库名称（非持久字段） |
| createBy | string | 创建人（BaseEntity） |
| createTime | string | 创建时间（yyyy-MM-dd HH:mm:ss） |
| updateBy | string | 更新人（BaseEntity） |
| updateTime | string | 更新时间（yyyy-MM-dd HH:mm:ss） |

## 请求示例
```http
GET /inventory/inventory-warehouse/list?pageNum=1&pageSize=10&warehouseName=Warehouse HTTP/1.1
Host: 127.0.0.1:9702
Authorization: Bearer 932e69e1-d3ed-4a4d-8bf8-7156092a3aca
```

## 响应示例（真实返回）
```json
{"total":4,"rows":[{"createBy":"superAdmin","createTime":"2026-03-11 17:27:36","updateBy":"superAdmin","updateTime":"2026-03-12 09:10:00","id":5,"warehouseCode":"ZY_20260311_001","warehouseName":"ceshi","type":"ZY","storeType":"fertilizer","orgName":"OAB","adminLevel":"province","parentId":4,"location":"ceshiceshi","capacity":1000.00,"address":"ceshiceshiceshiceshi","status":"0","remark":"ceshiceshiceshiceshiceshiceshi","parentWarehouseName":"Cooperative Warehouse A","beginTime":null,"endTime":null},{"createBy":"admin","createTime":"2026-03-10 09:12:44","updateBy":"","updateTime":null,"id":4,"warehouseCode":"WH004","warehouseName":"Cooperative Warehouse A","type":"COOPERATIVE","storeType":"Seed","orgName":"Farmers Cooperative Union A","adminLevel":"COUNTY","parentId":2,"location":"Adama","capacity":2000.00,"address":"Adama, Agricultural Zone","status":"0","remark":"Cooperative storage facility","parentWarehouseName":"East Region Warehouse","beginTime":null,"endTime":null},{"createBy":"admin","createTime":"2026-03-10 09:12:44","updateBy":"","updateTime":null,"id":3,"warehouseCode":"WH003","warehouseName":"West Region Warehouse","type":"REGIONAL","storeType":"Fertilizer,Pesticide","orgName":"West Region Agriculture Office","adminLevel":"CITY","parentId":1,"location":"Nekemte","capacity":4000.00,"address":"Nekemte, Commercial Area","status":"0","remark":"West region distribution center","parentWarehouseName":null,"beginTime":null,"endTime":null},{"createBy":"admin","createTime":"2026-03-10 09:12:44","updateBy":"","updateTime":null,"id":2,"warehouseCode":"WH002","warehouseName":"East Region Warehouse","type":"REGIONAL","storeType":"Fertilizer,Seed","orgName":"East Region Agriculture Office","adminLevel":"CITY","parentId":1,"location":"Dire Dawa","capacity":5000.00,"address":"Dire Dawa, Industrial Zone","status":"0","remark":"East region distribution center","parentWarehouseName":null,"beginTime":null,"endTime":null}],"code":200,"msg":"查询成功"}
```
