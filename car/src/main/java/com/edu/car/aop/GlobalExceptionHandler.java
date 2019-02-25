package com.edu.car.aop;

import com.edu.car.dto.Results;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 *
 * @author Administrator
 * @date 2019/1/24 13:20
 */
@Slf4j
@Order(1)
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public Results defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        return new Results().success(e.getMessage());
    }
}
