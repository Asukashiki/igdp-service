# 投入品供应管理系统 - 后端接口开发提示词

## 一、项目概述

本文档为投入品供应管理系统的后端接口开发指南，包含注册管理模块和投入品需求采集管理模块的完整接口设计、业务逻辑说明及数据库表结构。

---

## 二、数据库表结构SQL

```sql
-- ========================================
-- 注册管理模块
-- ========================================

-- 机构基础信息表
CREATE TABLE org_enterprise_info (
    id VARCHAR(36) NOT NULL COMMENT '主键UUID',
    enterprise_name VARCHAR(200) NOT NULL COMMENT '机构名称',
    enterprise_registration_id VARCHAR(100) DEFAULT NULL COMMENT '企业注册号',
    unified_social_credit_code VARCHAR(50) DEFAULT NULL COMMENT '统一社会信用代码',
    seed_enterprise_license_number VARCHAR(100) NOT NULL COMMENT '种子企业许可证号',
    license_validity_start DATE NOT NULL COMMENT '许可证有效期开始',
    license_validity_end DATE NOT NULL COMMENT '许可证有效期结束',
    enterprise_type VARCHAR(50) NOT NULL COMMENT '企业类型',
    org_type VARCHAR(20) NOT NULL COMMENT '机构类型(union/cooperative)',
    input_types VARCHAR(100) NOT NULL COMMENT '投入品类型(多选,逗号分隔:seed,fertilizer,pesticide)',
    sales_regions TEXT NOT NULL COMMENT '销售区域(组织机构代码,多选,逗号分隔)',
    application_status VARCHAR(20) NOT NULL DEFAULT 'draft' COMMENT '申请状态(draft/pending/approved/rejected)',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by VARCHAR(36) DEFAULT NULL COMMENT '更新人ID',
    updated_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0:否 1:是)',
    version INT NOT NULL DEFAULT 1 COMMENT '版本号(乐观锁)',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    INDEX idx_org_type (org_type),
    INDEX idx_application_status (application_status),
    INDEX idx_created_time (created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机构基础信息表';

-- 机构位置运营信息表
CREATE TABLE org_enterprise_location (
    id VARCHAR(36) NOT NULL COMMENT '主键UUID',
    enterprise_id VARCHAR(36) NOT NULL COMMENT '机构ID(关联org_enterprise_info)',
    region VARCHAR(100) DEFAULT NULL COMMENT '大区',
    zone VARCHAR(100) DEFAULT NULL COMMENT 'Zone',
    woreda VARCHAR(100) NOT NULL COMMENT 'Woreda',
    kebele VARCHAR(100) NOT NULL COMMENT 'Kebele',
    full_address VARCHAR(500) NOT NULL COMMENT '详细地址',
    gps_latitude DECIMAL(10,7) DEFAULT NULL COMMENT 'GPS纬度',
    gps_longitude DECIMAL(10,7) DEFAULT NULL COMMENT 'GPS经度',
    business_scope VARCHAR(500) DEFAULT NULL COMMENT '经营范围',
    annual_production_capacity DECIMAL(15,2) DEFAULT NULL COMMENT '年生产能力',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by VARCHAR(36) DEFAULT NULL COMMENT '更新人ID',
    updated_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0:否 1:是)',
    PRIMARY KEY (id),
    INDEX idx_enterprise_id (enterprise_id),
    CONSTRAINT fk_location_enterprise FOREIGN KEY (enterprise_id) REFERENCES org_enterprise_info(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机构位置运营信息表';

-- 机构许可证件表
CREATE TABLE org_enterprise_license (
    id VARCHAR(36) NOT NULL COMMENT '主键UUID',
    enterprise_id VARCHAR(36) NOT NULL COMMENT '机构ID(关联org_enterprise_info)',
    license_type VARCHAR(50) NOT NULL COMMENT '证件类型(business_license/seed_license/tax_certificate/factory_permit)',
    license_number VARCHAR(100) DEFAULT NULL COMMENT '证件号码',
    license_file_url VARCHAR(500) NOT NULL COMMENT '证件文件URL',
    issue_date DATE DEFAULT NULL COMMENT '发证日期',
    expiry_date DATE DEFAULT NULL COMMENT '到期日期',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by VARCHAR(36) DEFAULT NULL COMMENT '更新人ID',
    updated_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0:否 1:是)',
    PRIMARY KEY (id),
    INDEX idx_enterprise_id (enterprise_id),
    INDEX idx_license_type (license_type),
    CONSTRAINT fk_license_enterprise FOREIGN KEY (enterprise_id) REFERENCES org_enterprise_info(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机构许可证件表';

-- 机构注册审核记录表
CREATE TABLE org_registration_audit (
    id VARCHAR(36) NOT NULL COMMENT '主键UUID',
    enterprise_id VARCHAR(36) NOT NULL COMMENT '机构ID(关联org_enterprise_info)',
    audit_user_id VARCHAR(36) NOT NULL COMMENT '审核人ID',
    audit_user_name VARCHAR(100) NOT NULL COMMENT '审核人姓名',
    audit_time DATETIME NOT NULL COMMENT '审核时间',
    audit_result VARCHAR(20) NOT NULL COMMENT '审核结果(approved/rejected)',
    audit_opinion VARCHAR(1000) DEFAULT NULL COMMENT '审核意见(驳回时必填)',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0:否 1:是)',
    PRIMARY KEY (id),
    INDEX idx_enterprise_id (enterprise_id),
    INDEX idx_audit_time (audit_time),
    CONSTRAINT fk_audit_enterprise FOREIGN KEY (enterprise_id) REFERENCES org_enterprise_info(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机构注册审核记录表';

-- ========================================
-- 投入品需求采集管理模块
-- ========================================

-- 需求采集批次表
CREATE TABLE demand_collection_batch (
    id VARCHAR(36) NOT NULL COMMENT '主键UUID',
    batch_no VARCHAR(50) NOT NULL COMMENT '批次编号',
    batch_name VARCHAR(200) NOT NULL COMMENT '批次名称',
    year INT NOT NULL COMMENT '年度',
    start_date DATE NOT NULL COMMENT '采集开始日期',
    end_date DATE NOT NULL COMMENT '采集结束日期',
    status VARCHAR(20) NOT NULL DEFAULT 'collecting' COMMENT '状态(collecting/reviewing/completed/locked)',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by VARCHAR(36) DEFAULT NULL COMMENT '更新人ID',
    updated_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0:否 1:是)',
    version INT NOT NULL DEFAULT 1 COMMENT '版本号(乐观锁)',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE INDEX uk_batch_no (batch_no),
    INDEX idx_year (year),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='需求采集批次表';

-- 农民需求明细表
CREATE TABLE demand_farmer_detail (
    id VARCHAR(36) NOT NULL COMMENT '主键UUID',
    batch_id VARCHAR(36) NOT NULL COMMENT '批次ID(关联demand_collection_batch)',
    farmer_id VARCHAR(36) NOT NULL COMMENT '农民ID',
    farmer_name VARCHAR(100) NOT NULL COMMENT '农民姓名',
    farmer_id_number VARCHAR(50) NOT NULL COMMENT '农民身份证号',
    region VARCHAR(100) DEFAULT NULL COMMENT '大区',
    zone VARCHAR(100) DEFAULT NULL COMMENT 'Zone',
    woreda VARCHAR(100) NOT NULL COMMENT 'Woreda',
    kebele VARCHAR(100) NOT NULL COMMENT 'Kebele',
    village VARCHAR(100) NOT NULL COMMENT '村庄',
    land_area DECIMAL(10,2) DEFAULT NULL COMMENT '地块总面积(公顷)',
    max_seed_quantity DECIMAL(15,2) DEFAULT NULL COMMENT '估算最大种子量(kg)',
    max_fertilizer_quantity DECIMAL(15,2) DEFAULT NULL COMMENT '估算最大肥料量(kg)',
    status VARCHAR(20) NOT NULL DEFAULT 'draft' COMMENT '状态(draft/submitted/approved/rejected)',
    current_audit_level VARCHAR(20) DEFAULT NULL COMMENT '当前审核层级(village/town/district/state/ministry)',
    da_user_id VARCHAR(36) NOT NULL COMMENT '录入DA用户ID',
    da_user_name VARCHAR(100) NOT NULL COMMENT '录入DA用户姓名',
    submit_time DATETIME DEFAULT NULL COMMENT '提交时间',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by VARCHAR(36) DEFAULT NULL COMMENT '更新人ID',
    updated_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0:否 1:是)',
    version INT NOT NULL DEFAULT 1 COMMENT '版本号(乐观锁)',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    INDEX idx_batch_id (batch_id),
    INDEX idx_farmer_id (farmer_id),
    INDEX idx_kebele (kebele),
    INDEX idx_status (status),
    INDEX idx_current_audit_level (current_audit_level),
    CONSTRAINT fk_demand_batch FOREIGN KEY (batch_id) REFERENCES demand_collection_batch(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='农民需求明细表';

-- 农民需求投入品明细表
CREATE TABLE demand_farmer_input_item (
    id VARCHAR(36) NOT NULL COMMENT '主键UUID',
    demand_id VARCHAR(36) NOT NULL COMMENT '需求ID(关联demand_farmer_detail)',
    input_category VARCHAR(50) NOT NULL COMMENT '投入品大类(seed/fertilizer/pesticide)',
    input_type VARCHAR(100) NOT NULL COMMENT '农资类型',
    variety VARCHAR(200) NOT NULL COMMENT '品种',
    specification VARCHAR(100) DEFAULT NULL COMMENT '规格',
    unit VARCHAR(20) NOT NULL COMMENT '单位',
    quantity DECIMAL(15,2) NOT NULL COMMENT '需求数量',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by VARCHAR(36) DEFAULT NULL COMMENT '更新人ID',
    updated_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0:否 1:是)',
    PRIMARY KEY (id),
    INDEX idx_demand_id (demand_id),
    INDEX idx_input_category (input_category),
    CONSTRAINT fk_item_demand FOREIGN KEY (demand_id) REFERENCES demand_farmer_detail(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='农民需求投入品明细表';

-- 需求汇总表
CREATE TABLE demand_summary (
    id VARCHAR(36) NOT NULL COMMENT '主键UUID',
    batch_id VARCHAR(36) NOT NULL COMMENT '批次ID(关联demand_collection_batch)',
    admin_level VARCHAR(20) NOT NULL COMMENT '行政层级(kebele/woreda/zone/region/state)',
    admin_code VARCHAR(50) NOT NULL COMMENT '行政区划代码',
    admin_name VARCHAR(200) NOT NULL COMMENT '行政区划名称',
    parent_admin_code VARCHAR(50) DEFAULT NULL COMMENT '上级行政区划代码',
    input_category VARCHAR(50) NOT NULL COMMENT '投入品大类',
    input_type VARCHAR(100) NOT NULL COMMENT '农资类型',
    variety VARCHAR(200) DEFAULT NULL COMMENT '品种',
    total_quantity DECIMAL(18,2) NOT NULL COMMENT '汇总数量',
    farmer_count INT NOT NULL COMMENT '农民数量',
    status VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '状态(pending/submitted/approved/rejected)',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by VARCHAR(36) DEFAULT NULL COMMENT '更新人ID',
    updated_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0:否 1:是)',
    version INT NOT NULL DEFAULT 1 COMMENT '版本号(乐观锁)',
    PRIMARY KEY (id),
    INDEX idx_batch_id (batch_id),
    INDEX idx_admin_level (admin_level),
    INDEX idx_admin_code (admin_code),
    INDEX idx_status (status),
    CONSTRAINT fk_summary_batch FOREIGN KEY (batch_id) REFERENCES demand_collection_batch(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='需求汇总表';

-- 需求审核记录表
CREATE TABLE demand_audit_record (
    id VARCHAR(36) NOT NULL COMMENT '主键UUID',
    batch_id VARCHAR(36) NOT NULL COMMENT '批次ID(关联demand_collection_batch)',
    demand_id VARCHAR(36) DEFAULT NULL COMMENT '农民需求ID(关联demand_farmer_detail,单条审核时)',
    summary_id VARCHAR(36) DEFAULT NULL COMMENT '汇总ID(关联demand_summary,批量审核时)',
    audit_type VARCHAR(20) NOT NULL COMMENT '审核类型(single/batch)',
    audit_level VARCHAR(20) NOT NULL COMMENT '审核层级(village/town/district/state/ministry)',
    admin_code VARCHAR(50) NOT NULL COMMENT '审核行政区划代码',
    admin_name VARCHAR(200) NOT NULL COMMENT '审核行政区划名称',
    audit_user_id VARCHAR(36) NOT NULL COMMENT '审核人ID',
    audit_user_name VARCHAR(100) NOT NULL COMMENT '审核人姓名',
    audit_time DATETIME NOT NULL COMMENT '审核时间',
    audit_action VARCHAR(20) NOT NULL COMMENT '审核动作(submit/approve/reject)',
    audit_result VARCHAR(20) NOT NULL COMMENT '审核结果(passed/rejected)',
    audit_opinion VARCHAR(1000) DEFAULT NULL COMMENT '审核意见(驳回时必填)',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0:否 1:是)',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    INDEX idx_batch_id (batch_id),
    INDEX idx_demand_id (demand_id),
    INDEX idx_summary_id (summary_id),
    INDEX idx_audit_level (audit_level),
    INDEX idx_audit_time (audit_time),
    CONSTRAINT fk_audit_batch FOREIGN KEY (batch_id) REFERENCES demand_collection_batch(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='需求审核记录表';
```

