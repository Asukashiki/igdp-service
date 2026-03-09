# 农业物流平台库存管理系统 (igdp-inventory) 开发架构文档

## 1. 项目概述
本项目旨在将原 `igdp-agriculture-input` 模块中的库存管理功能剥离，构建独立的 `igdp-inventory` 库存中心模块。该模块将作为统一的库存服务中心，提供高并发、高可用的库存管理能力，支持多仓库、多批次、SKU 维度的精细化管理。

---

## 2. 技术架构

### 2.1 技术栈
| 技术组件 | 版本 | 说明 |
| :--- | :--- | :--- |
| **Java** | 8 | 基础开发语言 |
| **Spring Boot** | 2.7.10 | 核心框架 |
| **MyBatis-Plus** | 3.5.3.1 | ORM 框架 |
| **MySQL** | 8.0 | 关系型数据库 |
| **Redis** | 6.x | 缓存与分布式锁 |
| **Spring Security** | RuoYi内置 | 认证与授权 |
| **RuoYi** | V4.7.5 | 基础开发平台 |

### 2.2 系统分层架构
```mermaid
graph TD
    Client[前端/第三方系统] --> Controller[Controller 层 (Web API)]
    Controller --> Service[Service 层 (业务逻辑)]
    Service --> InventoryCore[InventoryCoreService (库存核心服务)]
    InventoryCore --> Mapper[Mapper 层 (数据访问)]
    Mapper --> MySQL[(MySQL 8.0)]
    InventoryCore -.-> Redis[(Redis 缓存/锁)]
```

### 2.3 包结构规范
基础包路径：`com.inspur.agriculture.inventory`

| 包名 | 说明 |
| :--- | :--- |
| `config` | 配置类 (MyBatisPlus配置, Redis配置等) |
| `constant` | 常量定义 (单据状态, 变更类型等) |
| `controller` | 控制层 (REST API) |
| `domain` | 实体类 (Entity, 继承 BaseEntity) |
| `dto` | 数据传输对象 (接收前端参数) |
| `vo` | 视图对象 (返回前端数据) |
| `mapper` | 数据访问接口 |
| `service` | 业务接口 |
| `service.impl` | 业务实现类 |
| `util` | 工具类 |

---

## 3. 数据库设计 (Database Schema)

**表前缀**: `inventory_`
**通用字段**: 所有表必须包含 `create_by`, `create_time`, `update_by`, `update_time`, `remark` (通过继承 `BaseEntity` 实现)。

### 3.1 基础数据表

#### 3.1.1 仓库表 (inventory_warehouse)
| 字段 | 类型 | 说明 | 备注 |
| :--- | :--- | :--- | :--- |
| id | bigint | 仓库ID | 主键 |
| warehouse_code | varchar(64) | 仓库编码 | 唯一 |
| warehouse_name | varchar(100)| 仓库名称 | |
| type | varchar(20) | 仓库类型 | 中央/联盟/合作社/企业 |
| address | varchar(255)| 地址 | |
| status | char(1) | 状态 | 0=正常, 1=停用 |

#### 3.1.2 商品表 (inventory_product)
| 字段 | 类型 | 说明 | 备注 |
| :--- | :--- | :--- | :--- |
| id | bigint | 商品ID | 主键 |
| product_code | varchar(64) | 商品编码 | 唯一 |
| product_name | varchar(100)| 商品名称 | |
| category_id | bigint | 分类ID | |
| category_name | varchar(50) | 分类名称 | 冗余字段 |
| status | char(1) | 状态 | 0=正常, 1=停用 |

#### 3.1.3 SKU表 (inventory_sku)
| 字段 | 类型 | 说明 | 备注 |
| :--- | :--- | :--- | :--- |
| id | bigint | SKU ID | 主键 |
| sku_code | varchar(64) | SKU编码 | 唯一 |
| sku_name | varchar(100)| SKU名称 | |
| product_id | bigint | 商品ID | 关联 inventory_product |
| unit | varchar(20) | 单位 | |
| spec | varchar(100)| 规格 | |
| status | char(1) | 状态 | 0=正常, 1=停用 |

### 3.2 核心库存表

#### 3.2.1 库存总表 (inventory_stock)
记录 SKU 在各仓库的总库存（聚合视图）。
**唯一索引**: `uk_sku_warehouse` (`sku_id`, `warehouse_id`)

| 字段 | 类型 | 说明 | 备注 |
| :--- | :--- | :--- | :--- |
| id | bigint | 主键 | |
| sku_id | bigint | SKU ID | |
| warehouse_id | bigint | 仓库ID | |
| available_qty | decimal(10,2)| 可用库存 | 实际可售/可领数量 |
| locked_qty | decimal(10,2)| 锁定库存 | 订单预占数量 |
| version | bigint | 版本号 | **乐观锁核心字段** |

