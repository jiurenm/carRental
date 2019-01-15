package com.edu.caradmin.service.impl;

import com.edu.car.mapper.CustomerMapper;
import com.edu.car.model.Customer;
import com.edu.car.model.Role;
import com.edu.caradmin.dao.AdminMapper;
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
    private final AdminMapper adminMapper;

    @Autowired
    public CustomerServiceImpl(CustomerMapper customerMapper, AdminMapper adminMapper) {
        this.customerMapper = customerMapper;
        this.adminMapper = adminMapper;
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
        adminMapper.setBlackList(id);
    }

    @Override
    public void addAuthority(Long id, Long uid, Integer rid) {
        adminMapper.addAuthority(id, uid, rid);
    }

    @Override
    public List<Role> showRoles(Long id) {
        return customerMapper.findRoles(id);
    }

    @Override
    public Role findRoleById(Long uid, Integer id) {
        return customerMapper.findRoleById(uid, id);
    }

    @Override
    public void deleteAuthority(Long uid, Integer rid) {
        adminMapper.deleteAuthority(uid, rid);
    }
}
