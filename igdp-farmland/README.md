# 农田管理模块 (igdp-farmland)

## 模块概述

农田管理模块是系统的基础数据模块，用于管理农业发展代理人（DA）、农民信息和土地信息。

### 子模块功能

- **DA管理**：管理农业发展代理人的注册、账号、权限
- **农民管理**：管理农民基础信息的采集、维护
- **土地信息管理**：管理土地/地块信息的采集、维护、关联

---

## 技术栈

- **框架**: Spring Boot 2.7.6
- **ORM**: MyBatis Plus（无XML配置）
- **查询方式**: LambdaQueryWrapper
- **工具库**: Hutool、Lombok
- **HTTP方法**: 仅使用GET和POST
- **响应格式**: AjaxResult

---

## 目录结构

```
igdp-farmland/
├── pom.xml                                    # Maven配置
├── src/main/java/com/inspur/farmland/
│   ├── domain/                                # 实体类
│   │   ├── DaInfo.java                        # DA信息实体
│   │   ├── FarmerInfo.java                    # 农民信息实体
│   │   └── LandInfo.java                      # 土地信息实体
│   ├── mapper/                                # Mapper接口
│   │   ├── DaInfoMapper.java                  # DA信息Mapper
│   │   ├── FarmerInfoMapper.java              # 农民信息Mapper
│   │   └── LandInfoMapper.java                # 土地信息Mapper
│   ├── service/                               # Service接口
│   │   ├── IDaInfoService.java                # DA信息Service
│   │   ├── IFarmerInfoService.java            # 农民信息Service
│   │   ├── ILandInfoService.java              # 土地信息Service
│   │   └── impl/                              # Service实现
│   │       ├── DaInfoServiceImpl.java
│   │       ├── FarmerInfoServiceImpl.java
│   │       └── LandInfoServiceImpl.java
│   └── controller/                            # 控制器
│       ├── DaInfoController.java              # DA管理Controller
│       ├── FarmerInfoController.java          # 农民管理Controller
│       └── LandInfoController.java            # 土地管理Controller
└── src/main/resources/
    └── farmland_tables.sql                    # 数据库建表脚本
```

---

## 数据库表

### 1. t_da_info - DA信息表

存储农业发展代理人（DA）的基本信息和账号信息。

**主要字段**:
- `da_id`: DA编码（业务主键）
- `da_name`: DA姓名
- `id_card`: 身份证号
- `account`: 登录账号
- `password`: 登录密码（BCrypt加密）
- `account_status`: 账号状态（1-启用 0-禁用）
- `woreda_code`: 所属镇代码
- `kebele_codes`: 负责的村代码（多个用逗号分隔）

### 2. t_farmer_info - 农民信息表

存储农民的基本信息、所属组织和统计数据。

**主要字段**:
- `farmer_id`: 农民编码（业务主键）
- `farmer_name`: 农民姓名
- `id_card`: 身份证号/ID
- `youth_category`: 青年类别
- `union_id/cooperative_id`: 所属组织
- `kebele_code`: 所属村代码
- `total_land_area`: 总土地面积（自动计算）
- `land_count`: 地块数量（自动计算）
- `da_id`: 负责DA编码

### 3. t_land_info - 土地信息表

存储土地/地块的详细信息和关联关系。

**主要字段**:
- `land_id`: 土地编码（业务主键）
- `land_name`: 地块名称
- `owner_type`: 土地权属类型
- `land_type`: 地块类型（水田/旱地/园地等）
- `area_size`: 地块面积（公顷）
- `latitude/longitude`: 地理坐标
- `farmer_id`: 关联农民ID
- `current_status`: 当前状态（耕种中/闲置/休耕）
- `max_seed_amount`: 估算最大种子量（自动计算）
- `max_fertilizer_amount`: 估算最大肥料量（自动计算）

---

## API接口文档

### DA管理接口

**基础路径**: `/farmland/da`

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 分页列表 | GET | `/page` | 查询DA分页列表 |
| 详情 | GET | `/{daId}` | 获取DA详细信息 |
| 新增 | POST | `/` | 新增DA |
| 修改 | POST | `/{daId}` | 修改DA信息 |
| 删除 | POST | `/{daId}/delete` | 删除DA（逻辑删除） |
| 启用/禁用 | POST | `/{daId}/status` | 启用或禁用账号 |
| 重置密码 | POST | `/{daId}/password/reset` | 重置DA密码 |
| 下拉选项 | GET | `/options` | 获取DA下拉列表 |

### 农民管理接口

**基础路径**: `/farmland/farmer`

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 分页列表 | GET | `/page` | 查询农民分页列表 |
| 详情 | GET | `/{farmerId}` | 获取农民详细信息 |
| 新增 | POST | `/` | 新增农民 |
| 修改 | POST | `/{farmerId}` | 修改农民信息 |
| 删除 | POST | `/{farmerId}/delete` | 删除农民（逻辑删除） |
| 批量删除 | POST | `/batch/delete` | 批量删除农民 |
| 下拉选项 | GET | `/options` | 获取农民下拉列表 |