#### 3.2.2 批次库存表 (inventory_stock_batch)
记录库存的批次明细（FIFO/FEFO 依据）。

| 字段 | 类型 | 说明 | 备注 |
| :--- | :--- | :--- | :--- |
| id | bigint | 主键 | |
| sku_id | bigint | SKU ID | |
| warehouse_id | bigint | 仓库ID | |
| batch_no | varchar(64) | 批次号 | |
| production_date| datetime | 生产日期 | |
| expire_date | datetime | 过期日期 | |
| qty | decimal(10,2)| 批次当前数量 | |

#### 3.2.3 库存流水表 (inventory_stock_log)
**必须记录所有变更**。

| 字段 | 类型 | 说明 | 备注 |
| :--- | :--- | :--- | :--- |
| id | bigint | 主键 | |
| sku_id | bigint | SKU ID | |
| warehouse_id | bigint | 仓库ID | |
| batch_no | varchar(64) | 批次号 | 可空（若非批次操作） |
| change_type | varchar(20) | 变更类型 | INBOUND, OUTBOUND, LOCK, RELEASE, ADJUST |
| change_qty | decimal(10,2)| 变更数量 | 正数增加，负数减少 |
| before_qty | decimal(10,2)| 变更前数量 | 记录 available_qty |
| after_qty | decimal(10,2)| 变更后数量 | |
| biz_type | varchar(50) | 业务类型 | 采购入库/销售出库/盘点 |
| biz_id | bigint | 业务单据ID | |
| biz_no | varchar(64) | 业务单号 | |

### 3.3 单据表

#### 3.3.1 入库单 (inventory_inbound) & 明细
- **主表**: `id`, `warehouse_id`, `type` (采购/归还/调拨), `status` (DRAFT/SUBMITTED/APPROVED/REJECTED), `order_date`
- **明细表**: `inbound_id`, `sku_id`, `batch_no`, `plan_qty`, `real_qty`

#### 3.3.2 出库单 (inventory_outbound) & 明细
- **主表**: `id`, `warehouse_id`, `type` (销售/领用/调拨), `status` (DRAFT/SUBMITTED/APPROVED)
- **明细表**: `outbound_id`, `sku_id`, `batch_no` (指定批次出库时填), `apply_qty`, `real_qty`

#### 3.3.3 调整单 (inventory_adjust)
用于盘点、报损、报溢。

---

## 4. 核心服务设计 (InventoryCoreService)

### 4.1 接口定义
此服务为库存操作的**唯一入口**。

```java
public interface IInventoryCoreService {

    /**
     * 锁定库存 (下单/申请出库)
     * 场景: 提交出库申请，预占库存
     * 逻辑: available_qty -= qty, locked_qty += qty
     */
    void lockStock(Long skuId, Long warehouseId, BigDecimal qty);

    /**
     * 释放锁定库存 (取消订单)
     * 场景: 申请被驳回或撤销
     * 逻辑: available_qty += qty, locked_qty -= qty
     */
    void releaseStock(Long skuId, Long warehouseId, BigDecimal qty);

    /**
     * 扣减库存 (确认出库)
     * 场景: 审核通过，实际发货
     * 逻辑: locked_qty -= qty, 扣减 inventory_stock_batch 对应数量
     */
    void reduceStock(Long skuId, Long warehouseId, String batchNo, BigDecimal qty);

    /**
     * 增加库存 (确认入库)
     * 场景: 审核通过，上架
     * 逻辑: available_qty += qty, 增加 inventory_stock_batch
     */
    void increaseStock(Long skuId, Long warehouseId, String batchNo, BigDecimal qty, Date prodDate, Date expDate);

    /**
     * 预占库存 (可选，用于特殊场景)
     */
    void reserveStock(Long skuId, Long warehouseId, BigDecimal qty);
}
```

### 4.2 并发控制策略
为防止超卖，**推荐使用方案1**。

**方案1: 数据库乐观锁 (Optimistic Locking)**
- 依赖 `inventory_stock.version` 字段。
- MyBatis-Plus 配置 `OptimisticLockerInnerInterceptor`。
- 更新 SQL 示例:
  ```sql
  UPDATE inventory_stock 
  SET available_qty = available_qty - #{qty}, 
      locked_qty = locked_qty + #{qty}, 
      version = version + 1 
  WHERE id = #{id} AND version = #{version} AND available_qty >= #{qty}
  ```
- 若更新失败（返回行数0），则抛出 `ServiceException("库存已被修改，请重试")` 或进行重试。

**方案2 (高并发补充): Redis + Lua**
- 仅在极其高并发场景下（如秒杀）使用 Redis 预扣减，异步同步数据库。鉴于农业物流场景，数据库乐观锁通常足以应付。

### 4.3 事务控制
所有写操作必须添加事务注解：
```java
@Transactional(rollbackFor = Exception.class)
public void lockStock(...) { ... }
```

---

## 5. RuoYi 开发规范实施

