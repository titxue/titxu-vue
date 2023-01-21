package com.titxu.cloud.common.security.domain;

import com.titxu.cloud.common.core.domain.StatusEnum;
import com.titxu.cloud.sys.api.dto.AuthenticationDTO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.util.HashMap;
import java.util.Map;


/**
 * 登录用户信息
 */
public class AuthUser extends User implements OAuth2AuthenticatedPrincipal {

    private static final long serialVersionUID = -6558907039263261134L;

    @Setter
    @Getter
    private String id;

//    @Setter
//    @Getter
//    @JsonIgnore
//    private String password;

    @Setter
    @Getter
    private String tenantId;

    @Setter
    @Getter
    private String userNick;

    public AuthUser(AuthenticationDTO authenticationDTO) {
        super(authenticationDTO.getMobile(), authenticationDTO.getPassword(), StatusEnum.ENABLE.getValue().equals(authenticationDTO.getStatus()),
                true, true, true, AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils.join(authenticationDTO.getPermissionCodes(), ",")));
        this.setId(authenticationDTO.getUserId());
        this.setUserNick(authenticationDTO.getUserNick());
//        this.setUsername(authenticationDTO.getUserName());
//        this.setPassword(AuthConstants.BCRYPT + authenticationDTO.getPassword());
//        this.setEnabled(St、atusEnum.ENABLE、.getValue().equals(authenticationDTO.getStatus()));
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
