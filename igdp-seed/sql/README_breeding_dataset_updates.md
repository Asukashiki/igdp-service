# 育种数据集模块 - 数据库更新说明

## 数据库分析结果

### 数据库类型
**MySQL** (InnoDB引擎，utf8mb4字符集)

### 表名确认
- 主表：`breeding_dataset` (育种数据集表)
- 审核表：`breeding_dataset_audit` (育种数据集审核记录表)

## 新增字段说明

### 一、breeding_dataset 表新增字段

为支持数据集编制功能，需要添加以下6个字段：

| 字段名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| trial_id | VARCHAR(50) | NULL | 试验ID，关联的试验记录 |
| version_no | VARCHAR(20) | '1.0' | 版本号，支持数据集版本管理 |
| compiled_by | VARCHAR(36) | NULL | 编制人ID |
| compiled_by_name | VARCHAR(50) | NULL | 编制人姓名(冗余字段) |
| compiled_at | DATETIME | NULL | 编制时间 |
| record_count | INT | 0 | 记录数量总计 |

**新增索引：**
- `idx_trial_id` - 试验ID索引
- `idx_compiled_by` - 编制人索引
- `idx_compiled_at` - 编制时间索引

### 二、breeding_dataset_audit 表新增字段

为保存审核时的数据集快照信息，需要添加以下16个字段：

| 字段名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| dataset_code | VARCHAR(50) | NULL | 数据集编号(冗余) |
| batch_name | VARCHAR(100) | NULL | 育种批次名称(冗余) |
| crop_type | VARCHAR(50) | NULL | 作物类型(快照) |
| variety_name | VARCHAR(100) | NULL | 品种名称(快照) |
| trial_id | VARCHAR(50) | NULL | 试验ID(快照) |
| version_no | VARCHAR(20) | '1.0' | 版本号(快照) |
| compiled_by | VARCHAR(36) | NULL | 编制人ID(快照) |
| compiled_by_name | VARCHAR(50) | NULL | 编制人姓名(快照) |
| compiled_at | DATETIME | NULL | 编制时间(快照) |
| record_count | INT | 0 | 记录数量(快照) |
| dataset_status | VARCHAR(20) | NULL | 数据集状态(快照) |
| trial_count | INT | 0 | 试验记录数(快照) |
| field_data_count | INT | 0 | 田间数据记录数(快照) |
| env_data_count | INT | 0 | 环境数据记录数(快照) |
| lab_test_count | INT | 0 | 实验室检测记录数(快照) |
| yield_data_count | INT | 0 | 产量数据记录数(快照) |

**新增索引：**
- `idx_dataset_code` - 数据集编号索引
- `idx_trial_id` - 试验ID索引
- `idx_version_no` - 版本号索引
- `idx_compiled_by` - 编制人索引

## 执行脚本

### 生成的SQL脚本文件

1. **breeding_dataset_add_compilation_fields.sql**
   - 为 `breeding_dataset` 表添加编制相关字段
   - 添加3个性能优化索引

2. **breeding_dataset_audit_add_fields.sql**
   - 为 `breeding_dataset_audit` 表添加数据集快照字段
   - 添加4个性能优化索引

### 执行顺序

```bash
# 1. 连接MySQL数据库
mysql -u root -p

# 2. 选择数据库
USE igdp_seed;

# 3. 执行 breeding_dataset 表更新
SOURCE /path/to/breeding_dataset_add_compilation_fields.sql;

# 4. 执行 breeding_dataset_audit 表更新
SOURCE /path/to/breeding_dataset_audit_add_fields.sql;

# 5. 验证字段添加成功
SHOW COLUMNS FROM breeding_dataset LIKE '%compil%';
SHOW COLUMNS FROM breeding_dataset_audit LIKE '%compil%';
```

### Windows 环境执行

```cmd
REM 进入SQL脚本目录
cd E:\work\companyProject\igdp-service\igdp-seed\sql

REM 执行SQL脚本（根据实际MySQL配置修改参数）
mysql -u root -p igdp_seed < breeding_dataset_add_compilation_fields.sql
mysql -u root -p igdp_seed < breeding_dataset_audit_add_fields.sql
```

## 注意事项

1. **备份数据库**：执行前请先备份 `igdp_seed` 数据库
   ```sql
   mysqldump -u root -p igdp_seed > igdp_seed_backup_20251205.sql
   ```

2. **检查数据库名称**：确认数据库名称是否为 `igdp_seed`，如不同需修改脚本中的 `USE` 语句

3. **字段冲突检查**：如果字段已存在，需先检查现有字段定义是否一致
   ```sql
   SHOW COLUMNS FROM breeding_dataset;
   SHOW COLUMNS FROM breeding_dataset_audit;
   ```

4. **索引冲突**：如果索引名称已存在，需先删除或使用不同名称
   ```sql
   SHOW INDEX FROM breeding_dataset;
   SHOW INDEX FROM breeding_dataset_audit;
   ```

5. **权限要求**：执行 ALTER TABLE 需要 ALTER 权限

6. **生产环境**：在生产环境执行前，建议先在测试环境验证

## 回滚脚本

如需回滚更改，可使用以下SQL：

```sql
-- 回滚 breeding_dataset 表
ALTER TABLE breeding_dataset
DROP COLUMN trial_id,
DROP COLUMN version_no,
DROP COLUMN compiled_by,
DROP COLUMN compiled_by_name,
DROP COLUMN compiled_at,
DROP COLUMN record_count;

DROP INDEX idx_trial_id ON breeding_dataset;
DROP INDEX idx_compiled_by ON breeding_dataset;
DROP INDEX idx_compiled_at ON breeding_dataset;

-- 回滚 breeding_dataset_audit 表
ALTER TABLE breeding_dataset_audit
DROP COLUMN dataset_code,
DROP COLUMN batch_name,
DROP COLUMN crop_type,
DROP COLUMN variety_name,
DROP COLUMN trial_id,
DROP COLUMN version_no,
DROP COLUMN compiled_by,
DROP COLUMN compiled_by_name,
DROP COLUMN compiled_at,
DROP COLUMN record_count,
DROP COLUMN dataset_status,
DROP COLUMN trial_count,
DROP COLUMN field_data_count,
DROP COLUMN env_data_count,
DROP COLUMN lab_test_count,
DROP COLUMN yield_data_count;

DROP INDEX idx_dataset_code ON breeding_dataset_audit;
DROP INDEX idx_trial_id ON breeding_dataset_audit;
DROP INDEX idx_version_no ON breeding_dataset_audit;
DROP INDEX idx_compiled_by ON breeding_dataset_audit;
```

## 相关文件

- **后端实体类**：
  - `BreedingDataset.java` - 数据集实体类
  - `BreedingDatasetAuditVO.java` - 审核VO类

- **前端页面**：
  - `dataset-compilation/index.vue` - 数据集编制列表
  - `dataset-compilation/form.vue` - 数据集编制表单
  - `dataset-audit/index.vue` - 数据集审核列表
  - `dataset-audit/review.vue` - 数据集审核详情

- **国际化文件**：
  - `locales/zh-CN/research/datasetCompilation.js`
  - `locales/zh-CN/research/datasetAudit.js`
  - `locales/en-US/research/datasetCompilation.js`
  - `locales/en-US/research/datasetAudit.js`

## 更新日期

2025-12-05

## 作者

系统生成
