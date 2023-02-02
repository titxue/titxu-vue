/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.titxu.cloud.common.log.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.titxu.cloud.common.core.constant.CommonConstant;
import com.titxu.cloud.common.core.util.Result;
import com.titxu.cloud.common.core.util.SpringContextHolder;
import com.titxu.cloud.common.core.util.WebUtils;
import com.titxu.cloud.common.log.annotation.SysLog;
import com.titxu.cloud.common.log.event.SysLogEvent;
import com.titxu.cloud.common.log.util.SysLogUtils;
import com.titxu.cloud.sys.api.dto.LogDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;

import java.util.LinkedHashMap;

/**
 * 操作日志使用spring event异步入库
 */
@Aspect
@Slf4j
public class SysLogAspect {

    @Around("@annotation(sysLog)")
    @SneakyThrows
    public Object around(ProceedingJoinPoint point, SysLog sysLog) {
        String strClassName = point.getTarget().getClass().getName();
        String strMethodName = point.getSignature().getName();
        log.debug("[类名]:{},[方法]:{}", strClassName, strMethodName);

        String value = sysLog.value();
        String expression = sysLog.expression();
        // 当前表达式存在 SPEL，会覆盖 value 的值
        if (StrUtil.isNotBlank(expression)) {
            // 解析SPEL
            MethodSignature signature = (MethodSignature) point.getSignature();
            EvaluationContext context = SysLogUtils.getContext(point.getArgs(), signature.getMethod());
            try {
                value = SysLogUtils.getValue(context, expression, String.class);
            } catch (Exception e) {
                // SPEL 表达式异常，获取 value 的值
                log.error("@SysLog 解析SPEL {} 异常", expression);
            }
        }

        LogDTO logDTO = SysLogUtils.getSysLog();
        logDTO.setOperation(value);

        // 发送异步日志事件
        Long startTime = System.currentTimeMillis();
        Object obj;

        try {
            obj = point.proceed();
            // obj 转换为对象

            if (((Result<?>)obj).getData() instanceof LinkedHashMap){
                @SuppressWarnings(value = "unchecked")
                Result<LinkedHashMap<String,Object>> result = (Result<LinkedHashMap<String,Object>>)obj;
                LinkedHashMap<String, Object> data = result.getData();

                JSONObject userInfo = JSONUtil.parseObj(data.get("user_info"));

                if (userInfo!=null){
                    String userNick = userInfo.getStr("userNick");
                    String username = userInfo.getStr("username");
                    logDTO.setUserNick(userNick);
                    logDTO.setMobile(username);
                }
            }else {
                JSONObject jwtPayload = WebUtils.getJwtPayload();
                String userName = jwtPayload.getStr(CommonConstant.USER_NAME);
                String userNick = jwtPayload.getStr(CommonConstant.USER_NICK);
                String tenantId = jwtPayload.getStr(CommonConstant.TENANT_KEY);
                logDTO.setMobile(userName);
                logDTO.setUserNick(userNick);
                logDTO.setUserNick(tenantId);
            }






        } catch (Exception e) {
            logDTO.setParams(e.getMessage());
            throw e;
        } finally {
            Long endTime = System.currentTimeMillis();
            logDTO.setTime(endTime - startTime);
            SpringContextHolder.publishEvent(new SysLogEvent(logDTO));
        }

        return obj;
    }

}
