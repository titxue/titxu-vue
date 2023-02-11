package com.titxu.cloud.wechat.infrastructure.feign.wechat.fallback;

import com.titxu.cloud.common.core.util.ResponseModel;
import com.titxu.cloud.wechat.api.query.wechat.*;
import com.titxu.cloud.wechat.infrastructure.feign.wechat.RemoteWechatService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 服务降级处理
 */
@Slf4j
@AllArgsConstructor
public class RemoteWechatServiceFallback implements RemoteWechatService {
    private static final String ERROR_MSG = "服务调用失败";
    private final Throwable throwable;

    @Override
    public ResponseModel<Map<String, String>> create() {
        log.error("服务调用失败，报错原因：{}", throwable.getMessage());
        return new ResponseModel<Map<String, String>>().errorMsg("创建微信实例" + ERROR_MSG);
    }

    @Override
    public ResponseModel<Map<String, String>> open(ClientOpenReqModel clientOpenReqModel) {
        return new ResponseModel<Map<String, String>>().errorMsg("打开微信" + ERROR_MSG);
    }

    @Override
    public ResponseModel<Map<String, String>> clientSetCallbackUrl(CallbackUrlReqModel callbackUrlReqModel) {
        return new ResponseModel<Map<String, String>>().errorMsg("设置微信消息回调地址" + ERROR_MSG);
    }

    @Override
    public ResponseModel<Map<String, String>> clientSetCallbackKafka(CallbackKafkaReqModel model) {
        return new ResponseModel<Map<String, String>>().errorMsg("设置微信消息推送配置" + ERROR_MSG);
    }

    @Override
    public ResponseModel<Map<String, String>> getProfile(ClientReqModel clientReqModel) {
        return new ResponseModel<Map<String, String>>().errorMsg("获取微信个人信息" + ERROR_MSG);
    }

    @Override
    public ResponseModel<Map<String, String>> getContacts(ClientReqModel clientReqModel) {
        return new ResponseModel<Map<String, String>>().errorMsg("获取微信联系人列表" + ERROR_MSG);
    }

    @Override
    public ResponseModel<Map<String, String>> getContactDetail(ContactDetailReqModel contactDetailReqModel) {
        return new ResponseModel<Map<String, String>>().errorMsg("获取指定联系人详细信息" + ERROR_MSG);
    }

    @Override
    public ResponseModel<Map<String, String>> modifyFriendRemark(ModifyFriendRemarkReqModel modifyFriendRemarkReqModel) {
        return new ResponseModel<Map<String, String>>().errorMsg("修改联系人备注" + ERROR_MSG);
    }

    @Override
    public ResponseModel<Map<String, String>> getRooms(ClientReqModel clientReqModel) {
        return new ResponseModel<Map<String, String>>().errorMsg("获取微信群列表" + ERROR_MSG);
    }

    @Override
    public ResponseModel<Map<String, String>> getRoomMembers(GetRoomMembersReqModel getRoomMembersReqModel) {
        return new ResponseModel<Map<String, String>>().errorMsg("获取群成员列表" + ERROR_MSG);
    }

    @Override
    public ResponseModel<Map<String, String>> createRoom(CreateRoomReqModel createRoomReqModel) {
        return new ResponseModel<Map<String, String>>().errorMsg("创建微信群" + ERROR_MSG);
    }

    @Override
    public ResponseModel<Map<String, String>> addRoomMember(RoomMembersReqModel roomMembersReqModel) {
        return new ResponseModel<Map<String, String>>().errorMsg("添加好友入群" + ERROR_MSG);
    }

    @Override
    public ResponseModel<Map<String, String>> inviteRoomMember(RoomMembersReqModel roomMembersReqModel) {
        return new ResponseModel<Map<String, String>>().errorMsg("邀请好友入群" + ERROR_MSG);
    }

    @Override
    public ResponseModel<Map<String, String>> delRoomMember(RoomMembersReqModel roomMembersReqModel) {
        return new ResponseModel<Map<String, String>>().errorMsg("删除群成员" + ERROR_MSG);
    }

    @Override
    public ResponseModel<Map<String, String>> addRoomFriend(AddRoomFriendReqModel addRoomFriendReqModel) {
        return new ResponseModel<Map<String, String>>().errorMsg("添加群成员为好友" + ERROR_MSG);
    }

    @Override
    public ResponseModel<Map<String, String>> modifyName(ModifyRoomNameReqModel modifyRoomNameReqModel) {
        return new ResponseModel<Map<String, String>>().errorMsg("修改群名" + ERROR_MSG);
    }

    @Override
    public ResponseModel<Map<String, String>> quitRoom(RoomReqModel roomReqModel) {
        return new ResponseModel<Map<String, String>>().errorMsg("退出群" + ERROR_MSG);
    }

    @Override
    public ResponseModel<Map<String, String>> sendText(SendTextReqModel sendTextReqModel) {
        return new ResponseModel<Map<String, String>>().errorMsg("发送文本消息" + ERROR_MSG);
    }

    @Override
    public ResponseModel<Map<String, String>> sendRoomAt(SendRoomAtReqModel sendRoomAtReqModel) {
        return new ResponseModel<Map<String, String>>().errorMsg("发送群@消息" + ERROR_MSG);
    }

    @Override
    public ResponseModel<Map<String, String>> sendCard(SendCardReqModel sendCardReqModel) {
        return new ResponseModel<Map<String, String>>().errorMsg("发送名片" + ERROR_MSG);
    }

    @Override
    public ResponseModel<Map<String, String>> sendLinkCard(SendLinkCardReqModel sendLinkCardReqModel) {
        return new ResponseModel<Map<String, String>>().errorMsg("发送链接卡片消息" + ERROR_MSG);
    }

    @Override
    public ResponseModel<Map<String, String>> sendImage(SendMediaReqModel sendMediaReqModel) {
        return new ResponseModel<Map<String, String>>().errorMsg("发送图片" + ERROR_MSG);
    }

    @Override
    public ResponseModel<Map<String, String>> sendFile(SendMediaReqModel sendMediaReqModel) {
        return new ResponseModel<Map<String, String>>().errorMsg("发送文件" + ERROR_MSG);
    }

    @Override
    public ResponseModel<Map<String, String>> sendVideo(SendMediaReqModel sendMediaReqModel) {
        return new ResponseModel<Map<String, String>>().errorMsg("发送视频" + ERROR_MSG);
    }

    @Override
    public ResponseModel<Map<String, String>> sendGif(SendMediaReqModel sendMediaReqModel) {
        return new ResponseModel<Map<String, String>>().errorMsg("发送GIF" + ERROR_MSG);
    }

    @Override
    public ResponseModel<Map<String, String>> sendXml(SendXmlReqModel sendXmlReqModel) {
        return new ResponseModel<Map<String, String>>().errorMsg("发送XML原始消息" + ERROR_MSG);
    }

    @Override
    public ResponseModel<Map<String, String>> sendPat(SendXmlReqModel sendXmlReqModel) {
        return new ResponseModel<Map<String, String>>().errorMsg("发送拍一拍" + ERROR_MSG);
    }
}
