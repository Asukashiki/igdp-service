# 农田管理模块 (igdp-farmland-management)

## 项目介绍

农田管理模块是智慧农业平台的核心组成部分，主要用于管理农民认证信息、用户信息和土地信息。该模块提供了完整的农田信息管理解决方案，包括农民身份认证、土地信息维护、土地流转等功能。

## 功能模块

### 1. 农民认证管理
- 农民身份认证申请
- 认证信息审核
- 认证状态管理

### 2. 用户管理
- 用户信息维护
- 用户权限管理
- 密码修改

### 3. 土地信息管理
- 土地信息录入与维护
- 土地流转管理
- 土地信息查询

## 技术架构

- **核心框架**: Spring Boot 2.7.6
- **持久层框架**: MyBatis Plus 3.5.3.1
- **数据库**: PostgreSQL
- **权限认证**: Sa-Token
- **API文档**: Swagger3
- **工具类库**: Hutool 5.8.27

## 包结构说明

```
com.inspur.farmland.management
├── bean
│   ├── dto         # 数据传输对象
│   ├── entity      # 实体类
│   └── vo          # 视图对象
├── config          # 配置类
├── constant        # 常量类
├── controller      # 控制器层
├── exception       # 异常处理
├── mapper          # 数据访问层
├── service         # 业务逻辑层
│   └── impl        # 业务逻辑实现
└── utils           # 工具类
```

## 核心实体类

1. **FarmerCertification** - 农民认证信息
2. **User** - 用户信息
3. **LandInfo** - 土地信息

## 接口文档

项目集成了Swagger3，启动后可通过以下地址访问API文档：
```
http://localhost:8080/swagger-ui/index.html
```

## 部署说明

1. 确保已安装PostgreSQL数据库
2. 创建数据库`farmland_db`
3. 修改`application.yml`中的数据库连接配置
4. 运行项目启动类`IgdpApplication`

## 注意事项

- 所有接口均采用RESTful风格设计
- 时间格式统一为`yyyy-MM-dd HH:mm:ss`
- 返回结果统一包装为`Result`对象