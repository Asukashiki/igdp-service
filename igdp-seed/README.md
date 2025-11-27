# igdp-seed 种子企业综合管理模块

## 模块说明

本模块实现了奥罗米亚州种子企业监管系统的核心功能，包括：
1. **种子企业认证备案和审核审批功能**
2. **种子品种登记、审核和发布功能**
3. **育种计划与过程管理功能**
4. **种子推广与信息公示服务**

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
│   ├── VarietyPublishController.java         # 品种发布接口
│   ├── BreedingPlanController.java           # 育种计划接口
│   ├── BreedingMaterialController.java       # 育种材料接口
│   ├── BreedingTrackingController.java       # 育种跟踪接口
│   ├── SeedPromotionInfoController.java      # 种子推广接口
│   └── SeedVarietyQueryRecordController.java # 种子信息公示接口
├── service          # 服务层
│   ├── IEnterpriseCertifyService.java        # 企业认证服务接口
│   ├── IEnterpriseAuditService.java          # 企业审核服务接口
│   ├── IVarietyRegistrationService.java      # 品种登记服务接口
│   ├── IVarietyAuditService.java             # 品种审核服务接口
│   ├── IVarietyPublishService.java           # 品种发布服务接口
│   ├── IBreedingPlanService.java             # 育种计划服务接口
│   ├── IBreedingMaterialService.java         # 育种材料服务接口
│   ├── IBreedingTrackingService.java         # 育种跟踪服务接口
│   ├── ISeedPromotionInfoService.java        # 种子推广服务接口
│   ├── ISeedVarietyQueryRecordService.java   # 种子查询记录服务接口
│   └── impl                                  # 服务实现层
│       ├── EnterpriseCertifyServiceImpl.java
│       ├── EnterpriseAuditServiceImpl.java
│       ├── VarietyRegistrationServiceImpl.java
│       ├── VarietyAuditServiceImpl.java
│       ├── VarietyPublishServiceImpl.java
│       ├── BreedingPlanServiceImpl.java
│       ├── BreedingMaterialServiceImpl.java
│       ├── BreedingTrackingServiceImpl.java
│       ├── SeedPromotionInfoServiceImpl.java
│       └── SeedVarietyQueryRecordServiceImpl.java
├── mapper           # 数据访问层
│   ├── EnterpriseInfoMapper.java
│   ├── EnterpriseAuditMapper.java
│   ├── VarietyRegistrationMapper.java
│   ├── VarietyAuditMapper.java
│   ├── VarietyPublishMapper.java
│   ├── BreedingPlanMapper.java
│   ├── BreedingMaterialMapper.java
│   ├── BreedingTrackingMapper.java
│   ├── SeedPromotionInfoMapper.java
│   └── SeedVarietyQueryRecordMapper.java
└── domain           # 实体类
    ├── EnterpriseInfo.java               # 企业认证信息实体
    ├── EnterpriseAudit.java              # 企业审核记录实体
    ├── VarietyRegistration.java          # 品种登记信息实体
    ├── VarietyAudit.java                 # 品种审核记录实体
    ├── VarietyPublish.java               # 品种发布记录实体
    ├── BreedingPlan.java                 # 育种计划实体
    ├── BreedingMaterial.java             # 育种材料实体
    ├── BreedingTracking.java             # 育种跟踪实体
    ├── SeedPromotionInfo.java            # 种子推广信息实体
    ├── SeedVarietyQueryRecord.java       # 种子查询记录实体
    └── vo                                # 视图对象
        ├── EnterpriseAuditVO.java        # 企业审核任务VO
        └── VarietyAuditTaskVO.java       # 品种审核任务VO
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

### 3. 育种管理相关表
```bash
igdp-seed/src/main/resources/sql/breeding_management.sql
```

该文件包含三个表：
- `breeding_plan` - 育种计划表
- `breeding_material` - 育种材料登记表
- `breeding_tracking` - 育种跟踪记录表

