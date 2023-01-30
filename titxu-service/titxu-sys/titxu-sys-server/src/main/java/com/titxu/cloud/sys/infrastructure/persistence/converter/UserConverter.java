package com.titxu.cloud.sys.infrastructure.persistence.converter;

import com.titxu.cloud.common.core.domain.StatusEnum;
import com.titxu.cloud.sys.domain.model.role.RoleId;
import com.titxu.cloud.sys.domain.model.tenant.TenantId;
import com.titxu.cloud.sys.domain.model.user.*;
import com.titxu.cloud.sys.infrastructure.persistence.entity.SysAccountDO;
import com.titxu.cloud.sys.infrastructure.persistence.entity.SysUserDO;

import java.util.List;

/**
 * 用户Converter
 **/
public class UserConverter {

    public static User toUser(SysUserDO sysUserDO, Account account, List<RoleId> roleIdList) {
        if (sysUserDO == null) {
            return null;
        }
        return new User(new UserId(sysUserDO.getId()), new UserNick(sysUserDO.getUserNick()), StatusEnum.getStatusEnum(sysUserDO.getStatus()), account, new TenantId(sysUserDO.getTenantId()), roleIdList,new Remarks(sysUserDO.getRemarks()));
    }

    public static SysUserDO fromUser(User user, String accountId) {
        SysUserDO sysUserDO = new SysUserDO();
        sysUserDO.setId(user.getUserId() == null ? null : user.getUserId().getId());
        sysUserDO.setUserNick(user.getUserNick() == null ? null : user.getUserNick().getName());
        sysUserDO.setStatus(user.getStatus() == null ? null : user.getStatus().getValue());
        sysUserDO.setRemarks(user.getRemarks() == null ? null : user.getRemarks().remarks());
        sysUserDO.setAccountId(accountId);
        return sysUserDO;
    }

    public static SysAccountDO getSysAccountDO(User user) {
        SysAccountDO sysAccountDO = new SysAccountDO();
        Account account = user.getAccount();
        if (account == null) {
            return null;
        }
        sysAccountDO.setId(account.getAccountId() == null ? null : account.getAccountId().getId());
        sysAccountDO.setEmail(account.getEmail() == null ? null : account.getEmail().getEmail());
        sysAccountDO.setMobile(account.getMobile() == null ? null : account.getMobile().getMobile());
        sysAccountDO.setPassword(account.getPassword() == null ? null : account.getPassword().getPassword());
        return sysAccountDO;
    }

    public static Account toAccount(SysAccountDO sysAccountDO) {
        if (sysAccountDO == null) {
            return null;
        }
        Mobile mobile = null;
        if (sysAccountDO.getMobile() != null) {
            mobile = new Mobile(sysAccountDO.getMobile());
        }
        Email email = null;
        if (sysAccountDO.getEmail() != null) {
            email = new Email(sysAccountDO.getEmail());
        }
        Password password = null;
        if (sysAccountDO.getPassword() != null) {
            password = new Password(sysAccountDO.getPassword());
        }

        return new Account(new AccountId(sysAccountDO.getId()), mobile, email, password);
    }
}
