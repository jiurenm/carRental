package com.edu.caradmin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/4 13:44
 */
@Data
@ApiModel
public class LoginDto {
    @NotEmpty(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名")
    private String username;
    @NotEmpty(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;
}
