# 种子企业认证审核与品种管理模块

## 模块说明

本模块实现了奥罗米亚州种子企业监管系统的核心功能，包括：
1. **种子企业认证备案和审核审批功能**
2. **种子品种登记、审核和发布功能**

## 技术栈

- Spring Boot 2.7.10
- MyBatis-Plus 3.5.3.1
- MySQL 8.0
- Sa-Token（权限控制）

## 模块结构

```
com.inspur.seed
├── controller       # 控制层
│   ├── EnterpriseCertifyController.java      # 企业认证接口
│   ├── EnterpriseAuditController.java        # 企业审核接口
│   ├── VarietyRegistrationController.java    # 品种登记接口
│   ├── VarietyAuditController.java           # 品种审核接口
│   └── VarietyPublishController.java         # 品种发布接口
├── service          # 服务层
│   ├── IEnterpriseCertifyService.java        # 企业认证服务接口
│   ├── IEnterpriseAuditService.java          # 企业审核服务接口
│   ├── IVarietyRegistrationService.java      # 品种登记服务接口
│   ├── IVarietyAuditService.java             # 品种审核服务接口
│   ├── IVarietyPublishService.java           # 品种发布服务接口
│   └── impl                                  # 服务实现层
│       ├── EnterpriseCertifyServiceImpl.java
│       ├── EnterpriseAuditServiceImpl.java
│       ├── VarietyRegistrationServiceImpl.java
│       ├── VarietyAuditServiceImpl.java
│       └── VarietyPublishServiceImpl.java
├── mapper           # 数据访问层
│   ├── EnterpriseInfoMapper.java
│   ├── EnterpriseAuditMapper.java
│   ├── VarietyRegistrationMapper.java
│   ├── VarietyAuditMapper.java
│   └── VarietyPublishMapper.java
└── domain           # 实体类
    ├── EnterpriseInfo.java               # 企业认证信息实体
    ├── EnterpriseAudit.java              # 企业审核记录实体
    ├── VarietyRegistration.java          # 品种登记信息实体
    ├── VarietyAudit.java                 # 品种审核记录实体
    └── VarietyPublish.java               # 品种发布记录实体
```

## 数据库初始化

执行以下 SQL 文件创建数据库表：

### 1. 企业认证相关表
```bash
igdp-seed/src/main/resources/sql/seed_enterprise.sql
```

该文件包含两个表：
- `enterprise_info` - 企业认证信息表
- `enterprise_audit` - 企业审核记录表

### 2. 品种管理相关表
```bash
igdp-seed/src/main/resources/sql/seed_variety.sql
```

该文件包含三个表：
- `variety_registration` - 品种登记信息表
- `variety_audit` - 品种审核记录表
- `variety_publish` - 品种发布记录表

## API 接口说明

### 1. 企业认证接口

#### 1.1 提交认证申请
- **接口地址**: `POST /seed/enterprise/certify/submit`
- **权限**: `seed:enterprise:certify:submit`
- **请求参数**: EnterpriseInfo 对象（JSON）
- **响应示例**:
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

#### 1.2 查询认证申请列表
- **接口地址**: `GET /seed/enterprise/certify/list`
- **权限**: `seed:enterprise:certify:list`
- **请求参数**:
  - keyword: 关键词（企业名称/信用代码/许可证编号）
  - enterpriseType: 企业类型
  - certificationStatus: 认证状态
- **响应示例**:
```json
{
  "code": 200,
  "msg": "查询成功",
  "data": {
    "total": 50,
    "list": [...]
  }
}
```

#### 1.3 查询认证详情
- **接口地址**: `GET /seed/enterprise/certify/{enterpriseId}`
- **权限**: `seed:enterprise:certify:query`

#### 1.4 保存表单（暂存）
- **接口地址**: `POST /seed/enterprise/certify/save`
- **权限**: `seed:enterprise:certify:save`

### 2. 企业审核接口

#### 2.1 处理审核
- **接口地址**: `POST /seed/enterprise/audit/handle`
- **权限**: `seed:enterprise:audit:handle`
- **请求参数**: EnterpriseAudit 对象（JSON）
- **响应示例**:
```json
{
  "code": 200,
  "msg": "审核完成",
  "data": {
    "enterpriseId": "ENT20250520001",
    "certificationStatus": 1,
    "auditTime": "2025-05-20 14:30:00"
  }
}
```

#### 2.2 查询审核记录列表
- **接口地址**: `GET /seed/enterprise/audit/list`
- **权限**: `seed:enterprise:audit:list`
- **请求参数**:
  - enterpriseId: 企业ID
  - auditResult: 审核结果
  - auditStage: 审核阶段

#### 2.3 查询待审核列表
- **接口地址**: `GET /seed/enterprise/audit/pending`
- **权限**: `seed:enterprise:audit:pending`
- **请求参数**:
  - keyword: 关键词

#### 2.4 查询审核详情
- **接口地址**: `GET /seed/enterprise/audit/{auditId}`
- **权限**: `seed:enterprise:audit:query`

