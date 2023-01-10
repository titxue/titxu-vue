package com.titxu.cloud.sys.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.titxu.cloud.sys.infrastructure.persistence.entity.SysUserRoleDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户角色关联Mapper
 **/
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRoleDO> {

    /**
     * 根据角色ID，批量删除
     */
    int deleteByRoleIds(List<String> roleIds);

    /**
     * 根据用户ID，批量删除
     */
    int deleteByUserIds(List<String> userIds);
}
