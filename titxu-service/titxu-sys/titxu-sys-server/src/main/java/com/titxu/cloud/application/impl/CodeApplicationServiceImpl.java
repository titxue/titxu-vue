package com.titxu.cloud.sys.application.impl;

import com.titxu.cloud.sys.application.CodeApplicationService;
import com.titxu.cloud.sys.domain.model.user.Account;
import com.titxu.cloud.sys.domain.model.user.Code;
import com.titxu.cloud.sys.domain.model.user.CodeCache;
import com.titxu.cloud.sys.domain.model.user.Mobile;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 验证码应用服务实现类
 **/
@Service
@AllArgsConstructor
public class CodeApplicationServiceImpl implements CodeApplicationService {


    private CodeCache codeCache;


    @Override
    public void sendCode(String mobile) {
        Account account = new Account(new Mobile(mobile));
        codeCache.save(account);
    }
}
