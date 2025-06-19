package com.inspur.transformation.service.impl;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.common.core.domain.model.LoginUser;
import com.inspur.common.utils.LoginHelper;
import com.inspur.transformation.domain.DifyUserRelationEntity;
import com.inspur.transformation.httputil.OkHttpSSEListener;
import com.inspur.transformation.mapper.IDifyUserReleationMapper;
import com.inspur.transformation.service.IProxyService;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * @author lijieming
 * @date 2024/10/2
 */
@Service

public class ProxyServiceImpl extends ServiceImpl<IDifyUserReleationMapper, DifyUserRelationEntity> implements IProxyService {
    private static Logger logger = LoggerFactory.getLogger(ProxyServiceImpl.class);
    private final IDifyUserReleationMapper difyUserReleationMapper;
    private String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiN2JkYTU1YTMtNWFhNS00ZTdkLWIxZmYtNDJlMDIwZGE3NDNmIiwiZXhwIjoxNzQ0OTY1MzQwLCJpc3MiOiJTRUxGX0hPU1RFRCIsInN1YiI6IkNvbnNvbGUgQVBJIFBhc3Nwb3J0In0.52yc5cxmEVp0aatGDNf-rwA5K3Lcjm1M8AqkW0_l0PI";
    @Value("${proxyBaseUrl.dify:}")
    private String difyBaseUrl;
    @Value("${proxyBaseUrl.labelStudio:}")
    private String labelStudioBaseUrl;
    private String difylogin = "/dify/console/api/login";
    private String labelloginGet = "/labelstudio/labelstudio/user/login/";
    private String labelloginIgdpLogin = "/labelstudio/user/user-login-igdp/";
    private final RestTemplate restTemplate;

    public ProxyServiceImpl(IDifyUserReleationMapper difyUserReleationMapper, RestTemplateBuilder restTemplateBuilder) {
        this.difyUserReleationMapper = difyUserReleationMapper;
        this.restTemplate = restTemplateBuilder.build();

    }


    @Override
    public Object difyProxy(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        String url = rebuildUlr(httpServletRequest);

        ResponseEntity<Resource> responseEntity;
        url = difyBaseUrl.concat(url.replace("/igdp/", "/"));
        logger.error("========url======{}", url);

        return difyProxyHttp(httpServletRequest, response, url);


    }

    @Override

