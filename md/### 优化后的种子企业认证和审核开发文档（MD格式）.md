### 优化后的种子企业认证和审核开发文档（MD格式）

### 文档指令
com.ruoyi
└── seed
    ├── controller       # 控制层
    │   ├── EnterpriseCertifyController.java  # 认证接口
    │   └── EnterpriseAuditController.java    # 审核接口
    ├── service          # 服务层
    │   ├── IEnterpriseCertifyService.java    # 认证服务接口
    │   └── IEnterpriseAuditService.java      # 审核服务接口
    ├── service.impl     # 服务实现层
    │   ├── EnterpriseCertifyServiceImpl.java # 认证服务实现
    │   └── EnterpriseAuditServiceImpl.java   # 审核服务实现
    └── domain           # 实体类
        ├── EnterpriseInfo.java               # 企业认证信息实体
        └── EnterpriseAudit.java              # 企业审核记录实体


    禁止生成前端代码 / 禁止使用 Lombok 以外的工具类 / 禁止自定义非必要 SQL
    基于上述MD文档中的enterprise_info表和提交认证申请接口，用Spring Boot+MyBatis-Plus生成对应的实体类、Mapper、Service、Controller代码。
    若模块复杂，请先生成 “实体类 + Mapper + 空 Service/Controller”，确认结构正确后，再补充业务规则完善逻辑


#### 文档说明
本文档基于RuoYi平台二次开发，适配MySQL数据库，聚焦种子企业认证、审核功能的开发规范，补充接口调用、SQL建表等核心开发细节。

---

# 1. 项目基础信息
## 1.1 开发背景
本项目为奥罗米亚州种子企业监管系统的核心模块，基于RuoYi平台二次开发（MySQL数据库），无前端页面开发内容，核心实现种子企业认证备案、审核审批功能，助力监管部门掌握区域内种子企业情况。

1. 框架版本：基于RuoYi-V4.7.5二次开发（Spring Boot 2.7.10、MyBatis-Plus 3.5.3.1、Spring Security）
2. 数据库：MySQL 8.0，表编码utf8mb4，引擎InnoDB
3. 核心规范：
   - 实体类继承RuoYi的BaseEntity（含createTime/updateTime等通用字段）
   - 接口响应统一用RuoYi的AjaxResult封装（code/msg/data）
   - 权限控制用@PreAuthorize注解（如@PreAuthorize("@ss.hasPermi('seed:enterprise:certify:submit')")）
   - 业务异常抛RuoYi的ServiceException（如throw new ServiceException("提交失败")）
   - 日志用@Log注解（title=“种子企业认证”, businessType=BusinessType.INSERT）

## 1.2 核心使用对象
- 种子企业人员：提交认证备案申请
- 审核人员：审核企业认证申请

---

# 2. 种子企业认证功能
## 2.1 功能描述
种子企业/机构通过该功能完成认证备案，填写企业基础信息并提交材料后，经系统验证、审核人员审批通过，方可使用品种管理、育种管理等核心功能。
### 2.1.1 操作流程
1. 点击「企业认证」按钮，弹出认证申请表单（原型设计见2.4节）；
2. 填写表单并上传证明文件，支持「保存表单」（暂存）、「提交申请」（正式提交）操作；
3. 提交申请后跳转至申请单列表页，列表支持以下查询条件：
   - 文本输入框：支持搜索企业名称、统一社会信用代码、许可证编号；
   - 下拉框1：按企业类型筛选；
   - 下拉框2：按认证状态筛选。

## 2.2 业务逻辑
1. 企业需提交完整备案材料完成认证，方可开展种子相关业务；
2. 企业在线提交材料后，需自行录入/核对信息，审核人员核验材料及信息的完整性、有效性、一致性；
3. 系统自动关联企业基本信息、法人信息、经营范围等基础数据；
4. 表单提交前，系统对必填字段做格式校验（如手机号、日期格式），对重复信息（如统一社会信用代码、许可证编号）给出提示。

