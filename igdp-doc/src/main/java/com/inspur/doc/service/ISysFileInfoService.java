package com.inspur.doc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.doc.domain.SysFileInfo;

import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName ISysFileInfoService
 * @date 2024/5/5 10:02
 */
public interface ISysFileInfoService extends IService<SysFileInfo> {


    /**
     * 保存文件
     * @param fileInfo 文件内容
     * */
    void saveSysFileInfo(SysFileInfo fileInfo);
    /**
     * 批量保存
     * @param fileInfoList 文件列表
     * */
    void saveList(List<SysFileInfo> fileInfoList);
    /**
     * 根据id获取文件信息
     * @param id 文件id
     * @return 文件信息
     * */
    SysFileInfo queryById(String id);
    /**
     * 根据dataId获取数据
     * @param dataId 文件响应id
     * @param serverType 文件服务类型：minio、est
     * @return 文件信息
     * */
    SysFileInfo queryByDataIdServer(String dataId, String serverType);
    /**
     * 批量获取
     * @param ids id集合
     * @return 列表
     * */
    List<SysFileInfo> getListByIds(String[] ids);
}
