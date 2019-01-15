package com.edu.caradmin.controller;

import com.alibaba.fastjson.JSONObject;
import com.edu.car.dto.Results;
import com.edu.car.model.Customer;
import com.edu.car.model.Role;
import com.edu.car.redis.RedisTool;
import com.edu.car.uid.IdWorker;
import com.edu.caradmin.dto.AuthorityDto;
import com.edu.caradmin.dto.PageDto;
import com.edu.caradmin.service.CustomerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import java.util.*;

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
    private static final String ADMIN = "ROLE_ADMIN";
    private static final String USER = "ROLE_USER";

    private final Jedis jedis;
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService, Jedis jedis) {
        this.customerService = customerService;
        this.jedis = jedis;
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
        String lockKey = "blackList_key";
        if (id == null) {
            return new Results().validateFailed("id不能为空");
        }
        Customer customer = customerService.findCustomerById(Long.valueOf(id));
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
            customerService.setBlackList(Long.valueOf(id));
            if (RedisTool.releaseDistributedLock(jedis, lockKey, requestId)) {
                return new Results().success(id);
            } else {
                return new Results().success("释放锁失败：" + lockKey + " : " + requestId);
            }
        }
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
        if (id == null) {
            return new Results().validateFailed("id不能为空");
        }
        List<Role> roles = customerService.showRoles(Long.valueOf(id));
        if (roles == null) {
            return new Results().failed();
        } else {
            List<Role> roleList = Lists.newArrayList();
            roles.forEach(role -> {
                if (ADMIN.equals(role.getName())) {
                    role.setName("管理员");
                }
                if (USER.equals(role.getName())) {
                    role.setName("用户");
                }
                roleList.add(role);
            });
            return new Results().success(roleList);
        }
    }

    @ApiOperation(value = "添加权限")
    @ApiParam(name = "authorityDto", value = "权限", required = true, type = "AuthorityDto")
    @PreAuthorize(value = "hasRole('ADMIN')")
    @RequestMapping(value = "/addAuthority", method = RequestMethod.POST)
    public Results addAuthority(@RequestBody String authorityDto) {
        List<AuthorityDto> authorityList = JSONObject.parseArray(authorityDto, AuthorityDto.class);
        String lockKey = "addAuthority_key";
        String requestId = UUID.randomUUID().toString();
        if (RedisTool.tryGetDistributedLock(jedis, lockKey, requestId, EXPIRE_TIME)) {
            return new Results().failed("操作太快");
        } else {
            try {
                Long id = IdWorker.getId();
                authorityList.forEach(authority -> {
                    Role role = customerService.findRoleById(Long.valueOf(authority.getUid()), authority.getRid());
                    if (role != null) {
                        return;
                    }
                    customerService.addAuthority(id, Long.valueOf(authority.getUid()), authority.getRid());
                });
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

    @ApiOperation(value = "删除权限")
    @ApiParam(name = "authorityDto", value = "权限", required = true, type = "AuthorityDto")
    @PreAuthorize(value = "hasRole('ADMIN')")
    @RequestMapping(value = "/deleteAuthority", method = RequestMethod.POST)
    public Results deleteAuthority(@RequestBody String authorityDto) {
        Set<Role> set = Sets.newHashSet();
        List<AuthorityDto> authorityList = JSONObject.parseArray(authorityDto, AuthorityDto.class);
        final Long[] uid = new Long[1];
        authorityList.forEach(authority -> {
            uid[0] = Long.valueOf(authority.getUid());
            Role role = new Role();
            role.setId(authority.getUid());
            role.setRid(authority.getRid());
            role.setName(customerService.showRolesById(authority.getRid()).getName());
            set.add(role);
        });
        List<Role> roles = customerService.showRoles(uid[0]);
        Set<Role> roleSet = new HashSet<>(roles);
        ImmutableList<Role> difference = Sets.difference(roleSet, set).immutableCopy().asList();
        if (difference.isEmpty()) {
            return new Results().failed("没有相关权限");
        }
        String lockKey = "deleteAuthority_key";
        String requestId = UUID.randomUUID().toString();
        if (RedisTool.tryGetDistributedLock(jedis, lockKey, requestId, EXPIRE_TIME)) {
            return new Results().failed("操作太快");
        } else {
            try {
                difference.forEach(role -> customerService.deleteAuthority(Long.valueOf(role.getId()),role.getRid()));
                if (RedisTool.releaseDistributedLock(jedis, lockKey, requestId)) {
                    return new Results( ).success("成功");
                } else {
                    return new Results().success("释放锁失败：" + lockKey + " : " + requestId);
                }
            } catch (Exception e) {
                return new Results().failed(e);
            }
        }
    }

    public Results editCustomer() {
        return null;
    }
}
