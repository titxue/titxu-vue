package com.titxu.cloud.common.log.aspect;

import com.google.gson.Gson;
import com.titxu.cloud.common.core.util.HttpContextUtils;
import com.titxu.cloud.common.core.util.IPUtils;
import com.titxu.cloud.common.core.util.WebUtils;
import com.titxu.cloud.common.log.annotation.SysLog;
import com.titxu.cloud.sys.dto.LogDTO;
import com.titxu.cloud.sys.feign.RemoteLogSaveService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 系统日志，切面处理类
 **/
@Aspect
@Component
public class SysLogAspect {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    private RemoteLogSaveService remoteLogSaveService;

    @Resource
    public void setRemoteLogSaveService(RemoteLogSaveService remoteLogSaveService) {
        this.remoteLogSaveService = remoteLogSaveService;
    }


    @Pointcut("@annotation(com.titxu.cloud.common.log.annotation.SysLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        try {
            //保存日志
            saveSysLog(point, time);
        } catch (Exception e) {
            logger.error("保存日志失败：" + e.getMessage());
        }

        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        LogDTO logDTO = new LogDTO();
        logDTO.setTime(time);
        logDTO.setUserName(WebUtils.getUserName());
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SysLog syslog = method.getAnnotation(SysLog.class);
        if (syslog != null) {
            //注解上的描述
            String operation = syslog.value();
            logDTO.setOperation(operation);
        }
        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        String methodString = className + "." + methodName + "()";
        logDTO.setMethod(methodString);
        //请求的参数
        Object[] args = joinPoint.getArgs();
        try {
            String params = new Gson().toJson(args);
            logDTO.setParams(params);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        //获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        //设置IP地址
        String ip = IPUtils.getIpAddr(request);
        logDTO.setIp(ip);
        remoteLogSaveService.saveLog(logDTO);
    }
}
