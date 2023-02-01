package com.titxu.cloud.sys.application.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.titxu.cloud.common.mybatis.util.Page;
import com.titxu.cloud.common.mybatis.util.PageAssembler;
import com.titxu.cloud.common.mybatis.util.Query;
import com.titxu.cloud.sys.application.LogQueryService;
import com.titxu.cloud.sys.application.command.PageCommand;
import com.titxu.cloud.sys.infrastructure.persistence.entity.SysLogDO;
import com.titxu.cloud.sys.infrastructure.persistence.mapper.SysLogMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 系统日志查询服务实现类
 **/
@Service
public class LogQueryServiceImpl implements LogQueryService {


    private SysLogMapper sysLogMapper;

    @Override
    public Page queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        IPage<SysLogDO> page = sysLogMapper.selectPage(
                new Query<SysLogDO>().getPage(params),
                new QueryWrapper<SysLogDO>().like(StringUtils.isNotBlank(key), "user_name", key)
        );
        return PageAssembler.toPage(page);
    }

    /**
     * 自定义查询字段
     * @param command 参数
     * @return 日志
     */
    @Override
    public Page queryPage(PageCommand command) {
        Map<String, Object> params = BeanUtil.beanToMap(command);
        IPage<SysLogDO> page = sysLogMapper.selectPage(
                new Query<SysLogDO>().getPage(params,"created_by",false),
                new QueryWrapper<SysLogDO>().like(StringUtils.isNotBlank(command.getValue()), command.getValue(), command.getValue())
        );
        return PageAssembler.toPage(page);
    }


    @Autowired
    public void setSysLogMapper(SysLogMapper sysLogMapper) {
        this.sysLogMapper = sysLogMapper;
    }
}
