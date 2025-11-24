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
  | userId | Long | 是 | 用户ID |
- **响应数据**:
  ```json
  {
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
  ```
- **响应说明**: 
  - status: 1表示正常，0表示禁用

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
  true
  ```
- **响应说明**: true表示更新成功，false表示更新失败

#### 3.1.3 修改密码
- **接口地址**: `POST /api/user/{userId}/password`
- **请求方式**: POST
- **请求参数**:
  | 参数名 | 类型 | 必填 | 说明 |
  |-------|------|------|------|
  | userId | Long | 是 | 用户ID |
  | newPassword | String | 是 | 新密码 |
- **响应数据**:
  ```json
  true
  ```
- **响应说明**: true表示修改成功，false表示修改失败

### 3.2 土地信息相关接口

#### 3.2.1 根据用户ID获取土地列表
- **接口地址**: `GET /api/land/user/{userId}`
- **请求方式**: GET
- **请求参数**:
  | 参数名 | 类型 | 必填 | 说明 |
  |-------|------|------|------|
  | userId | Long | 是 | 用户ID |
- **响应数据**:
  ```json
  [
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
      "farmerUserId": 2,
      "createBy": 2,
      "createTime": "2025-01-01T00:00:00",
      "updateTime": "2025-01-01T00:00:00",
      "remark": "优质耕地"
    }
  ]
  ```

#### 3.2.2 添加土地信息
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
  | farmerUserId | Long | 是 | 关联农民用户ID |
  | createBy | Long | 否 | 创建人ID |
  | remark | String | 否 | 备注 |
- **响应数据**:
  ```json
  true
  ```
- **响应说明**: true表示添加成功，false表示添加失败

#### 3.2.3 更新土地信息
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
  | farmerUserId | Long | 是 | 关联农民用户ID |
  | remark | String | 否 | 备注 |
- **响应数据**:
  ```json
  true
  ```
- **响应说明**: true表示更新成功，false表示更新失败

#### 3.2.4 删除土地信息
- **接口地址**: `POST /api/land/delete/{landId}`
- **请求方式**: POST
- **请求参数**:
  | 参数名 | 类型 | 必填 | 说明 |
  |-------|------|------|------|
  | landId | Long | 是 | 土地ID |
- **响应数据**:
  ```json
  true
  ```
- **响应说明**: true表示删除成功，false表示删除失败

### 3.3 农民认证相关接口

#### 3.3.1 根据用户ID查询认证状态
- **接口地址**: `GET /api/farmer/certification/user/{userId}`
- **请求方式**: GET
- **请求参数**:
  | 参数名 | 类型 | 必填 | 说明 |
  |-------|------|------|------|
  | userId | Long | 是 | 用户ID |
- **响应数据**:
  ```json
  {
    "certId": 1,
    "userId": 2,
    "realName": "张三",
    "idCard": "110101199001011234",
    "adCode": "110101",
    "farmType": "小麦种植",
    "certDocPath": "/certs/zhangsan_idcard.jpg",
    "detailAddress": "北京市朝阳区某某街道123号",
    "applyTime": "2025-01-01T00:00:00",
    "status": 2,
    "approverId": 1,
    "approveTime": "2025-01-01T00:00:00",
    "rejectReason": null
  }
  ```
- **响应说明**: 
  - status: 1表示审核中，2表示已通过，0表示未通过

#### 3.3.2 获取待审批列表
- **接口地址**: `GET /api/farmer/certification/pending`
- **请求方式**: GET
- **请求参数**: 无
- **响应数据**:
  ```json
  [
    {
      "certId": 1,
      "userId": 2,
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
  ```

#### 3.3.3 审批通过
- **接口地址**: `POST /api/farmer/certification/{certId}/approve`
- **请求方式**: POST
- **请求参数**:
  | 参数名 | 类型 | 必填 | 说明 |
  |-------|------|------|------|
  | certId | Long | 是 | 认证ID |
  | approverId | Long | 是 | 审批人ID |
- **响应数据**:
  ```json
  true
  ```
- **响应说明**: true表示审批通过成功，false表示审批通过失败

#### 3.3.4 审批驳回
- **接口地址**: `POST /api/farmer/certification/{certId}/reject`
- **请求方式**: POST
- **请求参数**:
  | 参数名 | 类型 | 必填 | 说明 |
  |-------|------|------|------|
  | certId | Long | 是 | 认证ID |
  | approverId | Long | 是 | 审批人ID |
  | rejectReason | String | 是 | 驳回原因 |
- **响应数据**:
  ```json
  true
  ```
- **响应说明**: true表示审批驳回成功，false表示审批驳回失败

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