# 环境数据分页查询使用说明

## 分页功能说明

环境数据模块已经集成了 **PageHelper** 分页插件，支持完整的分页查询功能。

## 分页查询接口

### 接口地址
```
GET /seed/environment/list
```

### 分页参数说明

分页参数通过 URL 查询字符串传递，PageHelper 会自动识别以下参数：

| 参数名 | 类型 | 必填 | 说明 | 默认值 |
|--------|------|------|------|--------|
| pageNum | Integer | 否 | 当前页码（从1开始） | 1 |
| pageSize | Integer | 否 | 每页显示条数 | 10 |

### 业务查询参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| trialId | String | 否 | 试验ID |
| batchId | String | 否 | 批次ID |
| plotId | String | 否 | 地块ID |
| stationId | String | 否 | 气象站ID |
| parameterCode | String | 否 | 参数代码 |
| dataSource | String | 否 | 数据来源 |
| queryStartTime | String | 否 | 查询起始时间 |
| queryEndTime | String | 否 | 查询结束时间 |

## 返回数据格式

### 响应结构
```json
{
  "total": 100,           // 总记录数
  "rows": [...],          // 当前页数据列表
  "code": 200,            // 状态码
  "msg": "查询成功"       // 提示信息
}
```

### 完整响应示例
```json
{
  "total": 100,
  "rows": [
    {
      "envRecordId": "9001",
      "trialId": "WHT-TR-ARSI-2025-01",
      "trialName": "小麦抗旱试验",
      "batchId": "BRD-WHT-2025-001",
      "batchName": "2025年春季小麦育种批次",
      "plotId": "ARSI-R1-P01",
      "plotName": "1号试验田",
      "stationId": "AWS-ARSI",
      "timestamp": "2025-08-10 12:00:00",
      "parameterCode": "RAIN_DAILY",
      "parameterName": "日降雨量",
      "value": 8.5,
      "unit": "mm",
      "dataSource": "IOT_SYSTEM",
      "createTime": "2025-08-10 13:00:00"
    },
    {
      "envRecordId": "9002",
      "trialId": "WHT-TR-ARSI-2025-01",
      "trialName": "小麦抗旱试验",
      "batchId": "BRD-WHT-2025-001",
      "batchName": "2025年春季小麦育种批次",
      "plotId": "ARSI-R1-P01",
      "plotName": "1号试验田",
      "stationId": "AWS-ARSI",
      "timestamp": "2025-08-10 14:00:00",
      "parameterCode": "TMAX",
      "parameterName": "最高温度",
      "value": 32.5,
      "unit": "°C",
      "dataSource": "IOT_SYSTEM",
      "createTime": "2025-08-10 15:00:00"
    }
  ],
  "code": 200,
  "msg": "查询成功"
}
```

## 使用示例

### 示例1: 基础分页查询
查询第1页，每页10条数据

```bash
GET /seed/environment/list?pageNum=1&pageSize=10
```

### 示例2: 查询指定试验的环境数据（分页）
查询指定试验的数据，第1页，每页20条

```bash
GET /seed/environment/list?trialId=WHT-TR-ARSI-2025-01&pageNum=1&pageSize=20
```

### 示例3: 按参数代码查询（分页）
查询降雨量数据，第2页，每页15条

```bash
GET /seed/environment/list?parameterCode=RAIN_DAILY&pageNum=2&pageSize=15
```

### 示例4: 时间范围查询（分页）
查询指定时间段的数据

```bash
GET /seed/environment/list?queryStartTime=2025-08-01&queryEndTime=2025-08-31&pageNum=1&pageSize=50
```

### 示例5: 多条件组合查询（分页）
查询指定试验、指定参数、指定时间段的数据

```bash
GET /seed/environment/list?trialId=WHT-TR-ARSI-2025-01&parameterCode=TMAX&queryStartTime=2025-08-01&queryEndTime=2025-08-31&pageNum=1&pageSize=20
```

### 示例6: 按数据来源查询（分页）
只查询IoT系统上传的数据

```bash
GET /seed/environment/list?dataSource=IOT_SYSTEM&pageNum=1&pageSize=100
```

## 前端调用示例

