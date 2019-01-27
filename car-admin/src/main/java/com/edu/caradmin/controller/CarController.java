package com.edu.caradmin.controller;

import com.edu.car.dto.Results;
import com.edu.car.model.Car;
import com.edu.caradmin.dto.CarTypeDto;
import com.edu.caradmin.dto.PageDto;
import com.edu.caradmin.service.CarService;
import com.edu.caradmin.util.OssUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * CarController
 *
 * @author Administrator
 * @date 2018/12/29 17:44
 */
@Api(value = "CarController", description = "车辆")
@RestController
@RequestMapping(value = "/type")
public class CarController {
    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @ApiOperation(value = "获取全部车型信息")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Results showCar() {
        List<Car> carList = carService.showCar();
        if (carList.isEmpty()) {
            return new Results().failed("没有记录");
        } else {
            return new Results().pageSuccess(carList);
        }
    }

    @ApiOperation(value = "分页获取车型信息")
    @ApiParam(name = "pageDto", value = "页码", required = true, type = "PageDto")
    @RequestMapping(value = "/carList", method = RequestMethod.POST)
    public Results findList(@RequestBody @Validated PageDto pageDto, BindingResult result) {
        if (result.hasErrors()) {
            return new Results().validateFailed(result);
        }
        PageHelper.startPage(pageDto.getPageNum(), pageDto.getPageSize());
        List<Car> carList = carService.showCar();
        if (carList.isEmpty()) {
            return new Results().failed("没有记录");
        } else {
            PageInfo<Car> pageInfo = new PageInfo<>(carList);
            return new Results().pageSuccess(pageInfo.getList());
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Results addCarType(@RequestBody CarTypeDto carTypeDto) {
        int result = carService.addType(carTypeDto);
        if (result == 1) {
            return new Results().success(result);
        } else {
            return new Results().failed();
        }
    }

    @RequestMapping(value = "/addPicture", method = RequestMethod.POST)
    public Object addPicture(MultipartFile file) {
        return OssUtil.getUrl(file);
    }

    @ApiOperation(value = "删除车型")
    @ApiImplicitParam(name = "id", value = "车型id", required = true, dataType = "String")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public Results deleteType(@PathVariable String id) {
        Optional.of(id).map(a -> carService.findTypeById(Long.valueOf(a))).orElseThrow(() -> new RuntimeException("车型不存在"));
        int result = carService.deleteType(Long.valueOf(id));
        if (result > 0) {
            return new Results().success(result);
        } else {
            return new Results().failed();
        }
    }

    @ApiOperation(value = "更新")
    @ApiParam(name = "carTypeDto", value = "车型", required = true, type = "CarTypeDto")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Results updateType(@RequestBody @Validated CarTypeDto carTypeDto, BindingResult result) {
        if (result.hasErrors()) {
            return new Results().validateFailed(result);
        }
        int ret = carService.updateType(carTypeDto);
        if (ret == 1) {
            return new Results().success(ret);
        } else {
            return new Results().failed();
        }
    }
}
