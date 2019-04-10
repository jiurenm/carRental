package com.edu.carportal.service;


import com.edu.car.model.Order;
import com.edu.car.model.Vehicle;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/4/7 18:44
 */
public interface OrderService {
    int insert(Long id, String cid, String getTime, String returnTime, Double price, String uid);
    Order findOne(Long id);
    Vehicle findNumber(Long id);
    List<Order> findAll(Long id);
    int pay(Long id);
}