### 4. 种子信息服务相关表
```bash
igdp-seed/src/main/resources/sql/seed_information_service.sql
```

该文件包含两个表：
- `seed_promotion_info` - 种子推广信息表
- `seed_variety_query_record` - 种子品种查询记录表

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

### 6. 育种计划管理接口

#### 6.1 新增育种计划
- **接口地址**: `POST /seed/breeding/plan/add`
- **权限**: `seed:breeding:plan:add`
- **请求参数**: BreedingPlan 对象（JSON）
- **响应示例**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "planId": "PLAN8F2E459A7D3B48C1"
  }
}
```

#### 6.2 查询育种计划列表
- **接口地址**: `GET /seed/breeding/plan/list`
- **权限**: `seed:breeding:plan:list`
- **请求参数**:
  - enterpriseId: 企业ID（可选）
  - breedingYear: 育种年度（可选）
  - cropType: 作物类型（可选）

#### 6.3 查询育种计划详情
- **接口地址**: `GET /seed/breeding/plan/{planId}`
- **权限**: `seed:breeding:plan:query`

#### 6.4 编辑育种计划
- **接口地址**: `PUT /seed/breeding/plan/edit`
- **权限**: `seed:breeding:plan:edit`

#### 6.5 删除育种计划
- **接口地址**: `DELETE /seed/breeding/plan/{planId}`
- **权限**: `seed:breeding:plan:remove`

### 7. 育种材料登记接口

#### 7.1 新增育种材料登记
- **接口地址**: `POST /seed/breeding/material/add`
- **权限**: `seed:breeding:material:add`
- **响应示例**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "materialId": "MAT1A3B5C7D9E8F7890",
    "registrationCode": "REG20250101ABCDEF"
  }
}
```

#### 7.2 查询育种材料列表
- **接口地址**: `GET /seed/breeding/material/list`
- **权限**: `seed:breeding:material:list`
- **请求参数**:
  - batchId: 育种批次ID（可选）
  - seedType: 种子类别（可选）

#### 7.3 查询育种材料详情
- **接口地址**: `GET /seed/breeding/material/{materialId}`
- **权限**: `seed:breeding:material:query`

#### 7.4 编辑育种材料
- **接口地址**: `PUT /seed/breeding/material/edit`
- **权限**: `seed:breeding:material:edit`

#### 7.5 删除育种材料
- **接口地址**: `DELETE /seed/breeding/material/{materialId}`
- **权限**: `seed:breeding:material:remove`

### 8. 育种跟踪管理接口

