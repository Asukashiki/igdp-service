package com.inspur.web.controller.common;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import com.inspur.doc.service.IFileService;
import com.inspur.doc.domain.SysFileInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.inspur.common.config.SystemConfig;
import com.inspur.common.constant.Constants;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.common.utils.StringUtils;
import com.inspur.common.utils.file.FileUtils;

/**
 * 通用请求处理
 *
 * @author liyunlong
 */
@RestController
@RequestMapping("/common")
public class CommonController {
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Resource
    private IFileService fileService;

    /**
     * 通用下载请求
     *
     * @param fileId 文件Id
     */
    @GetMapping("/download")
    public ResponseEntity<byte[]> fileDownload(String fileId) {
        return fileService.download(fileId);
    }

    /**
     * 通用上传请求（多个）
     */
    @PostMapping("/uploads")
    public AjaxResult uploadFile(MultipartFile[] files) throws Exception {
        try {
            List<SysFileInfo> fileInfos = fileService.uploadFiles(files);
            return AjaxResult.success(fileInfos);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 通用上传请求（单个）
     */
    @PostMapping("/upload")
    public AjaxResult uploadFile(MultipartFile file) throws Exception {
        SysFileInfo fileInfo = fileService.uploadFile(file);
        return AjaxResult.success(fileInfo);
    }

    /**
     * 获取文件预览链接
     */
    @GetMapping("/preview/{fileId}")
    public AjaxResult preview(@PathVariable("fileId") String fileId) {
        String url = fileService.preview(fileId);
        return AjaxResult.success(url);
    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/download/resource")
    public void resourceDownload(String resource, HttpServletResponse response) {
        try {
            if (!FileUtils.checkAllowDownload(resource)) {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
            }
            // 本地资源路径
            String localPath = SystemConfig.getProfile();
            // 数据库资源地址
            String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }
}
