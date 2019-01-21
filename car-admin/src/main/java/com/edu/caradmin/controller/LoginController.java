package com.edu.caradmin.controller;

import com.edu.car.dto.Results;
import com.edu.caradmin.dto.LoginDto;
import com.edu.caradmin.service.AdminService;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * LoginController
 *
 * @author Administrator
 * @date 2018/12/29 23:09
 */
@Slf4j
@Api(value = "LoginController", description = "登录")
@RestController
@RefreshScope
public class LoginController {
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    private final AdminService adminService;

    @Autowired
    public LoginController(AdminService adminService) {
        this.adminService = adminService;
    }

    @ApiOperation(value = "登录接口")
    @ApiParam(name = "用户", required = true)
    @RequestMapping(value = "/adminLogin", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    public Results login(@RequestBody @Validated LoginDto loginDto, BindingResult result) {
        if (result.hasErrors()) {
            return new Results().validateFailed(result);
        }
        String token = adminService.login(loginDto.getUsername(), loginDto.getPassword());
        if (Strings.isNullOrEmpty(token)) {
            return new Results().validateFailed("用户名或密码错误");
        }
        ImmutableMap<String, String> tokenMap = ImmutableMap.of("token", token, "tokenHead", tokenHead);
        return new Results().success(tokenMap);
    }

    @ApiOperation(value = "token刷新")
    @RequestMapping(value = "/token/refresh", method = RequestMethod.GET)
    public Results refreshToken(HttpServletRequest request) {
        String oldToken = request.getHeader(tokenHeader);
        if(Strings.isNullOrEmpty(oldToken)) {
            return new Results().unauthorized("token失效或未登录");
        }
        String newToken = adminService.refreshToken(oldToken);
        if(Strings.isNullOrEmpty(newToken)) {
            return new Results().failed();
        }
        ImmutableMap<String,String> tokenMap = ImmutableMap.of("token",newToken,"tokenHead",tokenHead);
        return new Results().success(tokenMap);
    }
}