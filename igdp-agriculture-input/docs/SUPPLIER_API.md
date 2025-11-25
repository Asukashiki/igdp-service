# 供应商管理模块 API 文档

## 基础路径
```
/supplier/cert
```

---

## 接口列表

### 1. 供应商认证申请

#### 接口信息
- **接口路径**: `/supplier/cert/apply`
- **请求方法**: `POST`
- **接口描述**: 已注册未认证的供应商用户提交认证申请
- **使用对象**: 已注册但未完成供应商身份认证的用户

#### 请求参数（Body - JSON）

| 字段名 | 数据类型 | 是否必填 | 描述 | 校验规则 | 示例值 |
|--------|---------|---------|------|---------|--------|
| userId | Long | 是 | 用户ID（登录后获取） | 必须关联用户表存在的记录 | 10001 |
| orgName | String | 是 | 企业/组织名称 | 长度1-100字符 | XX科技有限公司 |
| creditCode | String | 是 | 统一社会信用代码 | 18位字符，符合社会信用代码格式 | 91110105MA01G51234 |
| legalPerson | String | 是 | 法定代表人/负责人 | 长度1-50字符 | 张三 |
| legalId | String | 是 | 法定代表人身份证号 | 18位，符合身份证格式（含X） | 110101199001011234 |
| adCode | String | 是 | 行政区划代码 | 关联行政区划表存在的代码 | 110105 |
| businessScope | String | 是 | 经营范围/主要产品 | 长度1-255字符 | 电子产品研发、销售 |
| licensePath | String | 是 | 营业执照存储路径 | 文件上传后返回的路径 | /uploads/licenses/xxx.jpg |
| contactName | String | 是 | 联系人姓名 | 长度1-50字符 | 李四 |
| contactPhone | String | 是 | 联系人手机 | 11位手机号格式 | 13800138000 |

#### 请求示例

```json
{
  "userId": 10001,
  "orgName": "XX科技有限公司",
  "creditCode": "91110105MA01G51234",
  "legalPerson": "张三",
  "legalId": "110101199001011234",
  "adCode": "110105",
  "businessScope": "电子产品研发、销售",
  "licensePath": "/uploads/licenses/xxx.jpg",
  "contactName": "李四",
  "contactPhone": "13800138000"
}
```

#### 响应示例（成功）

```json
{
  "code": 200,
  "msg": "供应商认证申请提交成功，当前状态：审核中",
  "data": {
    "certId": 20001,
    "status": 1,
    "statusDesc": "审核中",
    "applyTime": "2025-11-25 14:30:00"
  }
}
```

#### 错误示例

```json
{
  "code": 500,
  "msg": "您已提交过认证申请，请勿重复提交",
  "data": null
}
```

```json
{
  "code": 500,
  "msg": "该统一社会信用代码已存在",
  "data": null
}
```

---

### 2. 供应商认证审批

#### 接口信息
- **接口路径**: `/supplier/cert/approve/{certId}`
- **请求方法**: `PUT`
- **接口描述**: 审核人员审批供应商认证申请
- **使用对象**: 系统审核人员（管理员）

#### 请求参数

| 类型 | 字段名 | 数据类型 | 是否必填 | 描述 | 校验规则 | 示例值 |
|------|--------|---------|---------|------|---------|--------|
| 路径参数 | certId | Long | 是 | 认证ID | 必须关联供应商认证表存在的记录 | 20001 |
| Body参数 | approverId | Long | 是 | 审批人ID | 必须是审核人员角色的用户ID | 30001 |
| Body参数 | auditResult | Integer | 是 | 审核结果 | 1-通过，0-驳回 | 1 |
| Body参数 | auditOpinion | String | 是 | 审核意见/驳回原因 | 长度1-255字符 | 材料齐全，符合要求 |

#### 请求示例（审核通过）

```json
PUT /supplier/cert/approve/20001

{
  "approverId": 30001,
  "auditResult": 1,
  "auditOpinion": "材料齐全，符合要求"
}
```

#### 请求示例（审核驳回）

```json
PUT /supplier/cert/approve/20001

{
  "approverId": 30001,
  "auditResult": 0,
  "auditOpinion": "营业执照信息与企业名称不一致"
}
```

#### 响应示例（审核通过）

```json
{
  "code": 200,
  "msg": "审核已通过",
  "data": {
    "certId": 20001,
    "status": 2,
    "statusDesc": "已通过",
    "approveTime": "2025-11-26 10:15:00",
    "approverId": 30001,
    "rejectReason": null
  }
}
```

#### 响应示例（审核驳回）

```json
{
  "code": 200,
  "msg": "审核已驳回",
  "data": {
    "certId": 20001,
    "status": 0,
    "statusDesc": "未通过",
    "approveTime": "2025-11-26 10:20:00",
    "approverId": 30001,
    "rejectReason": "营业执照信息与企业名称不一致"
  }
}
```

#### 错误示例

```json
{
  "code": 500,
  "msg": "认证记录不存在",
  "data": null
}
```

```json
{
  "code": 500,
  "msg": "该认证记录不是审核中状态，无法审批",
  "data": null
}
```

---

### 3. 查询认证状态

#### 接口信息
- **接口路径**: `/supplier/cert/status/{userId}`
- **请求方法**: `GET`
- **接口描述**: 查询指定用户的认证状态
- **使用对象**: 已提交认证申请的用户

#### 请求参数

| 类型 | 字段名 | 数据类型 | 是否必填 | 描述 | 示例值 |
|------|--------|---------|---------|------|--------|
| 路径参数 | userId | Long | 是 | 用户ID | 10001 |

#### 请求示例

```
GET /supplier/cert/status/10001
```

