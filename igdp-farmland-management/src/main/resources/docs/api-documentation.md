# 农田管理系统API接口文档

## 1. 概述

本文档描述了农田管理系统的RESTful API接口，供前端开发人员使用。

## 2. 公共信息

### 2.1 请求域名
```
http://localhost:8080
```

### 2.2 返回格式
统一返回JSON格式数据

### 2.3 状态码说明
| 状态码 | 说明 |
|-------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 3. 接口列表

### 3.1 用户相关接口

#### 3.1.1 获取用户信息
- **接口地址**: `GET /api/user/{userId}`
- **请求方式**: GET
- **请求参数**:
  | 参数名 | 类型 | 必填 | 说明 |
  |-------|------|------|------|
  | userId | String | 是 | 用户ID |
- **响应数据**:
  ```json
  {
    "code": 200,
    "msg": "操作成功",
    "data": {
      "userId": 1,
      "account": "admin",
      "userName": "系统管理员",
      "idCard": null,
      "gender": "男",
      "phone": "13800138000",
      "email": "admin@example.com",
      "adCode": null,
      "regDate": "2025-01-01T00:00:00",
      "status": 1
    }
  }
  ```
- **响应说明**:
  - status: 1表示正常，0表示禁用
  - 用户不存在时返回错误: `{"code": 500, "msg": "用户不存在"}`

#### 3.1.2 更新用户信息
- **接口地址**: `POST /api/user/update`
- **请求方式**: POST
- **请求参数**:
  | 参数名 | 类型 | 必填 | 说明 |
  |-------|------|------|------|
  | userId | Long | 是 | 用户ID |
  | account | String | 是 | 账号 |
  | userName | String | 是 | 姓名 |
  | idCard | String | 否 | 身份证号 |
  | gender | String | 否 | 性别 |
  | phone | String | 否 | 手机号 |
  | email | String | 否 | 邮箱 |
  | adCode | String | 否 | 行政区划代码 |
  | status | Integer | 否 | 账号状态 |
- **响应数据**:
  ```json
  {
    "code": 200,
    "msg": "更新用户信息成功",
    "data": {
      "userId": 1,
      "account": "admin",
      "userName": "系统管理员"
    }
  }
  ```
- **响应说明**: 返回更新后的用户信息

#### 3.1.3 修改密码
- **接口地址**: `POST /api/user/{userId}/password`
- **请求方式**: POST
- **请求参数**:
  | 参数名 | 类型 | 必填 | 说明 |
  |-------|------|------|------|
  | userId | String | 是 | 用户ID |
  | newPassword | String | 是 | 新密码 |
- **响应数据**:
  ```json
  {
    "code": 200,
    "msg": "修改密码成功"
  }
  ```
- **响应说明**: 修改成功返回成功消息,失败返回错误消息

### 3.2 土地信息相关接口

#### 3.2.1 查询土地信息列表(分页)
- **接口地址**: `GET /api/land/list`
- **请求方式**: GET
- **请求参数**:
  | 参数名 | 类型 | 必填 | 说明 |
  |-------|------|------|------|
  | landName | String | 否 | 地块名称(模糊查询) |
  | landType | String | 否 | 地块类型 |
  | currentStatus | String | 否 | 当前状态 |
  | adCode | String | 否 | 行政区划代码(模糊查询) |
  | farmerUserId | String | 否 | 所属农民用户ID |
  | keyword | String | 否 | 关键词搜索(会覆盖landName) |
  | page | Integer | 否 | 页码,默认1 |
  | pageSize | Integer | 否 | 每页数量,默认10 |
- **响应数据**:
  ```json
  {
    "code": 200,
    "msg": "操作成功",
    "data": {
      "list": [
        {
          "landId": 1,
          "landName": "一号地块",
          "ownerType": "个人",
          "adCode": "110101",
          "detailAddress": "北京市朝阳区某某街道123号",
          "areaSize": 10.50,
          "landType": "耕地",
          "currentStatus": "正常",
          "latitude": 39.904211,
          "longitude": 116.407395,
          "farmerUserId": "2",
          "createBy": "2",
          "createTime": "2025-01-01T00:00:00",
          "updateTime": "2025-01-01T00:00:00",
          "remark": "优质耕地"
        }
      ],
      "total": 100,
      "page": 1,
      "pageSize": 10
    }
  }
  ```
- **响应说明**:
  - list: 当前页数据列表
  - total: 总记录数
  - page: 当前页码
  - pageSize: 每页数量

