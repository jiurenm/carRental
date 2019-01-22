package com.edu.car.aop;

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.mongodb.BasicDBObject;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 日志
 *
 * @author Administrator
 * @date 2019/1/11 14:14
 */
@Aspect
@Order(1)
@Component
public class Log {
    private final static Logger log = Logger.getLogger("mongodb");
    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Log.class);

    private Stopwatch stopwatch;

    private ListeningExecutorService executorService = MoreExecutors.listeningDecorator(
            new ScheduledThreadPoolExecutor(2,
                    new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build())
    );

    private ListenableFuture listenableFuture;
    private BasicDBObject logInfo;

    @Pointcut("execution(public * com.edu.*.controller.*.*(..))")
    public void webLog() {}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        stopwatch = Stopwatch.createStarted();
        LocalDateTime time = LocalDateTime.now();
        Signature signature = joinPoint.getSignature();
        LOGGER.info("请求处理");
        LOGGER.info("方法:" + signature.getName());
        LOGGER.info("方法所在包:" + signature.getDeclaringTypeName());
        signature.getDeclaringType();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] strings = methodSignature.getParameterNames();
        LOGGER.info("参数名:" + Arrays.toString(strings));
        LOGGER.info("参数值:" + Arrays.toString(joinPoint.getArgs()));
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        LOGGER.info("请求URL:" + request.getRequestURL().toString());
        LOGGER.info("HTTP_METHOD:" + request.getMethod());
        LOGGER.info("IP:" + request.getRemoteAddr());
        LOGGER.info("CLASS_METHOD:" + joinPoint.getSignature().getDeclaringTypeName() +
                "." + joinPoint.getSignature().getName());
        logInfo = this.getBasicDBObject(request, joinPoint, time);
        listenableFuture = executorService.submit(() -> {});
    }

    @After("webLog()")
    public void after() {
        LOGGER.info("请求结束");
    }

    @AfterReturning(pointcut = "webLog()", returning = "ret")
    public void doAfterReturning(Object ret) {
        LOGGER.info("返回：" + ret);
        LOGGER.info("耗时" + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
        listenableFuture.addListener(() -> log.info(logInfo), executorService);
    }

    private BasicDBObject getBasicDBObject(HttpServletRequest request, JoinPoint joinPoint, LocalDateTime time) {
        BasicDBObject object = new BasicDBObject();
        Signature signature = joinPoint.getSignature();
        object.append("请求时间", time);
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
