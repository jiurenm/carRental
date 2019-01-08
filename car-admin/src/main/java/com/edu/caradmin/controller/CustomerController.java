package com.edu.caradmin.controller;

import com.edu.car.dto.Results;
import com.edu.car.model.Customer;
import com.edu.caradmin.redis.RedisTool;
import com.edu.caradmin.service.CustomerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.UUID;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/3 10:58
 */
@Slf4j
@Api(value = "CustomerController", description = "用户")
@RestController
public class CustomerController {
    private static final String LOCK_KEY = "Lock";
    private static final String REQUEST_ID = UUID.randomUUID().toString();
    private static final int EXPIRE_TIME = 100;

    private final Jedis jedis;
    private final RedisTool redisTool;
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService, RedisTool redisTool, Jedis jedis) {
        this.customerService = customerService;
        this.redisTool = redisTool;
        this.jedis = jedis;
    }

    @ApiOperation(value = "获取全部用户信息")
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public Results showCustomers() {
        List<Customer> customers = customerService.showCustomers();
        if(!customers.isEmpty()) {
            return new Results().pageSuccess(customers);
        } else {
            return new Results().failed();
        }
    }

    @ApiOperation(value = "通过用户名获取用户信息")
    @ApiImplicitParam(name = "username", value = "用户名",required = true, dataType = "String")
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/findCustomer/{username}", method = RequestMethod.POST)
    public Results findCustomer(@PathVariable String username) {
        if (Strings.isNullOrEmpty(username)) {
            return new Results().validateFailed("用户名不能为空");
        }
        Customer customer = customerService.findCustomerByName(username);
        if (customer != null) {
            return new Results().success(customer);
        } else {
            return new Results().failed("用户不存在");
        }
    }

    @ApiOperation(value = "设置黑名单")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int")
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/setBlackList/{id}", method = RequestMethod.POST)
    public Results setBlackList(@PathVariable Long id) {
        if (id == null) {
            return new Results().validateFailed("id不能为空");
        }
        Customer customer = customerService.findCustomerById(id);
        if (customer == null) {
            return new Results().failed("用户不存在");
        }
        if (!customer.getIsEnable()) {
            return new Results().failed("不能重复操作");
        }
        if (!redisTool.tryGetDistributedLock(jedis, LOCK_KEY, REQUEST_ID, EXPIRE_TIME)) {
            return new Results().failed("操作太快");
        } else {
            customerService.setBlackList(id);
            if (redisTool.releaseDistributedLock(jedis, LOCK_KEY, REQUEST_ID)) {
                return new Results().success(id);
            } else {
                log.error("释放锁失败：{},{},{}", id, LOCK_KEY, REQUEST_ID);
                return new Results().success("释放锁失败：" + id);
            }
        }
    }

    @ApiOperation(value = "分页获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数据量", required = true, dataType = "int")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/customerList", method = RequestMethod.POST)
    public Results findList(Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageSize == null) {
            return new Results().validateFailed("页码或每页数据量不能为空");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Customer> customers = customerService.showCustomers();
        if (customers.isEmpty()) {
            return new Results().failed("没有记录");
        }
        PageInfo<Customer> pageInfo = new PageInfo<>(customers);
        return new Results().pageSuccess(pageInfo.getList());
    }
}
