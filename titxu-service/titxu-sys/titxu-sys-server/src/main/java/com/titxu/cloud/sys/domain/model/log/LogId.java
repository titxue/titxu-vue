package com.titxu.cloud.sys.domain.model.log;

import com.titxu.cloud.common.core.domain.ValueObject;
import org.apache.commons.lang3.StringUtils;

/**
 * 日志ID
 **/
public class LogId implements ValueObject<LogId> {

    private String id;

    public LogId(final String id) {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("账号id不能为空");
        }
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean sameValueAs(LogId other) {
        return other != null && this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return id;
    }
}
