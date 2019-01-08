package com.edu.caradmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author Administrator
 */
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "com.edu")
@RefreshScope
public class CarAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarAdminApplication.class, args);
    }

}

