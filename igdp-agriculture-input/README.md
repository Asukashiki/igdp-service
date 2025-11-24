# 农业投入品管理模块 (igdp-agriculture-input)

## 模块简介

农业投入品管理模块负责对农业生产过程中使用的投入品（农药、化肥、种子等）进行统一管理和维护，确保投入品信息的准确性、规范性和可追溯性。

## 技术栈

- Spring Boot 2.7.6
- MyBatis Plus
- Swagger 3.0.0
- PageHelper

## 功能特性

### 1. 投入品目录管理
- 支持多类型投入品管理（农药、化肥、种子、其他）
- 每种类型投入品具有独立的特性字段
- 统一的基础信息管理（名称、SKU、登记证号、生产企业等）

### 2. 分类特性管理
- **农药特性**：毒性等级、防治对象、施用方法、安全间隔期等
- **化肥特性**：养分含量、适用作物、施用时期、建议用量等
- **种子特性**：品种信息、纯度、净度、发芽率等

### 3. 数据查询与统计
- 支持按类型、名称、SKU、状态等多维度查询
- 分页查询
- 统计信息展示（各类型数量、状态分布等）

## 数据库设计

### 主要表结构

1. **agri_input** - 投入品基础信息表
2. **agri_pesticide_properties** - 农药特性表
3. **agri_fertilizer_properties** - 化肥特性表
4. **agri_seed_properties** - 种子特性表

详细的数据库脚本请参考：`sql/agri_input.sql`

## API接口文档

### 基础路径
```
/agriculture/input
```

### 接口列表

#### 1. 查询投入品列表（分页）
```
GET /agriculture/input/list

参数：
- inputName: 投入品名称（可选）
- type: 投入品类型（可选，pesticide/fertilizer/seed/other）
- registerCode: 登记批号（可选）
- inputSku: SKU编码（可选）
- status: 状态（可选，active/inactive）
- keyword: 关键词搜索（可选，搜索名称/登记证号/SKU）
- page: 页码（默认1）
- pageSize: 每页数量（默认10）

返回：
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "list": [...],
    "total": 100,
    "page": 1,
    "pageSize": 10
  }
}
```

#### 2. 获取投入品详情
```
GET /agriculture/input/{id}

返回：包含完整的投入品信息及对应的特性信息
```

#### 3. 新增投入品
```
POST /agriculture/input

请求体示例（农药）：
{
  "inputName": "高效氯氟氰菊酯乳油",
  "type": "pesticide",
  "inputSku": "PEST-2024-001",
  "trademark": "绿盾",
  "registerCode": "PD20240001",
  "productionLicense": "XK13-003-00123",
  "productionStandard": "GB 20684-2006",
  "producerName": "山东绿盾农药有限公司",
  "producerAddress": "山东省济南市历城区工业园区",
  "status": "active",
  "pesticideProperties": {
    "totalIngredientContent": "4.5%",
    "toxicityLevel": "低毒",
    "targetCrops": "水稻、小麦、玉米、果树",
    "controlTargets": "蚜虫、菜青虫、小菜蛾、红蜘蛛",
    "applicationMethod": "喷雾",
    "dosage": "30-50ml/亩",
    "dilutionRatio": "1500-2000倍",
    "safetyInterval": 7,
    "precautions": "不可在强风天气或雨前施药",
    "firstAid": "如误服，立即催吐并送医",
    "storageRequirements": "存放于阴凉干燥处"
  }
}
```

#### 4. 修改投入品
```
PUT /agriculture/input/{id}

请求体：同新增接口
```

#### 5. 删除投入品
```
DELETE /agriculture/input/{id}

说明：逻辑删除，将del_flag设置为'2'
```

#### 6. 批量删除投入品
```
DELETE /agriculture/input/batch

请求体：[1, 2, 3]（投入品ID数组）
```

#### 7. 获取统计信息
```
GET /agriculture/input/statistics

返回：
{
  "code": 200,
  "data": {
    "total": 100,
    "pesticide": 30,
    "fertilizer": 40,
    "seed": 25,
    "other": 5,
    "active": 95,
    "inactive": 5
  }
}
```

#### 8. 导出投入品数据
```
GET /agriculture/input/export

参数：
- inputName: 投入品名称（可选）
- type: 投入品类型（可选）
- keyword: 关键词搜索（可选）

返回：符合条件的所有投入品数据
```

## 投入品类型说明

### type 字段值
- `pesticide` - 农药
- `fertilizer` - 化肥
- `seed` - 种子
- `other` - 其他

### status 字段值
- `active` - 正常（启用）
- `inactive` - 停用

## 使用示例

### 新增农药示例
```java
AgriInput input = new AgriInput();
input.setInputName("高效氯氟氰菊酯乳油");
input.setType("pesticide");
input.setInputSku("PEST-2024-001");
// ... 设置其他基础字段

PesticideProperties properties = new PesticideProperties();
properties.setTotalIngredientContent("4.5%");
properties.setToxicityLevel("低毒");
// ... 设置其他农药特性字段

input.setPesticideProperties(properties);
agriInputService.insertInput(input);
```

### 查询投入品列表示例
```java
AgriInput query = new AgriInput();
query.setType("fertilizer");
query.setStatus("active");

PageHelper.startPage(1, 10);
List<AgriInput> list = agriInputService.selectInputList(query);
PageInfo<AgriInput> pageInfo = new PageInfo<>(list);
```

## 注意事项

1. **SKU唯一性**：每个投入品的SKU（input_sku）必须唯一，系统会自动进行校验
2. **类型一致性**：创建投入品时，type字段与对应的特性对象必须匹配（例如type="pesticide"时必须提供pesticideProperties）
3. **逻辑删除**：删除操作为逻辑删除，不会物理删除数据，del_flag='2'表示已删除
4. **外键约束**：特性表与主表之间存在外键约束，删除主记录时会级联删除对应的特性记录

## 部署说明

1. 执行SQL脚本创建数据库表：
```bash
mysql -u用户名 -p密码 数据库名 < sql/agri_input.sql
```

2. 确保application.yml中配置了正确的数据库连接信息

3. 重新编译打包项目：
```bash
mvn clean package -Dmaven.test.skip=true
```

4. 启动应用后，访问Swagger文档查看API：
```
http://服务器地址:端口/swagger-ui/index.html
```

## 模块结构

```
igdp-agriculture-input
├── src/main/java/com/inspur/agriculture/input
│   ├── controller          # 控制层
│   │   └── AgriInputController.java
│   ├── domain              # 实体类
│   │   ├── AgriInput.java
│   │   ├── PesticideProperties.java
│   │   ├── FertilizerProperties.java
│   │   └── SeedProperties.java
│   ├── mapper              # 数据访问层
│   │   ├── AgriInputMapper.java
│   │   ├── PesticidePropertiesMapper.java
│   │   ├── FertilizerPropertiesMapper.java
│   │   └── SeedPropertiesMapper.java
│   └── service             # 业务逻辑层
│       ├── IAgriInputService.java
│       └── impl
│           └── AgriInputServiceImpl.java
├── src/main/resources
│   └── mapper/agriculture
│       └── AgriInputMapper.xml
├── sql
│   └── agri_input.sql     # 数据库初始化脚本
├── pom.xml
└── README.md
```

## 开发者

- 模块开发：IGDP Team
- 创建时间：2024
- 版本：3.8.7
