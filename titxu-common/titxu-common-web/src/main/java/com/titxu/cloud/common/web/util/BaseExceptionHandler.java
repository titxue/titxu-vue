package com.titxu.cloud.common.web.util;

import com.titxu.cloud.common.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 自定义异常处理器
 **/
@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(BaseException.class)
    public Result handleRRException(BaseException e) {
        Result r = new Result();
        r.put("code", e.getCode());
        r.put("msg", e.getMessage());
        log.error(e.getMessage(), e);
        return r;
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error(e.getMessage(), e);
        return Result.error();
    }
}
