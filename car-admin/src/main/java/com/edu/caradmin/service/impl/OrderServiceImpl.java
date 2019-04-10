package com.edu.caradmin.service.impl;

import com.edu.car.mapper.VehicleMapper;
import com.edu.car.model.Order;
import com.edu.car.model.Vehicle;
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

    private final VehicleMapper vehicleMapper;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper, VehicleMapper vehicleMapper) {
        this.orderMapper = orderMapper;
        this.vehicleMapper = vehicleMapper;
    }

    @Override
    public List<Order> all() {
        return orderMapper.all();
    }

    @Override
    public int get(String id) {
        return orderMapper.get(Long.valueOf(id));
    }

    @Override
    public int complete(String id) {
        return 0;
    }

    @Override
    public int cancel(String id) {
        Order order = orderMapper.one(Long.valueOf(id));
        vehicleMapper.retrun(Long.valueOf(order.getVid()));
        return orderMapper.cancel(Long.valueOf(id));
    }
}
