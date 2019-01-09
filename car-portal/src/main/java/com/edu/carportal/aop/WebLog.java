package com.edu.carportal.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;

/**
 * 日志
 *
 * @author Administrator
 * @date 2019/1/9 11:01
 */
@Slf4j
@Aspect
@Component
public class WebLog {
    @Pointcut("execution(public * com.edu.carportal.controller.*.*(..))")
    public void webLog() {}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("请求处理");
        Signature signature = joinPoint.getSignature();
        log.info("方法：" + signature.getName());
        log.info("方法所在包：" + signature.getDeclaringTypeName());
        signature.getDeclaringType();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] strings = methodSignature.getParameterNames();
        log.info("参数名：" + Arrays.toString(strings));
        log.info("参数值：" + Arrays.toString(joinPoint.getArgs()));
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        log.info("请求URL：" + request.getRequestURL().toString());
        log.info("HTTP_METHOD：" + request.getMethod());
        log.info("IP：" + request.getRemoteAddr());
        log.info("CLASS_METHOD：" + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
    }

    @After("webLog()")
    public void after() {
        log.info("请求结束");
    }

    @AfterReturning(pointcut = "webLog()", returning = "ret")
    public void doAfterReturning(Object ret) {
        log.info("返回：" + ret);
    }

    @AfterThrowing(pointcut = "webLog()", throwing = "e")
    public void doAfterThrowing(Throwable e) {
        log.debug("异常执行");
        log.debug("异常代码：" + e.getClass().getName());
        log.debug("异常信息：" + e.getMessage());
    }

    @Around("webLog()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) {
        try {
            return proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("异常：", throwable.getCause());
            return null;
        }
    }
}
