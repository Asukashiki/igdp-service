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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 公共转换服务
 */
@Service

public class ProxyServiceImpl extends ServiceImpl<IDifyUserReleationMapper, DifyUserRelationEntity> implements IProxyService {
    private static Logger logger = LoggerFactory.getLogger(ProxyServiceImpl.class);
    private final IDifyUserReleationMapper difyUserReleationMapper;
    private String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiN2JkYTU1YTMtNWFhNS00ZTdkLWIxZmYtNDJlMDIwZGE3NDNmIiwiZXhwIjoxNzMwMDIxODA5LCJpc3MiOiJTRUxGX0hPU1RFRCIsInN1YiI6IkNvbnNvbGUgQVBJIFBhc3Nwb3J0In0.IwxE5w3fpP4yY6P1bPGbQrRfFStwINrRi-tGNylVH5Q";
    private String difyAddress = "http://10.110.149.140:30099";
    private String difylogin = "http://10.110.149.140:30099/dify/console/api/login";
    private String labelloginGet = "http://10.110.149.140:30099/labelstudio/labelstudio/user/login/";
    private String labelloginIgdpLogin = "http://10.110.149.140:30099/labelstudio/user/user-login-igdp/";
    private final RestTemplate restTemplate;

    public ProxyServiceImpl(IDifyUserReleationMapper difyUserReleationMapper, RestTemplateBuilder restTemplateBuilder) {
        this.difyUserReleationMapper = difyUserReleationMapper;
        this.restTemplate = restTemplateBuilder.build();

    }


    @Override
    public Object difyProxy(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        String url = rebuildUlr(httpServletRequest);

        ResponseEntity<Resource> responseEntity;
        url = difyAddress.concat(url.replace("/igdp/", "/"));
        logger.error("========url======{}", url);

        return difyProxyHttp(httpServletRequest, response, url);


    }

    @Override

    public void draftRun(HttpServletRequest httpServletRequest, HttpServletResponse response, SseEmitter emitter) {
        String url = rebuildUlr(httpServletRequest);
        url = difyAddress.concat(url.replace("/igdp/", "/"));
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
                HttpResponse response1 = HttpUtil.createPost(difylogin)
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
        url = difyAddress.concat(url.replace("/igdp/", "/"));
        logger.error("========url======{}", url);

        return labelStudioProxyHttp(httpServletRequest, response, url);
    }

    @Override
    public void labelStudioProxyLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        LoginUser user = LoginHelper.getLoginUser();
        cn.hutool.http.HttpRequest request = cn.hutool.http.HttpRequest.get(labelloginGet);
        // 发送请求并获取登录页返回的seesionid和 csrftoken
        HttpResponse response = request.execute();
        List<String> setCookie = response.headers().get("Set-Cookie");
        HttpRequest loginRequest = HttpRequest.post(labelloginIgdpLogin);
        Map<String, Object> params = new HashMap<>();
        params.put("email", user.getUsername() + "@inspur.com");
        params.put("username", user.getUsername() );
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
        setResponseHeaders(loginResponse, httpServletResponse);
        System.out.println("Set-Cookie: " + setCookie);
        ServletOutputStream outputStream = null;
        try {
            outputStream = httpServletResponse.getOutputStream();
            InputStream inputStream = loginResponse.bodyStream();
            int bytesRead;
            for (byte[] buffer = new byte[4096]; (bytesRead = inputStream.read(buffer)) != -1; ) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private void setResponseHeaders(HttpResponse loginResponse, HttpServletResponse httpServletResponse) {
        Map<String, List<String>> headers = loginResponse.headers();

        Iterator<Map.Entry<String, List<String>>> entries = headers.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, List<String>> entry = entries.next();
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            for (int i = 0; i < entry.getValue().size(); i++) {
                httpServletResponse.setHeader(entry.getKey(),entry.getValue().get(i));
                if(entry.getKey()!=null &&entry.getKey().equals("Set-Cookie")){
                    if(entry.getValue().get(i).startsWith("session")){
                        Cookie sessionCookie = new Cookie("sessionid", entry.getValue().get(i).substring(10,149));
                        httpServletResponse.addCookie(sessionCookie);
                    }

                }
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
        return com.alibaba.fastjson2.JSONObject.parseObject(responseEntity.getBody().toString(), Map.class);
    }

    private Map difyProxyHttp(HttpServletRequest request, HttpServletResponse response, String url) {
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
            if (errmsg.startsWith("401")) {
                JSONObject loginJson = new JSONObject();
                loginJson.putOnce("email", "yymaas@inspur.com");
                loginJson.putOnce("password", "!QAZ2wsx.");
                loginJson.putOnce("remember_me", true);
                HttpResponse response1 = HttpUtil.createPost(difylogin)
                        .body(loginJson.toString())
                        .execute();
                if (response1.isOk()) {
                    apiKey = JSONUtil.parseObj(response1.body()).getStr("data");
                    difyProxy(request, response);
                }
            }
            errmsg = errmsg.split("\\{<EOL>")[1];
            errmsg = errmsg.replace("<EOL>", "");
            errmsg = "{" + errmsg;
            JSONObject jsonObject = JSONUtil.parseObj(errmsg);
            Map resMap = com.alibaba.fastjson2.JSONObject.parseObject(jsonObject.toString(), Map.class);

            if (errmsg.startsWith("400")) {
                response.setStatus(400);
                return resMap;
            } else {
                response.setStatus(500);
            }
            e.printStackTrace();
            return resMap;
        }
        logger.error("============response body============:" + responseEntity.getBody().toString());
        return com.alibaba.fastjson2.JSONObject.parseObject(responseEntity.getBody().toString(), Map.class);
    }

    private void putHeadersMap(HttpServletResponse response, Map<String, List<String>> headers) {
        response.setHeader("X-Frame-Options", "SAMEORIGIN");
        headers.forEach((k, v) -> {
            if (ObjectUtil.isNotNull(k)) {
                response.setHeader(k, v.stream().collect(Collectors.joining(", ")));
            }
        });
    }

    public static void main(String[] args) {
        String msg = "415 UNSUPPORTED MEDIA TYPE: \"{<EOL>    \"code\": \"unsupported_media_type\",<EOL>    \"message\": \"Did not attempt to load JSON data because the request Content-Type was not 'application/json'.\",<EOL>    \"status\": 415<EOL>}<EOL>\"";
        msg = msg.split("\\{<EOL>")[1];
        msg = msg.replace("<EOL>", "");
        msg = "{" + msg;
        JSONUtil.parseObj(msg);
        System.out.println(msg);

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
        headers.add("Authorization", "Bearer " + apiKey);
//        headers.add("X-WORKSPACE-ID" , workspaceId);
        //这里获取不到 form-data 中数据，只能获取,requestBody, form-urlencoded-www参数
        byte[] body = parseBody(request);
        logger.error("request: {}", new String(body));
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
        headers.forEach((k, v) -> {
            response.setHeader(k, v.stream().collect(Collectors.joining(", ")));
        });
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
