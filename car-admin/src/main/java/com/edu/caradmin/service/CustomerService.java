package com.edu.caradmin.service;

import com.edu.car.model.Customer;
import com.edu.car.model.Role;
import com.edu.caradmin.dto.CustomerDto;

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
    void addAuthority(Long id, Long uid, Integer rid);
    List<Role> showRoles(Long id);
    Role showRolesById(Integer id);
    Role findRoleById(Long uid, Integer id);
    void deleteAuthority(Long uid, Integer rid);
    void editCustomer(CustomerDto customerDto);
    List<Customer> blackList();
    void cancelBlackList(Long id);
}
