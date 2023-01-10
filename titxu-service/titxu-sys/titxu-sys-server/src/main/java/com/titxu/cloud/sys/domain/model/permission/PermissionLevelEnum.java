package com.titxu.cloud.sys.domain.model.permission;

import com.titxu.cloud.common.core.domain.ValueObject;
import org.apache.commons.lang3.StringUtils;

/**
 * 权限级别
 **/
public enum PermissionLevelEnum implements ValueObject<PermissionLevelEnum> {

    /**
     * 系统
     */
    SYSTEM("0", "系统"),

    /**
     * 租户
     */
    TENANT("1", "租户");


    private String value;

    private String label;

    PermissionLevelEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据匹配value的值获取Label
     *
     * @param value
     * @return
     */
    public static String getLabelByValue(String value) {
        if (StringUtils.isBlank(value)) {
            return "";
        }
        for (PermissionLevelEnum s : PermissionLevelEnum.values()) {
            if (value.equals(s.getValue())) {
                return s.getLabel();
            }
        }
        return "";
    }

    /**
     * 获取StatusEnum
     *
     * @param value
     * @return
     */
    public static PermissionLevelEnum getMenuLevelEnum(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        for (PermissionLevelEnum s : PermissionLevelEnum.values()) {
            if (value.equals(s.getValue())) {
                return s;
            }
        }
        return null;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean sameValueAs(final PermissionLevelEnum other) {
        return this.equals(other);
    }
}
