package com.inspur.doc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.utils.LoginHelper;

import com.inspur.common.utils.StringUtils;
import com.inspur.doc.domain.SysFileInfo;
import com.inspur.doc.mapper.SysFileInfoMapper;
import com.inspur.doc.service.ISysFileInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName SysFileInfoServiceImpl
 * @date 2024/5/5 10:03
 */
@Service
public class SysFileInfoServiceImpl extends ServiceImpl<SysFileInfoMapper, SysFileInfo> implements ISysFileInfoService {

    @Override
    public void saveSysFileInfo(SysFileInfo fileInfo) {
        fileInfo.setCreateBy(LoginHelper.getUsername());
        fileInfo.setCreateTime(LocalDateTime.now());
        save(fileInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveList(List<SysFileInfo> fileInfoList) {
        if (null != fileInfoList && !fileInfoList.isEmpty()) {
            for (SysFileInfo fileInfo : fileInfoList) {
                fileInfo.setCreateBy(LoginHelper.getUserId());
                fileInfo.setCreateTime(LocalDateTime.now());
            }
            saveBatch(fileInfoList);
        }
    }

    @Override
    public SysFileInfo queryById(String id) {
        return getById(id);
    }

    @Override
    public SysFileInfo queryByDataIdServer(String dataId, String serverType) {
        LambdaQueryWrapper<SysFileInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysFileInfo::getDataId, dataId);
        if (StringUtils.isNotEmpty(serverType)) {
            wrapper.eq(SysFileInfo::getServerType, serverType);
        }
        return getOne(wrapper);
    }

    @Override
    public List<SysFileInfo> getListByIds(String[] ids) {
        return list(new LambdaQueryWrapper<SysFileInfo>()
                .eq(SysFileInfo::getId, Arrays.stream(ids).toArray()));
    }
}
