# 投入品供应管理系统 - 模块梳理文档

## 一、注册管理模块

### 1.1 模块概述

注册管理模块用于管理Union（联合社）和Cooperative（合作社）的注册申请与审核流程。通过该模块实现投入品发放机构资质的合规校验与信息标准化存储，为后续投入品管理业务提供资质支撑。

### 1.2 业务流程图

```
┌─────────────────────────────────────────────────────────────────┐
│                      注册管理业务流程                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐      │
│  │   Zone录入    │    │   Zone审核    │    │   注册完成    │      │
│  │  Union信息    │───>│  Union申请    │───>│  开通权限    │      │
│  └──────────────┘    └──────────────┘    └──────────────┘      │
│         │                   │                                   │
│         │              驳回 │                                   │
│         │<──────────────────┘                                   │
│                                                                 │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐      │
│  │  Woreda录入   │    │  Woreda审核   │    │   注册完成    │      │
│  │Cooperative信息│───>│Cooperative申请│───>│  开通权限    │      │
│  └──────────────┘    └──────────────┘    └──────────────┘      │
│         │                   │                                   │
│         │              驳回 │                                   │
│         │<──────────────────┘                                   │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 1.3 功能说明

#### 1.3.1 Union/Cooperative注册申请

**功能描述：**
- Zone负责录入Union注册信息
- Woreda负责录入Cooperative注册信息
- 录入完成后等待审核通过完成注册

**使用对象：**
- Zone级行政单位（录入Union）
- Woreda级行政单位（录入Cooperative）

**业务规则：**
1. 录入信息包括：基础信息、行政区划信息、地址信息、许可信息、账号信息
2. 提交后可查询认证申请状态
3. 机构类型：Union或Cooperative
4. 投入品类型支持多选：种子、化肥、农药
5. 销售区域关联组织机构代码，支持多选

#### 1.3.2 Union/Cooperative注册审核

**功能描述：**
- Zone负责审核Union注册信息
- Woreda负责审核Cooperative注册信息

**使用对象：**
- Zone级行政单位（审核Union）
- Woreda级行政单位（审核Cooperative）

**业务规则：**
1. 系统按照行政区划将申请信息推送给对应行政单位
2. 审核通过：为机构开通系统权限
3. 审核不通过：必须填写审核意见，申请方可重新修改后提交

### 1.4 数据库设计

#### 1.4.1 机构基础信息表 (org_enterprise_info)

| 字段名                            | 类型           | 必填   | 说明                                    |
| ------------------------------ | ------------ | ---- | ------------------------------------- |
| id                             | varchar(36)  | 是    | 主键UUID                                |
| enterprise_name                | varchar(200) | 是    | 机构名称                                  |
| enterprise_registration_id     | varchar(100) | 否    | 企业注册号                                 |
| unified_social_credit_code     | varchar(50)  | 否    | 统一社会信用代码                              |
| seed_enterprise_license_number | varchar(100) | 是    | 种子企业许可证号                              |
| license_validity_start         | date         | 是    | 许可证有效期开始                              |
| license_validity_end           | date         | 是    | 许可证有效期结束                              |
| enterprise_type                | varchar(50)  | 是    | 企业类型                                  |
| org_type                       | varchar(20)  | 是    | 机构类型(union/cooperative)               |
| input_types                    | varchar(100) | 是    | 投入品类型(多选,逗号分隔)                        |
| sales_regions                  | text         | 是    | 销售区域(组织机构代码,多选)                       |
| application_status             | varchar(20)  | 是    | 申请状态(draft/pending/approved/rejected) |
| created_by                     | varchar(36)  | 是    | 创建人ID                                 |
| created_time                   | datetime     | 是    | 创建时间                                  |
| updated_by                     | varchar(36)  | 否    | 更新人ID                                 |
| updated_time                   | datetime     | 否    | 更新时间                                  |
| is_deleted                     | tinyint(1)   | 是    | 是否删除(0:否 1:是)                         |
| version                        | int          | 是    | 版本号(乐观锁)                              |
| remark                         | varchar(500) | 否    | 备注                                    |

#### 1.4.2 机构位置运营信息表 (org_enterprise_location)

| 字段名                        | 类型            | 必填   | 说明                          |
| -------------------------- | ------------- | ---- | --------------------------- |
| id                         | varchar(36)   | 是    | 主键UUID                      |
| enterprise_id              | varchar(36)   | 是    | 机构ID(关联org_enterprise_info) |
| region                     | varchar(100)  | 否    | 大区                          |
| zone                       | varchar(100)  | 否    | Zone                        |
| woreda                     | varchar(100)  | 是    | Woreda                      |
| kebele                     | varchar(100)  | 是    | Kebele                      |
| full_address               | varchar(500)  | 是    | 详细地址                        |
| gps_latitude               | decimal(10,7) | 否    | GPS纬度                       |
| gps_longitude              | decimal(10,7) | 否    | GPS经度                       |
| business_scope             | varchar(500)  | 否    | 经营范围                        |
| annual_production_capacity | decimal(15,2) | 否    | 年生产能力                       |
| created_by                 | varchar(36)   | 是    | 创建人ID                       |
| created_time               | datetime      | 是    | 创建时间                        |
| updated_by                 | varchar(36)   | 否    | 更新人ID                       |
| updated_time               | datetime      | 否    | 更新时间                        |
| is_deleted                 | tinyint(1)    | 是    | 是否删除(0:否 1:是)               |

#### 1.4.3 机构许可证件表 (org_enterprise_license)

| 字段名              | 类型           | 必填   | 说明                                       |
| ---------------- | ------------ | ---- | ---------------------------------------- |
| id               | varchar(36)  | 是    | 主键UUID                                   |
| enterprise_id    | varchar(36)  | 是    | 机构ID(关联org_enterprise_info)              |
| license_type     | varchar(50)  | 是    | 证件类型(business_license/seed_license/tax_certificate/factory_permit) |
| license_number   | varchar(100) | 否    | 证件号码                                     |
| license_file_url | varchar(500) | 是    | 证件文件URL                                  |
| issue_date       | date         | 否    | 发证日期                                     |
| expiry_date      | date         | 否    | 到期日期                                     |
| created_by       | varchar(36)  | 是    | 创建人ID                                    |
| created_time     | datetime     | 是    | 创建时间                                     |
| updated_by       | varchar(36)  | 否    | 更新人ID                                    |
| updated_time     | datetime     | 否    | 更新时间                                     |
| is_deleted       | tinyint(1)   | 是    | 是否删除(0:否 1:是)                            |

#### 1.4.4 机构注册审核记录表 (org_registration_audit)

| 字段名             | 类型            | 必填   | 说明                          |
| --------------- | ------------- | ---- | --------------------------- |
| id              | varchar(36)   | 是    | 主键UUID                      |
| enterprise_id   | varchar(36)   | 是    | 机构ID(关联org_enterprise_info) |
| audit_user_id   | varchar(36)   | 是    | 审核人ID                       |
| audit_user_name | varchar(100)  | 是    | 审核人姓名                       |
| audit_time      | datetime      | 是    | 审核时间                        |
| audit_result    | varchar(20)   | 是    | 审核结果(approved/rejected)     |
| audit_opinion   | varchar(1000) | 否    | 审核意见(驳回时必填)                 |
| remark          | varchar(500)  | 否    | 备注                          |
| created_by      | varchar(36)   | 是    | 创建人ID                       |
| created_time    | datetime      | 是    | 创建时间                        |
| is_deleted      | tinyint(1)    | 是    | 是否删除(0:否 1:是)               |

---

## 二、投入品需求采集管理模块

### 2.1 模块概述

投入品需求采集模块实现投入品需求从农民填报、村级汇总到州级终审的全流程数据管理，确保需求数据真实、规范、可追溯，为后续配额分配提供精准数据支撑。

### 2.2 业务流程图

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        投入品需求采集审核流程                                  │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌─────────┐   ┌─────────┐   ┌─────────┐   ┌─────────┐   ┌─────────────┐   │
│  │ DA录入   │   │ 村级审核 │   │ 镇级审核 │   │ 区级审核 │   │ 奥罗米亚州审核│   │
│  │ 农民需求 │──>│  提交   │──>│  提交   │──>│  提交   │──>│    审核     │   │
│  └─────────┘   └─────────┘   └─────────┘   └─────────┘   └─────────────┘   │
│       ^             │             │             │               │          │
│       │        驳回 │        驳回 │        驳回 │               │          │
│       └─────────────┘             │             │               ▼          │
│       ^                           │             │        ┌─────────────┐   │
│       └───────────────────────────┘             │        │ 州农业部审核 │   │
│       ^                                         │        │  最终审批   │   │
│       └─────────────────────────────────────────┘        └─────────────┘   │
│                                                                 │          │
│                                                            需求锁定        │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 2.3 功能说明

#### 2.3.1 DA录入农民需求

**功能描述：**
DA（发展代理）负责录入全村农民的投入品需求数据，支持按分类录入（种子、化肥、农药等）

**使用对象：**
- 村级操作员（DA）

**业务规则：**
1. 支持单条录入和批量导入
2. 系统校验必填字段完整性与数据合理性
3. 系统自动关联地块信息，计算地块总面积
4. 估算最大种子量、最大肥料量供DA参考
5. 需求类型包括：种子、化肥、农药

**输入信息：**
- 农民信息：农民姓名、ID、所属行政区划、村庄
- 需求信息：需求类型、农资类型、品种、数量

#### 2.3.2 投入品需求审核

**功能描述：**
村级需求填报提交后，按照"村→镇→区→奥罗米亚州→州农业部"逐级审核

**使用对象：**
- 村级操作员
- 镇级操作员
- 区级操作员
- 州级操作员
- 州农业部操作员

**各级审核职责：**

| 层级   | 查看范围        | 审核对象   | 操作权限     |
| ---- | ----------- | ------ | -------- |
| 村级   | DA录入的农民需求数据 | 农民需求   | 退回/提交镇级  |
| 镇级   | 各村需求汇总+村民明细 | 村级提交数据 | 退回/提交区级  |
| 区级   | 各镇需求汇总+全部汇总 | 镇级提交数据 | 退回/提交州级  |
| 州级   | 各县需求汇总+全部汇总 | 区级提交数据 | 退回/转发农业部 |
| 农业部  | 各市需求汇总+全部汇总 | 州级提交数据 | 最终审批/锁定  |

**业务规则：**
1. 驳回时必须填写审核意见
2. 被驳回的数据需重新修改后再次提交
3. 各级操作员应确保下属单位记录完善后再提交
4. 农业部审批完成后，需求数据锁定，不可修改

### 2.4 数据库设计

#### 2.4.1 需求采集批次表 (demand_collection_batch)

| 字段名          | 类型           | 必填   | 说明                                       |
| ------------ | ------------ | ---- | ---------------------------------------- |
| id           | varchar(36)  | 是    | 主键UUID                                   |
| batch_no     | varchar(50)  | 是    | 批次编号                                     |
| batch_name   | varchar(200) | 是    | 批次名称                                     |
| year         | int          | 是    | 年度                                       |
| start_date   | date         | 是    | 采集开始日期                                   |
| end_date     | date         | 是    | 采集结束日期                                   |
| status       | varchar(20)  | 是    | 状态(collecting/reviewing/completed/locked) |
| created_by   | varchar(36)  | 是    | 创建人ID                                    |
| created_time | datetime     | 是    | 创建时间                                     |
| updated_by   | varchar(36)  | 否    | 更新人ID                                    |
| updated_time | datetime     | 否    | 更新时间                                     |
| is_deleted   | tinyint(1)   | 是    | 是否删除(0:否 1:是)                            |
| version      | int          | 是    | 版本号(乐观锁)                                 |
| remark       | varchar(500) | 否    | 备注                                       |

#### 2.4.2 农民需求明细表 (demand_farmer_detail)

| 字段名                     | 类型            | 必填   | 说明                                       |
| ----------------------- | ------------- | ---- | ---------------------------------------- |
| id                      | varchar(36)   | 是    | 主键UUID                                   |
| batch_id                | varchar(36)   | 是    | 批次ID(关联demand_collection_batch)          |
| farmer_id               | varchar(36)   | 是    | 农民ID                                     |
| farmer_name             | varchar(100)  | 是    | 农民姓名                                     |
| farmer_id_number        | varchar(50)   | 是    | 农民身份证号                                   |
| region                  | varchar(100)  | 否    | 大区                                       |
| zone                    | varchar(100)  | 否    | Zone                                     |
| woreda                  | varchar(100)  | 是    | Woreda                                   |
| kebele                  | varchar(100)  | 是    | Kebele                                   |
| village                 | varchar(100)  | 是    | 村庄                                       |
| land_area               | decimal(10,2) | 否    | 地块总面积(公顷)                                |
| max_seed_quantity       | decimal(15,2) | 否    | 估算最大种子量(kg)                              |
| max_fertilizer_quantity | decimal(15,2) | 否    | 估算最大肥料量(kg)                              |
| status                  | varchar(20)   | 是    | 状态(draft/submitted/approved/rejected)    |
| current_audit_level     | varchar(20)   | 否    | 当前审核层级(village/town/district/state/ministry) |
| da_user_id              | varchar(36)   | 是    | 录入DA用户ID                                 |
| da_user_name            | varchar(100)  | 是    | 录入DA用户姓名                                 |
| submit_time             | datetime      | 否    | 提交时间                                     |
| created_by              | varchar(36)   | 是    | 创建人ID                                    |
| created_time            | datetime      | 是    | 创建时间                                     |
| updated_by              | varchar(36)   | 否    | 更新人ID                                    |
| updated_time            | datetime      | 否    | 更新时间                                     |
| is_deleted              | tinyint(1)    | 是    | 是否删除(0:否 1:是)                            |
| version                 | int           | 是    | 版本号(乐观锁)                                 |
| remark                  | varchar(500)  | 否    | 备注                                       |

#### 2.4.3 农民需求投入品明细表 (demand_farmer_input_item)

| 字段名            | 类型            | 必填   | 说明                               |
| -------------- | ------------- | ---- | -------------------------------- |
| id             | varchar(36)   | 是    | 主键UUID                           |
| demand_id      | varchar(36)   | 是    | 需求ID(关联demand_farmer_detail)     |
| input_category | varchar(50)   | 是    | 投入品大类(seed/fertilizer/pesticide) |
| input_type     | varchar(100)  | 是    | 农资类型                             |
| variety        | varchar(200)  | 是    | 品种                               |
| specification  | varchar(100)  | 否    | 规格                               |
| unit           | varchar(20)   | 是    | 单位                               |
| quantity       | decimal(15,2) | 是    | 需求数量                             |
| created_by     | varchar(36)   | 是    | 创建人ID                            |
| created_time   | datetime      | 是    | 创建时间                             |
| updated_by     | varchar(36)   | 否    | 更新人ID                            |
| updated_time   | datetime      | 否    | 更新时间                             |
| is_deleted     | tinyint(1)    | 是    | 是否删除(0:否 1:是)                    |

#### 2.4.4 需求汇总表 (demand_summary)

| 字段名               | 类型            | 必填   | 说明                                      |
| ----------------- | ------------- | ---- | --------------------------------------- |
| id                | varchar(36)   | 是    | 主键UUID                                  |
| batch_id          | varchar(36)   | 是    | 批次ID(关联demand_collection_batch)         |
| admin_level       | varchar(20)   | 是    | 行政层级(kebele/woreda/zone/region/state)   |
| admin_code        | varchar(50)   | 是    | 行政区划代码                                  |
| admin_name        | varchar(200)  | 是    | 行政区划名称                                  |
| parent_admin_code | varchar(50)   | 否    | 上级行政区划代码                                |
| input_category    | varchar(50)   | 是    | 投入品大类                                   |
| input_type        | varchar(100)  | 是    | 农资类型                                    |
| variety           | varchar(200)  | 否    | 品种                                      |
| total_quantity    | decimal(18,2) | 是    | 汇总数量                                    |
| farmer_count      | int           | 是    | 农民数量                                    |
| status            | varchar(20)   | 是    | 状态(pending/submitted/approved/rejected) |
| created_by        | varchar(36)   | 是    | 创建人ID                                   |
| created_time      | datetime      | 是    | 创建时间                                    |
| updated_by        | varchar(36)   | 否    | 更新人ID                                   |
| updated_time      | datetime      | 否    | 更新时间                                    |
| is_deleted        | tinyint(1)    | 是    | 是否删除(0:否 1:是)                           |
| version           | int           | 是    | 版本号(乐观锁)                                |

#### 2.4.5 需求审核记录表 (demand_audit_record)

| 字段名             | 类型            | 必填   | 说明                                       |
| --------------- | ------------- | ---- | ---------------------------------------- |
| id              | varchar(36)   | 是    | 主键UUID                                   |
| batch_id        | varchar(36)   | 是    | 批次ID(关联demand_collection_batch)          |
| demand_id       | varchar(36)   | 否    | 农民需求ID(关联demand_farmer_detail,单条审核时)     |
| summary_id      | varchar(36)   | 否    | 汇总ID(关联demand_summary,批量审核时)             |
| audit_type      | varchar(20)   | 是    | 审核类型(single/batch)                       |
| audit_level     | varchar(20)   | 是    | 审核层级(village/town/district/state/ministry) |
| admin_code      | varchar(50)   | 是    | 审核行政区划代码                                 |
| admin_name      | varchar(200)  | 是    | 审核行政区划名称                                 |
| audit_user_id   | varchar(36)   | 是    | 审核人ID                                    |
| audit_user_name | varchar(100)  | 是    | 审核人姓名                                    |
| audit_time      | datetime      | 是    | 审核时间                                     |
| audit_action    | varchar(20)   | 是    | 审核动作(submit/approve/reject)              |
| audit_result    | varchar(20)   | 是    | 审核结果(passed/rejected)                    |
| audit_opinion   | varchar(1000) | 否    | 审核意见(驳回时必填)                              |
| created_by      | varchar(36)   | 是    | 创建人ID                                    |
| created_time    | datetime      | 是    | 创建时间                                     |
| is_deleted      | tinyint(1)    | 是    | 是否删除(0:否 1:是)                            |
| remark          | varchar(500)  | 否    | 备注                                       |

---

## 三、数据库表关系图

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              注册管理模块                                    │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌─────────────────────┐                                                   │
│  │ org_enterprise_info │ (机构基础信息表)                                   │
│  │      PK: id         │                                                   │
│  └──────────┬──────────┘                                                   │
│             │                                                               │
│     ┌───────┼───────┬─────────────────────┐                                │
│     │       │       │                     │                                │
│     ▼       ▼       ▼                     ▼                                │
│  ┌──────┐ ┌──────┐ ┌──────┐        ┌───────────┐                          │
│  │位置表 │ │许可表 │ │账号表 │        │ 审核记录表  │                          │
│  │1:1   │ │1:N   │ │1:1   │        │    1:N    │                          │
│  └──────┘ └──────┘ └──────┘        └───────────┘                          │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                            需求采集管理模块                                   │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌───────────────────────┐                                                 │
│  │demand_collection_batch│ (需求采集批次表)                                 │
│  │       PK: id          │                                                 │
│  └───────────┬───────────┘                                                 │
│              │                                                              │
│      ┌───────┴───────┬─────────────────────┐                               │
│      │               │                     │                               │
│      ▼               ▼                     ▼                               │
│  ┌──────────┐  ┌───────────┐        ┌───────────┐                         │
│  │农民需求表 │  │ 汇总表     │        │ 审核记录表  │                         │
│  │   1:N    │  │   1:N     │        │    1:N    │                         │
│  └────┬─────┘  └───────────┘        └───────────┘                         │
│       │                                                                     │
│       ▼                                                                     │
│  ┌──────────┐                                                              │
│  │投入品明细 │                                                              │
│  │   1:N    │                                                              │
│  └──────────┘                                                              │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 四、通用字段说明

### 4.1 审计字段

所有表均包含以下审计字段：

| 字段名          | 类型          | 说明              |
| ------------ | ----------- | --------------- |
| created_by   | varchar(36) | 创建人ID           |
| created_time | datetime    | 创建时间            |
| updated_by   | varchar(36) | 最后更新人ID         |
| updated_time | datetime    | 最后更新时间          |
| is_deleted   | tinyint(1)  | 逻辑删除标识(0:否 1:是) |

### 4.2 版本控制字段

需要乐观锁控制的表包含：

| 字段名     | 类型   | 说明          |
| ------- | ---- | ----------- |
| version | int  | 版本号，用于乐观锁控制 |

### 4.3 状态枚举值

**机构申请状态 (application_status)：**
- draft: 草稿
- pending: 待审核
- approved: 已通过
- rejected: 已驳回

**需求状态 (status)：**
- draft: 草稿
- submitted: 已提交
- approved: 已通过
- rejected: 已驳回
- locked: 已锁定

**审核层级 (audit_level/current_audit_level)：**
- village: 村级
- town: 镇级
- district: 区级
- state: 州级
- ministry: 农业部

**投入品大类 (input_category)：**
- seed: 种子
- fertilizer: 化肥
- pesticide: 农药