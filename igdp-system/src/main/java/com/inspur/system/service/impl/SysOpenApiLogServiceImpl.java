package com.inspur.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.system.domain.SysOpenApiLog;
import com.inspur.system.mapper.SysOpenApiLogMapper;
import com.inspur.system.service.ISysOpenApiLogService;
import org.springframework.stereotype.Service;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName SysOpenApiLogServiceImpl
 * @date 2024/6/14 15:44
 */
@Service
public class SysOpenApiLogServiceImpl extends ServiceImpl<SysOpenApiLogMapper, SysOpenApiLog> implements ISysOpenApiLogService {
}
