package com.inspur.agriculture.input.service.inventory;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 批次管理服务接口
 *
 * @author igdp
 */
public interface IBatchService {

    /**
     * 生成批次号
     * 规则：BATCH-{物料ID}-{日期时间戳}-{序号}
     *
     * @param materialId   物料ID
     * @param warehouseId  仓库ID
     * @param inboundType  入库类型
     * @param quantity     数量
     * @param inboundTime  入库时间
     * @return 批次号
     */
    String generateBatchId(String materialId, String warehouseId, Integer inboundType,
                          BigDecimal quantity, Date inboundTime);

    /**
     * 生成二维码
     * 二维码内容包含：物料信息、批次号、仓库ID、有效期等
     *
     * @param materialId      物料ID
     * @param materialBatchId 批次ID
     * @param warehouseId     仓库ID
     * @param expiryDate      过期日期
     * @param quantity        数量
     * @return 二维码字符串
     */
    String generateQrCode(String materialId, String materialBatchId, String warehouseId,
                         Date expiryDate, BigDecimal quantity);

    /**
     * 根据二维码解析批次信息
     *
     * @param qrCode 二维码
     * @return 批次信息
     */
    Map<String, Object> parseQrCode(String qrCode);

    /**
     * 校验批次是否有效（未过期）
     *
     * @param materialBatchId 批次ID
     * @param warehouseId     仓库ID
     * @param materialId      物料ID
     * @return 是否有效
     */
    boolean validateBatch(String materialBatchId, String warehouseId, String materialId);

    /**
     * 查询批次追溯信息
     * 包含入库、出库、库存变动等完整信息
     *
     * @param materialBatchId 批次ID
     * @return 追溯信息
     */
    Map<String, Object> traceBatch(String materialBatchId);
}
