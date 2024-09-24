package com.inspur.workorder.constant;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName Constants
 * @date 2024/4/29 16:50
 */
public class Constants {

    /**
     * 工单状态
     * 0已发起；1处理中；2待确认；9已完成；-1已取消
     * */
    public static final String WORK_ORDER_STATE_NEW = "0";
    public static final String WORK_ORDER_STATE_BEING = "1";
    public static final String WORK_ORDER_STATE_AUDIT = "2";
    public static final String WORK_ORDER_STATE_FINISHED = "9";
    public static final String WORK_ORDER_STATE_CANCEL = "-1";
}
