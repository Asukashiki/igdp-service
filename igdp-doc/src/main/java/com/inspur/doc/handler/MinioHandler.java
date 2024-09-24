package com.inspur.doc.handler;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.inspur.common.utils.file.ImageUtils;
import com.inspur.common.utils.file.MimeTypeUtils;
import com.inspur.doc.domain.SysFileInfo;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;
import javax.xml.ws.WebServiceException;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName MinioHandler
 * @date 2024/8/5 9:46
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MinioHandler {

    @Value("${sys.doc.minio.bucket-name:''}")
    String bucketName;

    private final MinioClient minioClient;

    /**
     * description: 判断bucket是否存在，不存在则创建
     */
    public void existBucket(String name) {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(name).build());
            }
        } catch (Exception e) {
            log.info("创建bucket失败", e);
        }
    }

    public void uploadWithName(MultipartFile file, String fileName) {
        try (InputStream in = file.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(in, in.available(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
        } catch (Exception e) {
            log.info("上传文件失败", e);
            throw new WebServiceException("上传文件失败");
        }
    }


    /**
     * 从Minio读取文件并返回File对象，返回后删除文件
     */
    public File readTempFileFromMinio(String fileName) throws Exception {

        String suffix = FilenameUtils.getExtension(fileName);

        File file = File.createTempFile("tmp", "." + suffix);
        InputStream in = null;
        try {
            in = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());

            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = in.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        } catch (Exception e) {
            log.info("下载文件失败", e);
            throw new WebServiceException("获取文件失败");
        } finally {
            deleteByName(fileName);
            IoUtil.close(in);
            IoUtil.close(in);
        }
        return file;
    }

    public void deleteByName(String fileName) {
        try {
            // 删除对象
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
            log.info("成功删除对象");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("minio文件删除异常", e);
        }
    }

    /**
     * description: 上传文件
     */
    public String upload(MultipartFile file) throws IOException {
        //通过后缀名校验
        String extension = getExtension(file);
        assertAllowed(extension, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        //根据文件流获取文件类型校验
        String fileType = FileTypeUtil.getType(file.getInputStream());
        assertAllowed(fileType, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);

        String fileName = file.getOriginalFilename();
        String suffixName = FileNameUtil.getSuffix(fileName);
        fileName = UUID.randomUUID().toString(true);
        if (StrUtil.isNotBlank(suffixName)) {
            fileName = fileName + "." + suffixName;
        }

        InputStream inputStream;

        //图片上传，则重写图片，防止木马注入
        if (Arrays.binarySearch(MimeTypeUtils.IMAGE_EXTENSION, suffixName) > 0) {
            inputStream = ImageUtils.changeSize(file.getInputStream());
        } else {
            inputStream = file.getInputStream();
        }
        if (null == inputStream) {
            throw new RuntimeException("图片文件解析失败");
        }

        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
        } catch (Exception e) {
            log.info("上传文件失败", e);
            throw new WebServiceException("上传文件失败");
        } finally {
            inputStream.close();
        }
        return fileName;
    }

    /**
     * description: 下载文件
     *
     * @param fileName     文件存储名称
     * @param preview      预览标识
     * @param originalName 文件原名
     */
    public ResponseEntity<byte[]> download(String fileName, boolean preview, String originalName) {
        ResponseEntity<byte[]> responseEntity;
        InputStream in = null;
        ByteArrayOutputStream out;
        try {
            StatObjectResponse statObjectResponse = minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(fileName).build());
            String contentType = statObjectResponse.contentType();

            in = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
            out = new ByteArrayOutputStream();
            IOUtils.copy(in, out);
            // 文件名称
            fileName = StrUtil.isEmpty(originalName) ? fileName : originalName;
            // 封装返回值
            byte[] bytes = out.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            if (preview) {
                headers.add("Content-Disposition", "inline;filename=" + UriUtils.encode(fileName, Constants.UTF_8));
                headers.set(HttpHeaders.CONTENT_TYPE, contentType + ";charset=UTF-8");
            } else {
                headers.add("Content-Disposition", "attachment;filename=" + UriUtils.encode(fileName, Constants.UTF_8));
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            }
            headers.setContentLength(bytes.length);
            headers.setAccessControlExposeHeaders(Arrays.asList("*"));
            responseEntity = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.info("下载文件失败", e);
            throw new WebServiceException("获取文件失败");
        } finally {
            IoUtil.close(in);
            IoUtil.close(in);
        }
        return responseEntity;
    }


    public String preview(String fileName) {
        // 查看文件地址
        GetPresignedObjectUrlArgs build = GetPresignedObjectUrlArgs.builder().bucket(bucketName).method(Method.GET).object(fileName).build();
        String url = null;
        try {
            url = minioClient.getPresignedObjectUrl(build);
        } catch (Exception e) {
            log.info("预览文件失败", e);
        }
        return url;
    }


    private void assertAllowed(String extension, String[] allowedExtension) throws IOException {
        if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension)) {
            if (allowedExtension == MimeTypeUtils.IMAGE_EXTENSION) {
                throw new RuntimeException("图片格式异常");
            } else if (allowedExtension == MimeTypeUtils.FLASH_EXTENSION) {
                throw new RuntimeException("flash格式异常");
            } else if (allowedExtension == MimeTypeUtils.MEDIA_EXTENSION) {
                throw new RuntimeException("音频格式异常");
            } else if (allowedExtension == MimeTypeUtils.VIDEO_EXTENSION) {
                throw new RuntimeException("视频格式异常");
            } else {
                throw new RuntimeException("文件格式异常");
            }
        }
    }


    /**
     * 校验文件内容是否包含高位字符
     */
    private static boolean isSafe(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        String str = IoUtil.readHex(inputStream, 1000000, false);
        // 匹配16进制中的 <% ( ) %>
        // 匹配16进制中的 <? ( ) ?>
        // 匹配16进制中的 <script | /script> 大小写亦可
        // 通过匹配十六进制代码检测是否存在木马脚本
        String pattern = "(3c25.*?28.*?29.*?253e)|(3c3f.*?28.*?29.*?3f3e)|(3C534352495054)|(2F5343524950543E)|(3C736372697074)|(2F7363726970743E)";
        Pattern mPattern = Pattern.compile(pattern);
        Matcher mMatcher = mPattern.matcher(str);

        // 查找相应的字符串
        boolean flag = true;
        if (mMatcher.find()) {
            //过滤java关键字(java import String print write( read() php request alert system)（暂时先这样解决，这样改动最小，以后想后更好的解决方案再优化）
            String keywordPattern = "(6a617661)|(696d706f7274)|(537472696e67)|(7072696e74)|(777269746528)|(726561642829)|(706870)|(72657175657374)|(616c657274)|(73797374656d)";
            Pattern keywordmPattern = Pattern.compile(keywordPattern);
            Matcher keywordmMatcher = keywordmPattern.matcher(str);
            if (keywordmMatcher.find()) {
                flag = false;
            }
        }
        return flag;

    }

    /**
     * 判断MIME类型是否是允许的MIME类型
     */
    public static boolean isAllowedExtension(String extension, String[] allowedExtension) {
        if (null != allowedExtension) {
            for (String str : allowedExtension) {
                if (str.equalsIgnoreCase(extension)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getExtension(MultipartFile file) throws IOException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StrUtil.isEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(Objects.requireNonNull(file.getContentType()));
        }
        return extension;
    }
}
