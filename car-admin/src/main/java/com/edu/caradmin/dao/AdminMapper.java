package com.edu.caradmin.dao;

import com.edu.caradmin.dto.CustomerDto;
import org.apache.ibatis.annotations.*;
import org.springframework.security.core.parameters.P;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/8 11:11
 */
@Mapper
public interface AdminMapper {
    @Insert("INSERT INTO user_role(id,uid,rid) VALUES (#{id},#{uid},#{rid})")
    void addAuthority(Long id, Long uid, Integer rid);

    @Delete("DELETE FROM user_role WHERE uid=#{uid} AND rid=#{rid}")
    void deleteAuthority(@Param("uid") Long uid, @Param("rid") Integer rid);

    @Update("UPDATE car.login SET isEnable=0 WHERE id=#{id}")
    void setBlackList(Long id);

    @Update("UPDATE car.customer c SET c.name=#{name}," +
            "c.age=#{age}," +
            "c.tel=#{tel}," +
            "c.email=#{email}," +
            "c.driveNum=#{driveNum}," +
            "c.driveType=#{driveType} WHERE c.id=#{id}")
    void editCustomer(@Param("name") String name, @Param("age") Integer age,
                      @Param("tel") String tel, @Param("email") String email,
                      @Param("driveNum") String driveNum, @Param("driveType") String driveType,
                      @Param("id") Long id);
}
