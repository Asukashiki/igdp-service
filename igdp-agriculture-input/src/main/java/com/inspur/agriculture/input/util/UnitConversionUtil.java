package com.inspur.agriculture.input.util;

import com.inspur.common.core.domain.entity.SysDictData;
import com.inspur.common.utils.DictUtils;
import com.inspur.common.utils.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 计量单位转换工具类
 * 用于解析字典中的计量单位并计算实际的KG或L值
 * 
 * 字典配置说明（dict_type = 'input_material_unit'）：
 * dict_label（规格描述）    dict_value（代码）
 * KG                       U001
 * Package/10kg             U101
 * Package/20kg             U102
 * Package/50kg             U103
 * Package/100kg            U104
 * ml                       U002
 * Bottle/100ml             U201
 * Bottle/200ml             U202
 * Bottle/500ml             U203
 * Bottle/1000ml            U204
 * g                        U003
 * Package/100g             U301
 * Package/200g             U302
 * Package/500g             U303
 * Package/1000g            U304
 *
 * @author igdp
 */
public class UnitConversionUtil {

    /**
     * 计量单位字典类型
     */
    public static final String DICT_TYPE_UNIT = "input_material_unit";

    /**
     * 单位类型：重量（KG）
     */
    public static final String UNIT_TYPE_WEIGHT = "weight";

    /**
     * 单位类型：容积（L）
     */
    public static final String UNIT_TYPE_VOLUME = "volume";

    /**
     * 解析结果类
     */
    public static class UnitParseResult {
        private BigDecimal value;        // 数值
        private String unit;             // 单位（kg/g/l/ml）
        private String unitType;         // 单位类型（weight/volume）
        private BigDecimal convertedValue; // 转换后的值（统一为KG或L）
        private boolean success;         // 是否解析成功
        private String message;          // 错误信息

        public BigDecimal getValue() {
            return value;
        }

