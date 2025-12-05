# 研究中心管理模块API接口文档

## 接口概览

### 1. 分页查询研究中心
- **接口地址**: `/seed/locationMaster/page`
- **请求方式**: POST
- **接口描述**: 分页查询研究中心信息
- **请求参数**:

**Query参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| current | Integer | 否 | 当前页码，默认1 |
| size | Integer | 否 | 每页条数，默认10 |

**Body参数**:
```json
{
  "locationId": "string",      // 位置ID
  "locationName": "string",    // 位置名称
  "region": "string",          // 地区
  "zone": "string",          // 区域
  "woneda": "string"         // 沃雷达
}
```

**响应参数**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "records": [
      {
        "locationId": "string",
        "locationName": "string",
        "region": "string",
        "zone": "string",
        "woneda": "string",
        "latitude": "decimal",
        "longitude": "decimal",
        "createBy": "string",
        "createTime": "2024-01-01 12:00:00",
        "updateBy": "string",
        "updateTime": "2024-01-01 12:00:00",
        "remark": "string"
      }
    ],
    "total": 100,
    "size": 10,
    "current": 1,
    "pages": 10
  }
}
```

### 2. 新增研究中心
- **接口地址**: `/seed/locationMaster/add`
- **请求方式**: POST
- **接口描述**: 新增研究中心信息
- **请求参数**:

**Body参数**:
```json
{
  "locationName": "string",    // 必填，位置名称
  "region": "string",          // 地区
  "zone": "string",          // 区域
  "woneda": "string",         // 沃雷达
  "latitude": "decimal",     // 纬度
  "longitude": "decimal",    // 经度
  "remark": "string"         // 备注
}
```

**响应参数**:
```json
{
  "code": 200,
  "msg": "研究中心新增成功",
  "data": null
}
```

### 3. 修改研究中心
- **接口地址**: `/seed/locationMaster/update`
- **请求方式**: POST
- **接口描述**: 修改研究中心信息
- **请求参数**:

**Body参数**:
```json
{
  "locationId": "string",      // 必填，位置ID
  "locationName": "string",    // 位置名称
  "region": "string",          // 地区
  "zone": "string",          // 区域
  "woneda": "string",         // 沃雷达
  "latitude": "decimal",     // 纬度
  "longitude": "decimal",    // 经度
  "remark": "string"         // 备注
}
```

**响应参数**:
```json
{
  "code": 200,
  "msg": "研究中心修改成功",
  "data": null
}
```

### 4. 删除研究中心
- **接口地址**: `/seed/locationMaster/delete`
- **请求方式**: GET
- **接口描述**: 删除研究中心信息
- **请求参数**:

**Query参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| locationId | String | 是 | 位置ID |

**响应参数**:
```json
{
  "code": 200,
  "msg": "研究中心删除成功",
  "data": null
}
```

### 5. 获取研究中心详情
- **接口地址**: `/seed/locationMaster/detail`
- **请求方式**: GET
- **接口描述**: 获取研究中心详细信息
- **请求参数**:

**Query参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| locationId | String | 是 | 位置ID |

**响应参数**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "locationId": "string",
    "locationName": "string",
    "region": "string",
    "zone": "string",
    "woneda": "string",
    "latitude": "decimal",
    "longitude": "decimal",
    "createBy": "string",
    "createTime": "2024-01-01 12:00:00",
    "updateBy": "string",
    "updateTime": "2024-01-01 12:00:00",
    "remark": "string"
  }
}
```

### 6. 查询研究中心列表
- **接口地址**: `/seed/locationMaster/list`
- **请求方式**: POST
- **接口描述**: 查询研究中心列表信息（不分页）
- **请求参数**:

**Body参数**:
```json
{
  "locationId": "string",      // 位置ID
  "locationName": "string",    // 位置名称
  "region": "string",          // 地区
  "zone": "string",          // 区域
  "woneda": "string"         // 沃雷达
}
```

**响应参数**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "locationId": "string",
      "locationName": "string",
      "region": "string",
      "zone": "string",
      "woneda": "string",
      "latitude": "decimal",
      "longitude": "decimal",
      "createBy": "string",
      "createTime": "2024-01-01 12:00:00",
      "updateBy": "string",
      "updateTime": "2024-01-01 12:00:00",
      "remark": "string"
    }
  ]
}
```

## 状态码说明

| 状态码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 500 | 操作失败 |

## 通用响应格式

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {}
}
```