#### 响应示例（成功）

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "certId": 20001,
    "userId": 10001,
    "orgName": "XX科技有限公司",
    "creditCode": "91110105MA01G51234",
    "status": 1,
    "statusDesc": "审核中",
    "applyTime": "2025-11-25 14:30:00",
    "approveTime": null,
    "auditOpinion": null,
    "rejectReason": null,
    "approverId": null
  }
}
```

#### 错误示例

```json
{
  "code": 500,
  "msg": "未找到认证记录",
  "data": null
}
```

---

### 4. 查询待审核列表（分页）

#### 接口信息
- **接口路径**: `/supplier/cert/audit/list`
- **请求方法**: `GET`
- **接口描述**: 查询待审核的供应商认证申请列表
- **使用对象**: 系统审核人员（管理员）

#### 请求参数

| 字段名 | 数据类型 | 是否必填 | 描述 | 默认值 | 示例值 |
|--------|---------|---------|------|--------|--------|
| page | Integer | 否 | 页码 | 1 | 1 |
| pageSize | Integer | 否 | 每页数量 | 10 | 10 |

#### 请求示例

```
GET /supplier/cert/audit/list?page=1&pageSize=10
```

#### 响应示例（成功）

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "list": [
      {
        "certId": 20001,
        "userId": 10001,
        "orgName": "XX科技有限公司",
        "creditCode": "91110105MA01G51234",
        "legalPerson": "张三",
        "legalId": "110101199001011234",
        "adCode": "110105",
        "businessScope": "电子产品研发、销售",
        "licensePath": "/uploads/licenses/xxx.jpg",
        "contactName": "李四",
        "contactPhone": "13800138000",
        "applyTime": "2025-11-25 14:30:00",
        "status": 1,
        "approverId": null,
        "approveTime": null,
        "auditOpinion": null,
        "rejectReason": null,
        "createPeople": "system",
        "createTime": "2025-11-25 14:30:00",
        "updatePeople": null,
        "updateTime": null,
        "delFlag": "0"
      }
    ],
    "total": 1,
    "page": 1,
    "pageSize": 10
  }
}
```

---

### 5. 查询认证详情

#### 接口信息
- **接口路径**: `/supplier/cert/{certId}`
- **请求方法**: `GET`
- **接口描述**: 根据认证ID查询认证详细信息
- **使用对象**: 系统审核人员、申请用户

#### 请求参数

| 类型 | 字段名 | 数据类型 | 是否必填 | 描述 | 示例值 |
|------|--------|---------|---------|------|--------|
| 路径参数 | certId | Long | 是 | 认证ID | 20001 |

#### 请求示例

```
GET /supplier/cert/20001
```

#### 响应示例（成功）

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "certId": 20001,
    "userId": 10001,
    "orgName": "XX科技有限公司",
    "creditCode": "91110105MA01G51234",
    "legalPerson": "张三",
    "legalId": "110101199001011234",
    "adCode": "110105",
    "businessScope": "电子产品研发、销售",
    "licensePath": "/uploads/licenses/xxx.jpg",
    "contactName": "李四",
    "contactPhone": "13800138000",
    "applyTime": "2025-11-25 14:30:00",
    "status": 2,
    "approverId": 30001,
    "approveTime": "2025-11-26 10:15:00",
    "auditOpinion": "材料齐全，符合要求",
    "rejectReason": null,
    "createPeople": "system",
    "createTime": "2025-11-25 14:30:00",
    "updatePeople": "admin",
    "updateTime": "2025-11-26 10:15:00",
    "delFlag": "0"
  }
}
```

#### 错误示例

```json
{
  "code": 500,
  "msg": "认证记录不存在",
  "data": null
}
```

---

## 数据字典

### 认证状态（status）

| 值 | 描述 |
|----|------|
| 0 | 未通过 |
| 1 | 审核中 |
| 2 | 已通过 |

### 审核结果（auditResult）

| 值 | 描述 |
|----|------|
| 0 | 驳回 |
| 1 | 通过 |

### 删除标志（delFlag）

| 值 | 描述 |
|----|------|
| 0 | 正常 |
| 2 | 删除 |

---

## 业务流程说明

### 供应商认证申请流程

1. 用户填写认证申请信息
2. 系统校验用户是否已提交认证申请（排除状态为0的记录）
3. 系统校验统一社会信用代码唯一性
4. 系统创建认证记录，状态设置为"审核中"（1）
5. 返回认证申请响应信息

### 供应商认证审批流程

1. 审核人员查看待审核列表
2. 审核人员查看认证详情
3. 审核人员判断资料是否符合要求
4. 若符合，选择"通过"，填写审核意见
   - 系统更新认证状态为"已通过"（2）
   - 记录审核意见、审批人ID、审批时间
5. 若不符合，选择"驳回"，填写驳回原因
   - 系统更新认证状态为"未通过"（0）
   - 记录驳回原因、审批人ID、审批时间

---

## 错误码说明

| 错误码 | 描述 |
|--------|------|
| 200 | 操作成功 |
| 500 | 系统错误或业务异常 |

---

## 注意事项

1. **唯一性校验**：
   - 用户不可重复提交认证申请（排除状态为0的记录）
   - 统一社会信用代码必须唯一

2. **状态管理**：
   - 只有状态为"审核中"（1）的记录才能进行审批
   - 审批通过后状态变为"已通过"（2）
   - 审批驳回后状态变为"未通过"（0）

3. **数据安全**：
   - 所有接口需要进行身份认证
   - 审批接口需要校验审批人角色权限

4. **逻辑删除**：
   - 删除操作为逻辑删除，delFlag设置为'2'
   - 查询时需过滤已删除记录（delFlag='0'）
