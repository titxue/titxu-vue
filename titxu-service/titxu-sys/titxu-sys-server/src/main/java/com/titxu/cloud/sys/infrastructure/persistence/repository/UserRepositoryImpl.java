package com.titxu.cloud.sys.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.titxu.cloud.common.core.util.TenantContext;
import com.titxu.cloud.sys.domain.model.role.RoleId;
import com.titxu.cloud.sys.domain.model.user.*;
import com.titxu.cloud.sys.infrastructure.persistence.converter.UserConverter;
import com.titxu.cloud.sys.infrastructure.persistence.entity.SysAccountDO;
import com.titxu.cloud.sys.infrastructure.persistence.entity.SysRoleDO;
import com.titxu.cloud.sys.infrastructure.persistence.entity.SysUserDO;
import com.titxu.cloud.sys.infrastructure.persistence.entity.SysUserRoleDO;
import com.titxu.cloud.sys.infrastructure.persistence.mapper.SysAccountMapper;
import com.titxu.cloud.sys.infrastructure.persistence.mapper.SysRoleMapper;
import com.titxu.cloud.sys.infrastructure.persistence.mapper.SysUserMapper;
import com.titxu.cloud.sys.infrastructure.persistence.mapper.SysUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户-Repository实现类
 **/
@Repository
public class UserRepositoryImpl extends ServiceImpl<SysUserMapper, SysUserDO> implements UserRepository, IService<SysUserDO> {


    private SysUserRoleMapper sysUserRoleMapper;


    private SysRoleMapper sysRoleMapper;


    private SysAccountMapper sysAccountMapper;


    @Autowired
    public void setSysUserRoleMapper(SysUserRoleMapper sysUserRoleMapper) {
        this.sysUserRoleMapper = sysUserRoleMapper;
    }

    @Autowired
    public void setSysRoleMapper(SysRoleMapper sysRoleMapper) {
        this.sysRoleMapper = sysRoleMapper;
    }

    @Autowired
    public void setSysAccountMapper(SysAccountMapper sysAccountMapper) {
        this.sysAccountMapper = sysAccountMapper;
    }




    @Override
    public User find(UserId userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId.getId());
        SysUserDO sysUserDO = baseMapper.queryUser(params);
        if (sysUserDO == null) {
            return null;
        }
        return UserConverter.toUser(sysUserDO, getUserAccount(sysUserDO.getAccountId()), getUserRoleIds(sysUserDO.getId()));
    }

    @Override
    public List<User> find(Mobile mobile) {
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", mobile.getMobile());
        List<SysUserDO> sysUserDOList = baseMapper.queryUserNoTenant(params);
        if (sysUserDOList == null || sysUserDOList.isEmpty()) {
            return null;
        }
        List<User> users = new ArrayList<>();
        for (SysUserDO sysUserDO : sysUserDOList) {
            User user = UserConverter.toUser(sysUserDO, getUserAccount(sysUserDO.getAccountId()), getUserRoleIds(sysUserDO.getId()));
            users.add(user);
        }
        return users;
    }

    @Override
    public UserId store(User user) {
        /*
          获取数据库中user的数据，设置到并把前端数据set进去
         */
        User dataBaseUser = dataBaseToUser(user);

        SysAccountDO sysAccountDO = UserConverter.getSysAccountDO(dataBaseUser);
        String accountId = null;
        if (sysAccountDO != null) {
            if (sysAccountDO.getId() != null) {
                sysAccountMapper.updateById(sysAccountDO);
            } else {
                sysAccountMapper.insert(sysAccountDO);
            }
            accountId = sysAccountDO.getId();
        }
        if (TenantContext.getTenantId() != null) {
            SysUserDO sysUserDO = UserConverter.fromUser(dataBaseUser, accountId);
            this.saveOrUpdate(sysUserDO);
            String userId = sysUserDO.getId();
            //先删除用户与角色关系
            List<String> userIds = new ArrayList<>();
            userIds.add(userId);
            sysUserRoleMapper.deleteByUserIds(userIds);
            List<RoleId> roleIds = dataBaseUser.getRoleIds();
            if (roleIds != null && !roleIds.isEmpty()) {
                //保存角色与菜单关系
                for (RoleId roleId : roleIds) {
                    SysUserRoleDO sysUserRoleDO = new SysUserRoleDO();
                    sysUserRoleDO.setUserId(userId);
                    sysUserRoleDO.setRoleId(roleId.getId());
                    sysUserRoleMapper.insert(sysUserRoleDO);
                }
            }
            return new UserId(sysUserDO.getId());
        } else {
            return null;
        }
    }

    private User dataBaseToUser(User user) {
        // 如果userId为空，说明是新增用户，直接返回
        if (user.getUserId() == null) {
            return user;
        }
        Account account = user.getAccount();
        User dataBaseUser = this.find(user.getUserId());
        Account dataBaseAccount = dataBaseUser.getAccount();

        if (user.getStatus() != null) {
            dataBaseUser.setStatus(user.getStatus());
        }
        if (user.getRoleIds() != null) {
            dataBaseUser.setRoleIds(user.getRoleIds());
        }
        if (account.getEmail() != null) {
            dataBaseAccount.setEmail(account.getEmail());
        }
        if (account.getMobile() != null) {
            dataBaseAccount.setMobile(account.getMobile());
        }
        if (user.getUserNick() != null) {
            dataBaseUser.setUserNick(user.getUserNick());
        }
        if (user.getRemarks() != null) {
            dataBaseUser.setRemarks(user.getRemarks());
        }
        return dataBaseUser;
    }

    @Override
    public void remove(List<UserId> userIds) {
        List<String> ids = new ArrayList<>();
        userIds.forEach(userId -> ids.add(userId.getId()));
        this.removeByIds(ids);
        sysUserRoleMapper.deleteByUserIds(ids);
    }

    /**
     * 添加账号
     *
     * @param accountId 账号id
     */
    private Account getUserAccount(String accountId) {
        SysAccountDO sysAccountDO = sysAccountMapper.selectById(accountId);
        if (sysAccountDO == null) {
            return null;
        }
        return UserConverter.toAccount(sysAccountDO);
    }

    /**
     * 获取管理角色Id
     *
     * @param userId 用户id
     */
    private List<RoleId> getUserRoleIds(String userId) {
        List<RoleId> roleIdList = null;
        // 如果是超级管理员
        List<SysRoleDO> sysRoleDOList = sysRoleMapper.queryUserRole(userId);
        if (sysRoleDOList != null && !sysRoleDOList.isEmpty()) {
            roleIdList = new ArrayList<>();
            for (SysRoleDO sysRoleDO : sysRoleDOList) {
                roleIdList.add(new RoleId(sysRoleDO.getId()));
            }
        }
        return roleIdList;
    }

}
