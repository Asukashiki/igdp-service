# 种子企业认证与品种管理模块 API 接口文档

## 文档说明

本文档提供完整的 API 接口调用示例，包含详细的请求和响应 JSON 数据结构，供前端开发人员参考使用。

**基础信息**：
- 服务地址：http://localhost:8080
- 接口前缀：/seed
- 请求格式：application/json;charset=utf-8
- 响应格式：application/json;charset=utf-8
- 权限验证：Sa-Token（需在请求头中携带 token）

---

## 一、企业认证接口

### 1.1 提交企业认证申请

**接口描述**：企业提交认证备案申请，填写企业基础信息和上传证明材料。

**接口地址**：`POST /seed/enterprise/certify/submit`

**权限标识**：`seed:enterprise:certify:submit`

**请求头**：
```http
Content-Type: application/json;charset=utf-8
Authorization: Bearer {token}
```

**请求参数**：
```json
{
  "enterpriseName": "奥罗米亚优质种子有限公司",
  "unifiedSocialCreditCode": "91530000MA6K3X8X9L",
  "enterpriseType": "integrated",
  "seedLicenseNo": "YNZZ2025001",
  "licenseStartDate": "2025-01-01",
  "licenseEndDate": "2030-12-31",
  "region": "奥罗米亚州",
  "zone": "东奥罗米亚区",
  "county": "阿达玛县",
  "township": "纳兹雷特乡",
  "detailedAddress": "工业园区A区18号",
  "businessScope": "农作物种子生产、加工、销售；种子进出口贸易",
  "annualProductionCapacity": 5000.00,
  "establishmentDate": "2020-05-10",
  "legalPersonName": "阿贝贝·比克拉",
  "legalPersonId": "ET123456789012345678",
  "contactPerson": "泰德塞·梅科宁",
  "contactPhone": "+251-911-234567",
  "contactEmail": "tedesse.mekonnen@example.com",
  "businessLicenseUrl": "/upload/license/2025/biz_91530000MA6K3X8X9L.pdf",
  "seedLicenseUrl": "/upload/license/2025/seed_YNZZ2025001.pdf",
  "taxRegistrationUrl": "/upload/license/2025/tax_91530000MA6K3X8X9L.pdf",
  "factoryLicenseUrl": "/upload/license/2025/factory_2025001.pdf",
  "operator": "泰德塞·梅科宁",
  "operationOrg": "奥罗米亚优质种子有限公司"
}
```

**请求参数说明**：

| 字段名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|------|------|--------|
| enterpriseName | String | 是 | 企业名称 | 奥罗米亚优质种子有限公司 |
| unifiedSocialCreditCode | String | 是 | 统一社会信用代码（唯一） | 91530000MA6K3X8X9L |
| enterpriseType | String | 是 | 企业类型：Production-oriented/trade-oriented/integrated | integrated |
| seedLicenseNo | String | 是 | 种子许可证编号（唯一） | YNZZ2025001 |
| licenseStartDate | Date | 是 | 许可证有效期起始日 | 2025-01-01 |
| licenseEndDate | Date | 是 | 许可证有效期截止日 | 2030-12-31 |
| region | String | 是 | 地区 | 奥罗米亚州 |
| zone | String | 是 | 区域 | 东奥罗米亚区 |
| county | String | 是 | 县 | 阿达玛县 |
| township | String | 是 | 乡 | 纳兹雷特乡 |
| detailedAddress | String | 是 | 完整地址 | 工业园区A区18号 |
| businessScope | String | 是 | 业务范围 | 农作物种子生产、加工、销售 |
| annualProductionCapacity | Number | 是 | 年生产能力（吨/年） | 5000.00 |
| establishmentDate | Date | 是 | 企业成立时间 | 2020-05-10 |
| legalPersonName | String | 是 | 法人姓名 | 阿贝贝·比克拉 |
| legalPersonId | String | 是 | 法人ID | ET123456789012345678 |
| contactPerson | String | 是 | 联系人姓名 | 泰德塞·梅科宁 |
| contactPhone | String | 是 | 联系电话 | +251-911-234567 |
| contactEmail | String | 否 | 邮箱 | tedesse.mekonnen@example.com |
| businessLicenseUrl | String | 是 | 营业执照存储路径 | /upload/license/2025/... |
| seedLicenseUrl | String | 是 | 种子许可证存储路径 | /upload/license/2025/... |
| taxRegistrationUrl | String | 是 | 税务登记证存储路径 | /upload/license/2025/... |
| factoryLicenseUrl | String | 是 | 工厂许可证存储路径 | /upload/license/2025/... |
| operator | String | 是 | 操作人 | 泰德塞·梅科宁 |
| operationOrg | String | 是 | 操作机构 | 奥罗米亚优质种子有限公司 |

