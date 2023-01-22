package com.titxu.cloud.common.core.util;


import com.titxu.cloud.common.core.constant.ResultCode;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;


/**
 * 返回数据
 */
@Data
@Accessors(chain = true)
public class Result<T> implements Serializable {


    @Serial
    private static final long serialVersionUID = -7707422634279737658L;

    private int code;

    private String msg;

    private T data;

    public Result() {
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Result<?> error() {
        return error(ResultCode.SERVER_ERROR.getCode(), ResultCode.SERVER_ERROR.getMsg());
    }

    public static Result<?> error(String msg) {
        return error(ResultCode.SERVER_ERROR.getCode(), msg);
    }

    public static Result<?> error(int code, String msg) {
        return new Result<>(code, msg);
    }


    public static Result<?> ok() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg());
    }

    public static Result<?> ok(String msg) {
        return new Result<>(ResultCode.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg());
        result.setData(data);
        return result;
    }



}
