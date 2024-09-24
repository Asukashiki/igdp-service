package com.inspur.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.domain.SelectEntity;
import com.inspur.system.domain.SysUnifyTodo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liyunlong
 * @date 2024/1/25
 */
@Mapper
public interface SysUnifyTodoMapper extends BaseMapper<SysUnifyTodo> {

    /**
     * 获取统一待办类型
     * @return
     */
    List<SelectEntity> selectType();

    /**
     * 获取统一待办来源
     * @return
     */
    List<SelectEntity> selectSource();
}
