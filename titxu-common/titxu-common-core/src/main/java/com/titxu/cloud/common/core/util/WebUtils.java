package com.titxu.cloud.common.core.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.titxu.cloud.common.core.constant.CommonConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;


/**
 * 请求工具类
 **/
@Slf4j
@UtilityClass
public class WebUtils {

//    public  HttpServletRequest getRequest() {
//        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//    }

    /**
     * 获取 HttpServletRequest
     *
     * @return {HttpServletRequest}
     */
    public Optional<HttpServletRequest> getRequest() {
        return Optional
                .ofNullable(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
    }

    /**
     * 获取 HttpServletResponse
     *
     * @return {HttpServletResponse}
     */
    public HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }


    public JSONObject getJwtPayload() {
        String jwtPayload = getRequest().get().getHeader(CommonConstant.JWT_PAYLOAD_KEY);
        return JSONUtil.parseObj(jwtPayload);
    }

    public String getUserId() {
        return getJwtPayload().getStr(CommonConstant.USER_ID);
    }


    public String getUserName() {
        return getJwtPayload().getStr(CommonConstant.USER_NAME);
    }
}
