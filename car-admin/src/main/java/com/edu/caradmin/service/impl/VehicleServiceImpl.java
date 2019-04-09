package com.edu.caradmin.service.impl;

import com.edu.car.mapper.CarMapper;
import com.edu.car.mapper.VehicleMapper;
import com.edu.car.model.Vehicle;
import com.edu.car.uid.IdWorker;
import com.edu.caradmin.dto.CarDto;
import com.edu.caradmin.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/4/8 21:06
 */
@Service
public class VehicleServiceImpl implements VehicleService {
    private final CarMapper carMapper;
    private final VehicleMapper vehicleMapper;

    @Autowired
    public VehicleServiceImpl(VehicleMapper vehicleMapper, CarMapper carMapper) {
        this.vehicleMapper = vehicleMapper;
        this.carMapper = carMapper;
    }

    @Override
    public List<Vehicle> showVehicle() {
        return vehicleMapper.showVehicle();
    }

    @Override
    public int delete(String id) {
        return vehicleMapper.delete(Long.valueOf(id));
    }

    @Override
    public int add(CarDto carDto) {
        String cid = carMapper.showCarByName(carDto.getType()).getId();
        return vehicleMapper.insert(IdWorker.getId(), carDto.getNumber(), Long.valueOf(cid));
    }

    @Override
    public int update(CarDto carDto) {
        String cid = carMapper.showCarByName(carDto.getType()).getId();
        return vehicleMapper.update(Long.valueOf(carDto.getId()), carDto.getNumber(), Long.valueOf(cid));
    }

    @Override
    public Vehicle one(String id) {
        return vehicleMapper.findOne(Long.valueOf(id));
    }
}
