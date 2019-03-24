package com.edu.carportal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author Administrator
 */
@EnableDiscoveryClient
@MapperScan({"com.edu.carportal.dao","com.edu.car.mapper"})
@SpringBootApplication(scanBasePackages = "com.edu")
@RefreshScope
public class CarPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarPortalApplication.class, args);
    }

}
