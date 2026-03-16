# 入库接口

## 基本信息
- **接口路径**: `POST /inventory/open/inbound`
- **所属模块**: igdp-inventory
- **Controller**: `OpenInventoryController.createInbound`
- **鉴权**: Sa-Token（`Authorization: Bearer <token>`）

## 请求参数
### Body 参数（JSON）
> 接口接收 `InventoryInbound` 作为入参，其中 `detailList` 为明细列表。

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| inboundNo | string | 否 | 入库单号（不传可由外系统生成；系统不会自动生成） |
| warehouseCode | string | 是 | 仓库编码 |
| type | string | 否 | 入库类型（GENERAL/TRANSFER） |
| bizNo | string | 否 | 关联业务单号 |
| operator | string | 否 | 操作人 |
| orderDate | string | 否 | 入库时间（yyyy-MM-dd HH:mm:ss） |
| remark | string | 否 | 备注 |
| detailList | array | 是 | 入库明细列表 |

### detailList 字段（InventoryInboundDetail）
| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| productId | number | 是 | 商品 ID |
| mainCategory | string | 否 | 商品大类 |
| subCategory | string | 否 | 商品小类 |
| batchNo | string | 否 | 批次号 |
| supplier | string | 否 | 供应商 |
| qty | number | 是 | 数量（原始单位） |
| unit | string | 是 | 单位（KG/g/ML） |
| expireDate | string | 否 | 过期时间（yyyy-MM-dd） |
| qualityGrade | string | 否 | 质量等级（业务透传字段） |
| stockStatus | string | 否 | 库存状态（业务透传字段） |

## 响应参数
### 返回结构
| 字段 | 类型 | 说明 |
| --- | --- | --- |
| code | number | 状态码（200 表示成功） |
| msg | string | 提示信息 |
| data | number | 入库单 ID |

## 请求示例
```http
POST /inventory/open/inbound HTTP/1.1
Host: 127.0.0.1:9702
Authorization: Bearer 932e69e1-d3ed-4a4d-8bf8-7156092a3aca
Content-Type: application/json

{
  "inboundNo": "IN202603120001",
  "warehouseCode": "WH001",
  "type": "GENERAL",
  "operator": "external-system",
  "orderDate": "2026-03-12 15:30:00",
  "remark": "external inbound",
  "detailList": [
    {
      "productId": 1,
      "mainCategory": "Fertilizer",
      "subCategory": "Nitrogen Fertilizer",
      "batchNo": "BATCH-2026-001",
      "qty": 1000,
      "unit": "g",
      "expireDate": "2027-03-12"
    }
  ]
}
```

## 响应示例
```json
{
  "code": 200,
  "msg": "Inbound order created.",
  "data": 101
}
```
