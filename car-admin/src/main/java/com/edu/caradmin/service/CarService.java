package com.edu.caradmin.service;

import com.edu.car.model.Car;
import com.edu.car.model.CarDetail;
import com.edu.caradmin.dto.CarTypeDto;

import java.util.List;

/**
 * CarService
 *
 * @author Administrator
 * @date 2019/1/4 13:12
 */
public interface CarService {
    Car findTypeById(Long id);
    List<Car> showCar();
    CarDetail showDetail(Long id);
    int addType(CarTypeDto carTypeDto);
    int deleteType(Long id);
    int updateType(CarTypeDto carTypeDto);
}
