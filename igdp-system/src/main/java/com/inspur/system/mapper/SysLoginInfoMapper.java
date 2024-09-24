package com.inspur.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.inspur.system.domain.SysLoginInfo;
import org.apache.ibatis.annotations.Update;

/**
 * 系统访问日志情况信息 数据层
 *
 * @author liyunlong
 */
public interface SysLoginInfoMapper extends BaseMapper<SysLoginInfo> {


    /**
     * 清空系统登录日志
     *
     * @return 结果
     */
    @Update("truncate table sys_login_info")
    int cleanLoginInfo();
}