### 5.1 实体类
```java
@Data
@TableName("inventory_stock")
public class InventoryStock extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
    @TableId
    private Long id;
    
    private Long skuId;
    private Long warehouseId;
    private BigDecimal availableQty;
    private BigDecimal lockedQty;
    
    @Version
    private Long version;
}
```

### 5.2 Controller 规范
```java
@RestController
@RequestMapping("/inventory/stock")
public class StockController extends BaseController {

    @Autowired
    private IInventoryStockService stockService;

    @PreAuthorize("@ss.hasPermi('inventory:stock:list')")
    @GetMapping("/list")
    public AjaxResult list(InventoryStock stock) {
        startPage(); // RuoYi 分页
        List<InventoryStock> list = stockService.selectStockList(stock);
        return getDataTable(list); // RuoYi 表格数据封装
    }
}
```

### 5.3 异常处理
业务层检测到逻辑错误（如库存不足）时：
```java
if (stock.getAvailableQty().compareTo(qty) < 0) {
    throw new ServiceException("当前库存不足，可用库存: " + stock.getAvailableQty());
}
```

---

## 6. API 接口清单

| 模块 | 方法 | 路径 | 描述 |
| :--- | :--- | :--- | :--- |
| **库存** | GET | `/inventory/stock/list` | 查询库存列表 (支持分页) |
| **库存** | GET | `/inventory/stock/detail/{id}` | 获取库存详情 |
| **入库** | POST | `/inventory/inbound/create` | 创建入库申请 |
| **入库** | GET | `/inventory/inbound/list` | 查询入库单 |
| **入库** | POST | `/inventory/inbound/approve` | 审核入库 (触发库存增加) |
| **出库** | POST | `/inventory/outbound/create` | 创建出库申请 (触发库存锁定) |
| **出库** | GET | `/inventory/outbound/list` | 查询出库单 |
| **出库** | POST | `/inventory/outbound/approve` | 审核出库 (触发库存扣减) |
| **商品** | GET | `/inventory/product/list` | 查询商品列表 |

---

## 7. AI 辅助开发任务列表 (Task List)

请按照以下顺序生成代码：

### Task 1: 初始化基础结构
- 创建 `inventory_warehouse`, `inventory_product`, `inventory_sku` 的 Domain, Mapper, Service, Controller。
- 确保遵循 RuoYi 规范（继承 BaseEntity, 返回 AjaxResult）。

### Task 2: 构建库存核心表
- 创建 `inventory_stock` 和 `inventory_stock_batch`。
- 配置 MyBatis-Plus 乐观锁插件。

### Task 3: 实现 InventoryCoreService (重点)
- 编写 `IInventoryCoreService` 接口。
- 实现 `InventoryCoreServiceImpl`。
- 实现 `lockStock`: 使用乐观锁扣减可用、增加锁定。
- 实现 `reduceStock`: 扣减锁定，同时扣减批次表 `inventory_stock_batch`。
- 实现 `increaseStock`: 增加可用，插入/更新批次表。
- **关键**: 每次操作必须插入 `inventory_stock_log`。

### Task 4: 实现单据业务
- 实现 `InboundService` 和 `OutboundService`。
- 在 `create` 方法中调用 `coreService.lockStock` (仅出库)。
- 在 `approve` 方法中调用 `coreService.increaseStock` (入库) 或 `coreService.reduceStock` (出库)。
- 引入事务 `@Transactional`。

### Task 5: 前端对接
- 生成对应的 Vue 页面 (列表、新增、详情)。
- 对接后端 `/inventory/...` 接口。

---

## 8. 附录：核心逻辑代码片段

### 8.1 乐观锁更新库存 Mapper XML
```xml
<update id="updateStockOptimistic">
    UPDATE inventory_stock
    SET available_qty = available_qty - #{qty},
        locked_qty = locked_qty + #{qty},
        version = version + 1,
        update_time = NOW()
    WHERE id = #{id} 
      AND version = #{version}
      AND available_qty >= #{qty}
</update>
```

### 8.2 核心服务实现
```java
@Service
public class InventoryCoreServiceImpl implements IInventoryCoreService {
    
    @Autowired
    private InventoryStockMapper stockMapper;
    @Autowired
    private InventoryStockLogMapper logMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lockStock(Long skuId, Long warehouseId, BigDecimal qty) {
        // 1. 查询当前库存
        InventoryStock stock = stockMapper.selectOne(skuId, warehouseId);
        if (stock == null || stock.getAvailableQty().compareTo(qty) < 0) {
            throw new ServiceException("库存不足");
        }

        // 2. 乐观锁更新
        int rows = stockMapper.updateStockOptimistic(stock.getId(), qty, stock.getVersion());
        if (rows == 0) {
            throw new ServiceException("库存正忙，请重试");
        }

        // 3. 记录日志
        recordLog(stock, "LOCK", qty, ...);
    }
}
```