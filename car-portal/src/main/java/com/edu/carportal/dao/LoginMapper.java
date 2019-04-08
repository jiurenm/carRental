package com.edu.carportal.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/8 14:16
 */
@Mapper
public interface LoginMapper {
    @Insert("INSERT INTO login(id,password) VALUES (#{id},#{password})")
    int register1(@Param("id") Long id, @Param("password") String password);

    @Insert("INSERT INTO customer(id,tel,username) VALUES (#{id},#{tel},#{username})")
    int register2(@Param("id") Long id, @Param("tel") String tel, @Param("username") String username);

    @Insert("INSERT INTO user_role(id,uid,rid) VALUES (#{id},#{uid},2)")
    int register3(@Param("id") Long id, @Param("uid") Long uid);

    @Update("UPDATE customer c SET c.tel=#{tel} WHERE c.id=#{id}")
    int changePhone(@Param("id") Long id, @Param("tel") String tel);
}