---

## 三、注册管理模块接口设计

### 3.1 机构注册申请接口

#### 3.1.1 新增机构注册申请

**接口路径：** `POST /api/seed/registration/add`

**接口描述：** Zone录入Union信息或Woreda录入Cooperative信息，提交注册申请

**业务逻辑：**
1. 校验必填字段完整性
2. 校验许可证有效期（开始日期不能晚于结束日期，结束日期不能早于当前日期）
3. 校验投入品类型为有效枚举值（seed/fertilizer/pesticide）
4. 校验销售区域代码有效性
5. 生成UUID作为主键
6. 初始化申请状态为draft
7. 保存机构基础信息、位置信息、许可证件信息
8. 返回机构ID

**请求参数：**

```json
{
    "enterpriseName": "string, 必填, 机构名称, 最大200字符",
    "enterpriseRegistrationId": "string, 选填, 企业注册号, 最大100字符",
    "unifiedSocialCreditCode": "string, 选填, 统一社会信用代码, 最大50字符",
    "seedEnterpriseLicenseNumber": "string, 必填, 种子企业许可证号, 最大100字符",
    "licenseValidityStart": "date, 必填, 许可证有效期开始, 格式yyyy-MM-dd",
    "licenseValidityEnd": "date, 必填, 许可证有效期结束, 格式yyyy-MM-dd",
    "enterpriseType": "string, 必填, 企业类型, 最大50字符",
    "orgType": "string, 必填, 机构类型, 枚举值: union/cooperative",
    "inputTypes": ["string"], "必填, 投入品类型数组, 枚举值: seed/fertilizer/pesticide",
    "salesRegions": ["string"], "必填, 销售区域代码数组",
    "remark": "string, 选填, 备注, 最大500字符",
    "location": {
        "region": "string, 选填, 大区, 最大100字符",
        "zone": "string, 选填, Zone, 最大100字符",
        "woreda": "string, 必填, Woreda, 最大100字符",
        "kebele": "string, 必填, Kebele, 最大100字符",
        "fullAddress": "string, 必填, 详细地址, 最大500字符",
        "gpsLatitude": "number, 选填, GPS纬度",
        "gpsLongitude": "number, 选填, GPS经度",
        "businessScope": "string, 选填, 经营范围, 最大500字符",
        "annualProductionCapacity": "number, 选填, 年生产能力"
    },
    "licenses": [
        {
            "licenseType": "string, 必填, 证件类型, 枚举值: business_license/seed_license/tax_certificate/factory_permit",
            "licenseNumber": "string, 选填, 证件号码, 最大100字符",
            "licenseFileUrl": "string, 必填, 证件文件URL, 最大500字符",
            "issueDate": "date, 选填, 发证日期, 格式yyyy-MM-dd",
            "expiryDate": "date, 选填, 到期日期, 格式yyyy-MM-dd"
        }
    ]
}
```