#### 3.2.2 根据用户ID获取土地列表
- **接口地址**: `GET /api/land/user/{userId}`
- **请求方式**: GET
- **请求参数**:
  | 参数名 | 类型 | 必填 | 说明 |
  |-------|------|------|------|
  | userId | String | 是 | 用户ID |
- **响应数据**:
  ```json
  {
    "code": 200,
    "msg": "操作成功",
    "data": [
      {
        "landId": 1,
        "landName": "一号地块",
        "ownerType": "个人",
        "adCode": "110101",
        "detailAddress": "北京市朝阳区某某街道123号",
        "areaSize": 10.50,
        "landType": "耕地",
        "currentStatus": "正常",
        "latitude": 39.904211,
        "longitude": 116.407395,
        "farmerUserId": "2",
        "createBy": "2",
        "createTime": "2025-01-01T00:00:00",
        "updateTime": "2025-01-01T00:00:00",
        "remark": "优质耕地"
      }
    ]
  }
  ```

#### 3.2.3 添加土地信息
- **接口地址**: `POST /api/land/add`
- **请求方式**: POST
- **请求参数**:
  | 参数名 | 类型 | 必填 | 说明 |
  |-------|------|------|------|
  | landName | String | 是 | 地块名称 |
  | ownerType | String | 是 | 土地权属 |
  | adCode | String | 否 | 行政区划代码 |
  | detailAddress | String | 否 | 详细地址 |
  | areaSize | BigDecimal | 否 | 地块面积 |
  | landType | String | 否 | 地块类型 |
  | currentStatus | String | 否 | 当前状态 |
  | latitude | BigDecimal | 否 | 纬度 |
  | longitude | BigDecimal | 否 | 经度 |
  | farmerUserId | String | 是 | 关联农民用户ID |
  | createBy | String | 否 | 创建人ID |
  | remark | String | 否 | 备注 |
- **响应数据**:
  ```json
  {
    "code": 200,
    "msg": "添加土地信息成功",
    "data": {
      "landId": 1,
      "landName": "一号地块",
      "ownerType": "个人"
    }
  }
  ```
- **响应说明**: 返回新增后的土地信息

#### 3.2.4 更新土地信息
- **接口地址**: `POST /api/land/update`
- **请求方式**: POST
- **请求参数**:
  | 参数名 | 类型 | 必填 | 说明 |
  |-------|------|------|------|
  | landId | Long | 是 | 土地ID |
  | landName | String | 是 | 地块名称 |
  | ownerType | String | 是 | 土地权属 |
  | adCode | String | 否 | 行政区划代码 |
  | detailAddress | String | 否 | 详细地址 |
  | areaSize | BigDecimal | 否 | 地块面积 |
  | landType | String | 否 | 地块类型 |
  | currentStatus | String | 否 | 当前状态 |
  | latitude | BigDecimal | 否 | 纬度 |
  | longitude | BigDecimal | 否 | 经度 |
  | farmerUserId | String | 是 | 关联农民用户ID |
  | remark | String | 否 | 备注 |
- **响应数据**:
  ```json
  {
    "code": 200,
    "msg": "更新土地信息成功",
    "data": {
      "landId": 1,
      "landName": "一号地块",
      "ownerType": "个人"
    }
  }
  ```
- **响应说明**: 返回更新后的土地信息

#### 3.2.5 删除土地信息
- **接口地址**: `POST /api/land/delete/{landId}`
- **请求方式**: POST
- **请求参数**:
  | 参数名 | 类型 | 必填 | 说明 |
  |-------|------|------|------|
  | landId | Long | 是 | 土地ID |
- **响应数据**:
  ```json
  {
    "code": 200,
    "msg": "删除土地信息成功"
  }
  ```
- **响应说明**: 删除成功返回成功消息,失败返回错误消息

### 3.3 农民认证相关接口

#### 3.3.1 农民认证申请
- **接口地址**: `POST /api/farmer/certification/apply`
- **请求方式**: POST
- **请求参数**:
  | 参数名 | 类型 | 必填 | 说明 |
  |-------|------|------|------|
  | userId | String | 是 | 用户ID |
  | realName | String | 是 | 真实姓名 |
  | idCard | String | 是 | 身份证号(18位) |
  | adCode | String | 否 | 行政区划代码 |
  | farmType | String | 否 | 种植类型 |
  | certDocPath | String | 否 | 证明文件路径 |
  | detailAddress | String | 否 | 详细地址 |
- **请求示例**:
  ```json
  {
    "userId": "2",
    "realName": "张三",
    "idCard": "110101199001011234",
    "adCode": "110101",
    "farmType": "小麦种植",
    "certDocPath": "/upload/2025/01/cert_doc.jpg",
    "detailAddress": "北京市朝阳区某某街道123号"
  }
  ```