    public void draftRun(HttpServletRequest httpServletRequest, HttpServletResponse response, SseEmitter emitter) {
        String url = rebuildUlr(httpServletRequest);
        url = difyBaseUrl.concat(url.replace("/igdp/", "/"));
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1000, TimeUnit.SECONDS)
                .writeTimeout(1000, TimeUnit.SECONDS)
                .readTimeout(1000, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = null;
        Headers headers = parseOkHhttpHeader(httpServletRequest);
        try {
            StringBuilder bodyBuilder = new StringBuilder();
            String line;
            BufferedReader reader = httpServletRequest.getReader();
            while ((line = reader.readLine()) != null) {
                bodyBuilder.append(line);
            }
            String body = bodyBuilder.toString();


            requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"), body);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .post(requestBody)
                .build();
        CountDownLatch countDownLatch = new CountDownLatch(1);

        EventSource.Factory factory = EventSources.createFactory(client);
        StringBuffer output = new StringBuffer();
        try {
            OkHttpSSEListener listener = new OkHttpSSEListener(emitter);
            // 创建事件
            EventSource eventSource = factory.newEventSource(request, listener);
            countDownLatch.await();
        } catch (Exception e) {
            String errmsg = e.getMessage();
            if (errmsg.startsWith("401")) {
                JSONObject loginJson = new JSONObject();
                loginJson.putOnce("email", "yymaas@inspur.com");
                loginJson.putOnce("password", "!QAZ2wsx.");
                loginJson.putOnce("remember_me", true);
                HttpResponse response1 = HttpUtil.createPost(difyBaseUrl+difylogin)
                        .body(loginJson.toString())
                        .execute();
                if (response1.isOk()) {
                    apiKey = JSONUtil.parseObj(response1.body()).getStr("data");
                    draftRun(httpServletRequest, response, emitter);
                }
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object labelStudioProxy(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        String url = rebuildUlr(httpServletRequest);

        ResponseEntity<Resource> responseEntity;
        url = difyBaseUrl.concat(url.replace("/igdp/", "/"));
        logger.error("========url======{}", url);

        return labelStudioProxyHttp(httpServletRequest, response, url);
    }

    @Override
    public Object labelStudioProxyLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        LoginUser user = LoginHelper.getLoginUser();
        cn.hutool.http.HttpRequest request = cn.hutool.http.HttpRequest.get(labelStudioBaseUrl+labelloginGet);
        // 发送请求并获取登录页返回的seesionid和 csrftoken
        HttpResponse response = request.execute();

        List<String> setCookie = response.headers().get("Set-Cookie");
        System.out.println("Set-Cookie: " + setCookie);
        HttpRequest loginRequest = HttpRequest.post(labelStudioBaseUrl+labelloginIgdpLogin);
        Map<String, Object> params = new HashMap<>();
        params.put("email", user.getUsername() + "@inspur.com");
        params.put("username", user.getUsername());
        params.put("password", user.getUsername() + "yanyu");
        params.put("orgName", user.getDeptName());
        Map<String, String> headers = new HashMap<>();
        for (int i = 0; i < setCookie.size(); i++) {
            headers.put("Set-Cookie", setCookie.get(i));
        }
        String paramsJson = JSONUtil.toJsonStr(params);
        HttpResponse loginResponse = loginRequest
                .body(paramsJson)
                .addHeaders(headers)
                .execute();


        ServletOutputStream outputStream = null;
        try {
            outputStream = httpServletResponse.getOutputStream();
            InputStream inputStream = loginResponse.bodyStream();
            int bytesRead;
            for (byte[] buffer = new byte[4096]; (bytesRead = inputStream.read(buffer)) != -1; ) {
                outputStream.write(buffer, 0, bytesRead);
            }
            setResponseHeaders(loginResponse, httpServletResponse);
            outputStream.flush();
            log.error("httpServletResponse header: " + httpServletResponse.getHeaders("Set-Cookie"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


        return JSONUtil.parseObj(loginResponse.body());
    }

    private void setResponseHeaders(HttpResponse loginResponse, HttpServletResponse httpServletResponse) {
        Map<String, List<String>> headers = loginResponse.headers();
        Iterator<Map.Entry<String, List<String>>> entries = headers.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, List<String>> entry = entries.next();
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            for (int i = 0; i < entry.getValue().size(); i++) {
                httpServletResponse.addHeader(entry.getKey(), entry.getValue().get(i));
            }
        }

    }

    private Map labelStudioProxyHttp(HttpServletRequest request, HttpServletResponse response, String url) {
        ResponseEntity<Resource> responseEntity;
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
            String errmsg = e.getMessage();
            log.error("labelstudio proxy error:+  " + errmsg);
            e.printStackTrace();
            return null;
        }
        logger.error("============response body============:" + responseEntity.getBody().toString());
        return new HashMap();
    }

    private Map difyProxyHttp(HttpServletRequest request, HttpServletResponse response, String url) {
        ResponseEntity<Resource> responseEntity;
        //直接转发
        RequestEntity requestEntity = null;

        try {
            requestEntity = buildRequestEntity(url, request);

            responseEntity = restTemplate.exchange(requestEntity, Resource.class);
            // 关键修改：设置HTTP状态码为后端返回的状态码
            int statusCode = responseEntity.getStatusCodeValue();
            response.setStatus(statusCode);
            logger.error("=================== url：{} ",url);
            putResponseHeader(response, responseEntity.getHeaders());
            ServletOutputStream outputStream = response.getOutputStream();
            InputStream inputStream = responseEntity.getBody().getInputStream();
            int bytesRead;
            for (byte[] buffer = new byte[4096]; (bytesRead = inputStream.read(buffer)) != -1; ) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();


        } catch (Exception e) {
            e.printStackTrace();

            String errmsg = e.getMessage();
            logger.error("errmsg 278================================ {}    ",errmsg);
            logger.error("apiKey================================ {}    ",apiKey);
            if (errmsg.startsWith("401")) {
                JSONObject loginJson = new JSONObject();
                loginJson.putOnce("email", "yymaas@inspur.com");
                loginJson.putOnce("password", "!QAZ2wsx.");
                loginJson.putOnce("remember_me", true);
                HttpResponse response1 = HttpUtil.createPost(difyBaseUrl+difylogin)
                        .body(loginJson.toString())
                        .execute();

                logger.error("==============401 HttpResponse  status  {}",response1.getStatus());
                logger.error("==============401 HttpResponse  body  {}",response1.body());
                // 如果响应包含实体，则打印实体内容
                if (response1.body() != null) {
                    logger.error("Response Content:");
                    logger.error(response1.body());
                } else {
                    logger.error("Response content is null.");
                }
                if (response1.isOk()) {
                    logger.error("==============apyKey 赋值");
                    apiKey = JSONUtil.parseObj(response1.body()).getJSONObject("data").getStr("access_token");
                    logger.error("==============apyKey 新,{}",apiKey);
                    difyProxy(request, response);
                }
            }
            logger.error("==============error url {}",url);
            logger.error("==============error status {}",response.getStatus());
            logger.error("=================== errmsg :   {}",errmsg);
            int status  = extractHttpStatusCode(errmsg);
            String responseString  = extractJsonBody(errmsg);
            JSONObject jsonObject = JSONUtil.parseObj(responseString);
            Map resMap = com.alibaba.fastjson2.JSONObject.parseObject(jsonObject.toString(), Map.class);

            if (status == 400) {
                response.setStatus(400);
                return resMap;
            } else {
                response.setStatus(500);
            }
            e.printStackTrace();
            return resMap;
        }
        logger.error("============response body============:" + responseEntity.getBody().toString());
        logger.error("============response body body============:" + responseEntity.getBody());
        logger.error("============response body status============:" + responseEntity.getStatusCode());
        try {
            logger.error("=================== result :   {}", StreamUtils.copyToString(responseEntity.getBody().getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        logger.error("=================== baseUrl :   {}",difyBaseUrl);
        return new HashMap<>();
    }

    private void putHeadersMap(HttpServletResponse response, Map<String, List<String>> headers) {
        response.setHeader("X-Frame-Options", "SAMEORIGIN");
        headers.forEach((k, v) -> {
            if (ObjectUtil.isNotNull(k)) {
                response.setHeader(k, v.stream().collect(Collectors.joining(", ")));
            }
        });
    }
    // 提取 HTTP 状态码
    public static Integer extractHttpStatusCode(String response) {
        Pattern pattern = Pattern.compile("(\\d{3})\\s+\\w+");
        Matcher matcher = pattern.matcher(response.split(":")[0].trim());
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return null;
    }

    // 提取 JSON 主体
    public static String extractJsonBody(String response) {
        int startIndex = response.indexOf("{");
        int endIndex = response.lastIndexOf("}") + 1;
        if (startIndex != -1 && endIndex != -1) {
            String jsonContent = response.substring(startIndex, endIndex)
                    .replace("\\", "") // 移除转义字符
                    .replaceAll("<EOL>", "") // 移除人工标记的换行符
                    .replaceAll("^\\s+", "") // 移除行首空格
                    .replaceAll("\\s+$", ""); // 移除行尾空格
            return jsonContent;
        }
        return null;
    }
    public static void main(String[] args) {
        String response = "415 UNSUPPORTED MEDIA TYPE: \"{<EOL>    \"code\": \"unsupported_media_type\",<EOL>    \"message\": \"Did not attempt to load JSON data because the request Content-Type was not 'application/json'.\",<EOL>    \"status\": 415<EOL>}<EOL>\"";

//        String response = "400 BAD REQUEST: \"{\\\"code\\\": \\\"draft_workflow_not_exist\\\", \\\"message\\\": \\\"Draft workflow need to be initialized.\\\", \\\"status\\\": 400}<EOL>\"";

        // 提取 HTTP 状态码的方法
        Integer httpStatusCode = extractHttpStatusCode(response);
        System.out.println("HTTP 状态码: " + httpStatusCode);

        // 提取 JSON 字符串的方法
        String jsonString = extractJsonBody(response);
        System.out.println("JSON 内容:\n" + jsonString);

    }

    private RequestEntity buildRequestEntity(String url, HttpServletRequest request) throws IOException {
        //获取 所有heads
        HttpHeaders headers = parseHeader(request);
        headers.remove("Authorization");
        headers.add("Authorization", "Bearer " + apiKey);
        //这里获取不到 form-data 中数据，只能获取,requestBody, form-urlencoded-www  参数
        logger.error("Headers==========  {}",headers);
        byte[] body = parseBody(request);
        logger.error("request: {}", new String(body));
        //单独处理文件上传
        if (isMultipart(request)) {
            RequestEntity formData = getFormDataEntity(url, request, headers);
            if (formData != null) {
                return formData;
            }

        }

//        headers.add("X-WORKSPACE-ID" , workspaceId);


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

    private Headers parseOkHhttpHeader(HttpServletRequest request) {
        Headers.Builder builder = new Headers.Builder();
//        headers.add("X-Frame-Options", "SAMEORIGIN");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            Enumeration<String> v = request.getHeaders(headerName);
            List<String> arr = new ArrayList<>();
            while (v.hasMoreElements()) {
                arr.add(v.nextElement());
            }
            builder.add(headerName, arr.get(0));
        }
        builder.removeAll("Authorization");
        builder.add("Authorization", "Bearer " + apiKey);
        Headers headers = builder.build();
        return headers;
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
        return request.getRequestURI().replace("//", "/").concat(query != null ? "?".concat(query) : "");
    }

    private void putResponseHeader(HttpServletResponse response, HttpHeaders headers) {
        // 创建一个新的 HttpHeaders 对象用于存储清理后的头信息
        HttpHeaders cleanedHeaders = new HttpHeaders();

        // 遍历原始的 HttpHeaders 对象中的所有头信息
        headers.forEach((k, v) -> {
            if (!k.equalsIgnoreCase("Transfer-Encoding")) {
                // 如果不是 Transfer-Encoding 头，则直接添加到 cleanedHeaders 中
                cleanedHeaders.put(k, v);
            } else {
                // 如果是 Transfer-Encoding 头，则只保留第一个值
                if (!v.isEmpty()) {
//                    cleanedHeaders.set(k, v.get(0));
                }
            }
        });
        logger.error("=====headers=========================={}", headers);
        logger.error("=====cleanedHeaders==================={}", cleanedHeaders);
        // 将清理后的头信息设置到 HttpServletResponse 中
        cleanedHeaders.forEach((key, valueList) -> {
            String value = valueList.stream().collect(Collectors.joining(", "));
            response.setHeader(key, value);
        });

        // 设置额外的响应头 X-Frame-Options
        response.setHeader("X-Frame-Options", "SAMEORIGIN");
    }

    private String getTypeByUrl(String url) {
        if (url.endsWith("/console/api/apps")) {
            return "0";
        } else {
            return null;
        }
    }

}
