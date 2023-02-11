package com.titxu.cloud.wechat.api.query.wechat;

import lombok.Data;

@Data
public class ContactDetailReqModel extends ClientReqModel {
    private String wxid;
}
