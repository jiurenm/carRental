package com.edu.carportal.service;

import com.edu.car.model.Car;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/3/24 17:08
 */
public interface SearchService {
    List<Car> findAllCX();
    List<Car> findAllPP();
    List<Car> search(String[] params);
}
