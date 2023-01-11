package com.titxu.cloud.sys.feign;

import com.titxu.cloud.sys.dto.LogDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "titxu-server-sys")
public interface RemoteLogSaveService {
    /**
     * 保存日志
     *
     * @param logDTO 日志实体
     */
    @PostMapping("saveLog")
    void saveLog(@RequestBody LogDTO logDTO);
}
