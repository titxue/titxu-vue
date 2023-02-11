package com.titxu.cloud.wechat.api.query.wechat;

import lombok.Data;

@Data
public class SendPatReqModel extends ClientReqModel {

    private String roomWxid;


    private String pattedWxid;
}
