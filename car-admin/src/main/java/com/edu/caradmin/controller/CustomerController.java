package com.edu.caradmin.controller;

import com.edu.car.dto.Results;
import com.edu.car.model.Customer;
import com.edu.car.model.Role;
import com.edu.car.redis.RedisTool;
import com.edu.car.uid.IdWorker;
import com.edu.caradmin.dto.AuthorityDto;
import com.edu.caradmin.service.CustomerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
    private static final int EXPIRE_TIME = 1000;

    private final Jedis jedis;
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService, Jedis jedis) {
        this.customerService = customerService;
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
        String lockKey = "blackList_key";
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
        String requestId = UUID.randomUUID().toString();
        if (RedisTool.tryGetDistributedLock(jedis, lockKey, requestId, EXPIRE_TIME)) {
            return new Results().failed("操作太快");
        } else {
            customerService.setBlackList(id);
            if (RedisTool.releaseDistributedLock(jedis, lockKey, requestId)) {
                return new Results().success(id);
            } else {
                return new Results().success("释放锁失败：" + lockKey + " : " + requestId);
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

    @ApiOperation(value = "添加权限")
    @ApiParam(required = true)
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/addAuthority", method = RequestMethod.POST)
    public Results addAuthority(@RequestBody @Validated AuthorityDto authorityDto, BindingResult result) {
        String lockKey = "addAuthority_key";
        if(result.hasErrors()) {
            return new Results().validateFailed(result);
        }
        String requestId = UUID.randomUUID().toString();
        if (RedisTool.tryGetDistributedLock(jedis, lockKey, requestId, EXPIRE_TIME)) {
            return new Results().failed("操作太快");
        } else {
            try {
                Long id = IdWorker.getId();
                Role role = customerService.findRoleById(authorityDto.getUid(), authorityDto.getRid());
                if (role != null) {
                    return new Results().failed("不能重复操作");
                }
                customerService.addAuthority(id, authorityDto.getUid(), authorityDto.getRid());
                if (RedisTool.releaseDistributedLock(jedis, lockKey, requestId)) {
                    return new Results().success(id);
                } else {
                    return new Results().success("释放锁失败：" + lockKey + " : " + requestId);
                }
            } catch (Exception e) {
                return new Results().failed(e.getMessage());
            }
        }
    }
}