**成功响应**：
```json
{
  "code": 200,
  "msg": "提交成功",
  "data": {
    "enterpriseId": "ENT4B8E9F2A3C1D5E6F",
    "certificationStatus": 0,
    "submitTime": "2025-11-25 14:30:25"
  }
}
```

// 保存草稿（新建）
POST /seed/enterprise/certify/save
{
  "enterpriseName": "示例种子公司",
  "unifiedSocialCreditCode": "91510100MA62X1234A",
  // ... 其他字段
}

// 响应
{
  "code": 200,
  "msg": "草稿保存成功",
  "data": {
    "enterpriseId": "ENTA1B2C3D4E5F6G7H8",
    "certificationStatus": -1,
    "saveTime": "2025-11-25T10:30:00"
  }
}

// 更新草稿
POST /seed/enterprise/certify/save
{
  "enterpriseId": "ENTA1B2C3D4E5F6G7H8",
  "enterpriseName": "示例种子公司（已修改）",
  // ... 其他更新字段
}


**响应字段说明**：

| 字段名 | 类型 | 说明 |
|-------|------|------|
| code | Integer | 状态码：200-成功，500-失败 |
| msg | String | 提示消息 |
| data.enterpriseId | String | 企业唯一标识（系统生成） |
| data.certificationStatus | Integer | 认证状态：0-待审核 |
| data.submitTime | String | 提交时间 |

**错误响应示例**：
```json
{
  "code": 500,
  "msg": "统一社会信用代码已存在，请核对",
  "data": null
}
```

**常见错误**：
- `统一社会信用代码已存在，请核对`
- `种子许可证编号已存在，请核对`

---

### 1.2 查询企业认证申请列表

**接口描述**：查询企业认证申请列表，支持多条件筛选。

**接口地址**：`GET /seed/enterprise/certify/list`

**权限标识**：`seed:enterprise:certify:list`

**请求参数**：
```
GET /seed/enterprise/certify/list?keyword=种子&enterpriseType=integrated&certificationStatus=0
```

**请求参数说明**：

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|------|------|--------|
| keyword | String | 否 | 关键词（企业名称/信用代码/许可证编号） | 种子 |
| enterpriseType | String | 否 | 企业类型 | integrated |
| certificationStatus | Integer | 否 | 认证状态：0-待审核/1-通过/2-驳回 | 0 |

**成功响应**：
```json
{
  "code": 200,
  "msg": "查询成功",
  "data": {
    "total": 15,
    "list": [
      {
        "enterpriseId": "ENT4B8E9F2A3C1D5E6F",
        "enterpriseName": "奥罗米亚优质种子有限公司",
        "unifiedSocialCreditCode": "91530000MA6K3X8X9L",
        "seedLicenseNo": "YNZZ2025001",
        "enterpriseType": "integrated",
        "region": "奥罗米亚州",
        "certificationStatus": 0,
        "operator": "泰德塞·梅科宁",
        "operationTime": "2025-11-25 14:30:25",
        "createTime": "2025-11-25 14:30:25"
      },
      {
        "enterpriseId": "ENT7C5D8A4B2E1F3G6H",
        "enterpriseName": "阿法尔种业股份公司",
        "unifiedSocialCreditCode": "91530000MA7K4Y9Z0M",
        "seedLicenseNo": "YNZZ2025002",
        "enterpriseType": "Production-oriented",
        "region": "阿法尔州",
        "certificationStatus": 1,
        "operator": "穆罕默德·阿里",
        "operationTime": "2025-11-20 10:15:00",
        "createTime": "2025-11-20 10:15:00"
      }
    ]
  }
}
```

---

### 1.3 查询企业认证详情

**接口描述**：根据企业ID查询认证申请的详细信息。

**接口地址**：`GET /seed/enterprise/certify/{enterpriseId}`

**权限标识**：`seed:enterprise:certify:query`

**请求示例**：
```
GET /seed/enterprise/certify/ENT4B8E9F2A3C1D5E6F
```

