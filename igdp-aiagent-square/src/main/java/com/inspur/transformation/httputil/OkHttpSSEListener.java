package com.inspur.transformation.httputil;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.Getter;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

public class OkHttpSSEListener extends EventSourceListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(OkHttpSSEListener.class);
    @Getter
    private final CountDownLatch countDownLatch = new CountDownLatch(1);
    private final SseEmitter emitter;

    @Getter
    private final List<String> contextStreams = new ArrayList<>();

    @Getter
    private String errorMessage;

    public OkHttpSSEListener(SseEmitter emitter) {
        requireNonNull(emitter, "httpServletResponse is null");
        this.emitter = emitter;
    }


    @Override
    public void onClosed(@NotNull EventSource eventSource) {
        LOGGER.info("-- OpenAi see close ... --");
        emitter.complete();
        countDownLatch.countDown();
    }

    @Override
    public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
        try {
            LOGGER.info("chat stream data: {}", data);
            if ("[DONE]".equals(data)) {
                LOGGER.info("-- OpenAI return data success --");
            } else {
                contextStreams.add(data);
            }

            emitter.send(data, MediaType.TEXT_EVENT_STREAM);
        } catch (IOException e) {
            countDownLatch.countDown();
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onFailure(@NotNull EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
        try {
            if (Objects.isNull(response)) {
                throw new RuntimeException(String.format("-- OpenAi sse connection exception: %s --", t.getMessage()), t);
            } else {
                ResponseBody body = response.body();
                if (Objects.isNull(body)) {
                    throw new RuntimeException(String.format("-- OpenAi sse connection exception: %s --", response));
                } else {
                    throw new RuntimeException(String.format("-- OpenAi sse connection exception: %s --", body.string()));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            errorMessage = e.getMessage();
        }
        countDownLatch.countDown();
        eventSource.cancel();

    }

    @Override
    public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
        LOGGER.info("-- OpenAI establishes sse connection... --");
    }

    public static void main(String[] args) {

        String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiN2JkYTU1YTMtNWFhNS00ZTdkLWIxZmYtNDJlMDIwZGE3NDNmIiwiZXhwIjoxNzMwMDIxODA5LCJpc3MiOiJTRUxGX0hPU1RFRCIsInN1YiI6IkNvbnNvbGUgQVBJIFBhc3Nwb3J0In0.IwxE5w3fpP4yY6P1bPGbQrRfFStwINrRi-tGNylVH5Q";
//        HttpResponse response1 = HttpUtil.createPost(difylogin)
//                .body(loginJson.toString())
//                .execute();
//        if (response1.isOk()) {
//            apiKey = JSONUtil.parseObj(response1.body()).getStr("data");
//        }
        String body = "{\n" +
                "    \"inputs\": {\n" +
                "        \"content\": \"A self-important lion in the jungle tried to make his mastery clear to all.He was so confident that he paid no attention to the smaller animals and went right up to a bear. He asked the bear, \\\"Who is the king of the jungle?\\\" The bear replied, \\\"Of course yo\\nu are.\\\"\\nThen the lion asked a tiger the same question. The tiger replied with some reluctance1,\\\"Of course you are.\\\" And then he went to ask an elephant. But the elephant would not allow the lion to do so. He suddenly took hold of the lion with his long nose and bounced2 the lionagainst a tree, leaving him bleeding3 and badly shaken up.\\nWhen the lion finally got up, he blamed the elephant and said: \\\"Even if you couldn't answer my question, it's not necessary for you to act so rough4.\\\"\"\n" +
                "    },\n" +
                "    \"files\": []\n" +
                "}";
        Headers.Builder builder = new Headers.Builder();
        builder.add("Content-Type", "application/json");
        builder.add("Accept", "*/*");
        builder.add("Connection", "keep-alive");
        builder.add("Authorization", "Bearer " + apiKey);
        Headers headers = builder.build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1000, TimeUnit.SECONDS)
                .writeTimeout(1000, TimeUnit.SECONDS)
                .readTimeout(1000, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = null;
        try {
            requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"), body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Request request = new Request.Builder()
                .url("http://10.110.149.140:30099/dify/console/api/apps/ec0e13bc-98f8-4b6e-ad74-aff073cf5142/workflows/draft/run")
                .headers(headers)
                .post(requestBody)
                .build();
        CountDownLatch countDownLatch = new CountDownLatch(1);

        EventSource.Factory factory = EventSources.createFactory(client);
        StringBuffer output = new StringBuffer();
        try {
            // 自定义监听器
            EventSourceListener eventSourceListener = new EventSourceListener() {
                @Override
                public void onOpen(EventSource eventSource, Response response) {
                    System.out.println(response.body().toString());
                }

                @Override
                public void onEvent(EventSource eventSource, String id, String type, String data) {
                    //   接受消息 data
                    try {
                        System.out.println(data);
//                        response.getWriter().write(data + "\n");
//                        response.getWriter().flush();
//                        System.out.println(JSONObject.toJSONString(data));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }

                @Override
                public void onClosed(EventSource eventSource) {
                    System.out.println("sse close: {}");
                    countDownLatch.countDown();
                }

                @Override
                public void onFailure(EventSource eventSource, Throwable t, Response response) {
                    countDownLatch.countDown();
                }
            };

            // 创建事件
            EventSource eventSource = factory.newEventSource(request, eventSourceListener);
            countDownLatch.await();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
