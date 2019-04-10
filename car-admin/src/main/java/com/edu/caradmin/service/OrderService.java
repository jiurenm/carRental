package com.edu.caradmin.service;

import com.edu.car.model.Order;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/4/9 21:09
 */
public interface OrderService {
    List<Order> all();
    int get(String id);
    int complete(String id);
    int cancel(String id);
}