**成功响应**：
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "enterpriseId": "ENT4B8E9F2A3C1D5E6F",
    "enterpriseName": "奥罗米亚优质种子有限公司",
    "unifiedSocialCreditCode": "91530000MA6K3X8X9L",
    "enterpriseType": "integrated",
    "seedLicenseNo": "YNZZ2025001",
    "licenseStartDate": "2025-01-01",
    "licenseEndDate": "2030-12-31",
    "region": "奥罗米亚州",
    "zone": "东奥罗米亚区",
    "county": "阿达玛县",
    "township": "纳兹雷特乡",
    "detailedAddress": "工业园区A区18号",
    "businessScope": "农作物种子生产、加工、销售；种子进出口贸易",
    "annualProductionCapacity": 5000.00,
    "establishmentDate": "2020-05-10",
    "legalPersonName": "阿贝贝·比克拉",
    "legalPersonId": "ET123456789012345678",
    "contactPerson": "泰德塞·梅科宁",
    "contactPhone": "+251-911-234567",
    "contactEmail": "tedesse.mekonnen@example.com",
    "businessLicenseUrl": "/upload/license/2025/biz_91530000MA6K3X8X9L.pdf",
    "seedLicenseUrl": "/upload/license/2025/seed_YNZZ2025001.pdf",
    "taxRegistrationUrl": "/upload/license/2025/tax_91530000MA6K3X8X9L.pdf",
    "factoryLicenseUrl": "/upload/license/2025/factory_2025001.pdf",
    "operator": "泰德塞·梅科宁",
    "operationOrg": "奥罗米亚优质种子有限公司",
    "operationTime": "2025-11-25 14:30:25",
    "certificationStatus": 0,
    "createTime": "2025-11-25 14:30:25",
    "updateTime": null
  }
}
```

**错误响应**：
```json
{
  "code": 500,
  "msg": "企业信息不存在",
  "data": null
}
```

---

## 二、企业审核接口

### 2.1 处理企业审核

**接口描述**：审核人员对企业认证申请进行审核（通过/驳回）。

**接口地址**：`POST /seed/enterprise/audit/handle`

**权限标识**：`seed:enterprise:audit:handle`

**请求参数（审核通过）**：
```json
{
  "enterpriseId": "ENT4B8E9F2A3C1D5E6F",
  "auditResult": 1,
  "auditOpinion": "企业资质齐全，信息真实有效，符合认证要求，准予通过。",
  "auditor": "王审核",
  "auditStage": "Initial review"
}
```

**请求参数（审核驳回）**：
```json
{
  "enterpriseId": "ENT4B8E9F2A3C1D5E6F",
  "auditResult": 2,
  "auditOpinion": "营业执照复印件模糊不清，无法辨识企业名称和注册信息",
  "rejectReason": "营业执照复印件未加盖企业公章，且图片不清晰，需重新上传高清版本并加盖公章",
  "auditor": "王审核",
  "auditStage": "Initial review"
}
```

**请求参数说明**：

| 字段名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| enterpriseId | String | 是 | 企业唯一标识 |
| auditResult | Integer | 是 | 审核结果：1-通过/2-驳回 |
| auditOpinion | String | 否 | 审核意见 |
| rejectReason | String | 否 | 驳回原因（驳回时必填） |
| auditor | String | 是 | 审核人 |
| auditStage | String | 是 | 审核阶段：Initial review/re-review/final review |

**成功响应**：
```json
{
  "code": 200,
  "msg": "审核完成",
  "data": {
    "enterpriseId": "ENT4B8E9F2A3C1D5E6F",
    "certificationStatus": 1,
    "auditTime": "2025-11-25 15:45:30"
  }
}
```

**错误响应**：
```json
{
  "code": 500,
  "msg": "驳回时必须填写驳回原因",
  "data": null
}
```

---

### 2.2 查询企业审核列表

**接口描述**：查询企业审核记录列表。

**接口地址**：`GET /seed/enterprise/audit/list`

**权限标识**：`seed:enterprise:audit:list`

**请求示例**：
```
GET /seed/enterprise/audit/list?enterpriseId=ENT4B8E9F2A3C1D5E6F&auditResult=1
```

**成功响应**：
```json
{
  "code": 200,
  "msg": "查询成功",
  "data": {
    "total": 8,
    "list": [
      {
        "auditId": "AUD2F4E8B5C9A1D3E7F",
        "enterpriseId": "ENT4B8E9F2A3C1D5E6F",
        "auditResult": 1,
        "auditOpinion": "企业资质齐全，信息真实有效，符合认证要求，准予通过。",
        "rejectReason": null,
        "auditor": "王审核",
        "auditTime": "2025-11-25 15:45:30",
        "auditStage": "Initial review",
        "createTime": "2025-11-25 15:45:30"
      }
    ]
  }
}
```

---

### 2.3 查询待审核企业列表

**接口描述**：查询所有认证状态为"待审核"的企业列表。

**接口地址**：`GET /seed/enterprise/audit/pending`

**权限标识**：`seed:enterprise:audit:pending`

**请求示例**：
```
GET /seed/enterprise/audit/pending?keyword=种子
```

**成功响应**：
```json
{
  "code": 200,
  "msg": "查询成功",
  "data": {
    "total": 5,
    "list": [
      {
        "enterpriseId": "ENT4B8E9F2A3C1D5E6F",
        "enterpriseName": "奥罗米亚优质种子有限公司",
        "unifiedSocialCreditCode": "91530000MA6K3X8X9L",
        "seedLicenseNo": "YNZZ2025001",
        "enterpriseType": "integrated",
        "certificationStatus": 0,
        "operationTime": "2025-11-25 14:30:25"
      }
    ]
  }
}
```

---

## 三、品种登记接口

### 3.1 提交品种登记申请

**接口描述**：企业提交品种登记申请（前提：企业已完成认证）。

**接口地址**：`POST /seed/variety/registration/submit`

**权限标识**：`seed:variety:registration:submit`

**前置条件**：企业认证状态必须为"通过"（certificationStatus = 1）

**请求参数**：
```json
{
  "enterpriseId": "ENT4B8E9F2A3C1D5E6F",
  "enterpriseName": "奥罗米亚优质种子有限公司",
  "unifiedSocialCreditCode": "91530000MA6K3X8X9L",
  "enterpriseType": "integrated",
  "seedLicenseNo": "YNZZ2025001",
  "recordType": "新品种登记",
  "recordDate": "2025-11-25",
  "varietyName": "高产抗旱玉米2025",
  "varietyCode": "YM2025001",
  "cropType": "玉米",
  "species": "玉米",
  "genus": "玉蜀黍属",
  "family": "禾本科",
  "breedingMethod": "杂交育种",
  "methodPedigree": "父本：抗旱系A × 母本：高产系B",
  "breedingYear": 2023,
  "minYieldPotential": 80.00,
  "maxYieldPotential": 120.00,
  "diseaseResistance": "抗大斑病、小斑病、玉米螟",
  "stressResistance": "耐旱、耐贫瘠、耐高温",
  "growthPeriod": 125,
  "plantHeight": 260.00,
  "grainQualityTraits": "容重750g/L，粗蛋白含量10.8%，粗脂肪含量4.2%，赖氨酸含量0.35%",
  "testLocation": "奥罗米亚州农业科学研究院试验站、阿达玛县示范基地、东奥罗米亚区良种繁育中心",
  "testYear": 2024,
  "averageYield": 95.50,
  "stabilityScore": 4.65,
  "testReportUrl": "/upload/variety/2025/report_YM2025001.pdf",
  "photoUrl": "/upload/variety/2025/photo_YM2025001.jpg",
  "approvalDocNo": "HZ2025001",
  "approvalOrg": "奥罗米亚州种子管理局",
  "approvalDate": "2025-11-01",
  "certificationDocUrl": "/upload/variety/2025/cert_YM2025001.pdf",
  "operator": "泰德塞·梅科宁",
  "operationOrg": "奥罗米亚优质种子有限公司"
}
```

**请求参数说明**：

| 字段名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| enterpriseId | String | 是 | 关联企业唯一标识 |
| enterpriseName | String | 是 | 企业名称（冗余） |
| unifiedSocialCreditCode | String | 是 | 统一社会信用代码（冗余） |
| enterpriseType | String | 是 | 企业类型（冗余） |
| seedLicenseNo | String | 是 | 种子许可证编号（冗余） |
| recordType | String | 是 | 备案类型 |
| recordDate | Date | 是 | 备案日期 |
| varietyName | String | 是 | 品种名称 |
| varietyCode | String | 是 | 品种代码 |
| cropType | String | 是 | 作物类型 |
| species | String | 是 | 物种 |
| genus | String | 是 | 属 |
| family | String | 是 | 科 |
| breedingMethod | String | 是 | 育种方法 |
| methodPedigree | String | 否 | 方法系谱 |
| breedingYear | Integer | 是 | 培育年份 |
| minYieldPotential | Number | 是 | 最低产量潜力（公担/公顷） |
| maxYieldPotential | Number | 是 | 最高产量潜力（公担/公顷） |
| diseaseResistance | String | 是 | 抗病性 |
| stressResistance | String | 是 | 抗逆性 |
| growthPeriod | Integer | 是 | 生育期（天） |
| plantHeight | Number | 是 | 株高（厘米） |
| grainQualityTraits | String | 是 | 谷物质量性状 |
| testLocation | String | 是 | 试验地点 |
| testYear | Integer | 是 | 试验年份 |
| averageYield | Number | 是 | 平均产量 |
| stabilityScore | Number | 是 | 稳定性评分（0-5） |
| testReportUrl | String | 是 | 试验报告存储路径 |
| photoUrl | String | 否 | 照片存储路径 |
| approvalDocNo | String | 是 | 核准文件编号 |
| approvalOrg | String | 是 | 核准机构 |
| approvalDate | Date | 是 | 核准日期 |
| certificationDocUrl | String | 是 | 认证文件存储路径 |
| operator | String | 是 | 操作人 |
| operationOrg | String | 是 | 操作机构 |

**成功响应**：
```json
{
  "code": 200,
  "msg": "提交成功",
  "data": {
    "registrationId": "VAR_REG8A5C2E7B4D1F3G",
    "registrationNo": "VAR20251125678901",
    "recordStatus": 0,
    "submitTime": "2025-11-25 16:20:15"
  }
}
```

**错误响应**：
```json
{
  "code": 500,
  "msg": "企业尚未完成认证备案，无法提交品种登记申请",
  "data": null
}
```

---

### 3.2 查询品种登记列表

**接口描述**：查询品种登记申请列表，支持多条件筛选。

**接口地址**：`GET /seed/variety/registration/list`

**权限标识**：`seed:variety:registration:list`

**请求示例**：
```
GET /seed/variety/registration/list?varietyName=玉米&enterpriseName=种子&recordType=新品种登记
```

**成功响应**：
```json
{
  "code": 200,
  "msg": "查询成功",
  "data": {
    "total": 12,
    "list": [
      {
        "registrationId": "VAR_REG8A5C2E7B4D1F3G",
        "registrationNo": "VAR20251125678901",
        "enterpriseId": "ENT4B8E9F2A3C1D5E6F",
        "enterpriseName": "奥罗米亚优质种子有限公司",
        "varietyName": "高产抗旱玉米2025",
        "varietyCode": "YM2025001",
        "cropType": "玉米",
        "recordType": "新品种登记",
        "recordStatus": 0,
        "recordDate": "2025-11-25",
        "operator": "泰德塞·梅科宁",
        "operationTime": "2025-11-25 16:20:15",
        "createTime": "2025-11-25 16:20:15"
      },
      {
        "registrationId": "VAR_REG3C7F9A2D4E8B1G5",
        "registrationNo": "VAR20251120123456",
        "enterpriseId": "ENT7C5D8A4B2E1F3G6H",
        "enterpriseName": "阿法尔种业股份公司",
        "varietyName": "早熟高粱新品种",
        "varietyCode": "GZ2025002",
        "cropType": "高粱",
        "recordType": "新品种登记",
        "recordStatus": 1,
        "recordDate": "2025-11-20",
        "operator": "穆罕默德·阿里",
        "operationTime": "2025-11-20 11:30:00",
        "createTime": "2025-11-20 11:30:00"
      }
    ]
  }
}
```

---

### 3.3 查询品种登记详情

**接口描述**：根据登记ID查询品种登记的详细信息。

**接口地址**：`GET /seed/variety/registration/{registrationId}`

**权限标识**：`seed:variety:registration:query`

**请求示例**：
```
GET /seed/variety/registration/VAR_REG8A5C2E7B4D1F3G
```

**成功响应**：
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "registrationId": "VAR_REG8A5C2E7B4D1F3G",
    "registrationNo": "VAR20251125678901",
    "enterpriseId": "ENT4B8E9F2A3C1D5E6F",
    "enterpriseName": "奥罗米亚优质种子有限公司",
    "unifiedSocialCreditCode": "91530000MA6K3X8X9L",
    "enterpriseType": "integrated",
    "seedLicenseNo": "YNZZ2025001",
    "recordType": "新品种登记",
    "recordDate": "2025-11-25",
    "recordStatus": 0,
    "varietyName": "高产抗旱玉米2025",
    "varietyCode": "YM2025001",
    "cropType": "玉米",
    "species": "玉米",
    "genus": "玉蜀黍属",
    "family": "禾本科",
    "breedingMethod": "杂交育种",
    "methodPedigree": "父本：抗旱系A × 母本：高产系B",
    "breedingYear": 2023,
    "minYieldPotential": 80.00,
    "maxYieldPotential": 120.00,
    "diseaseResistance": "抗大斑病、小斑病、玉米螟",
    "stressResistance": "耐旱、耐贫瘠、耐高温",
    "growthPeriod": 125,
    "plantHeight": 260.00,
    "grainQualityTraits": "容重750g/L，粗蛋白含量10.8%，粗脂肪含量4.2%，赖氨酸含量0.35%",
    "testLocation": "奥罗米亚州农业科学研究院试验站、阿达玛县示范基地、东奥罗米亚区良种繁育中心",
    "testYear": 2024,
    "averageYield": 95.50,
    "stabilityScore": 4.65,
    "testReportUrl": "/upload/variety/2025/report_YM2025001.pdf",
    "photoUrl": "/upload/variety/2025/photo_YM2025001.jpg",
    "approvalDocNo": "HZ2025001",
    "approvalOrg": "奥罗米亚州种子管理局",
    "approvalDate": "2025-11-01",
    "certificationDocUrl": "/upload/variety/2025/cert_YM2025001.pdf",
    "operator": "泰德塞·梅科宁",
    "operationOrg": "奥罗米亚优质种子有限公司",
    "operationTime": "2025-11-25 16:20:15",
    "createTime": "2025-11-25 16:20:15",
    "updateTime": null
  }
}
```

