package com.edu.carportal.service.impl;

import com.edu.car.mapper.CarMapper;
import com.edu.car.model.Car;
import com.edu.carportal.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/3/11 11:07
 */
@Service
public class CarServiceImpl implements CarService {

    private final CarMapper carMapper;

    @Autowired
    public CarServiceImpl(CarMapper carMapper) {
        this.carMapper = carMapper;
    }

    @Override
    public List<Car> showCar() {
        return carMapper.showCar();
    }
}