**响应参数：**

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "id": "string, 机构ID"
    }
}
```

---

#### 3.1.2 修改机构注册申请

**接口路径：** `POST /api/seed/registration/update`

**接口描述：** 修改草稿或被驳回状态的机构注册申请信息

**业务逻辑：**
1. 校验机构ID存在且未删除
2. 校验申请状态为draft或rejected，否则不允许修改
3. 校验必填字段完整性
4. 校验许可证有效期合法性
5. 使用乐观锁更新数据，版本号+1
6. 更新机构基础信息、位置信息、许可证件信息
7. 被驳回状态下修改后保持draft状态

**请求参数：**

```json
{
    "id": "string, 必填, 机构ID",
    "version": "int, 必填, 版本号",
    "enterpriseName": "string, 必填, 机构名称",
    "enterpriseRegistrationId": "string, 选填, 企业注册号",
    "unifiedSocialCreditCode": "string, 选填, 统一社会信用代码",
    "seedEnterpriseLicenseNumber": "string, 必填, 种子企业许可证号",
    "licenseValidityStart": "date, 必填, 许可证有效期开始",
    "licenseValidityEnd": "date, 必填, 许可证有效期结束",
    "enterpriseType": "string, 必填, 企业类型",
    "orgType": "string, 必填, 机构类型",
    "inputTypes": ["string"],
    "salesRegions": ["string"],
    "remark": "string, 选填, 备注",
    "location": {
        "id": "string, 选填, 位置信息ID(修改时传入)",
        "region": "string, 选填",
        "zone": "string, 选填",
        "woreda": "string, 必填",
        "kebele": "string, 必填",
        "fullAddress": "string, 必填",
        "gpsLatitude": "number, 选填",
        "gpsLongitude": "number, 选填",
        "businessScope": "string, 选填",
        "annualProductionCapacity": "number, 选填"
    },
    "licenses": [
        {
            "id": "string, 选填, 许可证ID(修改时传入)",
            "licenseType": "string, 必填",
            "licenseNumber": "string, 选填",
            "licenseFileUrl": "string, 必填",
            "issueDate": "date, 选填",
            "expiryDate": "date, 选填"
        }
    ]
}
```

**响应参数：**

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": null
}
```

---

#### 3.1.3 提交机构注册申请

**接口路径：** `POST /api/seed/registration/submit`

**接口描述：** 将草稿状态的注册申请提交审核

**业务逻辑：**
1. 校验机构ID存在且未删除
2. 校验当前状态为draft
3. 校验所有必填信息完整
4. 更新申请状态为pending
5. 使用乐观锁更新，版本号+1

**请求参数：**

```json
{
    "id": "string, 必填, 机构ID",
    "version": "int, 必填, 版本号"
}
```

**响应参数：**

```json
{
    "code": 200,
    "msg": "提交成功",
    "data": null
}
```

---

#### 3.1.4 机构注册申请详情

**接口路径：** `GET /api/seed/registration/detail`

**接口描述：** 查询机构注册申请详细信息

**业务逻辑：**
1. 根据机构ID查询基础信息
2. 关联查询位置信息
3. 关联查询许可证件列表
4. 关联查询审核记录列表
5. 组装返回完整数据

**请求参数：**

| 参数名  | 类型     | 必填   | 说明   |
| ---- | ------ | ---- | ---- |
| id   | string | 是    | 机构ID |

**响应参数：**

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "id": "string, 机构ID",
        "enterpriseName": "string, 机构名称",
        "enterpriseRegistrationId": "string, 企业注册号",
        "unifiedSocialCreditCode": "string, 统一社会信用代码",
        "seedEnterpriseLicenseNumber": "string, 种子企业许可证号",
        "licenseValidityStart": "date, 许可证有效期开始",
        "licenseValidityEnd": "date, 许可证有效期结束",
        "enterpriseType": "string, 企业类型",
        "orgType": "string, 机构类型",
        "inputTypes": ["string, 投入品类型数组"],
        "salesRegions": ["string, 销售区域代码数组"],
        "applicationStatus": "string, 申请状态",
        "version": "int, 版本号",
        "remark": "string, 备注",
        "createdTime": "datetime, 创建时间",
        "location": {
            "id": "string, 位置ID",
            "region": "string, 大区",
            "zone": "string, Zone",
            "woreda": "string, Woreda",
            "kebele": "string, Kebele",
            "fullAddress": "string, 详细地址",
            "gpsLatitude": "number, GPS纬度",
            "gpsLongitude": "number, GPS经度",
            "businessScope": "string, 经营范围",
            "annualProductionCapacity": "number, 年生产能力"
        },
        "licenses": [
            {
                "id": "string, 许可证ID",
                "licenseType": "string, 证件类型",
                "licenseTypeName": "string, 证件类型名称",
                "licenseNumber": "string, 证件号码",
                "licenseFileUrl": "string, 证件文件URL",
                "issueDate": "date, 发证日期",
                "expiryDate": "date, 到期日期"
            }
        ],
        "auditRecords": [
            {
                "id": "string, 审核记录ID",
                "auditUserName": "string, 审核人姓名",
                "auditTime": "datetime, 审核时间",
                "auditResult": "string, 审核结果",
                "auditOpinion": "string, 审核意见"
            }
        ]
    }
}
```

---

#### 3.1.5 机构注册申请分页查询

**接口路径：** `POST /api/seed/registration/page`

**接口描述：** 分页查询机构注册申请列表

**业务逻辑：**
1. 根据当前用户行政区划过滤数据
2. 支持多条件组合查询
3. 使用QueryWrapper构建查询条件
4. 按创建时间倒序排列
5. 分页返回

**请求参数：**

```json
{
    "pageNum": "int, 必填, 当前页码, 默认1",
    "pageSize": "int, 必填, 每页条数, 默认10",
    "enterpriseName": "string, 选填, 机构名称(模糊查询)",
    "orgType": "string, 选填, 机构类型",
    "applicationStatus": "string, 选填, 申请状态",
    "inputTypes": "string, 选填, 投入品类型",
    "woreda": "string, 选填, Woreda",
    "zone": "string, 选填, Zone",
    "createdTimeStart": "date, 选填, 创建时间开始",
    "createdTimeEnd": "date, 选填, 创建时间结束"
}
```

**响应参数：**

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "records": [
            {
                "id": "string, 机构ID",
                "enterpriseName": "string, 机构名称",
                "orgType": "string, 机构类型",
                "orgTypeName": "string, 机构类型名称",
                "inputTypes": "string, 投入品类型",
                "applicationStatus": "string, 申请状态",
                "applicationStatusName": "string, 申请状态名称",
                "woreda": "string, Woreda",
                "zone": "string, Zone",
                "createdTime": "datetime, 创建时间"
            }
        ],
        "total": "long, 总记录数",
        "size": "long, 每页条数",
        "current": "long, 当前页码",
        "pages": "long, 总页数"
    }
}
```

