package com.inspur.doc.service;

import com.inspur.doc.domain.SysFileInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * minio文件存储接口
 *
 * @author liyunlong
 * @version 1.0
 * @ClassName IMinioService
 * @date 2024/8/2 11:38
 */
public interface IMinioService {
    /**
     * 上传单个文件
     *
     * @param file 文件内容
     * @return 系统存储文件内容
     */
    SysFileInfo uploadFile(MultipartFile file);

    /**
     * 上传多个文件
     *
     * @param files 文件集合
     * @return 结果集合
     */
    List<SysFileInfo> uploadFiles(MultipartFile[] files);

    /**
     * 文件下载
     *
     * @param fileInfo     文件信息
     * @return 字节
     */
    ResponseEntity<byte[]>  download(SysFileInfo fileInfo);

    /**
     * 文件预览
     *
     * @param fileId 文件id
     * @return 预览地址
     */
    String preview(String fileId);

    /**
     * 文件删除
     *
     * @param fileId 文件id
     */
    void delete(String fileId);
}
