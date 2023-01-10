package com.titxu.cloud.sys.domain.model.tenant;

import com.titxu.cloud.common.core.domain.ValueObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.regex.Pattern;

/**
 * 租户编码
 **/
public class TenantCode implements ValueObject<TenantCode> {

    private static final Pattern VALID_PATTERN = Pattern.compile("^[A-Za-z0-9]+$");
    private String code;


    public TenantCode(final String code) {
        if (StringUtils.isEmpty(code)) {
            throw new IllegalArgumentException("租户编码不能为空");
        }
        Validate.isTrue(VALID_PATTERN.matcher(code).matches(),
                "编码格式不正确");
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean sameValueAs(TenantCode other) {
        return other != null && this.code.equals(other.code);
    }

    @Override
    public String toString() {
        return code;
    }
}
