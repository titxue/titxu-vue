package com.titxu.cloud.wechat.api.query.wechat;

import lombok.Data;

@Data
public class SendTextReqModel extends SendMsgReqModel {
    private String content;
}
