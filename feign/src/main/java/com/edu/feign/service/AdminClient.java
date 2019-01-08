package com.edu.feign.service;

import com.edu.car.dto.LoginDto;
import com.edu.car.dto.Results;
import com.edu.feign.service.hystric.AdminClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/1 13:00
 */
@FeignClient(value = "CAR-ADMIN", fallback = AdminClientFallBack.class)
public interface AdminClient {
    @RequestMapping(value = "/adminLogin", method = RequestMethod.POST)
    Results login(@RequestBody LoginDto loginDto);

    @RequestMapping(value = "/token/refresh", method = RequestMethod.GET)
    Results refreshToken();

    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    Results showCustomers();

    @RequestMapping(value = "/findCustomer/{username}", method = RequestMethod.POST)
    Results findCustomer(@PathVariable String username);

    @RequestMapping(value = "/setBlackList/{id}", method = RequestMethod.POST)
    Results setBlackList(@PathVariable String id);

    @RequestMapping(value = "/customerList", method = RequestMethod.POST)
    Results findList(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize);
}
