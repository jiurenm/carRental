package com.edu.caradmin.controller;

import com.edu.car.dto.Results;
import com.edu.car.model.Car;
import com.edu.caradmin.service.CarService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2018/12/29 17:44
 */
@Api(value = "CarController", description = "车辆")
@RestController
public class CarController {
    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @ApiOperation(value = "获取全部车辆信息")
    @RequestMapping(value = "/cars", method = RequestMethod.GET)
    public Results showCar() {
        List<Car> carList = carService.showCar();
        if (carList.isEmpty()) {
            return new Results().failed("没有记录");
        } else {
            return new Results().pageSuccess(carList);
        }
    }

    @ApiOperation(value = "分页获取车辆信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数据量", required = true, dataType = "int")
    })
    @RequestMapping(value = "/carList", method = RequestMethod.POST)
    public Results findList(Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageSize == null) {
            return new Results().validateFailed("页码或每页数据量不能为空");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Car> carList = carService.showCar();
        if (carList.isEmpty()) {
            return new Results().failed("没有记录");
        } else {
            PageInfo<Car> pageInfo = new PageInfo<>(carList);
            return new Results().pageSuccess(pageInfo.getList());
        }
    }
}