#### 8.1 新增育种跟踪记录
- **接口地址**: `POST /seed/breeding/tracking/add`
- **权限**: `seed:breeding:tracking:add`
- **响应示例**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "trackingId": "TRK8F2E459A7D3B48C1"
  }
}
```

#### 8.2 查询育种跟踪列表
- **接口地址**: `GET /seed/breeding/tracking/list`
- **权限**: `seed:breeding:tracking:list`
- **请求参数**:
  - batchId: 育种批次ID（可选）
  - stageName: 阶段名称（可选）

#### 8.3 查询育种跟踪详情
- **接口地址**: `GET /seed/breeding/tracking/{trackingId}`
- **权限**: `seed:breeding:tracking:query`

#### 8.4 编辑育种跟踪记录
- **接口地址**: `PUT /seed/breeding/tracking/edit`
- **权限**: `seed:breeding:tracking:edit`

#### 8.5 删除育种跟踪记录
- **接口地址**: `DELETE /seed/breeding/tracking/{trackingId}`
- **权限**: `seed:breeding:tracking:remove`

### 9. 种子推广信息接口

#### 9.1 查询推广信息列表
- **接口地址**: `GET /seed/promotion/list`
- **权限**: `seed:promotion:list`
- **请求参数**:
  - enterpriseId: 企业ID
  - title: 推广标题（模糊查询，可选）

#### 9.2 上传推广内容
- **接口地址**: `POST /seed/promotion/upload`
- **权限**: `seed:promotion:upload`
- **请求参数**: FormData格式（包含视频文件）
- **响应示例**:
```json
{
  "code": 200,
  "msg": "上传成功",
  "data": {
    "promotionId": "PROMO8F2E459A7D3B48C1",
    "shareLink": "https://domain.com/seed/promotion/PROMO8F2E459A7D3B48C1"
  }
}
```

#### 9.3 更新访问次数
- **接口地址**: `POST /seed/promotion/visit/{promotionId}`
- **说明**: 当用户点击分享链接时触发，无需权限校验

### 10. 种子信息公示接口

#### 10.1 公开查询品种列表
- **接口地址**: `GET /seed/variety/public/list`
- **权限**: 无（公开接口）
- **请求参数**:
  - varietyName: 品种名称（模糊查询，可选）
  - year: 发布年度（可选）
  - cropType: 作物类型（可选）

#### 10.2 公开查询品种详情
- **接口地址**: `GET /seed/variety/public/detail/{publishId}`
- **权限**: 无（公开接口）

#### 10.3 记录查询行为
- **接口地址**: `POST /seed/variety/public/record`
- **权限**: 无（公开接口）
- **说明**: 前端查询或查看详情时自动调用，记录查询行为

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
- 3: 已发布

### 企业类型（enterprise_type）
- Production-oriented: 生产型
- trade-oriented: 贸易型
- integrated: 综合型

### 繁殖级别（propagation_level）
- 育种家种子: Breeder Seed
- 原原种: Pre-basic Seed
- 原种: Basic Seed
- 良种: Certified Seed

### 育种阶段（stage_name）
- 亲本系准备: Parent Line Preparation
- 育种家种子: Breeder Seed Production
- 原原种繁殖: Pre-basic Seed Reproduction
- 原种繁殖: Basic Seed Reproduction

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

### 育种管理权限
```
seed:breeding:plan:add            - 新增育种计划
seed:breeding:plan:list           - 查询育种计划列表
seed:breeding:plan:query          - 查询育种计划详情
seed:breeding:plan:edit           - 编辑育种计划
seed:breeding:plan:remove         - 删除育种计划

seed:breeding:material:add        - 新增育种材料登记
seed:breeding:material:list       - 查询育种材料列表
seed:breeding:material:query      - 查询育种材料详情
seed:breeding:material:edit       - 编辑育种材料
seed:breeding:material:remove     - 删除育种材料

