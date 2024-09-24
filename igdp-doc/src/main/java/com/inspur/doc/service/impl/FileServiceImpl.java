package com.inspur.doc.service.impl;

import com.inspur.common.constant.Constants;
import com.inspur.common.utils.StringUtils;
import com.inspur.doc.domain.SysFileInfo;
import com.inspur.doc.service.IEstDocService;
import com.inspur.doc.service.IFileService;
import com.inspur.doc.service.IMinioService;
import com.inspur.doc.service.ISysFileInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName fileService
 * @date 2024/7/5 15:21
 */
@Service("fileService")
@Slf4j
@RefreshScope
public class FileServiceImpl implements IFileService {
    @Value("${sys.doc.server.type:''}")
    private String serverType;
    @Resource
    private ISysFileInfoService fileInfoService;
    @Resource
    private IMinioService minioService;
    @Resource
    private IEstDocService estDocService;


    /**
     * 根据serverType调用不同的文件上传处理服务
     */
    @Override
    public List<SysFileInfo> uploadFiles(MultipartFile[] files) {

        if (StringUtils.isEmpty(serverType)) {
            throw new RuntimeException("文件服务类型为空");
        }
        switch (serverType) {
            case Constants.FILE_SERVER_TYPE_MINIO:
                return minioService.uploadFiles(files);
            case Constants.FILE_SERVER_TYPE_EST:
                return estDocService.uploadFiles(files);
            default:
                throw new RuntimeException("文件服务类型不匹配");
        }
    }

    /**
     * 根据serverType调用不同的文件上传处理服务
     */
    @Override
    public SysFileInfo uploadFile(MultipartFile file) {

        if (StringUtils.isEmpty(serverType)) {
            throw new RuntimeException("文件服务类型为空");
        }
        SysFileInfo fileInfo;
        switch (serverType) {
            case Constants.FILE_SERVER_TYPE_MINIO:
                fileInfo = minioService.uploadFile(file);
                break;
            case Constants.FILE_SERVER_TYPE_EST:
                fileInfo = estDocService.uploadFile(file);
                break;
            default:
                throw new RuntimeException("文件服务类型不匹配");
        }
        if (null != fileInfo) {
            fileInfoService.saveSysFileInfo(fileInfo);
        }
        return fileInfo;
    }

    /**
     * description: 预览文件
     *
     * @param fileId 文件id
     * @return url
     */
    @Override
    public String preview(String fileId) {
        if (StringUtils.isEmpty(serverType)) {
            throw new RuntimeException("文件服务类型为空");
        }
        if (serverType.equals(Constants.FILE_SERVER_TYPE_MINIO)) {
            return minioService.preview(fileId);
        }
        throw new RuntimeException("文件服务类型不匹配");
    }

    /**
     * 下载文件
     */
    @Override
    public ResponseEntity<byte[]> download(String fileId) {
        if (StringUtils.isEmpty(serverType)) {
            throw new RuntimeException("文件服务类型为空");
        }
        SysFileInfo fileInfo = fileInfoService.queryById(fileId);
        if (null == fileInfo) {
            throw new RuntimeException("文件信息不存在");
        }
        switch (serverType) {
            case Constants.FILE_SERVER_TYPE_MINIO:
                return minioService.download(fileInfo);
            case Constants.FILE_SERVER_TYPE_EST:
                return estDocService.download(fileInfo);
            default:
                throw new RuntimeException("文件服务类型不匹配");
        }
    }
}
