# 库存管理模块文档

## 模块概述

库存管理模块(Inventory Management)是农业投入品管理系统的核心模块之一,负责管理仓库、入库、出库和库存查询等功能。

### 主要功能

1. **仓库管理**: 管理仓库基本信息,支持普通仓库、冷藏仓库和危险品仓库
2. **入库管理**: 管理采购入库和退货入库,自动生成批次号和二维码
3. **出库管理**: 管理销售出库,支持批次管理和库存扣减
4. **库存查询**: 实时查询库存信息,支持多维度汇总和预警

## 技术架构

### 技术栈

- **Java**: 1.8
- **Spring Boot**: 2.7.6
- **MyBatis Plus**: 3.5.x
- **数据库**: MySQL 8.0 / HighGo / DM8

### 模块结构

```
igdp-agriculture-input/
├── src/main/java/com/inspur/agriculture/input/
│   ├── controller/inventory/         # 控制器层
│   │   ├── WarehouseController.java
│   │   ├── StockInController.java
│   │   ├── StockOutController.java
│   │   └── InventoryController.java
│   ├── service/inventory/            # 服务层
│   │   ├── IWarehouseService.java
│   │   ├── IStockInService.java
│   │   ├── IStockOutService.java
│   │   ├── IInventoryService.java
│   │   └── impl/
│   │       ├── WarehouseServiceImpl.java
│   │       ├── StockInServiceImpl.java
│   │       ├── StockOutServiceImpl.java
│   │       └── InventoryServiceImpl.java
│   ├── mapper/inventory/             # 数据访问层
│   │   ├── WarehouseMapper.java
│   │   ├── StockInMapper.java
│   │   ├── StockInItemMapper.java
│   │   ├── StockOutMapper.java
│   │   ├── StockOutItemMapper.java
│   │   └── InventoryMapper.java
│   ├── domain/inventory/             # 实体类
│   │   ├── Warehouse.java
│   │   ├── StockIn.java
│   │   ├── StockInItem.java
│   │   ├── StockOut.java
│   │   ├── StockOutItem.java
│   │   └── Inventory.java
│   ├── dto/inventory/                # 数据传输对象
│   │   ├── WarehouseDTO.java
│   │   ├── WarehouseQueryDTO.java
│   │   ├── StockInDTO.java
│   │   ├── StockInQueryDTO.java
│   │   ├── StockOutDTO.java
│   │   ├── StockOutQueryDTO.java
│   │   └── InventoryQueryDTO.java
│   └── vo/inventory/                 # 视图对象
│       ├── WarehouseVO.java
│       ├── StockInVO.java
│       ├── StockOutVO.java
│       └── InventoryVO.java
├── src/main/resources/mapper/agriculture/inventory/
│   ├── WarehouseMapper.xml
│   ├── StockInMapper.xml
│   ├── StockInItemMapper.xml
│   ├── StockOutMapper.xml
│   ├── StockOutItemMapper.xml
│   └── InventoryMapper.xml
└── sql/                              # 数据库脚本
    ├── inv_warehouse.sql
    ├── inv_stock_in.sql
    ├── inv_stock_in_item.sql
    ├── inv_stock_out.sql
    ├── inv_stock_out_item.sql
    └── inv_inventory.sql
```

## 数据库设计

### 表结构

#### 1. 仓库表 (inv_warehouse)

存储仓库基本信息。

| 字段名 | 类型 | 说明 |
|--------|------|------|
| warehouse_id | BIGINT | 仓库ID (主键,自增) |
| warehouse_code | VARCHAR(50) | 仓库编号 (唯一) |
| warehouse_name | VARCHAR(100) | 仓库名称 |
| warehouse_type | VARCHAR(20) | 仓库类型 (normal/cold/dangerous) |
| location | VARCHAR(255) | 仓库位置 |
| capacity | DECIMAL(15,2) | 仓库容量 |
| used_capacity | DECIMAL(15,2) | 已用容量 |
| supplier_id | BIGINT | 关联供应商ID |
| status | CHAR(1) | 状态 (0-停用/1-启用) |

#### 2. 入库单表 (inv_stock_in)

存储入库单主表信息。

| 字段名 | 类型 | 说明 |
|--------|------|------|
| stock_in_id | VARCHAR(30) | 入库单号 (主键) |
| warehouse_id | BIGINT | 入库仓库ID |
| batch_no | VARCHAR(50) | 批次号 |
| supplier_id | BIGINT | 供应商ID |
| type | CHAR(1) | 入库类型 (0-采购/1-退货) |
| status | CHAR(1) | 状态 (0-未入库/1-已入库/2-作废) |
| total_quantity | INT | 总数量 |
| expired_time | DATE | 过期日期 |

