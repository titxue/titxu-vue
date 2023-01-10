package com.titxu.cloud.sys.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.titxu.cloud.common.mybatis.util.BaseDO;
import lombok.Data;

/**
 * 用户Token DO
 **/
@Data
@TableName("sys_account")
public class SysAccountDO extends BaseDO {

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

}
