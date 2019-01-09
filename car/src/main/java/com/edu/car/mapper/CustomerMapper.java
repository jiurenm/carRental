package com.edu.car.mapper;

import com.edu.car.model.Customer;
import com.edu.car.model.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2018/12/29 22:17
 */
@Mapper
public interface CustomerMapper {
    @Select("SELECT c.id,c.username,c.name,c.age,c.tel,c.email,c.driveNum,c.driveType,l.password,l.isEnable FROM car.customer c JOIN car.login l ON l.id=c.id")
    List<Customer> showCustomers();

    @Select("SELECT c.id,c.username,c.name,c.age,c.tel,c.email,c.driveNum,c.driveType,l.password,l.isEnable FROM car.customer c JOIN car.login l ON l.id=c.id WHERE c.username=#{username}")
    Customer findCustomer(String username);

    @Select("SELECT c.id,c.username,c.name,c.age,c.tel,c.email,c.driveNum,c.driveType,l.password,l.isEnable FROM car.customer c JOIN car.login l ON l.id=c.id WHERE c.id=#{id}")
    Customer findCustomerById(Long id);

    @Select("SELECT ur.uid AS id,r.name FROM car.user_role ur JOIN car.role r ON r.id=ur.rid WHERE ur.uid=#{id}")
    List<Role> findRoles(Long id);

    @Select("SELECT ur.uid AS id,r.name FROM car.user_role ur JOIN car.role r ON r.id=ur.rid WHERE ur.uid=#{uid} AND r.id=#{id}")
    Role findRoleById(@Param("uid") Long uid, @Param("id") Integer id);
}
