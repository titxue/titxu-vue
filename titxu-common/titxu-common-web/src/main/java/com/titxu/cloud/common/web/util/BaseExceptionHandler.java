package com.titxu.cloud.common.web.util;

import com.titxu.cloud.common.core.exception.BaseException;
import com.titxu.cloud.common.web.constant.ResultCode;
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
    public Result handlerBaseException(BaseException e) {
        Result r = new Result();
        r.put("code", e.getCode());
        r.put("msg", e.getMessage());
        log.error(e.getMessage(), e);
        return r;
    }

    /**
     * 处理参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result handlerIllegalArgumentException(IllegalArgumentException e) {
        Result r = new Result();
        r.put("code", ResultCode.PARAM_ERROR.getCode());
        r.put("msg", e.getMessage());
        log.error(e.getMessage(), e);
        return r;
    }

    @ExceptionHandler(RuntimeException.class)
    public Result handlerRuntimeException(RuntimeException e) {
        Result r = new Result();
        r.put("code", ResultCode.DELETE_SYSTEM_ERROR.getCode());
        r.put("msg", ResultCode.DELETE_SYSTEM_ERROR.getMsg());
        log.error(e.getMessage(), e);
        return r;
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error(e.getMessage(), e);
        return Result.error();
    }
}
