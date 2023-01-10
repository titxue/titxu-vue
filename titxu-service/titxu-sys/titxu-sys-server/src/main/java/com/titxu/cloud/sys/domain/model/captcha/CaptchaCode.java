package com.titxu.cloud.sys.domain.model.captcha;

import com.titxu.cloud.common.core.domain.ValueObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.regex.Pattern;

/**
 * 验证码
 **/
public class CaptchaCode implements ValueObject<CaptchaCode> {

    private static final Pattern VALID_PATTERN = Pattern.compile("^[A-Za-z0-9]+$");
    private String code;


    public CaptchaCode(final String code) {
        if (StringUtils.isEmpty(code)) {
            throw new IllegalArgumentException("验证码不能为空");
        }
        Validate.isTrue(VALID_PATTERN.matcher(code).matches(),
                "编码格式不正确");
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean sameValueAs(CaptchaCode other) {
        return other != null && this.code.equals(other.code);
    }

    @Override
    public String toString() {
        return code;
    }
}
