package com.inspur.common.constant;

/**
 * @author liyunlong
 * @date 2023/9/21
 */
public class AssetsConstants {
    /**
     * 是否国产化标志内容
     */
    public static final String LOCALIZATION_FLAG = "是";

    public static final String ASSETS_STATUS_VALUE_USED = "在用";
    public static final String ASSETS_STATUS_USED = "01";

    /**
     * 运维人员进场状态值
     * */
    public static final String OPERATION_STAFF_STATUS_ENTER = "已进场";


    /**
     * 物理机编号
     * */
    public static final String PHYSICAL_MACHINE_TYPE_CODE = "0101";
    /**
     * 虚拟机编号
     * */
    public static final String VIRTUAL_MACHINE_TYPE_CODE = "0102";
    /**
     * 应用系统资产类型编号
     * */
    public static final String APPLICATION_SYSTEM_TYPE_CODE = "0201";
    /**
     * 项目
     * */
    public static final String ASSETS_PROJECT_TYPE_CODE = "0501";
    /**
     * 厂商
     * */
    public static final String ASSETS_VENDOR_TYPE_CODE = "0503";

    public static final String ROOT_NODE_CODE = "0000";

    public static final String STATISTICS_GROUP_BY_SERVICE = "service";
    public static final String STATISTICS_GROUP_BY_ORGAN = "organ";

    public static final String OPS_CONDITION_EXPIRED = "过保";

    public static final String OPS_CONDITION_UNDER = "在保";

    public static final String OPS_CONDITION_OVERCOMING = "即将过保";

    /**
     * 维保到期剩余多少天开始算即将过保
     * */
    public static final Integer OPS_CONDITION_OVERCOMING_DAYS = 30;


    public static final String ICD_RUNTIME_URI_KEY = "icd.runtime.uri";
    /**
     * 资产详情信息链接，将其中的关键参数替换即可
     * */
    public static final String ASSETS_VIEW_LINK = "%s/FormDetail?appId=%s&formId=%s&dataId=%s&isPrint=true&showHeader=true&hidingTabs=false";
}
