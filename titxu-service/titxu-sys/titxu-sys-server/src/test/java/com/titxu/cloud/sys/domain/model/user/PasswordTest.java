package com.titxu.cloud.sys.domain.model.user;

import org.junit.jupiter.api.Test;

/**
 * 密码测试
 *

 
 **/
public class PasswordTest {

    @Test
    public void create() {
        System.out.println(Password.create("123456"));
    }
}
