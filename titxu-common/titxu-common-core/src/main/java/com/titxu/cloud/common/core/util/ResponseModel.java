package com.titxu.cloud.common.core.util;


import com.titxu.cloud.common.core.constant.ResultCode;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;


/**
 * 返回fast api 的数据格式
 */
@Data
@Accessors(chain = true)
public class ResponseModel<T> implements Serializable {


    @Serial
    private static final long serialVersionUID = -7707422634279737658L;

    private int status;

    private String msg;

    private T data;

    public ResponseModel() {
    }

    public ResponseModel(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public static ResponseModel<?> error() {
        return error(ResultCode.SERVER_ERROR.getCode(), ResultCode.SERVER_ERROR.getMsg());
    }

    public static ResponseModel<?> error(String msg) {
        return error(ResultCode.SERVER_ERROR.getCode(), msg);
    }
    public ResponseModel<T> errorMsg(String msg) {
        return new ResponseModel<>(ResultCode.SERVER_ERROR.getCode(), msg);
    }

    public static ResponseModel<?> error(int status, String msg) {
        return new ResponseModel<>(status, msg);
    }


    public static ResponseModel<?> ok() {
        return new ResponseModel<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg());
    }

    public static ResponseModel<?> ok(String msg) {
        return new ResponseModel<>(ResultCode.SUCCESS.getCode(), msg);
    }

    public static <T> ResponseModel<T> ok(T data) {
        ResponseModel<T> result = new ResponseModel<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg());
        result.setData(data);
        return result;
    }



}