---

## 四、品种审核接口

### 4.1 处理品种审核

**接口描述**：审核人员对品种登记申请进行审核（通过/驳回）。

**接口地址**：`POST /seed/variety/audit/handle`

**权限标识**：`seed:variety:audit:handle`

**请求参数（审核通过）**：
```json
{
  "registrationId": "VAR_REG8A5C2E7B4D1F3G",
  "enterpriseId": "ENT4B8E9F2A3C1D5E6F",
  "varietyName": "高产抗旱玉米2025",
  "auditResult": 1,
  "auditOpinion": "品种特性明确，试验数据完整，符合登记要求，准予通过。",
  "auditor": "李审核员",
  "auditStage": "初审"
}
```

**请求参数（审核驳回）**：
```json
{
  "registrationId": "VAR_REG8A5C2E7B4D1F3G",
  "enterpriseId": "ENT4B8E9F2A3C1D5E6F",
  "varietyName": "高产抗旱玉米2025",
  "auditResult": 2,
  "auditOpinion": "试验数据未标注检测机构资质，真实性存疑",
  "rejectReason": "试验报告缺乏资质机构盖章，未提供检测机构的资质证明文件，不符合登记规范要求。建议补充：1.检测机构资质证明；2.加盖检测机构公章的试验报告",
  "auditor": "李审核员",
  "auditStage": "初审"
}
```

