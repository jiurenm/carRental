package com.edu.caradmin.controller;

import com.edu.car.dto.Results;
import com.edu.car.model.Carousel;
import com.edu.caradmin.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/4/20 20:28
 */
@RestController
@RequestMapping(value = "/carousel")
public class CarouselController {
    private final CarouselService carouselService;

    @Autowired
    public CarouselController(CarouselService carouselService) {
        this.carouselService = carouselService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Results list() {
        return new Results().success(carouselService.list());
    }

    @RequestMapping(value = "/change", method = RequestMethod.POST)
    public Results change(@RequestBody Carousel carousel) {
        return new Results().success(carouselService.insert(carousel));
    }
}
