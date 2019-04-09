package com.edu.feign.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/4 13:44
 */
@Data
public class LoginDto {
    @NotEmpty(message = "用户名不能为空")
    private String username;
    @NotEmpty(message = "密码不能为空")
    private String password;
}
