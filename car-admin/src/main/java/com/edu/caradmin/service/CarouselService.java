package com.edu.caradmin.service;

import com.edu.car.model.Carousel;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/4/20 20:26
 */
public interface CarouselService {
    List<Carousel> list();
    int insert(Carousel carousel);
}
