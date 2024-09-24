package com.inspur.doc.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import com.inspur.doc.domain.SysFileInfo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName IFileService
 * @date 2024/8/2 11:37
 */
public interface IFileService {

    /**
     * 单文件上传
     *
     * @param file 文件内容
     * @return 上传后文件信息
     */
    SysFileInfo uploadFile(MultipartFile file);

    /**
     * 多文件上传
     *
     * @param files 文件内容
     * @return 上传后文件信息
     */
    List<SysFileInfo> uploadFiles(MultipartFile[] files);


    /**
     * 获取文件预览地址
     *
     * @param fileId 文件id
     * @return 地址
     */
    String preview(String fileId);


    /**
     * 下载文件
     *
     * @param fileId 系统存储文件id，sysFileInfo的id
     * @return 二进制字节
     */
    ResponseEntity<byte[]> download(String fileId);
}