#### 3. 入库商品明细表 (inv_stock_in_item)

存储入库单明细信息。

| 字段名 | 类型 | 说明 |
|--------|------|------|
| stock_in_item_id | VARCHAR(30) | 入库商品明细ID (主键) |
| stock_in_id | VARCHAR(30) | 入库单号 |
| input_id | BIGINT | 投入品ID |
| warehouse_id | BIGINT | 入库仓库ID |
| quantity | INT | 入库数量 |

#### 4. 出库单表 (inv_stock_out)

存储出库单主表信息。

| 字段名 | 类型 | 说明 |
|--------|------|------|
| stock_out_id | VARCHAR(30) | 出库单号 (主键) |
| warehouse_id | BIGINT | 出库仓库ID |
| batch_no | VARCHAR(50) | 批次号 |
| customer | VARCHAR(255) | 客户 |
| type | CHAR(1) | 出库类型 (0-销售) |
| status | CHAR(1) | 状态 (0-未出库/1-已出库/2-作废) |
| total_quantity | INT | 总数量 |

#### 5. 出库商品明细表 (inv_stock_out_item)

存储出库单明细信息。

| 字段名 | 类型 | 说明 |
|--------|------|------|
| stock_out_item_id | VARCHAR(30) | 出库商品明细ID (主键) |
| stock_out_id | VARCHAR(30) | 出库单号 |
| input_id | BIGINT | 投入品ID |
| warehouse_id | BIGINT | 出库仓库ID |
| batch_no | VARCHAR(50) | 批次号 |
| quantity | INT | 出库数量 |

#### 6. 库存表 (inv_inventory)

存储实时库存信息。

| 字段名 | 类型 | 说明 |
|--------|------|------|
| inventory_id | VARCHAR(30) | 库存记录ID (主键) |
| input_id | BIGINT | 投入品ID |
| batch_no | VARCHAR(50) | 批次号 |
| warehouse_id | BIGINT | 仓库ID |
| current_quantity | INT | 当前库存数量 |
| in_date | DATE | 入库日期 |
| expired_date | DATE | 过期日期 |
| stock_status | CHAR(1) | 库存状态 (0-正常/1-临期/2-过期) |

## API接口

### 基础路径

```
/inventory
```

### 1. 仓库管理接口

#### 1.1 查询仓库列表

**接口**: `GET /inventory/warehouse/list`

**请求参数**:
- page: 页码 (默认1)
- pageSize: 每页数量 (默认10)
- warehouseName: 仓库名称 (模糊查询)
- warehouseType: 仓库类型
- status: 状态

