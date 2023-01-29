package com.titxu.cloud.sys.domain.model.user;

import com.titxu.cloud.common.core.domain.ValueObject;
import org.apache.commons.lang3.Validate;

public record Remarks(String remarks) implements ValueObject<Remarks> {
    /**
     * Constructor.
     *
     * @param remarks 备注信息（前端签名）
     */
    public Remarks {
        if (remarks != null) {
            Validate.isTrue(remarks.length() <= 100,
                    "签名信息长度不能超过100");
        }
    }

    @Override
    public boolean sameValueAs(Remarks other) {
        return other != null && this.remarks.equals(other.remarks);
    }

    @Override
    public String remarks() {
        return remarks;
    }

    @Override
    public String toString() {
        return "Remarks{" +
                "remarks='" + remarks + '\'' +
                '}';
    }
}
