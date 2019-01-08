package com.edu.caradmin.service.impl;

import com.edu.car.mapper.CustomerMapper;
import com.edu.car.model.Customer;
import com.edu.caradmin.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/3 10:55
 */
@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerServiceImpl(CustomerMapper customerMapper) {
        this.customerMapper = customerMapper;
    }

    @Override
    public List<Customer> showCustomers() {
        return customerMapper.showCustomers();
    }

    @Override
    public Customer findCustomerByName(String username) {
        return customerMapper.findCustomer(username);
    }

    @Override
    public Customer findCustomerById(Long id) {
        return customerMapper.findCustomerById(id);
    }

    @Override
    public void setBlackList(Long id) {
        customerMapper.setBlackList(id);
    }
}
