package com.bjpowernode.feign;

import com.bjpowernode.domain.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("manager-service")
public interface ManagerFeign {
    @GetMapping("/sys/user/getById")
    public SysUser getById(@RequestParam("userId") Long userId);
}
