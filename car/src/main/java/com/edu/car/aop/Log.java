package com.edu.car.aop;

import com.edu.car.dto.Results;
import com.edu.car.uid.IdWorker;
import com.google.common.collect.Queues;
import com.mongodb.BasicDBObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 日志
 *
 * @author Administrator
 * @date 2019/1/11 14:14
 */
@Slf4j
@Aspect
@Component
public class Log {
    private final static Logger logger = Logger.getLogger("mongodb");

    @Pointcut("execution(public * com.edu.*.controller.*.*(..))")
    public void webLog() {}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        Long num = IdWorker.getId();
        log.info("请求处理");
        LocalDateTime time = LocalDateTime.now();
        log.info("方法:" + signature.getName());
        log.info("方法所在包:" + signature.getDeclaringTypeName());
        signature.getDeclaringType();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] strings = methodSignature.getParameterNames();
        log.info("参数名:" + Arrays.toString(strings));
        log.info("参数值:" + Arrays.toString(joinPoint.getArgs()));
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        log.info("请求URL:" + request.getRequestURL().toString());
        log.info("HTTP_METHOD:" + request.getMethod());
        log.info("IP:" + request.getRemoteAddr());
        log.info("CLASS_METHOD:" + joinPoint.getSignature().getDeclaringTypeName() +
                "." + joinPoint.getSignature().getName());
        BasicDBObject logInfo = this.getBasicDBObject(request, joinPoint, time, num);
        logger.info(logInfo);
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
        synchronized (this) {
            log.info("***异常!!!***");
            log.info("异常信息：" + e.getMessage());
            e.printStackTrace();
        }
    }

    private BasicDBObject getBasicDBObject(HttpServletRequest request, JoinPoint joinPoint, LocalDateTime time, Long id) {
        BasicDBObject object = new BasicDBObject();
        Signature signature = joinPoint.getSignature();
        object.append("_id", id);
        object.append("请求开始", time);
        object.append("方法", signature.getName());
        object.append("方法所在包", signature.getDeclaringTypeName());
        signature.getDeclaringType();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] strings = methodSignature.getParameterNames();
        object.append("参数名", Arrays.toString(strings));
        object.append("参数值", Arrays.toString(joinPoint.getArgs()));
        object.append("请求URL", request.getRequestURL().toString());
        object.append("HTTP_METHOD", request.getMethod());
        object.append("IP", request.getRemoteAddr());
        object.append("CLASS_METHOD", joinPoint.getSignature().getDeclaringTypeName() +
                "." + joinPoint.getSignature().getName());
        return object;
    }
}
