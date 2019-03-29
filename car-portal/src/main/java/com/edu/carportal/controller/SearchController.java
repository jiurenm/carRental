package com.edu.carportal.controller;

import com.edu.car.dto.Results;
import com.edu.car.model.Car;
import com.edu.carportal.dto.SearchDto;
import com.edu.carportal.service.CarService;
import com.edu.carportal.service.SearchService;
import com.google.common.base.Splitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
        if (cars.isEmpty()) {
            return new Results().success(filter(carService.showCar(), params));
        }
        return new Results().success(cars);
    }

    private List<Car> filter(List<Car> cars, String[] params) {
        List<List<String>> price = new ArrayList();
        List<Car> newCars = new ArrayList<>();
        for (String param:params) {
            if ("0-150".equals(param) || "150-300".equals(param) ||
                    "300-500".equals(param) || "500-1000".equals(param)) {
                List<String> list = Splitter.on("-").omitEmptyStrings().trimResults().splitToList(param);
                price.add(list);
            }
        }
        price.forEach(param -> {
            Double low = Double.valueOf(param.get(0));
            Double high = Double.valueOf(param.get(1));
            newCars.addAll(cars.stream().filter(car -> low<car.getPrice().get(0).getShortTime()&&car.getPrice().get(0).getShortTime()<high).collect(Collectors.toList()));
        });
        return newCars;
    }
}
