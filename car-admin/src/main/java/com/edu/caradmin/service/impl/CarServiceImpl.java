package com.edu.caradmin.service.impl;

import com.edu.car.mapper.CarMapper;
import com.edu.car.model.Car;
import com.edu.caradmin.service.CarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/4 13:13
 */
@Slf4j
@Service
public class CarServiceImpl implements CarService {
    private final CarMapper carMapper;

    @Autowired
    public CarServiceImpl(CarMapper carMapper) {
        this.carMapper = carMapper;
    }

    @Override
    @Cacheable(value = "cars")
    public List<Car> showCar() {
        return carMapper.showCar();
    }
}
