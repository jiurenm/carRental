package com.edu.caradmin.service;

import com.edu.car.model.Customer;
import com.edu.car.model.Role;
import com.edu.caradmin.dto.AuthorityDto;
import com.edu.caradmin.dto.CustomerDto;

import java.util.List;

/**
 * CustomerService
 *
 * @author Administrator
 * @date 2019/1/3 10:52
 */
public interface CustomerService {
    /**
     * 全部用户
     *
     * @return 全部用户信息
     */
    List<Customer> showCustomers();

    /**
     * 通过username查找用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    Customer findCustomerByName(String username);

    /**
     * 通过id查找用户
     *
     * @param id 用户id
     * @return 用户信息
     */
    Customer findCustomerById(Long id);

    /**
     * 设置黑名单
     *
     * @param id 用户id
     * @return 成功:1
     */
    int setBlackList(Long id);

    /**
     * 增加权限
     *
     * @param authorityDto 权限
     * @return 成功:1
     */
    int addAuthority(List<AuthorityDto> authorityDto);

    /**
     * 通过id查找权限
     *
     * @param id 权限id
     * @return 权限
     */
    Role showRolesById(Integer id);

    /**
     * 通过用户id查找用户权限
     *
     * @param id 用户id
     * @return 权限
     */
    List<Role> showRoles(Long id);

    /**
     * 查找权限
     *
     * @param uid 用户id
     * @param id 权限id
     * @return 权限
     */
    Role findRoleById(Long uid, Integer id);

    /**
     * 删除权限
     *
     * @param authorityList 权限
     * @return 成功:1
     */
    int deleteAuthority(List<AuthorityDto> authorityList);

    /**
     * 编辑用户
     *
     * @param customerDto 用户
     * @return 成功:1
     */
    int editCustomer(CustomerDto customerDto);

    /**
     * 黑名单
     *
     * @return 黑名单
     */
    List<Customer> blackList();

    /**
     * 取消黑名单
     *
     * @param id 用户id
     * @return 成功:1
     */
    int cancelBlackList(Long id);
}
