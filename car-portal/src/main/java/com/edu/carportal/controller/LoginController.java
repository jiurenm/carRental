package com.edu.carportal.controller;

import com.edu.car.dto.Results;
import com.edu.car.model.Customer;
import com.edu.car.uid.IdWorker;
import com.edu.car.util.JwtTokenUtil;
import com.edu.carportal.dto.RegisterDto;
import com.edu.carportal.service.LoginService;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.loginService = loginService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
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

    @PreAuthorize(value = "hasRole('USER')")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Results getInfo(HttpServletRequest request) {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader(this.tokenHeader).substring(this.tokenHead.length()));
        Customer customer = loginService.findCustomerByName(username);
        return new Results().success(customer);
    }

    @PreAuthorize(value = "hasRole('USER')")
    @RequestMapping(value = "/changePhone/{phone}", method = RequestMethod.GET)
    public Results changePhone(HttpServletRequest request, @PathVariable String phone) {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader(this.tokenHeader).substring(this.tokenHead.length()));
        return new Results().success(loginService.changePhone(username, phone));
    }

    @RequestMapping(value = "/changePassword/{password}", method = RequestMethod.GET)
    public Results changePassword(HttpServletRequest request, @PathVariable String password) {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader(this.tokenHeader).substring(this.tokenHead.length()));
        return new Results().success(loginService.changePassword(username, password));
    }
}
