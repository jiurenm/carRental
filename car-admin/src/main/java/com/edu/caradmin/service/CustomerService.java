package com.edu.caradmin.service;

import com.edu.car.model.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/3 10:52
 */
public interface CustomerService {
    List<Customer> showCustomers();
    Customer findCustomerByName(String username);
    Customer findCustomerById(Long id);
    void setBlackList(Long id);
}
