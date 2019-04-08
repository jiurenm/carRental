package com.edu.carportal.dao;

import com.edu.car.model.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/4/7 18:34
 */
@Mapper
public interface OrderMapper {
    @Insert("INSERT INTO `order`(id, getTime, returnTime, price, `status`, vid, uid) VALUES (#{id}, #{getTime}, #{returnTime}, #{price}, 1, #{vid}, #{uid})")
    int insert(@Param("id") Long id, @Param("getTime") String getTime, @Param("returnTime") String returnTime,
               @Param("price") Double price, @Param("vid") Long vid, @Param("uid") Long uid);

    @Select("SELECT * FROM `order` WHERE id=#{id}")
    Order findOne(@Param("id") Long id);

    @Select("SELECT * FROM `order` WHERE uid=#{id}")
    List<Order> findAll(@Param("id") Long id);
}
