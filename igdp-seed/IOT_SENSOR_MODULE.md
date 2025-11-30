# 物联网传感器维护模块开发文档

## 模块概述

本模块实现了物联网传感器设备的完整CRUD功能，包括传感器信息的增删改查、分页查询、条件筛选等功能。

## 开发时间

2025-11-30

## 技术栈

- Spring Boot 2.7.6
- MyBatis Plus
- Hutool
- Lombok
- MySQL

## 文件清单

### 1. 实体类 (Entity)

**路径**: `src/main/java/com/inspur/seed/domain/entity/IotSensorInfo.java`

**功能**:
- 物联网传感器信息表实体类
- 使用 `@TableName("iot_sensor_info")` 映射数据库表
- 使用 `@TableId(type = IdType.ASSIGN_UUID)` 设置UUID主键策略
- 使用 `@TableLogic` 实现逻辑删除

**字段**:
- dataId: 主键ID (UUID)
- iotId: 传感器编号
- iotName: 传感器名称
- iotType: 传感器类型 (01-温度, 02-湿度, 03-光照, 04-土壤, 05-气体, 99-其他)
- manufacturer: 制造商
- calibrationDate: 校准日期
- firmwareVersion: 固件版本
- batteryStatus: 电池状态
- orgId/orgName: 操作机构信息
- remark: 备注
- createBy/createTime: 创建信息
- updateBy/updateTime: 更新信息
- delFlag: 删除标志 (0-正常, 2-删除)

### 2. 数据传输对象 (DTO)

#### 2.1 查询DTO
**路径**: `src/main/java/com/inspur/seed/domain/dto/IotSensorQueryDto.java`

**字段**:
- pageNum: 当前页码 (默认1)
- pageSize: 每页条数 (默认10)
- iotName: 传感器名称 (模糊查询)
- iotType: 传感器类型 (精确查询)
- startTime: 查询开始时间
- endTime: 查询结束时间

#### 2.2 保存DTO
**路径**: `src/main/java/com/inspur/seed/domain/dto/IotSensorSaveDto.java`

**字段**:
- dataId: 主键ID (修改时必填)
- iotId: 传感器编号 (必填, 最大32位)
- iotName: 传感器名称 (必填, 最大100位)
- iotType: 传感器类型 (必填, 2位字符)
- manufacturer: 制造商 (必填, 最大32位)
- calibrationDate: 校准日期 (必填)
- firmwareVersion: 固件版本 (必填, 最大32位)
- batteryStatus: 电池状态 (可选, 最大64位)
- remark: 备注 (可选, 最大500位)

**参数校验**: 使用 `javax.validation` 注解进行参数校验

#### 2.3 删除DTO
**路径**: `src/main/java/com/inspur/seed/domain/dto/IotSensorDeleteDto.java`

**字段**:
- dataIds: 主键ID列表 (支持批量删除)

### 3. 视图对象 (VO)

**路径**: `src/main/java/com/inspur/seed/domain/vo/IotSensorVo.java`

**功能**:
- 用于前端展示的视图对象
- 包含所有展示字段
- 新增 `iotTypeName` 字段用于展示传感器类型名称
- 使用 `@JsonFormat` 格式化日期时间字段

### 4. Mapper接口

**路径**: `src/main/java/com/inspur/seed/mapper/IotSensorInfoMapper.java`

**功能**:
- 继承 `BaseMapper<IotSensorInfo>`
- 使用MyBatis Plus内置方法
- 无需自定义SQL

### 5. Service接口

**路径**: `src/main/java/com/inspur/seed/service/IIotSensorInfoService.java`

**方法列表**:
- `IPage<IotSensorVo> page(IotSensorQueryDto)` - 分页查询
- `List<IotSensorVo> list(IotSensorQueryDto)` - 列表查询
- `AjaxResult add(IotSensorSaveDto)` - 新增
- `AjaxResult update(IotSensorSaveDto)` - 修改
- `IotSensorVo detail(String)` - 详情
- `AjaxResult delete(List<String>)` - 删除

