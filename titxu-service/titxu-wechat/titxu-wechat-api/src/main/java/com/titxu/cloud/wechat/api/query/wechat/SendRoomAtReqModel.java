package com.titxu.cloud.wechat.api.query.wechat;

import lombok.Data;

import java.util.List;

@Data
public class SendRoomAtReqModel extends SendTextReqModel {


    private List<String> atList;
}
