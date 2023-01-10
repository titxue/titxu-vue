package com.titxu.cloud.sys.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.titxu.cloud.sys.infrastructure.persistence.entity.SysCaptchaDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 验证码Mapper
 **/
@Mapper
public interface SysCaptchaMapper extends BaseMapper<SysCaptchaDO> {
}
