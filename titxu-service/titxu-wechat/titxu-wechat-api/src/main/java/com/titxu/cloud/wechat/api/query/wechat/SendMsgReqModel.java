package com.titxu.cloud.wechat.api.query.wechat;

import lombok.Data;

@Data
public class SendMsgReqModel extends ClientReqModel {


    private String toWxid;
}
