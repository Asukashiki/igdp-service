# 出库接口

## 基本信息
- **接口路径**: `POST /inventory/open/outbound`
- **所属模块**: igdp-inventory
- **Controller**: `OpenInventoryController.createOutbound`
- **鉴权**: Sa-Token（`Authorization: Bearer <token>`）

## 请求参数
### Body 参数（JSON）
> 接口接收 `InventoryOutbound` 作为入参，其中 `detailList` 为明细列表。

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| outboundNo | string | 否 | 出库单号（不传可由外系统生成；系统不会自动生成） |
| warehouseCode | string | 是 | 仓库编码 |
| type | string | 否 | 出库类型（GENERAL/TRANSFER） |
| receiverType | string | 否 | 接收方类型（FARMER/COOP/OTHER） |
| receiver | string | 否 | 接收方 |
| bizNo | string | 否 | 关联业务单号 |
| operator | string | 否 | 操作人 |
| orderDate | string | 否 | 出库时间（yyyy-MM-dd HH:mm:ss） |
| remark | string | 否 | 备注 |
| detailList | array | 是 | 出库明细列表 |

### detailList 字段（InventoryOutboundDetail）
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

## 响应参数
### 返回结构
| 字段 | 类型 | 说明 |
| --- | --- | --- |
| code | number | 状态码（200 表示成功） |
| msg | string | 提示信息 |
| data | number | 出库单 ID |

## 请求示例
```http
POST /inventory/open/outbound HTTP/1.1
Host: 127.0.0.1:9702
Authorization: Bearer 932e69e1-d3ed-4a4d-8bf8-7156092a3aca
Content-Type: application/json

{
  "outboundNo": "OUT202603120001",
  "warehouseCode": "WH001",
  "type": "GENERAL",
  "receiverType": "FARMER",
  "receiver": "Farmer Group A",
  "operator": "external-system",
  "orderDate": "2026-03-12 16:00:00",
  "remark": "external outbound",
  "detailList": [
    {
      "productId": 1,
      "mainCategory": "Fertilizer",
      "subCategory": "Nitrogen Fertilizer",
      "batchNo": "BATCH-2026-001",
      "qty": 1,
      "unit": "KG"
    }
  ]
}
```

## 响应示例
```json
{
  "code": 200,
  "msg": "Outbound order created.",
  "data": 201
}
```
