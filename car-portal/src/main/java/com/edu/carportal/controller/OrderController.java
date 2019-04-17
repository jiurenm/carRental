package com.edu.carportal.controller;

import com.edu.car.dto.Results;
import com.edu.car.model.Car;
import com.edu.car.model.Customer;
import com.edu.car.model.Order;
import com.edu.car.uid.IdWorker;
import com.edu.car.util.JwtTokenUtil;
import com.edu.carportal.dto.OrderDto;
import com.edu.carportal.service.CarService;
import com.edu.carportal.service.LoginService;
import com.edu.carportal.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/4/7 18:49
 */
@RestController
@RequestMapping(value = "/order")
public class OrderController {
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    private final CarService carService;
    private final LoginService loginService;
    private final JwtTokenUtil jwtTokenUtil;
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService, JwtTokenUtil jwtTokenUtil, LoginService loginService, CarService carService) {
        this.orderService = orderService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.loginService = loginService;
        this.carService = carService;
    }

    @RequestMapping(value = "/reserve", method = RequestMethod.POST)
    public Results reserve(@RequestBody OrderDto orderDto, HttpServletRequest request) {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader(this.tokenHeader).substring(this.tokenHead.length()));
        Customer customer = loginService.findCustomerByName(username);
        Long id = IdWorker.getId();
        orderService.insert(id, orderDto.getCid(), orderDto.getGetTime(),
                orderDto.getReturnTime(), orderDto.getPrice(), customer.getId());
        return new Results().success(id.toString());
    }

    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public Results findOne(@PathVariable String id) {
        return new Results().success(orderService.findOne(Long.valueOf(id)));
    }

    @RequestMapping(value = "/number/{id}", method = RequestMethod.GET)
    public Results findNumber(@PathVariable String id) {
        return new Results().success(orderService.findNumber(Long.valueOf(id)));
    }

    @RequestMapping(value = "/pay/{id}", method = RequestMethod.GET)
    public Results pay(@PathVariable String id) {
        return new Results().success(orderService.pay(Long.valueOf(id)));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Results findAll(HttpServletRequest request) {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader(this.tokenHeader).substring(this.tokenHead.length()));
        Customer customer = loginService.findCustomerByName(username);
        List<Order> orders = orderService.findAll(Long.valueOf(customer.getId()));
        List result = new ArrayList();
        for (Order order:orders) {
            Car car = carService.carDetails(order.getVid());
            List infos = new ArrayList();
            Map info = new HashMap(5);
            info.put("picture", car.getPicture());
            info.put("id", order.getId());
            info.put("cid", car.getId());
            info.put("status", getStatus(order.getStatus()));
            info.put("zws", car.getCarDetails().get(0).getZws());
            infos.add(info);
            String[] time = {order.getGetTime(), order.getReturnTime()};
            Map map = new HashMap(5);
            map.put("info", infos);
            map.put("time", time);
            map.put("price", order.getPrice());
            map.put("status", getStatus(order.getStatus()));
            result.add(map);
        }
            return new Results().success(result);
    }

    private String getStatus(Integer status) {
        switch (status) {
            case 1:
                return "等待付款";
            case 2:
                return "预定成功";
            case 3:
                return "租赁中";
            case 4:
                return "已完成";
            default:
                return "已取消";
        }
    }
}
