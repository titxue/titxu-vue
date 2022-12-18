package com.titxu.cloud.sys.application.assembler;

import com.titxu.cloud.sys.domain.model.user.User;
import com.titxu.cloud.sys.dto.AuthenticationDTO;

/**
 * Assembler class for the AuthenticationDTOAssembler.
 *

 
 **/
public class AuthenticationDTOAssembler {

    public static AuthenticationDTO fromUser(final User user) {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setUserId(user.getUserId().getId());
        authenticationDTO.setUserName(user.getUserName().getName());
        authenticationDTO.setPassword(user.getAccount().getPassword().getPassword());
        authenticationDTO.setTenantId(user.getTenantId().getId());
        authenticationDTO.setStatus(user.getStatus().getValue());
        return authenticationDTO;
    }
}