#### 2.5 查询最新审核记录
- **接口地址**: `GET /seed/enterprise/audit/latest/{enterpriseId}`
- **权限**: `seed:enterprise:audit:query`

### 3. 品种登记接口

#### 3.1 提交品种登记申请
- **接口地址**: `POST /seed/variety/registration/submit`
- **权限**: `seed:variety:registration:submit`
- **前置条件**: 企业必须已完成认证备案（certification_status = 1）
- **请求参数**: VarietyRegistration 对象（JSON）
- **响应示例**:
```json
{
  "code": 200,
  "msg": "提交成功",
  "data": {
    "registrationId": "VAR_REG20250520001",
    "registrationNo": "VAR20250520000001",
    "recordStatus": 0,
    "submitTime": "2025-05-20 10:30:00"
  }
}
```

#### 3.2 查询品种登记列表
- **接口地址**: `GET /seed/variety/registration/list`
- **权限**: `seed:variety:registration:list`
- **请求参数**:
  - varietyName: 品种名称（模糊查询）
  - enterpriseName: 企业名称（模糊查询）
  - enterpriseType: 企业类型
  - recordType: 备案类型

#### 3.3 查询登记详情
- **接口地址**: `GET /seed/variety/registration/{registrationId}`
- **权限**: `seed:variety:registration:query`

#### 3.4 保存表单（暂存）
- **接口地址**: `POST /seed/variety/registration/save`
- **权限**: `seed:variety:registration:save`

#### 3.5 查询待发布品种列表
- **接口地址**: `GET /seed/variety/registration/pending`
- **权限**: `seed:variety:registration:pending`
- **说明**: 查询备案状态为"待发布"的品种

### 4. 品种审核接口

#### 4.1 处理品种审核
- **接口地址**: `POST /seed/variety/audit/handle`
- **权限**: `seed:variety:audit:handle`
- **请求参数**: VarietyAudit 对象（JSON）
- **响应示例**:
```json
{
  "code": 200,
  "msg": "审核完成",
  "data": {
    "registrationId": "VAR_REG20250520001",
    "recordStatus": 1,
    "auditTime": "2025-05-20 14:30:00"
  }
}
```

#### 4.2 查询审核记录列表
- **接口地址**: `GET /seed/variety/audit/list`
- **权限**: `seed:variety:audit:list`
- **请求参数**:
  - varietyName: 品种名称
  - enterpriseName: 提交单位
  - auditResult: 审核结果

#### 4.3 查询待审核列表
- **接口地址**: `GET /seed/variety/audit/pending`
- **权限**: `seed:variety:audit:pending`
- **说明**: 查询备案状态为"审核中"的品种

#### 4.4 查询审核详情
- **接口地址**: `GET /seed/variety/audit/{auditId}`
- **权限**: `seed:variety:audit:query`

#### 4.5 查询最新审核记录
- **接口地址**: `GET /seed/variety/audit/latest/{registrationId}`
- **权限**: `seed:variety:audit:query`

### 5. 品种发布接口

#### 5.1 发布品种
- **接口地址**: `POST /seed/variety/publish/handle`
- **权限**: `seed:variety:publish:handle`
- **前置条件**: 品种备案状态必须为"待发布"（record_status = 1）
- **请求参数**: VarietyPublish 对象（JSON）
- **响应示例**:
```json
{
  "code": 200,
  "msg": "发布成功",
  "data": {
    "publishId": "VAR_PUB20250520001",
    "publishNo": "PUB20250520000001",
    "publishStatus": 1,
    "publishTime": "2025-05-20 16:30:00"
  }
}
```

#### 5.2 查询已发布品种列表
- **接口地址**: `GET /seed/variety/publish/list`
- **权限**: `seed:variety:publish:list`
- **请求参数**:
  - varietyName: 品种名称
  - cropType: 作物类型
  - publishStatus: 公示状态

#### 5.3 查询发布详情
- **接口地址**: `GET /seed/variety/publish/{publishId}`
- **权限**: `seed:variety:publish:query`

#### 5.4 下架品种
- **接口地址**: `POST /seed/variety/publish/unpublish/{publishId}`
- **权限**: `seed:variety:publish:unpublish`
- **说明**: 将发布状态更新为"已下架"

#### 5.5 根据登记ID查询发布记录
- **接口地址**: `GET /seed/variety/publish/registration/{registrationId}`
- **权限**: `seed:variety:publish:query`

## 状态说明

### 企业认证状态（certification_status）
- 0: 待审核
- 1: 通过
- 2: 驳回

### 品种备案状态（record_status）
- 0: 审核中
- 1: 待发布
- 2: 审核未通过
- 3: 已发布

### 审核结果（audit_result）
- 1: 通过
- 2: 驳回

### 审核阶段（audit_stage）
- 初审: Initial review
- 复审: re-review
- 终审: final review

### 公示状态（publish_status）
- 1: 公示中
- 2: 已下架

### 企业类型（enterprise_type）
- Production-oriented: 生产型
- trade-oriented: 贸易型
- integrated: 综合型

## 权限配置

需要在系统中配置以下权限：

