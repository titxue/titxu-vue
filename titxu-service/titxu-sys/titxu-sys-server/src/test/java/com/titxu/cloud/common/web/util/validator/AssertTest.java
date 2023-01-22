package com.titxu.cloud.common.web.util.validator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AssertTest {

    @Test
    void isBlank() {
        Assert.isBlank("test","test");
    }

    @Test
    void isNull() {
        Assert.isNull("test","test");
    }

}