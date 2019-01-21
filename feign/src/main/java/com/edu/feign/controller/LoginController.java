package com.edu.feign.controller;

import com.edu.feign.dto.LoginDto;
import com.edu.feign.dto.Results;
import com.edu.feign.service.AdminClient;
import feign.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/1 12:50
 */
@RestController
public class LoginController {
    private final AdminClient adminClient;

    @Autowired
    public LoginController(AdminClient adminClient) {
        this.adminClient = adminClient;
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Results login(@RequestBody LoginDto loginDto) {
        return adminClient.login(loginDto);
    }

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @RequestMapping(value = "token/refresh", method = RequestMethod.GET)
    public Results refreshToken() {
        return adminClient.refreshToken();
    }
}