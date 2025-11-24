# 数据库脚本说明

## 目录结构
- `schema.sql`: 数据库表结构定义
- `data.sql`: 初始化数据

## 使用说明

### 1. 创建数据库
```sql
CREATE DATABASE IF NOT EXISTS igdp_farmland CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 执行表结构脚本
```bash
mysql -u username -p igdp_farmland < schema.sql
```

### 3. 执行初始化数据脚本
```bash
mysql -u username -p igdp_farmland < data.sql
```

## 表结构说明

### user (用户表)
存储系统用户信息，包括管理员和农民用户。

### farmer_certification (农民认证表)
存储农民用户的认证信息，包括身份证明、种植类型等。

### land_info (土地信息表)
存储土地地块信息，包括位置、面积、状态等。

## 注意事项
1. 请根据实际环境修改数据库连接信息
2. 密码字段存储的是加密后的密码
3. 时间字段建议使用UTC时间存储