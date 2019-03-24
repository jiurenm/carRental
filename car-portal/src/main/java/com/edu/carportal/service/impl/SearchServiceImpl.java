package com.edu.carportal.service.impl;

import com.edu.car.model.Car;
import com.edu.carportal.dao.SearchMapper;
import com.edu.carportal.service.SearchService;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/3/24 17:09
 */
@Service
public class SearchServiceImpl implements SearchService {
    private final SearchMapper searchMapper;

    @Autowired
    public SearchServiceImpl(SearchMapper searchMapper) {
        this.searchMapper = searchMapper;
    }

    @Override
    public List<Car> findAllCX() {
        return searchMapper.findAllCX();
    }

    @Override
    public List<Car> findAllPP() {
        return searchMapper.findAllPP();
    }

    @Override
    public List<Car> search(String[] params) {
        List<Car> cars = new ArrayList<>();
        List<Car> cx = new ArrayList<>();
        List<Car> pp = new ArrayList<>();
        for (String param : params) {
            cx.addAll(searchMapper.searchByCx(param));
            pp.addAll(searchMapper.searchByPp(param));
        }
        if (cx.isEmpty()) {
            return pp;
        }
        if (pp.isEmpty()) {
            return cx;
        }
        for (String param : params) {
            cars.addAll(cx.stream().filter(car -> param.equals(car.getPp())).collect(Collectors.toList()));
        }
        return cars;
    }
}
