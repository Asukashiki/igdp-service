package com.inspur.doc.service.impl;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.inspur.common.constant.CacheConstants;
import com.inspur.common.constant.Constants;
import com.inspur.common.core.redis.RedisCache;
import com.inspur.common.utils.StringUtils;
import com.inspur.doc.domain.SysFileInfo;
import com.inspur.doc.service.IEstDocService;
import com.inspur.doc.service.ISysFileInfoService;
import com.inspur.doc.util.EstSign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * 浪潮-成都退役资源中心文件上传服务实现
 *
 * @author liyunlong
 * @version 1.0
 * @ClassName EstDocService
 * @date 2024/8/2 10:32
 */
@Service("estDocService")
@Slf4j
@RefreshScope
public class EstDocServiceImpl implements IEstDocService {


    @Value("${sys.doc.est.server-uri:''}")
    private String serverUri;
    @Value("${sys.doc.est.client.group-id:''}")
    private String groupId;
    @Value("${sys.doc.est.client.group-secret:''}")
    private String groupSecret;
    @Value("${sys.doc.est.client.file-path:''}")
    private String filePath;
    @Value("${sys.doc.est.client.api.token:''}")
    private String tokenApi;
    @Value("${sys.doc.est.client.api.upload:''}")
    private String uploadApi;
    @Value("${sys.doc.est.client.api.preUpload:''}")
    private String preUploadApi;
    @Value("${sys.doc.est.client.api.download:''}")
    private String downloadApi;
    @Value("${sys.doc.est.client.api.preDownload:''}")
    private String preDownloadApi;
    @Value("${sso.cheng-du-tui-yi.server:'http://10.110.148.2:88'}")
    private String ssoServer;
    @Value("${sso.cheng-du-tui-yi.client-id:'1b8539b861074e639cd39c9cf30c6c26'}")
    private String clientId;
    @Value("${sso.cheng-du-tui-yi.client-secret:'d21b56502bb0497aa560ece99bd9a919'}")
    private String clientSecret;

    @Resource
    private ISysFileInfoService sysFileInfoService;

    @Resource
    private RedisCache redisCache;


    /**
     * 获取上传签名
     */
    @Override
    public String preUpload(String fileName, String multipartFileName, String fileSize, long timestamp) {
        String content = "groupId=" + this.groupId + "&timestamp=" + timestamp +
                "&userId=" + clientId +
                "&filePath=" + filePath + "&multipartFileName=" + multipartFileName + "&fileSize=" + fileSize + "&fileName=" + (fileName == null ? "" : fileName);
        return EstSign.sign(this.groupSecret, content);
    }

    @Override
    public String preDownload(String fileId, long timestamp) {
        String content = "groupId=" + this.groupId + "&timestamp=" + timestamp + "&userId=" + this.clientId + "&fileId=" + fileId;
        return EstSign.sign(this.groupSecret, content);
    }

    @Override
    public SysFileInfo uploadFile(MultipartFile file) {

        // 调用接口上传文件
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.setBearerAuth(getAccessToken());
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("filePath", filePath);
        String suffixName = FileNameUtil.getSuffix(file.getOriginalFilename());
        String fileName = IdUtil.fastSimpleUUID();
        if (StrUtil.isNotBlank(suffixName)) {
            fileName = fileName + "." + suffixName;
        }
        long timestamp = System.currentTimeMillis();
        param.add("fileName", fileName);
        param.add("file", file.getResource());
        param.add("groupId", groupId);
        param.add("timestamp", timestamp);
        param.add("sign", preUpload(fileName, file.getOriginalFilename(), String.valueOf(file.getSize()), timestamp));

        headers.setContentType(MediaType.parseMediaType("multipart/form-data;charset=UTF-8"));
        HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(param, headers);
        String docUploadUrl = this.serverUri + uploadApi;
        ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(docUploadUrl, formEntity, JSONObject.class);
        if (responseEntity.getStatusCode().value() == 200) {
            System.out.println(responseEntity.getBody());
            JSONObject jsonObject = responseEntity.getBody();
            if (jsonObject.getInt("status") == 0) {
                JSONObject dataObject = jsonObject.getJSONObject("data");
                String id = dataObject.getStr("id");
                return new SysFileInfo(id, fileName, file.getOriginalFilename(), id, suffixName, Constants.FILE_SERVER_TYPE_EST);
            }
        }
        return null;
    }

    @Override
    public List<SysFileInfo> uploadFiles(MultipartFile[] files) {
        return Collections.emptyList();
    }

    @Override
    public ResponseEntity<byte[]> download(SysFileInfo fileInfo) {
        String fileId = fileInfo.getDataId();
        ResponseEntity<byte[]> responseEntity;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());
        headers.add("Content-Type", "application/json;charset=utf-8");
        headers.set("Content-Disposition", "attachment;filename=" + UriUtils.encode(fileInfo.getOriginalFileName(), StandardCharsets.UTF_8));
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        String fileName = "测试文件.docx";
        try {
            new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        long timestamp = System.currentTimeMillis();
        String sign = preDownload(fileId, timestamp);
        String downloadUrl = this.serverUri + downloadApi + "?fileId=" + fileId
                + "&groupId=" + this.groupId + "&sign=" + sign
                + "&timestamp=" + timestamp;
        RestTemplate restTemplate = new RestTemplate();
        URI uri = URI.create(downloadUrl);
        MultiValueMap<String, Object> forms = new LinkedMultiValueMap<String, Object>();
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(forms, headers);
        responseEntity = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, byte[].class);
        return responseEntity;
    }

    @Override
    public String preview(String fileId) {
        return "";
    }

    @Override
    public void delete(String fileId) {

    }


    private String getAccessToken() {
        String accessToken = null;
        String key = CacheConstants.CHENGDU_CLIENT_ACCESS_TOKEN_KEY + this.clientId;
        Boolean hasKey = redisCache.hasKey(key);
        if (null != hasKey && hasKey) {
            accessToken = redisCache.getCacheObject(key);
        }
        if (null == accessToken) {
            String paramStr = "?grant_type=client_credentials";
            String loginUrl = ssoServer + tokenApi + paramStr;
            String authorization = "Basic " + org.apache.commons.codec.binary.Base64.encodeBase64String((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));
            Map<String, String> headers = new HashMap<>(1);
            headers.put("Content-Type", "application/json;charset=UTF-8");
            headers.put("Authorization", authorization);
            String result = HttpRequest.post(loginUrl).addHeaders(headers).execute().body();
            JSONObject tokenObject = JSONUtil.parseObj(result);
            accessToken = tokenObject.getStr("access_token");
            if (StringUtils.isNotEmpty(accessToken)) {
                long expiresIn = tokenObject.getLong("expires_in");
                redisCache.setCacheObject(key, accessToken);
                redisCache.expire(key, expiresIn, TimeUnit.SECONDS);
            }
        }
        return accessToken;
    }
}
