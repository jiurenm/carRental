package com.edu.caradmin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author Administrator
 */
@EnableDiscoveryClient
@MapperScan({"com.edu.caradmin.dao","com.edu.car.mapper"})
@SpringBootApplication(scanBasePackages = {"com.edu"})
@RefreshScope
@EnableCaching
public class CarAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarAdminApplication.class, args);
    }

}

