package com.titxu.cloud.sys.domain.model.user;

import com.titxu.cloud.common.core.domain.ValueObject;
import org.apache.commons.lang3.StringUtils;

/**
 * 账号名
 **/
public class AccountName implements ValueObject<AccountName> {

    /**
     * 账号名
     */
    private String name;

    public AccountName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("账号名不能为空");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean sameValueAs(AccountName other) {
        return other != null && this.name.equals(other.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