### 土地管理接口

**基础路径**: `/farmland/land`

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 分页列表 | GET | `/page` | 查询土地分页列表 |
| 详情 | GET | `/{landId}` | 获取土地详细信息 |
| 新增 | POST | `/` | 新增土地 |
| 修改 | POST | `/{landId}` | 修改土地信息 |
| 删除 | POST | `/{landId}/delete` | 删除土地（逻辑删除） |
| 批量删除 | POST | `/batch/delete` | 批量删除土地 |
| 关联农民 | POST | `/{landId}/bindFarmer` | 关联农民 |
| 解除关联 | POST | `/{landId}/unbindFarmer` | 解除农民关联 |
| 农民土地 | GET | `/farmer/{farmerId}` | 查询农民的所有土地 |
| 统计数据 | GET | `/statistics` | 获取土地统计数据 |

---

## 核心功能特性

### 1. DA管理

- **账号管理**: 创建DA账号，支持启用/禁用
- **密码加密**: 使用BCrypt加密存储密码
- **权限控制**: 管理DA负责的村（kebele）
- **唯一性校验**: 身份证号、账号唯一性检查

### 2. 农民管理

- **基础信息**: 管理农民个人信息
- **组织关联**: 关联Union和Cooperative
- **统计信息**: 自动计算土地面积和地块数量
- **数据联动**: 删除农民时自动解除土地关联

### 3. 土地管理

- **地块信息**: 详细记录土地属性和位置
- **农民关联**: 支持关联/解除农民
- **自动计算**: 根据面积自动计算种子和肥料用量
  - 种子用量: 30 kg/公顷
  - 肥料用量: 100 kg/公顷
- **统计分析**: 按地块类型、状态统计土地数据

---

## 业务规则

### 逻辑删除

所有删除操作均为逻辑删除，通过`status`字段标记：
- `1`: 正常
- `0`: 已删除

### 数据联动

1. **删除农民**:
   - 自动解除关联的土地
   - 更新土地统计信息

2. **土地关联农民**:
   - 自动填充农民姓名、身份证号、电话
   - 更新农民的土地统计信息

3. **解除土地关联**:
   - 清空农民关联字段
   - 更新农民的土地统计信息

### 唯一性约束

- DA身份证号唯一
- DA账号唯一
- 农民身份证号唯一
- 土地编码唯一

---

## 部署说明

### 1. 数据库初始化

执行建表脚本：
```sql
source igdp-farmland/src/main/resources/farmland_tables.sql
```

### 2. Maven构建

```bash
cd igdp-service
mvn clean install -DskipTests
```

### 3. 模块依赖

确保在`igdp-admin`模块的pom.xml中添加依赖：

```xml
<dependency>
    <groupId>com.inspur</groupId>
    <artifactId>igdp-farmland</artifactId>
</dependency>
```

### 4. 启动应用

```bash
cd igdp-service
./ry.sh start
```

---

## 开发规范

### 代码规范

1. **类命名**: 大驼峰式命名法（PascalCase）
2. **方法命名**: 小驼峰式命名法（camelCase）
3. **查询构建**: 优先使用`LambdaQueryWrapper`
4. **事务管理**: 增删改操作使用`@Transactional`
5. **返回格式**: 统一使用`AjaxResult`

### 查询示例

```java
LambdaQueryWrapper<DaInfo> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(DaInfo::getStatus, "1")
       .like(DaInfo::getDaName, searchName)
       .eq(DaInfo::getWoredaCode, woredaCode)
       .orderByDesc(DaInfo::getCreateTime);
```

### 更新示例

```java
LambdaUpdateWrapper<DaInfo> wrapper = new LambdaUpdateWrapper<>();
wrapper.eq(DaInfo::getDaId, daId)
       .set(DaInfo::getAccountStatus, "0")
       .set(DaInfo::getUpdateBy, username);
daInfoMapper.update(null, wrapper);
```

---

## 注意事项

1. **不使用XML**: 完全使用MyBatis Plus注解方式，不创建XML映射文件
2. **HTTP方法**: 仅使用GET（查询）和POST（增删改）
3. **密码安全**: DA密码使用BCrypt加密，不可逆
4. **数据一致性**: 土地和农民关联时自动维护统计数据
5. **权限控制**: 建议在Controller层添加权限注解

---

## 后续扩展

### 建议增强功能

1. **数据导入导出**: Excel批量导入导出功能
2. **地图展示**: GeoJSON坐标在地图上可视化展示
3. **统计报表**: 更丰富的数据统计和分析报表
4. **审计日志**: 记录数据变更历史
5. **文件上传**: 支持上传土地证明文件

---

## 联系方式

- **模块负责人**: Inspur开发团队
- **版本**: 3.8.7
- **最后更新**: 2025-12-03