---

#### 3.1.6 删除机构注册申请

**接口路径：** `POST /api/seed/registration/delete`

**接口描述：** 删除草稿状态的机构注册申请

**业务逻辑：**
1. 校验机构ID存在
2. 校验当前状态为draft，其他状态不允许删除
3. 逻辑删除机构基础信息
4. 逻辑删除关联的位置信息
5. 逻辑删除关联的许可证件信息

**请求参数：**

```json
{
    "id": "string, 必填, 机构ID"
}
```

**响应参数：**

```json
{
    "code": 200,
    "msg": "删除成功",
    "data": null
}
```

---

### 3.2 机构注册审核接口

#### 3.2.1 待审核列表查询

**接口路径：** `POST /api/seed/registration/audit/page`

**接口描述：** 分页查询待审核的机构注册申请

**业务逻辑：**
1. 根据当前用户角色确定审核权限范围
2. Zone用户只能查看其管辖范围内的Union申请
3. Woreda用户只能查看其管辖范围内的Cooperative申请
4. 筛选状态为pending的申请
5. 使用QueryWrapper构建查询条件
6. 分页返回

**请求参数：**

```json
{
    "pageNum": "int, 必填, 当前页码",
    "pageSize": "int, 必填, 每页条数",
    "enterpriseName": "string, 选填, 机构名称(模糊查询)",
    "orgType": "string, 选填, 机构类型",
    "createdTimeStart": "date, 选填, 申请时间开始",
    "createdTimeEnd": "date, 选填, 申请时间结束"
}
```

**响应参数：**

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "records": [
            {
                "id": "string, 机构ID",
                "enterpriseName": "string, 机构名称",
                "orgType": "string, 机构类型",
                "orgTypeName": "string, 机构类型名称",
                "inputTypes": "string, 投入品类型",
                "woreda": "string, Woreda",
                "zone": "string, Zone",
                "createdTime": "datetime, 申请时间"
            }
        ],
        "total": "long, 总记录数",
        "size": "long, 每页条数",
        "current": "long, 当前页码",
        "pages": "long, 总页数"
    }
}
```

---

#### 3.2.2 审核通过

**接口路径：** `POST /api/seed/registration/audit/approve`

**接口描述：** 审核通过机构注册申请

**业务逻辑：**
1. 校验机构ID存在且状态为pending
2. 校验当前用户有审核权限
3. 更新申请状态为approved
4. 创建审核记录
5. 为机构开通系统权限（调用权限服务）
6. 使用乐观锁更新，版本号+1

**请求参数：**

```json
{
    "id": "string, 必填, 机构ID",
    "version": "int, 必填, 版本号",
    "remark": "string, 选填, 审核备注, 最大500字符"
}
```

**响应参数：**

```json
{
    "code": 200,
    "msg": "审核通过",
    "data": null
}
```

---

#### 3.2.3 审核驳回

**接口路径：** `POST /api/seed/registration/audit/reject`

**接口描述：** 驳回机构注册申请

**业务逻辑：**
1. 校验机构ID存在且状态为pending
2. 校验当前用户有审核权限
3. 校验审核意见必填
4. 更新申请状态为rejected
5. 创建审核记录
6. 使用乐观锁更新，版本号+1

**请求参数：**

```json
{
    "id": "string, 必填, 机构ID",
    "version": "int, 必填, 版本号",
    "auditOpinion": "string, 必填, 审核意见, 最大1000字符",
    "remark": "string, 选填, 审核备注, 最大500字符"
}
```

**响应参数：**

```json
{
    "code": 200,
    "msg": "已驳回",
    "data": null
}
```

---

#### 3.2.4 审核记录查询

**接口路径：** `GET /api/seed/registration/audit/list`

**接口描述：** 查询机构的审核记录列表

**业务逻辑：**
1. 根据机构ID查询所有审核记录
2. 按审核时间倒序排列
3. 列表返回

**请求参数：**

| 参数名          | 类型     | 必填   | 说明   |
| ------------ | ------ | ---- | ---- |
| enterpriseId | string | 是    | 机构ID |

**响应参数：**

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": [
        {
            "id": "string, 审核记录ID",
            "auditUserName": "string, 审核人姓名",
            "auditTime": "datetime, 审核时间",
            "auditResult": "string, 审核结果",
            "auditResultName": "string, 审核结果名称",
            "auditOpinion": "string, 审核意见",
            "remark": "string, 备注"
        }
    ]
}
```

---

## 四、投入品需求采集管理模块接口设计

### 4.1 需求采集批次管理接口

#### 4.1.1 新增采集批次

**接口路径：** `POST /api/seed/demand/batch/add`

**接口描述：** 创建新的需求采集批次

**业务逻辑：**
1. 校验必填字段
2. 校验采集开始日期不晚于结束日期
3. 生成批次编号（规则：DEMAND-年份-序号）
4. 生成UUID作为主键
5. 初始化状态为collecting
6. 保存批次信息

**请求参数：**

```json
{
    "batchName": "string, 必填, 批次名称, 最大200字符",
    "year": "int, 必填, 年度",
    "startDate": "date, 必填, 采集开始日期, 格式yyyy-MM-dd",
    "endDate": "date, 必填, 采集结束日期, 格式yyyy-MM-dd",
    "remark": "string, 选填, 备注, 最大500字符"
}
```

