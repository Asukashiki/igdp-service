package com.inspur.seed.service.impl;

import cn.hutool.core.util.IdUtil;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.EnvironmentNewData;
import com.inspur.seed.mapper.EnvironmentNewDataMapper;
import com.inspur.seed.service.IEnvironmentNewDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 环境监测新数据Service实现类
 * Environment New Data Service Implementation
 *
 * @author inspur
 */
@Service
public class EnvironmentNewDataServiceImpl implements IEnvironmentNewDataService {

    @Autowired
    private EnvironmentNewDataMapper environmentNewDataMapper;

    @Override
    public List<EnvironmentNewData> selectEnvironmentNewDataList(EnvironmentNewData environmentNewData) {
        return environmentNewDataMapper.selectEnvironmentNewDataList(environmentNewData);
    }

    @Override
    public EnvironmentNewData selectEnvironmentNewDataById(String envRecordId) {
        return environmentNewDataMapper.selectEnvironmentNewDataById(envRecordId);
    }

    @Override
    public String insertEnvironmentNewData(EnvironmentNewData environmentNewData) {
        // 生成主键
        String envRecordId = IdUtil.simpleUUID();
        environmentNewData.setEnvRecordId(envRecordId);

        // 设置创建信息
        environmentNewData.setCreateTime(LocalDateTime.now());
        environmentNewData.setCreateBy(SecurityUtils.getUsername());

        environmentNewDataMapper.insert(environmentNewData);
        return envRecordId;
    }

    @Override
    public int updateEnvironmentNewData(EnvironmentNewData environmentNewData) {
        // 设置更新信息
        environmentNewData.setUpdateTime(LocalDateTime.now());
        environmentNewData.setUpdateBy(SecurityUtils.getUsername());

        return environmentNewDataMapper.updateById(environmentNewData);
    }

    @Override
    public int deleteEnvironmentNewDataByIds(String[] envRecordIds) {
        int count = 0;
        for (String envRecordId : envRecordIds) {
            // 使用deleteById方法，让@TableLogic自动处理逻辑删除
            boolean success = environmentNewDataMapper.deleteById(envRecordId) > 0;
            if (success) {
                count++;
            }
        }
        return count;
    }
}
