package com.edu.car.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * carRental
 *
 * @author Administrator
 * @date 2018/12/29 23:12
 */
@Getter
@Setter
@ToString
public class LoginDto {
    @NotEmpty(message = "用户名不能为空")
    private String username;

    @NotEmpty(message = "密码不能为空")
    private String password;
}