**响应参数：**

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "id": "string, 批次ID",
        "batchNo": "string, 批次编号"
    }
}
```

---

#### 4.1.2 修改采集批次

**接口路径：** `POST /api/seed/demand/batch/update`

**接口描述：** 修改采集中状态的批次信息

**业务逻辑：**
1. 校验批次ID存在
2. 校验状态为collecting，其他状态不允许修改
3. 校验日期有效性
4. 使用乐观锁更新
5. 不允许修改批次编号和年度

**请求参数：**

```json
{
    "id": "string, 必填, 批次ID",
    "version": "int, 必填, 版本号",
    "batchName": "string, 必填, 批次名称",
    "startDate": "date, 必填, 采集开始日期",
    "endDate": "date, 必填, 采集结束日期",
    "remark": "string, 选填, 备注"
}
```

**响应参数：**

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": null
}
```

---

#### 4.1.3 批次详情

**接口路径：** `GET /api/seed/demand/batch/detail`

**接口描述：** 查询批次详细信息

**业务逻辑：**
1. 根据批次ID查询基础信息
2. 统计该批次下的需求记录数量
3. 统计各状态的需求数量

**请求参数：**

| 参数名  | 类型     | 必填   | 说明   |
| ---- | ------ | ---- | ---- |
| id   | string | 是    | 批次ID |

**响应参数：**

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "id": "string, 批次ID",
        "batchNo": "string, 批次编号",
        "batchName": "string, 批次名称",
        "year": "int, 年度",
        "startDate": "date, 采集开始日期",
        "endDate": "date, 采集结束日期",
        "status": "string, 状态",
        "statusName": "string, 状态名称",
        "version": "int, 版本号",
        "remark": "string, 备注",
        "createdTime": "datetime, 创建时间",
        "statistics": {
            "totalCount": "int, 总需求数",
            "draftCount": "int, 草稿数",
            "submittedCount": "int, 已提交数",
            "approvedCount": "int, 已通过数",
            "rejectedCount": "int, 已驳回数"
        }
    }
}
```

---

#### 4.1.4 批次分页查询

**接口路径：** `POST /api/seed/demand/batch/page`

**接口描述：** 分页查询采集批次列表

**业务逻辑：**
1. 支持多条件组合查询
2. 使用QueryWrapper构建查询条件
3. 按创建时间倒序排列
4. 分页返回

**请求参数：**

```json
{
    "pageNum": "int, 必填, 当前页码",
    "pageSize": "int, 必填, 每页条数",
    "batchNo": "string, 选填, 批次编号(模糊查询)",
    "batchName": "string, 选填, 批次名称(模糊查询)",
    "year": "int, 选填, 年度",
    "status": "string, 选填, 状态"
}
```

**响应参数：**

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "records": [
            {
                "id": "string, 批次ID",
                "batchNo": "string, 批次编号",
                "batchName": "string, 批次名称",
                "year": "int, 年度",
                "startDate": "date, 采集开始日期",
                "endDate": "date, 采集结束日期",
                "status": "string, 状态",
                "statusName": "string, 状态名称",
                "createdTime": "datetime, 创建时间"
            }
        ],
        "total": "long, 总记录数",
        "size": "long, 每页条数",
        "current": "long, 当前页码",
        "pages": "long, 总页数"
    }
}
```

---

#### 4.1.5 删除采集批次

**接口路径：** `POST /api/seed/demand/batch/delete`

**接口描述：** 删除采集中且无需求数据的批次

**业务逻辑：**
1. 校验批次ID存在
2. 校验状态为collecting
3. 校验该批次下无需求数据
4. 逻辑删除批次

**请求参数：**

```json
{
    "id": "string, 必填, 批次ID"
}
```

**响应参数：**

```json
{
    "code": 200,
    "msg": "删除成功",
    "data": null
}
```

---

### 4.2 农民需求录入接口

#### 4.2.1 新增农民需求

**接口路径：** `POST /api/seed/demand/farmer/add`

**接口描述：** DA录入单个农民的投入品需求

**业务逻辑：**
1. 校验批次ID存在且状态为collecting
2. 校验农民ID、姓名、身份证号必填
3. 校验投入品明细至少有一条
4. 根据地块信息计算最大种子量、最大肥料量（可调用外部服务）
5. 生成UUID作为主键
6. 初始化状态为draft
7. 保存农民需求及投入品明细

**请求参数：**

```json
{
    "batchId": "string, 必填, 批次ID",
    "farmerId": "string, 必填, 农民ID",
    "farmerName": "string, 必填, 农民姓名, 最大100字符",
    "farmerIdNumber": "string, 必填, 农民身份证号, 最大50字符",
    "region": "string, 选填, 大区",
    "zone": "string, 选填, Zone",
    "woreda": "string, 必填, Woreda",
    "kebele": "string, 必填, Kebele",
    "village": "string, 必填, 村庄",
    "landArea": "number, 选填, 地块总面积(公顷)",
    "remark": "string, 选填, 备注",
    "inputItems": [
        {
            "inputCategory": "string, 必填, 投入品大类, 枚举值: seed/fertilizer/pesticide",
            "inputType": "string, 必填, 农资类型",
            "variety": "string, 必填, 品种",
            "specification": "string, 选填, 规格",
            "unit": "string, 必填, 单位",
            "quantity": "number, 必填, 需求数量"
        }
    ]
}
```

**响应参数：**

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "id": "string, 需求ID"
    }
}
```

---

#### 4.2.2 修改农民需求

**接口路径：** `POST /api/seed/demand/farmer/update`

**接口描述：** 修改草稿或被驳回状态的农民需求

**业务逻辑：**
1. 校验需求ID存在
2. 校验状态为draft或rejected
3. 校验当前用户为录入的DA
4. 使用乐观锁更新
5. 先删除原投入品明细，再新增

**请求参数：**

```json
{
    "id": "string, 必填, 需求ID",
    "version": "int, 必填, 版本号",
    "farmerId": "string, 必填, 农民ID",
    "farmerName": "string, 必填, 农民姓名",
    "farmerIdNumber": "string, 必填, 农民身份证号",
    "region": "string, 选填, 大区",
    "zone": "string, 选填, Zone",
    "woreda": "string, 必填, Woreda",
    "kebele": "string, 必填, Kebele",
    "village": "string, 必填, 村庄",
    "landArea": "number, 选填, 地块总面积",
    "remark": "string, 选填, 备注",
    "inputItems": [
        {
            "inputCategory": "string, 必填, 投入品大类",
            "inputType": "string, 必填, 农资类型",
            "variety": "string, 必填, 品种",
            "specification": "string, 选填, 规格",
            "unit": "string, 必填, 单位",
            "quantity": "number, 必填, 需求数量"
        }
    ]
}
```

**响应参数：**

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": null
}
```

---

#### 4.2.3 农民需求详情

**接口路径：** `GET /api/seed/demand/farmer/detail`

**接口描述：** 查询农民需求详细信息

**业务逻辑：**
1. 根据需求ID查询基础信息
2. 关联查询投入品明细列表
3. 关联查询审核记录列表
4. 组装返回

**请求参数：**

| 参数名  | 类型     | 必填   | 说明   |
| ---- | ------ | ---- | ---- |
| id   | string | 是    | 需求ID |

