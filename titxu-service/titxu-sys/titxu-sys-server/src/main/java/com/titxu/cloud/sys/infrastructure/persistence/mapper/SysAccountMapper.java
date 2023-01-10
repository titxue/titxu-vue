package com.titxu.cloud.sys.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.titxu.cloud.sys.infrastructure.persistence.entity.SysAccountDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 账号Mapper
 **/
@Mapper
public interface SysAccountMapper extends BaseMapper<SysAccountDO> {
}
