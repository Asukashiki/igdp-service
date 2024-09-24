package com.inspur.common.core.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity基类
 *
 * @author liyunlong
 */
@Setter
@Getter
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 搜索值
     */
    @JsonIgnore
    @TableField(exist = false)
    private transient String searchValue;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private transient Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>(1);
        }
        return params;
    }

    public LocalDateTime getBeginTime() {
        Map<String, Object> params = getParams();
        LocalDateTime beginTime = null;
        if (null != params) {
            beginTime = null != params.get("beginTime") ? LocalDateTime.of(LocalDate.parse(params.get("beginTime").toString()), LocalTime.MIN) : null;

        }
        return beginTime;
    }

    public LocalDateTime getEndTime() {
        Map<String, Object> params = getParams();
        LocalDateTime endTime = null;
        if (null != params) {
            endTime = null != params.get("endTime") ? LocalDateTime.of(LocalDate.parse(params.get("endTime").toString()), LocalTime.MAX) : null;
        }
        return endTime;
    }

}