**响应参数：**

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "id": "string, 需求ID",
        "batchId": "string, 批次ID",
        "batchNo": "string, 批次编号",
        "farmerId": "string, 农民ID",
        "farmerName": "string, 农民姓名",
        "farmerIdNumber": "string, 农民身份证号",
        "region": "string, 大区",
        "zone": "string, Zone",
        "woreda": "string, Woreda",
        "kebele": "string, Kebele",
        "village": "string, 村庄",
        "landArea": "number, 地块总面积",
        "maxSeedQuantity": "number, 估算最大种子量",
        "maxFertilizerQuantity": "number, 估算最大肥料量",
        "status": "string, 状态",
        "statusName": "string, 状态名称",
        "currentAuditLevel": "string, 当前审核层级",
        "currentAuditLevelName": "string, 当前审核层级名称",
        "daUserName": "string, 录入DA姓名",
        "submitTime": "datetime, 提交时间",
        "version": "int, 版本号",
        "remark": "string, 备注",
        "createdTime": "datetime, 创建时间",
        "inputItems": [
            {
                "id": "string, 明细ID",
                "inputCategory": "string, 投入品大类",
                "inputCategoryName": "string, 投入品大类名称",
                "inputType": "string, 农资类型",
                "variety": "string, 品种",
                "specification": "string, 规格",
                "unit": "string, 单位",
                "quantity": "number, 需求数量"
            }
        ],
        "auditRecords": [
            {
                "id": "string, 审核记录ID",
                "auditLevel": "string, 审核层级",
                "auditLevelName": "string, 审核层级名称",
                "auditUserName": "string, 审核人姓名",
                "auditTime": "datetime, 审核时间",
                "auditAction": "string, 审核动作",
                "auditResult": "string, 审核结果",
                "auditOpinion": "string, 审核意见"
            }
        ]
    }
}
```

---

#### 4.2.4 农民需求分页查询

**接口路径：** `POST /api/seed/demand/farmer/page`

**接口描述：** 分页查询农民需求列表

**业务逻辑：**
1. 根据当前用户行政区划和角色过滤数据
2. DA只能查看自己录入的数据
3. 各级审核人员可查看其管辖范围内的数据
4. 支持多条件组合查询
5. 使用QueryWrapper构建查询条件
6. 分页返回

**请求参数：**

```json
{
    "pageNum": "int, 必填, 当前页码",
    "pageSize": "int, 必填, 每页条数",
    "batchId": "string, 选填, 批次ID",
    "farmerName": "string, 选填, 农民姓名(模糊查询)",
    "farmerIdNumber": "string, 选填, 农民身份证号",
    "kebele": "string, 选填, Kebele",
    "woreda": "string, 选填, Woreda",
    "zone": "string, 选填, Zone",
    "village": "string, 选填, 村庄",
    "status": "string, 选填, 状态",
    "currentAuditLevel": "string, 选填, 当前审核层级",
    "inputCategory": "string, 选填, 投入品大类",
    "createdTimeStart": "date, 选填, 创建时间开始",
    "createdTimeEnd": "date, 选填, 创建时间结束"
}
```

**响应参数：**

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "records": [
            {
                "id": "string, 需求ID",
                "batchNo": "string, 批次编号",
                "farmerId": "string, 农民ID",
                "farmerName": "string, 农民姓名",
                "farmerIdNumber": "string, 农民身份证号",
                "kebele": "string, Kebele",
                "woreda": "string, Woreda",
                "village": "string, 村庄",
                "landArea": "number, 地块总面积",
                "status": "string, 状态",
                "statusName": "string, 状态名称",
                "currentAuditLevel": "string, 当前审核层级",
                "daUserName": "string, 录入DA姓名",
                "createdTime": "datetime, 创建时间"
            }
        ],
        "total": "long, 总记录数",
        "size": "long, 每页条数",
        "current": "long, 当前页码",
        "pages": "long, 总页数"
    }
}
```

---

#### 4.2.5 删除农民需求

**接口路径：** `POST /api/seed/demand/farmer/delete`

**接口描述：** 删除草稿状态的农民需求

**业务逻辑：**
1. 校验需求ID存在
2. 校验状态为draft
3. 校验当前用户为录入的DA
4. 逻辑删除需求及关联的投入品明细

**请求参数：**

```json
{
    "id": "string, 必填, 需求ID"
}
```

**响应参数：**

```json
{
    "code": 200,
    "msg": "删除成功",
    "data": null
}
```

---

#### 4.2.6 批量导入农民需求

**接口路径：** `POST /api/seed/demand/farmer/import`

**接口描述：** 批量导入农民需求数据

**业务逻辑：**
1. 解析Excel文件
2. 校验模板格式正确性
3. 校验批次ID存在且状态为collecting
4. 逐行校验数据有效性
5. 记录校验失败的行及原因
6. 校验通过的数据批量保存
7. 返回导入结果统计

**请求参数：**

| 参数名     | 类型     | 必填   | 说明      |
| ------- | ------ | ---- | ------- |
| batchId | string | 是    | 批次ID    |
| file    | file   | 是    | Excel文件 |

**响应参数：**

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "totalCount": "int, 总行数",
        "successCount": "int, 成功数",
        "failCount": "int, 失败数",
        "failDetails": [
            {
                "rowNum": "int, 行号",
                "reason": "string, 失败原因"
            }
        ]
    }
}
```

---

#### 4.2.7 下载导入模板

**接口路径：** `GET /api/seed/demand/farmer/template`

**接口描述：** 下载农民需求导入Excel模板

**业务逻辑：**
1. 生成标准Excel模板
2. 包含表头及示例数据
3. 返回文件流

**请求参数：** 无

**响应：** 文件下载

---

### 4.3 需求审核接口

#### 4.3.1 提交审核（DA提交村级）

**接口路径：** `POST /api/seed/demand/audit/submit`

**接口描述：** DA将农民需求提交至村级审核

**业务逻辑：**
1. 校验需求ID存在且状态为draft
2. 校验当前用户为录入的DA
3. 更新状态为submitted
4. 设置当前审核层级为village
5. 记录提交时间
6. 创建审核记录

**请求参数：**

```json
{
    "ids": ["string, 必填, 需求ID数组"]
}
```

**响应参数：**

```json
{
    "code": 200,
    "msg": "提交成功",
    "data": {
        "successCount": "int, 成功数",
        "failCount": "int, 失败数"
    }
}
```

---

#### 4.3.2 待审核需求列表

**接口路径：** `POST /api/seed/demand/audit/pending/page`

**接口描述：** 分页查询当前用户待审核的需求列表

**业务逻辑：**
1. 根据当前用户角色确定审核层级
2. 根据用户行政区划过滤数据
3. 筛选当前审核层级匹配的submitted状态数据
4. 使用QueryWrapper构建查询条件
5. 分页返回

**请求参数：**

```json
{
    "pageNum": "int, 必填, 当前页码",
    "pageSize": "int, 必填, 每页条数",
    "batchId": "string, 选填, 批次ID",
    "farmerName": "string, 选填, 农民姓名",
    "kebele": "string, 选填, Kebele",
    "woreda": "string, 选填, Woreda",
    "village": "string, 选填, 村庄"
}
```

**响应参数：**

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "records": [
            {
                "id": "string, 需求ID",
                "batchNo": "string, 批次编号",
                "farmerName": "string, 农民姓名",
                "farmerIdNumber": "string, 农民身份证号",
                "kebele": "string, Kebele",
                "woreda": "string, Woreda",
                "village": "string, 村庄",
                "landArea": "number, 地块总面积",
                "currentAuditLevel": "string, 当前审核层级",
                "submitTime": "datetime, 提交时间"
            }
        ],
        "total": "long, 总记录数",
        "size": "long, 每页条数",
        "current": "long, 当前页码",
        "pages": "long, 总页数"
    }
}
```

