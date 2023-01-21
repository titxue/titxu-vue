package com.titxu.cloud.sys.application.impl;

import com.titxu.cloud.sys.api.dto.AuthenticationDTO;
import com.titxu.cloud.sys.api.service.AuthenticationService;
import com.titxu.cloud.sys.application.PermissionQueryService;
import com.titxu.cloud.sys.application.assembler.AuthenticationDTOAssembler;
import com.titxu.cloud.sys.domain.model.captcha.CaptchaCode;
import com.titxu.cloud.sys.domain.model.captcha.CaptchaRepository;
import com.titxu.cloud.sys.domain.model.captcha.Uuid;
import com.titxu.cloud.sys.domain.model.user.Mobile;
import com.titxu.cloud.sys.domain.model.user.User;
import com.titxu.cloud.sys.domain.model.user.UserId;
import com.titxu.cloud.sys.domain.model.user.UserRepository;
import com.titxu.cloud.sys.domain.service.CaptchaValidateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 身份验证应用服务实现类
 **/
@Service
public class AuthenticationServiceImpl implements AuthenticationService {


    private CaptchaRepository captchaRepository;
    private UserRepository userRepository;
    private PermissionQueryService permissionQueryService;

    @Autowired
    public void setCaptchaRepository(CaptchaRepository captchaRepository) {
        this.captchaRepository = captchaRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPermissionQueryService(PermissionQueryService permissionQueryService) {
        this.permissionQueryService = permissionQueryService;
    }

    @Override
    public boolean validateCaptcha(String uuid, String captchaCode) {
        if (StringUtils.isBlank(uuid) || StringUtils.isBlank(captchaCode)) {
            return false;
        }
        CaptchaValidateService captchaValidateService = new CaptchaValidateService(captchaRepository);
        return captchaValidateService.validate(new Uuid(uuid), new CaptchaCode(captchaCode));
    }

    @Override
    public AuthenticationDTO loginByUserName(String uerName) {
        List<User> users = new ArrayList<>();
        if (uerName.equals("超级管理员")) {
            User user = userRepository.find(new UserId("1"));
            users.add(user);
        } else {
            users = userRepository.find(new Mobile(uerName));
        }

        if (users == null || users.isEmpty()) {
            throw new RuntimeException("用户或密码不正确");
        }
        User user = users.get(0);
        AuthenticationDTO authenticationDTO = AuthenticationDTOAssembler.fromUser(user);
        authenticationDTO.setPermissionCodes(permissionQueryService.getPermissionCodes(user.getUserId().getId()));
        return authenticationDTO;
    }


}
