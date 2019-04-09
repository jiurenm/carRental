package com.edu.caradmin.service;

import com.edu.car.model.Vehicle;
import com.edu.caradmin.dto.CarDto;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/4/8 21:05
 */
public interface VehicleService {
    List<Vehicle> showVehicle();
    int delete(String id);
    int add(CarDto carDto);
    int update(CarDto carDto);
    Vehicle one(String id);
}
