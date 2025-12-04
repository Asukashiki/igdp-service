-- =============================================
-- 农田管理模块数据库表结构
-- =============================================

-- ----------------------------
-- 1. DA信息表 (t_da_info)
-- ----------------------------
CREATE TABLE t_da_info (
    -- 主键
    id                      BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',

    -- 业务字段
    da_id                   VARCHAR(32)     NOT NULL COMMENT 'DA编码（业务主键）',
    da_name                 VARCHAR(100)    NOT NULL COMMENT 'DA姓名',
    id_card                 VARCHAR(20)     NOT NULL COMMENT '身份证号',
    gender                  CHAR(1)         NOT NULL COMMENT '性别：M-男 F-女',
    birthday                DATE            NULL COMMENT '出生日期',
    phone                   VARCHAR(20)     NOT NULL COMMENT '联系电话',
    email                   VARCHAR(100)    NULL COMMENT '邮箱',

    -- 行政区划
    region_code             VARCHAR(20)     NULL COMMENT '州代码',
    zone_code               VARCHAR(20)     NULL COMMENT '区代码',
    woreda_code             VARCHAR(20)     NOT NULL COMMENT '镇代码',
    kebele_codes            VARCHAR(500)    NOT NULL COMMENT '负责的村代码（多个用逗号分隔）',

    -- 地址信息
    address                 VARCHAR(255)    NULL COMMENT '详细地址',

    -- 账号信息
    account                 VARCHAR(50)     NOT NULL COMMENT '登录账号',
    password                VARCHAR(255)    NOT NULL COMMENT '登录密码（加密存储）',
    account_status          CHAR(1)         NOT NULL DEFAULT '1' COMMENT '账号状态：1-启用 0-禁用',

    -- 审计字段
    status                  CHAR(1)         NOT NULL DEFAULT '1' COMMENT '数据状态：1-正常 0-删除',
    create_by               VARCHAR(50)     NULL COMMENT '创建人',
    create_by_name          VARCHAR(100)    NULL COMMENT '创建人姓名',
    create_org              VARCHAR(50)     NULL COMMENT '创建机构代码',
    create_org_name         VARCHAR(100)    NULL COMMENT '创建机构名称',
    create_time             DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by               VARCHAR(50)     NULL COMMENT '更新人',
    update_by_name          VARCHAR(100)    NULL COMMENT '更新人姓名',
    update_time             DATETIME        NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark                  VARCHAR(500)    NULL COMMENT '备注',

    PRIMARY KEY (id),
    UNIQUE KEY uk_da_id (da_id),
    UNIQUE KEY uk_id_card (id_card),
    UNIQUE KEY uk_account (account),
    KEY idx_woreda_code (woreda_code),
    KEY idx_phone (phone),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='DA信息表';

-- ----------------------------
-- 2. 农民信息表 (t_farmer_info)
-- ----------------------------
CREATE TABLE t_farmer_info (
    -- 主键
    id                      BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',

    -- 业务字段
    farmer_id               VARCHAR(32)     NOT NULL COMMENT '农民编码（业务主键）',
    farmer_name             VARCHAR(100)    NOT NULL COMMENT '农民姓名',
    id_card                 VARCHAR(20)     NOT NULL COMMENT '身份证号/ID',
    gender                  CHAR(1)         NOT NULL COMMENT '性别：M-男 F-女',
    birthday                DATE            NULL COMMENT '出生日期',
    phone                   VARCHAR(20)     NULL COMMENT '手机号',
    email                   VARCHAR(100)    NULL COMMENT '邮箱',

    -- 分类信息
    youth_category          CHAR(1)         NULL COMMENT '青年类别：1-是 0-否',

    -- 所属组织
    union_id                VARCHAR(32)     NULL COMMENT '所属Union ID',
    union_name              VARCHAR(100)    NULL COMMENT '所属Union名称',
    cooperative_id          VARCHAR(32)     NULL COMMENT '所属Cooperative ID',
    cooperative_name        VARCHAR(100)    NULL COMMENT '所属Cooperative名称',

    -- 行政区划
    region_code             VARCHAR(20)     NULL COMMENT '州代码',
    region_name             VARCHAR(50)     NULL COMMENT '州名称',
    zone_code               VARCHAR(20)     NULL COMMENT '区代码',
    zone_name               VARCHAR(50)     NULL COMMENT '区名称',
    woreda_code             VARCHAR(20)     NULL COMMENT '镇代码',
    woreda_name             VARCHAR(50)     NULL COMMENT '镇名称',
    kebele_code             VARCHAR(20)     NOT NULL COMMENT '村代码',
    kebele_name             VARCHAR(50)     NULL COMMENT '村名称',

    -- 地址信息
    address                 VARCHAR(255)    NULL COMMENT '详细地址',

    -- 统计字段
    total_land_area         DECIMAL(12,2)   NULL DEFAULT 0 COMMENT '总土地面积（公顷）',
    land_count              INT             NULL DEFAULT 0 COMMENT '地块数量',

    -- 负责DA
    da_id                   VARCHAR(32)     NOT NULL COMMENT '负责DA编码',
    da_name                 VARCHAR(100)    NULL COMMENT '负责DA姓名',

    -- 审计字段
    status                  CHAR(1)         NOT NULL DEFAULT '1' COMMENT '数据状态：1-正常 0-删除',
    create_by               VARCHAR(50)     NULL COMMENT '创建人',
    create_by_name          VARCHAR(100)    NULL COMMENT '创建人姓名',
    create_org              VARCHAR(50)     NULL COMMENT '创建机构代码',
    create_org_name         VARCHAR(100)    NULL COMMENT '创建机构名称',
    create_time             DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by               VARCHAR(50)     NULL COMMENT '更新人',
    update_by_name          VARCHAR(100)    NULL COMMENT '更新人姓名',
    update_time             DATETIME        NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark                  VARCHAR(500)    NULL COMMENT '备注',

    PRIMARY KEY (id),
    UNIQUE KEY uk_farmer_id (farmer_id),
    UNIQUE KEY uk_id_card (id_card),
    KEY idx_kebele_code (kebele_code),
    KEY idx_da_id (da_id),
    KEY idx_phone (phone),
    KEY idx_farmer_name (farmer_name),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='农民信息表';

-- ----------------------------
-- 3. 土地信息表 (t_land_info)
-- ----------------------------
CREATE TABLE t_land_info (
    -- 主键
    id                      BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',

    -- 业务字段
    land_id                 VARCHAR(32)     NOT NULL COMMENT '土地编码（业务主键）',
    land_name               VARCHAR(100)    NOT NULL COMMENT '地块名称',
    land_no                 VARCHAR(50)     NULL COMMENT '地块编号',

    -- 权属信息
    owner_type              VARCHAR(50)     NOT NULL COMMENT '土地权属类型：COLLECTIVE-集体所有 CONTRACT-承包经营权 PRIVATE-私有',
    owner_name              VARCHAR(100)    NULL COMMENT '权属人/单位名称',
    owner_id_card           VARCHAR(20)     NULL COMMENT '权属人身份证号',

    -- 地块属性
    land_type               VARCHAR(50)     NOT NULL COMMENT '地块类型：PADDY-水田 DRY-旱地 GARDEN-园地 FOREST-林地 OTHER-其他',
    land_graphic            VARCHAR(50)     NULL COMMENT '地形：FLAT-平坦 GENTLE_SLOPE-缓坡 STEEP_SLOPE-陡坡',
    area_size               DECIMAL(12,2)   NOT NULL COMMENT '地块面积（公顷）',
    area_unit               VARCHAR(20)     NOT NULL DEFAULT 'HECTARE' COMMENT '面积单位：HECTARE-公顷 MU-亩 SQM-平方米',

    -- 地理信息
    latitude                DECIMAL(10,6)   NULL COMMENT '纬度',
    longitude               DECIMAL(10,6)   NULL COMMENT '经度',
    plot_boundary           TEXT            NULL COMMENT '地块边界坐标（GeoJSON格式）',

    -- 行政区划
    region_code             VARCHAR(20)     NULL COMMENT '州代码',
    region_name             VARCHAR(50)     NULL COMMENT '州名称',
    zone_code               VARCHAR(20)     NULL COMMENT '区代码',
    zone_name               VARCHAR(50)     NULL COMMENT '区名称',
    woreda_code             VARCHAR(20)     NULL COMMENT '镇代码',
    woreda_name             VARCHAR(50)     NULL COMMENT '镇名称',
    kebele_code             VARCHAR(20)     NOT NULL COMMENT '村代码',
    kebele_name             VARCHAR(50)     NULL COMMENT '村名称',

    -- 详细地址
    address                 VARCHAR(255)    NOT NULL COMMENT '详细地址',

    -- 关联农民
    farmer_id               VARCHAR(32)     NULL COMMENT '关联农民ID',
    farmer_name             VARCHAR(100)    NULL COMMENT '关联农民姓名',
    farmer_id_card          VARCHAR(20)     NULL COMMENT '关联农民身份证号',
    farmer_phone            VARCHAR(20)     NULL COMMENT '关联农民电话',

    -- 土地状态
    current_status          VARCHAR(50)     NOT NULL DEFAULT 'IDLE' COMMENT '当前状态：CULTIVATING-耕种中 IDLE-闲置 FALLOW-休耕',

    -- 估算信息（根据面积自动计算）
    max_seed_amount         DECIMAL(12,2)   NULL COMMENT '估算最大种子量（kg）',
    max_fertilizer_amount   DECIMAL(12,2)   NULL COMMENT '估算最大肥料量（kg）',

    -- 负责DA
    da_id                   VARCHAR(32)     NOT NULL COMMENT '负责DA编码',
    da_name                 VARCHAR(100)    NULL COMMENT '负责DA姓名',

    -- 审计字段
    status                  CHAR(1)         NOT NULL DEFAULT '1' COMMENT '数据状态：1-正常 0-删除',
    create_by               VARCHAR(50)     NULL COMMENT '创建人',
    create_by_name          VARCHAR(100)    NULL COMMENT '创建人姓名',
    create_org              VARCHAR(50)     NULL COMMENT '创建机构代码',
    create_org_name         VARCHAR(100)    NULL COMMENT '创建机构名称',
    create_time             DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by               VARCHAR(50)     NULL COMMENT '更新人',
    update_by_name          VARCHAR(100)    NULL COMMENT '更新人姓名',
    update_time             DATETIME        NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark                  VARCHAR(500)    NULL COMMENT '备注',

    PRIMARY KEY (id),
    UNIQUE KEY uk_land_id (land_id),
    KEY idx_farmer_id (farmer_id),
    KEY idx_kebele_code (kebele_code),
    KEY idx_da_id (da_id),
    KEY idx_land_type (land_type),
    KEY idx_current_status (current_status),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='土地信息表';