## 2.3 输入/输出信息
### 2.3.1 输入信息（用户填写/上传）
| 信息分类       | 具体项                                                                 | 输入要求                  |
|----------------|------------------------------------------------------------------------|---------------------------|
| 企业身份信息   | 企业名称、统一社会信用代码、企业类型、种子许可证编号、许可证有效期起止日 | 必填，格式校验            |
| 位置与运营信息 | 地区、区域、县、乡、完整地址、业务范围、年生产能力（吨/年）            | 必填，年产能为数值型      |
| 企业额外信息   | 成立时间、法人姓名/ID、联系人姓名、联系电话、邮箱                      | 邮箱选填，其余必填        |
| 证明文件       | 营业执照、种子许可证、税务登记证、工厂许可证                          | 必填，上传文件生成存储路径|
| 操作信息       | 操作人、操作机构                                                       | 系统自动填充，不可编辑    |

### 2.3.2 输出信息
| 输出场景       | 输出内容                                                                 |
|----------------|--------------------------------------------------------------------------|
| 表单提交后     | 申请单ID、提交状态、操作提示（如“表单保存成功”“重复信用代码，请核对”）|
| 列表页展示     | 申请单ID、企业名称、认证状态、提交时间、操作人                           |
| 审核后         | 认证结果（通过/驳回）、审核意见、审核人、审核时间                         |

## 2.4 原型设计
### 2.4.1 认证申请页面
- 顶部：企业身份信息区（企业名称、企业注册ID、统一社会信用代码、企业类型、种子许可证编号、许可证有效期起止日）；
- 中部：位置与运营信息区（地区、区域、县、乡、完整地址、业务范围、年生产能力）；
- 中部：企业额外信息区（成立时间、法人姓名/ID、联系人姓名、联系电话、邮箱）；
- 中部：所需文件区（营业执照、种子许可证、税务登记证、工厂许可证上传入口）；
- 底部：操作信息展示区（操作人、操作机构、操作时间，系统自动填充）。

## 2.5 数据库设计
### 2.5.1 表名：enterprise_info（企业认证信息表）
#### 建表SQL语句
```sql
CREATE TABLE `enterprise_info` (
  `enterprise_id` VARCHAR(32) NOT NULL COMMENT '企业唯一标识（主键，系统生成）',
  `enterprise_name` VARCHAR(100) NOT NULL COMMENT '企业名称（唯一索引）',
  `unified_social_credit_code` VARCHAR(20) NOT NULL COMMENT '统一社会信用代码（唯一索引）',
  `enterprise_type` VARCHAR(50) NOT NULL COMMENT '企业类型（枚举：生产型/贸易型/综合型）',
  `seed_license_no` VARCHAR(50) NOT NULL COMMENT '种子经营许可证编号（唯一索引）',
  `license_start_date` DATE NOT NULL COMMENT '许可证有效期起始日',
  `license_end_date` DATE NOT NULL COMMENT '许可证有效期截止日',
  `region` VARCHAR(50) NOT NULL COMMENT '地区',
  `zone` VARCHAR(50) NOT NULL COMMENT '区域',
  `county` VARCHAR(50) NOT NULL COMMENT '县',
  `township` VARCHAR(50) NOT NULL COMMENT '乡',
  `detailed_address` VARCHAR(255) NOT NULL COMMENT '完整地址',
  `business_scope` VARCHAR(255) NOT NULL COMMENT '业务范围',
  `annual_production_capacity` DECIMAL(10,2) NOT NULL COMMENT '年生产能力（吨/年）',
  `establishment_date` DATE NOT NULL COMMENT '企业成立时间',
  `legal_person_name` VARCHAR(50) NOT NULL COMMENT '法人姓名',
  `legal_person_id` VARCHAR(30) NOT NULL COMMENT '法人ID',
  `contact_person` VARCHAR(50) NOT NULL COMMENT '联系人姓名',
  `contact_phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
  `contact_email` VARCHAR(100) NULL COMMENT '邮箱',
  `business_license_url` VARCHAR(255) NOT NULL COMMENT '营业执照存储路径',
  `seed_license_url` VARCHAR(255) NOT NULL COMMENT '种子许可证存储路径',
  `tax_registration_url` VARCHAR(255) NOT NULL COMMENT '税务登记证存储路径',
  `factory_license_url` VARCHAR(255) NOT NULL COMMENT '工厂许可证存储路径',
  `operator` VARCHAR(50) NOT NULL COMMENT '操作人',
  `operation_org` VARCHAR(100) NOT NULL COMMENT '操作机构',
  `operation_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `certification_status` TINYINT(1) NOT NULL COMMENT '认证状态（0-待审核/1-通过/2-驳回）',
  PRIMARY KEY (`enterprise_id`),
  UNIQUE KEY `idx_enterprise_name` (`enterprise_name`),
  UNIQUE KEY `idx_credit_code` (`unified_social_credit_code`),
  UNIQUE KEY `idx_seed_license` (`seed_license_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='种子企业认证信息表';
