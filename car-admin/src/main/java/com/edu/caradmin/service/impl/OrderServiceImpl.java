package com.edu.caradmin.service.impl;

import com.edu.car.model.Order;
import com.edu.caradmin.dao.OrderMapper;
import com.edu.caradmin.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/4/9 21:09
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Override
    public List<Order> all() {
        return orderMapper.all();
    }
}
