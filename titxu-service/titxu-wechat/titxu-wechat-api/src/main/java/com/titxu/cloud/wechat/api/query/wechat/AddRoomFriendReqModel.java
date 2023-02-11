package com.titxu.cloud.wechat.api.query.wechat;

import lombok.Data;

@Data
public class AddRoomFriendReqModel extends ClientReqModel {

    private String roomWxid;
    private String wxid;
    private String verify;
}
