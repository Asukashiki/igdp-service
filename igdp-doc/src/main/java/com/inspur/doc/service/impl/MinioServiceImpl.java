package com.inspur.doc.service.impl;

import cn.hutool.core.io.file.FileNameUtil;
import com.inspur.common.constant.Constants;
import com.inspur.doc.domain.SysFileInfo;
import com.inspur.doc.handler.MinioHandler;
import com.inspur.doc.service.IMinioService;
import com.inspur.doc.service.ISysFileInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName MinioServiceImpl
 * @date 2024/8/2 11:50
 */
@Service("minioService")
@Slf4j
@RefreshScope
@RequiredArgsConstructor
public class MinioServiceImpl implements IMinioService {

    @Value("${sys.doc.minio.bucket-name:''}")
    String bucketName;

    @Resource
    private MinioHandler minioHandler;
    @Resource
    private ISysFileInfoService sysFileInfoService;


    /**
     * 上传多个文件
     */
    @Override
    public List<SysFileInfo> uploadFiles(MultipartFile[] files) {
        try {
            if (null != files && files.length > 0) {
                List<SysFileInfo> fileInfos = new ArrayList<>(files.length);
                for (MultipartFile file : files) {
                    String fileName = minioHandler.upload(file);
                    SysFileInfo fileInfo = new SysFileInfo(fileName, fileName, file.getOriginalFilename(), null, FileNameUtil.getSuffix(file.getOriginalFilename()), Constants.FILE_SERVER_TYPE_MINIO);
                    fileInfos.add(fileInfo);
                }
                sysFileInfoService.saveList(fileInfos);
                return fileInfos;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 上传单个文件
     */
    @Override
    public SysFileInfo uploadFile(MultipartFile file) {
        try {
            String fileName = minioHandler.upload(file);
            return new SysFileInfo(fileName, fileName, file.getOriginalFilename(), null, FileNameUtil.getSuffix(file.getOriginalFilename()), Constants.FILE_SERVER_TYPE_MINIO);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }

    }


    /**
     * 文件下载
     */
    @Override
    public ResponseEntity<byte[]> download(SysFileInfo fileInfo) {
        ResponseEntity<byte[]> responseEntity;
        try {
            responseEntity = minioHandler.download(fileInfo.getDataId(), true, fileInfo.getOriginalFileName());
            return responseEntity;
        } catch (Exception e) {
            log.info("下载文件失败", e);
            throw new RuntimeException("获取文件失败");
        }
    }


    /**
     * description: 预览文件
     *
     * @return url
     */
    @Override
    public String preview(String fileId) {
        SysFileInfo fileInfo = sysFileInfoService.queryById(fileId);
        if (null == fileInfo) {
            throw new RuntimeException("文件信息不存在");
        }
        return minioHandler.preview(fileInfo.getDataId());
    }


    /**
     * description: 删除文件
     *
     * @param fileId 文件ID
     */
    @Override
    public void delete(String fileId) {
        SysFileInfo fileInfo = sysFileInfoService.queryById(fileId);
        if (null == fileInfo) {
            throw new RuntimeException("文件信息不存在");
        }
        try {
            minioHandler.deleteByName(fileInfo.getDataId());
        } catch (Exception e) {
            log.info("删除文件失败", e);
            throw new RuntimeException("删除文件失败！");
        }
    }
}
