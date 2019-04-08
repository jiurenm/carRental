package com.edu.carportal.service.impl;

import com.edu.car.mapper.CustomerMapper;
import com.edu.car.model.Customer;
import com.edu.car.util.JwtTokenUtil;
import com.edu.carportal.dao.LoginMapper;
import com.edu.carportal.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/8 14:18
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final LoginMapper loginMapper;
    private final CustomerMapper customerMapper;

    @Autowired
    public LoginServiceImpl(LoginMapper loginMapper, CustomerMapper customerMapper, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.loginMapper = loginMapper;
        this.customerMapper = customerMapper;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            log.error("登录异常：{}", e.getMessage());
            return null;
        }
    }

    @Override
    public int register(Long uid, Long id, String password, String username) {
        try {
            loginMapper.register1(uid, password);
            loginMapper.register2(uid, username, username);
            loginMapper.register3(id, uid);
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }

    @Override
    public Customer findCustomerByName(String username) {
        return customerMapper.findCustomer(username);
    }

    @Override
    public int changePhone(String username, String phone) {
        Customer customer = customerMapper.findCustomer(username);
        return loginMapper.changePhone(Long.valueOf(customer.getId()), phone);
    }
}
