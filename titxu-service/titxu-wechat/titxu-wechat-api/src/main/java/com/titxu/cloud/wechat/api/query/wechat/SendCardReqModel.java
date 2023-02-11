package com.titxu.cloud.wechat.api.query.wechat;

import lombok.Data;

@Data
public class SendCardReqModel extends SendMsgReqModel {

    private String cardWxid;
}
