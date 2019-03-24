package com.edu.carportal.controller;

import com.edu.car.dto.Results;
import com.edu.car.model.Car;
import com.edu.carportal.dto.SearchDto;
import com.edu.carportal.service.CarService;
import com.edu.carportal.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/3/24 17:04
 */
@RestController
@RequestMapping(value = "/search")
public class SearchController {

    private final CarService carService;
    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService, CarService carService) {
        this.searchService = searchService;
        this.carService = carService;
    }

    @RequestMapping(value = "/cx", method = RequestMethod.GET)
    public Results findAllCX() {
        List<Car> cx = searchService.findAllCX();
        HashSet set = new HashSet();
        cx.forEach( name -> set.add(name.getCx()));
        return new Results().success(set);
    }

    @RequestMapping(value = "/pp", method = RequestMethod.GET)
    public Results findAllPP() {
        List<Car> cx = searchService.findAllPP();
        HashSet set = new HashSet();
        cx.forEach( name -> set.add(name.getPp()));
        return new Results().success(set);
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Results search(@RequestBody SearchDto searchDto) {
        if ("".equals(searchDto.getParam())) {
            return new Results().success(carService.showCar());
        }
        String[] params = searchDto.getParam().split(",");
        List<Car> cars = searchService.search(params);
        return new Results().success(cars);
    }
}