**请求参数说明**：

| 字段名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| registrationId | String | 是 | 关联登记申请唯一标识 |
| enterpriseId | String | 是 | 关联企业唯一标识 |
| varietyName | String | 是 | 品种名称（冗余） |
| auditResult | Integer | 是 | 审核结果：1-通过/2-驳回 |
| auditOpinion | String | 否 | 审核意见 |
| rejectReason | String | 否 | 驳回原因（驳回时必填） |
| auditor | String | 是 | 审核人 |
| auditStage | String | 是 | 审核阶段：初审/复审/终审 |

**成功响应**：
```json
{
  "code": 200,
  "msg": "审核完成",
  "data": {
    "registrationId": "VAR_REG8A5C2E7B4D1F3G",
    "recordStatus": 1,
    "auditTime": "2025-11-25 17:10:45"
  }
}
```

**错误响应**：
```json
{
  "code": 500,
  "msg": "品种登记申请状态不是审核中，无法审核",
  "data": null
}
```

---

### 4.2 查询品种审核列表

**接口描述**：查询品种审核记录列表。

**接口地址**：`GET /seed/variety/audit/list`

**权限标识**：`seed:variety:audit:list`

**请求示例**：
```
GET /seed/variety/audit/list?varietyName=玉米&auditResult=1
```