**响应示例**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "list": [...],
    "total": 10,
    "page": 1,
    "pageSize": 10
  }
}
```

#### 1.2 查询仓库详情

**接口**: `GET /inventory/warehouse/{warehouseId}`

#### 1.3 添加仓库

**接口**: `POST /inventory/warehouse`

**请求体**:
```json
{
  "warehouseName": "主仓库A",
  "warehouseType": "normal",
  "location": "北京市朝阳区XX路XX号",
  "capacity": 10000.00,
  "belongs": "XX科技有限公司",
  "supplierId": 10001,
  "contactPerson": "张三",
  "contactPhone": "13800138000"
}
```

#### 1.4 更新仓库

**接口**: `POST /inventory/warehouse/update`

#### 1.5 删除仓库

**接口**: `POST /inventory/warehouse/delete/{warehouseId}`

### 2. 入库管理接口

#### 2.1 查询入库单列表

**接口**: `GET /inventory/stock-in/list`

**请求参数**:
- page: 页码
- pageSize: 每页数量
- warehouseId: 仓库ID
- supplierId: 供应商ID
- type: 入库类型
- status: 状态

#### 2.2 查询入库单详情

**接口**: `GET /inventory/stock-in/{stockInId}`

#### 2.3 创建入库单

**接口**: `POST /inventory/stock-in`

**请求体**:
```json
{
  "warehouseId": 1,
  "supplierId": 10001,
  "type": "0",
  "operator": "李四",
  "expiredTime": "2026-11-26",
  "remarks": "采购订单入库",
  "items": [
    {
      "inputId": 1,
      "quantity": 500,
      "remarks": "农药A批量采购"
    }
  ]
}
```

#### 2.4 确认入库

**接口**: `POST /inventory/stock-in/{stockInId}/confirm`

### 3. 出库管理接口

#### 3.1 查询出库单列表

**接口**: `GET /inventory/stock-out/list`

#### 3.2 查询出库单详情

**接口**: `GET /inventory/stock-out/{stockOutId}`

#### 3.3 创建出库单

**接口**: `POST /inventory/stock-out`

**请求体**:
```json
{
  "warehouseId": 1,
  "type": "0",
  "operator": "王五",
  "customer": "XX农场",
  "remark": "销售给XX农场",
  "items": [
    {
      "inputId": 1,
      "batchNo": "BN-20251126-0001",
      "quantity": 100,
      "remarks": "农药A销售"
    }
  ]
}
```

#### 3.4 确认出库

**接口**: `POST /inventory/stock-out/{stockOutId}/confirm`

### 4. 库存查询接口

#### 4.1 查询库存列表

**接口**: `GET /inventory/stock/list`

**请求参数**:
- page: 页码
- pageSize: 每页数量
- warehouseId: 仓库ID
- inputId: 投入品ID
- batchNo: 批次号
- stockStatus: 库存状态

#### 4.2 查询库存详情

**接口**: `GET /inventory/stock/{inventoryId}`

#### 4.3 查询库存预警列表

**接口**: `GET /inventory/stock/warning`

**请求参数**:
- warehouseId: 仓库ID
- warningType: 预警类型 (all/nearExpiry/expired)

#### 4.4 按投入品汇总库存

**接口**: `GET /inventory/stock/summary/by-input`

**请求参数**:
- warehouseId: 仓库ID
- inputType: 投入品类型

#### 4.5 按仓库汇总库存

**接口**: `GET /inventory/stock/summary/by-warehouse`

**请求参数**:
- supplierId: 供应商ID

## 业务规则

### 1. 编号生成规则

- **仓库编号**: `WH-{yyyyMMdd}-{4位序号}` (如: WH-20251126-0001)
- **入库单号**: `SI-{yyyyMMdd}-{4位序号}` (如: SI-20251126-0001)
- **批次号**: `BN-{yyyyMMdd}-{4位序号}` (如: BN-20251126-0001)
- **出库单号**: `SO-{yyyyMMdd}-{4位序号}` (如: SO-20251126-0001)
- **库存记录ID**: `INV-{yyyyMMdd}-{4位序号}` (如: INV-20251126-0001)

### 2. 入库流程

1. **创建入库单**: 填写入库信息和商品明细,系统自动生成入库单号和批次号
2. **确认入库**: 确认后更新库存,入库单状态变为"已入库",不可修改

### 3. 出库流程

1. **创建出库单**: 填写出库信息和商品明细,指定批次号
2. **确认出库**: 系统校验库存是否充足,确认后扣减库存,出库单状态变为"已出库"

### 4. 库存管理规则

- **唯一性**: 同一投入品、同一批次、同一仓库只有一条库存记录
- **库存状态自动更新**:
  - 距过期日期 > 30天: 正常 (0)
  - 距过期日期 ≤ 30天且未过期: 临期 (1)
  - 已过期: 过期 (2)

## 部署说明

### 1. 数据库初始化

执行以下SQL脚本创建表结构:

```bash
mysql -u root -p < sql/inv_warehouse.sql
mysql -u root -p < sql/inv_stock_in.sql
mysql -u root -p < sql/inv_stock_in_item.sql
mysql -u root -p < sql/inv_stock_out.sql
mysql -u root -p < sql/inv_stock_out_item.sql
mysql -u root -p < sql/inv_inventory.sql
```

### 2. 配置说明

无需额外配置,使用现有的Nacos配置即可。

### 3. 启动应用

```bash
cd igdp-admin
mvn spring-boot:run
```

## 注意事项

1. **数据安全**: 所有接口需要进行身份认证和权限校验
2. **逻辑删除**: 删除操作为逻辑删除,del_flag设置为'2'
3. **并发控制**: 库存操作涉及并发场景,已使用事务保证数据一致性
4. **事务管理**: 入库确认、出库确认涉及多表操作,使用@Transactional保证事务
5. **日期时间**: 所有时间字段使用服务器时间,格式为 yyyy-MM-dd HH:mm:ss

## 常见问题

### Q1: 入库确认失败怎么办?

A: 检查仓库容量是否充足,确保入库单状态为"未入库"。

### Q2: 出库时提示库存不足?

A: 检查指定批次的库存数量是否充足,可能需要从其他批次出库。

### Q3: 如何查看临期商品?

A: 使用库存预警接口,设置warningType=nearExpiry即可查询临期商品。

## 联系方式

如有问题,请联系:
- 开发团队: dev@inspur.com
- 技术支持: support@inspur.com
