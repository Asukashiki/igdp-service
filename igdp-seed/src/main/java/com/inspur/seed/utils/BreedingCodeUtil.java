package com.inspur.seed.utils;

import cn.hutool.core.date.DateUtil;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 繁殖业务编号生成工具类
 *
 * @author igdp
 * @date 2025-11-29
 */
public class BreedingCodeUtil {

    /**
     * 繁殖批次编号前缀
     */
    private static final String BATCH_PREFIX = "BB";

    /**
     * 跟踪编号前缀
     */
    private static final String TRACKING_PREFIX = "BT";

    /**
     * 检测编号前缀
     */
    private static final String TEST_PREFIX = "TE";

    /**
     * 流水号位数
     */
    private static final int SERIAL_LENGTH = 6;

    /**
     * 批次流水号计数器
     */
    private static final AtomicInteger batchCounter = new AtomicInteger(0);

    /**
     * 跟踪流水号计数器
     */
    private static final AtomicInteger trackingCounter = new AtomicInteger(0);

    /**
     * 检测流水号计数器
     */
    private static final AtomicInteger testCounter = new AtomicInteger(0);

    /**
     * 上次生成日期（用于按日重置流水号）
     */
    private static volatile String lastDate = "";

    /**
     * 生成繁殖批次编号
     * 规则：BB + yyyyMMdd + 6位流水号
     *
     * @return 繁殖批次编号
     */
    public static synchronized String generateBatchId() {
        return generateCode(BATCH_PREFIX, batchCounter);
    }

    /**
     * 生成跟踪编号
     * 规则：BT + yyyyMMdd + 6位流水号
     *
     * @return 跟踪编号
     */
    public static synchronized String generateTrackingId() {
        return generateCode(TRACKING_PREFIX, trackingCounter);
    }

    /**
     * 生成检测编号
     * 规则：TE + yyyyMMdd + 6位流水号
     *
     * @return 检测编号
     */
    public static synchronized String generateTestId() {
        return generateCode(TEST_PREFIX, testCounter);
    }

    /**
     * 生成编号的通用方法
     *
     * @param prefix  编号前缀
     * @param counter 计数器
     * @return 生成的编号
     */
    private static String generateCode(String prefix, AtomicInteger counter) {
        // 获取当前日期字符串
        String currentDate = DateUtil.format(new Date(), "yyyyMMdd");

        // 如果日期变化，重置计数器
        if (!currentDate.equals(lastDate)) {
            synchronized (BreedingCodeUtil.class) {
                if (!currentDate.equals(lastDate)) {
                    batchCounter.set(0);
                    trackingCounter.set(0);
                    testCounter.set(0);
                    lastDate = currentDate;
                }
            }
        }

        // 递增计数器并获取流水号
        int serialNumber = counter.incrementAndGet();

        // 格式化流水号（补零）
        String serialStr = String.format("%0" + SERIAL_LENGTH + "d", serialNumber);

        // 拼接编号
        return prefix + currentDate + serialStr;
    }

    /**
     * 重置所有计数器（测试用）
     */
    public static synchronized void resetCounters() {
        batchCounter.set(0);
        trackingCounter.set(0);
        testCounter.set(0);
        lastDate = "";
    }
}
