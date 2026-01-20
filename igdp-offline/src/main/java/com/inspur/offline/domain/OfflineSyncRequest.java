package com.inspur.offline.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 离线同步请求对象
 * 用于接收移动端提交的离线数据
 *
 * @author inspur
 */
@Getter
@Setter
public class OfflineSyncRequest {
    /**
     * 业务类型：farmer-农民, land-土地
     */
    private String businessType;

    /**
     * 表单编码：farmer-add, land-add
     */
    private String formCode;

    /**
     * 表单数据（JSON格式）
     */
    private Map<String, Object> formData;

    /**
     * 客户端记录ID（用于追踪）
     */
    private String clientRecordId;
}
