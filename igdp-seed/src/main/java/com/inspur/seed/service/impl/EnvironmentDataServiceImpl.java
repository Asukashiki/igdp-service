package com.inspur.seed.service.impl;

import cn.hutool.core.util.IdUtil;
import com.inspur.common.utils.MessageUtils;
import com.inspur.common.utils.SecurityUtils;
import com.inspur.seed.domain.EnvironmentData;
import com.inspur.seed.mapper.EnvironmentDataMapper;
import com.inspur.seed.service.IEnvironmentDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        List<EnvironmentData> list = environmentDataMapper.selectEnvironmentDataList(environmentData);
        // 为每个对象设置翻译名称
        list.forEach(this::setTranslatedNames);
        return list;
    }

    @Override
    public EnvironmentData selectEnvironmentDataById(String envId) {
        EnvironmentData data = environmentDataMapper.selectEnvironmentDataById(envId);
        if (data != null) {
            setTranslatedNames(data);
        }
        return data;
    }

    @Override
    public String insertEnvironmentData(EnvironmentData environmentData) {
        // 生成主键
        String envId = IdUtil.simpleUUID();
        environmentData.setEnvRecordId(envId);

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
            // 使用deleteById方法，让@TableLogic自动处理逻辑删除
            boolean success = environmentDataMapper.deleteById(envId) > 0;
            if (success) {
                count++;
            }
        }
        return count;
    }

    /**
     * 设置翻译名称
     */
    private void setTranslatedNames(EnvironmentData data) {
        if (data == null) {
            return;
        }
        // 设置数据类型名称（国际化）
        data.setDataTypeName(getDataTypeName(data.getDataType()));
    }

    /**
     * 获取数据类型名称（支持国际化）
     */
    private String getDataTypeName(String dataType) {
        if (dataType == null || dataType.isEmpty()) {
            return "";
        }
        String messageKey = "data.type." + dataType.toLowerCase();
        try {
            return MessageUtils.message(messageKey);
        } catch (Exception e) {
            // 如果找不到对应的国际化key，返回原值
            return dataType;
        }
    }
}
