package com.edu.carportal.controller;

import com.edu.car.dto.Results;
import com.edu.car.model.Customer;
import com.edu.car.uid.IdWorker;
import com.edu.carportal.dto.RegisterDto;
import com.edu.carportal.service.LoginService;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/8 14:20
 */
@RestController
public class LoginController {
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    private final PasswordEncoder passwordEncoder;
    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService, PasswordEncoder passwordEncoder) {
        this.loginService = loginService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Results login(@RequestBody RegisterDto registerDto) {
        String token = loginService.login(registerDto.getUsername(), registerDto.getPassword());
        if (Strings.isNullOrEmpty(token)) {
            return new Results().failed();
        }
        ImmutableMap tokenMap = ImmutableMap.of("token",token,"tokenHead",tokenHead);
        return new Results().success(tokenMap);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Results register(@RequestBody @Validated RegisterDto registerDto) {
        Customer customer = loginService.findCustomerByName(registerDto.getUsername());
        if (customer != null) {
            return new Results().failed("已注册");
        }
        String pwd = passwordEncoder.encode(registerDto.getPassword());
        Long uid = IdWorker.getId();
        int result = loginService.register(uid, uid, pwd, registerDto.getUsername());
        if (result == 1) {
            return new Results( ).success(result);
        } else {
            return new Results().failed();
        }
    }
}