```

#### 字段说明（修正原文档错误）
| 字段名称                | 数据类型         | 是否为空 | 描述                     | 备注                          |
|-------------------------|------------------|----------|--------------------------|-------------------------------|
| enterprise_id           | VARCHAR(32)      | NOT NULL | 企业唯一标识             | 主键，系统生成                |
| enterprise_name         | VARCHAR(100)     | NOT NULL | 企业名称                 | 唯一索引                      |
| unified_social_credit_code | VARCHAR(20)  | NOT NULL | 统一社会信用代码         | 唯一索引                      |
| enterprise_type         | VARCHAR(50)      | NOT NULL | 企业类型                 | 枚举：Production-oriented/trade-oriented/integrated |
| seed_license_no         | VARCHAR(50)      | NOT NULL | 种子经营许可证编号       | 唯一索引                      |
| license_start_date      | DATE             | NOT NULL | 许可证有效期起始日       | -                             |
| license_end_date        | DATE             | NOT NULL | 许可证有效期截止日       | -                             |
| region                  | VARCHAR(50)      | NOT NULL | 地区                     | -                             |
| zone                    | VARCHAR(50)      | NOT NULL | 区域                     | -                             |
| county                  | VARCHAR(50)      | NOT NULL | 县                       | -                             |
| township                | VARCHAR(50)      | NOT NULL | 乡                       | -                             |
| detailed_address        | VARCHAR(255)     | NOT NULL | 完整地址                 | -                             |
| business_scope          | VARCHAR(255)     | NOT NULL | 业务范围                 | -                             |
| annual_production_capacity | DECIMAL(10,2) | NOT NULL | 年生产能力（吨/年）| 修正原文档“A DECIMAL (1,2)”错误 |
| establishment_date      | DATE             | NOT NULL | 企业成立时间             | -                             |
| legal_person_name       | VARCHAR(50)      | NOT NULL | 法人姓名                 | -                             |
| legal_person_id         | VARCHAR(30)      | NOT NULL | 法人ID                   | -                             |
| contact_person          | VARCHAR(50)      | NOT NULL | 联系人姓名               | -                             |
| contact_phone           | VARCHAR(20)      | NOT NULL | 联系电话                 | -                             |
| contact_email           | VARCHAR(100)     | NULL     | 邮箱                     | -                             |
| business_license_url    | VARCHAR(255)     | NOT NULL | 营业执照存储路径         | -                             |
| seed_license_url        | VARCHAR(255)     | NOT NULL | 种子许可证存储路径       | -                             |
| tax_registration_url    | VARCHAR(255)     | NOT NULL | 税务登记证存储路径       | -                             |
| factory_license_url     | VARCHAR(255)     | NOT NULL | 工厂许可证存储路径       | -                             |
| operator                | VARCHAR(50)      | NOT NULL | 操作人                   | -                             |
| operation_org           | VARCHAR(100)     | NOT NULL | 操作机构                 | -                             |
| operation_time          | DATETIME         | NOT NULL | 操作时间                 | 默认当前时间                  |
| certification_status    | TINYINT(1)       | NOT NULL | 认证状态                 | 0-待审核/1-通过/2-驳回        |

## 2.6 接口调用格式（RESTful）
### 2.6.1 提交认证申请接口
- 请求方式：POST
- 请求路径：/seed/enterprise/certify/submit
- 请求头：Content-Type: application/json;charset=utf-8
- 请求参数（JSON）：
```json
{
  "enterpriseName": "XX种子有限公司",
  "unifiedSocialCreditCode": "91530000MA6K3X8X9L",
  "enterpriseType": "integrated",
  "seedLicenseNo": "YNZZ2025001",
  "licenseStartDate": "2025-01-01",
  "licenseEndDate": "2030-12-31",
  "region": "奥罗米亚州",
  "zone": "XX区",
  "county": "XX县",
  "township": "XX乡",
  "detailedAddress": "XX路XX号",
  "businessScope": "农作物种子生产、销售",
  "annualProductionCapacity": 5000.00,
  "establishmentDate": "2020-05-10",
  "legalPersonName": "张三",
  "legalPersonId": "123456789012345678",
  "contactPerson": "李四",
  "contactPhone": "13800138000",
  "contactEmail": "lisi@example.com",
  "businessLicenseUrl": "/upload/license/2025/biz_91530000MA6K3X8X9L.pdf",
  "seedLicenseUrl": "/upload/license/2025/seed_YNZZ2025001.pdf",
  "taxRegistrationUrl": "/upload/license/2025/tax_91530000MA6K3X8X9L.pdf",
  "factoryLicenseUrl": "/upload/license/2025/factory_2025001.pdf",
  "operator": "李四",
  "operationOrg": "XX种子企业"
}
```
- 响应参数（JSON）：
```json
{
  "code": 200,
  "msg": "提交成功",
  "data": {
    "enterpriseId": "ENT20250520001",
    "certificationStatus": 0,
    "submitTime": "2025-05-20 10:30:00"
  }
}
```

### 2.6.2 查询认证申请列表接口
- 请求方式：GET
- 请求路径：/seed/enterprise/certify/list
- 请求参数（URL拼接）：
  - keyword：企业名称/信用代码/许可证编号（可选）
  - enterpriseType：企业类型（可选）
  - certificationStatus：认证状态（可选）
- 响应参数（JSON）：
```json
{
  "code": 200,
  "msg": "查询成功",
  "data": {
    "total": 50,
    "list": [
      {
        "enterpriseId": "ENT20250520001",
        "enterpriseName": "XX种子有限公司",
        "unifiedSocialCreditCode": "91530000MA6K3X8X9L",
        "seedLicenseNo": "YNZZ2025001",
        "enterpriseType": "integrated",
        "certificationStatus": 0,
        "submitTime": "2025-05-20 10:30:00",
        "operator": "李四"
      }
    ]
  }
}
```

---

# 3. 种子企业审核功能
## 3.1 功能描述
审核人员对企业提交的认证备案申请进行合规性审核，核验信息及材料后，通过「同意/驳回」操作给出审核结果，并记录审核意见，最终更新申请单的认证状态。
### 3.1.1 操作流程
1. 审核人员进入「待办列表」，列表展示所有状态为“待审批”的申请单；
2. 点击申请单进入详情页，页面提供「同意」「驳回」单选框，需填写审核意见后点击「审核」按钮；
3. 审核操作完成后，系统更新申请单的认证状态及审核记录。
### 3.1.2 待办列表查询条件
- 文本输入框：支持搜索企业名称、统一社会信用代码、许可证编号；
- 下拉框：按认证状态筛选。

## 3.2 业务逻辑
1. 企业提交的备案申请自动分配至审核人员待办列表；
2. 审核人员核验内容：企业基础信息完整性、资质材料有效性、填报数据与材料的一致性；
3. 审核结果仅支持“通过”“驳回”，驳回需填写驳回原因；
4. 审核完成后，系统同步更新enterprise_info表的certification_status字段，并生成审核记录。

## 3.3 输入/输出信息
### 3.3.1 输入信息（审核人员操作）
| 输入项       | 输入要求                  |
|--------------|---------------------------|
| 审核结果     | 必填（单选：同意/驳回）|
| 审核意见     | 选填（驳回时建议必填）|
| 驳回原因     | 驳回时必填                |

### 3.3.2 输出信息
| 输出场景       | 输出内容                                                                 |
|----------------|--------------------------------------------------------------------------|
| 审核操作后     | 审核结果提示（如“审核通过”“驳回成功”）、更新后的申请单状态                 |
| 审核记录展示   | 审核ID、企业ID、审核结果、审核意见、审核人、审核时间、审核阶段             |

## 3.4 原型设计
### 3.4.1 审核任务列表页面
- 列表展示：企业名称、统一社会信用代码、申请日期、当前审核阶段、分配审核人；
- 筛选条件：企业名称、ID、许可证号、审核状态。

### 3.4.2 审核任务详情页面
- 顶部：企业基础信息概览（不可编辑）：企业名称、ID、许可证号、企业类型、申请类型；
- 中部：企业额外信息区：成立时间、法人姓名/ID、联系人姓名、联系电话、邮箱；
- 中部：所需文件区：营业执照、种子许可证、税务登记证、工厂许可证（可预览）；
- 底部：审核操作区：审核结果（单选框）、审核意见（文本框）、审核人（自动填充）、审核时间（自动填充）。

## 3.5 数据库设计
### 3.5.1 表名：enterprise_audit（企业审核记录表）
#### 建表SQL语句
```sql
CREATE TABLE `enterprise_audit` (
  `audit_id` VARCHAR(32) NOT NULL COMMENT '审核记录唯一标识（主键，系统生成）',
  `enterprise_id` VARCHAR(32) NOT NULL COMMENT '关联企业唯一标识',
  `audit_result` TINYINT(1) NOT NULL COMMENT '审核结果（1-通过/2-驳回）',
  `audit_opinion` VARCHAR(500) NULL COMMENT '审核意见',
  `auditor` VARCHAR(50) NOT NULL COMMENT '审核人',
  `audit_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '审核时间',
  `audit_stage` VARCHAR(50) NOT NULL COMMENT '当前审核阶段（枚举：Initial review/re-review/final review）',
  `reject_reason` VARCHAR(500) NULL COMMENT '驳回原因（驳回时必填）',
  PRIMARY KEY (`audit_id`),
  KEY `idx_enterprise_id` (`enterprise_id`),
  CONSTRAINT `fk_audit_enterprise` FOREIGN KEY (`enterprise_id`) REFERENCES `enterprise_info` (`enterprise_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='种子企业审核记录表';
```

#### 字段说明（修正原文档重复字段错误）
| 字段名称      | 数据类型         | 是否为空 | 描述                     | 备注                                   |
|---------------|------------------|----------|--------------------------|----------------------------------------|
| audit_id      | VARCHAR(32)      | NOT NULL | 审核记录唯一标识         | 主键，系统生成                        |
| enterprise_id | VARCHAR(32)      | NOT NULL | 关联企业唯一标识         | 外键关联enterprise_info.enterprise_id |
| audit_result  | TINYINT(1)       | NOT NULL | 审核结果                 | 1-通过/2-驳回                          |
| audit_opinion | VARCHAR(500)     | NULL     | 审核意见                 | -                                      |
| auditor       | VARCHAR(50)      | NOT NULL | 审核人                   | -                                      |
| audit_time    | DATETIME         | NOT NULL | 审核时间                 | 默认当前时间                            |
| audit_stage   | VARCHAR(50)      | NOT NULL | 当前审核阶段             | 枚举：Initial review/re-review/final review |
| reject_reason | VARCHAR(500)     | NULL     | 驳回原因                 | 审核结果为驳回时必填                  |

## 3.6 接口调用格式（RESTful）
### 3.6.1 审核申请接口
- 请求方式：POST
- 请求路径：/seed/enterprise/audit/handle
- 请求头：Content-Type: application/json;charset=utf-8
- 请求参数（JSON）：
```json
{
  "auditId": "AUD20250520001",
  "enterpriseId": "ENT20250520001",
  "auditResult": 2,
  "auditOpinion": "材料未加盖公章，信息核验不通过",
  "auditor": "王五",
  "auditStage": "Initial review",
  "rejectReason": "营业执照复印件未加盖企业公章，需补充后重新提交"
}
```
- 响应参数（JSON）：
```json
{
  "code": 200,
  "msg": "审核完成",
  "data": {
    "enterpriseId": "ENT20250520001",
    "certificationStatus": 2,
    "auditTime": "2025-05-20 14:30:00"
  }
}
```

### 3.6.2 查询审核列表接口
- 请求方式：GET
- 请求路径：/seed/enterprise/audit/list
- 请求参数（URL拼接）：
  - enterpriseId：企业ID（可选）
  - auditResult：审核结果（可选）
  - auditStage：审核阶段（可选）
- 响应参数（JSON）：
```json
{
  "code": 200,
  "msg": "查询成功",
  "data": {
    "total": 20,
    "list": [
      {
        "auditId": "AUD20250520001",
        "enterpriseId": "ENT20250520001",
        "enterpriseName": "XX种子有限公司",
        "auditResult": 2,
        "auditOpinion": "材料未加盖公章，信息核验不通过",
        "auditor": "王五",
        "auditTime": "2025-05-20 14:30:00",
        "auditStage": "Initial review",
        "rejectReason": "营业执照复印件未加盖企业公章，需补充后重新提交"
      }
    ]
  }
}
```


