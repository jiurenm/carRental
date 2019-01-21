package com.edu.feign.controller;

import com.edu.feign.dto.Results;
import com.edu.feign.service.AdminClient;
import feign.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/7 15:26
 */
@RestController
public class CustomerController {
    private final AdminClient adminClient;

    @Autowired
    public CustomerController(AdminClient adminClient) {
        this.adminClient = adminClient;
    }

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public Results showCustomers() {
        return adminClient.showCustomers();
    }

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @RequestMapping(value = "/findCustomer/{username}", method = RequestMethod.POST)
    public Results findCustomer(@PathVariable String username) {
        return adminClient.findCustomer(username);
    }

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @RequestMapping(value = "/setBlackList/{id}", method = RequestMethod.POST)
    public Results setBlackList(@PathVariable String id) {
        return adminClient.setBlackList(id);
    }

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @RequestMapping(value = "/customerList", method = RequestMethod.POST)
    public Results findList(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        return adminClient.findList(pageNum, pageSize);
    }
}
