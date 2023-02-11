package com.titxu.cloud.wechat.api.query.wechat;

import lombok.Data;

@Data
public class ModifyFriendRemarkReqModel extends ClientReqModel {
    private String wxid;
    private String remark;
}
