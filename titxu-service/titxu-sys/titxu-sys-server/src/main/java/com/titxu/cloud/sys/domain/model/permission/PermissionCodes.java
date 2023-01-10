package com.titxu.cloud.sys.domain.model.permission;

import com.titxu.cloud.common.core.domain.ValueObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.Set;

/**
 * 权限编码
 **/
public class PermissionCodes implements ValueObject<PermissionCodes> {

    private Set<String> codes;

    public PermissionCodes(final Set<String> codes) {
        Validate.notEmpty(codes);
        Validate.noNullElements(codes);

        this.codes = codes;
    }

    public Set<String> getCodes() {
        return codes;
    }

    public String getCodesString() {
        if (codes == null) {
            return null;
        }
        Object[] array = codes.toArray();
        return StringUtils.join(array, ",");
    }

    @Override
    public boolean sameValueAs(PermissionCodes other) {
        return other != null && codes.equals(other.codes);
    }

    @Override
    public String toString() {
        return "PermissionCodes{" +
                "codes=" + codes +
                '}';
    }
}