- **响应数据**:
  ```json
  {
    "code": 200,
    "msg": "认证申请提交成功，请等待审核",
    "data": {
      "certId": 1,
      "userId": "2",
      "realName": "张三",
      "idCard": "110101199001011234",
      "adCode": "110101",
      "farmType": "小麦种植",
      "certDocPath": "/upload/2025/01/cert_doc.jpg",
      "detailAddress": "北京市朝阳区某某街道123号",
      "applyTime": "2025-01-15T10:30:00",
      "status": 1
    }
  }
  ```
- **响应说明**:
  - status自动设置为1(审核中)
  - applyTime自动设置为当前时间
  - 身份证号必须为18位且符合规范
  - 重复申请会返回错误提示
- **错误响应**:
  ```json
  {
    "code": 500,
    "msg": "身份证号格式不正确"
  }
  ```
  或
  ```json
  {
    "code": 500,
    "msg": "您的认证申请正在审核中，请勿重复提交"
  }
  ```

#### 3.3.2 根据用户ID查询认证状态
- **接口地址**: `GET /api/farmer/certification/user/{userId}`
- **请求方式**: GET
- **请求参数**:
  | 参数名 | 类型 | 必填 | 说明 |
  |-------|------|------|------|
  | userId | String | 是 | 用户ID |
- **响应数据**:
  ```json
  {
    "code": 200,
    "msg": "操作成功",
    "data": {
      "certId": 1,
      "userId": "2",
      "realName": "张三",
      "idCard": "110101199001011234",
      "adCode": "110101",
      "farmType": "小麦种植",
      "certDocPath": "/certs/zhangsan_idcard.jpg",
      "detailAddress": "北京市朝阳区某某街道123号",
      "applyTime": "2025-01-01T00:00:00",
      "status": 2,
      "approverId": "1",
      "approveTime": "2025-01-01T00:00:00",
      "rejectReason": null
    }
  }
  ```
- **响应说明**:
  - status: 1表示审核中，2表示已通过，0表示未通过
  - 未找到认证信息时返回错误: `{"code": 500, "msg": "未找到认证信息"}`

#### 3.3.3 获取待审批列表
- **接口地址**: `GET /api/farmer/certification/pending`
- **请求方式**: GET
- **请求参数**: 无
- **响应数据**:
  ```json
  {
    "code": 200,
    "msg": "操作成功",
    "data": [
      {
        "certId": 1,
        "userId": "2",
        "realName": "张三",
        "idCard": "110101199001011234",
        "adCode": "110101",
        "farmType": "小麦种植",
        "certDocPath": "/certs/zhangsan_idcard.jpg",
        "detailAddress": "北京市朝阳区某某街道123号",
        "applyTime": "2025-01-01T00:00:00",
        "status": 1,
        "approverId": null,
        "approveTime": null,
        "rejectReason": null
      }
    ]
  }
  ```

#### 3.3.4 审批通过
- **接口地址**: `POST /api/farmer/certification/{certId}/approve`
- **请求方式**: POST
- **请求参数**:
  | 参数名 | 类型 | 必填 | 说明 |
  |-------|------|------|------|
  | certId | Long | 是 | 认证ID |
  | approverId | String | 是 | 审批人ID |
- **响应数据**:
  ```json
  {
    "code": 200,
    "msg": "审批通过成功"
  }
  ```
- **响应说明**: 审批成功返回成功消息,失败返回错误消息

#### 3.3.5 审批驳回
- **接口地址**: `POST /api/farmer/certification/{certId}/reject`
- **请求方式**: POST
- **请求参数**:
  | 参数名 | 类型 | 必填 | 说明 |
  |-------|------|------|------|
  | certId | Long | 是 | 认证ID |
  | approverId | String | 是 | 审批人ID |
  | rejectReason | String | 是 | 驳回原因 |
- **响应数据**:
  ```json
  {
    "code": 200,
    "msg": "审批驳回成功"
  }
  ```
- **响应说明**: 审批驳回成功返回成功消息,失败返回错误消息

## 4. 数据字典

### 4.1 用户状态
| 值 | 说明 |
|----|------|
| 0 | 禁用 |
| 1 | 正常 |

### 4.2 认证状态
| 值 | 说明 |
|----|------|
| 0 | 未通过 |
| 1 | 审核中 |
| 2 | 已通过 |

## 5. 错误码说明
| 错误码 | 说明 |
|-------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |