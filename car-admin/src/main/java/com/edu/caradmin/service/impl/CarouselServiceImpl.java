package com.edu.caradmin.service.impl;

import com.edu.car.mapper.CarouselMapper;
import com.edu.car.model.Carousel;
import com.edu.caradmin.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/4/20 20:27
 */
@Service
public class CarouselServiceImpl implements CarouselService {
    private final CarouselMapper carouselMapper;

    @Autowired
    public CarouselServiceImpl(CarouselMapper carouselMapper) {
        this.carouselMapper = carouselMapper;
    }

    @Override
    public List<Carousel> list() {
        return carouselMapper.selectList(null);
    }

    @Override
    public int insert(Carousel carousel) {
        return carouselMapper.updateById(carousel);
    }
}
