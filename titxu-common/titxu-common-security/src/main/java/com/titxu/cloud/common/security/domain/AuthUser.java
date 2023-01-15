package com.titxu.cloud.common.security.domain;

import com.titxu.cloud.common.core.constant.AuthConstants;
import com.titxu.cloud.common.core.domain.StatusEnum;
import com.titxu.cloud.sys.dto.AuthenticationDTO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;


/**
 * 登录用户信息
 */
public class AuthUser extends User implements OAuth2AuthenticatedPrincipal {
    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    @Setter
    @Getter
    private String id;

    @Setter
    @Getter
    private String password;

    @Setter
    @Getter
    private String tenantId;

    public AuthUser(AuthenticationDTO authenticationDTO) {
        super(authenticationDTO.getUserName(), authenticationDTO.getPassword(), StatusEnum.ENABLE.getValue().equals(authenticationDTO.getStatus()),
                true, true, true, AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils.join(authenticationDTO.getPermissionCodes(), ",")));
        this.setId(authenticationDTO.getUserId());
//        this.setUsername(authenticationDTO.getUserName());
        this.setPassword(AuthConstants.BCRYPT + authenticationDTO.getPassword());
//        this.setEnabled(StatusEnum.ENABLE、.getValue().equals(authenticationDTO.getStatus()));
        this.setTenantId(authenticationDTO.getTenantId());
//        this.authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils.join(authenticationDTO.getPermissionCodes(), ","));
    }

    /**
     * Get the OAuth 2.0 token attributes
     *
     * @return the OAuth 2.0 token attributes
     */
    @Override
    public Map<String, Object> getAttributes() {
        return new HashMap<>();
    }

    @Override
    public String getName() {
        return this.getUsername();
    }

}
