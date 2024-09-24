package com.inspur.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.core.domain.SelectEntity;
import com.inspur.system.domain.SysUnifyTodo;

import java.util.List;

/**
 * @author liyunlong
 * @date 2024/1/25
 */
public interface ISysUnifyTodoService extends IService<SysUnifyTodo> {
    /**
     * 查询列表
     * @param queryParam 查询条件
     * @return 集合列表
     * */
    List<SysUnifyTodo> selectSysTodoList(SysUnifyTodo queryParam);
    /**
     * 保存待办信息
     * @param sysUnifyTodo 待办信息
     * */
    void addTodo(SysUnifyTodo sysUnifyTodo);
    /**
     * 更新待办的状态
     * @param sysUnifyTodo 信息
     * */
    void updateStatus(SysUnifyTodo sysUnifyTodo);
    /**
     * 更新待办信息
     * @param sysUnifyTodo 待办信息
     * @return 结果
     * */
    boolean updateTodo(SysUnifyTodo sysUnifyTodo);

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