---

#### 4.3.3 审核通过（逐级上报）

**接口路径：** `POST /api/seed/demand/audit/approve`

**接口描述：** 审核通过并提交至上一级审核

**业务逻辑：**
1. 校验需求ID存在且状态为submitted
2. 校验当前审核层级与用户角色匹配
3. 确定下一审核层级：
   - village → town
   - town → district
   - district → state
   - state → ministry
4. 更新当前审核层级为下一层级
5. 农业部审核通过则更新状态为approved
6. 创建审核记录
7. 支持批量审核

**请求参数：**

```json
{
    "ids": ["string, 必填, 需求ID数组"],
    "remark": "string, 选填, 审核备注"
}
```

**响应参数：**

```json
{
    "code": 200,
    "msg": "审核通过",
    "data": {
        "successCount": "int, 成功数",
        "failCount": "int, 失败数"
    }
}
```

---

#### 4.3.4 审核驳回

**接口路径：** `POST /api/seed/demand/audit/reject`

**接口描述：** 驳回需求至DA重新修改

**业务逻辑：**
1. 校验需求ID存在且状态为submitted
2. 校验当前审核层级与用户角色匹配
3. 校验审核意见必填
4. 更新状态为rejected
5. 清空当前审核层级
6. 创建审核记录
7. 支持批量驳回

**请求参数：**

```json
{
    "ids": ["string, 必填, 需求ID数组"],
    "auditOpinion": "string, 必填, 审核意见",
    "remark": "string, 选填, 审核备注"
}
```

**响应参数：**

```json
{
    "code": 200,
    "msg": "已驳回",
    "data": {
        "successCount": "int, 成功数",
        "failCount": "int, 失败数"
    }
}
```

---

#### 4.3.5 需求锁定（农业部最终审批）

**接口路径：** `POST /api/seed/demand/audit/lock`

**接口描述：** 农业部最终审批，锁定需求数据

**业务逻辑：**
1. 校验当前用户为农业部角色
2. 校验批次ID存在且状态为reviewing
3. 校验该批次下所有需求已通过审核
4. 更新批次状态为locked
5. 更新所有需求状态为locked
6. 锁定后数据不可修改

**请求参数：**

```json
{
    "batchId": "string, 必填, 批次ID",
    "version": "int, 必填, 批次版本号"
}
```

**响应参数：**

```json
{
    "code": 200,
    "msg": "需求已锁定",
    "data": null
}
```

---

### 4.4 需求汇总查询接口

#### 4.4.1 需求汇总统计

**接口路径：** `POST /api/seed/demand/summary/statistics`

**接口描述：** 按行政层级汇总统计需求数据

**业务逻辑：**
1. 根据批次ID和行政层级查询
2. 按行政区划分组汇总
3. 统计各类投入品总量
4. 统计农民数量
5. 根据用户权限过滤数据范围

**请求参数：**

```json
{
    "batchId": "string, 必填, 批次ID",
    "adminLevel": "string, 必填, 行政层级(kebele/woreda/zone/region)",
    "parentAdminCode": "string, 选填, 上级行政区划代码",
    "inputCategory": "string, 选填, 投入品大类"
}
```

**响应参数：**

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": [
        {
            "adminCode": "string, 行政区划代码",
            "adminName": "string, 行政区划名称",
            "farmerCount": "int, 农民数量",
            "seedTotalQuantity": "number, 种子总量",
            "fertilizerTotalQuantity": "number, 化肥总量",
            "pesticideTotalQuantity": "number, 农药总量",
            "items": [
                {
                    "inputCategory": "string, 投入品大类",
                    "inputType": "string, 农资类型",
                    "variety": "string, 品种",
                    "totalQuantity": "number, 总量",
                    "unit": "string, 单位"
                }
            ]
        }
    ]
}
```

---

#### 4.4.2 需求汇总明细分页

**接口路径：** `POST /api/seed/demand/summary/detail/page`

**接口描述：** 查看汇总下的农民需求明细

**业务逻辑：**
1. 根据汇总条件查询农民需求明细
2. 支持多条件筛选
3. 分页返回

**请求参数：**

```json
{
    "pageNum": "int, 必填, 当前页码",
    "pageSize": "int, 必填, 每页条数",
    "batchId": "string, 必填, 批次ID",
    "adminLevel": "string, 必填, 行政层级",
    "adminCode": "string, 必填, 行政区划代码",
    "inputCategory": "string, 选填, 投入品大类",
    "farmerName": "string, 选填, 农民姓名"
}
```

**响应参数：**

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "records": [
            {
                "id": "string, 需求ID",
                "farmerName": "string, 农民姓名",
                "farmerIdNumber": "string, 农民身份证号",
                "village": "string, 村庄",
                "landArea": "number, 地块总面积",
                "inputItems": [
                    {
                        "inputCategory": "string, 投入品大类",
                        "inputType": "string, 农资类型",
                        "variety": "string, 品种",
                        "quantity": "number, 数量",
                        "unit": "string, 单位"
                    }
                ]
            }
        ],
        "total": "long, 总记录数",
        "size": "long, 每页条数",
        "current": "long, 当前页码",
        "pages": "long, 总页数"
    }
}
```

---

#### 4.4.3 需求汇总导出

**接口路径：** `POST /api/seed/demand/summary/export`

**接口描述：** 导出需求汇总数据为Excel

**业务逻辑：**
1. 根据查询条件汇总数据
2. 生成Excel文件
3. 返回文件流

**请求参数：**

```json
{
    "batchId": "string, 必填, 批次ID",
    "adminLevel": "string, 必填, 行政层级",
    "parentAdminCode": "string, 选填, 上级行政区划代码",
    "inputCategory": "string, 选填, 投入品大类"
}
```

**响应：** 文件下载

---

## 五、常量定义

### 5.1 机构类型 (OrgTypeEnum)

| 枚举值         | 说明   |
| ----------- | ---- |
| union       | 联合社  |
| cooperative | 合作社  |

### 5.2 申请状态 (ApplicationStatusEnum)

| 枚举值      | 说明   |
| -------- | ---- |
| draft    | 草稿   |
| pending  | 待审核  |
| approved | 已通过  |
| rejected | 已驳回  |

### 5.3 批次状态 (BatchStatusEnum)

| 枚举值        | 说明   |
| ---------- | ---- |
| collecting | 采集中  |
| reviewing  | 审核中  |
| completed  | 已完成  |
| locked     | 已锁定  |

### 5.4 需求状态 (DemandStatusEnum)

| 枚举值       | 说明   |
| --------- | ---- |
| draft     | 草稿   |
| submitted | 已提交  |
| approved  | 已通过  |
| rejected  | 已驳回  |
| locked    | 已锁定  |

### 5.5 审核层级 (AuditLevelEnum)

| 枚举值      | 说明   |
| -------- | ---- |
| village  | 村级   |
| town     | 镇级   |
| district | 区级   |
| state    | 州级   |
| ministry | 农业部  |

