package com.inspur.seed.service.impl;

import cn.hutool.core.util.IdUtil;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.EnvironmentData;
import com.inspur.seed.mapper.EnvironmentDataMapper;
import com.inspur.seed.service.IEnvironmentDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 环境属性数据Service实现类
 *
 * @author inspur
 */
@Service
public class EnvironmentDataServiceImpl implements IEnvironmentDataService {

    @Autowired
    private EnvironmentDataMapper environmentDataMapper;

    @Override
    public List<EnvironmentData> selectEnvironmentDataList(EnvironmentData environmentData) {
        return environmentDataMapper.selectEnvironmentDataList(environmentData);
    }

    @Override
    public EnvironmentData selectEnvironmentDataById(String envId) {
        return environmentDataMapper.selectEnvironmentDataById(envId);
    }

    @Override
    public String insertEnvironmentData(EnvironmentData environmentData) {
        // 生成主键
        String envId = IdUtil.simpleUUID();
        environmentData.setEnvId(envId);

        // 设置创建信息
        environmentData.setCreateTime(LocalDateTime.now());
        environmentData.setCreateBy(SecurityUtils.getUsername());

        environmentDataMapper.insert(environmentData);
        return envId;
    }

    @Override
    public int updateEnvironmentData(EnvironmentData environmentData) {
        // 设置更新信息
        environmentData.setUpdateTime(LocalDateTime.now());
        environmentData.setUpdateBy(SecurityUtils.getUsername());

        return environmentDataMapper.updateById(environmentData);
    }

    @Override
    public int deleteEnvironmentDataByIds(String[] envIds) {
        int count = 0;
        for (String envId : envIds) {
            EnvironmentData envData = new EnvironmentData();
            envData.setEnvId(envId);
            envData.setIsDeleted(1);
            count += environmentDataMapper.updateById(envData);
        }
        return count;
    }
}
