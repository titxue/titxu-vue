package com.titxu.cloud.wechat.infrastructure.feign.wechat;

import com.titxu.cloud.common.core.util.ResponseModel;
import com.titxu.cloud.wechat.api.query.wechat.ClientOpenReqModel;
import com.titxu.cloud.wechat.api.query.wechat.ClientReqModel;
import com.titxu.cloud.wechat.api.query.wechat.SendTextReqModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@Slf4j
@SpringBootTest
class RemoteWechatServiceTest {
    @Autowired
    private RemoteWechatService remoteWechatService;

    /**
     * 测试创建微信服务实例
     * 获取guid
     */
    @Test
    void create() {
        ResponseModel<Map<String, String>> responseModel = remoteWechatService.create();

        String guid = responseModel.getData().get("guid");
        // 测试guid是否为空
        assert guid != null;
        log.info("guid:{}", guid);
        ClientOpenReqModel reqModel = new ClientOpenReqModel();
        reqModel.setGuid(guid);
        remoteWechatService.open(reqModel);
    }

    /**
     * 测试打开微信服务
     */
    @Test
    void open() {
        ClientOpenReqModel reqModel = new ClientOpenReqModel();
        reqModel.setGuid("a4aa424b-5283-3e1d-af22-25652aa5801b");
        remoteWechatService.open(reqModel);
    }

    /**
     * 测试获取微信个人信息服务
     */
    @Test
    void getProfile() {
        ClientReqModel reqModel = new ClientReqModel();
        reqModel.setGuid("a4aa424b-5283-3e1d-af22-25652aa5801b");
        ResponseModel<Map<String, String>> profile = remoteWechatService.getProfile(reqModel);
        log.info("profile:{}", profile);
    }

    /**
     * 测试发送微信消息服务
     */
    @Test
    void sendText() {
        SendTextReqModel textReqModel = new SendTextReqModel();
        textReqModel.setGuid("e9a69815-a17a-3f07-86bd-55a39f7ba5ed");
        textReqModel.setContent("测试发送消息");
        textReqModel.setToWxid("43784291946@chatroom");
        ResponseModel<Map<String, String>> profile = remoteWechatService.sendText(textReqModel);
        log.info("profile:{}", profile);
    }

}