        public void setValue(BigDecimal value) {
            this.value = value;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getUnitType() {
            return unitType;
        }

        public void setUnitType(String unitType) {
            this.unitType = unitType;
        }

        public BigDecimal getConvertedValue() {
            return convertedValue;
        }

        public void setConvertedValue(BigDecimal convertedValue) {
            this.convertedValue = convertedValue;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    /**
     * 根据字典值解析计量单位并计算转换后的值
     * 
     * 字典中 dict_value 是代码（如 U001, U101），dict_label 是规格描述（如 KG, Package/10kg）
     *
     * @param dictValue 字典值（如 U001, U101）
     * @return 解析结果
     */
    public static UnitParseResult parseUnitFromDict(String dictValue) {
        UnitParseResult result = new UnitParseResult();
        result.setSuccess(false);

        if (StringUtils.isEmpty(dictValue)) {
            result.setMessage("计量单位值不能为空");
            return result;
        }

        // 从字典缓存获取字典数据
        List<SysDictData> dictDataList = DictUtils.getDictCache(DICT_TYPE_UNIT);
        if (dictDataList == null || dictDataList.isEmpty()) {
            result.setMessage("计量单位字典未配置或缓存为空，字典类型: " + DICT_TYPE_UNIT);
            return result;
        }

        // 查找匹配的字典数据（根据 dict_value 查找）
        SysDictData matchedDict = null;
        for (SysDictData dictData : dictDataList) {
            if (dictValue.equals(dictData.getDictValue())) {
                matchedDict = dictData;
                break;
            }
        }

        if (matchedDict == null) {
            result.setMessage("未找到计量单位字典数据: " + dictValue);
            return result;
        }

        // 使用 dict_label 作为规格描述进行解析
        String dictLabel = matchedDict.getDictLabel();
        
        return parseUnitString(dictLabel);
    }

    /**
     * 解析计量单位字符串
     * 
     * 支持格式：
     * - 纯单位：KG, g, ml（默认数值为1）
     * - 带数值：Package/10kg, Bottle/500ml, Package/100g 等
     *
     * @param unitString 计量单位字符串
     * @return 解析结果
     */
    public static UnitParseResult parseUnitString(String unitString) {
        UnitParseResult result = new UnitParseResult();
        result.setSuccess(false);

        if (StringUtils.isEmpty(unitString)) {
            result.setMessage("计量单位字符串不能为空");
            return result;
        }

        String trimmedString = unitString.trim();
        
        // 先处理纯单位的情况（如 KG, g, ml）
        String lowerString = trimmedString.toLowerCase();
        if ("kg".equals(lowerString)) {
            result.setValue(BigDecimal.ONE);
            result.setUnit("kg");
            result.setUnitType(UNIT_TYPE_WEIGHT);
            result.setConvertedValue(BigDecimal.ONE);
            result.setSuccess(true);
            return result;
        } else if ("g".equals(lowerString)) {
            result.setValue(BigDecimal.ONE);
            result.setUnit("g");
            result.setUnitType(UNIT_TYPE_WEIGHT);
            // 1g = 0.001kg
            result.setConvertedValue(new BigDecimal("0.001"));
            result.setSuccess(true);
            return result;
        } else if ("ml".equals(lowerString)) {
            result.setValue(BigDecimal.ONE);
            result.setUnit("ml");
            result.setUnitType(UNIT_TYPE_VOLUME);
            // 1ml = 0.001L
            result.setConvertedValue(new BigDecimal("0.001"));
            result.setSuccess(true);
            return result;
        } else if ("l".equals(lowerString)) {
            result.setValue(BigDecimal.ONE);
            result.setUnit("l");
            result.setUnitType(UNIT_TYPE_VOLUME);
            result.setConvertedValue(BigDecimal.ONE);
            result.setSuccess(true);
            return result;
        }

        // 正则表达式匹配数值和单位
        // 支持格式：Package/10kg, Bottle/500ml, Package/100g 等
        Pattern pattern = Pattern.compile("(?:.*[/\\s])?(\\d+(?:\\.\\d+)?)(kg|g|l|ml)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(trimmedString);

        if (!matcher.find()) {
            result.setMessage("无法解析计量单位格式: " + unitString + "，期望格式如: KG, Package/10kg, Bottle/500ml");
            return result;
        }

        try {
            BigDecimal value = new BigDecimal(matcher.group(1));
            String unit = matcher.group(2).toLowerCase();

            result.setValue(value);
            result.setUnit(unit);

            // 根据单位类型进行转换
            switch (unit) {
                case "kg":
                    result.setUnitType(UNIT_TYPE_WEIGHT);
                    result.setConvertedValue(value);
                    break;
                case "g":
                    result.setUnitType(UNIT_TYPE_WEIGHT);
                    // g 转换为 kg：除以 1000
                    result.setConvertedValue(value.divide(new BigDecimal("1000"), 6, RoundingMode.HALF_UP));
                    break;
                case "l":
                    result.setUnitType(UNIT_TYPE_VOLUME);
                    result.setConvertedValue(value);
                    break;
                case "ml":
                    result.setUnitType(UNIT_TYPE_VOLUME);
                    // ml 转换为 L：除以 1000
                    result.setConvertedValue(value.divide(new BigDecimal("1000"), 6, RoundingMode.HALF_UP));
                    break;
                default:
                    result.setMessage("不支持的计量单位: " + unit);
                    return result;
            }

            result.setSuccess(true);
            return result;

        } catch (NumberFormatException e) {
            result.setMessage("解析数值失败: " + unitString);
            return result;
        }
    }

    /**
     * 计算入库总量（KG或L）
     *
     * @param dictValue 计量单位字典值（如 U001, U101）
     * @param quantity  入库数量
     * @return 解析结果（包含计算后的总量）
     */
    public static UnitParseResult calculateTotalAmount(String dictValue, BigDecimal quantity) {
        UnitParseResult parseResult = parseUnitFromDict(dictValue);
        
        if (!parseResult.isSuccess()) {
            return parseResult;
        }

        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            parseResult.setSuccess(false);
            parseResult.setMessage("入库数量必须大于0");
            return parseResult;
        }

        // 计算总量 = 单位转换值 * 入库数量
        BigDecimal totalAmount = parseResult.getConvertedValue().multiply(quantity);
        parseResult.setConvertedValue(totalAmount);

        return parseResult;
    }

    /**
     * 直接从计量单位字符串计算入库总量
     *
     * @param unitString 计量单位字符串（如 Package/10kg）
     * @param quantity   入库数量
     * @return 解析结果
     */
    public static UnitParseResult calculateTotalAmountFromString(String unitString, BigDecimal quantity) {
        UnitParseResult parseResult = parseUnitString(unitString);
        
        if (!parseResult.isSuccess()) {
            return parseResult;
        }

        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            parseResult.setSuccess(false);
            parseResult.setMessage("入库数量必须大于0");
            return parseResult;
        }

        // 计算总量 = 单位转换值 * 入库数量
        BigDecimal totalAmount = parseResult.getConvertedValue().multiply(quantity);
        parseResult.setConvertedValue(totalAmount);

        return parseResult;
    }
}
