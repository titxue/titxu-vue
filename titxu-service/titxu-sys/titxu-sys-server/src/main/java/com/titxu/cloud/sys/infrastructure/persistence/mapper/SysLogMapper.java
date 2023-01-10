package com.titxu.cloud.sys.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.titxu.cloud.sys.infrastructure.persistence.entity.SysLogDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 日志Mapper
 **/
@Mapper
public interface SysLogMapper extends BaseMapper<SysLogDO> {

}
