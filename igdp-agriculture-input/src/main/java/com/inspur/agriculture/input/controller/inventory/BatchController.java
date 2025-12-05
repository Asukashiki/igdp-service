package com.inspur.agriculture.input.controller.inventory;

import com.inspur.agriculture.input.service.inventory.IBatchService;
import com.inspur.agriculture.input.service.inventory.IInventoryService;
import com.inspur.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 批次管理 Controller
 *
 * @author inspur
 * @date 2025-12-03
 */
@RestController
@RequestMapping("/api/batch")
public class BatchController {

    @Autowired
    private IInventoryService inventoryService;

    @Autowired
    private IBatchService batchService;

    /**
     * 查询可用批次列表(用于出库选择)
     *
     * @param warehouseId 仓库ID
     * @param inputId     投入品ID
     * @return 可用批次列表
     */
    @GetMapping("/list")
    public AjaxResult list(
            @RequestParam Long warehouseId,
            @RequestParam Long inputId
    ) {
        try {
            List<Map<String, Object>> list = inventoryService.getAvailableBatchList(warehouseId, inputId);
            return AjaxResult.success(list);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 生成批次号
     * 规则：BATCH-{物料ID}-{日期时间戳}-{序号}
     *
     * @param materialId 物料ID
     * @param warehouseId 仓库ID
     * @param inboundType 入库类型
     * @param quantity 数量
     * @param inboundTime 入库时间（格式：yyyy-MM-dd HH:mm:ss）
     * @return 批次信息
     */
    @PostMapping("/generate")
    public AjaxResult generateBatch(
            @RequestParam String materialId,
            @RequestParam String warehouseId,
            @RequestParam Integer inboundType,
            @RequestParam BigDecimal quantity,
            @RequestParam String inboundTime
    ) {
        try {
            // 解析入库时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parsedInboundTime = sdf.parse(inboundTime);

            // 生成批次号
            String batchId = batchService.generateBatchId(
                    materialId,
                    warehouseId,
                    inboundType,
                    quantity,
                    parsedInboundTime
            );

            // 生成二维码（假设过期日期为一年后，实际应该从请求中获取）
            Date expiryDate = new Date(parsedInboundTime.getTime() + 365L * 24 * 60 * 60 * 1000);
            String qrCode = batchService.generateQrCode(
                    materialId,
                    batchId,
                    warehouseId,
                    expiryDate,
                    quantity
            );

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("batch_id", batchId);
            result.put("qr_code", qrCode);
            result.put("batch_name", "批次-" + batchId);

            return AjaxResult.success("生成成功", result);
        } catch (Exception e) {
            return AjaxResult.error("生成批次号失败: " + e.getMessage());
        }
    }

    /**
     * 生成二维码
     * 二维码内容包含：物料信息、批次号、仓库ID、有效期等
     *
     * @param materialId 物料ID
     * @param materialBatchId 批次ID
     * @param warehouseId 仓库ID
     * @param expiryDate 过期日期（格式：yyyy-MM-dd）
     * @param quantity 数量
     * @return 二维码信息
     */
    @PostMapping("/qrcode/generate")
    public AjaxResult generateQrCode(
            @RequestParam String materialId,
            @RequestParam String materialBatchId,
            @RequestParam String warehouseId,
            @RequestParam(required = false) String expiryDate,
            @RequestParam(required = false, defaultValue = "0") BigDecimal quantity
    ) {
        try {
            // 解析过期日期
            Date parsedExpiryDate = null;
            if (expiryDate != null && !expiryDate.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                parsedExpiryDate = sdf.parse(expiryDate);
            }

            // 生成二维码
            String qrCode = batchService.generateQrCode(
                    materialId,
                    materialBatchId,
                    warehouseId,
                    parsedExpiryDate,
                    quantity
            );

            // 构建返回结果（这里假设有生成二维码图片的功能）
            Map<String, Object> result = new HashMap<>();
            result.put("qr_code", qrCode);
            // 实际项目中应该调用二维码生成服务生成图片URL
            result.put("qr_code_image_url", "/api/qrcode/image/" + qrCode);

            return AjaxResult.success("生成成功", result);
        } catch (Exception e) {
            return AjaxResult.error("生成二维码失败: " + e.getMessage());
        }
    }

    /**
     * 解析二维码
     * 根据二维码内容解析出批次信息
     *
     * @param qrCode 二维码字符串
     * @return 批次信息
     */
    @GetMapping("/qrcode/parse")
    public AjaxResult parseQrCode(@RequestParam String qrCode) {
        try {
            Map<String, Object> batchInfo = batchService.parseQrCode(qrCode);
            if (batchInfo == null || batchInfo.isEmpty()) {
                return AjaxResult.error("无效的二维码");
            }
            return AjaxResult.success(batchInfo);
        } catch (Exception e) {
            return AjaxResult.error("解析二维码失败: " + e.getMessage());
        }
    }

    /**
     * 校验批次是否有效（未过期）
     *
     * @param materialBatchId 批次ID
     * @param warehouseId 仓库ID
     * @param materialId 物料ID
     * @return 是否有效
     */
    @GetMapping("/validate")
    public AjaxResult validateBatch(
            @RequestParam String materialBatchId,
            @RequestParam String warehouseId,
            @RequestParam String materialId
    ) {
        try {
            boolean valid = batchService.validateBatch(materialBatchId, warehouseId, materialId);
            Map<String, Object> result = new HashMap<>();
            result.put("valid", valid);
            result.put("material_batch_id", materialBatchId);
            result.put("warehouse_id", warehouseId);
            result.put("material_id", materialId);

            if (!valid) {
                result.put("message", "批次已过期或不存在");
            }

            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("校验批次失败: " + e.getMessage());
        }
    }

    /**
     * 查询批次追溯信息
     * 包含入库、出库、库存变动等完整信息
     *
     * @param materialBatchId 批次ID
     * @return 追溯信息
     */
    @GetMapping("/trace")
    public AjaxResult traceBatch(@RequestParam String materialBatchId) {
        try {
            Map<String, Object> traceInfo = batchService.traceBatch(materialBatchId);
            if (traceInfo == null || traceInfo.isEmpty()) {
                return AjaxResult.error("批次不存在");
            }
            return AjaxResult.success(traceInfo);
        } catch (Exception e) {
            return AjaxResult.error("查询批次追溯信息失败: " + e.getMessage());
        }
    }

    /**
     * 根据批次ID查询批次基本信息
     *
     * @param materialBatchId 批次ID
     * @return 批次信息
     */
    @GetMapping("/{materialBatchId}")
    public AjaxResult getInfo(@PathVariable String materialBatchId) {
        try {
            // 这里可以调用批次追溯接口获取基本信息
            Map<String, Object> batchInfo = batchService.traceBatch(materialBatchId);
            if (batchInfo == null || batchInfo.isEmpty()) {
                return AjaxResult.error("批次不存在");
            }
            return AjaxResult.success(batchInfo);
        } catch (Exception e) {
            return AjaxResult.error("查询批次信息失败: " + e.getMessage());
        }
    }

    /**
     * 批量生成批次号
     * 用于批量入库时一次性生成多个批次号
     *
     * @param materialId 物料ID
     * @param warehouseId 仓库ID
     * @param inboundType 入库类型
     * @param count 生成数量
     * @return 批次号列表
     */
    @PostMapping("/generate/batch")
    public AjaxResult generateBatchList(
            @RequestParam String materialId,
            @RequestParam String warehouseId,
            @RequestParam Integer inboundType,
            @RequestParam(defaultValue = "1") Integer count,
            @RequestParam(defaultValue = "1.0") BigDecimal quantity
    ) {
        try {
            java.util.List<Map<String, Object>> batchList = new java.util.ArrayList<>();
            Date currentTime = new Date();

            for (int i = 0; i < count; i++) {
                // 生成批次号
                String batchId = batchService.generateBatchId(
                        materialId,
                        warehouseId,
                        inboundType,
                        quantity,
                        new Date(currentTime.getTime() + i * 1000) // 每个批次间隔1秒
                );

                Map<String, Object> batch = new HashMap<>();
                batch.put("batch_id", batchId);
                batch.put("batch_name", "批次-" + batchId);
                batchList.add(batch);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("count", count);
            result.put("batches", batchList);

            return AjaxResult.success("批量生成成功", result);
        } catch (Exception e) {
            return AjaxResult.error("批量生成批次号失败: " + e.getMessage());
        }
    }
}
