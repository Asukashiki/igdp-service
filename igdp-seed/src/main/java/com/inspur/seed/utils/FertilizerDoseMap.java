package com.inspur.seed.utils;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
/**
 * 作物施肥量映射工具类
 */
public class FertilizerDoseMap {

    // 定义不可变的施肥量映射字典（作物 -> 肥料 -> 施肥量）
    public static final Map<String, Map<String, Integer>> CROP_FERTILIZER_DOSE_MAP;

    static {
        // 初始化底层Map
        Map<String, Map<String, Integer>> tempMap = new HashMap<>();

        // 处理小麦的施肥数据
        Map<String, Integer> wheatMap = new HashMap<>();
        wheatMap.put("IN0201", 100);
        wheatMap.put("IN0204", 150);
        tempMap.put("IN0101", wheatMap);

        // 处理玉米的施肥数据
        Map<String, Integer> cornMap = new HashMap<>();
        cornMap.put("IN0201", 150);
        cornMap.put("IN0204", 100);
        tempMap.put("IN0102", cornMap);

        // 处理苔麸的施肥数据
        Map<String, Integer> teffMap = new HashMap<>();
        teffMap.put("IN0204", 100);
        tempMap.put("IN0103", teffMap);

        // 转为不可变Map，保证数据安全性
        CROP_FERTILIZER_DOSE_MAP = Collections.unmodifiableMap(tempMap);
    }

    /**
     * 获取指定作物和肥料的施肥量
     * @param crop 作物名称（如：小麦）
     * @param fertilizer 肥料名称（如：NPS）
     * @return 施肥量（公斤/公顷），无数据时返回null
     */
    public static Integer getDose(String crop, String fertilizer) {
        Map<String, Integer> fertilizerMap = CROP_FERTILIZER_DOSE_MAP.get(crop);
        return fertilizerMap == null ? null : fertilizerMap.get(fertilizer);
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试查询
        System.out.println("小麦-NPS：" + getDose("IN0101", "IN0202")); // 输出：100
        System.out.println("玉米-尿素：" + getDose("IN0102", "IN0203")); // 输出：100
        System.out.println("苔麸-NPS：" + getDose("IN0103", "IN0202")); // 输出：100
        System.out.println("苔麸-尿素：" + getDose("IN0103", "IN0203")); // 输出：null
    }
}
