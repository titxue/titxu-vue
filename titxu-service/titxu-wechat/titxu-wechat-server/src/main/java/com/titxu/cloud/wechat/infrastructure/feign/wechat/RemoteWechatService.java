package com.titxu.cloud.wechat.infrastructure.feign.wechat;


import com.titxu.cloud.common.core.util.ResponseModel;
import com.titxu.cloud.wechat.api.query.wechat.*;
import com.titxu.cloud.wechat.infrastructure.feign.wechat.factory.RemoteWechatServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "wechat", url = "http://nas.titxu.com:9010", fallbackFactory = RemoteWechatServiceFallbackFactory.class)
public interface RemoteWechatService {
    /**
     * 创建微信服务实例
     */
    @PostMapping("/client/create")
    ResponseModel<Map<String, String>> create();

    /**
     * 打开微信
     */
    @PostMapping("/client/open")
    ResponseModel<Map<String, String>> open(@RequestBody ClientOpenReqModel clientOpenReqModel);

    /**
     * 设置接收通知地址
     */
    @PostMapping("/global/set_callback_url")
    ResponseModel<Map<String, String>> clientSetCallbackUrl(@RequestBody CallbackUrlReqModel callbackUrlReqModel);

    /**
     * 设置接收通知地址
     */
    @PostMapping("/global/set_callback_kafka")
    ResponseModel<Map<String, String>> clientSetCallbackKafka(@RequestBody CallbackKafkaReqModel model);


    /**
     * 获取自己的信息
     */
    @PostMapping("/user/get_profile")
    ResponseModel<Map<String, String>> getProfile(@RequestBody ClientReqModel clientReqModel);

    /**
     * 获取联系人列表
     */
    @PostMapping("/contact/get_contacts")
    ResponseModel<Map<String, String>> getContacts(@RequestBody ClientReqModel clientReqModel);

    /**
     * 获取指定联系人详细信息
     */
    @PostMapping("/contact/get_contact_detail")
    ResponseModel<Map<String, String>> getContactDetail(@RequestBody ContactDetailReqModel contactDetailReqModel);


    /**
     * 修改联系人备注
     */
    @PostMapping("/contact/modify_remark")
    ResponseModel<Map<String, String>> modifyFriendRemark(@RequestBody ModifyFriendRemarkReqModel modifyFriendRemarkReqModel);


    /**
     * 获取群列表
     */
    @PostMapping("/room/get_rooms")
    ResponseModel<Map<String, String>> getRooms(@RequestBody ClientReqModel clientReqModel);

    /**
     * 获取群成员列表
     */
    @PostMapping("/room/get_room_members")
    ResponseModel<Map<String, String>> getRoomMembers(@RequestBody GetRoomMembersReqModel getRoomMembersReqModel);

    /**
     * 创建群
     */
    @PostMapping("/room/create_room")
    ResponseModel<Map<String, String>> createRoom(@RequestBody CreateRoomReqModel createRoomReqModel);


    /**
     * 添加好友入群
     */
    @PostMapping("/room/add_room_member")
    ResponseModel<Map<String, String>> addRoomMember(@RequestBody RoomMembersReqModel roomMembersReqModel);

    /**
     * 邀请好友入群
     */
    @PostMapping("/room/invite_room_member")
    ResponseModel<Map<String, String>> inviteRoomMember(@RequestBody RoomMembersReqModel roomMembersReqModel);

    /**
     * 删除群成员
     */
    @PostMapping("/room/del_room_member")
    ResponseModel<Map<String, String>> delRoomMember(@RequestBody RoomMembersReqModel roomMembersReqModel);

    /**
     * 添加群成员为好友
     */
    @PostMapping("/room/add_room_friend")
    ResponseModel<Map<String, String>> addRoomFriend(@RequestBody AddRoomFriendReqModel addRoomFriendReqModel);

    /**
     * 修改群名
     */
    @PostMapping("/room/modify_name")
    ResponseModel<Map<String, String>> modifyName(@RequestBody ModifyRoomNameReqModel modifyRoomNameReqModel);

    /**
     * 退出群
     */
    @PostMapping("/room/quit_room")
    ResponseModel<Map<String, String>> quitRoom(@RequestBody RoomReqModel roomReqModel);

    /**
     * 发送文本消息
     */
    @PostMapping("/msg/send_text")
    ResponseModel<Map<String, String>> sendText(@RequestBody SendTextReqModel sendTextReqModel);


    /**
     * 发送群@消息
     */
    @PostMapping("/msg/send_room_at")
    ResponseModel<Map<String, String>> sendRoomAt(@RequestBody SendRoomAtReqModel sendRoomAtReqModel);


    /**
     * 发送名片
     */
    @PostMapping("/msg/send_card")
    ResponseModel<Map<String, String>> sendCard(@RequestBody SendCardReqModel sendCardReqModel);

    /**
     * 发送链接卡片消息
     */
    @PostMapping("/msg/send_link_card")
    ResponseModel<Map<String, String>> sendLinkCard(@RequestBody SendLinkCardReqModel sendLinkCardReqModel);

    /**
     * 发送图片
     */
    @PostMapping("/msg/send_image")
    ResponseModel<Map<String, String>> sendImage(@RequestBody SendMediaReqModel sendMediaReqModel);

    /**
     * 发送文件
     */
    @PostMapping("/msg/send_file")
    ResponseModel<Map<String, String>> sendFile(@RequestBody SendMediaReqModel sendMediaReqModel);

    /**
     * 发送视频
     */
    @PostMapping("/msg/send_video")
    ResponseModel<Map<String, String>> sendVideo(@RequestBody SendMediaReqModel sendMediaReqModel);

    /**
     * 发送GIF
     */
    @PostMapping("/msg/send_gif")
    ResponseModel<Map<String, String>> sendGif(@RequestBody SendMediaReqModel sendMediaReqModel);

    /**
     * 发送XML原始消息
     */
    @PostMapping("/msg/send_xml")
    ResponseModel<Map<String, String>> sendXml(@RequestBody SendXmlReqModel sendXmlReqModel);

    /**
     * 发送拍一拍
     */
    @PostMapping("/msg/send_pat")
    ResponseModel<Map<String, String>> sendPat(@RequestBody SendXmlReqModel sendXmlReqModel);

}
