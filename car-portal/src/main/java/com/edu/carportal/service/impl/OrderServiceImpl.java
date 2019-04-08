package com.edu.carportal.service.impl;

import com.edu.car.mapper.VehicleMapper;
import com.edu.car.model.Order;
import com.edu.car.model.Vehicle;
import com.edu.carportal.dao.OrderMapper;
import com.edu.carportal.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/4/7 18:46
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
    public int insert(Long id, String cid, String getTime, String returnTime, Double price, String uid) {
        List<Vehicle> vehicles = vehicleMapper.findVehicle(Long.valueOf(cid));
        orderMapper.insert(id, getTime, returnTime, price, Long.valueOf(vehicles.get(0).getId()), Long.valueOf(uid));
        return vehicleMapper.rental(Long.valueOf(vehicles.get(0).getId()));
    }

    @Override
    public Order findOne(Long id) {
        return orderMapper.findOne(id);
    }

    @Override
    public Vehicle findNumber(Long id) {
        return vehicleMapper.findOne(id);
    }

    @Override
    public List<Order> findAll(Long id) {
        return orderMapper.findAll(id);
    }
}