### JavaScript (Axios)
```javascript
// 查询环境数据列表（分页）
async function getEnvironmentDataList(params) {
  const response = await axios.get('/seed/environment/list', {
    params: {
      pageNum: params.pageNum || 1,
      pageSize: params.pageSize || 10,
      trialId: params.trialId,
      batchId: params.batchId,
      parameterCode: params.parameterCode,
      queryStartTime: params.queryStartTime,
      queryEndTime: params.queryEndTime
    }
  });
  
  return {
    total: response.data.total,
    rows: response.data.rows
  };
}

// 使用示例
const result = await getEnvironmentDataList({
  pageNum: 1,
  pageSize: 20,
  trialId: 'WHT-TR-ARSI-2025-01',
  parameterCode: 'RAIN_DAILY'
});

console.log(`总共 ${result.total} 条数据`);
console.log(`当前页数据:`, result.rows);
```

### Vue.js 示例
```vue
<template>
  <div>
    <el-table :data="tableData">
      <el-table-column prop="timestamp" label="时间" />
      <el-table-column prop="parameterName" label="参数" />
      <el-table-column prop="value" label="数值" />
      <el-table-column prop="unit" label="单位" />
    </el-table>
    
    <el-pagination
      @current-change="handlePageChange"
      :current-page="pageNum"
      :page-size="pageSize"
      :total="total"
      layout="total, prev, pager, next, jumper"
    />
  </div>
</template>

<script>
export default {
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      queryParams: {
        trialId: '',
        parameterCode: ''
      }
    };
  },
  methods: {
    async fetchData() {
      const response = await this.$axios.get('/seed/environment/list', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          ...this.queryParams
        }
      });
      
      this.tableData = response.data.rows;
      this.total = response.data.total;
    },
    handlePageChange(page) {
      this.pageNum = page;
      this.fetchData();
    }
  },
  mounted() {
    this.fetchData();
  }
};
</script>
```

## 性能优化建议

### 1. 合理设置每页数量
- 推荐每页10-50条数据
- 不建议每页超过100条，避免影响查询性能

### 2. 使用索引字段进行查询
已建立索引的字段：
- `trial_id`
- `batch_id`
- `plot_id`
- `station_id`
- `parameter_code`
- `timestamp`

优先使用这些字段作为查询条件，可以提高查询效率。

### 3. 时间范围查询优化
```bash
# 推荐：使用具体的时间范围
GET /seed/environment/list?queryStartTime=2025-08-01&queryEndTime=2025-08-31

# 不推荐：不指定时间范围，查询全部数据
GET /seed/environment/list?pageNum=1&pageSize=1000
```

## 分页实现原理

### 技术栈
- **PageHelper**: MyBatis 分页插件
- **自动分页**: 通过 `startPage()` 方法自动拦截下一条查询并添加分页SQL
- **线程安全**: PageHelper 使用 ThreadLocal 确保线程安全

### 核心代码
```java
@GetMapping("/list")
public TableDataInfo list(EnvironmentData environmentData) {
    startPage();  // 启动分页，从请求参数中获取 pageNum 和 pageSize
    List<EnvironmentData> list = environmentDataService.selectEnvironmentDataList(environmentData);
    return getDataTable(list);  // 自动封装分页结果
}
```

### 生成的SQL示例
当调用 `/seed/environment/list?pageNum=2&pageSize=10` 时，PageHelper 会自动将查询SQL转换为：

```sql
SELECT * FROM environment_data 
WHERE del_flag = '0'
ORDER BY timestamp DESC, create_time DESC
LIMIT 10 OFFSET 10  -- 第2页，跳过前10条，取10条
```

## 常见问题

### Q1: 如何获取全部数据（不分页）？
A: 不传 `pageNum` 和 `pageSize` 参数，但不建议这样做，大量数据会影响性能。

### Q2: 分页参数放在哪里传递？
A: 分页参数通过 URL 查询字符串传递，例如：`?pageNum=1&pageSize=10`

### Q3: 返回数据中的 total 是什么？
A: `total` 是符合查询条件的总记录数（不受分页影响），用于前端展示总页数。

### Q4: 可以自定义排序吗？
A: 当前默认按 `timestamp DESC, create_time DESC` 排序（时间倒序）。如需自定义排序，可以在 Mapper XML 中修改 ORDER BY 子句。

### Q5: 为什么第一页是 pageNum=1 而不是 0？
A: PageHelper 的页码从 1 开始，这符合用户习惯。

## 扩展功能

如需添加自定义排序功能，可以修改 Controller：

```java
@GetMapping("/list")
public TableDataInfo list(EnvironmentData environmentData, 
                         @RequestParam(required = false) String orderBy) {
    startPage();
    // 可以根据 orderBy 参数动态调整排序
    List<EnvironmentData> list = environmentDataService.selectEnvironmentDataList(environmentData);
    return getDataTable(list);
}
```
