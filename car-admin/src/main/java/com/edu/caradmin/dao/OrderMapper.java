package com.edu.caradmin.dao;

import com.edu.car.model.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}
