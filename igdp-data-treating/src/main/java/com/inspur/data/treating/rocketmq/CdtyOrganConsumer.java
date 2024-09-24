package com.inspur.data.treating.rocketmq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.RPCHook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * 成都退役消息队列MQ
 *
 * @author liyunlong
 * @version 1.0
 * @ClassName IncrementConsumer
 * @date 2024/8/31 10:45
 */
@Service
@Slf4j
@RefreshScope
public class CdtyOrganConsumer {

    /**
     * 通过构造函数 实例化对象
     */
    public CdtyOrganConsumer(CdtyOrganConfig organConfig) throws MQClientException {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(null, organConfig.getConsumerGroup(), getAclRPCHook(organConfig));
        consumer.setNamesrvAddr(organConfig.getNameServer());
        consumer.subscribe("organTopic", "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.println("consumer启动成功");
//                for(int i=0;i<msgs.size();i++){
//                    MessageExt message = msgs.get(i);
//                    System.out.println("消费者收到信息：==="+message);
//                    String messageBody = new String(message.getBody());
//                    System.out.printf("Body", messageBody);
//
//                }
                msgs.forEach(message -> {
                    JSONObject jsonObject = JSON.parseObject(new String(message.getBody()));
                    System.out.println("message" + jsonObject);
                    JSONArray dataArray = jsonObject.getJSONArray("data");
//                    if(!dataArray.isEmpty()){
//                        for (int i = 0; i < dataArray.size(); i++) {
//                            JSONObject dataObj = dataArray.getJSONObject(i);
//                            System.out.println("dataObject"+dataObj);
//                            String cases = (String) dataObj.get("oper");
//                            if(cases.equals("I")){
//                                organAdd(dataObj);
//                            }else if(cases.equals("U")){
//                                organUpdate(dataObj);
//                            }else if(cases.equals("D")){
//                                organDelete(dataObj);
//                            }
//
//                        }
//                    }
                });
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        if(organConfig.isEnabled()){
            consumer.start();
            System.out.println("CdtyOrganConsumer启动成功");
        }
    }


    RPCHook getAclRPCHook(CdtyOrganConfig organConfig) {
        return new AclClientRPCHook(new SessionCredentials(organConfig.getAccessKey(), organConfig.getSecretKey()));
    }


}
