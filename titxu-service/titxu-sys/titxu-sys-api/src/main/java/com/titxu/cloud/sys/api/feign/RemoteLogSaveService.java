package com.titxu.cloud.sys.api.feign;

import com.titxu.cloud.common.core.constant.AuthConstants;
import com.titxu.cloud.sys.api.dto.LogDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(contextId = "remoteLogSaveService", value = "titxu-server-sys")
public interface RemoteLogSaveService {
    /**
     * 保存日志
     *
     * @param logDTO 日志实体
     */
    @PostMapping(value = "/remote/log/saveLog", headers = AuthConstants.HEADER_FROM_IN)
    void saveLog(@RequestBody LogDTO logDTO);
}
