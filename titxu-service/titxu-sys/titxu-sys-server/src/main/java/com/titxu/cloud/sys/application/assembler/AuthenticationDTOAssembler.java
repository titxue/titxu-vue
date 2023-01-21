package com.titxu.cloud.sys.application.assembler;

import com.titxu.cloud.sys.api.dto.AuthenticationDTO;
import com.titxu.cloud.sys.domain.model.user.User;

/**
 * Assembler class for the AuthenticationDTOAssembler.
 **/
public class AuthenticationDTOAssembler {

    public static AuthenticationDTO fromUser(final User user) {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setUserId(user.getUserId().getId());
        authenticationDTO.setMobile(user.getAccount().getMobile().getMobile());
        authenticationDTO.setUserNick(user.getUserNick().getName());
        authenticationDTO.setPassword(user.getAccount().getPassword().getPassword());
        authenticationDTO.setTenantId(user.getTenantId().getId());
        authenticationDTO.setStatus(user.getStatus().getValue());
        return authenticationDTO;
    }
}