**成功响应**：
```json
{
  "code": 200,
  "msg": "查询成功",
  "data": {
    "total": 6,
    "list": [
      {
        "auditId": "VAR_AUD5D3E8F2A9C1B4G7",
        "registrationId": "VAR_REG8A5C2E7B4D1F3G",
        "enterpriseId": "ENT4B8E9F2A3C1D5E6F",
        "varietyName": "高产抗旱玉米2025",
        "auditResult": 1,
        "auditOpinion": "品种特性明确，试验数据完整，符合登记要求，准予通过。",
        "rejectReason": null,
        "auditor": "李审核员",
        "auditTime": "2025-11-25 17:10:45",
        "auditStage": "初审",
        "createTime": "2025-11-25 17:10:45"
      }
    ]
  }
}
```

---

### 4.3 查询待审核品种列表

**接口描述**：查询所有备案状态为"审核中"的品种列表。

**接口地址**：`GET /seed/variety/audit/pending`

**权限标识**：`seed:variety:audit:pending`

**请求示例**：
```
GET /seed/variety/audit/pending?varietyName=玉米
```

**成功响应**：
```json
{
  "code": 200,
  "msg": "查询成功",
  "data": {
    "total": 3,
    "list": [
      {
        "registrationId": "VAR_REG8A5C2E7B4D1F3G",
        "registrationNo": "VAR20251125678901",
        "enterpriseName": "奥罗米亚优质种子有限公司",
        "varietyName": "高产抗旱玉米2025",
        "cropType": "玉米",
        "recordType": "新品种登记",
        "recordStatus": 0,
        "operationTime": "2025-11-25 16:20:15"
      }
    ]
  }
}
```

---

## 五、品种发布接口

### 5.1 发布品种

**接口描述**：管理人员将审核通过的品种发布到公示平台。

**接口地址**：`POST /seed/variety/publish/handle`

**权限标识**：`seed:variety:publish:handle`

**前置条件**：品种备案状态必须为"待发布"（recordStatus = 1）

