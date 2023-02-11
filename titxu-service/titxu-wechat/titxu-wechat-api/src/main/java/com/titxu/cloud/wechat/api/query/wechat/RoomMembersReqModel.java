package com.titxu.cloud.wechat.api.query.wechat;

import lombok.Data;

@Data
public class RoomMembersReqModel extends CreateRoomReqModel {

    private String roomWxid;
}
