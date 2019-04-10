package com.edu.caradmin.dao;

import com.edu.car.model.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/4/9 21:07
 */
@Mapper
public interface OrderMapper {
    @Select("SELECT * FROM `order`")
    List<Order> all();

    @Select("SELECT * FROM `order` WHERE id=#{id}")
    Order one(@Param("id") Long id);

    @Update("UPDATE `order` SET `status`=3 WHERE id=#{id}")
    int get(@Param("id") Long id);

    @Update("UPDATE `order` SET `status`=4 WHERE id=#{id}")
    int complete(@Param("id") Long id);

    @Update("UPDATE `order` SET `status`=5 WHERE id=#{id}")
    int cancel(@Param("id") Long id);
}