seed:breeding:tracking:add        - 新增育种跟踪记录
seed:breeding:tracking:list       - 查询育种跟踪列表
seed:breeding:tracking:query      - 查询育种跟踪详情
seed:breeding:tracking:edit       - 编辑育种跟踪记录
seed:breeding:tracking:remove     - 删除育种跟踪记录
```

### 种子信息服务权限
```
seed:promotion:list               - 查询推广信息列表
seed:promotion:upload             - 上传推广内容
seed:promotion:edit               - 编辑推广信息
seed:promotion:remove             - 删除推广信息
```

**注意**: 种子信息公示接口无需权限配置，为公开访问接口。

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
   - 如果存在publishId则修改为已发布状态，否则新增发布单

### 育种管理业务规则

1. **育种计划管理**:
   - 批次ID（batch_id）必须唯一
   - 系统自动生成计划ID（格式：PLAN + 16位UUID）
   - 起始时间必须早于或等于结束时间
   - 关联企业必须存在

2. **育种材料登记**:
   - **前置条件**: 必须先创建育种计划
   - 系统自动生成材料ID（格式：MAT + 16位UUID）
   - 系统自动生成登记编码（格式：REG + yyyyMMdd + 6位随机数）
   - 批次ID必须关联到已存在的育种计划
   - 支持上传实验室检测报告（PDF/JPG格式）

3. **育种跟踪管理**:
   - **前置条件**: 必须先创建育种计划
   - 系统自动生成跟踪ID（格式：TRK + 16位UUID）
   - 批次ID必须关联到已存在的育种计划
   - 阶段名称枚举：亲本系准备/育种家种子/原原种繁殖/原种繁殖
   - 田间检查评分范围：0-5分制
   - 支持阶段完成后更新实际产量、评分等信息

### 种子信息服务业务规则

1. **种子推广信息管理**:
   - 仅支持MP4格式视频，单文件大小限制≤100MB
   - 系统自动生成推广ID（格式：PROMO + UUID）
   - 系统自动生成唯一分享链接（格式：域名/seed/promotion/{promotionId}）
   - 访问次数默认为0，每次访问链接自动增加1
   - 有效期以天为单位，最小值为1

2. **种子信息公示**:
   - 公开接口，无需权限校验
   - 仅公示发布状态为"公示中"（1）的品种
   - 系统自动记录所有查询行为（关键词、时间、IP、结果数量）
   - 点击查看详情时记录viewed_publish_id
   - 支持按品种名称、年度、作物类型组合查询

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

   育种管理流程：
   1. 企业创建育种计划 → 2. 登记育种材料（关联批次ID）→
   3. 记录育种跟踪数据（各阶段）→ 4. 阶段完成后更新实际数据

   种子推广流程：
   1. 企业上传推广视频和信息 → 2. 系统生成分享链接 →
   3. 企业分享链接给目标用户 → 4. 用户访问链接，系统统计访问量

   信息公示流程：
   1. 公众访问公示平台 → 2. 输入查询条件搜索品种 →
   3. 查看品种详情 → 4. 系统记录查询行为
   ```

## 注意事项

### 数据库相关
1. 确保数据库已创建以下表：
   - 企业认证：`enterprise_info`、`enterprise_audit`
   - 品种管理：`variety_registration`、`variety_audit`、`variety_publish`
   - 育种管理：`breeding_plan`、`breeding_material`、`breeding_tracking`
   - 种子服务：`seed_promotion_info`、`seed_variety_query_record`
2. 确保配置了正确的数据库连接信息
3. 注意外键约束关系：
   - 品种登记依赖企业认证信息
   - 育种材料和跟踪记录依赖育种计划
   - 推广信息依赖企业信息
   - 查询记录依赖品种发布信息
4. MyBatis-Plus Mapper XML文件位置：`igdp-seed/src/main/resources/mapper/seed/`

### 权限配置
1. 确保用户已登录并拥有相应的操作权限
2. 各模块权限需分别配置：
   - 企业认证权限
   - 品种管理权限
   - 育种管理权限
   - 种子推广权限
3. 建议为不同角色分配不同权限：
   - **企业人员**: 认证申请、品种登记、育种管理、推广信息管理
   - **审核人员**: 企业审核、品种审核
   - **管理人员**: 品种发布、下架
   - **公众用户**: 种子信息公示（无需权限）

### 业务逻辑
1. **重要**: 企业必须先完成认证备案，才能提交品种登记申请和育种计划
2. 品种审核通过后才能发布
3. 育种材料和育种跟踪必须关联到已存在的育种计划
4. 文件上传路径需要根据实际情况配置
5. 所有ID由系统自动生成：
   - 企业ID: ENT + 16位UUID
   - 企业审核ID: AUD + 16位UUID
   - 品种登记ID: VAR_REG + 16位UUID
   - 品种审核ID: VAR_AUD + 16位UUID
   - 品种发布ID: VAR_PUB + 16位UUID
   - 育种计划ID: PLAN + 16位UUID
   - 育种材料ID: MAT + 16位UUID
   - 育种跟踪ID: TRK + 16位UUID
   - 推广信息ID: PROMO + UUID
   - 查询记录ID: QUERY + UUID
6. 登记申请号和发布编号格式：
   - 登记申请号: VAR + 年月日 + 6位随机数
   - 发布编号: PUB + 年月日 + 6位序号
   - 材料登记编码: REG + 年月日 + 6位随机数

