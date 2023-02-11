package com.titxu.cloud.wechat.api.query.wechat;

import lombok.Data;

@Data
public class SendMediaReqModel extends SendMsgReqModel {

    private String filePath = "";
    private String url = "";
}
