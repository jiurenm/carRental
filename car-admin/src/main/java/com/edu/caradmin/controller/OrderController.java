package com.edu.caradmin.controller;

import com.edu.car.dto.Results;
import com.edu.car.model.Car;
import com.edu.car.model.Order;
import com.edu.car.model.Vehicle;
import com.edu.caradmin.service.CarService;
import com.edu.caradmin.service.CustomerService;
import com.edu.caradmin.service.OrderService;
import com.edu.caradmin.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/4/9 21:10
 */
@RestController
@RequestMapping(value = "/order")
public class OrderController {
    private final VehicleService vehicleService;
    private final CustomerService customerService;
    private final CarService carService;
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService, CarService carService, CustomerService customerService, VehicleService vehicleService) {
        this.orderService = orderService;
        this.carService = carService;
        this.customerService = customerService;
        this.vehicleService = vehicleService;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Results all() {
        List<Order> orders = orderService.all();
        List result = new ArrayList();
        for (Order order:orders) {
            Vehicle vehicle = vehicleService.one(order.getVid());
            Map map = new HashMap(5);
            map.put("order", order);
            map.put("user", customerService.findCustomerById(Long.valueOf(order.getUid())));
            map.put("car", vehicle);
            map.put("type", carService.findTypeById(Long.valueOf(vehicle.getType())));
            result.add(map);
        }
        return new Results().pageSuccess(result);
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.GET)
    public Results cancel() {
        return new Results().success(null);
    }
}
