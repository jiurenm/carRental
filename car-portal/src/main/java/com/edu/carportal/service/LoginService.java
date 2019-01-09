package com.edu.carportal.service;

import com.edu.car.model.Customer;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/8 14:01
 */
public interface LoginService {
    int register(Long id, String password, String username);
    Customer findCustomerByName(String username);
}
