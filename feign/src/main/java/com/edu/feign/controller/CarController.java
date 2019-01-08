package com.edu.feign.controller;

import com.edu.feign.service.AdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/7 15:29
 */
@RestController
public class CarController {
    private final AdminClient adminClient;

    @Autowired
    public CarController(AdminClient adminClient) {
        this.adminClient = adminClient;
    }
}
