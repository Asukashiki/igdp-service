package com.inspur.doc.service;

import com.inspur.doc.domain.SysFileInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 浪潮-成都退役资源中心文件上传服务实现
 *
 * @author liyunlong
 * @version 1.0
 * @ClassName IEstDocService
 * @date 2024/8/2 11:38
 */
public interface IEstDocService {

    /**
     * 获取上传签名
     *
     * @param fileName          重命名的文件名，如果传空，将使用file的原始文件名
     * @param multipartFileName 文件名
     * @param fileSize          文件大小
     * @param timestamp         时间戳
     * @return 签名内容
     */
    String preUpload(String fileName, String multipartFileName, String fileSize, long timestamp);

    /**
     * 下载签名
     *
     * @param fileId    文件id
     * @param timestamp 时间戳
     * @return 签名
     */
    String preDownload(String fileId, long timestamp);

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
     * @param fileInfo 文件信息
     * @return 字节
     */
    ResponseEntity<byte[]> download(SysFileInfo fileInfo);

    /**
     * 文件预览
     *
     * @param fileId 文件ID
     * @return 预览地址
     */
    String preview(String fileId);

    /**
     * 文件删除
     *
     * @param fileId 文件ID
     */
    void delete(String fileId);
}