### 数据表关系
1. **外键约束**:
   - `breeding_material.batch_id` → `breeding_plan.batch_id`
   - `breeding_tracking.batch_id` → `breeding_plan.batch_id`
   - `seed_promotion_info.enterprise_id` → `enterprise_info.enterprise_id`
   - `seed_variety_query_record.viewed_publish_id` → `variety_publish.publish_id`

2. **级联操作**:
   - 删除育种计划时需注意关联的材料和跟踪记录
   - 删除企业时需注意关联的推广信息
   - 删除发布记录时需注意关联的查询记录

### 特殊功能
1. **品种登记提交逻辑**:
   - 如果没有registrationId，则新增登记记录
   - 如果有registrationId，则更新登记记录
   - 无论新增还是更新，都生成待审核的审核记录

2. **品种审核处理逻辑**:
   - 审核通过后自动生成待发布单，填充所有必要字段
   - 待发布单包含品种详情、审核意见、推荐地区、播种指南等

3. **品种发布处理逻辑**:
   - 如果存在publishId，则修改为已发布状态（3）
   - 如果不存在publishId，则新增发布单（公示中状态1）
   - 同步更新品种登记状态为已发布（3）

4. **审核任务查询优化**:
   - 使用子查询关联最新审核记录，避免一对多重复数据
   - VarietyAuditMapper.xml 使用优化的LEFT JOIN查询

## 功能模块总览

### 模块一：企业认证与审核
- **核心功能**: 种子企业认证备案、审核审批
- **涉及表**: enterprise_info、enterprise_audit
- **控制器**: EnterpriseCertifyController、EnterpriseAuditController
- **业务流程**: 提交认证 → 审核 → 通过/驳回

### 模块二：品种管理
- **核心功能**: 品种登记、审核、发布
- **涉及表**: variety_registration、variety_audit、variety_publish
- **控制器**: VarietyRegistrationController、VarietyAuditController、VarietyPublishController
- **业务流程**: 登记申请 → 审核 → 待发布 → 发布 → 公示

### 模块三：育种管理
- **核心功能**: 育种计划制定、材料登记、过程跟踪
- **涉及表**: breeding_plan、breeding_material、breeding_tracking
- **控制器**: BreedingPlanController、BreedingMaterialController、BreedingTrackingController
- **业务流程**: 制定计划 → 登记材料 → 跟踪记录 → 更新数据

### 模块四：种子信息服务
- **核心功能**: 推广信息管理、公开信息公示
- **涉及表**: seed_promotion_info、seed_variety_query_record
- **控制器**: SeedPromotionInfoController、SeedVarietyQueryRecordController
- **业务流程**: 上传推广 → 生成链接 → 访问统计 / 公众查询 → 记录行为

## 技术亮点

1. **统一的响应格式**: 使用 AjaxResult 和 TableDataInfo 封装返回数据
2. **分页支持**: 所有列表查询接口支持分页
3. **事务管理**: 关键业务操作使用 @Transactional 确保数据一致性
4. **ID自动生成**: 所有主键ID由系统自动生成，确保唯一性
5. **审核流程**: 完整的审核状态流转和记录
6. **关联查询优化**: 使用子查询避免一对多关联产生重复数据
7. **文件上传**: 支持视频、PDF、图片等多种格式文件上传
8. **访问统计**: 自动记录推广链接访问次数和查询行为
9. **公开接口**: 种子信息公示接口无需权限，面向公众开放

## API文档参考

详细的API接口文档请参考：
- [育种管理API接口文档](src/main/resources/md/育种管理API接口文档.md)
- [种子信息服务API接口文档](src/main/resources/md/种子信息服务模块API接口文档.md)
- [品种管理API接口文档](src/main/resources/md/品种管理模块API接口文档.md)

## 联系方式

如有问题，请联系系统管理员。
