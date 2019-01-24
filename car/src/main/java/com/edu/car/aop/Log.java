package com.edu.car.aop;

import com.edu.car.dto.Results;
import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.mongodb.BasicDBObject;
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
import java.util.Map;
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

    private Map<Long, BasicDBObject> logs = Maps.newConcurrentMap();

    @Pointcut("execution(public * com.edu.*.controller.*.*(..))")
    public void webLog() {}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        Long num = IdWorker.getId();
        LOGGER.info("请求处理");
        LocalDateTime time = LocalDateTime.now();
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
        BasicDBObject logInfo = this.getBasicDBObject(request, joinPoint, time, num);
        logs.put(num, logInfo);
    }

    @After("webLog()")
    public void after() {
        LOGGER.info("请求结束");
    }

    @AfterReturning(pointcut = "webLog()", returning = "ret")
    public void doAfterReturning(Object ret) {
        LOGGER.info("返回：" + ret);
        synchronized(this) {
            Long num = (Long) getMinKey(logs);
            BasicDBObject logInfo = logs.get(num);
            this.write(logInfo, ret);
            logs.remove(num);
        }
    }

    @AfterThrowing(pointcut = "webLog()", throwing = "e")
    public void doAfterThrowing(Throwable e) {
        synchronized (this) {
            log.info("***异常!!!***");
            log.info("异常信息：" + e.getMessage());
            log.info("原因：" + e);
            BasicDBObject logInfo = logQueue.remove( );
            logInfo.append("异常", e.getMessage());
            logger.info(logInfo);
        }
    }

    private BasicDBObject getBasicDBObject(HttpServletRequest request, JoinPoint joinPoint, LocalDateTime time, Long id) {
        BasicDBObject object = new BasicDBObject();
        Signature signature = joinPoint.getSignature();
        object.append("_id", id);
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

    private void write(BasicDBObject logInfo, Object ret) {
        logInfo.append("返回：", "");
        logInfo.append("  code", String.valueOf(((Results)ret).getCode()));
        logInfo.append("  message", ((Results)ret).getMessage());
        logInfo.append("  data", ((Results)ret).getData().toString());
        logInfo.append("请求结束", LocalDateTime.now());
        LOGGER.info("写入mongodb");
        log.info(logInfo);
    }

    private static Object getMinKey(Map<Long, BasicDBObject> map) {
        if (map == null) {
            return null;
        }
        Set<Long> set = map.keySet();
        Object[] obj = set.toArray();
        Arrays.sort(obj);
        return obj[0];
    }

}