### 6. Service实现类

**路径**: `src/main/java/com/inspur/seed/service/impl/IotSensorInfoServiceImpl.java`

**核心逻辑**:

#### 6.1 分页查询 (page)
- 使用 `QueryWrapper` 构建查询条件
- iotName 使用 `like` 模糊查询
- iotType 使用 `eq` 精确查询
- 时间范围使用 `ge/le` 查询 (基于create_time字段)
- 过滤已删除数据 (`del_flag = '0'`)
- 按 `create_time` 降序排序
- 转换 Entity 为 VO 返回

#### 6.2 列表查询 (list)
- 同分页查询，但不使用分页

#### 6.3 新增 (add)
- DTO 转 Entity
- 生成 UUID 作为 dataId
- 设置 `delFlag = '0'`
- 填充 `createTime`
- TODO: 填充 orgId、orgName、createBy (从当前登录用户获取)
- 调用 `save` 方法保存

#### 6.4 修改 (update)
- 校验 dataId 是否存在
- DTO 转 Entity
- 填充 `updateTime`
- TODO: 填充 updateBy (从当前登录用户获取)
- 调用 `updateById` 方法更新

#### 6.5 详情查询 (detail)
- 根据 dataId 查询
- 校验数据是否存在
- Entity 转 VO 返回
- 补充类型名称翻译字段

#### 6.6 删除 (delete)
- 逻辑删除，更新 `del_flag = '2'`
- 支持批量删除
- 使用 `updateBatchById` 而非 `remove`

#### 6.7 类型名称转换 (getIotTypeName)
- 01 -> 温度传感器
- 02 -> 湿度传感器
- 03 -> 光照传感器
- 04 -> 土壤传感器
- 05 -> 气体传感器
- 99 -> 其他

### 7. Controller控制器

**路径**: `src/main/java/com/inspur/seed/controller/IotSensorInfoController.java`

**接口列表**:

| 接口地址 | 请求方式 | 功能说明 |
|---------|---------|---------|
| `/seed/iotSensor/page` | POST | 分页查询传感器列表 |
| `/seed/iotSensor/list` | POST | 查询传感器列表(不分页) |
| `/seed/iotSensor/add` | POST | 新增传感器 |
| `/seed/iotSensor/update` | POST | 修改传感器 |
| `/seed/iotSensor/detail` | GET | 查询传感器详情 |
| `/seed/iotSensor/delete` | POST | 删除传感器(逻辑删除) |

**特性**:
- 继承 `BaseController` 获取分页等基础功能
- 使用 `@Validated` 进行参数校验
- 使用 `@Log` 注解记录操作日志
- 统一返回 `AjaxResult` 封装

### 8. 常量类

**路径**: `src/main/java/com/inspur/seed/constant/IotSensorConstant.java`

**常量定义**:
- `IOT_TYPE_TEMPERATURE = "01"` - 温度传感器
- `IOT_TYPE_HUMIDITY = "02"` - 湿度传感器
- `IOT_TYPE_LIGHT = "03"` - 光照传感器
- `IOT_TYPE_SOIL = "04"` - 土壤传感器
- `IOT_TYPE_GAS = "05"` - 气体传感器
- `IOT_TYPE_OTHER = "99"` - 其他
- `DEL_FLAG_NORMAL = "0"` - 正常
- `DEL_FLAG_DELETED = "2"` - 已删除

### 9. 数据库脚本

**路径**: `sql/iot_sensor_info.sql`

**表名**: `iot_sensor_info`

**索引**:
- 主键: `data_id`
- 普通索引: `iot_id`, `iot_type`, `org_id`, `create_time`

## 接口文档

### 1. 分页查询

**接口**: `POST /seed/iotSensor/page`

