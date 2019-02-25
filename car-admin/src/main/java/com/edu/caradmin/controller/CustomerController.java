package com.edu.caradmin.controller;

import com.alibaba.fastjson.JSONObject;
import com.edu.car.dto.Results;
import com.edu.car.model.Customer;
import com.edu.car.model.Role;
import com.edu.caradmin.dto.AuthorityDto;
import com.edu.caradmin.dto.CustomerDto;
import com.edu.caradmin.dto.PageDto;
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

import java.util.*;
import java.util.stream.Collectors;

/**
 * CustomerController
 *
 * @author Administrator
 * @date 2019/1/3 10:58
 */
@Slf4j
@Api(value = "CustomerController", description = "用户")
@RestController
public class CustomerController {
    private static final String ADMIN = "ROLE_ADMIN";
    private static final String USER = "ROLE_USER";

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @ApiOperation(value = "获取全部用户信息")
    @PreAuthorize(value = "hasRole('ADMIN')")
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
    @PreAuthorize(value = "hasRole('ADMIN')")
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
    @PreAuthorize(value = "hasRole('ADMIN')")
    @RequestMapping(value = "/setBlackList/{id}", method = RequestMethod.GET)
    public Results setBlackList(@PathVariable String id) {
        Optional<Customer> customer = getCustomer(id);
        Results results = new Results();
        customer.ifPresent(c -> {
            if (c.getIsEnable()) {
                int result = customerService.setBlackList(Long.valueOf(id));
                results.success(result);
            } else {
                results.failed("不能重复操作");
            }
        });
        return results;
    }

    @ApiOperation(value = "分页获取用户信息")
    @ApiParam(name = "pageDto", value = "页码", required = true, type = "PageDto")
    @PreAuthorize(value = "hasRole('ADMIN')")
    @RequestMapping(value = "/customerList", method = RequestMethod.POST)
    public Results findList(@RequestBody @Validated PageDto pageDto, BindingResult result) {
        if (result.hasErrors()) {
            return new Results().validateFailed(result);
        }
        PageHelper.startPage(pageDto.getPageNum(), pageDto.getPageSize());
        List<Customer> customers = customerService.showCustomers();
        if (customers.isEmpty()) {
            return new Results().failed("没有记录");
        }
        PageInfo<Customer> pageInfo = new PageInfo<>(customers);
        return new Results().pageSuccess(pageInfo.getList());
    }

    @ApiOperation(value = "获取用户权限")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "String")
    @PreAuthorize(value = "hasRole('ADMIN')")
    @RequestMapping(value = "/roles/{id}", method = RequestMethod.GET)
    public Results showRoles(@PathVariable String id) {
        List<Role> roles = Optional.ofNullable(id).map(a -> customerService.showRoles(Long.valueOf(a))).get();
        if (roles.isEmpty()) {
            return new Results().failed();
        } else {
            List<Role> roleList = roles.stream().map((role) -> {
                if (ADMIN.equals(role.getName())) {
                    role.setName("管理员");
                    return role;
                }
                if (USER.equals(role.getName())) {
                    role.setName("用户");
                    return role;
                }
                return role;
            }).collect(Collectors.toList());
            return new Results().success(roleList);
        }
    }

    @ApiOperation(value = "添加权限")
    @ApiParam(name = "authorityDto", value = "权限", required = true, type = "AuthorityDto")
    @PreAuthorize(value = "hasRole('ADMIN')")
    @RequestMapping(value = "/addAuthority", method = RequestMethod.POST)
    public Results addAuthority(@RequestBody String authorityDto) {
        List<AuthorityDto> authorityList = JSONObject.parseArray(authorityDto, AuthorityDto.class);
        int result = customerService.addAuthority(authorityList);
        if (result > 0) {
            return new Results().success(result);
        }
        return new Results().failed();
    }

    @ApiOperation(value = "删除权限")
    @ApiParam(name = "authorityDto", value = "权限", required = true, type = "AuthorityDto")
    @PreAuthorize(value = "hasRole('ADMIN')")
    @RequestMapping(value = "/deleteAuthority", method = RequestMethod.POST)
    public Results deleteAuthority(@RequestBody String authorityDto) {
        List<AuthorityDto> authorityList = JSONObject.parseArray(authorityDto, AuthorityDto.class);
        int result = customerService.deleteAuthority(authorityList);
        if (result > 0) {
            return new Results().success(result);
        }
        return new Results().failed();
    }

    @ApiOperation(value = "编辑信息")
    @ApiParam(name = "CustomerDto", value = "用户", required = true, type = "CustomerDto")
    @PreAuthorize(value = "hasRole('ADMIN')")
    @RequestMapping(value = "/editCustomer", method = RequestMethod.POST)
    public Results editCustomer(@RequestBody @Validated CustomerDto customerDto, BindingResult result) {
        if (result.hasErrors()) {
            return new Results().validateFailed(result);
        }
        int result1 = customerService.editCustomer(customerDto);
        if (result1 > 0) {
            return new Results().success(result);
        } else {
            return new Results().failed();
        }
    }

    @ApiOperation(value = "获取黑名单")
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/allBlackList", method = RequestMethod.GET)
    public Results blackList() {
        List<Customer> customer = customerService.blackList();
        if (customer.isEmpty()) {
            return new Results().success("没有记录");
        } else {
            return new Results().pageSuccess(customer);
        }
    }

    @ApiOperation(value = "分页获取黑名单")
    @ApiParam(name = "pageDto", value = "页码", required = true, type = "PageDto")
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/blackList", method = RequestMethod.POST)
    public Results blackList(@RequestBody @Validated PageDto pageDto, BindingResult result) {
        if (result.hasErrors()) {
            return new Results().validateFailed(result);
        }
        PageHelper.startPage(pageDto.getPageNum(), pageDto.getPageSize());
        List<Customer> customers = customerService.blackList();
        if (customers.isEmpty()) {
            return new Results().failed();
        }
        PageInfo<Customer> page = new PageInfo<>(customers);
        return new Results().pageSuccess(page.getList());
    }

    @ApiOperation(value = "取消黑名单")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "String")
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/cancelBlackList/{id}", method = RequestMethod.GET)
    public Results cancelBlackList(@PathVariable String id) {
        Optional<Customer> customer = getCustomer(id);
        Results results = new Results();
        customer.ifPresent(c -> {
            if (!c.getIsEnable()) {
                int result = customerService.cancelBlackList(Long.valueOf(id));
                results.success(result);
            } else {
                results.failed("不能重复操作");
            }
        });
        return results;
    }

    private Optional<Customer> getCustomer(@PathVariable String id) {
        Optional<Customer> customer = Optional.of(id).map(a -> customerService.findCustomerById(Long.valueOf(a)));
        customer.orElseThrow(() -> new RuntimeException("用户不存在"));
        return customer;
    }
}
