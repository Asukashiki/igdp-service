package com.inspur.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.core.domain.entity.SysApp;
import com.inspur.system.mapper.SysAppMapper;
import com.inspur.system.service.ISysAppService;
import org.springframework.stereotype.Service;

/**
 * @author liyunlong
 * @date 2024/4/2
 */
@Service
public class SysAppServiceImpl extends ServiceImpl<SysAppMapper, SysApp> implements ISysAppService {
}