**请求参数**:
```json
{
  "pageNum": 1,
  "pageSize": 10,
  "iotName": "传感器名称",
  "iotType": "01",
  "startTime": "2025-01-01 00:00:00",
  "endTime": "2025-12-31 23:59:59"
}
```

**响应数据**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "records": [...],
    "total": 100,
    "current": 1,
    "size": 10
  }
}
```

### 2. 列表查询

**接口**: `POST /seed/iotSensor/list`

**请求参数**: 同分页查询 (不需要pageNum和pageSize)

**响应数据**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [...]
}
```

### 3. 新增

**接口**: `POST /seed/iotSensor/add`

**请求参数**:
```json
{
  "iotId": "IOT001",
  "iotName": "温度传感器01",
  "iotType": "01",
  "manufacturer": "制造商A",
  "calibrationDate": "2025-01-01 10:00:00",
  "firmwareVersion": "v1.0.0",
  "batteryStatus": "100%",
  "remark": "备注信息"
}
```

### 4. 修改

**接口**: `POST /seed/iotSensor/update`

**请求参数**:
```json
{
  "dataId": "uuid-xxxx-xxxx",
  "iotId": "IOT001",
  "iotName": "温度传感器01",
  "iotType": "01",
  "manufacturer": "制造商A",
  "calibrationDate": "2025-01-01 10:00:00",
  "firmwareVersion": "v1.0.1",
  "batteryStatus": "95%",
  "remark": "更新备注"
}
```

### 5. 详情

**接口**: `GET /seed/iotSensor/detail?dataId=uuid-xxxx-xxxx`

**响应数据**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "dataId": "uuid-xxxx-xxxx",
    "iotId": "IOT001",
    "iotName": "温度传感器01",
    "iotType": "01",
    "iotTypeName": "温度传感器",
    "manufacturer": "制造商A",
    "calibrationDate": "2025-01-01 10:00:00",
    "firmwareVersion": "v1.0.0",
    "batteryStatus": "100%",
    "orgId": "org-001",
    "orgName": "测试机构",
    "remark": "备注信息",
    "createBy": "admin",
    "createTime": "2025-11-30 11:50:00",
    "updateBy": "admin",
    "updateTime": "2025-11-30 11:50:00"
  }
}
```

### 6. 删除

**接口**: `POST /seed/iotSensor/delete`

**请求参数**:
```json
["uuid-1", "uuid-2", "uuid-3"]
```

## 注意事项

1. **主键策略**: 使用UUID，字段类型为String
2. **逻辑删除**: 使用 `del_flag` 字段，0-正常，2-删除
3. **时间字段**: 创建时间自动填充，更新时间自动更新
4. **机构信息**: 需要从当前登录用户上下文获取 orgId 和 orgName (TODO)
5. **QueryWrapper**: 使用了 QueryWrapper 保证灵活的查询条件构建
6. **分页对象**: 使用 MyBatis Plus 的 Page 对象
7. **返回值**: 统一使用 AjaxResult 封装
8. **参数校验**: 使用 javax.validation 注解进行校验

## TODO 清单

1. [ ] 在 Service 实现类中实现从当前登录用户获取 orgId、orgName、createBy、updateBy
2. [ ] 根据需要添加权限校验注解 (如 @SaCheckPermission)
3. [ ] 根据实际业务需求添加更多查询条件
4. [ ] 考虑添加传感器数据监控相关功能
5. [ ] 考虑添加传感器告警功能

## 部署步骤

1. 执行 SQL 脚本创建数据库表
2. 确保 igdp-seed 模块已正确配置到主应用
3. 重启应用
4. 使用 Postman 或其他工具测试接口

## 测试建议

1. 测试新增功能，验证参数校验是否生效
2. 测试修改功能，验证数据是否正确更新
3. 测试删除功能，验证逻辑删除是否正确
4. 测试查询功能，验证条件筛选和分页是否正常
5. 测试详情功能，验证类型名称翻译是否正确

## 版本历史

- v1.0.0 (2025-11-30): 初始版本，实现基础CRUD功能
