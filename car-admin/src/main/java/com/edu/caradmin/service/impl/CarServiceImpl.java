package com.edu.caradmin.service.impl;

import com.edu.car.mapper.CarMapper;
import com.edu.car.model.Car;
import com.edu.caradmin.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/4 13:13
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
