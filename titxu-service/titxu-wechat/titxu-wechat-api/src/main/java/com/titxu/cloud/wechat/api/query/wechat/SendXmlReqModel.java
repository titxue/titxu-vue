package com.titxu.cloud.wechat.api.query.wechat;

import lombok.Data;

@Data
public class SendXmlReqModel extends SendMsgReqModel {
    private String xml = "";
}
