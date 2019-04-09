package com.edu.caradmin.controller;

import com.edu.car.dto.Results;
import com.edu.car.model.Car;
import com.edu.caradmin.dto.CarDto;
import com.edu.caradmin.service.CarService;
import com.edu.caradmin.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/4/8 21:08
 */
@RestController
@RequestMapping(value = "/car")
public class VehicleController {
    private final CarService carService;
    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService, CarService carService) {
        this.vehicleService = vehicleService;
        this.carService = carService;
    }

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public Results show() {
        List cars = vehicleService.showVehicle().stream().peek(vehicle -> {
            String id = vehicle.getType();
            Car car = carService.findTypeById(Long.valueOf(id));
            vehicle.setType(car.getName());
        }).collect(Collectors.toList());
        return new Results().pageSuccess(cars);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public Results delete(@PathVariable String id) {
        return new Results().success(vehicleService.delete(id));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Results add(@RequestBody CarDto carDto) {
        return new Results().success(vehicleService.add(carDto));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Results update(@RequestBody CarDto carDto) {
        return new Results().success(vehicleService.update(carDto));
    }
}
