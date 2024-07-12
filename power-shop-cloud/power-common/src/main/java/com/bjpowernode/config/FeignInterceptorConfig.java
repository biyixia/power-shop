package com.bjpowernode.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class FeignInterceptorConfig implements RequestInterceptor {
    /**
     * 1.浏览器------>A------->B
     * 2.mq------->B
     * 3.微信/支付宝------>A-------->B
     *
     * @param template 发起远程调用的请求
     */
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            String authorization = request.getHeader("Authorization");
            if (StringUtils.hasText(authorization)) {
                // 把token 传递给下一次请求
                template.header("Authorization", authorization);
                return;
            }
        }
        // mq的请求场景 还是其他回调场景
        template.header("Authorization",
                "bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJhbGwiXSwiZXhwIjozODU5MDc0NDMzLCJqdGkiOiI2Mjg3OTQyMS0wMmY5LTQ4ZTktYWQzZS0wOTM2OTNhODNmNjYiLCJjbGllbnRfaWQiOiJ3ZWIifQ.Tivm3nOWRwckz523ETEoLlGNpVvkAYUQgXI17xPkkFtQSiEhn7yJ3Jl759RjSbz8Ou2WcdV6gi4URGseIjHSd_jmIuixu-RxFMYT2xDuLTZeWZTcUN-UJliVjMX3hraq1Qnfl4wI_4YnZEp8H0vewyRECEw0sQSDPPJfFQvYW_PA6nS-yhJGwgS_VLevkSRIaxTae0gLAjyZNHarZ1WRhXyRNkzG-Ul3ZA2P06jSul7S1tviIZ-q008R1EFXnBIZlYNaYaLqJaOVqbDxgYXu3Wr6zv8WELailItMh7R6CNLyU1BFIL4cMei1YXC0A-wi9piaz9XIAW-Qk4x0Sba_Rg"
        );
    }
}
