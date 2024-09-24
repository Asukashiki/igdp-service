package com.inspur.assets.monitor.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.inspur.common.enums.BaseEnum;
import lombok.Getter;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName AssetsAlertStatus
 * @date 2024/7/11 16:40
 */
@Getter
public enum AssetsAlertStatus implements BaseEnum {
    /**
     * 严重
     */
    Critical("4","紧急"),
    /**
     * 重要
     * */
    Major("3","重要"),
    /**
     * 警告
     */
    Minor("2","次要"),
    /**
     * 提示
     */
    Warning("1","提示"),
    /**
     * 未定义
     */
    Unknown("0","未定义"),
    /**
     * 恢复
     */
    Ok("-1","恢复"),
    ;

    @EnumValue
    private final String code;

    private final String description;

    AssetsAlertStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

}
