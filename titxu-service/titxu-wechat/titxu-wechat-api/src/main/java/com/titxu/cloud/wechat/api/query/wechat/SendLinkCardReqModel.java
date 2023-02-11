package com.titxu.cloud.wechat.api.query.wechat;

import lombok.Data;

@Data
public class SendLinkCardReqModel extends SendMsgReqModel {
    private String title;
    private String desc;
    private String url;

    private String imageUrl;
}