**请求参数**：
```json
{
  "registrationId": "VAR_REG8A5C2E7B4D1F3G",
  "varietyName": "高产抗旱玉米2025",
  "cropType": "玉米",
  "publishDate": "2025-11-25",
  "publishDept": "奥罗米亚州种子管理局",
  "decisionExplanation": "该品种经审核，产量稳定性好，抗逆性强，符合区域种植规范，准予发布公示",
  "publicDescription": "高产抗旱玉米2025是经过三年区域试验选育的优质玉米品种，生育期125天，平均产量95.5公担/公顷，具有抗旱、抗病、高产稳产等特点，适宜在奥罗米亚州中部海拔1500-2200米区域种植。",
  "recommendedRegion": "奥罗米亚州东奥罗米亚区、西奥罗米亚区、阿达玛县、纳兹雷特县",
  "sowingGuide": "播种时间：4月中下旬至5月上旬；种植密度：每公顷4000-4500株；施肥建议：底肥每公顷施用复合肥300公斤，拔节期追施尿素150公斤；灌溉：生育期需水关键期为大喇叭口期和抽穗期，应保证灌溉；病虫害防治：注意防治玉米螟和大斑病。",
  "publishStatus": 1,
  "publisher": "张发布"
}
```

**请求参数说明**：

| 字段名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| registrationId | String | 是 | 关联登记申请唯一标识 |
| varietyName | String | 是 | 品种名称（冗余） |
| cropType | String | 是 | 作物类型（冗余） |
| publishDate | Date | 是 | 发布日期 |
| publishDept | String | 是 | 发布主管部门 |
| decisionExplanation | String | 否 | 决策说明 |
| publicDescription | String | 否 | 公开描述 |
| recommendedRegion | String | 否 | 推荐地区 |
| sowingGuide | String | 否 | 播种指南 |
| publishStatus | Integer | 是 | 公示状态：1-公示中 |
| publisher | String | 是 | 发布人 |

**成功响应**：
```json
{
  "code": 200,
  "msg": "发布成功",
  "data": {
    "publishId": "VAR_PUB9E2F5A8C4D1B3G7",
    "publishNo": "PUB20251125000001",
    "publishStatus": 1,
    "publishTime": "2025-11-25 18:25:30"
  }
}
```

**错误响应**：
```json
{
  "code": 500,
  "msg": "品种登记申请状态不是待发布，无法发布",
  "data": null
}
```

---

### 5.2 查询已发布品种列表

**接口描述**：查询已发布的品种列表（公示平台展示）。

**接口地址**：`GET /seed/variety/publish/list`

**权限标识**：`seed:variety:publish:list`

**请求示例**：
```
GET /seed/variety/publish/list?varietyName=玉米&cropType=玉米&publishStatus=1
```

**成功响应**：
```json
{
  "code": 200,
  "msg": "查询成功",
  "data": {
    "total": 8,
    "list": [
      {
        "publishId": "VAR_PUB9E2F5A8C4D1B3G7",
        "publishNo": "PUB20251125000001",
        "registrationId": "VAR_REG8A5C2E7B4D1F3G",
        "varietyName": "高产抗旱玉米2025",
        "cropType": "玉米",
        "publishDate": "2025-11-25",
        "publishDept": "奥罗米亚州种子管理局",
        "decisionExplanation": "该品种经审核，产量稳定性好，抗逆性强，符合区域种植规范，准予发布公示",
        "publicDescription": "高产抗旱玉米2025是经过三年区域试验选育的优质玉米品种...",
        "recommendedRegion": "奥罗米亚州东奥罗米亚区、西奥罗米亚区、阿达玛县、纳兹雷特县",
        "sowingGuide": "播种时间：4月中下旬至5月上旬...",
        "publishStatus": 1,
        "publisher": "张发布",
        "publishTime": "2025-11-25 18:25:30",
        "createTime": "2025-11-25 18:25:30"
      }
    ]
  }
}
```

---

### 5.3 查询品种发布详情

**接口描述**：根据发布ID查询品种发布的详细信息。

**接口地址**：`GET /seed/variety/publish/{publishId}`

**权限标识**：`seed:variety:publish:query`

**请求示例**：
```
GET /seed/variety/publish/VAR_PUB9E2F5A8C4D1B3G7
```

