package com.titxu.cloud.common.core.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.titxu.cloud.common.core.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求工具类
 **/
@Slf4j
public class RequestUtils {

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static JSONObject getJwtPayload() {
        String jwtPayload = getRequest().getHeader(CommonConstant.JWT_PAYLOAD_KEY);
        return JSONUtil.parseObj(jwtPayload);
    }

    public static String getUserId() {
        return getJwtPayload().getStr(CommonConstant.USER_ID);
    }


    public static String getUserName() {
        return getJwtPayload().getStr(CommonConstant.USER_NAME);
    }
}
