package com.titxu.cloud.wechat.api.query.wechat;

import lombok.Data;

@Data
public class GetRoomMembersReqModel extends ClientReqModel {

    private String roomWxid;
}
