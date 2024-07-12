package com.bjpowernode.aop;

import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.bjpowernode.anno.Log;
import com.bjpowernode.domain.SysLog;
import com.bjpowernode.pool.ManagerThreadPool;
import com.bjpowernode.service.SysLogService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import springfox.documentation.spring.web.json.Json;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;

@Component
@Aspect
@RequiredArgsConstructor
public class LogAspect {
    private final SysLogService sysLogService;
    @Around("@annotation(com.bjpowernode.anno.Log)")
    public Object aroundAnnotationAdvice(ProceedingJoinPoint joinPoint) {
        //用户名
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        //方法名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        StringBuilder stringBuilder = new StringBuilder();
        Object[] array = Arrays.stream(method.getParameterTypes()).map(Class::getTypeName).toArray();
        for (int i = 0; i < array.length; i++) {
            if (i != array.length - 1) {
                stringBuilder.append(array[i]).append(",");
            } else {
                stringBuilder.append(array[i]);
            }
        }
        String methodName = Modifier.toString(method.getModifiers()) + " "+
                method.getReturnType().getTypeName() +
                method.getDeclaringClass().getTypeName() + "." +
                method.getName() +
                "(" + stringBuilder + ")";
        //操作信息
        Log annotation = method.getAnnotation(Log.class);
        String operation = annotation.operation();
        //参数
        final Object[] args = joinPoint.getArgs();
        //ip
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String ip = requestAttributes.getRequest().getRemoteAddr();
        Object rtValue = null;
        //执行前的时间
        Long start = System.currentTimeMillis();
        try {
            rtValue = joinPoint.proceed(args);//明确调用切入点方法（切入点方法）
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        //执行时间
        Long end = System.currentTimeMillis();
        Long time = end - start;
        //用户名
        ManagerThreadPool.threadPoolExecutor.execute(
                () -> {
                    sysLogService.save(
                            SysLog.builder()
                                    .username(userId)
                                    .operation(operation)
                                    .method(methodName)
                                    .params(JSON.toJSONString(args))
                                    .time(time)
                                    .ip(ip)
                                    .createDate(new DateTime())
                                    .build()
                    );
                }
        );
        return rtValue;
    }
}
