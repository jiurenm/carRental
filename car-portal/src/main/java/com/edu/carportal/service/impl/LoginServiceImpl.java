package com.edu.carportal.service.impl;

import com.edu.car.mapper.CustomerMapper;
import com.edu.car.model.Customer;
import com.edu.carportal.dao.LoginMapper;
import com.edu.carportal.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/8 14:18
 */
@Service
public class LoginServiceImpl implements LoginService {
    private final LoginMapper loginMapper;
    private final CustomerMapper customerMapper;

    @Autowired
    public LoginServiceImpl(LoginMapper loginMapper, CustomerMapper customerMapper) {
        this.loginMapper = loginMapper;
        this.customerMapper = customerMapper;
    }

    @Override
    public int register(Long id, String password, String username) {
        try {
            loginMapper.register1(id, password);
            loginMapper.register2(id, username, username);
            loginMapper.register3(id, id);
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }

    @Override
    public Customer findCustomerByName(String username) {
        return customerMapper.findCustomer(username);
    }
}