**成功响应**：
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "publishId": "VAR_PUB9E2F5A8C4D1B3G7",
    "publishNo": "PUB20251125000001",
    "registrationId": "VAR_REG8A5C2E7B4D1F3G",
    "varietyName": "高产抗旱玉米2025",
    "cropType": "玉米",
    "publishDate": "2025-11-25",
    "publishDept": "奥罗米亚州种子管理局",
    "decisionExplanation": "该品种经审核，产量稳定性好，抗逆性强，符合区域种植规范，准予发布公示",
    "publicDescription": "高产抗旱玉米2025是经过三年区域试验选育的优质玉米品种，生育期125天，平均产量95.5公担/公顷，具有抗旱、抗病、高产稳产等特点，适宜在奥罗米亚州中部海拔1500-2200米区域种植。",
    "recommendedRegion": "奥罗米亚州东奥罗米亚区、西奥罗米亚区、阿达玛县、纳兹雷特县",
    "sowingGuide": "播种时间：4月中下旬至5月上旬；种植密度：每公顷4000-4500株；施肥建议：底肥每公顷施用复合肥300公斤，拔节期追施尿素150公斤；灌溉：生育期需水关键期为大喇叭口期和抽穗期，应保证灌溉；病虫害防治：注意防治玉米螟和大斑病。",
    "publishStatus": 1,
    "publisher": "张发布",
    "publishTime": "2025-11-25 18:25:30",
    "createTime": "2025-11-25 18:25:30",
    "updateTime": null
  }
}
```

---

### 5.4 下架品种

**接口描述**：将已发布的品种下架。

**接口地址**：`POST /seed/variety/publish/unpublish/{publishId}`

**权限标识**：`seed:variety:publish:unpublish`

**请求示例**：
```
POST /seed/variety/publish/unpublish/VAR_PUB9E2F5A8C4D1B3G7
```

**成功响应**：
```json
{
  "code": 200,
  "msg": "下架成功",
  "data": null
}
```

---

## 六、状态码说明

### 6.1 HTTP 状态码

| 状态码 | 说明 |
|-------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 401 | 未登录或token过期 |
| 403 | 无权限访问 |
| 500 | 服务器内部错误 |

### 6.2 业务状态码

**企业认证状态（certificationStatus）**：
- `0` - 待审核
- `1` - 通过
- `2` - 驳回

**品种备案状态（recordStatus）**：
- `0` - 审核中
- `1` - 待发布
- `2` - 审核未通过
- `3` - 已发布

**审核结果（auditResult）**：
- `1` - 通过
- `2` - 驳回

**公示状态（publishStatus）**：
- `1` - 公示中
- `2` - 已下架

**企业类型（enterpriseType）**：
- `Production-oriented` - 生产型
- `trade-oriented` - 贸易型
- `integrated` - 综合型

**审核阶段（auditStage）**：
- `Initial review` - 初审
- `re-review` - 复审
- `final review` - 终审

---

## 七、公共说明

### 7.1 请求头说明

所有接口都需要在请求头中携带以下信息：

```http
Content-Type: application/json;charset=utf-8
Authorization: Bearer {token}
```

### 7.2 分页参数（预留）

如需分页功能，可在请求参数中添加：

```
pageNum=1&pageSize=10
```

### 7.3 日期格式

- 日期：`yyyy-MM-dd`（如：2025-11-25）
- 日期时间：`yyyy-MM-dd HH:mm:ss`（如：2025-11-25 14:30:25）

### 7.4 数值格式

- 整数：直接传递数字（如：125）
- 小数：保留两位小数（如：95.50）

### 7.5 文件上传

文件上传需要先调用文件上传接口，获取文件路径后再填入相关字段。

文件上传接口（参考）：
```
POST /doc/upload
```

### 7.6 常见错误处理

**错误响应格式**：
```json
{
  "code": 500,
  "msg": "错误提示信息",
  "data": null
}
```

**常见错误**：
1. `统一社会信用代码已存在，请核对`
2. `种子许可证编号已存在，请核对`
3. `企业尚未完成认证备案，无法提交品种登记申请`
4. `品种登记申请状态不是审核中，无法审核`
5. `驳回时必须填写驳回原因`
6. `品种登记申请状态不是待发布，无法发布`

---

## 八、调试建议

### 8.1 完整业务流程测试顺序

1. **企业认证流程**：
   ```
   提交企业认证申请 → 查询待审核列表 → 处理企业审核（通过） → 查询认证详情
   ```

2. **品种管理流程**：
   ```
   提交品种登记申请 → 查询待审核品种列表 → 处理品种审核（通过） →
   查询待发布品种列表 → 发布品种 → 查询已发布品种列表
   ```

### 8.2 Postman 导入

建议将本文档的接口导入 Postman，创建环境变量：

```javascript
{
  "baseUrl": "http://localhost:8080",
  "token": "你的token值"
}
```

### 8.3 前端示例代码（参考）

```javascript
// axios 请求示例
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  }
});

// 请求拦截器 - 添加 token
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// 提交企业认证申请
export const submitEnterpriseCertify = (data) => {
  return api.post('/seed/enterprise/certify/submit', data);
};

// 查询企业认证列表
export const getEnterpriseCertifyList = (params) => {
  return api.get('/seed/enterprise/certify/list', { params });
};

// 提交品种登记申请
export const submitVarietyRegistration = (data) => {
  return api.post('/seed/variety/registration/submit', data);
};
```

---

## 九、联系方式

如有问题，请联系后端开发团队或系统管理员。

**文档版本**：v1.0
**更新日期**：2025-11-25
**维护人员**：系统开发团队
