package com.edu.carportal.controller;

import com.edu.car.dto.Results;
import com.edu.car.model.Car;
import com.edu.carportal.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/3/11 11:05
 */
@RestController
@RequestMapping(value = "/car")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Results showCar() {
        List<Car> carList = carService.showCar()
                .stream()
                .filter(car -> !car.getVehicles().isEmpty())
                .collect(Collectors.toList());
        if (carList.isEmpty()) {
            return new Results().failed();
        } else {
            return new Results().success(carList);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Results carDetails(@PathVariable String id) {
        Car car = carService.carDetails(id);
        return new Results().success(car);
    }
}
