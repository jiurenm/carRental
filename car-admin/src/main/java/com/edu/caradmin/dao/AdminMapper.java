package com.edu.caradmin.dao;


import com.edu.car.model.Customer;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * AdminMapper
 *
 * @author Administrator
 * @date 2019/1/8 11:11
 */
@Mapper
public interface AdminMapper {
    @Insert("INSERT INTO user_role(id,uid,rid) VALUES (#{id},#{uid},#{rid})")
    void addAuthority(Long id, Long uid, Integer rid);

    @Delete("DELETE FROM user_role WHERE uid=#{uid} AND rid=#{rid}")
    int deleteAuthority(@Param("uid") Long uid, @Param("rid") Integer rid);

    @Update("UPDATE car.login SET isEnable=0 WHERE id=#{id}")
    int setBlackList(Long id);

    @Update("UPDATE car.customer c SET c.name=#{name}," +
            "c.age=#{age}," +
            "c.tel=#{tel}," +
            "c.email=#{email}," +
            "c.driveNum=#{driveNum}," +
            "c.driveType=#{driveType} WHERE c.id=#{id}")
    int editCustomer(@Param("name") String name, @Param("age") Integer age,
                      @Param("tel") String tel, @Param("email") String email,
                      @Param("driveNum") String driveNum, @Param("driveType") String driveType,
                      @Param("id") Long id);

    @Select("SELECT c.id,c.username,c.name,c.age,c.tel,c.email,c.driveNum,c.driveType,l.password,l.isEnable FROM car.customer c JOIN car.login l ON l.id=c.id WHERE l.isEnable=0")
    List<Customer> blackList();

    @Update("UPDATE car.login SET isEnable=1 WHERE id=#{id}")
    int cancelBlackList(Long id);
}
