package com.titxu.cloud.sys.application.assembler;

import com.titxu.cloud.common.core.domain.StatusEnum;
import com.titxu.cloud.sys.application.command.UserCommand;
import com.titxu.cloud.sys.application.dto.UserDTO;
import com.titxu.cloud.sys.domain.model.role.RoleId;
import com.titxu.cloud.sys.domain.model.user.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户Assembler
 **/
public class UserDTOAssembler {



    public static User toUser(final UserCommand userCommand) {
        List<String> roleIds = userCommand.getRoleIdList();
        List<RoleId> roleIdList = null;
        if (roleIds != null && !roleIds.isEmpty()) {
            roleIdList = new ArrayList<>();
            for (String roleId : roleIds) {
                roleIdList.add(new RoleId(roleId));
            }
        }
        UserNick userNick = null;
        if (userCommand.getUserNick() != null) {
            userNick = new UserNick(userCommand.getUserNick());
        }
        Email email = null;
        if (userCommand.getEmail() != null) {
            email = new Email(userCommand.getEmail());
        }
        Remarks remarks = null;
        if (userCommand.getRemarks() != null) {
            remarks = new Remarks(userCommand.getRemarks());
        }
        StatusEnum statusEnum = null;
        if (userCommand.getStatus() != null) {
            statusEnum = StatusEnum.getStatusEnum(userCommand.getStatus());
        }
        Account account = new Account(new Mobile(userCommand.getMobile()), email);
        return new User(new UserId(userCommand.getId()), userNick, statusEnum, account, null, roleIdList, remarks);
    }

    public static UserDTO fromUser(final User user) {
        List<String> roleIdList = new ArrayList<>();
        if (user.getRoleIds() != null) {
            user.getRoleIds().forEach(roleId -> roleIdList.add(roleId.getId()));
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getUserId() == null ? null : user.getUserId().getId());
        userDTO.setUserNick(user.getUserNick() == null ? null : user.getUserNick().getName());
        userDTO.setEmail(user.getAccount().getEmail() == null ? null : user.getAccount().getEmail().getEmail());
        userDTO.setMobile(user.getAccount().getMobile() == null ? null : user.getAccount().getMobile().getMobile());
        userDTO.setRoleIdList(roleIdList);
        userDTO.setStatus(user.getStatus() == null ? null : user.getStatus().getValue());
        userDTO.setRemarks(user.getRemarks() == null ? null : user.getRemarks().remarks());
        return userDTO;
    }
}
