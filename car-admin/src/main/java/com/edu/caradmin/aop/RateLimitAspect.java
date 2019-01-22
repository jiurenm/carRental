package com.edu.caradmin.aop;

import com.edu.car.dto.Results;
import com.edu.caradmin.annotation.RateLimit;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 限流
 *
 * @author Administrator
 * @date 2019/1/22 10:32
 */
@Slf4j
@Component
@Scope
@Aspect
public class RateLimitAspect {
    private ConcurrentMap<String, RateLimiter> map = new ConcurrentHashMap<>();
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("@annotation(com.edu.caradmin.annotation.RateLimit)")
    public void limit() {}

    @Around("limit()")
    public Object around(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Object obj = null;
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Object target = joinPoint.getTarget();
        Method currentMethod = target.getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        RateLimit annotation = currentMethod.getAnnotation(RateLimit.class);
        int limitNum = annotation.limitNum();
        String functionName = methodSignature.getName();
        RateLimiter rateLimiter;
        if (map.containsKey(functionName)) {
            rateLimiter = map.get(functionName);
        } else {
            map.put(functionName, RateLimiter.create(limitNum));
            rateLimiter = map.get(functionName);
        }
        try {
            if (rateLimiter.tryAcquire()) {
                obj = joinPoint.proceed();
            } else {
                return new Results().failed("系统繁忙");
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace( );
        }
        return obj;
    }

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}
