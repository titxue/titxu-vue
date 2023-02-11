package com.titxu.cloud.wechat.infrastructure.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class WechatConsumer {

    private static final String TOPIC_NAME = "wechat-events";

    //监听一个或者多个Topic
    @KafkaListener(topics = WechatConsumer.TOPIC_NAME)
    public void handler(String message){
        System.out.println("收到消息："+message);
    }
}

