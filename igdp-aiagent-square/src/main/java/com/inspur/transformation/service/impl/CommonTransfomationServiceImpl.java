package com.inspur.transformation.service.impl;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.core.domain.AjaxResult;
import com.inspur.transformation.domain.DifyUserRelationEntity;
import com.inspur.transformation.mapper.IDifyUserReleationMapper;
import com.inspur.transformation.service.IDifyUserReleationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 公共转换服务
 */
@Service

public class CommonTransfomationServiceImpl extends ServiceImpl<IDifyUserReleationMapper, DifyUserRelationEntity> implements IDifyUserReleationService {
    private static Logger logger = LoggerFactory.getLogger(CommonTransfomationServiceImpl.class);
    private final IDifyUserReleationMapper difyUserReleationMapper;
    private String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiN2JkYTU1YTMtNWFhNS00ZTdkLWIxZmYtNDJlMDIwZGE3NDNmIiwiZXhwIjoxNzI5OTE0MzUyLCJpc3MiOiJTRUxGX0hPU1RFRCIsInN1YiI6IkNvbnNvbGUgQVBJIFBhc3Nwb3J0In0.8MAkylpdwRHUOmfoHY4ZWY4Ds0T0Yi-fkGUzju-xovU";
    private String workspaceId = "6326afba-ca97-47f1-b02c-3897cb5694e8";
    private String difyAddress = "http://10.110.149.140:30099";
    private String difylogin = "http://10.110.149.140:30099/dify/console/api/login";
    private final RestTemplate restTemplate;

    public CommonTransfomationServiceImpl(IDifyUserReleationMapper difyUserReleationMapper, RestTemplateBuilder restTemplateBuilder) {
        this.difyUserReleationMapper = difyUserReleationMapper;
        this.restTemplate = restTemplateBuilder.build();

    }


    @Override
    public AjaxResult commonCommit(HttpServletRequest request, HttpServletResponse response) {
        String url = rebuildUlr(request);
//        String host = request.getServerName();
//        int port = request.getServerPort();
        ResponseEntity<Resource> responseEntity;
        url =difyAddress.concat(url.replace("/igdp/" , "/"));
//        String type = getTypeByUrl(url);
        logger.error("========url======{}",url);
        //直接转发
        RequestEntity requestEntity = null;
        try {
            requestEntity = buildRequestEntity(url, request);

            responseEntity = restTemplate.exchange(requestEntity, Resource.class);
            putResponseHeader(response, responseEntity.getHeaders());
            ServletOutputStream outputStream = response.getOutputStream();
            InputStream inputStream = responseEntity.getBody().getInputStream();
            int bytesRead;
            for (byte[] buffer = new byte[4096]; (bytesRead = inputStream.read(buffer)) != -1; ) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();


        } catch (Exception e) {
            if(e.getMessage().startsWith("401")){
                JSONObject loginJson = new JSONObject();
                loginJson.putOnce("email","yymaas@inspur.com");
                loginJson.putOnce("password","!QAZ2wsx.");
                loginJson.putOnce("remember_me",true);
              HttpResponse response1= HttpUtil.createPost(difylogin)
                        .body(loginJson.toString())
                        .execute();
              if(response1.isOk()){
                  apiKey = JSONUtil.parseObj(response1.body()).getStr("data");
                  commonCommit(request,response);
              }
            }
            response.setStatus(500);
            e.printStackTrace();
            return AjaxResult.error("失败");
        }
        logger.error("============response body============:"+responseEntity.getBody());
        return AjaxResult.success(responseEntity.getBody());
    }

    private void putHeadersMap(HttpServletResponse response, Map<String, List<String>> headers) {
        response.setHeader("X-Frame-Options" , "SAMEORIGIN");
        headers.forEach((k, v) -> {
            if (ObjectUtil.isNotNull(k)) {
                response.setHeader(k, v.stream().collect(Collectors.joining(", ")));
            }
        });
    }

    private RequestEntity buildRequestEntity(String url, HttpServletRequest request) throws IOException {
        //获取 所有heads
        HttpHeaders headers = parseHeader(request);
        //单独处理文件上传
        if (isMultipart(request)) {
            RequestEntity formData = getFormDataEntity(url, request, headers);
            if (formData != null) {
                return formData;
            }
            throw new RuntimeException("");
        }
        headers.remove("Authorization");
        headers.add("Authorization" , "Bearer "+apiKey);
        headers.add("X-WORKSPACE-ID" , workspaceId);
        //这里获取不到 form-data 中数据，只能获取,requestBody, form-urlencoded-www参数
        byte[] body = parseBody(request);
        logger.error("request: {}" , new String(body));
        return new RequestEntity(body, headers, HttpMethod.resolve(request.getMethod()), URI.create(url));
    }


    private RequestEntity getFormDataEntity(String url, HttpServletRequest request, HttpHeaders headers) throws IOException {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultiValueMap<String, MultipartFile> multiFileMap = multipartHttpServletRequest.getMultiFileMap();
        //处理文件
        MultiValueMap formData = new LinkedMultiValueMap();
        for (Map.Entry<String, List<MultipartFile>> entry : multiFileMap.entrySet()) {
            String key = entry.getKey();
            for (MultipartFile multipartFile : entry.getValue()) {
                ByteArrayResource fileResource = new ByteArrayResource(multipartFile.getBytes()) {
                    @Override
                    public long contentLength() {
                        return multipartFile.getSize();
                    }

                    @Override
                    public String getFilename() {
                        return multipartFile.getOriginalFilename();
                    }
                };
                formData.add(key, fileResource);
            }
        }
        //处理 form-data 中非 文件类型参数
        Map<String, String[]> parameterMap = multipartHttpServletRequest.getParameterMap();
        for (Map.Entry<String, String[]> stringEntry : parameterMap.entrySet()) {
            for (String s : stringEntry.getValue()) {
                formData.add(stringEntry.getKey(), s);
            }
        }
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return new RequestEntity(formData, headers, HttpMethod.resolve(request.getMethod()), URI.create(url));
    }

    private boolean isMultipart(HttpServletRequest request) {
        return request instanceof MultipartHttpServletRequest;
    }

    private HttpHeaders parseHeader(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
//        headers.add("X-Frame-Options", "SAMEORIGIN");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            Enumeration<String> v = request.getHeaders(headerName);
            List<String> arr = new ArrayList<>();
            while (v.hasMoreElements()) {
                arr.add(v.nextElement());
            }
            headers.addAll(headerName, arr);
        }
        return headers;
    }

    private byte[] parseBody(HttpServletRequest request) throws IOException {
        return StreamUtils.copyToByteArray(request.getInputStream());
    }

    private String rebuildUlr(HttpServletRequest request) {
        String query = request.getQueryString();
        return request.getRequestURI().replace("//" , "/").concat(query != null ? "?".concat(query) : "");
    }

    private void putResponseHeader(HttpServletResponse response, HttpHeaders headers) {
        headers.forEach((k, v) -> {
            response.setHeader(k, v.stream().collect(Collectors.joining(", ")));
        });
        response.setHeader("X-Frame-Options" , "SAMEORIGIN");
    }

    private String getTypeByUrl(String url) {
        if (url.endsWith("/console/api/apps")) {
            return "0";
        } else {
            return null;
        }
    }

}
