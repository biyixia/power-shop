package com.bjpowernode.feign;

import com.bjpowernode.domain.UserAddr;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("member-service")
public interface MemberFeign {
    @GetMapping("/p/address/getDefaultAddr")
    public UserAddr getDefaultAddr(@RequestParam("userId") String userId);
}
