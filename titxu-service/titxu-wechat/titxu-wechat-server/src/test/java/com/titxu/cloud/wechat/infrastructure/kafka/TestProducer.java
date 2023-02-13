package com.titxu.cloud.wechat.infrastructure.kafka;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

@Slf4j
@SpringBootTest
public class TestProducer {

    @Autowired
    private KafkaTemplate<Object,Object> kafkaTemplate;

    @Test
    @SneakyThrows
    void sendMsg() {
        CompletableFuture<SendResult<Object, Object>> test_msg = kafkaTemplate.send("wechat-events", "test msg");
        log.info("发送成功{}", test_msg.get().toString());
    }
}