### 企业认证权限
```
seed:enterprise:certify:submit   - 提交认证申请
seed:enterprise:certify:list     - 查询认证列表
seed:enterprise:certify:query    - 查询认证详情
seed:enterprise:certify:save     - 保存表单

seed:enterprise:audit:handle     - 处理审核
seed:enterprise:audit:list       - 查询审核列表
seed:enterprise:audit:query      - 查询审核详情
seed:enterprise:audit:pending    - 查询待审核列表
```

### 品种管理权限
```
seed:variety:registration:submit  - 提交品种登记申请
seed:variety:registration:list    - 查询品种登记列表
seed:variety:registration:query   - 查询品种登记详情
seed:variety:registration:save    - 保存品种登记表单
seed:variety:registration:pending - 查询待发布品种列表

seed:variety:audit:handle         - 处理品种审核
seed:variety:audit:list           - 查询品种审核列表
seed:variety:audit:query          - 查询品种审核详情
seed:variety:audit:pending        - 查询待审核品种列表

seed:variety:publish:handle       - 发布品种
seed:variety:publish:list         - 查询已发布品种列表
seed:variety:publish:query        - 查询品种发布详情
seed:variety:publish:unpublish    - 下架品种
```

## 业务规则

### 企业认证业务规则

1. **认证申请提交**:
   - 统一社会信用代码唯一性校验
   - 种子许可证编号唯一性校验
   - 提交后认证状态默认为"待审核"

2. **企业审核处理**:
   - 审核驳回时必须填写驳回原因
   - 审核完成后自动更新企业认证状态
   - 记录审核意见和审核人信息

### 品种管理业务规则

1. **品种登记提交**:
   - **前置条件**: 企业必须已完成认证备案（certification_status = 1）
   - 系统自动生成登记申请号（格式：VAR+年月日+6位随机数）
   - 提交后备案状态默认为"审核中"（0）
   - 支持表单暂存功能

2. **品种审核处理**:
   - 仅能审核状态为"审核中"（0）的品种
   - 审核驳回时必须填写驳回原因
   - 审核通过：更新状态为"待发布"（1）
   - 审核驳回：更新状态为"审核未通过"（2）

3. **品种发布管理**:
   - **前置条件**: 品种备案状态必须为"待发布"（1）
   - 系统自动生成发布编号（格式：PUB+年月日+6位序号）
   - 发布后更新品种状态为"已发布"（3）
   - 支持品种下架功能（更新为"已下架"状态）

### 数据安全

- 使用 LoginHelper 自动获取当前用户信息
- 操作时间自动记录
- 支持事务回滚
- 所有ID由系统自动生成，确保唯一性

## 使用步骤

1. **执行数据库脚本**:
   ```sql
   -- 1. 执行企业认证相关表
   -- igdp-seed/src/main/resources/sql/seed_enterprise.sql

   -- 2. 执行品种管理相关表
   -- igdp-seed/src/main/resources/sql/seed_variety.sql
   ```

2. **重新加载 Maven 项目**:
   - 主项目 pom.xml 已添加 igdp-seed 模块
   - igdp-admin 已添加 igdp-seed 依赖

3. **配置权限**:
   - 在系统权限管理中添加上述企业认证权限和品种管理权限标识

4. **启动项目**:
   ```bash
   mvn clean install
   cd igdp-admin
   mvn spring-boot:run
   ```

5. **测试接口**:
   - 使用 Swagger UI 访问: http://localhost:端口/swagger-ui/index.html
   - 或使用 Postman 等工具测试接口

6. **业务流程**:
   ```
   企业认证流程：
   1. 企业提交认证申请 → 2. 审核人员审核 → 3. 认证通过

   品种管理流程：
   1. 企业提交品种登记（需先完成认证）→ 2. 审核人员审核 →
   3. 审核通过（状态变为待发布）→ 4. 管理人员发布 → 5. 公开展示
   ```

## 注意事项

### 数据库相关
1. 确保数据库已创建以下表：
   - 企业认证：`enterprise_info`、`enterprise_audit`
   - 品种管理：`variety_registration`、`variety_audit`、`variety_publish`
2. 确保配置了正确的数据库连接信息
3. 注意外键约束关系：品种登记依赖企业认证信息

### 权限配置
1. 确保用户已登录并拥有相应的操作权限
2. 企业认证权限和品种管理权限需分别配置
3. 建议为不同角色分配不同权限（企业人员、审核人员、管理人员）

### 业务逻辑
1. **重要**: 企业必须先完成认证备案，才能提交品种登记申请
2. 品种审核通过后才能发布
3. 文件上传路径需要根据实际情况配置
4. 所有ID由系统自动生成：
   - 企业ID: ENT + 16位UUID
   - 企业审核ID: AUD + 16位UUID
   - 品种登记ID: VAR_REG + 16位UUID
   - 品种审核ID: VAR_AUD + 16位UUID
   - 品种发布ID: VAR_PUB + 16位UUID
5. 登记申请号和发布编号格式：
   - 登记申请号: VAR + 年月日 + 6位随机数
   - 发布编号: PUB + 年月日 + 6位序号

## 联系方式

如有问题，请联系系统管理员。