### 5.6 投入品大类 (InputCategoryEnum)

| 枚举值        | 说明   |
| ---------- | ---- |
| seed       | 种子   |
| fertilizer | 化肥   |
| pesticide  | 农药   |

### 5.7 证件类型 (LicenseTypeEnum)

| 枚举值              | 说明    |
| ---------------- | ----- |
| business_license | 营业执照  |
| seed_license     | 种子许可证 |
| tax_certificate  | 税务登记证 |
| factory_permit   | 工厂许可证 |

### 5.8 审核动作 (AuditActionEnum)

| 枚举值     | 说明   |
| ------- | ---- |
| submit  | 提交   |
| approve | 通过   |
| reject  | 驳回   |

### 5.9 审核结果 (AuditResultEnum)

| 枚举值      | 说明   |
| -------- | ---- |
| passed   | 通过   |
| rejected | 驳回   |

---

## 六、代码结构说明

### 6.1 包结构

```
igdp-seed
└── src
    └── main
        └── java
            └── com
                └── inspur
                    └── seed
                        ├── domain
                        │   ├── dto
                        │   │   ├── registration
                        │   │   │   ├── EnterpriseAddDTO.java
                        │   │   │   ├── EnterpriseUpdateDTO.java
                        │   │   │   ├── EnterpriseSubmitDTO.java
                        │   │   │   ├── EnterprisePageDTO.java
                        │   │   │   ├── AuditApproveDTO.java
                        │   │   │   └── AuditRejectDTO.java
                        │   │   └── demand
                        │   │       ├── BatchAddDTO.java
                        │   │       ├── BatchUpdateDTO.java
                        │   │       ├── BatchPageDTO.java
                        │   │       ├── FarmerDemandAddDTO.java
                        │   │       ├── FarmerDemandUpdateDTO.java
                        │   │       ├── FarmerDemandPageDTO.java
                        │   │       ├── DemandAuditSubmitDTO.java
                        │   │       ├── DemandAuditApproveDTO.java
                        │   │       ├── DemandAuditRejectDTO.java
                        │   │       ├── DemandLockDTO.java
                        │   │       └── SummaryQueryDTO.java
                        │   ├── entity
                        │   │   ├── OrgEnterpriseInfo.java
                        │   │   ├── OrgEnterpriseLocation.java
                        │   │   ├── OrgEnterpriseLicense.java
                        │   │   ├── OrgRegistrationAudit.java
                        │   │   ├── DemandCollectionBatch.java
                        │   │   ├── DemandFarmerDetail.java
                        │   │   ├── DemandFarmerInputItem.java
                        │   │   ├── DemandSummary.java
                        │   │   └── DemandAuditRecord.java
                        │   └── vo
                        │       ├── registration
                        │       │   ├── EnterpriseDetailVO.java
                        │       │   ├── EnterprisePageVO.java
                        │       │   ├── LocationVO.java
                        │       │   ├── LicenseVO.java
                        │       │   └── AuditRecordVO.java
                        │       └── demand
                        │           ├── BatchDetailVO.java
                        │           ├── BatchPageVO.java
                        │           ├── FarmerDemandDetailVO.java
                        │           ├── FarmerDemandPageVO.java
                        │           ├── InputItemVO.java
                        │           ├── SummaryStatisticsVO.java
                        │           └── ImportResultVO.java
                        ├── constant
                        │   ├── OrgTypeEnum.java
                        │   ├── ApplicationStatusEnum.java
                        │   ├── BatchStatusEnum.java
                        │   ├── DemandStatusEnum.java
                        │   ├── AuditLevelEnum.java
                        │   ├── InputCategoryEnum.java
                        │   ├── LicenseTypeEnum.java
                        │   ├── AuditActionEnum.java
                        │   └── AuditResultEnum.java
                        ├── controller
                        │   ├── RegistrationController.java
                        │   ├── RegistrationAuditController.java
                        │   ├── DemandBatchController.java
                        │   ├── FarmerDemandController.java
                        │   ├── DemandAuditController.java
                        │   └── DemandSummaryController.java
                        ├── service
                        │   ├── IRegistrationService.java
                        │   ├── IRegistrationAuditService.java
                        │   ├── IDemandBatchService.java
                        │   ├── IFarmerDemandService.java
                        │   ├── IDemandAuditService.java
                        │   ├── IDemandSummaryService.java
                        │   └── impl
                        │       ├── RegistrationServiceImpl.java
                        │       ├── RegistrationAuditServiceImpl.java
                        │       ├── DemandBatchServiceImpl.java
                        │       ├── FarmerDemandServiceImpl.java
                        │       ├── DemandAuditServiceImpl.java
                        │       └── DemandSummaryServiceImpl.java
                        ├── mapper
                        │   ├── OrgEnterpriseInfoMapper.java
                        │   ├── OrgEnterpriseLocationMapper.java
                        │   ├── OrgEnterpriseLicenseMapper.java
                        │   ├── OrgRegistrationAuditMapper.java
                        │   ├── DemandCollectionBatchMapper.java
                        │   ├── DemandFarmerDetailMapper.java
                        │   ├── DemandFarmerInputItemMapper.java
                        │   ├── DemandSummaryMapper.java
                        │   └── DemandAuditRecordMapper.java
                        └── utils
                            ├── UuidUtil.java
                            └── ExcelUtil.java
```

### 6.2 Entity类设计要点

1. 使用`@TableName`注解映射表名
2. 使用`@TableId(type = IdType.ASSIGN_UUID)`配置主键策略
3. 使用`@TableField`注解映射字段名
4. 使用`@TableLogic`注解配置逻辑删除
5. 使用`@Version`注解配置乐观锁字段
6. 使用Lombok的`@Data`、`@Builder`、`@NoArgsConstructor`、`@AllArgsConstructor`注解

### 6.3 Mapper接口设计要点

1. 继承`BaseMapper<T>`接口
2. 使用`@Mapper`注解标识
3. 复杂查询使用XML配置SQL

### 6.4 Service实现设计要点

1. 接口继承`IService<T>`
2. 实现类继承`ServiceImpl<M, T>`
3. 使用`@Transactional`注解管理事务
4. 使用QueryWrapper/LambdaQueryWrapper构建查询条件

### 6.5 Controller设计要点

1. 使用`@RestController`和`@RequestMapping`注解
2. 返回类型统一使用`AjaxResult`
3. 分页查询使用`IPage`接收分页参数
4. 使用`@PostMapping`和`@GetMapping`注解

---

## 七、开发注意事项

### 7.1 数据权限控制

1. 所有查询接口需根据当前用户的行政区划过滤数据
2. Zone用户只能操作Union类型机构
3. Woreda用户只能操作Cooperative类型机构
4. 各级审核人员只能审核其管辖范围内的数据

### 7.2 并发控制

1. 更新操作使用乐观锁（version字段）
2. 批量操作需考虑事务回滚
3. 状态变更需校验当前状态

### 7.3 数据完整性

1. 逻辑删除使用is_deleted字段
2. 主表删除时需同步删除关联子表数据
3. 外键关联需校验数据存在性

### 7.4 日志记录

1. 关键业务操作需记录操作日志
2. 审核操作需保存完整审核记录
3. 异常情况需记录详细错误信息